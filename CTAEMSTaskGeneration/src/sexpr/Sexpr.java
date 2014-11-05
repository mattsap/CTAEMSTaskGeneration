package sexpr;

import java.util.ArrayList;
import java.util.List;

public class Sexpr {
	public String id;
	public List<Sexpr> args = new ArrayList<Sexpr>(); 
	
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
}