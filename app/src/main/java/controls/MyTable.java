package controls;
import main.SkillData;
import utils.*;
import utils.UIColors.*;
import utils.UIFonts.*;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class MyTable extends JTable {

    private final UIFonts fonts = new UIFonts();
    private final UIColors colors = new UIColors();

    public DefaultTableModel dtm;
    public DefaultTableCellRenderer dtcr;
    public JTableHeader header;
    public ArrayList<TableColumn> columns = new ArrayList<>();

    public SkillData skillData;



    public MyTable(String[] columnNames, Object[][] data){
        super(new DefaultTableModel(data, columnNames){
            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
                return false;
            }
        });

        // create a DefaultTableModel to hold the data and column names.
        dtm = (DefaultTableModel) getModel();
        dtcr = new MyTableCellRenderer();
        header = getTableHeader();

        // Get the TableColumns from MyTable.
        for (int i = 0; i < columnNames.length; i++) {
            TableColumn column = getColumnModel().getColumn(i);
            column.setHeaderRenderer(new MyTableHeaderRenderer());
            columns.add(column);
        }

        // set TableRenderer.
        setDefaultRenderer(Object.class, dtcr);

        // set Horizontal Alignment of the cells
        dtcr.setHorizontalAlignment(DefaultTableCellRenderer.LEFT);

        // show vertical and/or horizontal lines
        setShowHorizontalLines(true);
        setShowVerticalLines(false);

        // set selection colors
        setSelectionBackground(new Color(75,110,175));
        setSelectionForeground(new Color(187,187,187));

        // set header appearance.
        header.setBackground(colors.getColor(UIColors.UIColor.BG_PRIMARY));
        header.setForeground(colors.getColor(UIColors.UIColor.TABLE_FG_HEADER));
        header.setFont(fonts.getFont(UIFonts.FontStyle.MEDIUM, 14));

        ((DefaultTableCellRenderer)header.getDefaultRenderer()).setHorizontalAlignment(JLabel.LEFT);

        setBackground(colors.getColor(UIColor.BG_PRIMARY));
        setForeground(colors.getColor(UIColor.TABLE_FG));

        setSelectionBackground(colors.getColor(UIColor.TABLE_BG_SELECTION));
        setSelectionForeground(colors.getColor(UIColor.TABLE_FG_SELECTION));

        setFont(fonts.getFont(FontStyle.REGULAR, 14));
        setRowHeight(40);

        setGridColor(colors.getColor(UIColor.BORDER_GREY));

    }


    public void updateData(Object[][] data, String[] columnNames){

        // rename the only column left over.
        dtm.setDataVector(data, columnNames);

        header = getTableHeader();

        // Get the TableColumns from MyTable.
        for (int i = 0; i < columnNames.length; i++) {
            TableColumn column = getColumnModel().getColumn(i);
            column.setHeaderRenderer(new MyTableHeaderRenderer());
            columns.add(column);
            // only the last has a long column width.
            if (i < columnNames.length - 1){
                column.setMinWidth(200);
                column.setMaxWidth(300);
            }
        }

    }

    public void setActionListener(MyButtonPane buttonPane, Boolean isRules) {
        getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                if (getSelectedRow() >= 0) {
                    if (isRules) {
                        String selected = (String) dtm.getValueAt(getSelectedRow(), 0);
                        if (selected.equals("S") | selected.equals("ACTION")) {
                            // if selected is 'S' or 'ACTIONS', don't allow the user to edit or delete.
                            buttonPane.setButtonsEnabled(new Boolean[]{true, false, false});
                        } else {
                            Boolean isSkill = false;
                            for(String skill : skillData.skills) {
                                if (skill.equals(selected)) {
                                    isSkill = true;
                                    break;
                                }
                            }
                            if (isSkill) {
                                // if selected is on off the skills, don't allow the user to delete.
                                buttonPane.setButtonsEnabled(new Boolean[]{true, true, false});
                            } else {
                                buttonPane.setButtonsEnabled(new Boolean[]{true, true, true});
                            }
                        }
                    } else {
                        buttonPane.setButtonsEnabled(new Boolean[]{true, true, true});
                    }
                }
                else {
                    buttonPane.setButtonsEnabled(new Boolean[]{true, false, false});
                }
            }
        });
    }

}
