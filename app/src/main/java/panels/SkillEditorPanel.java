package panels;

import utils.ConfigUI;
import controls.TreeControl;
import controls.ListControl;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Array;
import java.util.ArrayList;

import static javax.swing.BorderFactory.createEmptyBorder;
import static javax.swing.BorderFactory.createLineBorder;

public class SkillEditorPanel extends JPanel{

    // define the background image.
    private final ImageIcon background = new ImageIcon(getClass().getResource("/imgs/chatbot_icon_transp.png"));

    // define colors & fonts used in the JPanel.

    // colors & fonts are defined in the UIConfig class.
    private final ConfigUI configUI = new ConfigUI();


    public SkillEditorPanel(){
        super();
        // define the main layout of this panel.
        FlowLayout layout = new FlowLayout();
        layout.setAlignment(FlowLayout.LEFT);
        setLayout(layout);

        ListControl skillList = new ListControl();
        TreeControl treeControl = new TreeControl();
        JTextField treeTextField = new JTextField();
        setupJTextField(treeTextField, 400, 40);

        // create all JLabels.
        //JLabel nameLabel = new JLabel();
        //setupJLabel(nameLabel, "Name", 250, 40);

        skillList.setPreferredSize(new Dimension(200, 600));
        treeControl.setPreferredSize(new Dimension(200, 600));
        add(skillList);
        add(treeControl);
        setBackground(new Color(68, 68, 68));
        skillList.setBackground(new Color(63,63,63));
        treeControl.setBackground(new Color(68, 68, 68));
        treeControl.setForeground(configUI.colorListFG);
        add(treeTextField);

        // add Action Listener for the JList "skillList".
        skillList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent evt) {
            }
        });

        // add Action Listener for the JTree "treeControl".
        treeControl.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent treeSelectionEvent) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) treeControl.getLastSelectedPathComponent();
                Object userObject = node.getUserObject();
                treeTextField.setText(userObject.toString());
            }
        });

        // add Action Listener for the JTextField "treeTextField".
        treeTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent documentEvent) {
                updateTreeNode(treeControl, treeTextField);
            }

            @Override
            public void removeUpdate(DocumentEvent documentEvent) {
                updateTreeNode(treeControl, treeTextField);
            }

            @Override
            public void changedUpdate(DocumentEvent documentEvent) {
                updateTreeNode(treeControl, treeTextField);
            }
        });
    }

    private void updateTreeNode (TreeControl tree, JTextField textField){
        DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        node.setUserObject(textField.getText());
        model.nodeChanged(node);
    }

    private void setupJTextField (JTextField textField, int width, int height) {
        // define the textField's appearance.
        textField.setBackground(new Color(46,49,53));
        textField.setFont(new Font("Monospaced", Font.BOLD, 16));//configUI.fontText);
        textField.setBorder(createLineBorder(new Color(80,80,80), 1));
        textField.setForeground(Color.WHITE);
        textField.updateUI();
        textField.setPreferredSize(new Dimension(width, height));
    }

    private void setupJLabel (JLabel label, String text, int width, int height) {
        // define the label's appearance.
        label.setText(text);
        label.setForeground(configUI.colorListFG);
        label.setVerticalAlignment(JLabel.BOTTOM);
        label.setPreferredSize(new Dimension(width, height));
    }

    protected void paintComponent(Graphics g)
    {
        g.setColor( getBackground() );
        g.fillRect(0, 0, getWidth(), getHeight());
        super.paintComponent(g);
    }

}
