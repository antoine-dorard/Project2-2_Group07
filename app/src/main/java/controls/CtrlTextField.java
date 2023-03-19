package controls;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class CtrlTextField extends JTextField {

    public JTextField textField;

    private String placeholder = new String ("Write a message....");

    public CtrlTextField() {
        super();
        addFocusListener(
                new FocusListener() {
                    @Override
                    public void focusGained(FocusEvent focusEvent) {
                        System.out.println("Focus Gained");
                        if (CtrlTextField.super.getText().equals(placeholder)){
                            CtrlTextField.super.setText("");
                        }
                    }
                    @Override
                    public void focusLost(FocusEvent focusEvent) {
                        System.out.println("Focus Lost");
                        if (CtrlTextField.super.getText().equals("")){
                            CtrlTextField.super.setText(placeholder);
                        }
                    }
                }
        );
    }

    @Override
    public String getText() {
        if (super.getText().equals(placeholder)) {
            return (String) "";
        }
        return (String) super.getText();
    }
}


