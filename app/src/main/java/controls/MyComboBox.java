package controls;

import utils.*;
import utils.UIColors.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListDataListener;
import java.awt.*;

public class MyComboBox extends JComboBox<String> {

    private final UIFonts fonts = new UIFonts();
    private final UIColors colors = new UIColors();


    public MyComboBox(String[] value) {
        super(value);

        setRenderer(new MyComboBoxRenderer());
        setEditor(new MyComboBoxEditor());

        // JComboBox must be 'editable' in order to customize the Editor.
        setEditable(true);

        setBackground(colors.getColor(UIColor.BG_PRIMARY));
        setForeground(colors.getColor(UIColor.TEXT_FG));

        setFont(fonts.getFont(UIFonts.FontStyle.LIGHT,16));

        setPreferredSize(new Dimension(200,40));

        // set an EmptyBorder as the JComboBox border.
        setBorder(new EmptyBorder(5,0,5,0));

    }

}
