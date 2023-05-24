package controls;

import utils.*;
import utils.UIColors.*;
import utils.UIFonts.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MyTablePane extends JScrollPane {

    private final UIFonts fonts = new UIFonts();
    private final UIColors colors = new UIColors();

    public JViewport viewport;


    public MyTablePane(Component view) {
        super(view);

        // get the Viewport of the JScrollPane.
        viewport = getViewport();

        // set appearance of the JScrollPane.
        setBackground(colors.getColor(UIColors.UIColor.BG_PRIMARY));
        setBorder(new EmptyBorder(0,0,0,0));

        // set appearance of the Viewport.
        viewport.setBackground(colors.getColor(UIColors.UIColor.BG_PRIMARY));
        setViewportBorder(new EmptyBorder(0,20,20,20));
    }


}
