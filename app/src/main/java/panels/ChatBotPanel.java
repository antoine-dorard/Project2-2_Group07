package panels;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static javax.swing.BorderFactory.createEmptyBorder;

public class ChatBotPanel extends JPanel implements Runnable{

    JTextField textField;
    JButton button;
    JLabel label;

    JTextArea jt;

    boolean isThreadOver = true;

    // the actual text
    JLabel chatLog = new JLabel("");

    // the text + icon
    JPanel chatContainer = new JPanel();
    JScrollPane scrollPane = new JScrollPane(chatContainer, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

    ImageIcon botImageIcon = new ImageIcon("app/src/imgs/chatbot_app_icon_blue.png");
    JLabel botIcon = new JLabel(botImageIcon);
    ImageIcon userImageIcon = new ImageIcon("app/src/imgs/user_icon.png");
    JLabel userIcon = new JLabel(userImageIcon);
    ImageIcon background = new ImageIcon("app/src/imgs/chatbot_icon_transp.png");

    GridBagConstraints c = new GridBagConstraints();
    Font textFont = new Font("Monospaced", Font.BOLD, 18);

    public ChatBotPanel(){
        super();
        this.setLayout(new GridBagLayout());

        textField = new JTextField();
        textField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    actionPerformed("send");
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        button = new JButton("send");

        button.addActionListener(e -> {
            actionPerformed(e.getActionCommand());
        });
        
        conversationLogSetup();

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
        if(buttonName.equals("send")) {
            if(isThreadOver){
                isThreadOver = false;
                button.setEnabled(false);

                Thread sendingThread = new Thread(this);
                sendingThread.start();
            }
        }
    }
    
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


    @Override
    public void run() {
        // Main Execution

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



        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        button.setEnabled(true);
        isThreadOver = true;
    }

}
