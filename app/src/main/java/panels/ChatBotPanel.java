package panels;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static javax.swing.BorderFactory.createEmptyBorder;

public class  ChatBotPanel extends JPanel {

    static JTextField textField;
    static JButton button;

    // the actual text
    static JLabel chatLog = new JLabel("");

    // the text + icon
    static JPanel chatContainer = new JPanel();
    static JScrollPane scrollPane = new JScrollPane(chatContainer, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

    static ImageIcon botImageIcon = new ImageIcon("app/src/imgs/chatbot_app_icon_blue.png");
    static JLabel botIcon = new JLabel(botImageIcon);
    static ImageIcon userImageIcon = new ImageIcon("app/src/imgs/user_icon.png");
    static JLabel userIcon = new JLabel(userImageIcon);
    static ImageIcon background = new ImageIcon("app/src/imgs/chatbot_icon_transp.png");

    static GridBagConstraints c = new GridBagConstraints();
    static Font textFont = new Font("Monospaced", Font.BOLD, 18);

    public ChatBotPanel(){
        super();
        this.setLayout(new GridBagLayout());

        textField = new JTextField();
        textField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if(e.getKeyCode() == 13){
                    actionPerformed("send");
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        conversationLogSetup();

        button = new JButton("send");
        button.addActionListener(e -> actionPerformed(e.getActionCommand()));

        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;   c.weighty = 1;
        c.gridx = 0;     c.gridy = 0;
        c.gridwidth = GridBagConstraints.REMAINDER;
        this.add(scrollPane, c);


        // user input field
        c.gridy = 1;
        c.weightx = 0.7;   c.weighty = 0.1;
        c.gridwidth = GridBagConstraints.RELATIVE;
        this.add(textField, c);

        // send button
        c.gridx = 1; c.gridy = 1;
        c.gridwidth = GridBagConstraints.REMAINDER;
        this.add(button, c);

        this.setBackground(new Color(68, 68, 68));
    }


    public void actionPerformed(String buttonName)
    {
        if (buttonName.equals("send")) {
            // set the text of the label to the text of the field
            //chatLog.setText(textField.getText());
            setChatText(textField.getText() + "\n", false);
            //chatLog.append("me: " + textField.getText() + "\n");
            if(textField.getText().contains("Laurent")){
                setChatText("That's a cool name"+"\n", true);
            }
            else if(textField.getText().contains("Antoine")){
                setChatText("beurk what a name", true);
            }

            // set the text of field to blank
            textField.setText("");
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background.getImage(), 170, 50, null);
    }

    public void conversationLogSetup(){
        chatContainer.setLayout(new BoxLayout(chatContainer, BoxLayout.Y_AXIS));
        setChatText("Hello I'm your chatBot, with what can I help you?", true);
        setChatText("What is your name?", true);

        chatContainer.setOpaque(false);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

    }
    public void setChatText(String text, boolean isBot){
        if(isBot)
            chatLog = new JLabel(" Robot: "+ text + "\n" , botIcon.getIcon(), SwingConstants.LEFT);
        else
            chatLog = new JLabel(" You: "+ text + "\n" , userIcon.getIcon(), SwingConstants.LEFT);

        chatLog.setOpaque(false);
        chatLog.setFont(textFont);
        chatLog.setBorder(createEmptyBorder(0,0,5,0));
        chatLog.setForeground(Color.WHITE);
        chatContainer.add(chatLog);
        chatContainer.updateUI();
    }

}
