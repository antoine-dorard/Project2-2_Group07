package controls;

import javax.swing.*;
import java.awt.*;

import static javax.swing.BorderFactory.createLineBorder;


public class TextFieldControl extends JTextField {

    int padding = 8;

    public TextFieldControl(int width, int height){
        super();

        // define the textField's appearance.
        setBackground(new Color(46,49,53));
        setFont(new Font("Monospaced", Font.BOLD, 16));//configUI.fontText);
        setBorder(createLineBorder(new Color(80,80,80), 1));
        setForeground(Color.WHITE);
        updateUI();
        setPreferredSize(new Dimension(width, height));

        // set a text color, for when this control is disabled.
        setDisabledTextColor(new Color(80,80,80));

        // add some padding to the text in the JTextField.
        setBorder(BorderFactory.createCompoundBorder(
                getBorder(),
                BorderFactory.createEmptyBorder(padding, padding, padding, padding)));
    }

    @Override
    public void setEnabled(boolean toggle) {
        super.setEnabled(toggle);
        if (!toggle){
            // if control was disabled, set some disabled text to inform user.
            setText("nothing selected");
        }
    }
}
