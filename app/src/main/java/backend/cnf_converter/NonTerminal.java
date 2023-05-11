package backend.cnf_converter;

import java.util.Objects;

public class NonTerminal extends GrammarVariable {


    private String nonTerminal;

    public NonTerminal(){
    }

    public NonTerminal(String nonTerminal){
        this.nonTerminal = nonTerminal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NonTerminal that = (NonTerminal) o;

        if(that.nonTerminal == null && this.nonTerminal == null){
            return false;
        }

        if(that.nonTerminal == null && this.nonTerminal != null){
            return false;
        }
        if(that.nonTerminal != null && this.nonTerminal == null){
            return false;
        }

        if(that.nonTerminal.equals(this.nonTerminal)){
            return true;
        }

        return false;
    }

    @Override
    public String toString() {
        return nonTerminal != null ? nonTerminal : Integer.toHexString(this.hashCode());
    }
}
