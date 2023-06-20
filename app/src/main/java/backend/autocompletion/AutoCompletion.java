package backend.autocompletion;
import java.util.*;
// TODO: only generate suggestions if first word is recognized as first word
public class AutoCompletion {
    NGramModel nGramModel;
    final int MAXNGRAMSIZE = 3;
    int NGramSize = MAXNGRAMSIZE;

    private List<String> suggestions = new ArrayList<>();
    private List<String> secondSuggestions = new ArrayList<>();
    private final List<String> finalSuggestions = new ArrayList<>();

    final int MAXSUGGESTIONS = 3;

    List<String> questions = Arrays.asList(
            "[S] What is the temperature in London? [E]",
            "[S] What is the temperature in Maastricht? [E]",
            "[S] What is the weather in Berlin? [E]",
            "[S] What is the weather in Amsterdam? [E]",
            "[S] What is the populationsize of Tokyo? [E]",
            "[S] What is the populationsize of Rome? [E]",
            "[S] What is the language of Spain? [E]",
            "[S] Where is DeepSpace? [E]",
            "[S] Where is SpaceBox? [E]",
            "[S] How do I get to DeepSpace? [E]",
            "[S] How do I get to SpaceBox? [E]",
            "[S] How do you get to DeepSpace? [E]",
            "[S] How do you get to SpaceBox? [E]",
            "[S] How do he get to DeepSpace? [E]",
            "[S] How do he get to SpaceBox? [E]",
            "[S] How do she get to DeepSpace? [E]",
            "[S] How do she get to SpaceBox? [E]",
            "[S] Who is Antoine? [E]",
            "[S] Who is John? [E]",
            "[S] Which lectures are there on Monday at 9? [E]",
            "[S] Which lectures are there on Monday at 12? [E]",
            "[S] Which lectures are there on Tuesday at 9? [E]",
            "[S] Which lectures are there on Tuesday at 12? [E]",
            "[S] Which lectures are there on Wednesday at 9? [E]",
            "[S] Which lectures are there on Wednesday at 12? [E]",
            "[S] Which lectures are there on Thursday at 9? [E]",
            "[S] Which lectures are there on Thursday at 12? [E]",
            "[S] Which lectures are there on Friday at 9? [E]",
            "[S] Which lectures are there on Friday at 12? [E]",
            "[S] Which lectures are there on Saturday at 9? [E]",
            "[S] Which lectures are there on Saturday at 12? [E]",
            "[S] Which lectures are there on Sunday at 9? [E]",
            "[S] Which lectures are there on Sunday at 12? [E]",
            "[S] Which lectures are there at 9 on Monday? [E]",
            "[S] Which lectures are there at 9 on Tuesday? [E]",
            "[S] Which lectures are there at 9 on Wednesday? [E]",
            "[S] Which lectures are there at 9 on Thursday? [E]",
            "[S] Which lectures are there at 9 on Friday? [E]",
            "[S] Which lectures are there at 9 on Saturday? [E]",
            "[S] Which lectures are there at 9 on Sunday? [E]",
            "[S] Which lectures are there at 12 on Monday? [E]",
            "[S] Which lectures are there at 12 on Tuesday? [E]",
            "[S] Which lectures are there at 12 on Wednesday? [E]",
            "[S] Which lectures are there at 12 on Thursday? [E]",
            "[S] Which lectures are there at 12 on Friday? [E]",
            "[S] Which lectures are there at 12 on Saturday? [E]",
            "[S] Which lectures are there at 12 on Sunday? [E]",
            "[S] Which lectures are there at 9? [E]",
            "[S] Which lectures are there at 12? [E]"
    );


    public AutoCompletion(){
        nGramModel = new NGramModel();
        this.train(questions);
    }

    public void train(List<String> questions){
        nGramModel.train(questions, MAXNGRAMSIZE);
    }

    /**
     * method that predicts the next word based on a piece of sentence
     * @param subSentence subsentence of which the next word will be predicted
     */
    public void nextProbableWord(String subSentence){

        // reset
        suggestions.clear();
        secondSuggestions.clear();
        finalSuggestions.clear();

        subSentence = subSentence.toLowerCase(Locale.ROOT); // lowercase
        String[] words = subSentence.split("\\s+"); // tokenize

        // determine the appropriate n-gram size:
        // take NGramSize = maxNGramSize if number of words is bigger
        NGramSize = Math.min(MAXNGRAMSIZE, words.length);

        // extract the subsequence from the last n-gram
        String subsequence = String.join(" ", Arrays.copyOfRange(words, words.length - NGramSize, words.length));

        List<String> nGrams = nGramModel.generateNGrams(subsequence, NGramSize);

        for (String nGram : nGrams){
            Map<String, Integer> nextWordFreq = new LinkedHashMap<>();

            for (String question : questions){
                List<String> questionNgrams = nGramModel.generateNGrams(question, NGramSize);

                for (int i = 0; i < questionNgrams.size() - 1; i++) {
                    if(questionNgrams.get(i).equals(nGram)){
                        String nextWord = questionNgrams.get(i+1).split("\\s+")[NGramSize-1];
                        int freq = nextWordFreq.getOrDefault(nextWord, 0);
                        nextWordFreq.put(nextWord, freq + 1);
                    }
                }
            }
            int maxFreq = 0;
            List<String> mostFreqWords = new ArrayList<>();
            List<String> secondMostFreqWords = new ArrayList<>();

            for (Map.Entry<String, Integer> entry : nextWordFreq.entrySet()) {

                // if it is the end of the question, do add it to the suggestions (it is the end of the sentence)
                if (entry.getKey().equals("e")){
                    continue;
                }
                // if we find a word with a higher frequency, overwrite list
                if (entry.getValue() > maxFreq) {
                    secondMostFreqWords.clear();
                    secondMostFreqWords.addAll(mostFreqWords);

                    mostFreqWords.clear();

                    mostFreqWords.add(entry.getKey());

                    maxFreq = entry.getValue();
                } else if (entry.getValue().equals(maxFreq)) {
                    mostFreqWords.add(entry.getKey());
                } else if (secondMostFreqWords.isEmpty() || entry.getValue() > nextWordFreq.get(secondMostFreqWords.get(0))) {
                    secondMostFreqWords.clear();
                    secondMostFreqWords.add(entry.getKey());
                } else if (entry.getValue().equals(nextWordFreq.get(secondMostFreqWords.get(0)))) {
                    secondMostFreqWords.add(entry.getKey());
                }
            }
            suggestions.addAll(mostFreqWords);
            secondSuggestions.addAll(secondMostFreqWords);
        }
        limitMaxSuggestions();
    }

    /**
     * Method that shortens suggestions list, such that not all
     * suggestions will be displayed.
     */
    public void limitMaxSuggestions(){
        if (suggestions.size() > MAXSUGGESTIONS){
            suggestions = suggestions.subList(0, MAXSUGGESTIONS);
        } else if (suggestions.size()+secondSuggestions.size() > MAXSUGGESTIONS){
            secondSuggestions = secondSuggestions.subList(0, MAXSUGGESTIONS-suggestions.size());
        }
        finalSuggestions.addAll(suggestions);
        finalSuggestions.addAll(secondSuggestions);
    }


    // accessors
    public List<String> getSuggestions() {
        return suggestions;
    }

    public List<String> getSecondSuggestions() {
        return secondSuggestions;
    }

    public List<String> getFinalSuggestions() {
        return finalSuggestions;
    }
}
