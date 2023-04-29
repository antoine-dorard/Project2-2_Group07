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
    HashMap<String, Object> rulesHM = new HashMap<>();

    public CFG(){
        readRulesFromFile();
    }

    public void readRulesFromFile(){
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
                    rulesHM.put(key, ruleList);
                }
                // if there is only one element in the value, add directly to HM
                else
                    rulesHM.put(key, rules.get(key));
            }

            ruleReader.close();
        } catch (URISyntaxException | IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public HashMap<String, Object> getRulesHM() {
        return rulesHM;
    }

    //    public void addRuleToHM(String line){
//        // splitting left-hand and right-hand side
//        String lhs = "", rhs = "";
//        for (int currIndex = 0; currIndex < line.length(); currIndex++){
//            // first '>' indicates the end of the LHS
//            if(line.charAt(currIndex) == '>'){
//                lhs = line.substring(0,currIndex+1);
//                // the rest of the string are the values (RHS):
//                rhs = line.substring(currIndex+1);
//                break;
//            }
//        }
//
//        // splitting the multiple options on the right-hand side
//        ArrayList<String> rhsList = new ArrayList<>();
//        int indexLastOr = 0;
//        for (int currIndex = 0; currIndex < rhs.length(); currIndex++){
//            // if the RHS has multiple options, add left element. ie. if a|b, add a
//            if(rhs.charAt(currIndex) == '|') {
//                rhsList.add(rhs.substring(indexLastOr + 1, currIndex - 1));
//                indexLastOr = currIndex;
//            }
//        }
//        // add last element, ie. add b, or if there is only 1 element on LHS, add this
//        rhsList.add(rhs.substring(indexLastOr + 1));
//
//        // add this rule to the hashmap
//        updateHM(lhs, rhsList);
//    }
//
//    public void updateHM(String lhs, ArrayList<String> rhs){
//        rulesHM.put(lhs, rhs);
//    }
//
//    public void readActionsFromFile(){
//        try {
//            cfgActionsFile = new File(getClass().getResource("/CFG/actions.json").toURI());
//            actionsReader = new BufferedReader(new FileReader(cfgActionsFile));
//
//            String nextLine = actionsReader.readLine();
//            while (nextLine != null){
//                splitVariablesAndAnswers(nextLine);
//                nextLine = ruleReader.readLine();
//            }
//
//        } catch (URISyntaxException | IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void splitVariablesAndAnswers(String line){
//        String skill, variables, answer;
//
//        for (int index = 0; index < line.length(); index++){
//            // find the skill
//            if (line.charAt(index) == '>'){
//                skill = line.substring(1, index - 2);
//                break;
//            }
//        }
//        // find the skill
//
//        // find the answer
//        for (int index = line.length() - 1; index >= 0; index--) {
//            if(line.charAt(index) == '>') {
//                answer = line.substring(index + 1);
//                break;
//            }
//        }
//
//    }
}
