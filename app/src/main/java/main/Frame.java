package main;

import panels.ChatBotPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Frame extends JFrame {

    JPanel chatBotPanel;

    public Frame(){
        super("ChatBot");

        JPanel chatBotPanel = new ChatBotPanel();
        this.add(chatBotPanel);

        int winWidth = 450;
        int winHeight = 700;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(winWidth, winHeight);
        this.setLocation((int) screenSize.getWidth()/2 - winWidth/2, (int) screenSize.getHeight()/2 - winHeight/2);


        this.setVisible(true);
    }
}
