package controls;

import utils.*;
import utils.UIColors.*;
import utils.UIFonts.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicComboBoxEditor;
import java.awt.*;

public class MyComboBoxEditor extends BasicComboBoxEditor {
    private final UIFonts fonts = new UIFonts();
    private final UIColors colors = new UIColors();
    private JLabel label = new JLabel();
    private JPanel panel = new JPanel();
    private Object selectedItem;

    public MyComboBoxEditor() {

        label.setOpaque(true);
        label.setFont(fonts.getFont(FontStyle.LIGHT, 16));
        label.setBackground(colors.getColor(UIColor.BG_PRIMARY));
        label.setForeground(colors.getColor(UIColor.TEXT_FG));

        label.setBorder(new EmptyBorder(0,5,0,5));

        panel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 2));
        panel.add(label);
        panel.setBackground(colors.getColor(UIColor.BG_PRIMARY));

        panel.setBorder(new LineBorder(colors.getColor(UIColor.BORDER_GREY), 1));

    }

    public Component getEditorComponent() {
        return this.panel;
    }

    public Object getItem() {
        return this.selectedItem.toString();
    }

    public void setItem(Object item) {
        this.selectedItem = item;
        label.setText(item.toString());
    }

}
