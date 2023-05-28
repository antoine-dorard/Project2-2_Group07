package panels;

import controls.*;
import dialogs.ActionsEditDialog;
import dialogs.RulesEditDialog;
import main.SkillData;
import utils.*;
import utils.UIColors.*;
import utils.UIFonts.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Arrays;
import java.util.HashMap;

public class EditorActionsPanel extends JPanel {

    private final UIFonts fonts = new UIFonts();
    private final UIColors colors = new UIColors();

    MyTablePane tablePane;
    MyTextField textField;
    Boolean isUserInput = true;
    MyComboBox comboBox;
    Boolean cbUserInput = true;

    MyTable table;
    private Object[][] tableData;
    String[] columnNames = {"DAY", "TIME", "Answer"};

    SkillData skillData;



    public EditorActionsPanel(JFrame ownerFrame) {
        super();
        BorderLayout layout = new BorderLayout();
        this.setLayout(layout);

        // set the background of this JPanel.
        setBackground(colors.getColor(UIColor.BG_PRIMARY));

        // initialize the JPanel that contains the JComboBox & JTextField.
        JPanel northPane = createNorthPane();

        // create both the Center and South panes from predefined MyPane classes.
        MyButtonPane southPane = createButtonPane(ownerFrame);
        MyTablePane centerPane = createTablePane(southPane);

        // add everything to the current JPanel.
        this.add(northPane, BorderLayout.NORTH);
        this.add(centerPane, BorderLayout.CENTER);
        this.add(southPane, BorderLayout.SOUTH);
    }


    public void updateAll(SkillData data){
        skillData = data;

        // comboBox should contain all skills
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(skillData.skills);
        comboBox.setModel(model);
        comboBox.setSelectedIndex(0);

        cbUserInput = false;
        updateTable((String) comboBox.getSelectedItem());
        cbUserInput = true;

        // update answer TextField.
        isUserInput = false;
        textField.setText(skillData.defaultAnswer);
        isUserInput = true;
    }

    private void updateTable(String skill) {
        // update table data and column names.
        tableData = skillData.actions.get(skill);

        String[] actionIDs = skillData.actionIDs.get(skill);
        columnNames = new String[actionIDs.length + 1];
        for (int i = 0; i < actionIDs.length + 1; i++){
            if (i < actionIDs.length){
                columnNames[i] = actionIDs[i];
            } else {
                columnNames[i] = "Answer";
            }
        }

        // update everything to JTable.
        table.updateData(tableData, columnNames);
    }

    public void saveDefault(){
        skillData.setDefaultAnswer(textField.getText());
    }
    public void saveAll(){
        String skill = (String) comboBox.getSelectedItem();

        int rowCount = table.dtm.getRowCount();
        int columnCount = table.dtm.getColumnCount();
        String[][] data = new String[rowCount][columnCount];

        for (int row = 0; row < rowCount; row++) {
            for (int column = 0; column < columnCount; column++) {
                data[row][column] = (String) table.dtm.getValueAt(row, column);
            }
        }

        skillData.setActions(skill, data);
    }


    private JPanel createNorthPane() {
        // initialize the panel and its layout.
        BorderLayout layout = new BorderLayout();
        JPanel panel = new JPanel(layout);

        // set the background of this JPanel.
        panel.setBackground(colors.getColor(UIColor.BG_PRIMARY));

        // set some spacing between the components and the borders.
        panel.setBorder(new EmptyBorder(20,20,0,20));

        // initialize the JPanel that contains the JComboBox.
        JPanel cbPane = createComboBoxPane();

        // create the JLabel that will be the header for the JTextField.
        MyLabel label = new MyLabel("Default Answer");

        // create a JTextField to input the 'Default' answer/action.
        textField = new MyTextField();
        textField.setPreferredSize(new Dimension(400,30));
        textField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent documentEvent) {
                if (isUserInput) {
                    saveDefault();
                }
            }

            @Override
            public void removeUpdate(DocumentEvent documentEvent) {
                if (isUserInput) {
                    saveDefault();
                }
            }

            @Override
            public void changedUpdate(DocumentEvent documentEvent) {
                // not used.
            }
        });

        panel.add(label, BorderLayout.NORTH);
        panel.add(textField, BorderLayout.WEST);
        panel.add(cbPane, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createComboBoxPane() {
        // initialize the panel and its layout.
        FlowLayout layout = new FlowLayout(FlowLayout.RIGHT);//.LEFT);
        JPanel panel = new JPanel(layout);

        panel.setBackground(colors.getColor(UIColor.BG_PRIMARY));
        layout.setVgap(0);
        layout.setHgap(0);

        // create a new MyComboBox.
        comboBox = new MyComboBox(new String[] {"SCHEDULE", "LOCATION"});

        comboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent itemEvent) {
                // switch to another skill.
                if(cbUserInput) {
                    updateTable((String) comboBox.getSelectedItem());
                }
            }
        });

        // add to the current JPanel.
        panel.add(comboBox);

        return panel;
    }


    private MyTablePane createTablePane(MyButtonPane buttonPane){
        // Create the data for the table
        Object[][] data = {
                {"Saturday", "*", "There are no lectures on Saturday"},
                {"Sunday", "*", "There are no lectures on Sunday"}
        };

        // create the JTable with the DefaultTableModel.
        table = new MyTable(columnNames, data);

        // set action listener for the JTable.
        table.setActionListener(buttonPane, false);

        // for every column, but the last one, set the MaxWidth.
        for(int i = 0; i <= table.columns.size() - 2 ; i++) {
            table.columns.get(i).setMinWidth(200);
            table.columns.get(i).setMaxWidth(300);
        }

        // create the MyTablePane, which is a JScrollbar with the JTable as its component.
        tablePane = new MyTablePane(table);

        return tablePane;
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

                int columnCount = table.getColumnCount();

                String[] colValues = new String[columnCount - 1];
                for(int i = 0; i < columnCount - 1; i++) {
                    colValues[i] = "*";
                }
                String[] colNames = Arrays.copyOfRange(columnNames, 0, columnCount - 1);

                ActionsEditDialog dialog = new ActionsEditDialog(
                        ownerFrame, skillData,
                        colNames, colValues, "New Answer");

                if (dialog.savePressed){
                    // set the new value in the JTable.
                    table.dtm.addRow(dialog.answers);
                    saveAll();
                }
            }
        });

        editBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                int row = table.getSelectedRow();
                int columnCount = table.getColumnCount();
                String answer = (String) table.getValueAt(row, columnCount - 1);

                String[] colValues = new String[columnCount - 1];
                for(int i = 0; i < columnCount - 1; i++){
                    colValues[i] = (String) table.getValueAt(row, i);
                }
                String[] colNames = Arrays.copyOfRange(columnNames, 0, columnCount - 1);

                ActionsEditDialog dialog = new ActionsEditDialog(
                        ownerFrame, skillData,
                        colNames, colValues, answer);

                if (dialog.savePressed){
                    // set the new value in the JTable.
                    for(int i = 0; i < dialog.answers.length; i++) {
                        String newAnswer = dialog.answers[i];
                        table.setValueAt(newAnswer, row, i);
                    }
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
