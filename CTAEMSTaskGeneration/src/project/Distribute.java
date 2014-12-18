package project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import sexpr.Sexpr;

public class Distribute {

	
	public static SexprGraph ToSexprs(List<Sexpr> exprs, List<Method> generatedMethods) {
		HashMap<String, SexprGraph> lookup = new HashMap<String, SexprGraph>();
		
		//  Setup lookup table
		for (Sexpr exp : exprs) {
			if (exp.id.equals("spec_task") || exp.id.equals("spec_task_group")) {
				lookup.put(exp.getArgsOfArgWithName("label").get(0).id, new SexprGraph(exp));
				exp.shouldBeEmitted = false;
			}
		}
		
		//  Build graph
		for (SexprGraph node : lookup.values()) {
			List<Sexpr> toRemove = new ArrayList<Sexpr>();
			for (Sexpr task : node.expr.getArgsOfArgWithName("subtasks")) {
				String taskid = task.id;
				int percent = 0;
				if (taskid.contains("%")) {
					percent = Integer.parseInt(taskid.substring(taskid.indexOf("%")+1));
					taskid = taskid.substring(0, taskid.indexOf("%"));
				}
				if (taskid.endsWith("...")) {
					taskid = taskid.substring(0, taskid.length() - 3);
					SexprGraph lookedup = lookup.get(taskid);
					lookedup.willNeedACopy = true;
					task.id = taskid;
					node.dotdotdot.add(lookedup);
					node.percentMethodsPerDotDotDot.add(percent);
					toRemove.add(task);
				}
				else if (lookup.containsKey(taskid)) {
					SexprGraph lookedup = lookup.get(taskid);
					if (lookedup.willNeedACopy)
						throw new RuntimeException("Referenced the following from multiple places: " + lookedup.expr.toString());
					lookedup.willNeedACopy = true;
					node.edges.add(lookedup);
					node.percentMethodsPerEdge.add(percent);
					task.id = taskid;
				}
			}

			if (toRemove != null) {
				for (Sexpr sei : node.expr.args) {
					if (sei.id.equals("subtasks")) {
						sei.args.removeAll(toRemove);
					}
				}
			}
		}
		
		//  Find root
		SexprGraph rootnode = null;
		for (SexprGraph node : lookup.values()) {
			boolean found = true;
			for (SexprGraph test : lookup.values()) {
				if (test.edges.contains(node) || test.dotdotdot.contains(node)) {
					found = false;
					break;
				}
			}
			if (found) {
				if (rootnode != null) 
					throw new RuntimeException("Found multiple root tasks");
				rootnode = node;
			}
		}
		
		if (rootnode == null)
			throw new RuntimeException("Could not find a root task");
		
		rootnode.Distribute(exprs, generatedMethods);
		rootnode.MarkGraph();
		return rootnode;
	}
}
