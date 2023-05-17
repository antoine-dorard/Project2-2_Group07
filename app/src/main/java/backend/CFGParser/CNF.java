package backend.CFGParser;

import backend.CFGParser.datastructures.*;

import java.util.*;

public class CNF {

    private CFG cfg;
    private ArrayList<CNFRule> cnf = new ArrayList<>();

    private HashMap<String, ArrayList<NonTerminal>> actionRule = new HashMap<>();

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

    public ArrayList<NonTerminal> getNonTerminals(Terminal terminal){
        ArrayList<NonTerminal> nonTerminals = new ArrayList<>();
        for (int i = 0; i < cnf.size(); i++) {
            if (cnf.get(i).getRHS().isTerminal() && cnf.get(i).getRHS().getTerminals().contains(terminal)) {
                nonTerminals.add(cnf.get(i).getLHS());
            }
        }
        return nonTerminals;
    }

    public ArrayList<CNFRule> getBinaryProductions(){
        ArrayList<CNFRule> binaryProductions = new ArrayList<>();
        for (int i = 0; i < cnf.size(); i++) {
            if (!cnf.get(i).getRHS().isTerminal()) {
                binaryProductions.add(cnf.get(i));
            }
        }
        return binaryProductions;
    }

    public void generateCNF() {
        //TODO generate CNF

        this.printCNF();

        // Step 1: Copy  all the CFG rules that only have terminals on the RHS to the CNF
        for (Map.Entry<String, ArrayList<ArrayList<GrammarVariable>>> cfgRule: cfg.getCfgRules().entrySet()){
            ArrayList<Terminal> currentTerminals = new ArrayList<>();
            boolean containsNonTerminal = false;

            for(int i = 0; i < cfgRule.getValue().size(); i++){
                for(int j = 0; j < cfgRule.getValue().get(i).size(); j++){
                    if(cfgRule.getValue().get(i).get(j) instanceof NonTerminal){
                        containsNonTerminal = true;
                    }
                }
                if(!containsNonTerminal) currentTerminals.add((Terminal) cfgRule.getValue().get(i).get(0));
            }


            // If none of the RHS elements were NonTerminals, add it in the cnf:
            if(!containsNonTerminal) cnf.add(new CNFRule(new NonTerminal(cfgRule.getKey()), new RHS(currentTerminals.toArray(new Terminal[0])), cfgRule.getKey()));

        }
        System.out.println("After step 1: ");
        this.printCNF();

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
                            else{
                                System.out.println("Found existing non terminal: " + newNonTerminal);
                            }
                            cfgRule.getValue().get(i).set(j, newNonTerminal);
                        }
                    }
                }
            }
        }

        System.out.println();
        System.out.println("After step 2: ");
        this.printCNF();

        // Step 3: create pairs
        for (Map.Entry<String, ArrayList<ArrayList<GrammarVariable>>> cfgRule: cfg.getCfgRules().entrySet()){

            for(int i = 0; i < cfgRule.getValue().size(); i++){
                if(cfgRule.getValue().get(i).size() > 2){

                    int size = cfgRule.getValue().get(i).size();
                    for(int j = 0; j < size - 1; j++){
                        NonTerminal nonTerminal1 = (NonTerminal) cfgRule.getValue().get(i).get(0);
                        NonTerminal nonTerminal2 = (NonTerminal) cfgRule.getValue().get(i).get(1);
                        NonTerminal newNonTerminal = new NonTerminal();
                        if(j == size - 2){
                            newNonTerminal = new NonTerminal(cfgRule.getKey());
                        }

                        CNFRule newRule = new CNFRule(newNonTerminal, new RHS(nonTerminal1, nonTerminal2));
                        cnf.add(newRule);
                        cfgRule.getValue().get(i).remove(0);
                        cfgRule.getValue().get(i).set(0, newNonTerminal);
                    }
                }
                else if(cfgRule.getValue().get(i).size() == 2){
                    NonTerminal nonTerminal1 = (NonTerminal) cfgRule.getValue().get(i).get(0);
                    NonTerminal nonTerminal2 = (NonTerminal) cfgRule.getValue().get(i).get(1);
                    //NonTerminal newNonTerminal = new NonTerminal();
                    System.out.println("KEY:");
                    System.out.println(cfgRule.getKey());
                    CNFRule newRule = new CNFRule(new NonTerminal(cfgRule.getKey()), new RHS(nonTerminal1, nonTerminal2));
                    cnf.add(newRule);
                }
            }
        }
        System.out.println();
        System.out.println("After step 3: ");
        this.printCNF();

    }

    private void printCNF(){
        for(int i = 0; i < cnf.size(); i++){
            System.out.println(cnf.get(i));
        }
    }

    public static void main(String[] args) {
        CNF cnf = new CNF(new CFG());
        cnf.generateCNF();

    }
}
