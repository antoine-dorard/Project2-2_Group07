package main;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;
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

            System.out.println("Actions: " + this.getActions());
            // Adding the slots
            this.getActions().forEach((key, value) -> {
                System.out.println(key);
                try {
                    for (Object o : value.values()) {
                        System.out.println(o);
                        JSONObject subAction = (JSONObject) o;
                        ArrayList<String> slotsArray = generate(subAction);

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

    public ArrayList<String> generate(JSONObject json){
        // the following two lists contain the slots and keys respectively in the order they appear in the JSON until a
        // value is reached. They are cleared after each value is reached, so actions must be taken to create the slot
        // list on the UI when each value is reached.
        ArrayList<String> keysList = new ArrayList<>();

        traverse(json, keysList);
        return keysList;
    }

    private void traverse(JSONObject obj, ArrayList<String> keysList) {
        for (Object key : obj.keySet()) {
            Object value = obj.get(key);
            if (value instanceof JSONObject) {
                String[] splitArray = ((String) key).split(" ");

                if (!keysList.contains(splitArray[1])) {
                    keysList.add(splitArray[1]);
                }

                traverse((JSONObject) value, keysList);

            } else { // value is reached
                if(!key.equals("default")) {
                    String[] splitArray = ((String) key).split(" ");
                    if (!keysList.contains(splitArray[1])) {
                        keysList.add(splitArray[1]);
                    }
                }
            }
        }
    }
}
