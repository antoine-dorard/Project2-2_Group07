package backend.CFG;

import java.util.HashMap;

public class CFGAction {
    private String skill;
    private HashMap<String, String> slotValuePair;
    private String answer;

    public CFGAction(String skill, HashMap<String, String> slotValuePair, String answer){
        this.skill = skill;
        this.slotValuePair = slotValuePair;
        this.answer = answer;
    }

    // getters
    public String getSkill(){
        return skill;
    }
    public HashMap<String, String> getSlotValuePair() { return slotValuePair; }
    public String getAnswer() { return answer; }
}
