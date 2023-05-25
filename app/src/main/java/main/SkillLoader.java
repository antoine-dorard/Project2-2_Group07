package main;

import backend.CFGParser.CFG;
import backend.CFGParser.CNF;
import backend.CFGParser.datastructures.CNFRule;
import backend.CFGParser.datastructures.Terminal;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Iterator;

public class SkillLoader {


    private JSONObject questions; // holds all the possible inputs the user can give

    private HashMap<String, JSONObject> slots; // holds all the possible slots (placeholders) the user can provide
    private HashMap<String, JSONObject> actions; // holds all the possible outputs the bot can send back

    private CFG cfg;
    private CNF cnf;

    public SkillLoader(){

    }

    public void loadCFGandCNF(){
        this.cfg = new CFG();
        this.cfg.readCFG();
        this.cnf = new CNF(cfg);
        this.cnf.generateCNF();
        writeWordsFromCFG();
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

        writeWordsFromQuestions();
    }


    private void writeWordsFromCFG(){
        try {
            Writer clearWriter;
            clearWriter = new FileWriter("app/src/main/java/backend/spelling_checker/words.txt");
            clearWriter.write("");
            clearWriter.close();

            Writer output;
            output = new BufferedWriter(new FileWriter("app/src/main/java/backend/spelling_checker/words.txt", true));

            for (CNFRule rule : this.cnf.getRules()){
                if (rule.getRHS().isTerminal()){
                    for (Terminal word : rule.getRHS().getTerminals()){
                        output.append(word.toString()).append("\n");
                    }
                }
            }

            output.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

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
                            System.out.println(words[i] + " added");
                            output.append(words[i]).append("\n");
                        }
                    }

                }
            }

            // Adding the slots


            this.getSlots().forEach((key, value) -> {
                System.out.println("Slots: " + value);
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

    public CFG getCfg() {
        return cfg;
    }

    public CNF getCnf() {
        return cnf;
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
