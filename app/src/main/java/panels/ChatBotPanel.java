package panels;


import backend.FullTextIP;
import main.App;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import static javax.swing.BorderFactory.createEmptyBorder;

public class ChatBotPanel extends JPanel implements Runnable {
    App app;
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
    ImageIcon sendImageIcon = new ImageIcon(getClass().getResource("/imgs/send_icon.png"));

    GridBagConstraints c = new GridBagConstraints();
    Font textFont = new Font("Monospaced", Font.BOLD, 18);

    public ChatBotPanel(App app){
        super();
        this.app = app;
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
    public void textFieldSetUp(JTextField textField) {
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

        if (chatLog.getPreferredSize().width > 440) //if text is wider than panel, need to wrap text
            chatLog.setText(wrapText(chatLog.getText()));

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

    /**
     * Method that wraps given text, making use of HTML format
     * @param text text to be wrapped
     * @return wrapped text in html
     */
    private String wrapText(String text){
        int size = 60; //number of characters that can be displayed in 1 line
        ArrayList<String> list = new ArrayList<>(); //list of all separated lines
        for (int i = 0; i < text.length(); i += size)
            list.add(text.substring(i, Math.min(text.length(), i + size))); //take substring till either the end of the text or end of each jump
        String newText = "<html>";
        for (int i = 0; i < list.size()-1; i++)
            newText += list.get(i)+"<br>"; //break line
        newText+=list.get(list.size()-1)+"</html>";
        return newText;
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
        //chatLog.setText(textField.getText());
        setChatText(textField.getText() + "\n", false);

        //chatLog.append("me: " + textField.getText() + "\n");

        String input = textField.getText();
        String output = new FullTextIP(app.getSkillLoader()).processInput(input);
        setChatText(output, true);

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
