package main;

import panels.ChatBotPanel;
import panels.SelectOptionsPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Frame extends JFrame {

    final int winWidth = 700;
    final int winHeight = 450;
    JPanel chatBotPanel, selectOptionsPanel;

    public Frame(){
        super("DACS Project 2-2, Group 07, UM Chatbot");

        this.setLayout(new BorderLayout(50,30));
        selectOptionsPanel = new SelectOptionsPanel();
        this.add(selectOptionsPanel, BorderLayout.LINE_START);
        chatBotPanel = new ChatBotPanel();
        this.add(chatBotPanel, BorderLayout.CENTER);


        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setSize(winWidth, winHeight);
        this.setLocation((int) screenSize.getWidth()/2 - winWidth/2, (int) screenSize.getHeight()/2 - winHeight/2);
        this.setIconImage(new ImageIcon("app/src/imgs/UMlogo.jpg").getImage());

        this.setVisible(true);
    }
}
