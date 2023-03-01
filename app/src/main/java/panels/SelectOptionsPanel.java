package panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileReader;

public class  SelectOptionsPanel extends JPanel {

    static JToggleButton chatBtn, skillsBtn;
    static JLabel label;
    static ButtonGroup group = new ButtonGroup();

    static Font btnFont = new Font("Monospaced", Font.BOLD, 14);

    public SelectOptionsPanel(){
        super();
        GridLayout layout = new GridLayout(3,1);
        layout.setVgap(50);
        this.setLayout(layout);

        // buttons to select functionality at left side of the screen

        chatBtn = new JToggleButton("Chat", new ImageIcon("app/src/imgs/chat_icon.png"));
        buttonSetup(chatBtn);
        skillsBtn = new JToggleButton("Skills", new ImageIcon("app/src/imgs/skills_icon.png"));
        buttonSetup(skillsBtn);

        label = new JLabel(" ...More space...");
        label.setForeground(Color.white);
        this.add(label);

        this.setBackground(new Color(59, 59, 59));
    }
    public void buttonSetup(JToggleButton btn){
        btn.setContentAreaFilled(false);
        btn.setHorizontalTextPosition(JButton.CENTER);
        btn.setVerticalTextPosition(JToggleButton.BOTTOM);
        btn.setFont(btnFont);
        btn.setForeground(Color.white);
        group.add(btn);
        this.add(btn);
    }
}