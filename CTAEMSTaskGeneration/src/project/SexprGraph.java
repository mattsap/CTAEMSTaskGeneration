package project;

import java.util.ArrayList;
import java.util.List;

import sexpr.Sexpr;
import sexpr.SexprUtils;

public class SexprGraph {
	public Sexpr expr;
	public boolean willNeedACopy = false;
	public SexprGraph dotdotdot = null;
	public List<SexprGraph> edges = new ArrayList<SexprGraph>();
	public List<Integer> percentMethodsPerEdge = new ArrayList<Integer>();
	public SexprGraph(Sexpr expr) {
		this.expr = expr;
	}
	
	public SexprGraph clone(List<Sexpr> exprs) {
		Sexpr cloned = expr.clone(exprs);
		exprs.add(cloned);
		SexprGraph ret = new SexprGraph(cloned);
		ret.percentMethodsPerEdge.addAll(this.percentMethodsPerEdge);
		ret.dotdotdot = this.dotdotdot == null ? null : this.dotdotdot.clone(exprs);
		for (SexprGraph exp : edges) {
			ret.edges.add(exp.clone(exprs));
		}
		return ret;
	}
	
	public void Distribute(List<Sexpr> exprs, List<Method> methods) {
		
		List<SexprGraph> edgesNeedingMethods = new ArrayList<SexprGraph>();
		List<Integer> edgeMethodPercentages = new ArrayList<Integer>();
		
		List<Sexpr> thisNeedingMethods = new ArrayList<Sexpr>();
		List<Integer> thisMethodPercentages = new ArrayList<Integer>();
		List<Integer> thisMethodCounts = new ArrayList<Integer>();
		
		for (SexprGraph edge : edges) {
			if (edge.NeedsMethods()) {
				edgesNeedingMethods.add(edge);
				edgeMethodPercentages.add(percentMethodsPerEdge.get(edges.indexOf(edge)));
			}
		}
		
		
		if (expr.NeedsMethods()) {
			thisNeedingMethods.add(expr);
			thisMethodPercentages.add(expr.PercentNeeded());
			thisMethodCounts.add(expr.CountNeeded());
		}
		
		int sum = 0;
		for (Integer pc : thisMethodPercentages) sum += pc;
		for (Integer pc : edgeMethodPercentages) sum += pc;
		assert sum <= 100 && sum >= 0;
		

		int count = 0;
		for (Integer pc : thisMethodCounts) count += pc;
		assert count <= methods.size() && count >= 0;
		
		int generatedMethodsUsed = 0;
		for (int i = thisNeedingMethods.size() - 1; i >= 0; i--) {
			int toUse = thisMethodPercentages.get(i) == 0 ? thisMethodCounts.get(i) : (thisMethodPercentages.get(i) * (methods.size() - count)) / 100;
			if (i == 0 && edgesNeedingMethods.size() == 0 && dotdotdot == null) {
				// FIX: need to pull out the multiple of CountNeeded for dotdotdot
				toUse = methods.size() - generatedMethodsUsed;
			}
			thisNeedingMethods.get(i).AddGeneratedMethods(exprs, methods.subList(generatedMethodsUsed, generatedMethodsUsed + toUse));
			generatedMethodsUsed += toUse;
		}
		
		if (edgesNeedingMethods.size() > 0) {
			int countWithZeroPercent = 0;
			for (int i = edgesNeedingMethods.size() - 1; i >= 0; i--) {
				if (edgeMethodPercentages.get(i) == 0) {
					countWithZeroPercent++;
				}
				else {
					int toUse = (edgeMethodPercentages.get(i) * methods.size()) / 100;
					if (i == 0 && countWithZeroPercent == 0 && dotdotdot == null) {
						// FIX: need to pull out the multiple of CountNeeded for dotdotdot
						toUse = methods.size() - generatedMethodsUsed;
					}
					edgesNeedingMethods.get(i).Distribute(exprs, methods.subList(generatedMethodsUsed, generatedMethodsUsed + toUse));
					generatedMethodsUsed += toUse;
				}
			}
			if (countWithZeroPercent > 0) {
				int forEachEdge = (methods.size() - generatedMethodsUsed) / countWithZeroPercent;
				for (int i = edgesNeedingMethods.size() - 1; i >= 0; i--) {
					if (edgeMethodPercentages.get(i) == 0) {
						if (i == 0 && dotdotdot == null) {
							// FIX: need to pull out the multiple of CountNeeded for dotdotdot
							edgesNeedingMethods.get(i).Distribute(exprs, methods.subList(generatedMethodsUsed, methods.size()));
							generatedMethodsUsed = methods.size();
						}
						else {
							edgesNeedingMethods.get(i).Distribute(exprs, methods.subList(generatedMethodsUsed, generatedMethodsUsed+forEachEdge));
							generatedMethodsUsed += forEachEdge;
						}
					}
				}
			}
		}
		
		if (dotdotdot != null) {
			int countNeeded = dotdotdot.CountNeeded();
			assert countNeeded > 0;
			for (; generatedMethodsUsed+countNeeded <= methods.size(); generatedMethodsUsed += countNeeded) {
				SexprGraph clone = dotdotdot.clone(exprs);
				clone.Distribute(exprs, methods.subList(generatedMethodsUsed, generatedMethodsUsed+countNeeded));
				SexprUtils.AppendField(expr, "subtasks", clone.expr.getArgsOfArgWithName("label").get(0).id);
				this.edges.add(clone);
				this.percentMethodsPerEdge.add(0);
			}
		}
		//  FIX ^^^
		
		assert generatedMethodsUsed == methods.size();
	}
	
	public boolean NeedsMethods() {
		if (dotdotdot != null)
			return true;
		
		if (expr.NeedsMethods()) {
			return true;
		}
		
		for (SexprGraph edge : edges) {
			if (edge.NeedsMethods())
				return true;
		}
		
		return false;
	}
	
	public int CountNeeded() {
		int count = expr.CountNeeded();
	
		for (SexprGraph edge : edges) {
			count += edge.CountNeeded();
		}
		
		return count;
	}
}