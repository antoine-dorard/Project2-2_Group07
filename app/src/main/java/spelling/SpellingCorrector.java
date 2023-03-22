/*
 * The spelling package contains classes that implement a spelling checker.
 */
package spelling;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/*
 * The SpellingCorrector class is used to correct spelling mistakes in a given text.
 * It uses a Trie data structure to load and store a dictionary of words for lookup.
 * It also uses the Levenshtein Distance algorithm to find the closest matching word in the dictionary
 * for each word in the input text.
 */
public class SpellingCorrector {
    private static Trie dictionary;
    private static String fileName;

    /*
     * Constructor for the SpellingCorrector class.
     * It initializes the fileName and loads the dictionary from the file.
     */
    public SpellingCorrector(String fileNameWordList) throws IOException {
        fileName = fileNameWordList;
        loadDictionary();
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
     * dictionary for each word,
     * and returns the corrected text as a string.
     */
    public static String correctSpelling(String question) {
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
    private static String findClosestWord(String word) {
        AtomicReference<String> closestWord = new AtomicReference<>("");
        AtomicReference<Integer> minDistance = new AtomicReference<>(Integer.MAX_VALUE);

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String dictionaryWord;
            while ((dictionaryWord = br.readLine()) != null) {
                int distance = levenshteinDistance(word.toLowerCase(), dictionaryWord.toLowerCase());

                if (distance < minDistance.get()) {
                    minDistance.set(distance);
                    closestWord.set(dictionaryWord);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return closestWord.get();
    }

    /*
     * The levenshteinDistance method calculates the Levenshtein Distance between
     * two strings.
     * It uses dynamic programming to fill a matrix with the distances between each
     * pair of substrings,
     * and returns the distance between the two input strings.
     */
    private static int levenshteinDistance(String a, String b) {
        int[][] dp = new int[a.length() + 1][b.length() + 1];

        for (int i = 0; i <= a.length(); i++) {
            for (int j = 0; j <= b.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    dp[i][j] = Math.min(Math.min(dp[i - 1][j], dp[i][j - 1]), dp[i - 1][j - 1])
                            + (a.charAt(i - 1) == b.charAt(j - 1) ? 0 : 1);
                }
            }
        }

        return dp[a.length()][b.length()];
    }

    /*
     * 
     * The getCorrectedQuestion method is a convenience method that calls the
     * correctSpelling method.
     * It takes a string question as input and returns the corrected question by
     * calling the correctSpelling method.
     */
    public static String getCorrectedQuestion(String question) {
        return correctSpelling(question);
    }
}