package project;

import sexpr.SexprParser;
import sexpr.Sexpr;

public class Method {

	private long arrivalTime, releaseTime, duration, deadline, reward;
	public int id;
	private boolean hasBecomeSexpr = false;
	
	private static int nextid = 1;
	
	public Method() {
		this.id = nextid++;
	}
	
	public long getArrivalTime() {
		return arrivalTime;
	}
	public void setArrivalTime(long arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public long getReleaseTime() {
		return releaseTime;
	}
	public void setReleaseTime(long releaseTime) {
		this.releaseTime = releaseTime;
	}

	public long getDuration() {
		return duration;
	}
	public void setDuration(long duration) {
		this.duration = duration;
	}

	public long getDeadline() {
		return deadline;
	}
	public void setDeadline(long deadline) {
		this.deadline = deadline;
	}

	public long getReward() {
		return reward;
	}
	public void setReward(long reward) {
		this.reward = reward;
	}

	public Sexpr toSexpr(String name) {
		hasBecomeSexpr = true;
		String sexpr =
				"(spec_method (label " + name + ") " + 
					"(outcomes " + 
						"(" + name + "_o0 " + 
							"(density 1.0) " + 
							"(duration-distribution " + getDuration() + " 1.0)" + 
							"(quality-distribution " + getReward() + " 1.0)" + 
						")" + 
					") " + 
					"(deadline " + deadline + ")" + 
					"(earliest_start_time " + arrivalTime + ")" + 
				")";
	
		try {
			return SexprParser.parseExpr(sexpr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public boolean hasBecomeSexpr() {
		return hasBecomeSexpr;
	}

	public void setHasBecomeSexpr(boolean hasBecomeSexpr) {
		this.hasBecomeSexpr = hasBecomeSexpr;
	}
}
