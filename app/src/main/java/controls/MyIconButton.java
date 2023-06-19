package controls;


import utils.*;
import utils.UIColors.*;
import utils.UIFonts.*;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class MyIconButton extends JToggleButton {

    private final UIFonts fonts = new UIFonts();
    private final UIColors colors = new UIColors();

    private String imgFolder = "app\\src\\main\\resources\\imgs\\";


    public MyIconButton(String name, String toolTipText) {
        super();

        setName(name);

        // create button icons for specific states in the imgs folder.
        ImageIcon iconOff = new ImageIcon(imgFolder + name.toLowerCase() + "_off.png");
        ImageIcon iconHover = new ImageIcon(imgFolder + name.toLowerCase() + "_hover.png");
        ImageIcon iconOn = new ImageIcon(imgFolder + name.toLowerCase() + "_on.png");

        // set the icons off different states.
        setIcon(iconOff);
        setRolloverIcon(iconHover);
        setSelectedIcon(iconOn);
        setRolloverSelectedIcon(iconHover);

        // only relevant for send button.
        setDisabledIcon(iconHover);

        // set the button settings & layout variables.
        setBorderPainted(false);
        setContentAreaFilled(false);
        setFocusPainted(false);

        if(!toolTipText.equals("")){
            // only set ToolTipText and Name if it isn't empty.
            setToolTipText(toolTipText);
        }

    }

    @Override
    public JToolTip createToolTip() {
        JToolTip tip = super.createToolTip();
        tip.setBackground(colors.getColor(UIColor.BG_NAVIGATION));
        tip.setForeground(colors.getColor(UIColor.FG_NAVIGATION));
        tip.setFont(fonts.getFont(FontStyle.MEDIUM, 16));
        ToolTipManager.sharedInstance().setInitialDelay(0);
        return tip;
    }

}