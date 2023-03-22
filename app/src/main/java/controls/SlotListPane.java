package controls;

import org.json.simple.JSONObject;
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

        System.out.println(map.keySet());
        System.out.println(map.values());
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

}
