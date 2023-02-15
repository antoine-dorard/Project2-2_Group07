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
    static ButtonGroup group = new ButtonGroup();

    public SelectOptionsPanel(){
        super();
        this.setLayout(new GridLayout(3,1));

        // buttons to select functionality at left side of the screen

        chatBtn = new JToggleButton("Chat", new ImageIcon("app/src/imgs/chat_icon.png"));
        buttonSetup(chatBtn);
        skillsBtn = new JToggleButton("Skills", new ImageIcon("app/src/imgs/skills_icon.png"));
        buttonSetup(skillsBtn);

        label = new JLabel("More space");
        this.add(label);

        this.setBackground(new Color(148, 148, 148));
    }
    public void buttonSetup(JToggleButton btn){
        btn.setHorizontalTextPosition(JButton.CENTER);
        btn.setVerticalTextPosition(JToggleButton.BOTTOM);
        group.add(btn);
        this.add(btn);
    }
}