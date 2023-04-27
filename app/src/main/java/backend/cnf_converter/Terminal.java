package backend.cnf_converter;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A Terminal is a Grammar Variable that represents a word or multiple words that don't contain a placeholder.
 * An instance of this class can also contain multiple terminals.
 */
public class Terminal extends GrammarVariable {

    private final NonTerminal nonTerminalParent;

    private ArrayList<String> terminals = new ArrayList<>();

    public Terminal(NonTerminal parent){
        this.nonTerminalParent = parent;
    }

    public Terminal(NonTerminal parent, String... terminals){
        this.nonTerminalParent = parent;
        setTerminals(terminals);
    }

    @Override
    public NonTerminal getParent() {
        return nonTerminalParent;
    }

    public ArrayList<String> getTerminals() {
        return terminals;
    }

    public void setTerminals(String... terminals) {
        this.terminals = new ArrayList<>();
        this.terminals.addAll(Arrays.asList(terminals));
    }

    public void addTerminal(String terminal){
        terminals.add(terminal);
    }

    @Override
    public String toString() {
        return null;
    }
}
