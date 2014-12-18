package gui;
import java.awt.BorderLayout;
import java.awt.LayoutManager;
import java.util.regex.Pattern;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import javax.swing.JButton;
	
@SuppressWarnings("serial")
public class NumericTextField extends JTextField
{
	
	
	@Override
	protected Document createDefaultModel() {
		return new NumericDocument();
	}
	
	private class NumericDocument extends PlainDocument
	{
		private final Pattern Digits = Pattern.compile("\\d*\\.?\\d*");
		
		@Override
		public void insertString(int offs, String str, AttributeSet a) throws BadLocationException
		{
			if (str != null && Digits.matcher(str).matches()){
				super.insertString(offs, str, a);
			}
		}
	}
}

	


