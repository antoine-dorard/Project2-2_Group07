package controls;

import utils.ConfigUI;

import javax.swing.*;
import java.awt.*;

public class ListControl extends JList<String> {

    public DefaultListModel<String> listModel;

    public JScrollPane listScroller = new JScrollPane();


    public ListControl(int cellWidth, int cellHeight){
        super();
        ConfigUI configUI = new ConfigUI();

        //prefixStr = prefix;

        UIManager.put("List.focusCellHighlightBorder", BorderFactory.createEmptyBorder());

        // create a list model.
        listModel = new DefaultListModel<String>(); //newModel();
        // set this model to the JList (ListControl).
        setModel(listModel);


        // define the list appearance and functionality.
        setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        setLayoutOrientation(JList.VERTICAL);
        setVisibleRowCount(-1);
        setBackground(configUI.colorListBG);
        setForeground(configUI.colorListFG);
        setFont(configUI.fontList);
        setSelectionBackground(configUI.colorListSelectionBG);
        setSelectionForeground(configUI.colorListSelectionFG);
        setFixedCellHeight(cellHeight);
        setFixedCellWidth(cellWidth);

        // define the list scroller for the list control.
        listScroller.setViewportView(this);
        setLayoutOrientation(JList.VERTICAL);
        listScroller.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
    }

    public void setListItems(String[] listItems){
        // firstly remove all elements.
        listModel.removeAllElements();
        // add all items, provided in the input, to the current JList.
        for (String item :  listItems) {listModel.addElement(item);}
    }

    /*
    private DefaultListModel<String> newModel(){
        // define a list model, which will work as skill selector.
        DefaultListModel<String> listModel = new DefaultListModel<>(){
            @Override
            public void addElement(String element) {
                if (prefix.equals("##")){
                    super.addElement(String.format("%02d ) ", super.getSize()+1) + element); // prefix is number.
                }
                else{
                    super.addElement(prefix + element); // add prefix.
                }
            }
        };
        return listModel;
    }

    @Override
    public String getSelectedValue() {
        int beginIndex;
        if (super.getSelectedValue()!=null) {
            if (prefixStr.equals("##")) {
                beginIndex = 5;
            } // number prefix always has length 5.
            else {
                beginIndex = prefixStr.length();
            } // get the length of the prefix.
            return (super.getSelectedValue().substring(beginIndex)); // remove prefix.
        }
        else{
            return null;
        }
    }*/
}

