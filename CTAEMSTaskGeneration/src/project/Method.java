package project;

import sexpr.Parser;
import sexpr.Sexpr;

public class Method extends Task{

	public long arivalTime, releaseTime, duration, deadline, reward;
	public int id;
	
	public Method(int id) {
		this.id = id;
	}
	
	@Override
	public long getArivalTime() {
		return arivalTime;
	}

	@Override
	public long getReleaseTime() {
		return releaseTime;
	}

	@Override
	public long getDuration() {
		return duration;
	}

	@Override
	public long getDeadline() {
		return deadline;
	}
	public void setDeadline(long deadline) {
		this.deadline = deadline;
	}

	@Override
	public long getReward() {
		return reward;
	}

	public Sexpr toSexpr(String name) {
		
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
					"(arival_time " + arivalTime + ")" + 
					"(release_time " + releaseTime + ")" + 
				")";
	
		try {
			return Parser.parseExpr(sexpr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
