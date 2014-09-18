package tasks;

import java.util.ArrayList;
import java.util.List;

import project.Task;

public class OrTask extends Task {
	
	private List<Task> subtasks = new ArrayList<Task>();
	
	public void addTask(Task task) {
		subtasks.add(task);
	}
	
	@Override
	public long getArivalTime() {
		long min = Long.MAX_VALUE;
		for (Task st : subtasks) {
			min = st.getArivalTime() < min ? st.getArivalTime() : min;
		}
		
		return min;
	}

	@Override
	public long getReleaseTime() {
		long min = Long.MAX_VALUE;
		for (Task st : subtasks) {
			min = st.getReleaseTime() < min ? st.getReleaseTime() : min;
		}
		
		return min;
	}

	@Override
	public long getDuration() {
		// TODO Auto-generated method stub
		return this.getDeadline() - this.getArivalTime();
	}

	@Override
	public long getDeadline() {
		long max = 0;
		for (Task st : subtasks) {
			max = st.getReleaseTime() < max ? st.getReleaseTime() : max;
		}
		
		return max;
	}

	@Override
	public long getReward() {
		long max = 0;
		for (Task st : subtasks) {
			max = st.getReward() < max ? st.getReward() : max;
		}
		
		return max;
	}
}
