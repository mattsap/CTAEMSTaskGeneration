package project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import sexpr.Sexpr;
import sexpr.SexprUtils;

public class Distribute {

	
	public static void ToSexprs(List<Sexpr> exprs, GenerateLoad g) {
		HashMap<String, SexprGraph> lookup = new HashMap<String, SexprGraph>();
		
		//  Setup lookup table
		for (Sexpr exp : exprs) {
			if (!exp.id.equals("spec_task"))
				continue;
		
			lookup.put(exp.getArgsOfArgWithName("label").get(0).id, new SexprGraph(exp));
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
					assert !lookedup.willNeedACopy : "Referenced the following from multiple places: " + lookedup.expr.toString();
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
				assert rootnode == null;
				rootnode = node;
			}
		}
		
		assert rootnode != null;
		
		rootnode.Distribute(exprs, g.generated);
	}
	
	public static void ToSexprs_OLD(List<Sexpr> exprs, GenerateLoad g) {
		
		List<Sexpr> percentages = SexprUtils.FindTasksRequirePercentOfMethods(exprs);
		List<Method> methods = new ArrayList<Method>(g.generated);
		
		for (Sexpr task : percentages) {
			Sexpr percentTask = null;
			float percent = 0;
			for (Sexpr sexp : task.args) {
				if (sexp.id.equals("subtasks")) {
					percentTask = sexp;
					Sexpr toRemove = null;
					for (Sexpr mexp : sexp.args) {
						if (mexp.id.startsWith("Method%")) {
							percent = Integer.parseInt(mexp.id.substring(7)) / 100f;
							toRemove = mexp;
							break;
						}
					}
					sexp.args.remove(toRemove);
					break;
				}
			}
			
			int totake = (int)Math.round(percent * g.generated.size());
			long earliest_start_time = Long.MAX_VALUE;
			long task_deadline = 0;
			for (; totake > 0 && methods.size() > 0; totake--) {
				Method m = methods.remove(methods.size()-1);
				Sexpr mexp = new Sexpr();
				mexp.id = "Method_GEN_" + methods.size();
				percentTask.args.add(mexp);
				exprs.add(m.toSexpr(mexp.id));
				
				earliest_start_time = Math.min(earliest_start_time, m.releaseTime);
				task_deadline = Math.max(task_deadline, m.deadline);
			}

			SexprUtils.SetField(task, "deadline", "" + task_deadline);
			SexprUtils.SetField(task, "earliest_start_time", "" + earliest_start_time);
		}
	}
}
