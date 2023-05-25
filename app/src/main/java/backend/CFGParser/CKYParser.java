package backend.CFGParser;

import java.util.*;

import backend.CFGParser.datastructures.CNFRule;
import backend.CFGParser.datastructures.GrammarVariable;
import backend.CFGParser.datastructures.NonTerminal;
import backend.CFGParser.datastructures.Terminal;





public class CKYParser {
    private CNF cnf;
    private HashMap<NonTerminal, Terminal> placeholderValuePairs = new HashMap<>();
    private NonTerminal[][][] ckyMatrix;
    public CKYParser(CNF cnf) {
       this.cnf = cnf;

    }
    public static void main(String[] args) {
        CNF cnf = new CNF(new CFG().readCFG());
        cnf.generateCNF();
        CKYParser ckyParser = new CKYParser(cnf);
        Terminal[] words = new Terminal[]{
//                new Terminal("How"),
//                new Terminal("do"),
//                new Terminal("I"),
//                new Terminal("get"),
//                new Terminal("to"),
//                new Terminal("DeepSpace")

                new Terminal("Which"),
                new Terminal("lectures"),
                new Terminal("are"),
                new Terminal("there"),
                new Terminal("on"),
                new Terminal("Monday"),
                new Terminal("at"),
                new Terminal("9")
        };
        GrammarVariable[][][] table = ckyParser.ckyParse(words);
        ckyParser.printTable(table);
        System.out.println(Arrays.toString(table[0][2]));
        System.out.println(ckyParser.placeholderValuePairs);
        System.out.println(Arrays.toString(ckyParser.getSkills()));

        // TODO
        // 1) match CFG NonTerminals to their value in the given sentence (e.g. "DeepSpace" -> "ROOM")
        // 2) find output action in the json
    }


    public NonTerminal[][][] ckyParse(Terminal[] words) {
        System.out.println(Arrays.toString(words));
        int n = words.length;
        NonTerminal[][][] table = new NonTerminal[n][n][];

        for (int j = 0; j < n; j++) {
            table[j][j] = cnf.getNonTerminals(words[j]).toArray(new NonTerminal[0]);
            if(table[j][j].length != 0 && !table[j][j][0].isDummy()) {
                for (int index = 0; index < table[j][j].length; index++){
                    placeholderValuePairs.put(table[j][j][index], words[j]);
                }
            }
            for (int i = j - 1; i >= 0; i--) {
                List<NonTerminal> ijProductions = new ArrayList<>();
                for (int k = i; k < j; k++) {
                    for (CNFRule production : cnf.getBinaryProductions()) {
                        NonTerminal A = production.getLHS();
                        NonTerminal B = production.getRHS().getPair()[0];
                        NonTerminal C = production.getRHS().getPair()[1];
                        //System.out.println(j + " " + k);
                        if (contains(table[i][k], B) && contains(table[k + 1][j], C)) {
                            ijProductions.add(A);
                        }
                        
                    }
                }
                table[i][j] = ijProductions.toArray(new NonTerminal[0]);
                
            }
        }
        this.ckyMatrix = table;
        return table;
    }

    public String[] getSkills(){
        String[] skills = new String[ckyMatrix[0][ckyMatrix.length - 1].length];
        for (int i = 0; i < ckyMatrix[0][ckyMatrix.length - 1].length; i++){
            skills[i] = ckyMatrix[0][ckyMatrix.length - 1][i].toString();
        }
        return skills;
    }

    private boolean contains(GrammarVariable[] array, NonTerminal target) {
        for (GrammarVariable s : array) {
            if (s.equals(target)) {
                return true;
            }
        }
        return false;
    }


    public void printTable(){
        printTable(ckyMatrix);
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

    public HashMap<NonTerminal, Terminal> getPlaceholderValuePairs() {
        return placeholderValuePairs;
    }
}


