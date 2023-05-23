package panels;

import controls.MyIconButton;
import utils.*;
import utils.UIFonts.*;
import utils.UIColors.*;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;

public class  SelectOptionsPanel extends JPanel {

    private final UIFonts fonts = new UIFonts();
    private final UIColors colors = new UIColors();
    public ArrayList<MyIconButton> naviButtons = new ArrayList<>();
    private String imgFolder = "app\\src\\main\\resources\\imgs\\";


    public SelectOptionsPanel(){
        super();
        BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
        this.setLayout(layout);

        // initialize the navigation buttons
        buttonSetup("Chat", "Chat");
        buttonSetup("Skills", "Skill Editor");

        // set the background of this JPanel.
        this.setBackground(colors.getColor(UIColor.BG_NAVIGATION));
    }

    public void buttonSetup(String name, String toolTipText){

        MyIconButton button = new MyIconButton(name, toolTipText);

        // add the button to "naviButtons", which is an ArrayList of JToggleButtons.
        naviButtons.add(button);

        // add button to the JPanel with a space of 15px between each button.
        Component spacer = Box.createVerticalStrut(15);
        this.add(spacer);
        this.add(button);
    }

}