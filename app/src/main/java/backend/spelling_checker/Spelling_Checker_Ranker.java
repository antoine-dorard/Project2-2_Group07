package Spelling_Checker;

import spelling.SpellingCorrector;

import java.io.IOException;

public class Spelling_Checker_Ranker {
    public static void main(String[] args) {

        String fileName = "app/src/main/java/spelling/data/words.txt";
        String spellingMistake = "is this ream";
        String correctedPhrase = "";

        try {
            Spelling_Checker_Sub corrector = new Spelling_Checker_Sub(fileName);
            correctedPhrase = corrector.correctSpelling(spellingMistake);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Mistaken phrase: " + spellingMistake + "\nCorrected phrase: " + correctedPhrase);
    }
}
