package sexpr;

import java.util.ArrayList;
import java.util.List;

public class SexprParser {

	public static List<Sexpr> parse(String sexpr) {
		ArrayList<Sexpr> ret = new ArrayList<Sexpr>();
		Code code = new Code(sexpr, 0);
		
		while (!code.isDone()) {
			code.skipWhite();

			assert code.isOpenParen() : "Expected '('";
			
			ret.add(parseExpr(code));
		}
		
		return ret;
	}
	
	public static Sexpr parseExpr(String sexpr)  {
		return parseExpr(new Code(sexpr, 0));
	}
	
	private static Sexpr parseExpr(Code code) {
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
			
				assert !code.isDone() : "Expected closing ')'";
				ret.args.add(parseExpr(code));

				assert !code.isDone() : "Expected closing ')'";
			}
			code.next();
		}

		code.skipWhite();
		return ret;
	}
}
