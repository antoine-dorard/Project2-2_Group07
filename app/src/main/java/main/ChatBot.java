package main;

//I think these should be the main imports
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

//Skele code for the main class skrrskrr
public class ChatBot {
  private Scanner in; // scanner to read user input
  private JSONArray conversation; // jsonarray to store the conversation
  private static final String CONVERSATION_FILE = "conversation.json"; // file to store the conversation
  private Map<String, String> keywordToFileMap; // map keywords to the files

  public ChatBot() {
    in = new Scanner(System.in); // create a scanner so we can read the command-line input
    conversation = new JSONArray(); // create a jsonarray to store the conversation
    retrieveConversation(); // grab the convo history from a file

    // just maped some random keywords and their corresponding files for now
    keywordToFileMap = new HashMap<>(); // create a map to store keywords and their corresponding files

    // Here under: map the keyword "calendar" to the file "calendar_data.json"
    keywordToFileMap.put("calendar", "calendar_data.json");
    keywordToFileMap.put("sport", "sport_data.json"); // same as above
    keywordToFileMap.put("history", "history_data.json"); // same as above and above
    keywordToFileMap.put("math", "math_data.json"); // same as above and above and above
  }

  // main loop of the chatbot
  public void run() {
    while (true) { // loop forever (it's gonna break when the user types "exit")

      System.out.print("You: "); // I hope you understand this
      String input = in.nextLine(); // read user input

      // check if the user wants to exit
      if (input.equals("exit")) { // check if the user typed "exit"
        break;
      }

      String response = generateResponse(input); // generate a response
      System.out.println("ChatBot: " + response); // print ze response

      addToConversation(input, response); // add the user input and the response to ze conversation
    }

    writeConversationToFile(); // write the conversation to a file
  }

  // Now we need to create a response to the user input
  private String generateResponse(String input) {
    String[] words = input.split("\\s+"); // split the input into words (split on whitespace, i think it's the good regex, not sure tho)

    // check if any of the words are keywords
    for (String word : words) { // loop through the words
      if (keywordToFileMap.containsKey(word)) { // check if the word is a keyword
        return retrieveDataFromFile(word); // retrieve data from the file
      }
    }

    // Big brain code here that generates a response according to the input 

    return null; // ofc we should return the actual response here, didnt implement it yet
  }

  // retrieve data from a file
  private String retrieveDataFromFile(String keyword) {
    String file = keywordToFileMap.get(keyword); // get the file name from the keyword
    JSONParser parser = new JSONParser(); // create a json parser

    // read the file and parse ze data
    try (FileReader reader = new FileReader(file)) { // create a file reader
      Object obj = parser.parse(reader); // parse the file
      JSONObject data = (JSONObject) obj; // cast the object to a jsonobjext

      return (String) data.get(keyword); // return the data
    } catch (IOException | ParseException e) { // catch any IO or parse exceptions
      return "Sorry, I don't have information on that topic."; // Ig you know what this does lol
    }
  }

  @SuppressWarnings("unchecked") // suppress warnings about unchecked casts, since we know the types are correct, sometimes its annoying
  // add the user input and the response to the conversation
  private void addToConversation(String input, String response) {
    JSONObject message = new JSONObject(); // create a new JSONObject to store the message
    message.put("user", input); // add the user input to the message
    message.put("chatbot", response); // add the resp to the message
    conversation.add(message); // add the message to the convooo
  }

  // retrieve the conversation history from a file
  private void retrieveConversation() {
    JSONParser parser = new JSONParser(); // create a JSON parser

    // read the file and parse the data
    try (FileReader reader = new FileReader(CONVERSATION_FILE)) { // create a file reader
      Object obj = parser.parse(reader); // parse the file
      conversation = (JSONArray) obj; // cast the object to a JSONArray
    } catch (IOException | ParseException e) { // catch any IO or parse exceptions
      System.out.println("Failed to retrieve conversation history: " + e.getMessage()); // Ig you know what this does
                                                                                        // lol (again)
    }
  }

  // write the conversation to a file
  private void writeConversationToFile() {
    try (FileWriter writer = new FileWriter(CONVERSATION_FILE)) { // create a file writer
      writer.write(conversation.toJSONString()); // write the conversation to the file
    } catch (IOException e) { // catch any IO exceptions
      System.out.println("Failed to write conversation history: " + e.getMessage()); // Ig you know what this does
                                                                                     // (extra lol)
    }
  }

  public static void main(String[] args) {
    ChatBot chatBot = new ChatBot(); // create a new chatbot bipbip
    chatBot.run(); // run the chatbot bopbop
  }
}
