package generate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import project.Method;

public class LoadFirstGenerator extends MethodGenerator {
	
	private Random random = new Random();
	
	private IMakeSpanDistribution makespanDist;
	public void setMakeSpanDist(IMakeSpanDistribution msd) { makespanDist = msd; }
	
	private ILoadDistribution loadDist;
	public void setLoadDist(ILoadDistribution ld) { loadDist = ld; }
	
	private IRewardDistribution rewardDist;
	public void setRewardDist(IRewardDistribution rd) { rewardDist = rd; }
	
	private ITimePressureDistribution timePressureDist;
	public void setTimePressureDist(ITimePressureDistribution tpd) { timePressureDist = tpd; }
	
	@Override
	public void generate() {
		
		generated.clear();
		
		List<TimeSlot> timeSlots = computeTimeSlots();
		List<Integer> makespanSample = createSampleOfMakeSpans();
		fitTimeSlotsWithMakeSpanSample(timeSlots, makespanSample);
		createMethodsFromTimeSlots(timeSlots);
		
	}
	
	class TimeSlot {
		public int start, end, duration;
		public ArrayList<Integer> makeSpans = new ArrayList<Integer>();
		@Override
		public String toString() {
			return "" + start + "->" + end;
		}
	}
	
	private List<TimeSlot> computeTimeSlots() {
		List<TimeSlot> times = new ArrayList<TimeSlot>();
		int[] loadUsed = new int[loadDist.getTimeScale()];
		int k = 0;
		while (k < loadDist.getTimeScale()) {
			if (loadDist.getLoadAtTimeUnit(k) <= loadUsed[k]) { 
				k++; 
				continue; 
			} 
			
			TimeSlot t = new TimeSlot();
			t.start = k;
			int j = k;
			for (; j < loadDist.getTimeScale(); j++) {
				if (loadDist.getLoadAtTimeUnit(j) <= loadUsed[j]) break;
				loadUsed[j]++;
			}
			t.end = j;
			times.add(t);
		}
		
		return times;
	}
	
	private List<Integer> createSampleOfMakeSpans() {
		//  Compute total timeunits that are available from load distribution
		int timeUnits = 0;
		for (int i = 0; i < loadDist.getTimeScale(); i++)
			timeUnits += loadDist.getLoadAtTimeUnit(i);
		
		//  Obtain a sample of make spans that will cover all available timeUnits
		ArrayList<Integer> makespanSamples = new ArrayList<Integer>();
		int sum = 0;
		while (sum < timeUnits) {
			int sample = makespanDist.getSampleMakeSpan();
			sum += sample;
			makespanSamples.add(sample);
		}
		return makespanSamples;
	}
	
	private void fitTimeSlotsWithMakeSpanSample(List<TimeSlot> timeSlots, List<Integer> makespanSample) {
		//  Sort makespanSamples to fit the largest make span times first
		Collections.sort(makespanSample);
		
		//  Greedly fit Open Times from largest to smallest in the TimeSlots
		for (int i = makespanSample.size() - 1; i >= 0; i--) {
			int randomMakeSpan = makespanSample.get(i);
			
			//  Search for a TimeSlot that has enough room for the make span
			Collections.shuffle(timeSlots);
			for (TimeSlot ts : timeSlots) {
				if (ts.end - ts.start - ts.duration >= randomMakeSpan) {
					ts.makeSpans.add(randomMakeSpan);
					ts.duration += randomMakeSpan;
					break;
				}
			}
		}
	}
	
	private void createMethodsFromTimeSlots(List<TimeSlot> timeSlots) {
		//  Generate the actual Methods out of the time slots
		//  For timeslots that aren't completely full, distribute
		//  the remaining time randomly through the timeslot
		int generatedCount = 0;
		for (TimeSlot ts : timeSlots) {
			Collections.shuffle(ts.makeSpans);
			int extra = ts.end - ts.start - ts.duration;
			int arrivalTime = ts.start;
			int partExtra;
			if (extra > 0) {
				partExtra = random.nextInt(extra+1);
				arrivalTime += partExtra;
				extra -= partExtra;
			}
			for (Integer makeSpan : ts.makeSpans) {
				final Method method = new Method();
				method.setArrivalTime(arrivalTime);
				method.setReleaseTime(arrivalTime);
				method.setDeadline(arrivalTime + makeSpan);
				
				int duration = (int)(makeSpan * timePressureDist.getTimePressureFor(arrivalTime));
				int reward = rewardDist.getRewardWithArrivalTime(arrivalTime);
				method.setDuration(duration);
				method.setReward(reward);
				
				arrivalTime += makeSpan;
				
				if (extra > 0) {
					partExtra = random.nextInt(extra+1);
					arrivalTime += partExtra;
					extra -= partExtra;
				}
				generated.add(method);
			}
		}
	}
	
	public int getTimeScale() {
		return loadDist.getTimeScale();
	}
	
	public double[] histInputLoad() {
		ArrayList<Integer> load = new ArrayList<Integer>();
		for (int i = 0; i < loadDist.getTimeScale(); i++) {
			for (int j = loadDist.getLoadAtTimeUnit(i); j > 0; j--) {
				load.add(i);
			}
		}
		double[] ret = new double[load.size()];
		for (int i = 0; i < load.size(); i++)
			ret[i] = load.get(i);
		return ret;
	}
	
	
	public double[] histInputTimePressure() {
		ArrayList<Integer> timePressure = new ArrayList<Integer>();
		for (int i = 0; i < loadDist.getTimeScale(); i++) {
			for (int j = (int)(100*timePressureDist.getTimePressureFor(i)); j > 0; j--) {
				timePressure.add(i);
			}
		}
		double[] ret = new double[timePressure.size()];
		for (int i = 0; i < timePressure.size(); i++)
			ret[i] = timePressure.get(i);
		return ret;
	}
	
	public double[] histInputReward() {
		ArrayList<Integer> reward = new ArrayList<Integer>();
		for (int i = 0; i < loadDist.getTimeScale(); i++) {
			for (int j = rewardDist.getRewardWithArrivalTime(i); j > 0; j--) {
				reward.add(i);
			}
		}
		double[] ret = new double[reward.size()];
		for (int i = 0; i < reward.size(); i++)
			ret[i] = reward.get(i);
		return ret;
	}
}
