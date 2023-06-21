package backend.zeromq;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ZMQMessage {

    private HashMap<String, Object> hashMap = new HashMap<>();


    public ZMQMessage(String cmd, Object data){

        hashMap.put("cmd", cmd);
        hashMap.put("data", data);

    }

    public void loadFromJSONString(String jsonString) throws ParseException {

        // Convert string to a JSON Object.
        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(jsonString);

        // Update attributes from the JSON Object.
        hashMap.put("cmd", json.get("cmd"));
        hashMap.put("data", json.get("data"));

    }
    
    public String convertToJSONString() throws Exception {

        // First convert HashMap to a new 'converted' state, that is acceptable for sending.
        HashMap<String, Object> converted = getConvertedHashMap();

        // Convert ZMQMessage to JSON Object.
        JSONObject jsonObject = new JSONObject(converted);

        // Return the JSON Message as String.
        return jsonObject.toString();
    }

    public String getCommand(){
        return (String) hashMap.get("cmd");
    }

    public Object getData(){
        return hashMap.get("data");
    }

    public String getDataAsString(){
        // Convert Object to String here....
        return (String) getData();
    }

    public Boolean isEmpty(){
        return getCommand().equals("");
    }


    private HashMap<String, Object> getConvertedHashMap() throws Exception {

        Object data = hashMap.get("data");

        // Check if we can convert it to a String.
        if (data instanceof String) {
            // Nothing has to happen here...
            data = (String) data;
        }
        else if (data instanceof Integer) {
            data = (Integer) data;
        }
        else if (data instanceof Long) {
            data = (Long) data;
        }
        else if (data instanceof JSONArray) {

            // Data Object received is a JSONArray.
            JSONArray arr = (JSONArray) data;

            System.out.println(data.getClass());

            // Check for what kind of array it is.
            if(arr.size() > 0) {

                System.out.println(arr.get(0).getClass());
                if(arr.get(0) instanceof Integer) {

                    // Integer array.
                    List<Integer> list = new ArrayList<>();
                    for (int i = 0; i < arr.size(); i++){
                        list.add((Integer) arr.get(i));
                    }
                    data = list;
                }
                else if(arr.get(0) instanceof Long) {

                    // Integer array.
                    List<Long> list = new ArrayList<>();
                    for (int i = 0; i < arr.size(); i++){
                        list.add((Long) arr.get(i));
                    }
                    data = list;
                }
                else if (arr.get(0) instanceof String) {

                    // String array.
                    List<String> list = new ArrayList<>();
                    for (int i = 0; i < arr.size(); i++){
                        list.add((String) arr.get(i));
                    }
                    data = list;
                }
                else {

                    // Unknown array type, just convert to String.
                    List<String> list = new ArrayList<>();
                    for (int i = 0; i < arr.size(); i++){
                        list.add(arr.get(i).toString());
                    }
                    data = list;
                }
            }
        }
        else {
            // Unknown instance of 'data'.
            throw new Exception("Error in 'convertDataToString' method... " +
                    "instance of input 'data' cannot be converted to String type...");
        }

        // Assign new value of data to a new HashMap.
        HashMap<String, Object> newHashMap = (HashMap<String, Object>) hashMap.clone();
        newHashMap.put("data", data);

        return newHashMap;
    }


}
