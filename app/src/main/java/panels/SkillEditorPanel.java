package panels;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.ArrayList;

import static javax.swing.BorderFactory.createEmptyBorder;
import static javax.swing.BorderFactory.createLineBorder;

public class SkillEditorPanel extends JPanel{

    // define the background image.
    private final ImageIcon background = new ImageIcon(getClass().getResource("/imgs/chatbot_icon_transp.png"));

    // define colors & fonts used in the JPanel.
    private final Font listFont = new Font("Monospaced", Font.BOLD, 18);
    private final Font txtFont = new Font("Monospaced", Font.BOLD, 14);

    // define colors used in the JList.
    private final Color listBGColor = new Color(47, 49, 52);
    private final Color listFGColor = new Color(210, 210, 210);
    private final Color listSelectBGColor = new Color(68, 70, 74);
    private final Color listSelectFGColor = new Color(255, 255, 255);
    public ArrayList<JTextField> txtFields = new ArrayList<JTextField>();


    public SkillEditorPanel(){
        super();
        FlowLayout layout = new FlowLayout();
        layout.setAlignment(FlowLayout.LEFT);
        this.setLayout(layout);
        this.setBackground(new Color(68, 68, 68));

        // define a boxLayout for all input JTextFields.
        JPanel boxPane = new JPanel();
        boxPane.setLayout(new BoxLayout(boxPane, BoxLayout.PAGE_AXIS));
        boxPane.setPreferredSize(new Dimension(400, 500));
        boxPane.setOpaque(false);

        // define a list model, which will work as skill selector.
        DefaultListModel<String> listModel = new DefaultListModel<>();
        listModel.addElement("Math");
        listModel.addElement("History");
        listModel.addElement("Calendar");

        // define the list control with the list model created earlier.
        JList<String> skillList = new JList<>(listModel);
        skillList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        skillList.setLayoutOrientation(JList.VERTICAL);
        skillList.setVisibleRowCount(-1);
        skillList.setBackground(listBGColor);
        skillList.setForeground(listFGColor);
        skillList.setFont(listFont);
        skillList.setSelectionBackground(listSelectBGColor);
        skillList.setSelectionForeground(listSelectFGColor);
        skillList.setPreferredSize(new Dimension(200, 550));

        // define the list scroller for the list control.
        JScrollPane listScroller = new JScrollPane(skillList);
        listScroller.setPreferredSize(new Dimension(250, 80));

        // create all JTextFields.
        JTextField skillName = new JTextField();
        JTextField skillRule = new JTextField();
        JTextField skillIdentifier = new JTextField();

        txtFields.add(skillName);
        txtFields.add(skillRule);
        txtFields.add(skillIdentifier);

        // create all JLabels.
        JLabel nameLabel = new JLabel();
        JLabel ruleLabel = new JLabel();
        JLabel identifierLabel = new JLabel();

        nameLabel.setText("Name");
        ruleLabel.setText("Rule(s)");
        identifierLabel.setText("Identifier(s)");

        nameLabel.setForeground(listFGColor);
        ruleLabel.setForeground(listFGColor);
        identifierLabel.setForeground(listFGColor);

        nameLabel.setVerticalAlignment(JLabel.BOTTOM);
        ruleLabel.setVerticalAlignment(JLabel.BOTTOM);
        identifierLabel.setVerticalAlignment(JLabel.BOTTOM);

        // define the dimensions of the JTextFields.
        skillName.setPreferredSize(new Dimension(250, 20));
        skillRule.setPreferredSize(new Dimension(250, 80));
        skillIdentifier.setPreferredSize(new Dimension(250, 80));

        // define the dimensions of the JLabels.
        nameLabel.setPreferredSize(new Dimension(250, 40));
        ruleLabel.setPreferredSize(new Dimension(250, 80));
        identifierLabel.setPreferredSize(new Dimension(250, 80));

        // define the background of the JTextFields.
        skillName.setBackground(new Color(46,49,53));
        skillName.setFont(txtFont);
        skillName.setBorder(createLineBorder(new Color(80,80,80), 1));
        skillName.setForeground(Color.WHITE);
        skillName.updateUI();

        skillRule.setBackground(new Color(46,49,53));
        skillRule.setFont(txtFont);
        skillRule.setBorder(createLineBorder(new Color(80,80,80), 1));
        skillRule.setForeground(Color.WHITE);
        skillRule.updateUI();

        skillIdentifier.setBackground(new Color(46,49,53));
        skillIdentifier.setFont(txtFont);
        skillIdentifier.setBorder(createLineBorder(new Color(80,80,80), 1));
        skillIdentifier.setForeground(Color.WHITE);
        skillIdentifier.updateUI();

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

    protected void paintComponent(Graphics g)
    {
        g.setColor( getBackground() );
        g.fillRect(0, 0, getWidth(), getHeight());
        super.paintComponent(g);
    }

}
