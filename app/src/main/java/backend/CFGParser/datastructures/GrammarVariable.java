package backend.CFGParser.datastructures;

/**
 * A Grammar Variable is either a Terminal or a NonTerminal.
 * It is used to represent both the RHS and LHS of a rule.
 */
public abstract class GrammarVariable {
    public abstract String toString();
}
