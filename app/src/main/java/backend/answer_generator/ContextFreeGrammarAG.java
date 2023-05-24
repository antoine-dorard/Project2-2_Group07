package backend.answer_generator;

import backend.CFGParser.CKYParser;
import backend.CFGParser.datastructures.CFGAction;
import backend.CFGParser.datastructures.NonTerminal;
import backend.CFGParser.datastructures.Terminal;
import main.SkillLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class ContextFreeGrammarAG implements AnswerGenerator{
    SkillLoader skillLoader;

    private CKYParser parser;

    public ContextFreeGrammarAG(SkillLoader skillLoader){
        this.skillLoader = skillLoader;
        parser = new CKYParser(skillLoader.getCnf());
    }

    @Override
    public String generateAnswer(String input) {
        String[] words = input.split(" ");
        Terminal[] terminals = new Terminal[words.length];

        for(int i = 0; i < words.length; i++){
            terminals[i] = new Terminal(words[i]);
        }
        parser.ckyParse(terminals);
        parser.printTable();

        ArrayList<CFGAction> actions = skillLoader.getCfg().getCfgActions();
        HashMap<String, String> placeholderValuePairs = new HashMap<>();
        for (Map.Entry<NonTerminal, Terminal> entry : parser.getPlaceholderValuePairs().entrySet()){
            placeholderValuePairs.put(entry.getKey().toString(), entry.getValue().toString());
        }

        System.out.println(placeholderValuePairs);

        int minDifference = Integer.MAX_VALUE;
        CFGAction bestAction = null;

        for (String skill : parser.getSkills()) {
            for (CFGAction action : actions) {
                if (action.getSkill().equals(skill)) {

                    int difference = placeholderValuePairs.size();
                    System.out.println(difference);
                    for (String key : action.getSlotValuePair().keySet()) {
                        String value = action.getSlotValuePair().get(key);
                        System.out.println("Slot: " + key + " Value: " + value);

                        if(placeholderValuePairs.containsKey(key)){
                            if(placeholderValuePairs.get(key).equals(value)){
                                difference--;
                            }
                        }
                    }

                    if(difference < minDifference){
                        System.out.println("New best action: " + action.getAnswer() + " with difference: " + difference);
                        minDifference = difference;
                        bestAction = action;
                    }
                }
            }
        }

        String answer;
        if(bestAction == null) {
            answer = "Sorry, I don't know the answer to that question.";
        }
        else{
            answer = bestAction.getAnswer();
        }

        if(minDifference == placeholderValuePairs.size()){
            for (Map.Entry<String, String> entry : placeholderValuePairs.entrySet()){
                System.out.println(entry.getKey() + " " + entry.getValue());
                answer = answer.replace("<"+entry.getKey()+">", entry.getValue());
            }
        }

        return answer;
    }
}
