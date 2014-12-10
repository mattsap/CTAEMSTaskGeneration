package sexpr;

import java.util.ArrayList;
import java.util.List;

public class SexprUtils {

	public static List<Sexpr> FindTasksRequirePercentOfMethods(List<Sexpr> exprs) {
		ArrayList<Sexpr> ret = new ArrayList<Sexpr>();
		for (Sexpr exp : exprs) {
			if (exp.id.equals("spec_task")) {
				for (Sexpr sexp : exp.args) {
					if (sexp.id.equals("subtasks")) {
						for (Sexpr mexp : sexp.args) {
							if (mexp.id.startsWith("Method%")) {
								ret.add(exp);
								break;
							}
						}
						break;
					}
				}
			}
		}
		
		return ret;
	}
	
	public static List<Sexpr> FindTasksWithRepetition(List<Sexpr> exprs) {
		ArrayList<Sexpr> ret = new ArrayList<Sexpr>();
		for (Sexpr exp : exprs) {
			if (exp.id.equals("spec_task")) {
				for (Sexpr sexp : exp.args) {
					if (sexp.id.equals("subtasks")) {
						for (Sexpr mexp : sexp.args) {
							if (mexp.id.endsWith("...")) {
								ret.add(exp);
								break;
							}
						}
						break;
					}
				}
			}
		}
		
		return ret;
	}
	
	public static void SetField(Sexpr expr, String name, String value) {

		Sexpr newVal = new Sexpr();
		newVal.id = value;
		
		for (Sexpr exp : expr.args) {
			if (exp.id.equals(name)) {
				exp.args.clear();
				exp.args.add(newVal);
				return;
			}
		}
		
		Sexpr newName = new Sexpr();
		newName.id = name;
		newName.args.add(newVal);
		expr.args.add(newName);
	}
	
	public static void AppendField(Sexpr expr, String name, String value) {

		Sexpr newVal = new Sexpr();
		newVal.id = value;
		
		for (Sexpr exp : expr.args) {
			if (exp.id.equals(name)) {
				exp.args.add(newVal);
				return;
			}
		}
		
		Sexpr newName = new Sexpr();
		newName.id = name;
		newName.args.add(newVal);
		expr.args.add(newName);
	}
}
