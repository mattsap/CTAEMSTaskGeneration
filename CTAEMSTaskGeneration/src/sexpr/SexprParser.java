package sexpr;

import java.util.ArrayList;
import java.util.List;

public class SexprParser {

	public static List<Sexpr> parse(String sexpr) {
		ArrayList<Sexpr> ret = new ArrayList<Sexpr>();
		Code code = new Code(sexpr, 0);
		
		while (!code.isDone()) {
			code.skipWhite();

			if (!code.isOpenParen())
				throw new RuntimeException("Expected '('");
			
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
			
				if (code.isDone()) 
					new RuntimeException("Expected closing ')' but found the end of file");
				ret.args.add(parseExpr(code));
				if (code.isDone()) 
					new RuntimeException("Expected closing ')' but found the end of file");
			}
			code.next();
		}

		code.skipWhite();
		return ret;
	}
}
