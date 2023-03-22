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

    /*
     * The getSpellingMistakes method generates spelling mistakes in a string based
     * on a specified probability.
     * It replaces each character in the string with a random letter with the given
     * probability,
     * and returns the resulting string.
     */
    public static String getSpellingMistakes(String question, double probability) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < question.length(); i++) {
            char character = question.charAt(i);
            double randomNumber = Math.random();
            if (randomNumber < probability) {
                char randomLetter = (char) (Math.random() * 26 + 'a');
                result.append(randomLetter);
            } else {
                result.append(character);
            }
        }
        return result.toString();
    }

    public static String[] generateQuestions() {
        String[] DAYS = { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday" };
        String[] TIMES = { "9:00", "10:00", "11:00", "12:00", "1:00" };
        String[] TEACHERS = { "Ms. Johnson", "Mr. Smith", "Dr. Lee", "Professor Chen" };
        String[] CITIES = { "New York", "Los Angeles", "Chicago", "Houston", "Miami" };

        Random rand = new Random();
        String[] questions = new String[6];
        questions[0] = "Which lectures are there on " + DAYS[rand.nextInt(DAYS.length)] + " at "
                + TIMES[rand.nextInt(TIMES.length)] + "?";
        questions[1] = "What lecture do I have with " + TEACHERS[rand.nextInt(TEACHERS.length)] + " at "
                + TIMES[rand.nextInt(TIMES.length)] + "?";
        questions[2] = "What is the weather like in " + CITIES[rand.nextInt(CITIES.length)] + "?";
        questions[3] = "What temperature is it in " + CITIES[rand.nextInt(CITIES.length)] + "?";
        questions[4] = "Is it raining in " + CITIES[rand.nextInt(CITIES.length)] + "?";
        questions[5] = "Is it snowing in " + CITIES[rand.nextInt(CITIES.length)] + "?";

        return questions;
    }

    public static void main(String[] args) {
        // create a string with the question
        String[] questions = generateQuestions();
        for (String q : questions) {
            System.out.println(q);
        }
        String fileName = "app/src/main/java/spelling/data/words.txt";

        // create an array of probabilities doubles from 1% to 10% in steps of 1%
        double[] probabilities = new double[10];
        for (int i = 0; i < probabilities.length; i++) {
            probabilities[i] = (i + 1) * 0.01;
        }

        // print the header of the table
        System.out.printf("%-15s %-30s %-30s%n", "Probability", "Spelling Mistake", "Corrected Phrase");
        for (int j = 0; j < questions.length; j++) {
            // loop through the probabilities array and print the spelling mistakes
            for (int i = 0; i < probabilities.length; i++) {
                // create a string with the spelling mistake
                String spellingMistake = getSpellingMistakes(questions[j], probabilities[i]);
                String correctedPhrase = "";

                try {
                    // create an instance of the SpellingCorrector class and correct the spelling
                    // mistake
                    SpellingCorrector corrector = new SpellingCorrector(fileName);
                    correctedPhrase = corrector.correctSpelling(spellingMistake);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // print the row of the table
                System.out.printf("%-15s %-30s %-30s%n", probabilities[i], spellingMistake, correctedPhrase);
            }
        }

    }
}
