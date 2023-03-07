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
    private ArrayList<JSONArray> slots;
    private ArrayList<String> slotsKeys;
    private HashMap<String, String> placeHolders;
    private boolean DEBUG = false;
    
    public String processInput(String input) {
        JSONParser parser = new JSONParser(); // create a json parser
        try{
            String[] words = input.split("\\s+");
            Object obj = parser.parse(new FileReader(file));
            JSONObject jsonObject = (JSONObject)obj;


            JSONObject subjects = (JSONObject) jsonObject.get("slots");
            JSONArray dayArray = (JSONArray) subjects.get("DAY");
            slots = new ArrayList();
            slotsKeys = new ArrayList<>();
            placeHolders = new HashMap<>();
            if(DEBUG)System.out.println(subjects);
            Iterator<String> keysIter = subjects.keySet().iterator();


            while(keys.hasNext()) {
                String key = keys.next();
                slots.add((JSONArray) subjects.get(key));
            }
            for (int i = 0; i < slots.size(); i++) {
                to:
                for (int j = 0; j < words.length; j++) {
                    for (int k = 0; k < (slots.get(i)).size(); k++) {
                        //System.out.println(i + " " + j + " " + k + " ");
                        if(words[j].equals((slots.get(i)).get(k))){
                            placeHolders.put(slotsKeys.get(i), words[j]);
                            if(DEBUG)System.out.println("this is a keyword " + words[j]);
                            if(DEBUG)System.out.println(slots.get(i).size());
                            break to;
                        }
                    }
                }
            }
            JSONObject actions = (JSONObject) jsonObject.get("actions");
            //System.out.println(((JSONObject)actions.get(placeHolders.get(1))).get(placeHolders.get(0)));
//            for (Map.Entry placeholder : placeHolders.entrySet()) {
//                JSONObject temp = (JSONObject) actions.get(placeholder.getValue());
//                actions = temp;
//
//                System.out.println("this si loop " + actions);
//            }

            System.out.println("Placeholders:");
            System.out.println(placeHolders.get("DAY") + "  " + placeHolders.get("TIME"));
            System.out.println("Actions:");
            System.out.println(actions);

            // to associate the placeholder with all the key
            /*for (Map.Entry placeholder : placeHolders.entrySet()) {
                for (Map.Entry action : actions.entrySet()){

                }
            }*/



        }
        catch(Exception IO){
            System.out.println("There is a problem with the code");
        }
        return null;
    }
}
