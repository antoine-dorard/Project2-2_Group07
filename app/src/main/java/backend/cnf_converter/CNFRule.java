package backend.cnf_converter;

import java.util.ArrayList;
import java.util.Arrays;

public class CNFRule {
    NonTerminal LHS;

    ArrayList<NonTerminal[]> pairs = new ArrayList<>();
    ArrayList<Terminal> terminal;

    boolean isTerminal = false;

    public CNFRule(NonTerminal LHS, ArrayList<NonTerminal[]> pairs){
        this.LHS = LHS;
        this.pairs = pairs;
    }

    public CNFRule(NonTerminal LHS, Terminal... terminals){
        this.LHS = LHS;
        this.terminal.addAll(Arrays.asList(terminals));
        System.out.println("Terminals: " + terminals);
        isTerminal = true;
    }

    public NonTerminal getLHS(){
        return LHS;
    }

    public ArrayList<NonTerminal[]> getPairs(){
        return pairs;
    }

    public ArrayList<Terminal> getTerminal(){
        return terminal;
    }

    public boolean isTerminal(){
        return isTerminal;
    }

}
