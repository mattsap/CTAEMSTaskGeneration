package sexpr;

import java.util.ArrayList;
import java.util.List;

public class Parser {

	public static List<Sexpr> parse(String sexpr) throws Exception {
		ArrayList<Sexpr> ret = new ArrayList<Sexpr>();
		Code code = new Code(sexpr, 0);
		
		while (!code.isDone()) {
			code.skipWhite();
			if (!code.isOpenParen()) {
				throw new Exception("Expected '('");
			}
			ret.add(parseExpr(code));
		}
		
		return ret;
	}
	
	public static Sexpr parseExpr(String sexpr) throws Exception {
		return parseExpr(new Code(sexpr, 0));
	}
	
	private static Sexpr parseExpr(Code code) throws Exception {
		Sexpr ret = new Sexpr();
		
		code.skipWhite();
		if (!code.isOpenParen()) {
			ret.id = code.nextLiteral();
		}
		else {
			code.next();
			ret.id = code.nextLiteral();
			code.skipWhite();
			
			while(!code.isCloseParen()) {
				ret.args.add(parseExpr(code));
			}
			code.next();
		}

		code.skipWhite();
		return ret;
	}
}
