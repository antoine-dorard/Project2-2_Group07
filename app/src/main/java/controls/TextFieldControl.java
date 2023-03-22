package controls;

import utils.ConfigUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import static javax.swing.BorderFactory.createLineBorder;


public class TextFieldControl extends JTextField {

    ConfigUI configUI = new ConfigUI();
    int padding = 8;

    public TextFieldControl(String name, int width, int height){
        super();

        // define the textField's appearance.
        setBackground(new Color(46,49,53));
        setFont(configUI.fontList);
        setBorder(createLineBorder(new Color(80,80,80), 1));
        setForeground(Color.WHITE);
        updateUI();
        setName(name);
        setPreferredSize(new Dimension(width, height));

        // set the caret color, the same as the foreground color.
        setCaretColor(getForeground());
        getCaret().setVisible(false);

        // set a text color, for when this control is disabled.
        setDisabledTextColor(new Color(80,80,80));

        // add some padding to the text in the JTextField.
        setBorder(BorderFactory.createCompoundBorder(
                getBorder(),
                BorderFactory.createEmptyBorder(padding, padding, padding, padding)));

        addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent focusEvent) {
                getCaret().setVisible(true);
            }
            @Override
            public void focusLost(FocusEvent focusEvent) {
                getCaret().setVisible(false);
            }
        });
    }

    @Override
    public void setEnabled(boolean toggle) {
        super.setEnabled(toggle);
        if (!toggle){
            // if control was disabled, set some disabled text to inform user.
            setText("nothing selected");
        }
    }

    @Override
    public String getText() {
        if (super.isEnabled()){
            // Control is enabled, just return the text.
            return super.getText();
        }
        else {
            // Control is disabled, return empty text.
            return "";
        }
    }
}
