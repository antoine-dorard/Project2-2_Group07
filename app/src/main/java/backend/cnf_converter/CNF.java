package backend.cnf_converter;

import java.util.ArrayList;
import java.util.List;

public class CNF {

    ArrayList<CNFRule> cnf = new ArrayList<>();

    /*
    NonTerminal -> NonTerminal NonTerminal | NonTerminal NonTerminal
    NonTerminal -> Terminal | Terminal | Terminal
     */

    /**
     * Returns null if the terminal does not exist.
     * Returns a list of possible parents otherwise
     */
    public List<Integer> getLHSs(Terminal terminal){
        return null;
    }
    /**
     * Returns null if the terminal does not exist.
     * Returns a list of possible parents otherwise
     */
    public List<Integer> getLHSs(NonTerminal left, NonTerminal right){
        return null;
    }


    public RHS getRHS(Integer parent){
        //return cnf.get(parent).;
        return null;
    }

    public void generateCNF() {
        //TODO generate CNF

        for(int i = 0; i < cnf.size(); i++){
            cnf.get(i).getLHS();
        }
    }


    public static void main(String[] args) {
        NonTerminal nonTerminal = new NonTerminal();
        Terminal t1 = new Terminal(nonTerminal);
        Terminal t2 = new Terminal(nonTerminal);
        Terminal t3 = new Terminal(nonTerminal);
        System.out.println(t1);
        System.out.println("-----");
        new CNFRule(nonTerminal, t1, t2, t3);
    }
}
