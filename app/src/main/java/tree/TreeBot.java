package tree;

import main.ChatBot;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.util.Iterator;

public class TreeBot {
    private String file = "app/src/TimeTableSkill.json";
    private String keyword = "questions";
    private String response;

    public TreeBot(){
        JSONParser parser = new JSONParser(); // create a json parser

        // read the file and parse the data
        try { // create a file reader
            System.out.println("hello");
            Object obj = parser.parse(new FileReader(file));
            JSONObject jsonObject = (JSONObject)obj;
            JSONArray subjects = (JSONArray)jsonObject.get(keyword);

            Iterator iterator = subjects.iterator();
            while (iterator.hasNext()) {
                System.out.println(iterator.next());
            }
        } catch (Exception e) { // catch any IO or parse exceptions
            System.out.println("Sorry, I don't have information on that topic.");
        }
    }
    public void run(){

    }


    public static void main(String[] args) {
        TreeBot chatBot = new TreeBot(); // create a new chatbot bipbip
        chatBot.run(); // run the chatbot bopbop
    }
}
