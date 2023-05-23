package controls;

import utils.*;
import utils.UIFonts.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MyButtonPane extends JPanel {

    private final UIFonts fonts = new UIFonts();
    private final UIColors colors = new UIColors();

    public ArrayList<JButton> buttons = new ArrayList<>();


    public MyButtonPane(Object[][] data) {
        super();

        FlowLayout layout = new FlowLayout();
        setLayout(layout);

        // set Alignment of button to the LEFT.
        layout.setAlignment(FlowLayout.LEFT);
        layout.setHgap(0);
        layout.setVgap(10);

        // set the background of this JPanel.
        setBackground(colors.getColor(UIColors.UIColor.BG_PRIMARY));

        // create the left spacing of the first JButton.
        Component leftSpacing = Box.createHorizontalStrut(20);
        add(leftSpacing);

        for(Object[] dataItem : data) {
            // index current button attributes.
            String name = (String) dataItem[0];
            UIColors.UIColor colorEnum = (UIColors.UIColor) dataItem[1];
            UIColors.UIColor colorPressedEnum = (UIColors.UIColor) dataItem[2];

            // create a new JButton.
            JButton btn = new JButton(name) {
                @Override
                public void setEnabled(boolean b) {
                    super.setEnabled(b);
                    if(b) {
                        setBackground(colors.getColor(colorEnum));
                    }
                    else {
                        setBackground(new Color(100,100,100));
                    }
                }
            };

            // set the appearance of the JButton.
            btn.setBackground(colors.getColor(colorEnum));
            btn.setForeground(colors.getColor(UIColors.UIColor.BTN_FG_ON));
            btn.setFont(fonts.getFont(FontStyle.MEDIUM, 14));

            btn.setBorderPainted(false);
            btn.setFocusPainted(false);
            btn.setPreferredSize(new Dimension(200, 40));

            // create the space between this JButton and the next one.
            Component space = Box.createHorizontalStrut(20);

            // add the JButton and the space into this JPanel.
            add(btn);
            add(space);

            // add it to the 'buttons' ArrayList.
            buttons.add(btn);
        }
    }


    public void setButtonsEnabled(Boolean[] boolList) {

        int smallest = Math.min(boolList.length, buttons.toArray().length);

        for (int i = 0; i < smallest; i++) {
            buttons.get(i).setEnabled(boolList[i]);
        }
    }


}
