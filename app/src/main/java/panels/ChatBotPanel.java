package panels;


import backend.CFG_InputProcessor;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static javax.swing.BorderFactory.createEmptyBorder;

public class ChatBotPanel extends JPanel implements Runnable {
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

    //ImageIcon botImageIcon = new ImageIcon("app/src/imgs/chatbot_app_icon_blue.png");
    ImageIcon botImageIcon = new ImageIcon(getClass().getResource("/imgs/chatbot_app_icon_blue.png"));
    JLabel botIcon = new JLabel(botImageIcon);
    ImageIcon userImageIcon = new ImageIcon(getClass().getResource("/imgs/user_icon.png"));
    JLabel userIcon = new JLabel(userImageIcon);
    ImageIcon background = new ImageIcon(getClass().getResource("/imgs/chatbot_icon_transp.png"));
    ImageIcon sendImageIcon = new ImageIcon(getClass().getResource("/imgs/send_icon.png"));

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

        button = new JButton("send", sendImageIcon);

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
        c.weightx = 1;   c.weighty = 0.1;
        c.gridwidth = GridBagConstraints.RELATIVE;
        this.add(textField, c);

        // send button
        c.gridx = 5; c.gridy = 1;
        c.weightx = 0;
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
        if(isBot) {
            chatLog = new JLabel(" Robot: ", botIcon.getIcon(), SwingConstants.LEFT); //minor changes by John in this line
            
            thinkingBot(chatLog, text);// added by John
        } else
            chatLog = new JLabel(" You: "+ text + "\n" , userIcon.getIcon(), SwingConstants.LEFT);

        chatLog.setOpaque(false);
        chatLog.setFont(textFont);
        chatLog.setBorder(createEmptyBorder(0,0,5,0));
        chatLog.setForeground(Color.WHITE);
        chatContainer.add(chatLog);
        chatContainer.revalidate();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        scrollToBottomAutomatically();
    }

    private void scrollToBottomAutomatically(){
        JScrollBar bar = scrollPane.getVerticalScrollBar();
        bar.setValue(bar.getMaximum());

    }

    //Thinking-Bot effect method
    public void thinkingBot(JLabel target, String str) {
        Thread runner = new Thread(() -> {
            String[] dots = {"#"," ","#"," "};
            String[] letters = str.split("");
            String initial = target.getText();// stores "Robot" word

            for (String dot : dots) {
                target.setText(initial + dot);
                try {
                    Thread.sleep(500);
                } catch(Exception e) {
                    //... oh shit
                }
            }

            target.setText(initial);
            for (String letter:letters) {
                String current = target.getText();
                target.setText(current + letter);
                try {
                    Thread.sleep(50);
                } catch (Exception e) {
                    //... oh dear
                }
            }
            String current = target.getText();
            target.setText(current + "\n");
        });
        runner.start();
    }


    @Override
    public void run() {
        // Main Execution

        // set the text of the label to the text of the field
        setChatText(textField.getText() + "\n", false);
        
        //chatLog.append("me: " + textField.getText() + "\n");

        if(textField.getText().contains("Laurent")){
            setChatText("That's a cool name"+"\n", true);
        }
        else if(textField.getText().contains("Antoine")){
            setChatText("beurk what a name", true);
        }
        else{
            String input = textField.getText();
            String output = new CFG_InputProcessor().processInput(input);
            setChatText(output, true);
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
