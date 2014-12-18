package project;

import java.util.ArrayList;
import java.util.List;

import sexpr.Sexpr;
import sexpr.SexprUtils;

public class SexprGraph {
	public Sexpr expr;
	public boolean willNeedACopy = false;
	public List<SexprGraph> dotdotdot = new ArrayList<SexprGraph>();
	public List<Integer> percentMethodsPerDotDotDot = new ArrayList<Integer>();
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
		for (SexprGraph exp : dotdotdot) {
			ret.dotdotdot.add(exp.clone(exprs));
		}
		for (SexprGraph exp : edges) {
			SexprGraph clone = exp.clone(exprs);
			ret.edges.add(clone);
			List<Sexpr> args = ret.expr.getArgsOfArgWithName("subtasks");
			for (int i = 0; i < args.size(); i++) {
				if (args.get(i).id.equals(exp.expr.getArgsOfArgWithName("label").get(0).id)) {
					args.get(i).id = clone.expr.getArgsOfArgWithName("label").get(0).id;
					break;
				}
			}
		}
		return ret;
	}
	
	public void Distribute(List<Sexpr> exprs, List<Method> methods)  {
		
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
		
		int sum = expr.PercentNeeded();
		for (Integer pc : edgeMethodPercentages) sum += pc;
		if (sum > 100)
			throw new RuntimeException("Found task with percentages summing to over 100: " + this.expr.toString());
		

		int count = 0;
		for (Integer pc : thisMethodCounts) count += pc;
		if (count > methods.size())
			throw new RuntimeException("Not enough methods available for this Sexpr: " + expr.toString());
		
		int generatedMethodsUsed = 0;
		
		//  Add methods to this Sexpr
		if (this.expr.NeedsMethods()) {
			int toUse = expr.CountNeeded() + (expr.PercentNeeded() * (methods.size() - count)) / 100;
			if (edgesNeedingMethods.size() == 0 && dotdotdot == null) {
				// FIX: need to pull out the multiple of CountNeeded for dotdotdot
				toUse = methods.size() - generatedMethodsUsed;
			}
			List<Method> methodsToUse = methods.subList(generatedMethodsUsed, generatedMethodsUsed + toUse);
			List<Sexpr> methodsAdded = expr.AddGeneratedMethods(exprs, methodsToUse);
			for (Sexpr exp : methodsAdded)
				this.edges.add(new SexprGraph(exp));
			generatedMethodsUsed += toUse;
		}
		
		//  Give methods to the sub spec_task
		if (edgesNeedingMethods.size() > 0) {
			int methodsRemaining = methods.size() - generatedMethodsUsed;
			int countWithZeroPercent = 0;
			for (int i = edgesNeedingMethods.size() - 1; i >= 0; i--) {
				if (edgeMethodPercentages.get(i) == 0) {
					countWithZeroPercent++;
				}
				else {
					int toUse = (edgeMethodPercentages.get(i) * methodsRemaining) / 100;
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
		
		int sumP = 0;
		int countZeros = 0;
		for (Integer pc : this.percentMethodsPerDotDotDot) {
			sumP += pc;
			if (pc == 0) countZeros++;
		}
		if (sumP > 100)
			throw new RuntimeException("Found task with percentages summing to over 100: " + this.expr.toString());
		
		
		List<Integer> realPercentDotDotDot = new ArrayList<Integer>();
		for (Integer i : percentMethodsPerDotDotDot) {
			if (i == 0)
				realPercentDotDotDot.add((100-sumP) / countZeros);
			else
				realPercentDotDotDot.add(i);
		}
		
		int methodsRemaining = methods.size() - generatedMethodsUsed;
		for (int i = 0; i < dotdotdot.size(); i++) {
			SexprGraph graph = dotdotdot.get(i);
			Integer percent = realPercentDotDotDot.get(i);
			int countNeeded = graph.CountNeeded();
			if (countNeeded <= 0)
				throw new RuntimeException("With TASK..., TASK is expected to need generated methods: " + this.expr.toString());
			
			for (int j = ((percent*methodsRemaining)/100)/countNeeded; j > 0; j--) {
				SexprGraph clone = graph.clone(exprs);
				clone.Distribute(exprs, methods.subList(generatedMethodsUsed, generatedMethodsUsed+countNeeded));
				generatedMethodsUsed+=countNeeded;
				SexprUtils.AppendField(expr, "subtasks", clone.expr.getArgsOfArgWithName("label").get(0).id);
				this.edges.add(clone);
				this.percentMethodsPerEdge.add(0);
			}
		}
		
		//assert generatedMethodsUsed == methods.size();
	}
	
	public boolean NeedsMethods() {
		if (dotdotdot.size() > 0)
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
	
	public String EmitGraph() {
		String ret = expr.toString();
		
		for (SexprGraph sg : edges) {
			ret += sg.EmitGraph();
		}
		
		return ret;
	}
	
	public void MarkGraph() {
		expr.shouldBeEmitted = true;
		for (SexprGraph sg : edges) {
			sg.MarkGraph();
		}
	}
}