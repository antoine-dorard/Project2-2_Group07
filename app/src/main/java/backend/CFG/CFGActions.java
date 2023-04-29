package backend.CFG;

import java.util.HashMap;

public class CFGActions {
    String skill;
    HashMap<String, String> slotValuePair;
    String answer;

    public CFGActions(String skill, HashMap<String, String> slotValuePair, String answer){
        this.skill = skill;
        this.slotValuePair = slotValuePair;
        this.answer = answer;
    }
    public String getSkill(){
        return skill;
    }
    public String getDefault(){
        return "I have no idea";
    }
}
