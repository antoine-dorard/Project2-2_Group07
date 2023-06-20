package backend.autocompletion;
import java.util.*;
public class AutoCompletion {
    NGramModel nGramModel;
    final int MAXNGRAMSIZE = 3;
    int NGramSize = MAXNGRAMSIZE;

    private List<String> suggestions = new ArrayList<>();
    private List<String> secondSuggestions = new ArrayList<>();
    private final List<String> finalSuggestions = new ArrayList<>();

    final int MAXSUGGESTIONS = 3;

    List<String> startWords = new ArrayList<>();

    List<String> questions = Arrays.asList(

            "What is the temperature in London? [E]",
            "What is the temperature in Maastricht? [E]",
            "What is the weather in Berlin? [E]",
            "What is the weather in Amsterdam? [E]",
            "What is the populationsize of Tokyo? [E]",
            "What is the populationsize of Rome? [E]",
            "What is the language of Spain? [E]",
            "Where is DeepSpace? [E]",
            "Where is SpaceBox? [E]",
            "How do I get to DeepSpace? [E]",
            "How do I get to SpaceBox? [E]",
            "How do you get to DeepSpace? [E]",
            "How do you get to SpaceBox? [E]",
            "How do he get to DeepSpace? [E]",
            "How do he get to SpaceBox? [E]",
            "How do she get to DeepSpace? [E]",
            "How do she get to SpaceBox? [E]",
            "Who is Antoine? [E]",
            "Who is John? [E]",
            "Which lectures are there on Monday at 9? [E]",
            "Which lectures are there on Monday at 12? [E]",
            "Which lectures are there on Tuesday at 9? [E]",
            "Which lectures are there on Tuesday at 12? [E]",
            "Which lectures are there on Wednesday at 9? [E]",
            "Which lectures are there on Wednesday at 12? [E]",
            "Which lectures are there on Thursday at 9? [E]",
            "Which lectures are there on Thursday at 12? [E]",
            "Which lectures are there on Friday at 9? [E]",
            "Which lectures are there on Friday at 12? [E]",
            "Which lectures are there on Saturday at 9? [E]",
            "Which lectures are there on Saturday at 12? [E]",
            "Which lectures are there on Sunday at 9? [E]",
            "Which lectures are there on Sunday at 12? [E]",
            "Which lectures are there at 9 on Monday? [E]",
            "Which lectures are there at 9 on Tuesday? [E]",
            "Which lectures are there at 9 on Wednesday? [E]",
            "Which lectures are there at 9 on Thursday? [E]",
            "Which lectures are there at 9 on Friday? [E]",
            "Which lectures are there at 9 on Saturday? [E]",
            "Which lectures are there at 9 on Sunday? [E]",
            "Which lectures are there at 12 on Monday? [E]",
            "Which lectures are there at 12 on Tuesday? [E]",
            "Which lectures are there at 12 on Wednesday? [E]",
            "Which lectures are there at 12 on Thursday? [E]",
            "Which lectures are there at 12 on Friday? [E]",
            "Which lectures are there at 12 on Saturday? [E]",
            "Which lectures are there at 12 on Sunday? [E]",
            "Which lectures are there at 9? [E]",
            "Which lectures are there at 12? [E]",
            "Is it going to rain in Paris? [E]",
            "What is the population size of New York City? [E]",
            "Where can I find the nearest grocery store? [E]",
            "How long does it take to drive from London to Edinburgh? [E]",
            "What are the top tourist attractions in Rome? [E]",
            "Who is the president of France? [E]",
            "Which restaurants are open on Sundays in Amsterdam? [E]",
            "What time does the train to Barcelona depart? [E]",
            "Where can I buy tickets for the concert? [E]",
            "What is the cost of living in Tokyo? [E]",
            "Who won the last World Cup? [E]",
            "Which museums are free on weekdays in London? [E]",
            "What is the exchange rate between US dollars and Euros? [E]",
            "Where is the nearest bus stop? [E]",
            "How do I apply for a visa to Germany? [E]",
            "Who is the author of the book 'Pride and Prejudice'? [E]",
            "What time does the museum open on Saturdays? [E]",
            "What is the distance between Sydney and Melbourne? [E]",
            "Where can I find a good sushi restaurant in New York City? [E]",
            "Which airlines fly from Los Angeles to Tokyo? [E]",
            "What is the average temperature in San Francisco in July? [E]",
            "Who painted the Mona Lisa? [E]",
            "Where is the nearest pharmacy? [E]",
            "How do I get to the nearest subway station? [E]",
            "What is the capital of Brazil? [E]",
            "Which hotels have swimming pools in Las Vegas? [E]",
            "What time does the store close on weekdays? [E]",
            "Where is the nearest ATM? [E]",
            "How can I book a hotel room online? [E]",
            "What is the current time in Sydney, Australia? [E]",
            "Who composed the Symphony No. 9? [E]",
            "Which universities offer computer science programs? [E]",
            "What is the duration of the movie 'Inception'? [E]",
            "Where can I find parking near the city center? [E]",
            "How do I reset my password for the online banking service? [E]",
            "Who directed the film 'The Shawshank Redemption'? [E]",
            "What is the main export of Japan? [E]",
            "Where is the nearest post office? [E]",
            "How do I get to the nearest park? [E]",
            "What is the official language of Canada? [E]",
            "Which countries are part of the European Union? [E]"
    );


    public AutoCompletion(){
        nGramModel = new NGramModel();
        setStartWords();
        this.train(questions);
    }

    public void train(List<String> questions){
        nGramModel.train(questions, MAXNGRAMSIZE);
    }

    /**
     * Method that predicts the next word based on a piece of sentence
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

    /**
     * Method that goes through all questions and gets the
     * starting words of the question. No duplicates.
     */
    public void setStartWords(){
        for (String question : questions) {
            String[] words = question.split("\\s+"); // tokenize
            if (!startWords.contains(words[0])){
                startWords.add(words[0]); // add start word if it doesn't exist already
            }
        }
    }

    public List<String> getFinalSuggestions() {
        return finalSuggestions;
    }

    public List<String> getStartWords() {
        return startWords;
    }
}
