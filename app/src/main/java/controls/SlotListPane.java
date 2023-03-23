package controls;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import utils.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class SlotListPane extends JPanel {

    ConfigUI configUI = new ConfigUI();
    public JScrollPane scrollPane = new JScrollPane();
    public GridBagConstraints gbc = new GridBagConstraints();

    public SlotListPane(){
        super();
        GridBagLayout layout = new GridBagLayout();
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.weighty = 1;
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = java.awt.GridBagConstraints.RELATIVE;
        gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        setLayout(layout);

        // define a scroll pane for scrolling between slots
        scrollPane.setViewportView(this);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0,30,0,30));
    }


    @Override
    public void setBackground(Color bg) {
        super.setBackground(bg);
        System.out.println("test");
        if(scrollPane!=null){
            System.out.println("test2");
            scrollPane.setBackground(bg);
            for (Component comp: getComponents()){
                comp.setBackground(bg);
            }
        }
    }

    public void addSlot(String[] strList){
        SlotListControl slotListCtrl = new SlotListControl();
        slotListCtrl.setBackground(configUI.colorPanelBG);
        for (String name: strList){
            slotListCtrl.addSlot(name);
        }
        gbc.gridx = 1;
        gbc.gridy = getComponentCount();
        add(slotListCtrl, gbc);

        slotListCtrl.removeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JButton o = (JButton)actionEvent.getSource();
                SlotListControl s = (SlotListControl)o.getParent();
                remove(s);
                //IMPORTANT
                revalidate();
                repaint();
            }
        });
    }

    public void populate(JSONObject jsonObject){

        HashMap<String, ArrayList<String>> map = new HashMap<>();

        createSlotList(jsonObject, map);

//        System.out.println(map.keySet());
//        System.out.println(map.values());
    }

    private void createSlotList(JSONObject jsonObject, HashMap<String, ArrayList<String>> map){
        for (Object keyStr : jsonObject.keySet()) {
            String[] splitArray = ((String) keyStr).split(" ");
            if(splitArray.length>=2){
                if(map.containsKey(splitArray[0])){
                    ArrayList<String> newAr = map.get(splitArray[0]);
                    newAr.add(splitArray[1]);
                    map.replace(splitArray[0], newAr);
                }
                else{
                    ArrayList<String> ar = new ArrayList<String>();
                    ar.add(splitArray[1]);
                    map.put(splitArray[0], ar);
                }
            }
            try {
                JSONObject newJSONObject = (JSONObject) jsonObject.get(keyStr);
                createSlotList(newJSONObject, map);
            }
            catch (Exception ignore){
                String newValue = (String) jsonObject.get(keyStr);
                if(map.containsKey("actions")){
                    ArrayList<String> newAr = map.get("actions");
                    newAr.add(newValue);
                    map.replace("actions", newAr);
                } else {
                    ArrayList<String> ar = new ArrayList<String>();
                    ar.add(newValue);
                    map.put("actions", ar);
                }
            }
        }
    }

    public void convertSlotListToTree(){

    }

    /**
     * Test method. The code below is capable of parsing a JSON object of a certain question's corresponding
     * action (answer) and generating a lists of slots and keys that describe the path to each value.
     * From the populate() method above in the code, you can call the generate() method below to fill in the
     * slot list on the UI (a better name for the generate() method may be found when implemented in the actual code).
     * The main method and its content can be deleted when the code has been implemented.
     * @param args
     */
    public static void main(String[] args) {
        String jsonString = "{\"DAY Monday\":{\"TIME 11\":\"TCS\",\"TIME 13\":\"MM\",\"TIME 16\":\"HCI\"},\"DAY Tuesday\":{\"TIME 11\":\"ABC\",\"TIME 13\":\"MM\",\"TIME 16\":\"HCI\"},\"default\":\"You don't have any class at this time!\"}";
        JSONParser parser = new JSONParser();
        JSONObject json = null;
        try {
            json = (JSONObject) parser.parse(jsonString);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        generate(json);
    }

    private static void generate(JSONObject json){
        // the following two lists contain the slots and keys respectively in the order they appear in the JSON until a
        // value is reached. They are cleared after each value is reached, so actions must be taken to create the slot
        // list on the UI when each value is reached.
        ArrayList<String> slotsList = new ArrayList<>();
        ArrayList<String> keysList = new ArrayList<>();
        String defaultValue = "";
        traverse(json, slotsList, keysList, defaultValue);
    }

    private static void traverse(JSONObject obj, ArrayList<String> slotsList, ArrayList<String> keysList, String defaultValue) {
        for (Object key : obj.keySet()) {
            Object value = obj.get(key);
            if (value instanceof JSONObject) {
                String[] splitArray = ((String) key).split(" ");
                slotsList.add(splitArray[0]);
                keysList.add(splitArray[1]);

                traverse((JSONObject) value, slotsList, keysList, defaultValue);

                slotsList.remove(slotsList.size()-1);
                keysList.remove(keysList.size()-1);
            } else { // value is reached
                if(key.equals("default")){
                    defaultValue = (String) value;
                }else {
                    String[] splitArray = ((String) key).split(" ");
                    slotsList.add(splitArray[0]);
                    keysList.add(splitArray[1]);

                    // #########
                    // This is where you can do something with the slotsList,  keysList, defaultValue and value
                    // Instead of printing them out, you can use them to create the slot lists on the UI
                    //
                    // Example:
                    //
                    // === Values ===
                    // slotsList = ["DAY", "TIME"]
                    // keysList = ["Monday", "11"]
                    // value = "TCS"
                    // defaultValue = "You don't have any class at this time!"
                    // ==============
                    //
                    // Output on the UI (in the skill editor under the question text field):
                    //
                    // DAY     TIME  |  Value
                    // Monday  11    |  TCS
                    //

                    System.out.println(slotsList + "\n" + keysList + "\n" + value);
                    System.out.println();

                    // #########

                    slotsList.remove(slotsList.size()-1);
                    keysList.remove(keysList.size()-1);
                }
            }
        }
    }

}
