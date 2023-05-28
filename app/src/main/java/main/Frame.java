package main;

import controls.MyIconButton;
import panels.ChatBotPanel;
import panels.SelectOptionsPanel;
import panels.SkillEditorPanel;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class Frame extends JFrame {

    final int winWidth = 1200;
    final int winHeight = 730;
    JPanel mainPanel;
    SelectOptionsPanel selectOptionsPanel;
    ChatBotPanel chatBotPanel;
    SkillEditorPanel skillEditorPanel;
    CardLayout cL;
    JPanel cards;
    App app;

    public Frame(App app){
        super("UM DACS Project 2-2 - Group 07 Chatbot");
        this.app = app;

        this.setLayout(new BorderLayout(0,0));

        // create the panel that contains the navigation buttons.
        selectOptionsPanel = new SelectOptionsPanel();
        selectOptionsPanel.setPreferredSize(new Dimension(86, 480));
        this.add(selectOptionsPanel, BorderLayout.LINE_START);

        // create your sub-panels.
        chatBotPanel = new ChatBotPanel(app);
        skillEditorPanel = new SkillEditorPanel(this);

        // create the panel that contains the "cards".
        cards = new JPanel(new CardLayout());
        cards.add(chatBotPanel, "ChatBotPanel");
        cards.add(skillEditorPanel, "SkillsEditorPanel");

        // get the card-layout, which was created as a layout around the "cards".
        cL = (CardLayout)(cards.getLayout());

        // default show the first panel.
        cL.show(cards, "ChatBotPanel");
        // and set the button to selected.
        selectOptionsPanel.naviButtons.get(0).setSelected(true);

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
        for (MyIconButton btn :  selectOptionsPanel.naviButtons) {
            btn.addActionListener(e -> {
                actionPerformed(btn.getName());
                for (MyIconButton iBtn : selectOptionsPanel.naviButtons) {
                    // set everything unselected, except the currently selected button.
                    iBtn.setSelected(iBtn.getName().equals(btn.getName()));
                }
            });
        }
    }

    public void actionPerformed(String buttonName) // define your Listener action.
    {
        System.out.println("clicked on: "+buttonName);
        if (buttonName.equals("Chat")){
            app.skillLoader.loadCFGandCNF(); // update the skills after changes made in the skill editor.
            cL.show(cards, "ChatBotPanel");
        }
        else if (buttonName.equals("Skills")){
            cL.show(cards, "SkillsEditorPanel");
        }
    }
}
