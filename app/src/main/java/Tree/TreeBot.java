package Tree;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;

public class TreeBot {
    private String file = "./app/TimeTableSkill.json";
    private String keyword = "slots";
    private String response;

    public TreeBot(){
        JSONParser parser = new JSONParser(); // create a json parser

        // read the file and parse the data
        try { // create a file reader

            Object obj = parser.parse(new FileReader(file));
            JSONObject jsonObject = (JSONObject)obj;

            JSONObject subjects = (JSONObject) jsonObject.get("slots");
            JSONArray dayArray = (JSONArray) subjects.get("DAY");
            JSONArray timeArray = (JSONArray) subjects.get("TIME");


            Node root = new Node("root","Hello you can ask me anything",null);

            for(Object day : dayArray){
                Node child = new Node((String) day,"the day is " + (String) day,root);
                root.children.add(child);
                System.out.println(child.name);

                for(Object time : timeArray){
                    String timeConv = time+"";
                    Node subchild = new Node(timeConv, "the time is "+ timeConv,child);
                    child.children.add(subchild);
                }
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
