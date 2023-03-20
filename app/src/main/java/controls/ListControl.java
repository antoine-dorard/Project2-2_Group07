package controls;

import utils.ConfigUI;

import javax.swing.*;
import java.awt.*;

public class ListControl extends JList<String> {

    private final ConfigUI configUI = new ConfigUI();


    public ListControl(String[] listItems){
        super();

        // define a list model, which will work as skill selector.
        DefaultListModel<String> listModel = new DefaultListModel<>();

        // set this model to the JList (ListControl).
        setModel(listModel);

        // define the list items and call the setup method for the JList.
        //String[] listItems = {"Math", "History", "Calendar", "etc"};

        // add all items, provided in the input, to the current JList.
        for (String item :  listItems) {listModel.addElement(item);}
        System.out.println("added");

        // define the list appearance and functionality.
        setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        setLayoutOrientation(JList.VERTICAL);
        setVisibleRowCount(-1);
        setBackground(configUI.colorListBG);
        setForeground(configUI.colorListFG);
        setFont(configUI.fontList);
        setSelectionBackground(configUI.colorListSelectionBG);
        setSelectionForeground(configUI.colorListSelectionFG);
        setPreferredSize(new Dimension(200, 550));

        // define the list scroller for the list control.
        JScrollPane listScroller = new JScrollPane(this);
        listScroller.setPreferredSize(new Dimension(250, 80));

    }

}

