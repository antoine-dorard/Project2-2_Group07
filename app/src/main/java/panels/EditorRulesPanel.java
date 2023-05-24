package panels;

import controls.MyButtonPane;
import controls.MyTable;
import controls.MyTablePane;
import dialogs.RulesEditDialog;
import dialogs.SkillEditDialog;
import main.SkillData;
import utils.*;
import utils.UIColors.*;
import utils.UIFonts.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.Set;

public class EditorRulesPanel extends JPanel {

    private final UIFonts fonts = new UIFonts();
    private final UIColors colors = new UIColors();

    private MyTable table;
    private Object[][] tableData;
    String[] columnNames = {"Token", "Value"};
    private SkillData skillData;



    public EditorRulesPanel(JFrame ownerFrame) {
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
        table.skillData = data;

        // get entry Set.
        Map.Entry<Integer, String>[] entry = skillData.rules.entrySet().toArray(new Map.Entry[0]);

        // create keys String array.
        String[] keys = new String[entry.length];

        // set every index of keys String corresponding to the entry Set.
        for (int i = 0; i < entry.length; i++) {
            keys[i] = String.valueOf(entry[i].getKey());
        }

        // create data for table.
        tableData = new Object[keys.length][2];

        for(int i = 0; i < keys.length; i++) {
            String key = keys[i];
            String rulesStr = String.join(" | ", skillData.rules.get(key));
            tableData[i] = new String[] {key, rulesStr};
        }

        table.updateData(tableData, columnNames);
    }

    public void saveAll(){
        String[] keys = getColumnAsArray(table, 0);
        String[] values = getColumnAsArray(table, 1);
        skillData.setRules(keys, values);
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
        Object[][] data = {};

        // create the JTable with the DefaultTableModel.
        table = new MyTable(columnNames, data);
        table.setActionListener(buttonPane, true);

        table.columns.get(0).setMinWidth(200);
        table.columns.get(0).setMaxWidth(300);

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

                RulesEditDialog dialog = new RulesEditDialog(
                        ownerFrame, "", "NEW", getColumnAsArray(table, 0), skillData.skills);

                if (dialog.savePressed){
                    // set the new value in the JTable.
                    Object[] rowData = {dialog.newName, dialog.newText};
                    table.dtm.addRow(rowData);
                    saveAll();
                }
            }
        });

        editBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                int row = table.getSelectedRow();
                String name = (String) table.getValueAt(row, 0);
                String text = (String) table.getValueAt(row, 1);

                RulesEditDialog dialog = new RulesEditDialog(
                        ownerFrame, text, name, getColumnAsArray(table, 0), skillData.skills);

                if (dialog.savePressed){
                    // set the new value in the JTable.
                    table.setValueAt(dialog.newName, row, 0);
                    table.setValueAt(dialog.newText, row, 1);
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
