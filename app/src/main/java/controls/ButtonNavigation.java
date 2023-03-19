package controls;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.Position;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonNavigation extends JToggleButton {

    ImageIcon imgIcon = new ImageIcon(getClass().getResource("/imgs/chat_icon_2.png"));

    Color fgColor = new Color(255,255,255);
    Color bgColorOff = new Color(0,0,0, 0);
    Color bgColorOn = new Color(255,255,255, 10);

    public ButtonNavigation(){
        super();

        setBackground(bgColorOff);

        setIcon(imgIcon);
        setIconTextGap(2);
        setText("Chat");
        //setHorizontalTextPosition(JButton.CENTER);
        setHorizontalTextPosition(JButton.CENTER);
        setVerticalTextPosition(JButton.CENTER);

        setPreferredSize(new Dimension(40, 40));
        setMaximumSize(new Dimension(40, 40));
        setBorderPainted(false);
        setFocusPainted(false);

        setForeground(fgColor);
        setBackground(bgColorOff);
        setSelected(false);

        ////Add your own select color by setting background////
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(((JToggleButton) e.getSource()).isSelected()) {
                    setBackground(bgColorOn);
                } else {
                    setBackground(bgColorOff);
                }
            }
        });
    }

}
