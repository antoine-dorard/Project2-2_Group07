package backend.CFGParser.datastructures;

import java.util.ArrayList;
import java.util.Objects;

public class RHS {

    private ArrayList<NonTerminal[]> pairs = new ArrayList<>();
    private ArrayList<Terminal> terminal;

    private boolean isTerminal = false;

    public RHS(NonTerminal left, NonTerminal right){
        this.pairs.add(new NonTerminal[]{left, right});
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


    public ArrayList<NonTerminal[]> getPairs(){
        return pairs;
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
            if(pairs.size() != rhs.pairs.size()){
                return false;
            }

            for (int i = 0; i < pairs.size(); i++) {
                if(pairs.get(i)[0] != rhs.pairs.get(i)[0]){
                    return false;
                }
                if(pairs.get(i)[1] != rhs.pairs.get(i)[1]){
                    return false;
                }
            }
            return true;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(pairs, terminal, isTerminal);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for(int i = 0; i < pairs.size(); i++){
            sb.append(pairs.get(i)[0].toString());
            sb.append(", ");
            sb.append(pairs.get(i)[1].toString());
            if(i != pairs.size() - 1){
                sb.append(" | ");
            }
        }
        sb.append("]");

        return isTerminal ? terminal.toString() : sb.toString();
    }
}
