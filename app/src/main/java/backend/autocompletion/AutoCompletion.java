package backend.autocompletion;
import java.util.*;

public class AutoCompletion {
    NGramModel nGramModel;
    final int MAXNGRAMSIZE = 3;
    int NGramSize = MAXNGRAMSIZE;

    List<String> suggestions;
    List<String> secondSuggestions;

    final int MAXSUGGESTIONS = 3;

    List<String> questions = Arrays.asList(
            "What is the temperature in London?",
            "What is the temperature in Maastricht?",
            "What is the weather in Berlin?",
            "What is the weather in Amsterdam?",
            "What is the populationsize of Tokyo?",
            "What is the populationsize of Rome?",
            "What is the language of Spain?",
            "Where is DeepSpace?",
            "Where is SpaceBox?",
            "How do I get to DeepSpace?",
            "How do I get to SpaceBox?",
            "How do you get to DeepSpace?",
            "How do you get to SpaceBox?",
            "How do he get to DeepSpace?",
            "How do he get to SpaceBox?",
            "How do she get to DeepSpace?",
            "How do she get to SpaceBox?",
            "Who is Antoine?",
            "Who is John?",
            "Which lectures are there on Monday at 9?",
            "Which lectures are there on Monday at 12?",
            "Which lectures are there on Tuesday at 9?",
            "Which lectures are there on Tuesday at 12?",
            "Which lectures are there on Wednesday at 9?",
            "Which lectures are there on Wednesday at 12?",
            "Which lectures are there on Thursday at 9?",
            "Which lectures are there on Thursday at 12?",
            "Which lectures are there on Friday at 9?",
            "Which lectures are there on Friday at 12?",
            "Which lectures are there on Saturday at 9?",
            "Which lectures are there on Saturday at 12?",
            "Which lectures are there on Sunday at 9?",
            "Which lectures are there on Sunday at 12?",
            "Which lectures are there at 9 on Monday?",
            "Which lectures are there at 9 on Tuesday?",
            "Which lectures are there at 9 on Wednesday?",
            "Which lectures are there at 9 on Thursday?",
            "Which lectures are there at 9 on Friday?",
            "Which lectures are there at 9 on Saturday?",
            "Which lectures are there at 9 on Sunday?",
            "Which lectures are there at 12 on Monday?",
            "Which lectures are there at 12 on Tuesday?",
            "Which lectures are there at 12 on Wednesday?",
            "Which lectures are there at 12 on Thursday?",
            "Which lectures are there at 12 on Friday?",
            "Which lectures are there at 12 on Saturday?",
            "Which lectures are there at 12 on Sunday?",
            "On Monday at 9, which lectures?",
            "On Monday at 12, which lectures?",
            "On Tuesday at 9, which lectures?",
            "On Tuesday at 12, which lectures?",
            "On Wednesday at 9, which lectures?",
            "On Wednesday at 12, which lectures?",
            "On Thursday at 9, which lectures?",
            "On Thursday at 12, which lectures?",
            "On Friday at 9, which lectures?",
            "On Friday at 12, which lectures?",
            "On Saturday at 9, which lectures?",
            "On Saturday at 12, which lectures?",
            "On Sunday at 9, which lectures?",
            "On Sunday at 12, which lectures?",
            "At 9 on Monday, which lectures?",
            "At 9 on Tuesday, which lectures?",
            "At 9 on Wednesday, which lectures?",
            "At 9 on Thursday, which lectures?",
            "At 9 on Friday, which lectures?",
            "At 9 on Saturday, which lectures?",
            "At 9 on Sunday, which lectures?",
            "At 12 on Monday, which lectures?",
            "At 12 on Tuesday, which lectures?",
            "At 12 on Wednesday, which lectures?",
            "At 12 on Thursday, which lectures?",
            "At 12 on Friday, which lectures?",
            "At 12 on Saturday, which lectures?",
            "At 12 on Sunday, which lectures?",
            "Which lectures are there at 9?",
            "Which lectures are there at 12?"
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
        String[] words = subSentence.split(" "); // tokenize

        // Determine the appropriate n-gram size
        NGramSize = Math.min(MAXNGRAMSIZE, words.length);

        // Extract the subsequence from the last n-gram
        String subsequence = String.join(" ", Arrays.copyOfRange(words, words.length - NGramSize, words.length));

        suggestions = new ArrayList<>();
        secondSuggestions = new ArrayList<>();

        List<String> nGrams = nGramModel.generateNGrams(subsequence, NGramSize);

        for (String nGram : nGrams){
            Map<String, Integer> nextWordFreq = new LinkedHashMap<>();

            for (String question : questions){
                List<String> questionNgrams = nGramModel.generateNGrams(question, NGramSize);

                for (int i = 0; i < questionNgrams.size() - 1; i++) {
                    if(questionNgrams.get(i).equals(nGram)){
                        String nextWord = questionNgrams.get(i+1).split(" ")[NGramSize-1];
                        int freq = nextWordFreq.getOrDefault(nextWord, 0);
                        nextWordFreq.put(nextWord, freq + 1);
                    }
                }
            }
            int maxFreq = 0;
            List<String> mostFreqWords = new ArrayList<>();
            List<String> secondMostFreqWords = new ArrayList<>();

            for (Map.Entry<String, Integer> entry : nextWordFreq.entrySet()) {
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
    }
}
