package backend.cnf_converter;

import java.util.ArrayList;
import java.util.List;

public class CNF {

    ArrayList<RHS> cnf = new ArrayList<>();

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
        return cnf.get(parent);
    }

    public void generateCNF() {

    }
}
