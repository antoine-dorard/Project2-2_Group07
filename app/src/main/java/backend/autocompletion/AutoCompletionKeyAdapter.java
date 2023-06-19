package backend.autocompletion;

import controls.MyIconButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class AutoCompletionKeyAdapter extends KeyAdapter {
    AutoCompletion autoCompletion;
    JTextField textField;
    String subsentence;
    MyIconButton warning;
    JPopupMenu suggestionsPopup;

    public AutoCompletionKeyAdapter(JTextField textField, MyIconButton button){
        this.autoCompletion = new AutoCompletion();
        this.textField = textField;
        this.warning = button;
        warning.setEnabled(true);
        suggestionsPopupSetUp();
    }

    private void suggestionsPopupSetUp(){
        suggestionsPopup = new JPopupMenu();
        suggestionsPopup.setLayout(new BoxLayout(suggestionsPopup, BoxLayout.X_AXIS));
        suggestionsPopup.setBorder(BorderFactory.createEmptyBorder()); // remove border
    }

    @Override
    public void keyTyped(KeyEvent e) {
        subsentence = textField.getText();
        if (e.getKeyChar() == ' '){
            autoCompletion.nextProbableWord(subsentence);
            //printSuggestions();
            showSuggestions();
            textField.requestFocus(); // ensures that the user can type immediately after not selecting a suggestion
        }
        else {
            hideSuggestions();
        }
        System.out.println(autoCompletion.getFinalSuggestions().isEmpty());
        warning.setEnabled(autoCompletion.getFinalSuggestions().isEmpty());
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    private void showSuggestions() {
        suggestionsPopup.removeAll(); // reset

        // add list of suggestions to popup menu
        for (String suggestion : autoCompletion.getFinalSuggestions()){
            JMenuItem suggestionItem = new JMenuItem(suggestion);
            suggestionItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // retrieve info of object that triggered actionEvent, in this case the MenuItem
                    JMenuItem menuItem = (JMenuItem) e.getSource();
                    String selectedSuggestion = menuItem.getText();
                    textField.setText(textField.getText() + selectedSuggestion); // update textfield
                    suggestionsPopup.setVisible(false); // hide current suggestions
                }
            });
            suggestionsPopup.add(suggestionItem);
            suggestionItem.setOpaque(false);
            suggestionItem.setForeground(Color.white);
        }

        if (suggestionsPopup.getComponentCount() > 0) { // if there is (more than) one suggestion, show it
            suggestionsPopup.show(textField, 0, -25);
        }

        suggestionsPopup.setVisible(true);
    }

    private void hideSuggestions() {
        suggestionsPopup.setVisible(false);
    }

    private void printSuggestions() {
        System.out.println("--- Suggestions (Only generates max 3 suggestions in total) ---");
        System.out.println("- Most probable next word:");
        for (String suggestion : autoCompletion.getSuggestions()) {
            System.out.println(suggestion);
        }
        System.out.println("- Second most probable next word:");
        for (String suggestion : autoCompletion.getSecondSuggestions()) {
            System.out.println(suggestion);
        }
    }

    public AutoCompletion getAutoCompletion() {
        return autoCompletion;
    }
}
