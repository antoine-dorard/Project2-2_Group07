package panels;
import controls.MyHeaderPane;
import controls.MyLineButton;
import main.SkillData;
import utils.*;
import utils.UIFonts.*;
import utils.UIColors.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;


public class SkillEditorPanel extends JPanel{

    private final UIFonts fonts = new UIFonts();
    private final UIColors colors = new UIColors();

    private CardLayout cardLayout;
    private JPanel cardPanel;
    EditorSkillsPanel skillsPanel;
    EditorRulesPanel rulesPanel;
    EditorActionsPanel actionsPanel;

    SkillData skillData = new SkillData();



    public SkillEditorPanel(JFrame ownerFrame){
        super();
        BorderLayout layout = new BorderLayout();
        this.setLayout(layout);

        // set the background of this JPanel.
        setBackground(colors.getColor(UIColor.BG_PRIMARY));

        // create the headers and selector buttons in separate JPanels.
        //JPanel headerPanel = createHeaderPanel(
        MyHeaderPane headerPanel = new MyHeaderPane(
                "Skill Editor",
                "Here you can create, edit and delete your skills and its attributes.",
                false
        );
        JPanel flowPanel = createSelectorPanel(
                new String[]{"Skills", "Rules", "Actions"}
        );

        // create the top JPanel, which contains the Header- and SelectorPanel.
        JPanel topPanel = createTopPanel(headerPanel, flowPanel);

        // create spacers of 10 pixels.
        Component vSpacer = Box.createVerticalStrut(10);
        Component hSpacerL = Box.createHorizontalStrut(10);
        Component hSpacerR = Box.createHorizontalStrut(10);

        // create the JPanel with the switching CardLayout.
        JPanel cardPanel = createCenterPanel(ownerFrame);

        // add everything to the current JPanel.
        this.add(topPanel, BorderLayout.NORTH);
        this.add(vSpacer, BorderLayout.SOUTH);
        this.add(hSpacerL, BorderLayout.WEST);
        this.add(hSpacerR, BorderLayout.EAST);
        this.add(cardPanel, BorderLayout.CENTER);
    }


    private void switchPanel(String name) {
        if (name.equals("Skills")){
            skillData.loadSkills();
            skillsPanel.updateAll(skillData);
        }
        if (name.equals("Rules")){
            skillData.loadRules();
            rulesPanel.updateAll(skillData);
        }
        if (name.equals("Actions")){
            skillData.loadActions();
            actionsPanel.updateAll(skillData);
        }
        cardLayout.show(cardPanel, name);
    }

    private JPanel createCenterPanel(JFrame ownerFrame) {
        // create the JPanel with the switching CardLayout.
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        skillsPanel = new EditorSkillsPanel(ownerFrame);
        rulesPanel = new EditorRulesPanel(ownerFrame);
        actionsPanel = new EditorActionsPanel(ownerFrame);

        cardPanel.add(skillsPanel, "Skills");
        cardPanel.add(rulesPanel, "Rules");
        cardPanel.add(actionsPanel, "Actions");

        // default show the first panel.
        switchPanel("Skills");

        return cardPanel;
    }

    private JPanel createTopPanel(JPanel headerPanel, JPanel selectorPanel) {
        // initialize the panel and its layout.
        JPanel panel = new JPanel(new BorderLayout());

        // set the background of the JPanel.
        panel.setBackground(colors.getColor(UIColor.BG_PRIMARY));

        // add two input panels into this JPanel.
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(selectorPanel);

        return panel;
    }


    private JPanel createSelectorPanel(String[] btnNames) {
        // initialize the panel and its layout.
        FlowLayout layout = new FlowLayout(FlowLayout.LEFT);
        JPanel panel = new JPanel(layout);

        // set spacing between all elements.
        layout.setHgap(10);
        layout.setVgap(0);

        // create a border, with only the bottom part visible (divider).
        panel.setBorder(BorderFactory.createMatteBorder(
                0, 0, 1, 0,
                colors.getColor(UIColor.BORDER_GREY)));
        // set the background of the panel
        panel.setBackground(colors.getColor(UIColor.BG_PRIMARY));

        // initialize a list to save all buttons in.
        ArrayList<MyLineButton> buttons = new ArrayList<>();

        // create all the buttons.
        for(String name : btnNames) {
            MyLineButton btn = new MyLineButton(name);
            // add to the current JPanel.
            panel.add(btn);
            // add to the 'buttons' ArrayList.
            buttons.add(btn);
        }

        // loop through all buttons again, to set their functionality.
        for(MyLineButton btn : buttons) {
            btn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    // set everything unselected.
                    for(MyLineButton button : buttons) {
                        button.setSelected(false);
                    }
                    // only set current button selected.
                    btn.setSelected(true);
                    // switch cardLayout to specific JPanel.
                    switchPanel(btn.getText());
                }
            });
        }

        // default select the first button.
        buttons.get(0).setSelected(true);

        return panel;
    }


}
