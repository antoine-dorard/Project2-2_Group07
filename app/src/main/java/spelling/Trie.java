package spelling;

import java.util.HashMap;
import java.util.Map;

/*
 * The TrieNode class represents a node in a Trie data structure. 
 * It has a map of children that maps each character to the next node, 
 * and a boolean flag indicating if the node represents a complete word.
 */
class TrieNode {
    Map<Character, TrieNode> children = new HashMap<>();
    boolean isWord;
}

/*
 * The Trie class implements a Trie data structure for efficient searching and
 * insertion of words.
 * It has a root node, which is a TrieNode.
 */
public class Trie {
    private TrieNode root;

    // Constructor that initializes the root node.
    public Trie() {
        root = new TrieNode();
    }

    /*
     * The insert method adds a word to the Trie.
     * It traverses the Trie, creating new nodes if necessary, until it reaches the
     * end of the word,
     * where it sets the isWord flag to true.
     */
    public void insert(String word) {
        TrieNode current = root;
        for (char c : word.toCharArray()) {
            current = current.children.computeIfAbsent(c, k -> new TrieNode());
        }
        current.isWord = true;
    }

    /*
     * The search method looks up a word in the Trie.
     * It traverses the Trie, following the path of the characters in the word,
     * and returns true if it finds a node at the end of the path with isWord set to
     * true.
     */
    public boolean search(String word) {
        TrieNode current = root;
        for (char c : word.toCharArray()) {
            TrieNode node = current.children.get(c);
            if (node == null) {
                return false;
            }
            current = node;
        }
        return current.isWord;
    }
}
