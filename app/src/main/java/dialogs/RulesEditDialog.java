package dialogs;

import controls.*;
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
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class RulesEditDialog extends JDialog {

    final int winWidth = 850;
    final int winHeight = 650;
    private final UIFonts fonts = new UIFonts();
    private final UIColors colors = new UIColors();

    private MyTable table;
    MyTextField textField;
    MyTextField ruleTextField;
    MyButtonPane buttonPane;
    Boolean isUserInput;
    public Boolean savePressed = false;
    public String newText = "";
    public String newName = "";


    public RulesEditDialog(JFrame owner, String text, String name){
        super(owner, "Edit Skill", true);

        setSize(winWidth, winHeight);
        setResizable(false);

        BorderLayout layout = new BorderLayout();
        setLayout(layout);

        setBackground(colors.getColor(UIColor.BG_PRIMARY));

        MyHeaderPane headerPane = new MyHeaderPane(
                "Edit Rules",
                "Add, edit or delete specific rules here.",
                true);

        JPanel tablePane = createTablePane(text, name);

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
        String[] texts = getColumnAsArray(table, 0);
        newText = String.join(" | ", texts);
        newName = ruleTextField.getText();
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
        textField = new MyTextField();
        Border tfBorder = textField.getBorder();
        Border emptyBorder = BorderFactory.createEmptyBorder(5, 20, 5, 20);
        Border border = BorderFactory.createCompoundBorder(emptyBorder, tfBorder);
        textField.setBorder(border);

        return textField;
    }

    private JPanel createTablePane(String text, String name){

        BorderLayout layout = new BorderLayout();
        JPanel panel = new JPanel(layout);

        // create the column names and an empty 2D data array.
        String[] columnNames = {"Rule"};
        Object[][] data = {};

        // create the JTable with the DefaultTableModel.
        table = new MyTable(columnNames, data);

        // get all rules separately and add them to the JTable.
        String[] rules = text.split(" \\| ");
        for (String rule : rules) {
            Object[] rowData = {rule};
            table.dtm.addRow(rowData);
        }

        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                int selected = table.getSelectedRow();
                if (selected >= 0){
                    isUserInput = false;
                    textField.setText((String) table.getValueAt(selected, 0));
                    isUserInput = true;
                    // set editing tools enabled.
                    textField.setEnabled(true);
                    buttonPane.setButtonsEnabled(new Boolean[] {true, true});
                }
                else{
                    // set flag so action listener of textField knows that it's not a user typing.
                    isUserInput = false;
                    textField.setText("");
                    isUserInput = true;
                    // set editing tools disabled.
                    textField.setEnabled(false);
                    buttonPane.setButtonsEnabled(new Boolean[] {true, false});
                }
            }
        });

        // create the Southern JPanel.
        JPanel northPane = createNorthPane(name);
        JPanel southPane = createSouthPane();

        // create the MyTablePane, which is a JScrollbar with the JTable as its component.
        panel.add(northPane, BorderLayout.NORTH);
        panel.add(new MyTablePane(table), BorderLayout.CENTER);
        panel.add(southPane, BorderLayout.SOUTH);

        return panel;
    }


    private JPanel createNorthPane(String name){

        BorderLayout layout = new BorderLayout();
        JPanel panel = new JPanel(layout);

        // create label.
        MyLabel label = new MyLabel("Name");
        label.setBackground(colors.getColor(UIColor.BG_PRIMARY));
        label.setBorder(new EmptyBorder(15,20,5,20));
        label.setOpaque(true);

        // create a MyTextField for editing the rules.
        ruleTextField = createTextField();
        ruleTextField.setText(name);

        panel.add(label, BorderLayout.NORTH);
        panel.add(ruleTextField, BorderLayout.SOUTH);

        return panel;
    }
    private JPanel createSouthPane(){

        BorderLayout layout = new BorderLayout();
        JPanel panel = new JPanel(layout);

        // define the JButtons which will be created.
        Object[][] buttons = {
                {"New", UIColor.BTN_BG_COLORED, UIColor.BTN_BG_COLORED_PRESSED},
                {"Delete", UIColor.BTN_BG_RED, UIColor.BTN_BG_RED_PRESSED}
        };
        buttonPane = new MyButtonPane(buttons);
        buttonPane.setButtonsEnabled(new Boolean[] {true, false});

        JButton newBtn = buttonPane.buttons.get(0);
        JButton deleteBtn = buttonPane.buttons.get(1);

        newBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Object[] data = {"New rule"};
                table.dtm.addRow(data);
            }
        });
        deleteBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                // remove the selected row.
                table.dtm.removeRow(table.getSelectedRow());
            }
        });

        // create a MyTextField for editing the rules.
        textField = createTextField();

        // disable textField because nothing is selected.
        textField.setEnabled(false);

        textField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent documentEvent) {
                if (isUserInput) {
                    textUpdated();
                }
            }

            @Override
            public void removeUpdate(DocumentEvent documentEvent) {
                if (isUserInput) {
                    textUpdated();
                }
            }

            @Override
            public void changedUpdate(DocumentEvent documentEvent) {
                // not used.
            }
        });

        panel.add(textField, BorderLayout.NORTH);
        panel.add(buttonPane, BorderLayout.SOUTH);

        return panel;
    }

    private void textUpdated(){
        String text = textField.getText();
        table.setValueAt(text, table.getSelectedRow(), 0);
    }
}
