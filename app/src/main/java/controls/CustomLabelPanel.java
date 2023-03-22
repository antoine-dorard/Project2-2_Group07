package controls;

import utils.ConfigUI;

import javax.swing.*;
import java.awt.*;

public class CustomLabelPanel extends JPanel {

    public JPanel spacingPane = new JPanel();

    private final ConfigUI configUI = new ConfigUI();
    JLabel listLabel = new JLabel();

    public CustomLabelPanel(JComponent control, String labelText, int vGap, int hGap, int labelGap){
        super();
        setupSpacingWithLabel(control, labelText, vGap, hGap, labelGap);
    }

    private void setupSpacingWithLabel(JComponent ctrl, String lblTxt, int vGap, int hGap, int labelGap){

        // define a layout for the label.
        BoxLayout spacingLayout = new BoxLayout(spacingPane, BoxLayout.Y_AXIS);
        spacingPane.setLayout(spacingLayout);
        spacingPane.setOpaque(true);

        // define another layout for the horizontal spacing.
        BoxLayout listLayout = new BoxLayout(this, BoxLayout.X_AXIS);
        setLayout(listLayout);
        setOpaque(true);

        // create the label with the name.
        listLabel.setFont(configUI.fontList);
        listLabel.setForeground(new Color(255,255,255,100));
        listLabel.setText(lblTxt);
        listLabel.setOpaque(true);
        listLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));

        // add all elements to pane, also some empty spacers
        spacingPane.add(Box.createVerticalStrut(vGap));
        spacingPane.add(listLabel);
        spacingPane.add(Box.createVerticalStrut(labelGap));
        spacingPane.add(ctrl);
        add(Box.createHorizontalStrut(hGap));
        add(spacingPane);

        // set both alignments to the left.
        listLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        ctrl.setAlignmentX(Component.LEFT_ALIGNMENT);

        // set both alignments to the top.
        spacingPane.setAlignmentY(Component.TOP_ALIGNMENT);
        setAlignmentY(Component.TOP_ALIGNMENT);
    }

    @Override
    public void setBackground(Color bg) {
        super.setBackground(bg);
        if (listLabel!=null){
            spacingPane.setBackground(bg);
            listLabel.setBackground(bg);
        }
    }

    public void addWithGap(JComponent component, int gap){
        spacingPane.add(Box.createVerticalStrut(gap));
        component.setAlignmentX(Component.LEFT_ALIGNMENT);
        spacingPane.add(component);
    }
}
