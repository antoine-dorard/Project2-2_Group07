package panels;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import controls.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import utils.ConfigUI;

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
import java.util.Arrays;
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


    public SkillEditorPanel() {
        super();
        setAlignmentX(JPanel.LEFT_ALIGNMENT);
        // define the main layout of this panel.
        BoxLayout layout = new BoxLayout(this, BoxLayout.X_AXIS);
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
        ListControl questionsList = new ListControl(600, 25, " ");
        questionsList.setVisibleRowCount(4);
        // create a label pane around the list, so it can display the label : "SKILLS"
        CustomLabelPanel customQuestionsListPane = new CustomLabelPanel(
                questionsList.listScroller, "QUESTIONS", 30, 30, 30);
        questionsList.setBorder(createLineBorder(new Color(80, 80, 80), 1));

        // create a TextField below the questions list, to display the selected question in.
        TextFieldControl questionText = new TextFieldControl("Question", 50, 40);
        customQuestionsListPane.addWithGap(questionText, 30);
        customQuestionsListPane.add(Box.createHorizontalStrut(30));
        questionText.setEnabled(false);

        // define a layout with questionsList, tree & other components.
        JPanel rightYPane = new JPanel();
        BoxLayout rightYLayout = new BoxLayout(rightYPane, BoxLayout.Y_AXIS);
        rightYPane.setLayout(rightYLayout);

        rightYPane.add(customQuestionsListPane);
        rightYPane.add(Box.createHorizontalStrut(30));

        // create the Slot List Pane and add the Slot List Controls.
        SlotListPane slotListPane = new SlotListPane();
        slotListPane.scrollPane.setPreferredSize(new Dimension(200, 450));
        slotListPane.addSlot(new String[]{"A", "B", "C"});
        slotListPane.addSlot(new String[]{"D", "E", "F"});
        slotListPane.addSlot(new String[]{"D", "E", "F"});
        slotListPane.addSlot(new String[]{"D", "E", "F"});
        slotListPane.addSlot(new String[]{"WEATHER", "TEACHER", "TIME"});
        slotListPane.addSlot(new String[]{"WEATHER", "TEACHER", "TIME"});
        slotListPane.addSlot(new String[]{"WEATHER", "TEACHER", "TIME"});
        slotListPane.addSlot(new String[]{"WEATHER", "TEACHER", "TIME"});
        slotListPane.addSlot(new String[]{"WEATHER", "TEACHER", "TIME"});

        // set slot list pane a bit more user friendly.
        slotListPane.scrollPane.setAlignmentY(Component.TOP_ALIGNMENT);
        slotListPane.setAlignmentY(Component.TOP_ALIGNMENT);
        slotListPane.setBackground(configUI.colorPanelBG);
        slotListPane.setOpaque(true);

        // add all Components to rightYPane.
        rightYPane.add(Box.createVerticalStrut(30));
        rightYPane.add(slotListPane.scrollPane);
        rightYPane.add(Box.createVerticalStrut(30));
        rightYPane.setAlignmentY(Component.TOP_ALIGNMENT);

        // set some alignment.
        customSkillListPane.setAlignmentY(Component.TOP_ALIGNMENT);
        customQuestionsListPane.setPreferredSize(new Dimension(100, 250));

        // add the controls to the current layout.
        add(customSkillListPane);
        add(Box.createHorizontalStrut(30));
        add(rightYPane);

        // set some UI colors.
        setBackground(configUI.colorPanelBG);
        skillList.setBackground(new Color(63, 63, 63));
        questionsList.setBackground(new Color(63, 63, 63));
        rightYPane.setBackground(configUI.colorPanelBG);
        customQuestionsListPane.setBackground(configUI.colorPanelBG);
        customSkillListPane.setBackground(new Color(63, 63, 63));

        // add Action Listener for the JList "skillList".
        skillList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent evt) {
                // get the currently selected skill.
                String current_skill = skills[skillList.getSelectedIndex()];
                // change the population of the questions list.
                questionsList.setListItems(jsonReader.questions.get(current_skill).toArray(String[]::new));

            }
        });

        // add Selection Listener for the JList "questionsList".
        questionsList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                if(listSelectionEvent.getValueIsAdjusting()){ // ignore multiple events, only process one.
                    return;
                }

                String selected = questionsList.getSelectedValue();
                if (selected != null) {
                    questionText.setText(selected);
                    questionText.setEnabled(true);
                    //
                    populateSlotList(slotListPane,
                            String.format(actionsJSONFilePath,
                                    skillList.getSelectedValue()), 1);
                } else {
                    // selection changed to 'no selection', so set textField disabled.
                    questionText.setEnabled(false);
                }
            }
        });

        // add DocumentListener to the questionText JTextField.
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
    }


    private void populateSlotList(SlotListPane slotListPane, String actionsJSONFilePath, int questionIdx){
        try {
            JSONParser parser = new JSONParser(); // create a json parser
            // read the file and parse the data
            JSONObject obj = (JSONObject) parser.parse(new FileReader(actionsJSONFilePath));
            for (Object keyStr : obj.keySet()) {
                if(keyStr.equals(String.format("%d",questionIdx))){
                    slotListPane.populate((JSONObject) obj.get(keyStr));
                };
            }
        }
        catch (ParseException | IOException e) {
            throw new RuntimeException(e);
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
        if(text.hasFocus()){
            list.listModel.set(list.getSelectedIndex(), text.getText());
        }
    }


    protected void paintComponent(Graphics g)
    {
        g.setColor( getBackground() );
        g.fillRect(0, 0, getWidth(), getHeight());
        super.paintComponent(g);
    }

    /*
    private void updateTreeNode (TreeControl tree, JTextField keyTF, JTextField idTF, JTextField valTF){
        // define the node that we want to change.
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        // only handle this, if the new defined node is not 'null'.
        if (node != null && keyTF.isEnabled() && (keyTF.hasFocus() || idTF.hasFocus() || valTF.hasFocus())) {
            // define the text which should be replaced in the tree node.
            String nodeText = keyTF.getText();
            if (idTF.isEnabled()) {
                // also add the identifier to the text.
                nodeText = nodeText +  " " + idTF.getText();
                if (valTF.isEnabled()){
                    // also add the value to the text.
                    nodeText = nodeText + " : " + valTF.getText();
                }
            }
            // set the new text as userObject to that node.
            node.setUserObject(nodeText);
            // get the model and call nodeChanged on the model (to update).
            ((DefaultTreeModel) tree.getModel()).nodeChanged(node);
        }
    }
    */

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