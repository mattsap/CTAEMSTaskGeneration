package project;

public class Method extends Task{

	public long arivalTime, releaseTime, duration, deadline, reward;
	
	public Method() {
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

}
