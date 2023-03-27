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
import java.util.Collections;
import java.util.HashMap;

import static java.lang.String.format;
import static java.lang.Thread.sleep;
import static javax.swing.BorderFactory.createEmptyBorder;
import static javax.swing.BorderFactory.createLineBorder;

public class SkillEditorPanel extends JPanel{

    private final ConfigUI configUI = new ConfigUI(); // colors & fonts are defined in the UIConfig class.

    String questionsJSONFilePath = "./app/src/main/resources/skills/questions.json";
    String actionsJSONFilePath = "./app/src/main/resources/skills/%s/actions.json";
    String templateActionsJSONFilePath = "./app/src/main/resources/skills/template-actions.json";
    QuestionsJSON questionsJSON;
    ActionsJSON actionsJSON;

    String[] skills;  // define the skills array list.
    ListControl skillList; // define a new ListControl.
    CustomLabelPanel customSkillListPane; // with it, define a custom pane for that ListControl.

    ListControl questionsList;
    CustomLabelPanel customQuestionsListPane;
    TextFieldControl questionText;
    ButtonPane questionButtonPane;

    SlotListPane slotListPane;
    ButtonPane slotListButtonPane;



    public SkillEditorPanel() {
        super();
        setAlignmentX(JPanel.LEFT_ALIGNMENT);
        // define the main layout of this panel.
        BoxLayout layout = new BoxLayout(this, BoxLayout.X_AXIS);
        // set the layout
        setLayout(layout);

        // initialize the skill list.
        initSkillList();

        // initialize the question list.
        initQuestionList();

        // initialize the slot list pane (which will contain a list of slots)
        initSlotListPane();

        // initialize the buttons belonging to the question list.
        initQuestionButtons();

        // initialize the buttons belonging to the slot list.
        initSlotListButtons();

        // define the panel that will be to the right of the skills Pane.
        JPanel rightYPane = new JPanel();
        BoxLayout rightYLayout = new BoxLayout(rightYPane, BoxLayout.Y_AXIS);
        rightYPane.setLayout(rightYLayout);

        // add the Question List.
        rightYPane.add(customQuestionsListPane);
        rightYPane.add(Box.createHorizontalStrut(30));

        // add the buttons for the Question List.
        rightYPane.add(questionButtonPane);
        rightYPane.add(Box.createVerticalStrut(30));

        // add the Slot List with a scrollbar.
        rightYPane.add(slotListPane.scrollPane);
        rightYPane.add(Box.createVerticalStrut(30));

        // add the buttons for the Slot List.
        rightYPane.add(slotListButtonPane);
        rightYPane.setAlignmentY(Component.TOP_ALIGNMENT);

        // set some alignments.
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

        // for startup, select the first skill automatically (do this after the listener is initialized).
        skillList.setSelectedIndex(0);
    }

