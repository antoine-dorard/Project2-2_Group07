package panels;

import backend.CFG_InputProcessor;

import javax.swing.*;
import java.awt.*;
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
    JPanel chatContainer = new JPanel(){
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            // find where we should put the image to be in the center.
            int x = (this.getWidth() - background.getImage().getWidth(null)) / 2;
            int y = (this.getHeight() - background.getImage().getHeight(null)) / 2;
            // draw the background image in the center.
            g2d.drawImage(background.getImage(), x, y, null);
        }
    };
    JScrollPane scrollPane = new JScrollPane(chatContainer, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

    //ImageIcon botImageIcon = new ImageIcon("app/src/imgs/chatbot_app_icon_blue.png");
    ImageIcon botImageIcon = new ImageIcon(getClass().getResource("/imgs/chatbot_app_icon_blue.png"));
    JLabel botIcon = new JLabel(botImageIcon);
    ImageIcon userImageIcon = new ImageIcon(getClass().getResource("/imgs/user_icon.png"));
    JLabel userIcon = new JLabel(userImageIcon);
    ImageIcon background = new ImageIcon(getClass().getResource("/imgs/chatbot_icon_transp.png"));
    ImageIcon sendBtn = new ImageIcon(getClass().getResource("/imgs/send_icon.png"));

    GridBagConstraints c = new GridBagConstraints();
    Font textFont = new Font("Monospaced", Font.BOLD, 18);

    public ChatBotPanel(){
        super();
        this.setLayout(new GridBagLayout());

        // should be focusable to lose focus from other controls.
        this.setFocusable(true);

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
        button.setIcon(sendBtn);
        
        conversationLogSetup();

        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;   c.weighty = 1;
        c.gridx = 0;     c.gridy = 0;
        c.gridwidth = GridBagConstraints.REMAINDER;
        this.add(scrollPane, c);


        // user input field
        c.gridy = 1;
        c.weightx = 0.9; c.weighty = 0.1;
        c.gridwidth = GridBagConstraints.RELATIVE;
        textFieldSetUp(textField);

        this.add(textField, c);

        // send button
        c.gridx = 1;
        c.weightx = 0.1;
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
    public void textFieldSetUp(JTextField textField){
        textField.setBackground(new Color(64, 68, 75));
        textField.setFont(new Font("Monospaced", Font.BOLD, 14));
        textField.setForeground(new Color(255, 255, 255));

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
            chatLog = new JLabel(" Robot: ", botImageIcon, SwingConstants.LEFT);//botIcon.getIcon(), SwingConstants.LEFT);//minor changes by John in this line
            thinkingBot(chatLog, text);// added by John
        } else
            chatLog = new JLabel(" You: "+ text + "\n" , userIcon.getIcon(), SwingConstants.LEFT);

        chatLog.setOpaque(false);
        chatLog.setFont(textFont);
        chatLog.setBorder(createEmptyBorder(0,0,5,0));
        chatLog.setForeground(Color.WHITE);

        chatContainer.add(chatLog);
        chatContainer.updateUI();
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
