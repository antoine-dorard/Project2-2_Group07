package panels;

import utils.ConfigUI;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;

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
        this.setLayout(layout);
        this.setBackground(configUI.colorPanelBG);

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
        boxPane.add(Box.createRigidArea(new Dimension(0, 10)));
        boxPane.add(skillName);
        boxPane.add(ruleLabel);
        boxPane.add(Box.createRigidArea(new Dimension(0, 10)));
        boxPane.add(skillRule);
        boxPane.add(identifierLabel);
        boxPane.add(Box.createRigidArea(new Dimension(0, 10)));
        boxPane.add(skillIdentifier);

        // add the skill selector to this JPanel. (do this first)
        this.add(skillList);
        // add the boxPane, which contains all JTextFields.
        this.add(boxPane);

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
        textField.setFont(configUI.fontText);
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
