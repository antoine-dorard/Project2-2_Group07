package main;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;
import java.util.HashMap;

public class SkillLoader {


    private JSONObject questions; // holds all the possible inputs the user can give

    private HashMap<String, JSONObject> slots; // holds all the possible slots (placeholders) the user can provide
    private HashMap<String, JSONObject> actions; // holds all the possible outputs the bot can send back

    public SkillLoader(){

    }

    public void loadSkills(String[] skills) {
        questions = new JSONObject();
        slots = new HashMap<>();
        actions = new HashMap<>();

        try {

            // Questions
            JSONParser parser = new JSONParser();
            Reader questionsReader = new FileReader(new File(getClass().getResource("/skills/questions.json").toURI()));
            questions = (JSONObject) parser.parse(questionsReader);
            questionsReader.close();

            // Slots and Actions
            Reader slotsReader;
            Reader actionsReader;
            for (String skillName : skills){
                parser = new JSONParser();
                slotsReader = new FileReader(new File(getClass().getResource("/skills/" + skillName + "/slots.json").toURI()));
                actionsReader = new FileReader(new File(getClass().getResource("/skills/" + skillName + "/actions.json").toURI()));

                slots.put(skillName, (JSONObject) parser.parse(slotsReader));
                actions.put(skillName, (JSONObject) parser.parse(actionsReader));

                slotsReader.close();
                actionsReader.close();
            }

        } catch (IOException | ParseException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public JSONObject getQuestions() {
        return questions;
    }

    public HashMap<String, JSONObject> getSlots() {
        return slots;
    }

    public HashMap<String, JSONObject> getActions() {
        return actions;
    }
}
