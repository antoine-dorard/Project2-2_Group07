package backend.CFGParser.datastructures;

import java.util.ArrayList;
import java.util.Objects;

public class RHS {

    private NonTerminal[] pair;
    private ArrayList<Terminal> terminal;

    private boolean isTerminal = false;

    public RHS(NonTerminal left, NonTerminal right){
        this.pair = new NonTerminal[]{left, right};
    }

    public RHS(Terminal... terminals){
        isTerminal = true;
        terminal = new ArrayList<>();
        for(Terminal terminal1 : terminals){
            terminal.add(terminal1);
        }
    }
    public boolean isTerminal(){
        return isTerminal;
    }


    public NonTerminal[] getPair(){
        return pair;
    }

    public ArrayList<Terminal> getTerminals(){
        return terminal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RHS rhs = (RHS) o;

        if(isTerminal != rhs.isTerminal){
            return false;
        }

        if(isTerminal){
            if(terminal.size() != rhs.terminal.size()){
                return false;
            }

            for(int i = 0; i < terminal.size(); i++){
                if(!terminal.contains(rhs.terminal.get(i))){
                    return false;
                }
                if(!rhs.getTerminals().contains(terminal.get(i))){
                    return false;
                }
            }
            return true;
        }
        else{
            if(pair[0] != rhs.pair[0]){
                return false;
            }
            if(pair[1] != rhs.pair[1]){
                return false;
            }
            return true;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(pair, terminal, isTerminal);
    }

    @Override
    public String toString() {
        return isTerminal ?
                terminal.toString() :
                "(" + pair[0].toString() + ", " + pair[1].toString() + ")";
    }
}
