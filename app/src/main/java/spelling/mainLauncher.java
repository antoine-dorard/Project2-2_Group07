/*
 * The spelling package contains classes that implement a spelling checker.
 */
package spelling;

import java.io.IOException;
import java.util.Random;

/*
 * The mainLauncher class generates spelling mistakes in a given string with a specified probability 
 * and applies a spelling correction algorithm to the mistakes. 
 * It prints a table with the generated mistakes and their corrections for a range of probabilities.
 */
public class mainLauncher {
    public static void main(String[] args) {

        String fileName = "app/src/main/java/spelling/data/words.txt";
        String spellingMistake = "whatt lekture doe ii havve on moenday";
        String correctedPhrase = "";

        try {
            SpellingCorrector corrector = new SpellingCorrector(fileName);
            correctedPhrase = corrector.correctSpelling(spellingMistake);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        System.out.println("Mistaken phrase: " + spellingMistake + "\nCorrected phrase: " + correctedPhrase);
            
        
    }
}
