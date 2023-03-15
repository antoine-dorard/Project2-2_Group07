package Spelling_Checker;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class WordSuggester{
    String file = "Spelling_Checker/words.txt";
    ArrayList<String> wordlist = new ArrayList<String>();
    String input = "hallo";
    LevenshteinDistance lDistance = new LevenshteinDistance();
    int i;
    HashMap<String, Integer> newlist = new HashMap<String, Integer>();

    public void inputMatches(String input){
        try{
            loadworddoc(file);
        }
        catch (IOException e) {
            System.out.println("didn't find the file");
            e.printStackTrace();
        }
        for(String word : wordlist){
            i = lDistance.computeDistance(word,input);
            if(i<3){
                newlist.put(word,i);
            }
        }
        // Sorting to get the best cities in the top
        List<Map.Entry<String, Integer>> list = new ArrayList<>(newlist.entrySet());

        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });

        LinkedHashMap<String, Integer> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        System.out.println(sortedMap);
    }


    public void loadworddoc(String filename) throws IOException {
        String line;
        BufferedReader br = new BufferedReader(new FileReader(filename));
        while((line = br.readLine()) != null){
            if(line.length()>2 && line.length()<26)
                wordlist.add(line);
        }
    }

    public static void main(String[] args) {
        WordSuggester ws = new WordSuggester();
        String input = "hella";
        ws.inputMatches(input);
    }
}
