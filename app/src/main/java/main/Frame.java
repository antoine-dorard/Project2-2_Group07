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
        super("UM DACS Project 2-2 - Group 07 Chatbot");

        this.setLayout(new BorderLayout(10,30));
        selectOptionsPanel = new SelectOptionsPanel();
        selectOptionsPanel.setPreferredSize(new Dimension(80, 480));
        this.add(selectOptionsPanel, BorderLayout.LINE_START);
        chatBotPanel = new ChatBotPanel();
        this.add(chatBotPanel, BorderLayout.CENTER);


        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setSize(winWidth, winHeight);
        this.setLocationRelativeTo(null);
        this.setIconImage(new ImageIcon("app/src/imgs/UMlogo.jpg").getImage());

        this.setVisible(true);
    }
}
