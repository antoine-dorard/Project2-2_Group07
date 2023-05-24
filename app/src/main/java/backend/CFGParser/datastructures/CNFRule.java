package backend.CFGParser.datastructures;

import java.util.ArrayList;

public class CNFRule {
    private NonTerminal lhs;
    private String CFGName;
    private RHS rhs;

    public CNFRule(NonTerminal LHS, RHS rhs){
        this.lhs = LHS;
        this.rhs = rhs;
    }
    public CNFRule(NonTerminal LHS, RHS rhs, String CFGName){
        this.lhs = LHS;
        this.rhs = rhs;
        this.CFGName = CFGName;
    }

    public NonTerminal getLHS(){
        return lhs;
    }

    public RHS getRHS() {
        return rhs;
    }

    public NonTerminal[] getPair(){
        return rhs.getPair();
    }

    public ArrayList<Terminal> getTerminals(){
        return rhs.getTerminals();
    }

    @Override
    public String toString() {
        return "RULE: " + lhs + " -> " + rhs + " (CFG: " + CFGName + ")";
    }
}
