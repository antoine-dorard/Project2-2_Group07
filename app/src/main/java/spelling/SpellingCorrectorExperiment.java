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
            // Write the CSV header (first row)
            csvWriter.append(
                    "Probability,Original Question,Spelling Mistake,Corrected Phrase,Score,Time (ms)\n");

            // Run the experiments
            for (int probability : PROBABILITIES) {
                int counter = 0;
                for (int i = 0; i < NUMBER_OF_QUESTIONS; i++) {
                    String originalQuestion = removeQuestionMark(questions[i % questions.length]); 
                    String spellingMistake = removeQuestionMark(mainLauncher.getSpellingMistakes(originalQuestion, (double) probability/100));
                    String correctedPhrase = "";

                    long startTime = System.currentTimeMillis();

                    try {
                        SpellingCorrector corrector = new SpellingCorrector(DICTIONARY_FILE);
                        correctedPhrase = corrector.correctSpelling(spellingMistake);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    long endTime = System.currentTimeMillis();
                    long timeTaken = endTime - startTime;

                    // Calculate the score
                    double score = calculateScore(originalQuestion, correctedPhrase);
                    counter += (int) (score * 100);
                    // Write the results to the CSV file (and format the data according to the header)
                    csvWriter.append(String.format("%d,\"%s\",\"%s\",\"%s\",%d,%d%n",
                            probability, originalQuestion,
                            spellingMistake, correctedPhrase, (int) (score * 100), timeTaken));

                }
                System.out.println("Average score for probability " + probability + " is " + counter/NUMBER_OF_QUESTIONS);
            }

            csvWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Experiment data has been exported to " + CSV_FILE);
    }

    /*
     * The calculateScore method calculates the score of the corrected phrase.
     * It returns the percentage of characters that are correct.
     */
    private static double calculateScore(String original, String corrected) {
        original = original.toLowerCase();
        corrected = corrected.toLowerCase();
        int correctCharacters = 0;
        int minLength = Math.min(original.length(), corrected.length());

        for (int i = 0; i < minLength; i++) {
            if (original.charAt(i) == corrected.charAt(i)) {
                correctCharacters++;
            }
        }

        return (double) correctCharacters / original.length();
    }

    /*
     * The removeQuestionMark method removes the question mark from the end of a
     * question.
     * It returns the question without the question mark.
     */
    private static String removeQuestionMark(String question) {
        if (question.charAt(question.length() - 1) == '?') {
            return question.substring(0, question.length() - 1);
        }
        return question;
    }
}
