package project;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


@Deprecated
public class GenerateLoad {

	public ArrayList<Method> generated = new ArrayList<Method>();
	
	public ArrayList<Method> aliveMethods = new ArrayList<Method>();
	public long time = 0;
	
	private double[] loadActual;
	private double[] opentimeActual;
	private double[] timepressureActual;
	private double[] rewardActual;
	
	private Random random = new Random();
	
	public void generateNextTimeStep(int load) {

		for (int i = aliveMethods.size()-1; i >= 0; i--) {
			if (aliveMethods.get(i).getDeadline() <= time) {
				generated.add(aliveMethods.remove(i));
			}
		}

		int generatedCount = 0;
		while (load > aliveMethods.size()) {
			long duration = random.nextInt(5) + 1;
			final Method method = new Method();
			method.setArrivalTime(this.time);
			method.setReleaseTime(this.time);
			method.setDuration(duration);
			method.setDeadline(this.time + duration);
			aliveMethods.add(method);
		}
		
		while (load < aliveMethods.size()) {
			Method method = aliveMethods.remove(random.nextInt(aliveMethods.size()));
			method.setDeadline(time);
			method.setDuration(method.getDeadline() - method.getArrivalTime());
			generated.add(method);
		}
		
		time += 1;
	}
	
	public double[] timeHist;
	public int timeLength;
	public int maxReward;
	
