package controls;

import utils.*;
import utils.UIColors.*;
import utils.UIFonts.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;

public class MyComboBoxRenderer extends JLabel implements ListCellRenderer<String> {

    private final UIFonts fonts = new UIFonts();
    private final UIColors colors = new UIColors();

    Border border;
    Font font;
    Color bg;
    Color fg;

    public MyComboBoxRenderer() {
        super();

        Border lineBorder = BorderFactory.createLineBorder(colors.getColor(UIColor.BORDER_GREY), 1);
        Border emptyBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        border = BorderFactory.createCompoundBorder(lineBorder, emptyBorder);

        font = fonts.getFont(FontStyle.LIGHT, 16);
        bg = colors.getColor(UIColor.BG_PRIMARY);
        fg = colors.getColor(UIColor.TEXT_FG);

    }


    @Override
    public Component getListCellRendererComponent(JList list, String value,
                                                  int index, boolean isSelected, boolean cellHasFocus) {
        setText(value);
        setBackground(bg);
        setForeground(fg);
        setFont(font);
        setBorder(border);

        return this;
    }

}
