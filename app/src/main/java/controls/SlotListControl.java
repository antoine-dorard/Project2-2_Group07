package controls;

import utils.*;
import javax.swing.*;
import java.awt.*;

public class SlotListControl extends JPanel {

    TextFieldControl actionsField = new TextFieldControl("actions", 100, 30);
    ConfigUI configUI = new ConfigUI();
    public JButton removeBtn = new JButton();

    public SlotListControl(){
        super();

        BoxLayout layout = new BoxLayout(this, BoxLayout.X_AXIS);
        setLayout(layout);
        removeBtn.setText("Remove");
        actionsField.setBackground(configUI.colorPanelBG);
        add(actionsField.labelPane);
        add(removeBtn);
        setAlignmentY(Component.TOP_ALIGNMENT);
        setAlignmentX(Component.LEFT_ALIGNMENT);
        setBackground(configUI.colorPanelBG);
        //setMinimumSize(new Dimension(400, 50));
    }

    public void addSlot(String name){
        TextFieldControl newField = new TextFieldControl(name, 100, 30);
        newField.labelPane.setBackground(configUI.colorPanelBG);
        add(newField.labelPane, getComponentCount()-2);
    }


}
