package main;

import dialogs.TestDialog;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
    }

    private void loadSkills(){

        // initialize default values.
        skills = new String[] {
                "SCHEDULE",
                "LOCATION"
        };
        // READ FROM FILE HERE!!
    }


    public void setRules(String[] keys, String[] values) {
        // clear whole rules HashMap.
        rules.clear();

        // populate new rules HashMap.
        for(int i = 0; i < keys.length; i++){
            rules.put(keys[i], values[i].split(" \\| "));
        }

    }

    private void loadRules(){

        // initialize default values.
        rules.put("S"       , new String[]{"<ACTION>"});
        rules.put("ACTION"  , skills);
        rules.put("SCHEDULE", new String[]{
                    "Which lectures are there <TIMEEXPRESSION>",
                    "<TIMEEXPRESSION>, which lectures are there"
        });
        rules.put("TIMEEXPRESSION", new String[]{
                "on <DAY> at <TIME>",
                "at <TIME> at <DAY>"
        });
        rules.put("TIME", new String[]{"9", "12"});
        rules.put("LOCATION", new String[]{
                "Where is <ROOM>",
                "How do <PRO> get to <ROOM>",
                "Where is <ROOM> located"
        });
        rules.put("PRO", new String[]{"I","you","he","she"});
        rules.put("ROOM", new String[]{"DeepSpace","SpaceBox"});
        rules.put("DAY", new String[]{
                "Monday","Tuesday","Wednesday","Thursday",
                "Friday","Saturday","Sunday"
        });

        // READ FROM FILE HERE!!

    }


    public void setDefaultAnswer(String value) {
        defaultAnswer = value;
    }

    public void setActions(String skill, String[][] newActions) {
        actions.put(skill, newActions);
    }

    private void loadActions() {

        // initialize default values.
        defaultAnswer = "I have no idea.";

        // loop through skills.
        for(String skill : skills) {

            String[] actIds = getActionIDs(skill);
            //System.out.println("RESULT : <" + skill + "> " + Arrays.toString(actIds));
            actionIDs.put(skill, actIds);

            if(skill.equals("SCHEDULE")) {
                String[][] acts = new String[][]{
                        {"Saturday", "*", "There are no lectures on Saturday"},
                        {"Sunday", "*", "There are no lectures on Sunday"}
                };
                actions.put(skill, acts);
            } else {
                if(skill.equals("LOCATION")) {
                    String[][] acts = new String[][]{
                            {"DeepSpace", "*", "Somewhere upstairs"},
                            {"SpaceBox", "*", "Somewhere downstairs"}
                    };
                    actions.put(skill, acts);
                } else {
                    actions.put(skill, new String[][]{});
                }
            }
        }


        // READ FROM FILE HERE!!
    }


    private String[] getActionIDs(String text){

        ArrayList<String> actionIDs = new ArrayList<>();
        if(rules.containsKey(text)) {
            String[] actions = rules.get(text);

            for (String action : actions) {
                // find IDs between '<' and '>'.
                String[] foundIDs = findIDs(action);
                // if this action has no IDs, end the loop.
                if (foundIDs.length == 0) {
                    actionIDs.add(text);
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
