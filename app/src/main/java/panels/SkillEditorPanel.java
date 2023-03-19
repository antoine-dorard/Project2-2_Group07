package panels;

import utils.ConfigUI;
import controls.TreeControl;

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
        FlowLayout layout = new FlowLayout();
        layout.setAlignment(FlowLayout.LEFT);
        setLayout(layout);
        //JPanel listPane = new JPanel();
        //JPanel treePane = new JPanel();
        //add(listPane);
        //add(treePane);

        //this.setLayout(layout);
        //this.setBackground(configUI.colorPanelBG);


        // define a boxLayout for all input JTextFields.
        JPanel boxPane = new JPanel();
        boxPane.setLayout(new BoxLayout(boxPane, BoxLayout.PAGE_AXIS));
        boxPane.setPreferredSize(new Dimension(400, 500));
        boxPane.setOpaque(false);

        // define a list model, which will work as skill selector.
        DefaultListModel<String> listModel = new DefaultListModel<>();

        // define the list control with the list model created earlier.
        JList<String> skillList = new JList<>(listModel);

        // define the list items and call the setup method for the JList.
        String[] listItems = {"Math", "History", "Calendar", "etc"};
        setupJList(skillList,listItems);

        TreeControl treeControl = new TreeControl();

        JTextField treeTextField = new JTextField();
        setupJTextField(treeTextField, 400, 40);

        // create all JTextFields.
        JTextField skillName = new JTextField();
        JTextField skillRule = new JTextField();
        JTextField skillIdentifier = new JTextField();

        // create all JLabels.
        JLabel nameLabel = new JLabel();
        JLabel ruleLabel = new JLabel();
        JLabel identifierLabel = new JLabel();

        setupJLabel(nameLabel, "Name", 250, 40);
        setupJLabel(ruleLabel, "Rule(s)", 250, 80);
        setupJLabel(identifierLabel, "Identifier(s)", 250, 80);

        // setup all JTextFields.
        setupJTextField(skillName, 250, 20);
        setupJTextField(skillRule, 250, 80);
        setupJTextField(skillIdentifier, 250, 80);

        // add all the JTextFields to the JPanel.
        boxPane.add(nameLabel);
        boxPane.add(treeControl);
        //boxPane.add(Box.createRigidArea(new Dimension(0, 10)));
        //boxPane.add(skillName);
        //boxPane.add(ruleLabel);
        //boxPane.add(Box.createRigidArea(new Dimension(0, 10)));
        //boxPane.add(skillRule);
        //boxPane.add(identifierLabel);
        //boxPane.add(Box.createRigidArea(new Dimension(0, 10)));
        //boxPane.add(skillIdentifier);

        // add the skill selector to this JPanel. (do this first)
        //add(skillList);
        // add the boxPane, which contains all JTextFields.
        //this.add(boxPane);
        //treeControl.setAlignmentY(JTree.TOP_ALIGNMENT);
        //add(treeControl);
        //setAlignmentY(JPanel.TOP_ALIGNMENT);
        //add(skillList, BorderLayout.NORTH);
        //skillList.setPreferredSize(new Dimension(100, 200));
        //add(treeControl, BorderLayout.NORTH);
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
                skillListValueChanged(evt);
            }
            private void skillListValueChanged(ListSelectionEvent evt) {
                if (!skillList.getValueIsAdjusting()) {
                    skillName.setText((String) skillList.getSelectedValue());
                }
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

    private void setupJList (JList<String> list, String[] listItems){

        // retrieve listModel and set all items from the input array.
        DefaultListModel<String> model = (DefaultListModel<String>) list.getModel();

        // add all items, provided in the input, to the current JList.
        for (String item :  listItems) {model.addElement(item);}

        // define the list appearance and functionality.
        list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        list.setLayoutOrientation(JList.VERTICAL);
        list.setVisibleRowCount(-1);
        list.setBackground(configUI.colorListBG);
        list.setForeground(configUI.colorListFG);
        list.setFont(configUI.fontList);
        list.setSelectionBackground(configUI.colorListSelectionBG);
        list.setSelectionForeground(configUI.colorListSelectionFG);
        list.setPreferredSize(new Dimension(200, 550));

        // define the list scroller for the list control.
        JScrollPane listScroller = new JScrollPane(list);
        listScroller.setPreferredSize(new Dimension(250, 80));
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
