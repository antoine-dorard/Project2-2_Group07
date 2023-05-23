package controls;

import utils.*;
import utils.UIColors.*;
import utils.UIFonts.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.text.Caret;
import java.awt.*;

public class MyTextField extends JTextField {

    private final UIFonts fonts = new UIFonts();
    private final UIColors colors = new UIColors();



    public MyTextField() {
        super();

        // set font.
        setFont(fonts.getFont(UIFonts.FontStyle.LIGHT,16));

        // set back-/foreground if not selected.
        setBackground(colors.getColor(UIColor.TEXT_BG));
        setForeground(colors.getColor(UIColor.TEXT_FG));

        // set back-/foreground if selected.
        setSelectionColor(colors.getColor(UIColor.TEXT_BG_SELECTION));
        setSelectedTextColor(colors.getColor(UIColor.TEXT_FG_SELECTION));

        // create the borders and add both to the JTextField border.
        Border lineBorder = BorderFactory.createLineBorder(colors.getColor(UIColor.BORDER_GREY), 1);
        Border emptyBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        Border border = BorderFactory.createCompoundBorder(lineBorder, emptyBorder);

        // set the border as the JTextField border.
        setBorder(border);

        // set the background of the blinking Caret.
        setCaretColor(colors.getColor(UIColor.TEXT_BG_SELECTION));
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (enabled){
            setBackground(colors.getColor(UIColor.TEXT_BG));
        }
        else{
            setBackground(colors.getColor(UIColor.BG_SECONDARY));
        }
    }
}