	/**
	 * 
	 * @param loadGraph 
	 * @param rewardGraph
	 * @param opentimeGraph 
	 * @param timepressureGraph array of length 100; represents count of how likely array index percentage
	 */
	public void generate(int[] loadGraph, int[] rewardGraph, int[] opentimeGraph, int[] timepressureGraph) {
		
		//assert timepressureGraph.length == 100;
		
		timeLength = loadGraph.length;
		maxReward = rewardGraph.length;
		
		opentimeGraph[0] = 0;
		
		int[] cloneLoad = new int[loadGraph.length];
		int count = 0;
		for (int i = 0; i < loadGraph.length; i++) {
			cloneLoad[i] = loadGraph[i];
			count += loadGraph[i];
		}
		loadActual = new double[count];
		count = 0;
		for (int i = 0; i < loadGraph.length; i++) {
			for (int j = 0; j < loadGraph[i]; j++) {
				loadActual[count++] = i;
			}
		}
		loadGraph = cloneLoad;
		
		//  Convert rewardGraph into an array of probabilities
		float[] rewardProbabilities = new float[rewardGraph.length];
		float sumOfRewards = 0;
		for (int i = 0; i < rewardGraph.length; i++) {
			sumOfRewards += rewardGraph[i];
		}
		for (int i = 0; i < rewardGraph.length; i++) {
			rewardProbabilities[i] = rewardGraph[i] / sumOfRewards;
			if (i >= 1) {
				rewardProbabilities[i] += rewardProbabilities[i-1];
			}
		}
		//  Set the last probability sum to 1 (undo rounding errors)
		for (int i = rewardProbabilities.length - 1; i >= 0; i--) {
			if (rewardProbabilities[i] > 0) {
				rewardProbabilities[i] = 1;
				break;
			}
		}
		
		//  Convert opentimeGraph into an array of probabilities
		//  Same procedure as above
		float[] opentimeProbabilities = new float[opentimeGraph.length];
		float sumOfOpentimes = 0;
		for (int i = 0; i < opentimeGraph.length; i++) {
			sumOfOpentimes += opentimeGraph[i];
		}
		for (int i = 0; i < opentimeGraph.length; i++) {
			opentimeProbabilities[i] = opentimeGraph[i] / sumOfOpentimes;
			if (i >= 1) {
				opentimeProbabilities[i] += opentimeProbabilities[i-1];
			}
		}
		//  Set the last probability sum to 1 (undo rounding errors)
		for (int i = opentimeProbabilities.length - 1; i >= 0; i--) {
			if (opentimeProbabilities[i] > 0) {
				opentimeProbabilities[i] = 1;
				break;
			}
		}
		
		//  Convert timepressureGraph into an array of probabilities
		//  Same procedure as above
		float[] timepressureProbabilities = new float[timepressureGraph.length];
		float sumOfTimepressure = 0;
		for (int i = 0; i < timepressureGraph.length; i++) {
			sumOfTimepressure += timepressureGraph[i];
		}
		for (int i = 0; i < timepressureGraph.length; i++) {
			timepressureProbabilities[i] = timepressureGraph[i] / sumOfTimepressure;
			if (i >= 1) {
				timepressureProbabilities[i] += timepressureProbabilities[i-1];
			}
		}
		//  Set the last probability sum to 1 (undo rounding errors)
		for (int i = timepressureProbabilities.length - 1; i >= 0; i--) {
			if (timepressureProbabilities[i] > 0) {
				timepressureProbabilities[i] = 1;
				break;
			}
		}
	
		
		//  Calculate total time all methods will consume
		int timeUnits = 0;
		for (int i = 0; i < loadGraph.length; i++)
			timeUnits += loadGraph[i];
		
		//  Find the maximum length timeslots in the loadGraph
		ArrayList<TimeSlot> times = new ArrayList<TimeSlot>();	
		int k = 0;
		while (k < loadGraph.length) {
			if (loadGraph[k] == 0) { 
				k++; 
				continue; 
			} 
			
			TimeSlot t = new TimeSlot();
			t.start = k;
			int j = k;
			for (; j < loadGraph.length; j++) {
				if (loadGraph[j] == 0) break;
				loadGraph[j]--;
			}
			t.end = j;
			times.add(t);
		}

		//  Generate open time from distribution approximately matching
		//  total time of timeUnits
		Random r = new Random();
		ArrayList<Integer> timeDist = new ArrayList<Integer>();
		int sum = 0;
		while (sum < timeUnits) {
			float rn = r.nextFloat();
			for (int i = 0; i < opentimeProbabilities.length; i++) {
				if (opentimeProbabilities[i] > rn) {
					timeDist.add(i);
					sum += i;
					break;
				}
			}
		}
		
		//  Sort sample Open Times to fit the largest Open Times first
		Collections.sort(timeDist);
		
		//  Greedly fit Open Times from largest to smallest in the TimeSlots
		for (int i = timeDist.size() - 1; i >= 0; i--) {
			int randomDuration = timeDist.get(i);
			
			Collections.shuffle(times);
			for (TimeSlot ts : times) {
				if (ts.end - ts.start - ts.duration >= randomDuration) {
					ts.durations.add(randomDuration);
					ts.duration += randomDuration;
					break;
				}
			}
		}
		
		//  Try to fit other time slots that would improve the match
		//  to the input opentimeGraph
		for (TimeSlot ts : times) {
			boolean found = true;
			while (found) {
				found = false;
				ArrayList<Integer> choices = possibleChoices();
				for (Integer duration : choices) {
					if (ts.end - ts.start - ts.duration <= duration) {
						ts.durations.add(duration);
						ts.duration += duration;
						found = true;
						break;
					}
				}
			}
		}

		//  Generate the actual Methods out of the time slots
		//  For timeslots that aren't completely full, distribute
		//  the remaining time randomly through the timeslot
		int generatedCount = 0;
		for (TimeSlot ts : times) {
			Collections.shuffle(ts.durations);
			int extra = ts.end - ts.start - ts.duration;
			int start = ts.start;
			int partExtra;
			if (extra > 0) {
				partExtra = r.nextInt(extra+1);
				start += partExtra;
				extra -= partExtra;
			}
			for (Integer duration : ts.durations) {
				final Method method = new Method();
				method.setArrivalTime(start);
				method.setReleaseTime(start);
				method.setDeadline(start + duration);
				
				float rn = r.nextFloat();
				for (int i = 0; i < timepressureProbabilities.length; i++) {
					if (timepressureProbabilities[i] > rn) {
						method.setDuration((int)( ((100-i)*duration)/ 100));
						break;
					}
				}
				
				rn = r.nextFloat();
				for (int i = 0; i < rewardProbabilities.length; i++) {
					if (rewardProbabilities[i] > rn) {
						method.setReward(i);
						break;
					}
				}
				
				start += duration;
				
				if (extra > 0) {
					partExtra = r.nextInt(extra+1);
					start += partExtra;
					extra -= partExtra;
				}
				generated.add(method);
			}
		}
		calculateActualReward(rewardGraph);
		calculateActualOpenTime(opentimeGraph);
		calculateActualTimePressure(timepressureGraph);
	}
	public void generate(int[] loadDist) {
		int timeUnits = 0;
		for (int i = 0; i < loadDist.length; i++)
			timeUnits += loadDist[i];
		
		ArrayList<TimeSlot> times = new ArrayList<TimeSlot>();
		
		int k = 0;
		while (k < loadDist.length) {
			if (loadDist[k] == 0) { 
				k++; 
				continue; 
			} 
			
			TimeSlot t = new TimeSlot();
			t.start = k;
			int j = k;
			for (; j < loadDist.length; j++) {
				if (loadDist[j] == 0) break;
				loadDist[j]--;
			}
			t.end = j;
			times.add(t);
		}

		Random r = new Random();
		ArrayList<Integer> timeDist = new ArrayList<Integer>();
		int sum = 0;
		while (sum < timeUnits) {
			int rn = r.nextInt(1) + 1;
			timeDist.add(rn);
			sum += rn;
		}
		
		Collections.sort(timeDist);
		
		for (int i = timeDist.size() - 1; i >= 0; i--) {
			int randomDuration = timeDist.get(i);
			
			Collections.shuffle(times);
			for (TimeSlot ts : times) {
				if (ts.end - ts.start - ts.duration >= randomDuration) {
					ts.durations.add(randomDuration);
					ts.duration += randomDuration;
					break;
				}
			}
		}
		
		
		for (TimeSlot ts : times) {
			boolean found = true;
			while (found) {
				found = false;
				ArrayList<Integer> choices = possibleChoices();
				for (Integer duration : choices) {
					if (ts.end - ts.start - ts.duration <= duration) {
						ts.durations.add(duration);
						ts.duration += duration;
						found = true;
						break;
					}
				}
			}
		}
		
		
		int generatedCount = 0;
		for (TimeSlot ts : times) {
			Collections.shuffle(ts.durations);
			int extra = ts.end - ts.start - ts.duration;
			int start = ts.start;
			int partExtra;
			if (extra > 0) {
				partExtra = r.nextInt(extra+1);
				start += partExtra;
				extra -= partExtra;
			}
			for (Integer duration : ts.durations) {
				final Method method = new Method();
				method.setArrivalTime(start);
				method.setReleaseTime(start);
				method.setDuration((int)Math.ceil(duration * (1.0f-r.nextFloat())));
				method.setDeadline(start + duration);
				start += duration;
				
				if (extra > 0) {
					partExtra = r.nextInt(extra+1);
					start += partExtra;
					extra -= partExtra;
				}
				generated.add(method);
			}
		}
	}
	
