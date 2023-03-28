package controls;

import utils.*;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SlotListControl extends JPanel {

    ConfigUI configUI = new ConfigUI();
    public JButton removeBtn = new JButton();

    public ArrayList<String> names = new ArrayList<>();
    public ArrayList<String> values = new ArrayList<>();
    public String action = "";

    // Storing the text fields allows to get at anytime what is the value of the text field to save it to file.
    public ArrayList<TextFieldControl> slotValuesTextFields = new ArrayList<>();
    public TextFieldControl actionTextField = null;

    public SlotListControl(){
        super();

        BoxLayout layout = new BoxLayout(this, BoxLayout.X_AXIS);
        setLayout(layout);
        removeBtn.setText("Remove");

        add(removeBtn);
        setAlignmentY(Component.TOP_ALIGNMENT);
        setAlignmentX(Component.LEFT_ALIGNMENT);
        setBackground(configUI.colorPanelBG);
    }

    public void addSlot(String name, String value){
        TextFieldControl newField = new TextFieldControl(name, 100, 35);
        newField.labelPane.setBackground(configUI.colorPanelBG);
        newField.setText(value);
        add(newField.labelPane, getComponentCount()-1);
        if (!name.equals("Action")){
            // don't add the action ctrl.
            names.add(name);
            values.add(value);

            slotValuesTextFields.add(newField);
        }
        else{
            action = value;
            actionTextField = newField;
        }
    }


}
