package backend.spelling_checker;

import spelling.SpellingCorrector;

import java.io.IOException;
import java.util.Locale;

public class Spelling_Checker_Ranker {
    public static void main(String[] args) {

        String fileName = "app/src/main/java/spelling/data/words.txt";
        String spellingMistake = "is this ream";
        String correctedPhrase = "";

        Spelling_Checker_Sub corrector = new Spelling_Checker_Sub(fileName.toLowerCase());
        correctedPhrase = corrector.correctSpelling(spellingMistake);

        System.out.println("Mistaken phrase: " + spellingMistake + "\nCorrected phrase: " + correctedPhrase);
    }
}
