package backend.cnf_converter;

/**
 * A Terminal is a Grammar Variable that represents a word or multiple words that don't contain a placeholder.
 */
public class Terminal extends GrammarVariable {

    private String terminal;

    public Terminal(){

    }

    public Terminal(String terminal){
        this.terminal = terminal;
    }

    @Override
    public String toString() {
        return terminal;
    }
}
