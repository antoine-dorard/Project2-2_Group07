package panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileReader;

public class  SelectOptionsPanel extends JPanel {

    static JToggleButton chatBtn, skillsBtn;
    static JLabel label;

    public SelectOptionsPanel(){
        super();
        this.setLayout(new GridLayout(3,1));

        // buttons to select functionality at left side of the screen

        ButtonGroup group = new ButtonGroup();
        chatBtn = new JToggleButton("chat", new ImageIcon("app/src/imgs/chat_icon.png"));
        group.add(chatBtn);
        this.add(chatBtn);

        skillsBtn = new JToggleButton("Skills", new ImageIcon("app/src/imgs/skills_icon.png"));
        group.add(skillsBtn);
        this.add(skillsBtn);

        label = new JLabel("More space for additional functionality");
        this.add(label);
    }
}