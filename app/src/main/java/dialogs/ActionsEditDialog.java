package dialogs;

import controls.*;
import main.SkillData;
import utils.UIColors;
import utils.UIColors.UIColor;
import utils.UIFonts;

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

public class ActionsEditDialog extends JDialog {

    final int winWidth = 750;
    final int winHeight = 550;
    private final UIFonts fonts = new UIFonts();
    private final UIColors colors = new UIColors();

    private MyTable table;
    MyComboBox combobox;
    MyTextField answerTextField;
    public Boolean savePressed = false;
    public String[] answers;
    HashMap<String, String[]> options = new HashMap<>();


    public ActionsEditDialog(JFrame owner, SkillData skillData, String[] idNames, String[] idValues, String answer){
        super(owner, "Edit Action", true);

        options = skillData.rules;

        setSize(winWidth, winHeight);
        setResizable(false);

        BorderLayout layout = new BorderLayout();
        setLayout(layout);

        setBackground(colors.getColor(UIColor.BG_PRIMARY));

        MyHeaderPane headerPane = new MyHeaderPane(
                "Edit Actions",
                "Edit specific actions and the corresponding answer here.",
                true);

        JPanel tablePane = createTablePane(idNames, idValues, answer);

        Object[][] buttons = {
                {"Cancel", UIColor.BTN_BG_RED, UIColor.BTN_BG_RED_PRESSED},
                {"Save", UIColor.BTN_BG_COLORED, UIColor.BTN_BG_COLORED_PRESSED}
        };
        MyDialogButtonPane buttonPane = new MyDialogButtonPane(buttons);
        // add Action Listener for closing the dialog to the 'Cancel' button.
        setCloseAction(buttonPane.buttons.get(0));
        // add Action Listener for the saving of the text.
        JButton saveBtn = buttonPane.buttons.get(1);
        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                save();
            }
        });

        add(headerPane, BorderLayout.NORTH);
        add(tablePane, BorderLayout.CENTER);
        add(buttonPane, BorderLayout.SOUTH);

        // set position to center.
        setLocationRelativeTo(null);
        // show dialog.
        setVisible(true);
    }



    private void save(){
        savePressed = true;
        String[] actions = getColumnAsArray(table, 1);
        // answers should also include the answer.
        answers = new String[actions.length + 1];
        for(int i = 0; i < actions.length + 1; i++){
            if(i < actions.length) {
                answers[i] = actions[i];
            }
            else {
                answers[i] = answerTextField.getText();
            }
        }
        dispose();
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

    private void setCloseAction(JButton closeButton){
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    private MyTextField createTextField(){
        // create a MyTextField for editing the rules.
        MyTextField textField = new MyTextField();
        Border tfBorder = textField.getBorder();
        Border emptyBorder = BorderFactory.createEmptyBorder(5, 20, 5, 20);
        Border border = BorderFactory.createCompoundBorder(emptyBorder, tfBorder);
        textField.setBorder(border);

        return textField;
    }

    private JPanel createTablePane(String[] idNames, String[] idValues, String answer){

        BorderLayout layout = new BorderLayout();
        JPanel panel = new JPanel(layout);

        // create the column names and an empty 2D data array.
        String[] columnNames = {"Name", "Value"};
        Object[][] data = new Object[idNames.length][2];

        for (int i = 0; i < idNames.length; i++){
            Object[] dataItem = {idNames[i], idValues[i]};
            data[i] = dataItem;
        }

        // create the JTable with the DefaultTableModel.
        table = new MyTable(columnNames, data);

        // for every column, but the last one, set the MaxWidth.
        for(int i = 0; i <= table.columns.size() - 2 ; i++) {
            table.columns.get(i).setMinWidth(200);
            table.columns.get(i).setMaxWidth(300);
        }

        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                // get selected row.
                int row = table.getSelectedRow();
                if (row >= 0){
                    // get the selected rows values.
                    String name = (String) table.getValueAt(row, 0);
                    String value = (String) table.getValueAt(row, 1);
                    // initialize a new JComboBox Model (with the new values).
                    String[] items;
                    try {
                        String[] optionItems = options.get(name);
                        items = new String[optionItems.length + 1];
                        for(int i = 0; i < optionItems.length + 1; i++){
                            if (i == 0) {
                                items[i] = "*";
                            } else {
                                items[i] = optionItems[i-1];
                            }
                        }
                    }
                    catch (Exception e) {
                        System.out.println(String.format("name : '%s' not found in options HashMap...", name));
                        items = new String[] {"*"};
                    }
                    DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(items);
                    combobox.setModel(model);
                    // select the current value.
                    if(model.getIndexOf(value) == -1){
                        // value not in JComboBox, so add it.
                        model.addElement(value);
                    }
                    model.setSelectedItem(value);
                    // set editing tools enabled.
                    combobox.setEnabled(true);
                }
                else{
                    // set editing tools disabled.
                    combobox.getModel().setSelectedItem("*");
                    combobox.setEnabled(false);
                }
            }
        });

        // create the Southern JPanel.
        JPanel northPane = createNorthPane(answer);
        JPanel southPane = createSouthPane();

        // create the MyTablePane, which is a JScrollbar with the JTable as its component.
        panel.add(northPane, BorderLayout.NORTH);
        panel.add(new MyTablePane(table), BorderLayout.CENTER);
        panel.add(southPane, BorderLayout.SOUTH);

        return panel;
    }


    private JPanel createNorthPane(String answer){

        BorderLayout layout = new BorderLayout();
        JPanel panel = new JPanel(layout);

        // create label.
        MyLabel label = new MyLabel("Answer");
        label.setBackground(colors.getColor(UIColor.BG_PRIMARY));
        label.setBorder(new EmptyBorder(15,20,5,20));
        label.setOpaque(true);

        // create a MyTextField for editing the rules.
        answerTextField = createTextField();
        answerTextField.setText(answer);

        panel.add(label, BorderLayout.NORTH);
        panel.add(answerTextField, BorderLayout.SOUTH);

        return panel;
    }
    private JPanel createSouthPane(){

        BorderLayout layout = new BorderLayout();
        JPanel panel = new JPanel(layout);

        layout.setVgap(20);
        panel.setBackground(colors.getColor(UIColor.BG_PRIMARY));

        // create a MyComboBox for editing the actions.
        combobox = new MyComboBox(new String[] {"*"});

        combobox.setBorder(new EmptyBorder(5,20,5,20));
        combobox.setEnabled(false);

        combobox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent itemEvent) {
                // get selected row.
                int row = table.getSelectedRow();
                String value = (String) combobox.getSelectedItem();
                table.setValueAt(value, row, 1);
            }
        });

        //panel.add(textField, BorderLayout.NORTH);
        panel.add(combobox, BorderLayout.NORTH);

        return panel;
    }

}
