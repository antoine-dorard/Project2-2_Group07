package backend.autocompletion;

import java.util.*;

public class NGramModel {

    Map<String, Integer> nGramFreq;

    public NGramModel() {
        nGramFreq = new HashMap<>();
    }

    /**
     * Method that generates all n-grams of a sentence, according to a certain n-gram size
     * @param question the sentence of which the n-grams will be generated
     * @param nGramSize size of n-gram
     * @return list of all n-grams of the sentence
     */
    public List<String> generateNGrams(String question, int nGramSize){
        question = question.replaceAll("\\p{Punct}", ""); // deleting chars that are not letters
        question = question.toLowerCase(Locale.ROOT);
        List<String> nGrams = new ArrayList<>();
        String[] words = question.split("\\s+"); // tokenization

        for (int i = 0; i < words.length - nGramSize + 1; i++) {
            StringBuilder builder = new StringBuilder();
            // add words to nGram according to nGramSize
            for (int j = i; j < i + nGramSize; j++) {
                builder.append(words[j]).append(" ");
            }
            String nGram = builder.toString().trim();
            nGrams.add(nGram);
        }
        return nGrams;
    }

    public void train(List<String> questions, int nGramSize){
        // iterate over all predefined questions
        for (String question : questions) {
            List<String> nGrams = generateNGrams(question, nGramSize);

            for (String nGram : nGrams){
                // if n-gram is already present in map, retrieve freq
                int freq = nGramFreq.getOrDefault(nGram, 0);
                // since the n-gram is visited once more, increase freq
                nGramFreq.put(nGram, freq + 1);
            }
        }
    }
}
