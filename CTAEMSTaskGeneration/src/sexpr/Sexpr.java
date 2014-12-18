package sexpr;

import java.util.ArrayList;
import java.util.List;

import project.Method;

public class Sexpr {
	public String id;
	public List<Sexpr> args = new ArrayList<Sexpr>(); 
	public int cloneCount = 0;
	public boolean shouldBeEmitted = true;

	public Sexpr clone(List<Sexpr> exprs) {
		Sexpr ret = new Sexpr();
		ret.id = this.id;
		for (Sexpr exp : args) {
			ret.args.add(exp.clone(exprs));
			if (this.id.equals("label")) {
				ret.args.get(ret.args.size()-1).id += "_CLONE_" + ++cloneCount;
			}
		}
		return ret;
	}
	
	@Override
	public String toString() {
		return toString("");
	}
	public String toString(String indent) {
		if (args.size() == 0) {
			return " " + id;
		}
		else {
			String ret = "\n" + indent + "(" + id;
			for (Sexpr expr : args) {
				ret += expr.toString(indent + "\t");
			}
			if (ret.indexOf('\n', 1) != -1)
				ret += "\n" + indent;
			ret += ")";
			return ret;
		}
	}
	
	public String Emit() {
		if (shouldBeEmitted)
			return this.toString();
		else
			return "";
	}
	
	public boolean NeedsMethods() {
		for (Sexpr exp : this.getArgsOfArgWithName("subtasks")) {
			if (exp.id.contains("%") || exp.id.contains("#"))
				return true;
		}
		return false;
	}
	
	public int PercentNeeded() {
		int percent = 0;
		for (Sexpr exp : this.getArgsOfArgWithName("subtasks")) {
			if (exp.id.contains("%")) {
				percent += Integer.parseInt(exp.id.substring(exp.id.indexOf("%")+1));
			}
		}
		return percent;
	}
	
	public int CountNeeded() {
		int count = 0;
		for (Sexpr exp : this.getArgsOfArgWithName("subtasks")) {
			if (exp.id.contains("#")) {
				count += Integer.parseInt(exp.id.substring(exp.id.indexOf("#")+1));
			}
		}
		return count;
	}
	
	public List<Sexpr> AddGeneratedMethods(List<Sexpr> exprs, List<Method> methods) {
		List<Sexpr> ret = new ArrayList<Sexpr>();
		Sexpr percentTask = null;
		for (Sexpr sexp : this.args) {
			if (sexp.id.equals("subtasks")) {
				percentTask = sexp;
				List<Sexpr> toRemove = new ArrayList<Sexpr>();
				for (Sexpr mexp : sexp.args) {
					if (mexp.id.startsWith("Method%") || mexp.id.startsWith("Method#")) {
						toRemove.add(mexp);
					}
				}
				sexp.args.removeAll(toRemove);
				break;
			}
		}
		
		int totake = methods.size();
		long earliest_start_time = this.isArgWithName("earliest_start_time") ? 
					Long.parseLong(this.getArgsOfArgWithName("earliest_start_time").get(0).id) :
					Long.MAX_VALUE ;
		long task_deadline = this.isArgWithName("deadline") ? 
					Long.parseLong(this.getArgsOfArgWithName("deadline").get(0).id) :
					0;
		for (; totake > 0; totake--) {
			Method m = methods.get(totake-1);
			Sexpr mexp = new Sexpr();
			mexp.id = "Method_GEN_" + m.id;
			percentTask.args.add(mexp);
			Sexpr sexprMethod = m.toSexpr(mexp.id);
			exprs.add(sexprMethod);
			ret.add(sexprMethod);
			
			earliest_start_time = Math.min(earliest_start_time, m.getReleaseTime());
			task_deadline = Math.max(task_deadline, m.getDeadline());
		}
		
		for (Sexpr arg : percentTask.args) {
			Sexpr subtask = SexprUtils.FindTaskWithName(exprs, arg.id);
			if (subtask.id.equals("spec_task")) {
				
				if (subtask.isArgWithName("earliest_start_time"))
					earliest_start_time = Math.min(earliest_start_time, Long.parseLong(subtask.getArgsOfArgWithName("earliest_start_time").get(0).id));
				
				if (subtask.isArgWithName("deadline"))
					task_deadline = Math.max(task_deadline, Long.parseLong(subtask.getArgsOfArgWithName("deadline").get(0).id));
			}
		}

		SexprUtils.SetField(this, "deadline", "" + task_deadline);
		SexprUtils.SetField(this, "earliest_start_time", "" + earliest_start_time);
		return ret;
	}
	
	public List<Sexpr> getArgsOfArgWithName(String name) {
		for (Sexpr arg : args) {
			if (arg.id.equals(name)) {
				return arg.args;
			}
		}
		throw new RuntimeException("Expected arg with name: " + name + " in Sexpr: " + this.toString());
	}
	public boolean isArgWithName(String name) {
		for (Sexpr arg : args) {
			if (arg.id.equals(name)) {
				return true;
			}
		}
		return false;
	}
	
}