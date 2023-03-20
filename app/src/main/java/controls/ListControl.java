package controls;

import utils.ConfigUI;

import javax.swing.*;
import java.awt.*;

public class ListControl extends JList<String> {

    private final ConfigUI configUI = new ConfigUI();

    public JPanel listPane = new JPanel();
    public JPanel spacingPane = new JPanel();

    public ListControl(String name, String[] listItems, int width, int height){
        super();
        setName(name);

        UIManager.put("List.focusCellHighlightBorder", BorderFactory.createEmptyBorder());

        // create a list model.
        DefaultListModel<String> listModel = newModel();

        // set this model to the JList (ListControl).
        setModel(listModel);

        // add all items, provided in the input, to the current JList.
        for (String item :  listItems) {listModel.addElement(item);}

        // define the list appearance and functionality.
        setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        setLayoutOrientation(JList.VERTICAL);
        setVisibleRowCount(-1);
        setBackground(configUI.colorListBG);
        setForeground(configUI.colorListFG);
        setFont(configUI.fontList);
        setSelectionBackground(configUI.colorListSelectionBG);
        setSelectionForeground(configUI.colorListSelectionFG);
        setFixedCellHeight(25);
        setFixedCellWidth(width-25);
        setPreferredSize(new Dimension(width, height));

        // define the list scroller for the list control.
        JScrollPane listScroller = new JScrollPane(this);
        listScroller.setPreferredSize(new Dimension(250, 80));

        // set up a panel with a label
        setupLabel();
    }

    public JPanel getListPane() {
        return spacingPane;
    }

    private DefaultListModel<String> newModel(){
        // define a list model, which will work as skill selector.
        DefaultListModel<String> listModel = new DefaultListModel<>(){
            @Override
            public void addElement(String element) {
                super.addElement(" " + element); // add some spacing.
            }
            @Override
            public String elementAt(int index) {
                return (super.elementAt(index).substring(1)); // remove the spacing.
            }
        };
        return listModel;
    }

    private void setupLabel(){

        // define a layout for the label.
        BoxLayout listLayout = new BoxLayout(listPane, BoxLayout.Y_AXIS);
        listPane.setLayout(listLayout);
        listPane.setBackground(new Color(63,63,63));
        listPane.setOpaque(true);

        // define another layout for the horizontal spacing.
        BoxLayout spacingLayout = new BoxLayout(spacingPane, BoxLayout.X_AXIS);
        spacingPane.setLayout(spacingLayout);
        spacingPane.setBackground(new Color(63,63,63));
        spacingPane.setOpaque(true);

        // create the label with the name.
        JLabel listLabel = new JLabel();
        listLabel.setFont(configUI.fontList);
        listLabel.setForeground(new Color(255,255,255,100));
        listLabel.setText(this.getName());
        listLabel.setBackground(new Color(63,63,63));
        listLabel.setOpaque(true);
        listLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));

        // set both alignments to the left.
        listLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        this.setAlignmentX(Component.LEFT_ALIGNMENT);

        // add all elements to pane, also some empty spacers
        listPane.add(Box.createVerticalStrut(30));
        listPane.add(listLabel);
        listPane.add(Box.createVerticalStrut(30));
        listPane.add(this);
        spacingPane.add(Box.createHorizontalStrut(30));
        spacingPane.add(listPane);
        // set both alignments to the top.
        listPane.setAlignmentY(Component.TOP_ALIGNMENT);
        spacingPane.setAlignmentY(Component.TOP_ALIGNMENT);
    }

}

