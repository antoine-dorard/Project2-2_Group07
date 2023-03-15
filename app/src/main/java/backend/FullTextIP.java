package backend;

import main.SkillLoader;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class FullTextIP implements InputProcessor{
    private SkillLoader skillLoader;

    private HashMap<String, String> placeHolders;


    public FullTextIP(SkillLoader skillLoader) {
        this.skillLoader = skillLoader;
        placeHolders = new HashMap<>();
    }

    @Override
    public String processInput(String input) {
        return processInputOnFullText(input);
    }


    private String processInputOnFullText(String input){
        input = input.toUpperCase();

        //ArrayList<String> questions = new ArrayList<>();
        String foundSkill = "";
        String foundQuestion = "";

        Iterator<String> skillIter = skillLoader.getQuestions().keySet().iterator();

        mainloop:
        while(skillIter.hasNext()){
            String skillKey = skillIter.next();
            JSONObject skillObject = (JSONObject) skillLoader.getQuestions().get(skillKey);

            // Looping through the questions
            Iterator<String> questionIter = skillObject.keySet().iterator();
            while(questionIter.hasNext()){
                String questionKey = questionIter.next();
                String question = ((String) skillObject.get(questionKey)).toUpperCase();

                int inputIndex = 0;
                for (int questIndex = 0; questIndex < question.length(); questIndex++) {

                    // traverse the question and input until we find a mismatch
                    if (question.charAt(questIndex) == input.charAt(inputIndex)) {
                        inputIndex++;
                    }
                    else if (question.charAt(questIndex) == '<') {

                        int questPlaceholderStart = questIndex;
                        int questPlaceholderEnd;

                        // Skipping the placeholder in the question
                        while (question.charAt(questIndex) != '>') {
                            questIndex++;
                        }

                        questPlaceholderEnd = questIndex;

                        int inputPlaceholderStart = inputIndex;
                        int inputPlaceholderEnd;

                        // Skipping the placeholder in the input
                        while (inputIndex < input.length() && !(input.charAt(inputIndex) == ' ' || input.charAt(inputIndex) == '.' || input.charAt(inputIndex) == ',' || input.charAt(inputIndex) == '?')) {
                            inputIndex++;
                        }

                        inputPlaceholderEnd = inputIndex;

                        String questPlaceholder = question.substring(questPlaceholderStart+1, questPlaceholderEnd);
                        String inputPlaceholder = input.substring(inputPlaceholderStart, inputPlaceholderEnd);
                        placeHolders.put(questPlaceholder, inputPlaceholder);

                    }
                    else {
                        System.out.println("## No match for question: " + questionKey + " in skill: " + skillKey);
                        System.out.println("Position: "+ question.substring(0, questIndex) + " vs " + input.substring(0, inputIndex));
                        break;
                    }

                    // we found the question if we have reached the end of the input
                    if(questIndex == question.length() - 1){
                        foundSkill = skillKey;
                        foundQuestion = questionKey;
                        break mainloop; // The question is found, no need to continue looping through the questions
                    }
                }
            }
        }

        System.out.println("## Found skill: " + foundSkill + " and question: " + foundQuestion);
        if(foundSkill.equals("") || foundQuestion.equals("")){
            return "That's not a question I can answer";
        }

        return traverse(skillLoader.getActions().get(foundSkill).get(foundQuestion), foundSkill, foundQuestion);
    }


    public String traverse(Object obj, String foundSkill, String foundQuestion) {
        if (obj instanceof JSONObject) {
            JSONObject jsonObj = (JSONObject) obj;
            for (Object key : jsonObj.keySet()) {

                String[] keySplit = ((String) key).split("\\s+");

                if(placeHolders.containsKey(keySplit[0])){
                    System.out.println("this is a placeholder " + placeHolders.get(keySplit[0]));

                    if(placeHolders.get(keySplit[0]).equalsIgnoreCase(keySplit[1])){
                        System.out.println("this is a match " + jsonObj.get(key));
                        if(jsonObj.get(key) instanceof JSONObject){
                            return traverse(jsonObj.get(key), foundSkill, foundQuestion);
                        }
                        else if(jsonObj.get(key) instanceof JSONArray) {
                            throw new RuntimeException("Please provide a correct actions file structure (JSON Object, not JSON Array)");
                        }
                        else {
                            return (String) jsonObj.get(key);
                        }
                    }
                }
            }
        } else if (obj instanceof JSONArray) {
            throw new RuntimeException("Please provide a correct actions file structure (JSON Object, not JSON Array)");
        } else {
            //System.out.println(obj.toString());
            //TODO maybe going to use it later
        }

        Object defaultAnswer = ((JSONObject) skillLoader.getActions().get(foundSkill).get(foundQuestion)).get("default");

        return defaultAnswer == null ? "I understood the question but the answer could not be found" : defaultAnswer.toString();
    }
}
