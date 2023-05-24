package panels;

import controls.MyButtonPane;
import controls.MyTable;
import controls.MyTablePane;
import dialogs.SkillEditDialog;
import main.SkillData;
import utils.*;
import utils.UIColors.*;
import utils.UIFonts.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EditorSkillsPanel extends JPanel {

    private final UIFonts fonts = new UIFonts();
    private final UIColors colors = new UIColors();

    public MyTable table;

    private Object[][] tableData;
    // create the column names.
    private String[] columnNames = {"Skills"};
    private SkillData skillData;



    public EditorSkillsPanel(JFrame ownerFrame) {
        super();
        BorderLayout layout = new BorderLayout();
        this.setLayout(layout);

        // set the background of this JPanel.
        setBackground(colors.getColor(UIColor.BG_PRIMARY));

        // create a spacer with height 10px.
        Component vSpacer = Box.createVerticalStrut(10);

        // create the MyButtonPane that holds all the JButtons.
        MyButtonPane buttonPane = createButtonPane(ownerFrame);

        // create the MyTablePane that holds the JTable.
        MyTablePane tablePane = createTablePane(buttonPane);

        // Add the JScrollPanel to the current JPanel.
        add(vSpacer, BorderLayout.NORTH);
        add(tablePane, BorderLayout.CENTER);
        add(buttonPane, BorderLayout.SOUTH);
    }


    public void updateAll(SkillData data){
        skillData = data;
        // Create the data for the table
        tableData = new Object[skillData.skills.length][1];

        for(int i = 0; i < skillData.skills.length; i++) {
            tableData[i] = new Object[]{skillData.skills[i]};
        }
        table.updateData(tableData, columnNames);
    }

    public void saveAll(){
        // update skill data.
        skillData.setSkills(getColumnAsArray(table, 0));
    }

    private String[] getColumnAsArray(JTable table, int columnIndex) {
        int rowCount = table.getRowCount();
        String[] columnArray = new String[rowCount];
        for (int i = 0; i < rowCount; i++) {
            Object value = table.getValueAt(i, columnIndex);
            columnArray[i] = value != null ? value.toString() : "";
        }
        return columnArray;
    }


    private MyTablePane createTablePane(MyButtonPane buttonPane){
        // Create the data for the table
        Object[][] data = {
                {"SCHEDULE"},
                {"LOCATION"}
        };

        // create the JTable with the DefaultTableModel.
        table = new MyTable(columnNames, data);

        // set action listener for the JTable.
        table.setActionListener(buttonPane, false);

        // create the MyTablePane, which is a JScrollbar with the JTable as its component.
        return new MyTablePane(table);
    }


    private MyButtonPane createButtonPane(JFrame ownerFrame) {
        // define the JButtons which will be created.
        Object[][] buttons = {
                {"New", UIColor.BTN_BG_COLORED, UIColor.BTN_BG_COLORED_PRESSED},
                {"Edit", UIColor.BTN_BG_COLORED, UIColor.BTN_BG_COLORED_PRESSED},
                {"Delete", UIColor.BTN_BG_RED, UIColor.BTN_BG_RED_PRESSED},
        };

        // create the JPanel that holds all the JButtons.
        MyButtonPane buttonPane = new MyButtonPane(buttons);

        // start off with two buttons disabled.
        buttonPane.setButtonsEnabled(new Boolean[]{true, false, false});

        // get buttons separately to apply their defined actions.
        JButton newBtn = buttonPane.buttons.get(0);
        JButton editBtn = buttonPane.buttons.get(1);
        JButton deleteBtn = buttonPane.buttons.get(2);

        newBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                SkillEditDialog dialog = new SkillEditDialog(ownerFrame, "NEW");

                if (dialog.savePressed){
                    // set the new value in the JTable.
                    Object[] newRowData = {dialog.newText};
                    table.dtm.addRow(newRowData);
                    saveAll();
                }
            }
        });

        editBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                int row = table.getSelectedRow();
                Object value = table.getValueAt(row, 0);
                String text = (String) value;

                SkillEditDialog dialog = new SkillEditDialog(ownerFrame, text);

                if (dialog.savePressed){
                    // set the new value in the JTable.
                    table.setValueAt(dialog.newText, row, 0);
                    saveAll();
                }
            }
        });

        deleteBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                // remove the selected row.
                table.dtm.removeRow(table.getSelectedRow());
                saveAll();
            }
        });

        return buttonPane;
    }

}
