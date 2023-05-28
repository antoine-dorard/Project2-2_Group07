package backend.CFGParser;

import backend.CFGParser.datastructures.CFGAction;
import backend.CFGParser.datastructures.GrammarVariable;
import backend.CFGParser.datastructures.NonTerminal;
import backend.CFGParser.datastructures.Terminal;
import main.App;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Class that reads the CFG rules and actions from a json file and stores them in a HashMap.
 * Call readCFG() to read the rules and actions from the json file.
 */
public class CFG {
    private final HashMap<String, ArrayList<ArrayList<GrammarVariable>> > cfgRulesHM = new HashMap<>();
    private final ArrayList<CFGAction> cfgActions = new ArrayList<>();
    private HashMap<String, String> defaults = new HashMap<>();

    private String defaultAnswer;

    public CFG(){
        readRules();
        newReadActions();
    }

    public CFG(boolean read){
        if (read) {
            readRules();
            newReadActions();
        }
    }

    public CFG readCFG(){
        readRules();
        newReadActions();
        return this;
    }

    /**
     * Method that reads CFG rules from a json file and stores them in a Hashmap as combinations
     * of terminals and non-terminals.
     */
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
                ArrayList<ArrayList<GrammarVariable>> convertedRules = new ArrayList<>();
                if (rules.get(key) instanceof JSONArray) {
                    JSONArray arr = (JSONArray) rules.get(key);
                    Iterator<String> values = arr.iterator();
                    // while there are still values left in the array, convert them to term and nonTerm
                    while (values.hasNext())
                        convertedRules.add(splitRule(values.next()));
                    cfgRulesHM.put(key, convertedRules);
                }
                else {
                    convertedRules.add(splitRule((String) rules.get(key)));
                    cfgRulesHM.put(key, convertedRules);
                }
            }

            ruleReader.close();
        } catch (URISyntaxException | IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method that splits a rule into (non-)terminals
     * Assumes that non-terminals are surrounded by brackets (< >)
     * @param rule rule string to be converted into terminals and non-terminals
     * @return ArrayList of GrammarVariables representing the rule
     */
    public ArrayList<GrammarVariable> splitRule(String rule) {
        ArrayList<GrammarVariable> list = new ArrayList<>();
        int beginIndex = 0;
        for (int i = 0; i < rule.length(); i++) {
            if (rule.charAt(i) == '<'){
                if(i != beginIndex){
                    String[] terminalStrings = rule.substring(beginIndex, i-1).trim().split(" ");
                    for (String term : terminalStrings) {
                        if (!term.isEmpty()) {
                            list.add(new Terminal(term.toLowerCase()));
                        }
                    }
                    beginIndex = i;
                    i--;
                } else {
                    while (rule.charAt(i) != '>')
                        i++;
                    list.add(new NonTerminal(rule.substring(beginIndex+1, i)));
                    beginIndex = i+2;
                }
            } else if (i==rule.length()-1 && rule.charAt(rule.length()-1)!='>'){
                String[] terminalStrings = rule.substring(beginIndex).trim().split(" ");
                for (String term : terminalStrings) {
                    if (!term.isEmpty()) {
                        list.add(new Terminal(term.toLowerCase()));
                    }
                }
            }
        }
        return list;
    }

    public void newReadActions(){
        JSONParser parser = new JSONParser();

        try {
            File cfgActionsFile = new File(App.resourcesPath + "CFG/new_actions.json");
            Reader actionReader = new FileReader(cfgActionsFile);

            JSONObject jsonObject = (JSONObject) parser.parse(actionReader);

            jsonObject.keySet().forEach(skillKey -> {
                JSONObject skillObject = (JSONObject) jsonObject.get(skillKey);
                JSONArray ids = (JSONArray) skillObject.get("ids");
                JSONArray actions = (JSONArray) skillObject.get("actions");

                actions.forEach(action -> {
                    JSONArray actionArray = (JSONArray) action;
                    HashMap<String, String> slotValuePair = new HashMap<>();
                    for (int i = 0; i < ids.size(); i++) {
                        String value = (String) actionArray.get(i);
                        if (!"*".equals(value)) {
                            slotValuePair.put((String) ids.get(i), value);
                        }
                    }
                    String answer = (String) actionArray.get(actionArray.size() - 1);
                    cfgActions.add(new CFGAction((String) skillKey, slotValuePair, answer));
                });
            });
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

    }

    /**
     * Method that reads CFG actions from a json file and stores them in an ArrayList of CFGAction instances
     * @deprecated use readNewActions instead
     */
    @Deprecated
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

    // based on the traverse method of phase 1
    /**
     * Recursive method that traverses skills and creates CFGAction instances for each action
     * @param obj a JSONObject for actions with slot-value pairs, or a String in the default case
     * @param skillName the name of the skill that the action(s) belong to
     * @param HM HashMap to store the slot-value pairs, used when recursion occurs
     */
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

                if(key.equals("default") ){
                    slotValuePair.put("default", null);
                    defaults.put(skillName, (String) jsonObj.get(key));
                }else{
                    String[] keySplit = ((String) key).split("\\s+");
                    slotValuePair.put(keySplit[0], keySplit[1].toLowerCase()); // Lower case for consistency (all the terminals are put to lower case
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
            defaultAnswer = (String) obj;
        } else {
            //System.out.println(obj.toString());
            //TODO maybe going to use it later
        }
    }

    // getters
    public ArrayList<CFGAction> getCfgActions() { return cfgActions; }

    /**
     * Accessor method for CFG rules
     * @return HashMap with rules stored as combinations of terminals (T) and non-terminals (NT)
     * e.g.             "LOCATION": ["Where is <ROOM>", "How do <PRO> get to <ROOM>"]
     * is stored as     key: LOCATION -> value: [ [T(Where is), NT(ROOM)] , [T(How do), NT(PRO), T(get to), NT(ROOM)] ]
     */
    public HashMap<String, ArrayList<ArrayList<GrammarVariable>>> getCfgRules(){ return cfgRulesHM; }

    //TODO: the way we add rules and actions might change depending on our skill editor
    public void addRule(String ruleLHS, ArrayList<String> rules){
        ArrayList<ArrayList<GrammarVariable>> ruleList = new ArrayList<>();
        for (String rule : rules) ruleList.add(splitRule(rule));
        cfgRulesHM.put(ruleLHS, ruleList);
    }

    public void addAction(String skillName, String answer, HashMap<String, String> slotValuePairs){
        cfgActions.add(new CFGAction(skillName, slotValuePairs, answer));
    }

    public HashMap<String, String> getDefaults() {
        return defaults;
    }

    public static void main(String[] args) {
        //CFG cfg = new CFG();
        //System.out.println(cfg.cfgRulesHM);
        //System.out.println(cfg.cfgActions);

        String test = "The room <ROOM> is in the first floor";
        if(test.contains("<ROOM>")){
            test = test.replace("<ROOM>", "DeepSpace");
        }

        System.out.println(test);
    }
}
