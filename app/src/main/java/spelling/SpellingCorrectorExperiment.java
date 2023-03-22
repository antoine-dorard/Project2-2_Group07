package spelling;

import java.io.FileWriter;
import java.io.IOException;

public class SpellingCorrectorExperiment {

    // Experiment constants
    private static final String DICTIONARY_FILE = "app/src/main/java/spelling/data/words.txt";
    private static final int NUMBER_OF_QUESTIONS = 10;
    private static final int[] PROBABILITIES = {1,2,3,4,5,6,7,8,9,10};
    private static final String CSV_FILE = "app/src/main/java/spelling/data/experiment_data.csv";

    public static void main(String[] args) {
        // Generate questions
        String[] questions = mainLauncher.generateQuestions();

        try (FileWriter csvWriter = new FileWriter(CSV_FILE)) {
            // Write the CSV header
            csvWriter.append(
                    "Probability,Original Question,Spelling Mistake,Corrected Phrase,Score,Time (ms),Original Length,Mistake Length,Corrected Length\n");

            // Run the experiments
            for (int probability : PROBABILITIES) {
                for (int i = 0; i < NUMBER_OF_QUESTIONS; i++) {
                    String originalQuestion = questions[i % questions.length];
                    String spellingMistake = mainLauncher.getSpellingMistakes(originalQuestion, (double) probability/100);
                    String correctedPhrase = "";

                    long startTime = System.currentTimeMillis();

                    try {
                        // Create an instance of the SpellingCorrector class and correct the spelling
                        // mistake
                        SpellingCorrector corrector = new SpellingCorrector(DICTIONARY_FILE);
                        correctedPhrase = corrector.correctSpelling(spellingMistake);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    long endTime = System.currentTimeMillis();
                    long timeTaken = endTime - startTime;

                    // Calculate the score
                    double score = calculateScore(originalQuestion, correctedPhrase);

                    // Write the results to the CSV file
                    csvWriter.append(String.format("%d,\"%s\",\"%s\",\"%s\",%d,%d,%d,%d,%d%n",
                            probability, originalQuestion,
                            spellingMistake, correctedPhrase, (int) (score * 100), timeTaken, originalQuestion.length(),
                            spellingMistake.length(), correctedPhrase.length()));

                }
            }

            csvWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Experiment data has been exported to " + CSV_FILE);
    }

    private static double calculateScore(String original, String corrected) {
        int correctCharacters = 0;
        int minLength = Math.min(original.length(), corrected.length());

        for (int i = 0; i < minLength; i++) {
            if (original.charAt(i) == corrected.charAt(i)) {
                correctCharacters++;
            }
        }

        return (double) correctCharacters / original.length();
    }
}
