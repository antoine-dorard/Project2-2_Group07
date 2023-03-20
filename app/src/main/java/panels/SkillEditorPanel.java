package panels;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import utils.ConfigUI;
import controls.TreeControl;
import controls.ListControl;
import controls.TextFieldControl;

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
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

import static java.lang.String.format;
import static javax.swing.BorderFactory.createEmptyBorder;
import static javax.swing.BorderFactory.createLineBorder;

public class SkillEditorPanel extends JPanel{

    // define the background image.
    private final ImageIcon background = new ImageIcon(getClass().getResource("/imgs/chatbot_icon_transp.png"));

    // colors & fonts are defined in the UIConfig class.
    private final ConfigUI configUI = new ConfigUI();

    // define the skills array list.
    ArrayList<String> skills = new ArrayList<String>();

    // define the file path, where we should look for the questions.json file.
    String questionsJSONFilePath = "./app/src/main/resources/skills/questions.json";
    // define the unformatted file path, where we should look for the actions.json files.
    String actionsJSONFilePath = "./app/src/main/resources/skills/%s/actions.json";


    public SkillEditorPanel(){
        super();
        // define the main layout of this panel.
        FlowLayout layout = new FlowLayout();
        layout.setAlignment(FlowLayout.LEFT);
        setLayout(layout);

        // define a new TreeControl
        TreeControl treeControl = new TreeControl();

        // define the skills array and populate it from the questions.json
        skills = getSkills(questionsJSONFilePath);

        // populate the skill List with the found skills in the questions.json
        ListControl skillList = new ListControl(skills.toArray(String[]::new));

        // define a new TextFieldControl, with a specific width & height.
        TextFieldControl treeTextField = new TextFieldControl(400, 40);
        // immediately set disabled, because we haven't selected anything yet.
        treeTextField.setEnabled(false);

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
                // change the population of the tree, if something else was selected in the skill list.
                treeControl.parseJSONtoTree(format(actionsJSONFilePath, skills.get(skillList.getSelectedIndex())));
            }
        });

        // for startup, select the first skill automatically (do this after the listener is initialized).
        skillList.setSelectedIndex(0);

        // add Action Listener for the JTree "treeControl".
        treeControl.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent treeSelectionEvent) {

                // get the node of the last selected element in the tree.
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) treeControl.getLastSelectedPathComponent();
                // only handle the valueChange when the new selected path component is not 'null'.
                if (node != null) {
                    Object userObject = node.getUserObject();
                    // get the text from that node and set it to the treeTextField.
                    treeTextField.setText(userObject.toString());
                    // set it enabled (if it was disabled last valueChange).
                    treeTextField.setEnabled(true);
                }
                else {
                    // new selected path component is 'null'.
                    //treeTextField.setText("nothing selected");
                    // set disabled.
                    treeTextField.setEnabled(false);
                }
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
        // define the node that we want to change.
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        // only handle this, if the new defined node is not 'null'.
        if (node != null) {
            // set the new text as userObject to that node.
            node.setUserObject(textField.getText());
            // get the model and call nodeChanged on the model (to update).
            ((DefaultTreeModel) tree.getModel()).nodeChanged(node);
        }
    }

    private ArrayList<String> getSkills (String questionsFilePath){
        // define arrayList of strings to output as skills.
        ArrayList<String> outputArray = new ArrayList<String>();
        try {
            JSONParser parser = new JSONParser(); // create a json parser
            // read the file and parse the data
            Object obj = parser.parse(new FileReader(questionsFilePath));
            JSONObject jsonObject = (JSONObject) obj;
            for (Object keyStr : jsonObject.keySet()) {
                // for every found key in the question.json, add it to the arrayList.
                outputArray.add((String)keyStr);
            }
        }
        catch (ParseException | IOException e) {
            throw new RuntimeException(e);
        }
        return outputArray;
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