	public ArrayList<Integer> possibleChoices() {
		int[] counts = new int[11];
		int maxIndex = 0;
		for (Method method : generated) {
			counts[(int) method.getDuration()]++;
			if (counts[(int)method.getDuration()] > counts[maxIndex]) {
				maxIndex = (int)method.getDuration();
			}
		}
		
		ArrayList<Integer> choices = new ArrayList<Integer>();
		for (int i = counts.length - 1; i > 0; i--) {
			if (counts[i] < counts[maxIndex]) {
				choices.add(i);
			}
		}
		return choices;
	}
	
	class TimeSlot {
		public int start, end, duration;
		public ArrayList<Integer> durations = new ArrayList<Integer>();
		@Override
		public String toString() {
			return "" + start + "->" + end;
		}
	}
	
	private void calculateActualOpenTime(int[] opentimeDist) {
		opentimeActual = new double[generated.size()];
    	int sumOpenTime = 0;
		for (int i = 0; i < opentimeDist.length; i++)
			sumOpenTime += opentimeDist[i];
		int countO = 0;
		for (int i = 0; i < opentimeDist.length; i++)
			for (int j = 0; j < opentimeDist[i] * opentimeActual.length / sumOpenTime; j++)
				opentimeActual[countO++] = i;
	}
	private void calculateActualTimePressure(int[] timepressureDist) {
		timepressureActual = new double[generated.size()];
    	int sum = 0;
		for (int i = 0; i < timepressureDist.length; i++)
			sum += timepressureDist[i];
		int countO = 0;
		for (int i = 0; i < timepressureDist.length; i++)
			for (int j = 0; j < timepressureDist[i] * timepressureActual.length / sum; j++)
				timepressureActual[countO++] = i;
	}
	private void calculateActualReward(int[] rewardDist) {
		rewardActual = new double[generated.size()];
    	int sum = 0;
		for (int i = 0; i < rewardDist.length; i++)
			sum += rewardDist[i];
		int countO = 0;
		for (int i = 0; i < rewardDist.length; i++)
			for (int j = 0; j < rewardDist[i] * rewardActual.length / sum; j++)
				rewardActual[countO++] = i;
	}
	
