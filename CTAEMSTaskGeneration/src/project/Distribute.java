package project;

import java.util.ArrayList;
import java.util.List;

import sexpr.Sexpr;
import sexpr.SexprUtils;

public class Distribute {

	public static void ToSexprs(List<Sexpr> exprs, GenerateLoad g) {
		
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
