package controls;

import utils.*;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class MyLineButton extends JButton {

    private final UIFonts fonts = new UIFonts();
    private final UIColors colors = new UIColors();

    int spacing = 10;
    int lineThickness = 2;
    Color lineColor = new Color(0, 0, 0);
    Color textColorOn = new Color(0,0,0);
    Color textColorOff = new Color(100,100,100);

    public MyLineButton(String text){
        super(text);

        // set the button settings & layout variables.
        setBorderPainted(true);
        setContentAreaFilled(false);
        setFocusPainted(false);

        setTextColorOff(colors.getColor(UIColors.UIColor.BTN_FG_OFF));
        setTextColorOn(colors.getColor(UIColors.UIColor.BTN_FG_ON));
        setFont(fonts.getFont(UIFonts.FontStyle.MEDIUM, 16));
        setLineThickness(3);
        setLineColor(colors.getColor(UIColors.UIColor.BORDER_COLORED));

        update();
    }


    @Override
    public void setSelected(boolean b) {
        super.setSelected(b);
        // update the look of the button
        update();
    }

    private void update(){
        if (isSelected()) {
            Border bottomBorder = BorderFactory.createMatteBorder(0, 0, lineThickness, 0, lineColor);
            Border topBorder = BorderFactory.createEmptyBorder(spacing, spacing, spacing-lineThickness, spacing);
            Border border = BorderFactory.createCompoundBorder(bottomBorder, topBorder);
            setBorder(border);
            setForeground(textColorOn);
        }
        else {
            Border border = BorderFactory.createEmptyBorder(spacing, spacing, spacing, spacing);
            setBorder(border);
            setForeground(textColorOff);
        }
    }

    public void setSpacing(int value){
        spacing = value;
        update();
    }

    public void setLineThickness(int value){
        lineThickness = value;
        update();
    }

    public void setLineColor(Color value){
        lineColor = value;
        update();
    }

    public void setTextColorOn(Color value){
        textColorOn = value;
        update();
    }

    public void setTextColorOff(Color value){
        textColorOff = value;
        update();
    }

}
