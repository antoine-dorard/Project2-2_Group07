package backend;

import main.SkillLoader;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.util.*;

public class CFG_InputProcessor implements InputProcessor {

    SkillLoader skillLoader;
    private String file = "/skills/TimeTableSkill.json";
    private ArrayList<JSONArray> slotsValues;
    private ArrayList<String> slotsKeys;
    private HashMap<String, String> placeHolders;
    private ArrayList<JSONObject> actionObjects;
    private ArrayList<JSONArray> actionArrays;
    private String output;

    public CFG_InputProcessor(SkillLoader skillLoader) {
        this.skillLoader = skillLoader;
    }


    public String processInput(String input) {
        return processInputOnKeywords(input);
    }

    private String processInputOnKeywords(String input){
        JSONParser parser = new JSONParser(); // create a json parser
        try{
            String[] words = input.split("\\s+"); //the array of the words from the user input
            Object obj = parser.parse(new FileReader(new File(getClass().getResource(file).toURI()))); //read the file
            JSONObject jsonObject = (JSONObject)obj; //make an JSON object from the file


            JSONObject subjects = (JSONObject) jsonObject.get("slots"); //From the JSON Object we select the sltos

            slotsValues = new ArrayList(); //Store the number of subarray of the slots
            slotsKeys = new ArrayList<>(); //Store the key of the subarray of the slots

            placeHolders = new HashMap<>(); //Store the key words that match the input from the user and the slots values

            actionObjects = new ArrayList<>(); // Store the actions json objects that are relevant to the user input
            actionArrays = new ArrayList<>();  // Store the actions json arrays that are relevant to the user input

            Iterator<String> keysIter = subjects.keySet().iterator();
            while(keysIter.hasNext()) { //Store all the keys and values of the slots "DAY : [...]"
                String key = keysIter.next();
                slotsKeys.add(key);
                slotsValues.add((JSONArray) subjects.get(key));
            }

            //the for loop is trying to match if words from the input of the user match the words that we have in our slots
            //O(n*m*p) ~ O(n^3)
            for (int i = 0; i < slotsValues.size(); i++) {
                to:
                for (int j = 0; j < words.length; j++) {
                    for (int k = 0; k < (slotsValues.get(i)).size(); k++) {
                        if(words[j].equals((slotsValues.get(i)).get(k))){
                            placeHolders.put(slotsKeys.get(i), words[j]);
                            break to;
                        }
                    }
                }
            }
            JSONObject actions = (JSONObject) jsonObject.get("actions"); //get the JSONObject of the actions
            //loop through placeholders
            // and get all the keys to points to null
            for (String key : slotsKeys) {
                if (placeHolders.get(key) == null) {
                    return "I don't understand, please try again (You might be missing a keyword)";
                }
            }

            traverse(actions);
            return "You have "+ output+ " class at " + placeHolders.get("TIME") + " o'clock on " + placeHolders.get("DAY");


        }
        catch(Exception IO){
            System.err.println("Error in CFG_InputProcessor.java");
            System.out.println(IO.getMessage());
        }

        return "Error in CFG_InputProcessor";
    }

    private String processInputOnFullText(String input){
        return null; // TODO
    }

    public void traverse(Object obj) {
        if (obj instanceof JSONObject) {
            JSONObject jsonObj = (JSONObject) obj;
            for (Object key : jsonObj.keySet()) {

                String[] keySplit = ((String) key).split("\\s+");

                if(placeHolders.containsKey(keySplit[0])){
                    System.out.println("this is a placeholder " + placeHolders.get(keySplit[0]));

                    if(placeHolders.get(keySplit[0]).equals(keySplit[1])){
                        traverse(jsonObj.get(key));
                        System.out.println("this is a match " + jsonObj.get(key));
                        if(jsonObj.get(key) instanceof JSONObject)  actionObjects.add((JSONObject) jsonObj.get(key));
                        else if(jsonObj.get(key) instanceof JSONArray) actionArrays.add((JSONArray) jsonObj.get(key));
                        else {
                            output = (String) jsonObj.get(key);
                            return;
                        }
                    }
                }
            }
        } else if (obj instanceof JSONArray) {
            JSONArray jsonArr = (JSONArray) obj;
            for (Object elem : jsonArr) {
                // TODO in case of arrays in the actions
                traverse(elem);
            }
        } else {
            //System.out.println(obj.toString());
            //TODO maybe going to use it later
        }
    }
}
