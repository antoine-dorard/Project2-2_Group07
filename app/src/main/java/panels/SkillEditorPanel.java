package panels;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import controls.CustomLabelPanel;
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
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.String.format;
import static javax.swing.BorderFactory.createEmptyBorder;
import static javax.swing.BorderFactory.createLineBorder;

public class SkillEditorPanel extends JPanel{


    // define the background image.
    private final ImageIcon background = new ImageIcon(getClass().getResource("/imgs/chatbot_icon_transp.png"));

    // colors & fonts are defined in the UIConfig class.
    private final ConfigUI configUI = new ConfigUI();

    // define the skills array list.
    String[] skills;

    // define the file path, where we should look for the questions.json file.
    String questionsJSONFilePath = "./app/src/main/resources/skills/questions.json";
    // define the unformatted file path, where we should look for the actions.json files.
    String actionsJSONFilePath = "./app/src/main/resources/skills/%s/actions.json";


    public SkillEditorPanel(){
        super();
        setAlignmentX(JPanel.LEFT_ALIGNMENT);
        // define the main layout of this panel.
        //FlowLayout layout = new FlowLayout();
        //layout.setAlignment(FlowLayout.LEADING);//LEFT);
        BoxLayout layout = new BoxLayout(this, BoxLayout.X_AXIS);
        // set gaps to zero, it should stick to the left.
        //layout.setVgap(0);
        //layout.setHgap(0);
        //layout.setAlignOnBaseline(true);
        // set the layout
        setLayout(layout);

        // define the skills array and populate it from the questions.json
        ReadFromJSON jsonReader = new ReadFromJSON(questionsJSONFilePath);
        // get the skills list from the jsonReader and cast it to String[]
        skills = jsonReader.skills.toArray(String[]::new);

        // define a new ListControl to display all skills.
        ListControl skillList = new ListControl(175, 25, " ");
        // populate the skill List with the found skills in the questions.json
        skillList.setListItems(skills);
        skillList.setPreferredSize(new Dimension(200, 650));
        // create a label pane around the list, so it can display the label : "SKILLS"
        CustomLabelPanel customSkillListPane = new CustomLabelPanel(
                skillList, "SKILLS", 30, 30, 30);

        // define a new ListControl to display all questions from the currently selected skill.
        ListControl questionsList = new ListControl(600, 25, "##");
        questionsList.setVisibleRowCount(4);
        // create a label pane around the list, so it can display the label : "SKILLS"
        CustomLabelPanel customQuestionsListPane = new CustomLabelPanel(
                questionsList.listScroller, "QUESTIONS", 30, 30, 30);
        questionsList.setBorder(createLineBorder(new Color(80,80,80), 1));

        // create a TextField below the questions list, to display the selected question in.
        TextFieldControl questionText = new TextFieldControl("Question", 50, 40);
        customQuestionsListPane.addWithGap(questionText, 30);
        customQuestionsListPane.add(Box.createHorizontalStrut(30));
        questionText.setEnabled(false);

        // define a new TreeControl
        TreeControl treeControl = new TreeControl();//200, 400);
        treeControl.setVisibleRowCount(10);
        // create a label pane around the tree, so it can display the label : "ACTIONS"
        CustomLabelPanel customTreePane = new CustomLabelPanel(
                treeControl.listScroller, "ACTIONS", 30, 30, 30);
        customTreePane.setPreferredSize(new Dimension(300, 350));
        customTreePane.spacingPane.add(Box.createVerticalStrut(30));

        // define a button to save changes.
        JButton addToJsonBtn = new JButton("Save");
        addToJsonBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                treeControl.writeJsonToFile(treeControl.convertTreeToJson(
                        (DefaultMutableTreeNode) treeControl.getModel().getRoot()),format(actionsJSONFilePath,
                        skills[skillList.getSelectedIndex()]));
            }
        });

        // define a new TextFieldControl, with a specific width & height.
        TextFieldControl keyTextField = new TextFieldControl("Key",400, 40);
        TextFieldControl idTextField = new TextFieldControl("Identifier",400, 40);
        TextFieldControl valueTextField = new TextFieldControl("Value",400, 40);

        // define a layout with questionsList, tree & other components.
        JPanel rightYPane = new JPanel();
        BoxLayout rightYLayout = new BoxLayout(rightYPane, BoxLayout.Y_AXIS);
        rightYPane.setLayout(rightYLayout);

        // define a layout with tree and textFields for editing (X aligned).
        JPanel treePane = new JPanel();
        BoxLayout treeLayout = new BoxLayout(treePane, BoxLayout.X_AXIS);
        treePane.setLayout(treeLayout);

        // define a layout with all textFields (Y aligned).
        JPanel textFieldPane = new JPanel();
        BoxLayout textFieldLayout = new BoxLayout(textFieldPane, BoxLayout.Y_AXIS);
        textFieldPane.setLayout(textFieldLayout);

        // set up the controls in the right panel.
        textFieldPane.add(Box.createVerticalStrut(100));
        setupTextFieldControl(keyTextField, textFieldPane);
        setupTextFieldControl(idTextField, textFieldPane);
        setupTextFieldControl(valueTextField, textFieldPane);
        textFieldPane.add(Box.createVerticalStrut(30));
        textFieldPane.add(addToJsonBtn);
        textFieldPane.add(Box.createVerticalStrut(30));
        textFieldPane.setAlignmentY(Component.TOP_ALIGNMENT);

        treePane.add(customTreePane);
        treePane.add(Box.createHorizontalStrut(30));
        treePane.add(textFieldPane);
        treePane.add(Box.createHorizontalStrut(30));
        treePane.setAlignmentY(Component.TOP_ALIGNMENT);

        rightYPane.add(customQuestionsListPane);
        rightYPane.add(Box.createHorizontalStrut(30));
        rightYPane.add(treePane);
        rightYPane.add(Box.createHorizontalStrut(30));
        rightYPane.setAlignmentY(Component.TOP_ALIGNMENT);

        customSkillListPane.setAlignmentY(Component.TOP_ALIGNMENT);
        customQuestionsListPane.setPreferredSize(new Dimension(100, 250));

        addToJsonBtn.setAlignmentY(Component.TOP_ALIGNMENT);
        // add the controls to the current layout.
        add(customSkillListPane);
        add(Box.createHorizontalStrut(30));
        add(rightYPane);

        // set some UI colors.
        setBackground(configUI.colorPanelBG);
        skillList.setBackground(new Color(63,63,63));
        questionsList.setBackground(new Color(63,63,63));
        treeControl.setBackground(configUI.colorPanelBG);
        treeControl.setForeground(configUI.colorListFG);

        rightYPane.setBackground(configUI.colorPanelBG);
        treePane.setBackground(configUI.colorPanelBG);
        textFieldPane.setBackground(configUI.colorPanelBG);
        customTreePane.setBackground(configUI.colorPanelBG);
        customQuestionsListPane.setBackground(configUI.colorPanelBG);
        customSkillListPane.setBackground(new Color(63,63,63));

        // add Action Listener for the JList "skillList".
        skillList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent evt) {
                // get the currently selected skill.
                String current_skill = skills[skillList.getSelectedIndex()];
                // change the population of the tree, if something else was selected in the skill list.
                treeControl.parseJSONtoTree(format(actionsJSONFilePath, current_skill));
                questionsList.setListItems(jsonReader.questions.get(current_skill).toArray(String[]::new));
            }
        });

        questionsList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                String selected = questionsList.getSelectedValue();
                if (selected!=null){
                    // getElementAt is overriden to remove the prefix, that's why it's used here.
                    questionText.setText(selected);
                    questionText.setEnabled(true);
                }
                else{
                    // selection changed to 'no selection', so set textField disabled.
                    questionText.setEnabled(false);
                }
            }
        });

        DocumentListener questionsDocListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent documentEvent) {
                updateQuestionList(questionsList, questionText);
            }
            @Override
            public void removeUpdate(DocumentEvent documentEvent) {
                updateQuestionList(questionsList, questionText);
            }
            @Override
            public void changedUpdate(DocumentEvent documentEvent) {
                updateQuestionList(questionsList, questionText);
            }
        };
        questionText.getDocument().addDocumentListener(questionsDocListener);

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

    private ArrayList<String> getSkills (String questionsFilePath) {
        // define arrayList of strings to output as skills.
        ArrayList<String> outputArray = new ArrayList<String>();
        try {
            JSONParser parser = new JSONParser(); // create a json parser
            // read the file and parse the data
            Object obj = parser.parse(new FileReader(questionsFilePath));
            JSONObject jsonObject = (JSONObject) obj;
            for (Object keyStr : jsonObject.keySet()) {
                // for every found key in the question.json, add it to the arrayList.
                outputArray.add((String) keyStr);
            }
        } catch (ParseException | IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(outputArray);
        return outputArray;
    }

    private void updateQuestionList(ListControl list, JTextField text){
        //list.setSelectedValue(text.getText(), false);
        //list.listModel.set(list.getSelectedIndex(), text.getText());
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

final class ReadFromJSON {

    // define arrayList of strings to define the skills.
    public ArrayList<String> skills = new ArrayList<String>();

    // define a HashMap, with as key the skill string, and as value an arrayList of questions.
    public HashMap<String,ArrayList<String>> questions=new HashMap<String,ArrayList<String>>();

    public ReadFromJSON(String questionsFilePath){
        try {
            JSONParser parser = new JSONParser(); // create a json parser
            // read the file and parse the data
            Object obj = parser.parse(new FileReader(questionsFilePath));
            JSONObject jsonObject = (JSONObject) obj;
            for (Object keyStr : jsonObject.keySet()) {
                // for every found key in the question.json, add it to the arrayList.
                skills.add((String)keyStr);
                // create a new JSONObject to go another level deeper (to find the questions).
                JSONObject newJSONObject = (JSONObject) jsonObject.get(keyStr);
                // also define the questions array in which we will save the questions found.
                ArrayList<String> qArray = new ArrayList<>();
                for (Object keyStr2 : newJSONObject.keySet()) {
                    // add the questions found.
                    qArray.add((String)newJSONObject.get(keyStr2));
                }
                // put the hash map element as, current skill string & current questions array.
                questions.put((String)keyStr, qArray);
            }
        }
        catch (ParseException | IOException e) {
            throw new RuntimeException(e);
        };
    }
}