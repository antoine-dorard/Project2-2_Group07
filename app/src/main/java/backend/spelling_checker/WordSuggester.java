package backend.spelling_checker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class WordSuggester {
    File file;


    // Added error handling
    public String inputMatches(String input) {
        file = new File("app/src/main/java/backend/spelling_checker/words.txt");

        ArrayList<String> wordlist = new ArrayList<>();
        LevenshteinDistance lDistance = new LevenshteinDistance();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.length() > 0 && line.length() < 26) { //the parameters of the words.txt we want to compare with (in between 2 and 26)
                    wordlist.add(line);
                    System.out.println("this is the line " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading word document: " + e.getMessage());
        }
        
        // The HashMap<String, Integer> newlist got replaced with Map<String, List<Integer>> newlist = new HashMap<>().
        // The reason for this change is that the original HashMap only stored one integer value per word,
        // which meant that if we had multiple words with the same distance to the input word,
        // only one of them would be stored. 
        // By using a HashMap with a List, multiple distances can be stored for each word,
        // this is more efficient for the sorting of the distances and returning more.
        System.out.println("Word\tDistance");
        Map<String, List<Integer>> newlist = new HashMap<>();
        for (String word : wordlist) {
            int i = lDistance.computeDistance(word, input);
            System.out.printf("%s\t%d\n", word, i);

            if (i < 3) {
                newlist.computeIfAbsent(word, k -> new ArrayList<>()).add(i);
            }
        }

        System.out.println("newlist: " + newlist);

        List<Map.Entry<String, List<Integer>>> list = new ArrayList<>(newlist.entrySet());

        // I simplified the sorting logic to use the Collections.min() method to find
        // the minimum distance for each word, rather than sorting the list.
        Collections.sort(list, Comparator.comparing(entry -> Collections.min(entry.getValue())));

        LinkedHashMap<String, List<Integer>> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<String, List<Integer>> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        System.out.println("sortedMap: " + sortedMap);
        return list.size() != 0 ? String.valueOf(list.get(0).getKey()) : input;
    }
}