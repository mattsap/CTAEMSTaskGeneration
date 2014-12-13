package project;

import sexpr.SexprParser;
import sexpr.Sexpr;

public class Method {

	public long arrivalTime, releaseTime, duration, deadline, reward;
	public int id;
	public boolean becameSexpr = false;
	
	public Method(int id) {
		this.id = id;
	}
	
	public long getArrivalTime() {
		return arrivalTime;
	}

	public long getReleaseTime() {
		return releaseTime;
	}

	public long getDuration() {
		return duration;
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

	public Sexpr toSexpr(String name) {
		becameSexpr = true;
		String sexpr =
				"(spec_method (label " + name + ") " + 
					"(outcomes " + 
						"(" + name + "_o0 " + 
							"(density 1.0) " + 
							"(duration-distribution " + getDuration() + " 1.0)" + 
						")" + 
					") " + 
					"(reward " + reward + ")" + 
					"(deadline " + deadline + ")" + 
					"(arival_time " + arrivalTime + ")" + 
					"(release_time " + releaseTime + ")" + 
				")";
	
		try {
			return SexprParser.parseExpr(sexpr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
