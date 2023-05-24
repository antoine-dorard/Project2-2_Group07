package controls;

import utils.*;
import utils.UIColors.*;
import utils.UIFonts.*;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class MyHeaderPane extends JPanel {

    private final UIFonts fonts = new UIFonts();
    private final UIColors colors = new UIColors();


    public MyHeaderPane(String headerText, String subHeaderText, Boolean showDivider) {

        // initialize the panel and its layout.
        setLayout(new BorderLayout());

        // set background
        setBackground(colors.getColor(UIColors.UIColor.BG_PRIMARY));

        // create the two JLabels as header and sub-header and add them in a list.
        JLabel header = new JLabel(headerText);
        initLabel(header, UIColors.UIColor.FG_HEADER, 20, 24, false);
        add(header, BorderLayout.NORTH);

        JLabel subHeader = new JLabel(subHeaderText);
        initLabel(subHeader, UIColors.UIColor.FG_SUBHEADER, 0, 14, showDivider);
        add(subHeader, BorderLayout.SOUTH);
    }

    private void initLabel(JLabel label, UIColors.UIColor uiColor, int top, int size, Boolean divider) {
        label.setForeground(colors.getColor(uiColor));
        if(divider){
            Border lineBorder = BorderFactory.createMatteBorder(0, 0, 1, 0, colors.getColor(UIColor.BORDER_GREY));
            Border emptyBorder = BorderFactory.createEmptyBorder(top, 15, 10, 10);
            label.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
        }
        else{
            label.setBorder(BorderFactory.createEmptyBorder(top, 15, 10, 10));
        }
        label.setFont(fonts.getFont(UIFonts.FontStyle.LIGHT, size));
    }

}
