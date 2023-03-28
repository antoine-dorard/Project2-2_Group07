package main;

import panels.ChatBotPanel;
import panels.SelectOptionsPanel;
import panels.SkillEditorPanel;

import javax.swing.*;
import java.awt.*;

public class Frame extends JFrame {

    final int winWidth = 1200;//850
    final int winHeight = 730;//600
    JPanel mainPanel;
    SelectOptionsPanel selectOptionsPanel;
    ChatBotPanel chatBotPanel;
    SkillEditorPanel skillEditorPanel;
    CardLayout cL;
    JPanel cards;

    public Frame(App app){
        super("UM DACS Project 2-2 - Group 07 Chatbot");

        this.setLayout(new BorderLayout(0,0));


        // we should be able to focus on the frame itself.
        this.setFocusable(true);

        // create the panel that contains the navigation buttons.
        selectOptionsPanel = new SelectOptionsPanel();
        selectOptionsPanel.setPreferredSize(new Dimension(100, 200));//(100, 480));
        selectOptionsPanel.setMaximumSize(new Dimension(40, 100));
        this.add(selectOptionsPanel, BorderLayout.LINE_START);

        // create your sub-panels.
        chatBotPanel = new ChatBotPanel(app);
        skillEditorPanel = new SkillEditorPanel();

        // create the panel that contains the "cards".
        cards = new JPanel(new CardLayout());
        cards.add(chatBotPanel, "ChatBotPanel");
        cards.add(skillEditorPanel, "SkillsEditorPanel");

        // get the card-layout, which was created as a layout around the "cards".
        cL = (CardLayout)(cards.getLayout());

        // default show the first panel.
        cL.show(cards, "ChatBotPanel");

        // add the cards to this Frame.
        this.add(cards);

        // set some Frame variables.
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setSize(winWidth, winHeight);
        this.setLocationRelativeTo(null);
        this.setIconImage(new ImageIcon(getClass().getResource("/imgs/UMlogo.jpg")).getImage());
        this.setVisible(true);

        // define the navigation button Listeners.
        for (JToggleButton btn :  selectOptionsPanel.naviButtons) {
            btn.addActionListener(e -> {
                actionPerformed(e.getActionCommand());
            });
        }
    }

    public void actionPerformed(String buttonName) // define your Listener action.
    {
        if (buttonName.equals("Chat")){
            cL.show(cards, "ChatBotPanel");
        }
        else if (buttonName.equals("Skills")){
            cL.show(cards, "SkillsEditorPanel");
        }
    }
}
