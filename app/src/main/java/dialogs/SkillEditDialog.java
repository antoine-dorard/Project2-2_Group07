package dialogs;

import controls.MyDialogButtonPane;
import controls.MyHeaderPane;
import controls.MyTextField;
import controls.MyUppercaseDocumentFilter;
import utils.*;
import utils.UIColors.*;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.awt.event.*;

public class SkillEditDialog extends JDialog {

    final int winWidth = 550;
    final int winHeight = 420;
    private final UIFonts fonts = new UIFonts();
    private final UIColors colors = new UIColors();

    private MyTextField textField;
    public Boolean savePressed = false;
    public String newText = "";


    public SkillEditDialog(JFrame owner, String text){
        super(owner, "Edit Skill", true);

        setSize(winWidth, winHeight);

        BorderLayout layout = new BorderLayout();
        setLayout(layout);

        setBackground(colors.getColor(UIColor.BG_PRIMARY));

        MyHeaderPane headerPane = new MyHeaderPane(
                "Edit Skill",
                "Change the name of your skill here.",
                true);

        JPanel centerPane = createCenterPane(text);

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
        add(centerPane, BorderLayout.CENTER);
        add(buttonPane, BorderLayout.SOUTH);

        // set position to center.
        setLocationRelativeTo(null);
        // show dialog.
        setVisible(true);
    }


    private void save(){
        savePressed = true;
        newText = textField.getText();
        dispose();
    }

    private void setCloseAction(JButton closeButton){
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    private JPanel createCenterPane(String text) {
        FlowLayout layout = new FlowLayout(FlowLayout.CENTER);
        JPanel panel = new JPanel(layout);

        panel.setBackground(colors.getColor(UIColor.BG_PRIMARY));
        layout.setVgap(40);

        textField = new MyTextField();
        textField.setPreferredSize(new Dimension(400,30));
        textField.setText(text);

        // define the DocumentFilter, so that it converts every input to an uppercase.
        ((AbstractDocument) textField.getDocument()).setDocumentFilter(new MyUppercaseDocumentFilter());
        textField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {
            }
            @Override
            public void keyPressed(KeyEvent keyEvent) {
                if(keyEvent.getKeyCode() == KeyEvent.VK_ENTER){
                    save();
                }
            }
            @Override
            public void keyReleased(KeyEvent keyEvent) {
            }
        });

        // create the left spacing of the first JButton.
        Component space = Box.createVerticalStrut(20);
        panel.add(space);
        panel.add(textField);

        return panel;
    }


    static class UppercaseDocumentFilter extends DocumentFilter {
        public void insertString(FilterBypass fb, int offset, String text, AttributeSet attr)
                throws BadLocationException {
            fb.insertString(offset, text.toUpperCase().replaceAll("\\s", ""), attr);
        }

        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                throws BadLocationException {
            fb.replace(offset, length, text.toUpperCase().replaceAll("\\s", ""), attrs);
        }
    }

}
