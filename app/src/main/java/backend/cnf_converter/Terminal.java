package backend.cnf_converter;

import java.util.Objects;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Terminal terminal1 = (Terminal) o;
        return Objects.equals(terminal, terminal1.terminal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(terminal);
    }

    @Override
    public String toString() {
        return terminal;
    }
}
