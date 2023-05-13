package backend.spelling_checker;

import spelling.Trie;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/*
 * The SpellingCorrector class is used to correct spelling mistakes in a given text.
 * It uses a Trie data structure to load and store a dictionary of words for lookup.
 * It also uses the Levenshtein Distance algorithm to find the closest matching word in the dictionary
 * for each word in the input text.
 */
public class Spelling_Checker_Sub {
    private static Trie dictionary;
    private static String fileName;

    public Spelling_Checker_Sub(String fileNameWordList){
        fileName = fileNameWordList;
        try{
            loadDictionary();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Spelling_Checker_Sub(){
        try{
            fileName = "app/src/main/java/spelling/data/words.txt";
            loadDictionary();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * The loadDictionary method reads words from the dictionary file and adds them
     * to the Trie data structure.
     */
    private static void loadDictionary() throws IOException {
        dictionary = new Trie();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String word;
            while ((word = br.readLine()) != null) {
                dictionary.insert(word);
            }
        }
    }

    /*
     * The correctSpelling method corrects the spelling mistakes in a given text.
     * It splits the text into words, finds the closest matching word in the
     * dictionary for each word, and returns the corrected text as a string.
     */
    public String correctSpelling(String question) {
        List<String> words = Arrays.asList(question.split("\\s+"));
        List<String> correctedWords = words.parallelStream()
                .map(word -> findClosestWord(word))
                .collect(Collectors.toList());

        return String.join(" ", correctedWords);
    }

    /*
     * The findClosestWord method finds the closest matching word in the dictionary
     * for a given word.
     * It uses the Levenshtein Distance algorithm to calculate the distance between
     * the given word and each word in the dictionary,
     * and returns the closest matching word in the dictionary.
     */
    public String findClosestWord(String word) {
        Map<Integer, String> value_word = new HashMap<Integer, String>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String dictionaryWord;
            LevenshteinDistance levenditance = new LevenshteinDistance();
            KeyBoardChecker keyboard = new KeyBoardChecker();
            Soundex soundex = new Soundex();
            keyboard.selectKeyboard();

            while ((dictionaryWord = br.readLine()) != null) {
                int distance = levenditance.computeDistance(word, dictionaryWord);
                int keyboardscore = keyboard.checkerkeys(word, dictionaryWord);
                if(keyboardscore==0){
                    keyboardscore=1;
                }else{
                    keyboardscore=0;
                }
                String soundexcode1 = soundex.getCode(word);
                String soundexcode2 = soundex.getCode(dictionaryWord);
                int soundexScore = 0;
                if(soundexcode1.equals(soundexcode2)){
                    soundexScore = 1;
                }

                value_word.put(distance*4-keyboardscore-soundexScore,dictionaryWord);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        int minKey = Collections.min(value_word.keySet());
        String closestWord = value_word.get(minKey);

        System.out.println("Closest Word: " + minKey);
        return closestWord;
    }
}
