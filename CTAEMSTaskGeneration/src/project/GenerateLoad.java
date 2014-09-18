package project;
import java.util.ArrayList;
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
			long duration = random.nextInt(40) + 1;
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
}
