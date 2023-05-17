package backend.CFGParser;

import java.util.*;

import backend.CFGParser.datastructures.CNFRule;
import backend.CFGParser.datastructures.GrammarVariable;
import backend.CFGParser.datastructures.NonTerminal;
import backend.CFGParser.datastructures.Terminal;





public class CKYParser {
    private CNF cnf;
    public CKYParser(CNF cnf) {
       this.cnf = cnf;

    }
    public static void main(String[] args) {
        CNF cnf = new CNF(new CFG());
        cnf.generateCNF();
        CKYParser ckyParser = new CKYParser(cnf);
        Terminal[] words = new Terminal[]{
            new Terminal("How"), 
            new Terminal("do"), 
            new Terminal("I"), 
            new Terminal("get"), 
            new Terminal("to"), 
            new Terminal("DeepSpace")
        };
        GrammarVariable[][][] table = ckyParser.ckyParse(words);
        ckyParser.printTable(table);
        System.out.println(Arrays.toString(table[0][2]));

        // TODO
        // 1) match CFG NonTerminals to their value in the given sentence (e.g. "DeepSpace" -> "ROOM")
        // 2) find output action in the json
    }

    public GrammarVariable[][][] ckyParse(Terminal[] words) {
        int n = words.length;
        GrammarVariable[][][] table = new GrammarVariable[n][n][];

        for (int j = 0; j < n; j++) {
            table[j][j] = cnf.getNonTerminals(words[j]).toArray(new NonTerminal[0]);
            for (int i = j - 1; i >= 0; i--) {
                List<NonTerminal> ijProductions = new ArrayList<>();
                for (int k = i; k < j; k++) {
                    for (CNFRule production : cnf.getBinaryProductions()) {
                        NonTerminal A = production.getLHS();
                        NonTerminal B = production.getRHS().getPairs().get(0)[0];
                        NonTerminal C = production.getRHS().getPairs().get(0)[1];
                        //System.out.println(j + " " + k);
                        if (contains(table[i][k], B) && contains(table[k + 1][j], C)) {
                            ijProductions.add(A);
                        }
                        
                    }
                }
                table[i][j] = ijProductions.toArray(new NonTerminal[0]);
                
            }
        }

        return table;
    }

    private boolean contains(GrammarVariable[] array, NonTerminal target) {
        for (GrammarVariable s : array) {
            if (s.equals(target)) {
                return true;
            }
        }
        return false;
    }

    

    private void printTable(GrammarVariable[][][] table) {
        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[i].length; j++) {
                System.out.print("[");
                if (table[i][j] != null) {
                    for (int k = 0; k < table[i][j].length; k++) {
                        System.out.print(table[i][j][k]);
                        if (k != table[i][j].length - 1) {
                            System.out.print(", ");
                        }
                    }
                }
                System.out.print("] ");
            }
            System.out.println();
        }
    }
}


