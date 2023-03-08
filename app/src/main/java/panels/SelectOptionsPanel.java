package panels;

import java.util.ArrayList;

import javax.swing.*;
import java.awt.*;

public class  SelectOptionsPanel extends JPanel {

    public JToggleButton chatBtn, skillsBtn;
    private JLabel label;
    private Font btnFont = new Font("Monospaced", Font.BOLD, 14);
    public ArrayList<JToggleButton> naviButtons = new ArrayList<JToggleButton>();

    public SelectOptionsPanel(){
        super();
        GridLayout layout = new GridLayout(3,1);
        layout.setVgap(50);
        this.setLayout(layout);

        // define the navigation buttons, which select functionality, at left side of the screen
        chatBtn = new JToggleButton("Chat", new ImageIcon(getClass().getResource("/imgs/chat_icon.png")));
        buttonSetup(chatBtn);
        skillsBtn = new JToggleButton("Skills", new ImageIcon(getClass().getResource("/imgs/skills_icon.png")));
        buttonSetup(skillsBtn);

        // add a label as a placeholder below the navigation buttons.
        label = new JLabel(" ...More space...");
        label.setForeground(Color.white);
        this.add(label);

        // set the background of this JPanel.
        this.setBackground(new Color(59, 59, 59));
    }

    public void buttonSetup(JToggleButton btn){
        // set the button settings & layout variables.
        btn.setContentAreaFilled(false);
        btn.setHorizontalTextPosition(JButton.CENTER);
        btn.setVerticalTextPosition(JToggleButton.BOTTOM);
        btn.setFont(btnFont);
        btn.setForeground(Color.white);

        // add the button to "naviButtons", which is an ArrayList of JToggleButtons.
        naviButtons.add(btn);

        // also add it to the current JPanel.
        this.add(btn);
    }

}