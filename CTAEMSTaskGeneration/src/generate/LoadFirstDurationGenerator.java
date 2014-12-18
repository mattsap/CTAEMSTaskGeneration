package generate;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import project.Method;

public class LoadFirstDurationGenerator extends MethodGenerator {

	private IDurationDistribution durationDist;
	public void setDurationDist(IDurationDistribution msd) { durationDist = msd; }
	
	private ILoadDistribution loadDist;
	public void setLoadDist(ILoadDistribution ld) { loadDist = ld; }
	
	private IRewardDistribution rewardDist;
	public void setRewardDist(IRewardDistribution rd) { rewardDist = rd; }
	
	private ITimePressureDistribution timePressureDist;
	public void setTimePressureDist(ITimePressureDistribution tpd) { timePressureDist = tpd; }
	
	@Override
	public void generate() {
		
		List<TimeSlot> timeSlots = computeTimeSlots();
		fitMethodsToTimeSlots(timeSlots);
	}
	
	class TimeSlot {
		public int start, end;
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
	
	private void fitMethodsToTimeSlots(List<TimeSlot> timeSlots) {
		for (TimeSlot ts : timeSlots) {
			List<Method> best = getBestMatchOfTimeSlot(ts.start, ts.end);
			if (best != null)
				generated.addAll(best);
		}
	}
	
	private Random random = new Random();
	private List<Method> getBestMatchOfTimeSlot(int start, int end) {
		
		List<Method> ret = null;
		int timeUnitsUsed = 0;
		
		for (int s = start; s < end; s++) {
			int duration = Math.max(1, durationDist.getDurationAtArrivalTime(s));
			double timePressure = timePressureDist.getTimePressureFor(s);
			int makeSpan = Math.max(1, duration + (int)(duration * timePressure));
			
			if (s + makeSpan > end)
				continue;
			
			List<Method> best = getBestMatchOfTimeSlot(s+makeSpan, end);
			
			int sum = makeSpan;
			if (best != null)
				for (Method m : best)
					sum += m.getDeadline() - m.getArrivalTime();
			
			if (sum > timeUnitsUsed || (sum == timeUnitsUsed && random.nextBoolean())) {
				timeUnitsUsed = sum;
				ret = best != null ? best : new ArrayList<Method>();
				Method m = new Method();
				m.setArrivalTime(s);
				m.setReleaseTime(s);
				m.setDeadline(s + makeSpan);
				m.setDuration(duration);
				m.setReward(rewardDist.getRewardWithArrivalTime(s));
				ret.add(m);
			}
				
		}
		
		return ret;
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