	public double[] actualLoadHist() {
		return loadActual;
	}
	public double[] loadhistogram() {
		ArrayList<Double> stuff = new ArrayList<Double>();
		
		for(Method m : generated) {
			for (long i = m.getArrivalTime(); i < m.getDeadline(); i++) {
				stuff.add((double) i);
			}
		}
		
		double[] ret = new double[stuff.size()];
		for (int i = 0; i < ret.length; i++)
			ret[i] = stuff.get(i);
		return ret;
	}public double[] structureloadhistogram() {
		ArrayList<Double> stuff = new ArrayList<Double>();
		
		for(Method m : generated) {
			for (long i = m.getArrivalTime(); i < m.getDeadline(); i++) {
				stuff.add((double) i);
			}
		}
		
		double[] ret = new double[stuff.size()];
		for (int i = 0; i < ret.length; i++)
			ret[i] = stuff.get(i);
		return ret;
	}
	
	public double[] actualRewardHist() {
		return rewardActual;
	}
	public double[] rewardhistogram() {
		ArrayList<Double> stuff = new ArrayList<Double>();
		
		for(Method m : generated) {
			stuff.add((double) m.getReward());
		}
		
		double[] ret = new double[stuff.size()];
		for (int i = 0; i < ret.length; i++)
			ret[i] = stuff.get(i);
		return ret;
	}public double[] structurerewardhistogram() {
		ArrayList<Double> stuff = new ArrayList<Double>();
		
		for(Method m : generated) {
			if (!m.hasBecomeSexpr())
				continue;
			stuff.add((double) m.getReward());
		}
		
		double[] ret = new double[stuff.size()];
		for (int i = 0; i < ret.length; i++)
			ret[i] = stuff.get(i);
		return ret;
	}
	
	public double[] actualOpenTimeHist() {
		return opentimeActual;
	}
	public double[] opentimehistogram() {
		ArrayList<Double> stuff = new ArrayList<Double>();
		for(Method m : generated) {
			stuff.add((double)(m.getDeadline() - m.getArrivalTime()));
		}
		
		double[] ret = new double[stuff.size()];
		for (int i = 0; i < ret.length; i++)
			ret[i] = stuff.get(i);
		return ret;
	}public double[] structureopentimehistogram() {
		ArrayList<Double> stuff = new ArrayList<Double>();
		for(Method m : generated) {
			if (!m.hasBecomeSexpr())
				continue;
			stuff.add((double)(m.getDeadline() - m.getArrivalTime()));
		}
		
		double[] ret = new double[stuff.size()];
		for (int i = 0; i < ret.length; i++)
			ret[i] = stuff.get(i);
		return ret;
	}
	
	public double[] actualTimePressureHist() {
		return timepressureActual;
	}
	public double[] timePressurehist() {
		ArrayList<Double> stuff = new ArrayList<Double>();
		for(Method m : generated) {
			if (!m.hasBecomeSexpr())
				continue;
			double opentime = m.getDeadline() - m.getArrivalTime();
			double unusedtime = opentime - m.getDuration();
			double timepressure = unusedtime / opentime;
			stuff.add(timepressure * 100);
		}
		
		double[] ret = new double[stuff.size()];
		for (int i = 0; i < ret.length; i++)
			ret[i] = stuff.get(i);
		
		return ret;
	}
	public double[] structuretimePressurehist() {
		ArrayList<Double> stuff = new ArrayList<Double>();
		for(Method m : generated) {
			if (!m.hasBecomeSexpr())
				continue;
			double opentime = m.getDeadline() - m.getArrivalTime();
			double unusedtime = opentime - m.getDuration();
			double timepressure = unusedtime / opentime;
			stuff.add(timepressure * 100);
		}
		
		double[] ret = new double[stuff.size()];
		for (int i = 0; i < ret.length; i++)
			ret[i] = stuff.get(i);
		
		return ret;
	}
	
	public int methodsUsed() {
		int count = 0;
		for(Method m : generated) {
			count += m.hasBecomeSexpr() ? 1: 0;
		}
		return count;
	}
}
