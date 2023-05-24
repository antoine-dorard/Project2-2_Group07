package dialogs;

import controls.MyDialogButtonPane;
import controls.MyHeaderPane;
import controls.MyTextField;
import utils.*;
import utils.UIColors.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public class TestDialog extends JFrame {

    final int winWidth = 550;
    final int winHeight = 420;
    private final UIFonts fonts = new UIFonts();
    private final UIColors colors = new UIColors();
    private JTable table;


    public TestDialog(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Custom Dialog Example");
        setSize(400, 300);
        setLayout(new FlowLayout());

        JButton showDialogButton = new JButton("Show Custom Dialog");
        showDialogButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showCustomDialog();
            }
        });
        add(showDialogButton);

        table = new JTable();
        add(new JScrollPane(table));

    }


    private void showCustomDialog() {
        // Create a custom dialog
        SkillEditDialog dialog = new SkillEditDialog(this, "LOCATION");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                TestDialog example = new TestDialog();
                example.setVisible(true);
            }
        });
    }
}