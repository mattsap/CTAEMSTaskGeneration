package project;

import tasks.OrTask;
import tasks.SumTask;

public class MattsStructure {

	public static Task generate() {
		SumTask root = new SumTask();
		
		for (int i = 0; i < 10; i++) {
			root.addTask(new OrTask());
		}
		
		return root;
	}
}
