package main;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

public class SkillLoader {


    private JSONObject questions; // holds all the possible inputs the user can give

    private HashMap<String, JSONObject> slots; // holds all the possible slots (placeholders) the user can provide
    private HashMap<String, JSONObject> actions; // holds all the possible outputs the bot can send back

    private String resourcePath = "./app/src/main/resources/";

    public SkillLoader(){

    }

    public void loadSkills() {
        questions = new JSONObject();
        slots = new HashMap<>();
        actions = new HashMap<>();

        try {

            // Questions
            JSONParser parser = new JSONParser();
            Reader questionsReader = new FileReader(resourcePath + "skills/questions.json");
            questions = (JSONObject) parser.parse(questionsReader);
            questionsReader.close();

            // (Getting the skills)
            String[] skills = (String[]) questions.keySet().toArray(new String[0]);
            System.out.println("Skills: " + Arrays.toString(skills));

            // Slots and Actions
            Reader slotsReader;
            Reader actionsReader;
            for (String skillName : skills){
                parser = new JSONParser();

//                if(new File(resourcePath + "/skills/" + skillName + "/slots.json") != null){
//                    slotsReader = new FileReader(resourcePath + "/skills/" + skillName + "/slots.json");
//                    slots.put(skillName, (JSONObject) parser.parse(slotsReader));
//                    slotsReader.close();
//                }

                actionsReader = new FileReader(resourcePath + "/skills/" + skillName + "/actions.json");
                actions.put(skillName, (JSONObject) parser.parse(actionsReader));
                actionsReader.close();
            }

        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }

        writeWordsFromQuestions();
    }


    private void writeWordsFromQuestions(){

        Iterator<String> skillIter = this.getQuestions().keySet().iterator();


        try {
            Writer clearWriter;
            clearWriter = new FileWriter("app/src/main/java/backend/spelling_checker/words.txt");
            clearWriter.write("");
            clearWriter.close();

            Writer output;
            output = new BufferedWriter(new FileWriter("app/src/main/java/backend/spelling_checker/words.txt", true));

            // Adding the actions
            while(skillIter.hasNext()){
                String skillKey = skillIter.next();
                JSONObject skillObject = (JSONObject) this.getQuestions().get(skillKey);

                // Looping through the questions
                Iterator<String> questionIter = skillObject.keySet().iterator();
                while(questionIter.hasNext()){
                    String questionKey = questionIter.next();
                    String question = ((String) skillObject.get(questionKey)).toUpperCase();

                    String[] words = question.split("\\s+");

                    for (int i = 0; i < words.length; i++) {
                        if(!words[i].contains("<") && !words[i].contains(">")){
                            output.append(words[i]).append("\n");
                        }
                    }

                }
            }

            // Adding the slots


            this.getSlots().forEach((key, value) -> {
                try {
                    for (Object o : value.values()) {
                        JSONArray slotsArray = (JSONArray) o;

                        for (Object o1 : slotsArray) {
                            output.append((String) o1).append("\n");
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            output.close();

        } catch (IOException e) {
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
