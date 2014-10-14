package project;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


public class GenerateLoad {

	public ArrayList<Method> generated = new ArrayList<Method>();
	
	public ArrayList<Method> aliveMethods = new ArrayList<Method>();
	public long time = 0;
	
	private Random random = new Random();
	
	public void generateNextTimeStep(int load) {

		for (int i = aliveMethods.size()-1; i >= 0; i--) {
			if (aliveMethods.get(i).deadline <= time) {
				generated.add(aliveMethods.remove(i));
			}
		}
		
		while (load > aliveMethods.size()) {
			long duration = random.nextInt(5) + 1;
			final Method method = new Method();
			method.arivalTime = this.time;
			method.releaseTime = this.time;
			method.duration = duration;
			method.deadline = this.time + duration;
			aliveMethods.add(method);
		}
		
		while (load < aliveMethods.size()) {
			Method method = aliveMethods.remove(random.nextInt(aliveMethods.size()));
			method.deadline = time;
			method.duration = method.deadline - method.releaseTime;
			generated.add(method);
		}
		
		time += 1;
	}
	
	public double[] timeHist;
	
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
			int rn = r.nextInt(10) + 1;
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
				method.arivalTime = start;
				method.releaseTime = start;
				method.duration = duration;
				method.deadline = start + duration;
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
			counts[(int) method.duration]++;
			if (counts[(int)method.duration] > counts[maxIndex]) {
				maxIndex = (int)method.duration;
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
	
	public double[] histogram() {
		ArrayList<Double> stuff = new ArrayList<Double>();
		
		for(Method m : generated) {
			for (long i = m.arivalTime; i < m.deadline; i++) {
				stuff.add((double) i);
			}
		}
		
		double[] ret = new double[stuff.size()];
		for (int i = 0; i < ret.length; i++)
			ret[i] = stuff.get(i);
		return ret;
	}
	
	public double[] durationhistogram() {
		ArrayList<Double> stuff = new ArrayList<Double>();
		for(Method m : generated) {
			stuff.add((double)m.getDuration());
		}
		
		double[] ret = new double[stuff.size()];
		for (int i = 0; i < ret.length; i++)
			ret[i] = stuff.get(i);
		return ret;
	}
}
