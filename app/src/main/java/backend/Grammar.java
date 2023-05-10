package backend;

import java.util.*;

public class Grammar {

    private Map<String, List<String>> terminalProductions;
    private List<String[]> binaryProductions;

    public Grammar() {
        this.terminalProductions = new HashMap<>();
        this.binaryProductions = new ArrayList<>();
    }

    public void addProduction(String A, String B, String C) {
        binaryProductions.add(new String[]{A, B, C});
    }

    public void addTerminal(String A, String terminal) {
        if (!terminalProductions.containsKey(A)) {
            terminalProductions.put(A, new ArrayList<>());
        }
        terminalProductions.get(A).add(terminal);
    }

    public List<String> getNonTerminals(String terminal) {
        List<String> nonTerminals = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : terminalProductions.entrySet()) {
            if (entry.getValue().contains(terminal)) {
                nonTerminals.add(entry.getKey());
            }
        }
        return nonTerminals;
    }

    public List<String[]> getBinaryProductions() {
        return binaryProductions;
    }
}

    

