package panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileReader;

public class  ChatBotPanel extends JPanel {

    static JTextField textField;
    static JButton button;
    static JLabel label;

    static JTextArea jt;

    public ChatBotPanel(){
        super();
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        textField = new JTextField(16);
        button = new JButton("send");
        label = new JLabel("nothing entered");

        textField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == 10){
                    actionPerformed("send");
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        button.addActionListener(e -> {
            actionPerformed(e.getActionCommand());
        });

        // create a text area, specifying the rows and columns
        jt = new JTextArea(10, 10);
        jt.setEditable(false);
        jt.setOpaque(false);

        jt.append("Robot: Hello I'm your chatBot, with what can I help you?"+"\n");
        jt.append("Robot: What is your name?"+"\n");


        c.gridwidth = GridBagConstraints.REMAINDER;
        c.weighty = 10;
        this.add(jt, c);
        c.gridwidth = GridBagConstraints.RELATIVE;
        c.weighty = 1;
        this.add(textField, c);
        c.gridwidth = GridBagConstraints.REMAINDER;
        this.add(button, c);

        this.setBackground(new Color(217, 217, 217));
    }


    public void actionPerformed(String buttonName)
    {
        if (buttonName.equals("send")) {
            // set the text of the label to the text of the field
            //jt.setText(textField.getText());
            jt.append("me: " + textField.getText() + "\n");
            if(textField.getText().contains("Laurent")){
                jt.append("Robot: That's a cool name"+"\n");
            }
            else if(textField.getText().contains("Antoine")){
                jt.append("Robot: beurk what a name");
            }

            // set the text of field to blank
            textField.setText("  ");
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(new ImageIcon("app/src/imgs/chatbot_icon_transp.png").getImage(), 0, 0, null);
    }
}