    private void initSkillList(){

        // first read the questions.json
        readQuestionsJSON();

        // define a new ListControl to display all skills.
        skillList = new ListControl(175, 25);
        // populate the skill List with the found skills in the questions.json
        skillList.setListItems(skills);
        skillList.setPreferredSize(new Dimension(200, 650));
        // create a label pane around the list, so it can display the label : "SKILLS"
        customSkillListPane = new CustomLabelPanel(
                skillList, "SKILLS", 30, 30, 30);

        JButton addSkillBtn = new JButton();
        addSkillBtn.setText("Add Skill");

        customSkillListPane.spacingPane.add(addSkillBtn);

        // add Action Listener for the JList "skillList".
        skillList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent evt) {
                populateQuestionList(true);
                //questionsList.setSelectedIndex(-1);
                //populateSlotList();
                slotListPane.removeAll();
            }
        });

        // add Action Listener for the Add Skill Button.
        addSkillBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String newSkillStr = "Skill";
                if(questionsJSON.skills.contains("Skill")){
                    int i = 0;
                    for(String skill: questionsJSON.skills){
                        if(skill=="Skill"){
                            i = i + 1;
                        }
                    }
                    newSkillStr = String.format(newSkillStr + "(%d)", i);
                }
                questionsJSON.skills.add(newSkillStr);
                questionsJSON.questions.put(newSkillStr, new ArrayList<>());
                skillList.listModel.addElement(newSkillStr);
            }
        });
    }

    private void initQuestionList(){
        // define a new ListControl to display all questions from the currently selected skill.
        questionsList = new ListControl(600, 25);
        questionsList.setVisibleRowCount(4);
        // create a label pane around the list, so it can display the label : "SKILLS"
        customQuestionsListPane = new CustomLabelPanel(
                questionsList.listScroller, "QUESTIONS", 30, 30, 30);
        questionsList.setBorder(createLineBorder(new Color(80, 80, 80), 1));

        // create a TextField below the questions list, to display the selected question in.
        questionText = new TextFieldControl("Question", 50, 40);
        customQuestionsListPane.addWithGap(questionText, 30);
        customQuestionsListPane.add(Box.createHorizontalStrut(30));
        questionText.setEnabled(false);

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
                    populateSlotList();
                } else {
                    // selection changed to 'no selection', so set textField disabled.
                    questionText.setEnabled(false);
                    // remove the slotList controls from the slotList pane.
                    slotListPane.removeAll();
                }
            }
        });

        // add DocumentListener to the questionText JTextField.
        questionText.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent documentEvent) {
                if(questionText.hasFocus()){
                    saveQuestion();
                }}
            @Override
            public void removeUpdate(DocumentEvent documentEvent) {
                if(questionText.hasFocus()){
                    saveQuestion();
            }}
            @Override
            public void changedUpdate(DocumentEvent documentEvent) {
                if(questionText.hasFocus()){
                    saveQuestion();
            }}
        });
    }

    private void initQuestionButtons(){
        questionButtonPane = new ButtonPane();
        questionButtonPane.addButtons(new String[] {"Add New", "Revert", "Save"});
        ButtonControl addNewBtn = (ButtonControl) questionButtonPane.getComponents()[0];
        ButtonControl revertBtn = (ButtonControl) questionButtonPane.getComponents()[1];
        ButtonControl saveBtn = (ButtonControl) questionButtonPane.getComponents()[2];

        // add ActionListener to the AddNewButton.
        addNewBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String current_skill = skills[skillList.getSelectedIndex()];
                questionsJSON.questions.get(current_skill).add("New Question with <SLOT1> and <SLOT2>");
                try {
                    JSONParser parser = new JSONParser(); // create a json parser
                    // read the file and parse the data
                    JSONObject obj = (JSONObject) parser.parse(new FileReader(templateActionsJSONFilePath));
                    System.out.println(obj);
                    // add to current actionsJSON.
                    actionsJSON.actions.add(obj);
                    System.out.println(actionsJSON.actions);
                }
                catch (ParseException | IOException e) {
                    throw new RuntimeException(e);
                }
                questionsList.setSelectedIndex(-1);
                populateQuestionList(false);
            }
        });

        // add ActionListener to the RevertButton.
        revertBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                readQuestionsJSON();
                populateQuestionList(true);
                populateSlotList();
            }
        });

        // add ActionListener to the Save Button.
        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                saveQuestionsToJSON();
            }
        });
    }

    private void initSlotListPane(){
        // create the Slot List Pane and add the Slot List Controls.
        slotListPane = new SlotListPane();
        slotListPane.scrollPane.setPreferredSize(new Dimension(200, 450));

        // set slot list pane a bit more user friendly.
        slotListPane.scrollPane.setAlignmentY(Component.TOP_ALIGNMENT);
        slotListPane.setAlignmentY(Component.TOP_ALIGNMENT);
        slotListPane.setBackground(configUI.colorPanelBG);
        slotListPane.setOpaque(true);
    }

    private void initSlotListButtons(){
        slotListButtonPane = new ButtonPane();
        slotListButtonPane.addButtons(new String[] {"Add New", "Revert", "Save"});
        ButtonControl addNewBtn = (ButtonControl) slotListButtonPane.getComponents()[0];
        ButtonControl revertBtn = (ButtonControl) slotListButtonPane.getComponents()[1];
        ButtonControl saveBtn = (ButtonControl) slotListButtonPane.getComponents()[2];

        // add ActionListener to the AddNewButton.
        addNewBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                slotListPane.addNewSlot();
            }
        });

        // add ActionListener to the RevertButton.
        revertBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                slotListPane.removeAll();
                populateSlotList();
            }
        });

        // add ActionListener to the Save Button.
        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                saveActionsToJSON();
            }
        });
    }

    private void populateSlotList(){
        int selectedIdx = questionsList.getSelectedIndex();
        if(selectedIdx>=0){
            slotListPane.generate(actionsJSON.actions.get(selectedIdx));
        }
        else{
            // no selection, so remove all actions
            slotListPane.removeAll();
        }
    }

    private void readQuestionsJSON(){
        // define the skills array and populate it from the questions.json
        questionsJSON = new QuestionsJSON(questionsJSONFilePath);

        // get the skills list from the jsonReader and cast it to String[]
        skills = questionsJSON.skills.toArray(String[]::new);
    }

    private void populateQuestionList(Boolean refreshActions){
        if(skillList.getSelectedIndex() >= 0 ) {
            // get the currently selected skill.
            String current_skill = skills[skillList.getSelectedIndex()];
            // change the population of the questions list.
            questionsList.setListItems(questionsJSON.questions.get(current_skill).toArray(String[]::new));

            if(refreshActions){
                // create the actions List from the selected skill.
                actionsJSON = new ActionsJSON(String.format(actionsJSONFilePath, current_skill));
            }
        }
    }

    private void saveQuestion(){
        questionsList.listModel.set(questionsList.getSelectedIndex(), questionText.getText());
    }

    private void saveQuestionsToJSON() {
        System.out.println(questionsJSON.questions);

        if (skillList.getSelectedIndex() >= 0) {
            // get the currently selected skill.
            String current_skill = skills[skillList.getSelectedIndex()];
            int i = 0;
            for (String question : questionsJSON.questions.get(current_skill)) {
                //checkQuestionWithActions(question, i);

                // reverse generate on the question.
                // put that in actionsJSON.actions
                // also put it in questionsJSON.questions
                populateSlotList(); // refresh SlotList.
                // save to file

                i = i + 1;
            }
        }
    }

    private void saveActionsToJSON(){
        System.out.println(actionsJSON.actions);
        // reverse_generate
        // put it in actionsJSON.actions
        // save to file.
    }

    /*
    private void checkQuestionWithActions(String question, int idx){
        JSONObject obj = actionsJSON.actions.get(idx);
        int ids = ((SlotListControl) slotListPane.getComponents()[0]).getComponentCount()-2; // remove&actionBtn=-2
        // count the number of times the element '<' occurs.
        int count = 0;
        for(int i=0; i<question.length(); i++){
            if(question.substring(i,i+1).equals("<")){
                count = count + 1;
            }
        }
        String questionSub = question;
        ArrayList<String> array = new ArrayList<>();
        for (int i=0; i<count; i++){
            String sub1 = questionSub.substring(questionSub.indexOf("<")+1);
            String id = sub1.substring(0,sub1.indexOf(">"));
            questionSub = sub1.substring(sub1.indexOf(">")+1);
            array.add(id);
        }
        if(ids==count){
            Boolean stop = false;
            JSONObject newObj = obj;
            ArrayList<String> strArr = new ArrayList<>();
            while(stop==false){
                for (Object keyStr: obj.keySet()){
                    String str = (String) keyStr;
                    if (str.contains(" ")){
                        str = str.substring(0, ((String) keyStr).indexOf(" "));
                    }
                    if (str!="default" && !strArr.contains(str)){
                        strArr.add(str);
                        try {
                            newObj = (JSONObject) newObj.get(keyStr);
                        }
                        catch (Exception ignore){stop=true;}
                    }
                }
            }
            System.out.println(strArr);
        }
        else {
            JSONObject previousJSONObj = new JSONObject();
            JSONObject newJSONObj = new JSONObject();
            System.out.println(array);
            for (int i = 0; i < array.size(); i++) {
                String str = array.get(array.size() - i - 1); // get elements in reverse.
                newJSONObj = new JSONObject();
                if (i == 0) {
                    // first call
                    newJSONObj.put(str + " ", "");
                } else {
                    newJSONObj.put(str + " ", previousJSONObj);
                }
                previousJSONObj = newJSONObj;
            }
            newJSONObj.put("default", "");
            actionsJSON.actions.set(idx, newJSONObj);
            System.out.println(actionsJSON.actions);
            populateSlotList();
        }
    }
     */

    protected void paintComponent(Graphics g)
    {
        g.setColor( getBackground() );
        g.fillRect(0, 0, getWidth(), getHeight());
        super.paintComponent(g);
    }
}

final class QuestionsJSON {

    // define arrayList of strings to define the skills.
    public ArrayList<String> skills = new ArrayList<String>();

    // define a HashMap, with as key the skill string, and as value an arrayList of questions.
    public HashMap<String,ArrayList<String>> questions=new HashMap<String,ArrayList<String>>();

    public QuestionsJSON(String questionsFilePath){
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

final class ActionsJSON {

    public ArrayList<JSONObject> actions = new ArrayList<>();

    public ActionsJSON(String JSONFilePath){
        try {
            JSONParser parser = new JSONParser(); // create a json parser
            // read the file and parse the data
            JSONObject obj = (JSONObject) parser.parse(new FileReader(JSONFilePath));
            for (Object keyStr : obj.keySet()) {
                actions.add((JSONObject) obj.get(keyStr));
            }
        }
        catch (ParseException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}