package backend.autocompletion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NGramModel {

    Map<String, Integer> nGramFreq;

    public NGramModel() {
        nGramFreq = new HashMap<>();
    }

    public List<String> generateNGrams(String question, int nGramSize){
        question = question.replaceAll("\\p{Punct}", ""); // deleting chars that are not letters
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
