package backend.cnf_converter;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A Terminal is a Grammar Variable that represents a word or multiple words that don't contain a placeholder.
 */
public class Terminal extends GrammarVariable {

    private final NonTerminal nonTerminalParent;

    private String terminal;

    public Terminal(NonTerminal parent){
        this.nonTerminalParent = parent;
    }

    public Terminal(NonTerminal parent, String terminal){
        this.nonTerminalParent = parent;
        this.terminal = terminal;
    }

    @Override
    public NonTerminal getParent() {
        return nonTerminalParent;
    }

    public String getTerminal() {
        return terminal;
    }

    @Override
    public String toString() {
        return null;
    }
}
