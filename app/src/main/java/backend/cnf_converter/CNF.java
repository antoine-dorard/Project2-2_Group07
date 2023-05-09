package backend.cnf_converter;

import backend.CFG.CFG;

import java.util.*;

public class CNF {

    private CFG cfg;
    private ArrayList<CNFRule> cnf = new ArrayList<>();

    /*
    -- A1 -> NonTerminal NonTerminal | NonTerminal NonTerminal
    -- B1 -> Terminal | Terminal | Terminal

    -- A1 : NonTerminal NonTerminal
    -- A1 : NonTerminal NonTerminal
    -- A1 : NonTerminal NonTerminal
    -- B1 -> Terminal
    -- B1 -> Terminal
    -- B1 -> Terminal
     */

    public CNF(CFG cfg) {
        this.cfg = cfg;
    }

    public ArrayList<CNFRule> getCNFRules(){
        return null;
    }

    public RHS getRHS(NonTerminal parent){
        for (int i = 0; i < cnf.size(); i++) {
            if (cnf.get(i).getLHS().equals(parent)) {
                return cnf.get(i).getRHS();
            }
        }
        return null;
    }

    public NonTerminal findLHS(RHS rhs){
        for (int i = 0; i < cnf.size(); i++) {
            if (cnf.get(i).getRHS().equals(rhs)) {
                return cnf.get(i).getLHS();
            }
        }
        return null;
    }

    public void generateCNF() {
        //TODO generate CNF

        // Step 1: Copy  all the CFG rules that only have terminals on the RHS to the CNF
        for (Map.Entry<String, ArrayList<ArrayList<GrammarVariable>>> cfgRule: cfg.getCfgRules().entrySet()){
            ArrayList<Terminal> currentTerminals = new ArrayList<>();
            a:
            for(int i = 0; i < cfgRule.getValue().size(); i++){
                for(int j = 0; j < cfgRule.getValue().get(i).size(); j++){
                    if(cfgRule.getValue().get(i).get(j) instanceof NonTerminal){
                        break a;
                    }
                    currentTerminals.add((Terminal) cfgRule.getValue().get(i).get(0));
                }
            }
            // If none of the RHS elements were NonTerminals, add it in the cnf:
            cnf.add(new CNFRule(new NonTerminal(), new RHS(currentTerminals.toArray(new Terminal[0])), cfgRule.getKey()));

        }

        // Step 2: transform all the Terminals to NonTerminals where the RHS contains more than 2 elements
        for (Map.Entry<String, ArrayList<ArrayList<GrammarVariable>>> cfgRule: cfg.getCfgRules().entrySet()){

            for(int i = 0; i < cfgRule.getValue().size(); i++){
                if(cfgRule.getValue().get(i).size() > 1){
                    for(int j = 0; j < cfgRule.getValue().get(i).size(); j++){
                        if(cfgRule.getValue().get(i).get(j) instanceof Terminal){
                            NonTerminal newNonTerminal = findLHS(new RHS((Terminal) cfgRule.getValue().get(i).get(j)));
                            if(newNonTerminal == null){
                                newNonTerminal = new NonTerminal();
                                CNFRule newRule = new CNFRule(newNonTerminal, new RHS((Terminal) cfgRule.getValue().get(i).get(j)));
                                cnf.add(newRule);
                            }
                            cfgRule.getValue().get(i).set(j, newNonTerminal);
                        }
                    }
                }
            }
        }

        // Step 3: create pairs
        for (Map.Entry<String, ArrayList<ArrayList<GrammarVariable>>> cfgRule: cfg.getCfgRules().entrySet()){

            for(int i = 0; i < cfgRule.getValue().size(); i++){
                if(cfgRule.getValue().get(i).size() > 2 ){

                    int size = cfgRule.getValue().get(i).size();
                    for(int j = 0; j < size - 2; j++){
                        NonTerminal nonTerminal1 = (NonTerminal) cfgRule.getValue().get(i).get(0);
                        NonTerminal nonTerminal2 = (NonTerminal) cfgRule.getValue().get(i).get(1);
                        NonTerminal newNonTerminal = new NonTerminal();
                        CNFRule newRule = new CNFRule(newNonTerminal, new RHS(nonTerminal1, nonTerminal2));
                        cnf.add(newRule);
                        cfgRule.getValue().get(i).remove(0);
                        cfgRule.getValue().get(i).set(0, newNonTerminal);
                    }
                }
            }
        }

    }


    public static void main(String[] args) {
        CNF cnf = new CNF(new CFG());
        cnf.generateCNF();
        for(int i = 0; i < cnf.cnf.size(); i++){
            System.out.println(cnf.cnf.get(i));
        }

    }
}
