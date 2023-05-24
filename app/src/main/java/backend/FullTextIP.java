package backend;

import backend.answer_generator.ContextFreeGrammarAG;
import backend.answer_generator.FullTextAG;
import backend.spelling_checker.WordSuggester;
import main.SkillLoader;

public class FullTextIP implements InputProcessor{

    WordSuggester word_check = new WordSuggester();
    SkillLoader skillLoader;

    public FullTextIP(SkillLoader skillLoader) {
        this.skillLoader = skillLoader;
    }


    /**
     * Processes the user input and returns the processed input.
     *
     * @param input The user input.
     * @return
     */
    @Override
    public String processInput(String input) {

        // 1) Spelling Check
        String[] words = input.split("\\s+");
        String[] transformedWords = new String[words.length];
        for (int i = 0; i < words.length; i++) {
            transformedWords[i] = word_check.inputMatches(words[i]);
        }

        // Join the words back together
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < transformedWords.length; i++) {
            sb.append(transformedWords[i]);
            if (i < transformedWords.length - 1) {
                sb.append(" ");
            }
        }


        input = sb.toString();
        System.out.println("Input after spelling check: " + input);

        // 2) Answer generation
        //String answer = new FullTextAG(skillLoader).generateAnswer(input);
        String answer = new ContextFreeGrammarAG(skillLoader).generateAnswer(input);

        return answer;
    }

}

