package main;

import backend.CFGParser.CFG;
import backend.CFGParser.datastructures.CFGAction;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SkillData {


    public String[] skills;
    public ArrayList<String> ruleKeys = new ArrayList<>();
    public LinkedHashMap<String, String[]> rules = new LinkedHashMap<>();
    public String defaultAnswer;
    public HashMap<String, String[]> actionIDs = new HashMap<>();
    public HashMap<String, String[][]> actions = new HashMap<>();


    public SkillData() {
        revertAll();
    }

    public void revertAll() {
        loadSkills();
        loadRules();
        loadActions();
    }


    public void setSkills(String[] newSkills) {
        skills = newSkills;
        // update rules if 'ACTION' is a key.
        if (rules.containsKey("ACTION")) {
            rules.put("ACTION", skillsToRuleSkills(skills));
            // check if we need to add new skills to actions.
            for(String skill : skills) {
                if (!actions.containsKey(skill)) {
                    // add skill with empty String[][].
                    actions.put(skill, new String[][]{});
                }
            }
        }
        // skills are saved in rules.json, so save them there.
        saveRules();
    }

    public void loadSkills(){
        System.out.println("Loading skills...");
        JSONParser parser = new JSONParser();

        try {
            File cfgRulesFile = new File(App.resourcesPath + "CFG/rules.json");
            Reader ruleReader = new FileReader(cfgRulesFile);
            JSONObject rules = (JSONObject) parser.parse(ruleReader);


            Iterator keys = rules.keySet().iterator();

            while (keys.hasNext()){
                String key = (String) keys.next();
                if(key.equals("ACTION")) {
                    skills = convertJsonArrToJavaArr((JSONArray) rules.get(key));
                    System.out.println(Arrays.toString(skills));
                    break;
                }
            }

            ruleReader.close();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        // Example skills:
//        skills = new String[] {
//                "SCHEDULE",
//                "LOCATION"
//        };
    }

    // "<LOCATION>", "<SCHEDULE>" -> String[]{"LOCATION", "SCHEDULE"}
    private String[] convertJsonArrToJavaArr(JSONArray jsonArr){
        String[] javaArr = new String[jsonArr.size()];
        for(int i = 0; i < jsonArr.size(); i++){
            String currentString = (String) jsonArr.get(i);
            if(currentString.charAt(0) == '<' && currentString.charAt(currentString.length() - 1) == '>' ){
                currentString = ((String) jsonArr.get(i)).substring(1, ((String) jsonArr.get(i)).length()-1);
            }
            javaArr[i] = currentString;
        }
        return javaArr;
    }
    public void setRules(String[] keys, String[] values) {
        // clear whole rules HashMap.
        rules.clear();

        // populate new rules HashMap.
        for(int i = 0; i < keys.length; i++){
            rules.put(keys[i], values[i].split(" \\| "));
        }
        // save to file.
        saveRules();
    }

    public void loadRules(){
        loadSkills();

        JSONParser parser = new JSONParser();

        try {
            File cfgRulesFile = new File(App.resourcesPath + "CFG/rules.json");
            Reader ruleReader = new FileReader(cfgRulesFile);
            JSONObject rulesJson = (JSONObject) parser.parse(ruleReader);


            Iterator keys = rulesJson.keySet().iterator();

            System.out.println("Reading from file:");
            while (keys.hasNext()){
                String key = (String) keys.next();
                if(key.equals("ACTION")) {
                    rules.put(key, skillsToRuleSkills(skills));
                }
                else{
                    if (rulesJson.get(key) instanceof String){
                        rules.put(key, new String[]{(String) rulesJson.get(key)});
                    }
                    else if(rulesJson.get(key) instanceof JSONArray){
                        JSONArray jsonArr = (JSONArray) rulesJson.get(key);
                        String[] javaArr = new String[jsonArr.size()];
                        for(int i = 0; i < jsonArr.size(); i++){
                            System.out.print(jsonArr.get(i));
                            System.out.print(" | ");
                            javaArr[i] = (String) jsonArr.get(i);
                        }
                        System.out.println();
                        rules.put(key, javaArr);
                    }
                    else{
                        System.out.println("ERROR: " + key + " is not a String or JSONArray");
                    }
                }
            }

            ruleReader.close();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        // Example rule:
//        rules.put("LOCATION", new String[]{
//                "Where is <ROOM>",
//                "How do <PRO> get to <ROOM>",
//                "Where is <ROOM> located"
//        });
    }

    private void saveRules() {
        JSONObject jsonObject = new JSONObject();

        for (String key : rules.keySet()) {
            String[] value = rules.get(key);
            if (value.length == 1 && key.equals("S")) {
                jsonObject.put(key, value[0]);
            } else {
                JSONArray jsonArray = new JSONArray();
                for (String s : value) {
                    jsonArray.add(s);
                }
                jsonObject.put(key, jsonArray);
            }
        }

        try (FileWriter file = new FileWriter(App.resourcesPath + "CFG/rules.json")) {
            file.write(jsonObject.toString());
            System.out.println("Successfully Saved Rules to File...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String[] skillsToRuleSkills(String[] skills) {
        String[] newSkills = new String[skills.length];
        for(int i = 0; i < skills.length; i++) {
            newSkills[i] = "<" + skills[i] + ">";
        }
        return newSkills;
    }

    public void setDefaultAnswer(String value) {
        // TODO Write the general default.
        defaultAnswer = value;

        // SAVE TO FILE HERE!
    }

    public void setActions(String skill, String[][] newActions) {
        System.out.println("Saving actions for " + skill);

        actions.put(skill, newActions);

        JSONParser parser = new JSONParser();

        try {
            File cfgActionsFile = new File(App.resourcesPath + "CFG/new_actions.json");
            Reader actionReader = new FileReader(cfgActionsFile);

            JSONObject jsonObject = (JSONObject) parser.parse(actionReader);
            JSONObject objectToUpdate = (JSONObject) jsonObject.get(skill);
            if(objectToUpdate == null){
                objectToUpdate = new JSONObject();
            }

            JSONArray ids = new JSONArray();
            JSONArray actions = new JSONArray();

            // First update the ids:
            ids.addAll(Arrays.asList(actionIDs.get(skill)));

            // Then update the actions:
            for (String[] action : newActions) {
                JSONArray jsonAction = new JSONArray();

                for (String s : action) {
                    jsonAction.add(s);
                }
                actions.add(jsonAction);
            }

            // update the object:
            objectToUpdate.put("ids", ids);
            objectToUpdate.put("actions", actions);

            // put the updated arrays into the main object back:
            jsonObject.put(skill, objectToUpdate);

            try (FileWriter file = new FileWriter(App.resourcesPath + "CFG/new_actions.json")) {
                file.write(jsonObject.toString());
                System.out.println("Successfully Saved Actions to File...");
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (ParseException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadActions() {
        loadSkills();

        // TODO Read from file here.
        CFG cfg = new CFG(false);
        //cfg.readActions();
        cfg.newReadActions();
        ArrayList<CFGAction> cfgActions = cfg.getCfgActions();

        for (String skill : skills) {
            actionIDs.put(skill, getActionIDs(skill));

            ArrayList<String[]> tempAction = new ArrayList<>();
            // adding each action of the current skill
            // e.g. tempAction[0] = {"Saturday", "*", "There are no lectures on Saturday"}
            for (CFGAction cfgAction : cfgActions) {
                if (cfgAction.getSkill().equals(skill)) {

                    if(cfgAction.getSlotValuePair().containsKey("default")){
                        String[] starList = new String[actionIDs.get(skill).length + 1];
                        Arrays.fill(starList, "*");
                        starList[starList.length - 1] = cfgAction.getAnswer();
                        tempAction.add(starList);
                    }else {
                        String[] tempActionArr = new String[actionIDs.get(skill).length + 1]; // + 1 for the answer at the end
                        int i = 0;
                        for (String placeholderName : actionIDs.get(skill)) {
                            if(!cfgAction.getSlotValuePair().containsKey(placeholderName) || cfgAction.getSlotValuePair().get(placeholderName) == null) {
                                tempActionArr[i] = "*";
                            } else {
                                tempActionArr[i] = cfgAction.getSlotValuePair().get(placeholderName);
                            }
                            i++;
                        }
                        tempActionArr[i] = cfgAction.getAnswer();
                        tempAction.add(tempActionArr);
                    }
                }
            }

            actions.put(skill, tempAction.toArray(new String[tempAction.size()][]) );

        }

        // initialize default values.
        defaultAnswer = "I have no idea.";

        // Example actions (for one specific skill):
//        String[] actIds = getActionIDs(skill);
//        actionIDs.put(skill, actIds);
//
//        String[][] acts = new String[][]{
//                {"DeepSpace", "*", "Somewhere upstairs"},
//                {"SpaceBox", "*", "Somewhere downstairs"}
//        };
//        actions.put(skill, acts);
    }


    private String[] getActionIDs(String skill){

        ArrayList<String> actionIDs = new ArrayList<>();
        if(rules.containsKey(skill)) {
            String[] actions = rules.get(skill);

            for (String action : actions) {
                // find IDs between '<' and '>'.
                String[] foundIDs = findIDs(action);
                // if this action has no IDs, end the loop.
                if (foundIDs.length == 0) {
                    actionIDs.add(skill);
                    break;
                } else {
                    for (String foundID : foundIDs) {
                        // if it has, recursion.
                        String[] newActions = getActionIDs(foundID);
                        for (String newAction : newActions) {
                            if (!actionIDs.contains(newAction)) {
                                actionIDs.add(newAction);
                            }
                        }
                    }
                }
            }
        }

        return actionIDs.toArray(new String[0]);
    }

    private String[] findIDs(String text){

        // define regular expression
        String regex = "<(.*?)>";

        // create pattern.
        Pattern pattern = Pattern.compile(regex);

        // create Matcher object and apply it.
        Matcher matcher = pattern.matcher(text);

        ArrayList<String> strings = new ArrayList<>();
        // build the strings ArrayList with the Matcher.
        while (matcher.find()) {
            strings.add(matcher.group(1));
        }
        // convert to String[].
        return strings.toArray(new String[0]);
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                SkillData example = new SkillData();
                System.out.println(example.actions.toString());
            }
        });
    }
}
