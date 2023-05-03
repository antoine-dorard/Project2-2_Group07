package backend.CFG;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class CFG {
    private HashMap<String, Object> cfgRulesHM = new HashMap<>();
    private ArrayList<Object> cfgActions = new ArrayList<>();

    public CFG(){
        readRules();
        readActions();
    }

    public void readRules(){
        JSONParser parser = new JSONParser();

        try {
            File cfgRulesFile = new File(getClass().getResource("/CFG/rules.json").toURI());
            Reader ruleReader = new FileReader(cfgRulesFile);
            JSONObject rules = (JSONObject) parser.parse(ruleReader);

            // storing every CFG rule in a hashmap
            Iterator keys = rules.keySet().iterator();

            // while there are keys+values left to be added to the HM
            while (keys.hasNext()){
                String key = (String) keys.next();
                // if there are multiple elements in the value, create arraylist
                if (rules.get(key) instanceof JSONArray) {
                    JSONArray arr = (JSONArray) rules.get(key);
                    Iterator<String> values = arr.iterator();
                    ArrayList<String> ruleList = new ArrayList<>();
                    // while there are still values left in the array
                    while (values.hasNext())
                        ruleList.add(values.next());
                    cfgRulesHM.put(key, ruleList);
                }
                // if there is only one element in the value, add directly to HM
                else
                    cfgRulesHM.put(key, rules.get(key));
            }

            ruleReader.close();
        } catch (URISyntaxException | IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public void readActions(){
        JSONParser parser = new JSONParser();

        try {
            File cfgActionsFile = new File(getClass().getResource("/CFG/actions.json").toURI());
            Reader actionReader = new FileReader(cfgActionsFile);

            // the root object
            JSONObject actions = (JSONObject) parser.parse(actionReader);

            // create CFGAction object for each action in the file
            Iterator keys = actions.keySet().iterator();
            for (Object skill : actions.keySet()){
                String skillName = (String) keys.next();
                traverse(actions.get(skill), skillName, new HashMap<>());
            }
            actionReader.close();
        } catch (URISyntaxException | IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    // based on the traverse method of phase 1...
    // traverse per skill, and create CFGaction object for each action
    public void traverse(Object obj, String skillName, HashMap<String, String> HM){
        String answer;

        if (obj instanceof JSONObject) {

            JSONObject jsonObj = (JSONObject) obj;
            for (Object key : jsonObj.keySet()){

                // initial call; use an empty HM to store the slot-value pairs
                HashMap<String, String> slotValuePair = new HashMap<>();

                // for recursive call; use the partly filled-in HM
                if(!HM.isEmpty())
                    slotValuePair.putAll(HM);

                String[] keySplit = ((String) key).split("\\s+");

                try {
                    // try splitting the keyword and its value
                    slotValuePair.put(keySplit[0], keySplit[1]);
                } catch (IndexOutOfBoundsException e){
                    // if 'default', there is no second index in the array (no slot-value)
                    slotValuePair.put(keySplit[0], null);
                }

                // if there is a json object within a json object, recursive call
                if (jsonObj.get(key) instanceof JSONObject){
                    traverse(jsonObj.get(key), skillName, slotValuePair);
                }
                // if we found an answer, add action to cfg
                else {
                    answer = (String) jsonObj.get(key);
                    CFGAction cfgAction = new CFGAction(skillName, slotValuePair, answer);
                    cfgActions.add(cfgAction);
                }
            }

        } else if (obj instanceof String) { // this action has no slotValuePairs
            answer = (String) obj;
            CFGAction cfgAction = new CFGAction(skillName, HM, answer);
            cfgActions.add(cfgAction);

        } else {
            //System.out.println(obj.toString());
            //TODO maybe going to use it later
        }
    }

    // getters
    public ArrayList<Object> getCfgActions() { return cfgActions; }
    public HashMap<String, Object> getCfgRules(){ return cfgRulesHM; }

    public static void main(String[] args) {
        CFG cfg = new CFG();
        System.out.println(cfg.cfgRulesHM);
        System.out.println(cfg.cfgActions);
    }
}
