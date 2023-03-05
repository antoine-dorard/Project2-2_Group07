package backend;

import jdk.swing.interop.SwingInterOpUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class CFG_InputProcessor implements InputProcessor {

    private String file = "./app/TimeTableSkill.json";
    private String DAY;
    private String TIME;
    private ArrayList<JSONArray> slots;
    private ArrayList<String> placeHolders;
    
    public String processInput(String input) {
        JSONParser parser = new JSONParser(); // create a json parser
        try{
            String[] words = input.split("\\s+");
            Object obj = parser.parse(new FileReader(file));
            JSONObject jsonObject = (JSONObject)obj;

            JSONObject subjects = (JSONObject) jsonObject.get("slots");
            slots = new ArrayList();
            placeHolders = new ArrayList<>();
            System.out.println(subjects);
            Iterator<String> keys = subjects.keySet().iterator();

            while(keys.hasNext()) {
                String key = keys.next();
                slots.add((JSONArray) subjects.get(key));
            }
            for (int i = 0; i < slots.size(); i++) {
                to:
                for (int j = 0; j < words.length; j++) {
                    for (int k = 0; k < (slots.get(i)).size(); k++) {
                        System.out.println(i + " " + j + " " + k + " ");
                        if(words[j].equals((slots.get(i)).get(k))){
                            placeHolders.add(words[j]);
                            System.out.print("saucisse ");
                            System.out.println(words[j]);
                            break to;
                        }
                    }
                }
            }
            placeHolders.size();

        }
        catch(Exception IO){
            System.out.println("blabla");
        }
        return null;
    }
}
