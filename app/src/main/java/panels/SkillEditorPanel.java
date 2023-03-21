package panels;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import utils.ConfigUI;
import controls.TreeControl;
import controls.ListControl;
import controls.TextFieldControl;

import javax.print.Doc;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
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
import java.util.concurrent.Flow;

import static java.lang.String.format;
import static javax.swing.BorderFactory.createEmptyBorder;
import static javax.swing.BorderFactory.createLineBorder;

public class SkillEditorPanel extends JPanel{


    // define the background image.
    private final ImageIcon background = new ImageIcon(getClass().getResource("/imgs/chatbot_icon_transp.png"));

    // colors & fonts are defined in the UIConfig class.
    private final ConfigUI configUI = new ConfigUI();

    // define the skills array list.
    ArrayList<String> skills;

    // define the file path, where we should look for the questions.json file.
    String questionsJSONFilePath = "./app/src/main/resources/skills/questions.json";
    // define the unformatted file path, where we should look for the actions.json files.
    String actionsJSONFilePath = "./app/src/main/resources/skills/%s/actions.json";


    public SkillEditorPanel(){
        super();
        setAlignmentX(JPanel.LEFT_ALIGNMENT);
        // define the main layout of this panel.
        FlowLayout layout = new FlowLayout();
        layout.setAlignment(FlowLayout.LEFT);
        // set gaps to zero, it should stick to the left.
        layout.setVgap(0);
        layout.setHgap(0);
        // set the layout
        setLayout(layout);

        // define a new TreeControl
        TreeControl treeControl = new TreeControl(200, 600);

        // define the skills array and populate it from the questions.json
        skills = getSkills(questionsJSONFilePath);

        // populate the skill List with the found skills in the questions.json
        ListControl skillList = new ListControl("SKILLS",skills.toArray(String[]::new), 200, 650);

        // define a layout with key, identifier & value JTextFields(TextFieldControls).
        JPanel textFieldPane = new JPanel();
        BoxLayout textFieldLayout = new BoxLayout(textFieldPane, BoxLayout.Y_AXIS);
        textFieldPane.setLayout(textFieldLayout);

        // define a new TextFieldControl, with a specific width & height.
        TextFieldControl keyTextField = new TextFieldControl("Key",400, 40);
        TextFieldControl idTextField = new TextFieldControl("Identifier",400, 40);
        TextFieldControl valueTextField = new TextFieldControl("Value",400, 40);
        // set up the controls in the right panel.
        setupTextFieldControl(keyTextField, textFieldPane);
        setupTextFieldControl(idTextField, textFieldPane);
        setupTextFieldControl(valueTextField, textFieldPane);

        JButton addToJsonBtn = new JButton("Save");
        addToJsonBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                treeControl.writeJsonToFile(treeControl.convertTreeToJson((DefaultMutableTreeNode) treeControl.getModel().getRoot()),format(actionsJSONFilePath, skills.get(skillList.getSelectedIndex())));
            }
        });


        // add the controls to the current layout.
        //add(skillList);
        add(skillList.getListPane());
        add(Box.createHorizontalStrut(30));
        add(treeControl);
        add(Box.createHorizontalStrut(30));
        add(textFieldPane);
        add(addToJsonBtn);

        // set some UI colors.
        setBackground(new Color(68, 68, 68));
        skillList.setBackground(new Color(63,63,63));
        treeControl.setBackground(new Color(68, 68, 68));
        treeControl.setForeground(configUI.colorListFG);

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
                    String nodeText = userObject.toString();
                    String[] split = nodeText.split(" ");
                    if (split.length == 2) {
                        // get the text from that node and set it to the treeTextField.
                        keyTextField.setText(split[0]);
                        // set it enabled (if it was disabled last valueChange).
                        keyTextField.setEnabled(true);
                        idTextField.setEnabled(true);
                        String[] split2 = split[1].split("=");
                        if (split2.length == 2){
                            idTextField.setText(split2[0]);
                            valueTextField.setText(split2[1]);
                            valueTextField.setEnabled(true);
                        }
                        else {
                            idTextField.setText(split[1]);
                            valueTextField.setEnabled(false);
                        }
                    }
                    else {
                        keyTextField.setEnabled(false);
                        idTextField.setEnabled(false);
                        valueTextField.setEnabled(false);
                    }
                }
                else {
                    // set Disabled.
                    keyTextField.setEnabled(false);
                    idTextField.setEnabled(false);
                    valueTextField.setEnabled(false);
                }
            }
        });

        // add Action Listener for the JTextField "treeTextField".
        DocumentListener documentListener = (new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent documentEvent) {
                updateTreeNode(treeControl, keyTextField, idTextField, valueTextField);
            }
            @Override
            public void removeUpdate(DocumentEvent documentEvent) {
                updateTreeNode(treeControl, keyTextField, idTextField, valueTextField);
            }
            @Override
            public void changedUpdate(DocumentEvent documentEvent) {
                updateTreeNode(treeControl, keyTextField, idTextField, valueTextField);
            }
        });
        keyTextField.getDocument().addDocumentListener(documentListener);
        idTextField.getDocument().addDocumentListener(documentListener);
        valueTextField.getDocument().addDocumentListener(documentListener);
    }

    private void updateTreeNode (TreeControl tree, JTextField keyTF, JTextField idTF, JTextField valTF){
        // define the node that we want to change.
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        // only handle this, if the new defined node is not 'null'.
        if (node != null && keyTF.isEnabled()) {
            // define the text which should be replaced in the tree node.
            String nodeText = keyTF.getText();
            if (idTF.isEnabled()) {
                // also add the identifier to the text.
                nodeText = nodeText +  " " + idTF.getText();
                if (valTF.isEnabled()){
                    // also add the value to the text.
                    nodeText = nodeText + "=" + valTF.getText();
                }
            }
            // set the new text as userObject to that node.
            node.setUserObject(nodeText);
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
        System.out.println(outputArray);
        return outputArray;
    }



    private void setupTextFieldControl(TextFieldControl control, JPanel pane){

        pane.setBackground(new Color(68,68,68));
        pane.setOpaque(true);

        // create the label with the name.
        JLabel label = new JLabel();
        label.setForeground(new Color(200,200,200));
        label.setText(control.getName() + " : ");
        System.out.println(control.getName());
        label.setBackground(new Color(68,68,68));
        label.setOpaque(true);
        label.setPreferredSize(new Dimension(label.getWidth(), 40));

        // set both alignments to the left.
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        control.setAlignmentX(Component.LEFT_ALIGNMENT);

        // set it first disabled, because it isn't used in the beginning.
        control.setEnabled(false);
        pane.add(label);
        pane.add(control);
    }

    protected void paintComponent(Graphics g)
    {
        g.setColor( getBackground() );
        g.fillRect(0, 0, getWidth(), getHeight());
        super.paintComponent(g);
    }

}
