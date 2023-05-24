package controls;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

import utils.*;
import utils.UIColors.*;
import utils.UIFonts.*;

class MyTableHeaderRenderer extends JLabel implements TableCellRenderer {

    private final UIFonts fonts = new UIFonts();
    private final UIColors colors = new UIColors();


    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                   boolean hasFocus, int rowIndex, int vColIndex) {
        setText(value.toString());
        setToolTipText((String) value);

        setForeground(colors.getColor(UIColor.TABLE_FG_HEADER));
        setFont(fonts.getFont(FontStyle.MEDIUM, 16));
        setBorder(new EmptyBorder(20,20,20,20));
        return this;
    }
}