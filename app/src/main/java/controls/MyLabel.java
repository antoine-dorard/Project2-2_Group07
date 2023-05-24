package controls;

import utils.*;
import utils.UIColors.*;
import utils.UIFonts.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class MyLabel extends JLabel {

    private final UIFonts fonts = new UIFonts();
    private final UIColors colors = new UIColors();

    public MyLabel(String text) {
        super(text);

        setFont(fonts.getFont(UIFonts.FontStyle.MEDIUM,16));

        setBackground(colors.getColor(UIColor.BG_PRIMARY));
        setForeground(colors.getColor(UIColor.FG_HEADER));

        setBorder(new EmptyBorder(5,0,5,0));
    }
}
