1) implement a button to activate or not the spelling checker
2) if the spelling checker is activated, return the 'corrected' text in the UI chat


Tests: 
- Simulation: Create a simulation that generates a set of predetermined user inputs with typos and misspelled words, 
and compare the performance of your chatbot with and without the spelling checker. You can measure the chatbot's accuracy,
precision, and recall in recognizing and correcting the typos and misspelled words.    

- Time-based experiments: Conduct experiments that measure the time taken to process a set of predetermined inputs with 
and without the spelling checker. You can measure the time taken to recognize and correct the typos and misspelled words 
and compare it to the time taken by the chatbot without the spelling checker.

This Java code consists of three classes: Trie, SpellingCorrector, and mainLauncher. 
The code is designed to implement a spelling checker and corrector. 
Here's a summary of what each class does:

Trie class:
The Trie class implements a Trie data structure for efficient searching and insertion of words. 
It contains a TrieNode inner class, which represents a node in the Trie. 
Each TrieNode contains a map of children (character-to-TrieNode) and a boolean flag indicating if the node represents a complete word. 
The Trie class provides methods to insert words and search for words in the Trie.

SpellingCorrector class:
The SpellingCorrector class is responsible for correcting spelling mistakes in a given text using a dictionary. 
It loads the dictionary from a text file into a Trie data structure for efficient lookup. 
The class uses the Levenshtein Distance algorithm to find the closest matching word in the dictionary for each incorrect word in the input text. 
It provides methods to load the dictionary, correct the spelling, find the closest matching word, and calculate the Levenshtein Distance between two strings.

mainLauncher class:
The mainLauncher class serves as the driver code for the spelling corrector application. 
It generates a set of questions with spelling mistakes based on a specified probability and corrects these mistakes using the SpellingCorrector class. 
The mainLauncher class provides methods to generate spelling mistakes, create random questions, and print 
a table with generated mistakes and their corrections for a range of probabilities.

The main method in the mainLauncher class demonstrates the usage of the Trie and SpellingCorrector classes. 
It generates a set of questions, introduces spelling mistakes based on different probabilities, 
and corrects these mistakes using the SpellingCorrector class. 
The main method prints a table that shows the original questions, spelling mistakes, and corrected phrases for different probabilities.
