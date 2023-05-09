package backend.cnf_converter;

public class NonTerminal extends GrammarVariable {


    private String nonTerminal;

    public NonTerminal(){
    }

    public NonTerminal(String nonTerminal){
        this.nonTerminal = nonTerminal;
    }

    @Override
    public String toString() {
        return nonTerminal != null ? nonTerminal : Integer.toHexString(this.hashCode());
    }
}
