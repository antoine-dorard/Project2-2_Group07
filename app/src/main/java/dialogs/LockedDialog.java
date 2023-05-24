package dialogs;

import controls.MyDialogButtonPane;
import controls.MyHeaderPane;
import controls.MyTextField;
import controls.MyUppercaseDocumentFilter;
import utils.UIColors;
import utils.UIColors.UIColor;
import utils.UIFonts;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class LockedDialog extends JDialog {

    final int winWidth = 350;
    final int winHeight = 200;
    private final UIFonts fonts = new UIFonts();
    private final UIColors colors = new UIColors();

    private String imgFolder = "app\\src\\main\\resources\\imgs\\";
    ImageIcon icon = new ImageIcon(imgFolder + "lock.png");


    public LockedDialog(JFrame owner, Boolean withCloseButton){
        super(owner, "LOCK", true);

        setSize(winWidth, winHeight);

        setResizable(false);

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        BorderLayout layout = new BorderLayout();
        setLayout(layout);

        setBackground(colors.getColor(UIColor.BG_PRIMARY));

        MyHeaderPane headerPane = new MyHeaderPane(
                "Application LOCKED",
                "No face detected...",
                true);



        JPanel labelPane = new JPanel(new BorderLayout());
        JLabel labelIcon = new JLabel(icon);
        labelIcon.setBackground(colors.getColor(UIColor.BG_PRIMARY));
        labelPane.setBackground(colors.getColor(UIColor.BG_PRIMARY));
        labelPane.add(labelIcon, BorderLayout.CENTER);

        JButton button = new JButton("CLOSE");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                closeDialog();
            }
        });


        add(headerPane, BorderLayout.NORTH);
        add(labelPane, BorderLayout.CENTER);
        add(button, BorderLayout.SOUTH);

        // set position to center.
        setLocationRelativeTo(null);
        // show dialog.
        setVisible(true);
    }


    public void closeDialog() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        dispose();
    }

}
