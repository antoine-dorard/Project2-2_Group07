package panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ChatBotPanel extends JPanel implements Runnable{

    JTextField textField;
    JButton button;
    JLabel label;

    JTextArea jt;

    boolean isThreadOver = true;


    public ChatBotPanel(){
        super();
        this.setLayout(new BorderLayout(50,30));


        textField = new JTextField(16);
        button = new JButton("send");
        label = new JLabel("nothing entered");

        textField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == 10){
                    actionPerformed("send");
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        button.addActionListener(e -> {
            if(isThreadOver){
                isThreadOver = false;
                button.setEnabled(false);

                actionPerformed(e.getActionCommand());
                Thread t = new Thread(this);
                t.start();
            }
        });

        // create a text area, specifying the rows and columns
        jt = new JTextArea(10, 10);
        jt.append("Robot: Hello I'm your chatBot with what can I help you?"+"\n");
        jt.append("Robot: What is your name?"+"\n");

        this.add(jt,BorderLayout.NORTH);
        this.add(textField,BorderLayout.CENTER);
        this.add(button,BorderLayout.EAST);
    }


    public void actionPerformed(String buttonName)
    {
        if (buttonName.equals("send")) {
            // set the text of the label to the text of the field
            //jt.setText(textField.getText());
            jt.append("me: " + textField.getText() + "\n");
            if(textField.getText().contains("Laurent")){
                jt.append("Robot: That's a cool name"+"\n");
            }
            else if(textField.getText().contains("Antoine")){
                jt.append("Robot: beurk what a name");
            }

            // set the text of field to blank
            textField.setText("  ");
        }
    }

    @Override
    public void run() {
        for (int i = 0; i <10; i++) {
            System.out.println("Thread running: "+i);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        button.setEnabled(true);
        isThreadOver = true;
    }
}
