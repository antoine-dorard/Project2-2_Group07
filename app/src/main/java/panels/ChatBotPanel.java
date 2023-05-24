package panels;


import backend.FullTextIP;
import controls.MyHeaderPane;
import controls.MyIconButton;
import controls.MyTextField;
import main.App;
import utils.*;
import utils.UIColors.*;
import utils.UIFonts.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import static javax.swing.BorderFactory.createEmptyBorder;

public class ChatBotPanel extends JPanel implements Runnable {

    private final UIFonts fonts = new UIFonts();
    private final UIColors colors = new UIColors();

    App app;
    MyTextField textField;
    MyIconButton button;
    JLabel label;

    JTextArea jt;

    boolean isThreadOver = true;

    // the actual text
    JLabel chatLog = new JLabel("");

    Boolean wasBot = false;

    // the text + icon
    JPanel chatContainer = new JPanel();
    JScrollPane scrollPane = new JScrollPane(chatContainer);//, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
            //JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

    //ImageIcon botImageIcon = new ImageIcon("app/src/imgs/chatbot_app_icon_blue.png");
    ImageIcon botImageIcon = new ImageIcon(getClass().getResource("/imgs/chatbot_app_icon.png"));
    JLabel botIcon = new JLabel(botImageIcon);
    ImageIcon userImageIcon = new ImageIcon(getClass().getResource("/imgs/user_icon.png"));
    JLabel userIcon = new JLabel(userImageIcon);
    ImageIcon emptyImageIcon = new ImageIcon(getClass().getResource("/imgs/empty_icon.png"));
    JLabel emptyIcon = new JLabel(emptyImageIcon);
    ImageIcon background = new ImageIcon(getClass().getResource("/imgs/chatbot_icon.png"));

    //ImageIcon sendImageIcon = new ImageIcon(getClass().getResource("/imgs/send_icon.png"));
    //GridBagConstraints c = new GridBagConstraints();
    Font textFont = fonts.getFont(FontStyle.MEDIUM, 16);


    public ChatBotPanel(App app){
        super();
        this.app = app;
        //this.setLayout(new GridBagLayout());
        this.setLayout(new BorderLayout());

        textField = new MyTextField();
        button = new MyIconButton("send", "Send Message");
        // start disabled, because textField is empty.
        button.setEnabled(false);

        textField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                button.setEnabled(!textField.getText().equals(""));
            }
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    // check if textField isn't empty.
                    if(!textField.getText().equals("")){
                        actionPerformed("send");
                    }
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {
            }
        });


        button.addActionListener(e -> {
            actionPerformed(e.getActionCommand());
        });
        button.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                if (button.isSelected()) {
                    actionPerformed("send");
                }
            }
        });

        MyHeaderPane northPane = new MyHeaderPane(
                "Chat",
                "Here you can chat with our ChatBot.",
                true
        );

        BorderLayout southLayout = new BorderLayout();
        JPanel southPane = new JPanel(southLayout);

        southPane.add(textField, BorderLayout.CENTER);
        southPane.add(button, BorderLayout.EAST);

        southPane.setBackground(colors.getColor(UIColor.BG_CHAT_TEXT));
        southPane.setBorder(new EmptyBorder(20,20,20,20));
        southPane.setPreferredSize(new Dimension(2000, 80));

        conversationLogSetup();

        scrollPane.setBackground(colors.getColor(UIColor.BG_PRIMARY));
        scrollPane.setBorder(new EmptyBorder(20,20,20,20));

        add(northPane, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(southPane, BorderLayout.SOUTH);

        setBackground(colors.getColor(UIColor.BG_PRIMARY));
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
        g.drawImage(background.getImage(), 250, 100, null);
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

        String btnText;
        Icon btnIcon;
        Color btnFg;

        if(isBot) {
            btnText = "";
            // if previous message was also from bot, display empty icon.
            if(wasBot){ btnIcon = emptyIcon.getIcon(); }
            // otherwise display normal icon.
            else { btnIcon = botIcon.getIcon(); }
            btnFg = colors.getColor(UIColor.CHAT_FG_BOT);
            wasBot = true;
        }
        else {
            btnText = text + "\n";
            // if previous message was also from user, display empty icon.
            if(!wasBot){ btnIcon = emptyIcon.getIcon(); }
            // otherwise display normal icon.
            else { btnIcon = userIcon.getIcon(); }
            btnFg = colors.getColor(UIColor.CHAT_FG_USER);
            wasBot = false;
        }

        chatLog = new JLabel(btnText, btnIcon, SwingConstants.LEFT);
        chatLog.setForeground(btnFg);
        // imitate the bot 'thinking'.
        if(isBot){thinkingBot(chatLog, text);};

        //if text is wider than panel, need to wrap text
        if (chatLog.getPreferredSize().width > 800) //440)
            chatLog.setText(wrapText(chatLog.getText()));

        chatLog.setOpaque(false);
        chatLog.setFont(textFont);
        chatLog.setIconTextGap(10);
        chatLog.setBorder(createEmptyBorder(0,0,5,0));

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

        // first get the text
        String text = textField.getText();

        // so we can set it to blank immediately.
        textField.setText("");
        button.setEnabled(false);

        // set the text of the label to the text of the field
        setChatText(text + "\n", false);
        
        //chatLog.append("me: " + textField.getText() + "\n");

        if(text.contains("Laurent")){
            setChatText("That's a cool name"+"\n", true);
        }
        else if(text.contains("Antoine")){
            setChatText("beurk what a name", true);
        }
        else{
            String input = text;
            String output = new FullTextIP(app.getSkillLoader()).processInput(input);
            setChatText(output, true);
        }

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        button.setEnabled(true);
        isThreadOver = true;
    }

}
