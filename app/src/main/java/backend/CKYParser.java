package backend;

import java.util.*;





public class CKYParser {

    public static void main(String[] args) {
        Grammar grammar = new Grammar();
        grammar.addProduction("S", "NP", "VP");
        grammar.addProduction("NP", "Det", "N");
        grammar.addProduction("VP", "V", "NP");

        grammar.addTerminal("Det", "the");
        grammar.addTerminal("N", "dog");
        grammar.addTerminal("N", "cat");
        grammar.addTerminal("V", "chased");

        String[] words = {"the", "dog", "chased", "the", "cat"};
        String[][][] table = ckyParse(words, grammar);
        System.out.println("Table contents:");
        printTable(table);
    }

    public static String[][][] ckyParse(String[] words, Grammar grammar) {
        int n = words.length;
        String[][][] table = new String[n][n][];

        for (int j = 0; j < n; j++) {
            table[j][j] = grammar.getNonTerminals(words[j]).toArray(new String[0]);
            for (int i = j - 1; i >= 0; i--) {
                List<String> ijProductions = new ArrayList<>();
                for (int k = i; k < j; k++) {
                    for (String[] production : grammar.getBinaryProductions()) {
                        String A = production[0];
                        String B = production[1];
                        String C = production[2];
                        if (contains(table[i][k], B) && contains(table[k + 1][j], C)) {
                            ijProductions.add(A);
                        }
                    }
                }
                table[i][j] = ijProductions.toArray(new String[0]);
                // table[][] will be GramarVariable which holds NonTerminal and Terminal instances
                // 
            }
        }

        return table;
    }

    private static boolean contains(String[] array, String target) {
        for (String s : array) {
            if (s.equals(target)) {
                return true;
            }
        }
        return false;
    }

    private static void printTable(String[][][] table) {
        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[i].length; j++) {
                System.out.print("[");
                if (table[i][j] != null) {
                    System.out.print(String.join(",", table[i][j]));
                }
                System.out.print("] ");
            }
            System.out.println();
        }
    }
}

//TO DO:
// CNFRule : holds a NonTerminal