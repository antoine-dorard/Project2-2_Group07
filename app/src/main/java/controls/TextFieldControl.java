package controls;

import utils.ConfigUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import static javax.swing.BorderFactory.createLineBorder;


public class TextFieldControl extends JTextField {

    ConfigUI configUI = new ConfigUI();
    int padding = 5;

    public JPanel labelPane = new JPanel();
    private BoxLayout labelLayout = new BoxLayout(labelPane, BoxLayout.Y_AXIS);

    public TextFieldControl(String name, int width, int height){
        super();

        // define the textField's appearance.
        //setBackground(new Color(46,49,53));
        setBackground(new Color(63,63,63));
        setFont(configUI.fontList);
        setBorder(createLineBorder(new Color(80,80,80), 1));
        setForeground(configUI.colorListFG);
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


        labelPane.setLayout(labelLayout);
        // create the label with the name.
        JLabel label = new JLabel();
        label.setForeground(new Color(255,255,255));
        label.setText(name);
        label.setBackground(new Color(68,68,68));
        label.setOpaque(true);

        // set both alignments to the left.
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        setAlignmentX(Component.LEFT_ALIGNMENT);

        setEnabled(true);
        labelPane.add(label);
        labelPane.add(this);

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
