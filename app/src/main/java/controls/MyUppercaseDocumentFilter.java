package controls;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class MyUppercaseDocumentFilter extends DocumentFilter {

    private String[] notAllowed = {
            "\\.", "\\,", "\\!", "\\<", "\\>", "\\:", "\\;",
            "\\[", "\\]", "\\{", "\\}", "\\+", "\\=", "\\-",
            "\\?", "\\@", "\\#", "\\%", "\\^", "\\&", "\\*",
            "\\(", "\\)", "\\$", "\\'", "\\\"", "\\|", "\\/", "\\\\"
    };

    public MyUppercaseDocumentFilter() {
        super();
    }

    public void insertString(FilterBypass fb, int offset, String text, AttributeSet attr)
            throws BadLocationException {
        fb.insertString(offset, convertText(text), attr);
    }

    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
            throws BadLocationException {
        fb.replace(offset, length, convertText(text), attrs);
    }

    private String convertText(String text) {
        text = text.toUpperCase().replaceAll("\\s", "");
        for(String notAllow : notAllowed) {
            text = text.replaceAll(notAllow, "");
        }
        return text;
    }
}
