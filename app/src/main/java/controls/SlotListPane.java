package controls;

import org.json.simple.JSONObject;
import utils.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
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

    private void addSlot(String[] strList, String[] valueList, String action){
        SlotListControl slotListCtrl = new SlotListControl();
        slotListCtrl.setBackground(configUI.colorPanelBG);
        int i = 0;
        for (String name: strList){
            slotListCtrl.addSlot(name, valueList[i]);
            i = i + 1;
        }
        slotListCtrl.addSlot("Action", action);
        gbc.gridx = 1;
        gbc.gridy = getComponentCount();
        add(slotListCtrl, gbc);
        //IMPORTANT
        revalidate();
        repaint();

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

    public void addNewSlot(){
        // names string[] is same as the first component (and all others).
        String[] names = ((SlotListControl) getComponents()[0]).names.toArray(new String[0]);
        // values will be empty, but of equal length to names.
        String[] values = new String[names.length];
        Arrays.fill(values, "");
        // add the slot.
        addSlot(names, values, "");
    }

    public JSONObject createJSONObject(final ArrayList<ArrayList<String>> slotKeysLists, final ArrayList<ArrayList<String>> slotValuesLists, final ArrayList<String> valuesList) {
        if (valuesList.size() != slotKeysLists.size()) {
            throw new IllegalArgumentException("The number of values does not match the number of slots/keys lists");
        }

        JSONObject obj = new JSONObject();
        for (int i = 0; i < slotKeysLists.size(); i++) {
            ArrayList<String> slotsList = slotKeysLists.get(i);
            ArrayList<String> keysList = slotValuesLists.get(i);
            Object value = valuesList.get(i);

            JSONObject currentObj = obj;
            for (int j = 0; j < slotsList.size(); j++) {
                String slot = slotsList.get(j);
                String key = keysList.get(j);
                if (j == slotsList.size() - 1) {
                    currentObj.put(slot + " " + key, value);
                } else {
                    JSONObject newObj = (JSONObject) currentObj.get(slot + " " + key);

                    if (newObj == null) {
                        newObj = new JSONObject();
                        currentObj.put(slot + " " + key, newObj);
                    }
                    currentObj = newObj;
                }
            }
        }
        return obj;
    }

    /**
     * The code below is capable of parsing a JSON object of a certain question's corresponding
     * action (answer) and generating a lists of slots and keys that describe the path to each value.
     * From the populate() method above in the code, you can call the generate() method below to fill in the
     * slot list on the UI (a better name for the generate() method may be found when implemented in the actual code).
     * @param json
     */
    public void generate(JSONObject json){
        // the following two lists contain the slots and keys respectively in the order they appear in the JSON until a
        // value is reached. They are cleared after each value is reached, so actions must be taken to create the slot
        // list on the UI when each value is reached.
        ArrayList<String> slotsList = new ArrayList<>();
        ArrayList<String> keysList = new ArrayList<>();
        String defaultValue = "";
        removeAll();
        traverse(json, slotsList, keysList, defaultValue);
    }

    private void traverse(JSONObject obj, ArrayList<String> slotsList, ArrayList<String> keysList, String defaultValue) {
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

                    addSlot(slotsList.toArray(new String[0]), keysList.toArray(new String[0]), (String) value);

                    // #########

                    slotsList.remove(slotsList.size()-1);
                    keysList.remove(keysList.size()-1);
                }
            }
        }
    }

}
