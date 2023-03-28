package controls;

import javax.swing.*;

public class ButtonPane extends JPanel {

    public ButtonPane(){
        super();

        BoxLayout layout = new BoxLayout(this, BoxLayout.X_AXIS);
        setLayout(layout);
    }

    public void addButtons(String[] buttonTexts){
        for(String btnTxt: buttonTexts){
            ButtonControl newBtn = new ButtonControl();
            newBtn.setText(btnTxt);
            add(newBtn);
        }
    }
}
