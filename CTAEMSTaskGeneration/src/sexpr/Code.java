package sexpr;

public class Code {
	public String code;
	public int index;
	public Code(String code, int index) {
		this.code = code;
		this.index = index-1;
		next();
	}
	
	public void skipWhite() {
		while (!isDone() && isWhiteSpace()) 
			next();
	}
	public void next() {
		index++;
		if (!this.isDone() && code.charAt(index) == ';') {
			while (!this.isDone() && code.charAt(index) != '\n') index++;
			this.next();
		}
	}
	public boolean isDone() {
		return index >= code.length();
	}
	public boolean isOpenParen() {
		return code.charAt(index) == '(';
	}
	public boolean isCloseParen() {
		return code.charAt(index) == ')';
	}
	public boolean isWhiteSpace() {
		return code.charAt(index) == ' ' || code.charAt(index) == '\t' || code.charAt(index) == '\n' || code.charAt(index) == '\r';
	}
	public String nextLiteral() {
		String ret = "";
		while (!(isOpenParen() || isCloseParen() || isWhiteSpace() || isDone())) {
			ret += code.charAt(index);
			next();
		}
		return ret;
	}
}