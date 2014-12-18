package generate;

import java.util.ArrayList;
import java.util.List;

import project.Method;

public abstract class MethodGenerator {

	protected List<Method> generated = new ArrayList<Method>();
	
	public abstract void generate();
	
	public double[] histStructureLoad() {
		ArrayList<Double> stuff = new ArrayList<Double>();
		
		for(Method m : generated) {
			//if (!m.hasBecomeSexpr())
				//continue;
			for (long i = m.getArrivalTime(); i < m.getDeadline(); i++) {
				stuff.add((double) i);
			}
		}
		
		double[] ret = new double[stuff.size()];
		for (int i = 0; i < ret.length; i++)
			ret[i] = stuff.get(i);
		return ret;
	}

	public List<Method> getGeneratedMethods() {
		return generated;
	}
	
	public int getMethodsUsed() {
		int count = 0;
		for(Method m : generated) {
			count += m.hasBecomeSexpr() ? 1: 0;
		}
		return count;
	}
	
	public double[] histStructureTimePressure() {
		ArrayList<Integer> timePressure = new ArrayList<Integer>();
		
		int[] sumOfPressure = new int[1];
		int[] countOfPressure = new int[1];
		
		for(Method m : generated) {
			//if (!m.hasBecomeSexpr())
				//continue;
			long timePressureAsPercent = (100*(m.getDeadline() - m.getArrivalTime()-m.getDuration())) / (m.getDuration());
			//assert timePressureAsPercent >= 0 && timePressureAsPercent <= 100 : "Time Pressure not between 0-100: " + timePressureAsPercent;
			
			if (m.getArrivalTime() >= sumOfPressure.length) {
				int[] newSOP = new int[(int)m.getArrivalTime()+1];
				int[] newCOP = new int[(int)m.getArrivalTime()+1];
				
				for (int i = 0; i < sumOfPressure.length; i++) {
					newSOP[i] = sumOfPressure[i];
					newCOP[i] = countOfPressure[i];
				}
				
				sumOfPressure = newSOP;
				countOfPressure = newCOP;
			}
			
			sumOfPressure[(int)m.getArrivalTime()] += timePressureAsPercent;
			countOfPressure[(int)m.getArrivalTime()]++;
		}
		
		for (int i = 0; i < sumOfPressure.length; i++) {
			if (countOfPressure[i] == 0)
				continue;
			for (int j = sumOfPressure[i] / countOfPressure[i]; j > 0; j--) {
				timePressure.add(i);
			}
		}
		
		double[] ret = new double[timePressure.size()];
		for (int i = 0; i < ret.length; i++)
			ret[i] = timePressure.get(i);
		return ret;
	}
	
	public double[] histStructureReward() {
		ArrayList<Integer> reward = new ArrayList<Integer>();
		
		int[] sumOfReward = new int[1];
		int[] countOfReward = new int[1];
		
		for(Method m : generated) {
			//if (!m.hasBecomeSexpr())
				//continue;
			
			if (m.getArrivalTime() >= sumOfReward.length) {
				int[] newSOR = new int[(int)m.getArrivalTime()+1];
				int[] newCOR = new int[(int)m.getArrivalTime()+1];
				
				for (int i = 0; i < sumOfReward.length; i++) {
					newSOR[i] = sumOfReward[i];
					newCOR[i] = countOfReward[i];
				}
				
				sumOfReward = newSOR;
				countOfReward = newCOR;
			}
			
			sumOfReward[(int)m.getArrivalTime()] += m.getReward();
			countOfReward[(int)m.getArrivalTime()]++;
		}
		
		for (int i = 0; i < sumOfReward.length; i++) {
			if (countOfReward[i] == 0)
				continue;
			for (int j = sumOfReward[i] / countOfReward[i]; j > 0; j--) {
				reward.add(i);
			}
		}
		
		double[] ret = new double[reward.size()];
		for (int i = 0; i < ret.length; i++)
			ret[i] = reward.get(i);
		return ret;
	}
}
