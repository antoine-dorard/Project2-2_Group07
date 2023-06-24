package backend.autocompletion;

import controls.MyIconButton;
import main.SkillLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Locale;

public class AutoCompletionKeyAdapter implements KeyListener {
    AutoCompletion autoCompletion;
    JTextField textField;
    String subsentence;
    MyIconButton warning;

    JPopupMenu suggestionsPopup;
    int selectedSuggestionIndex;
    JMenuItem selectedSuggestion;

    /**
     * Class that is responsible for showing suggestions based on the input of a textfield,
     * @param textField the input (a subsentence) is read from this textfield
     * @param button warning icon that is visible when subsentence is not recognized
     */
    public AutoCompletionKeyAdapter(JTextField textField, MyIconButton button, SkillLoader skillLoader){
        this.autoCompletion = new AutoCompletion(skillLoader);
        this.textField = textField;
        this.warning = button;
        warning.setEnabled(false);

        suggestionsPopupSetUp();

        selectedSuggestionIndex = -1; // nothing is selected yet with tab keys
    }

    private void suggestionsPopupSetUp(){
        suggestionsPopup = new JPopupMenu();
        suggestionsPopup.setLayout(new BoxLayout(suggestionsPopup, BoxLayout.X_AXIS));
        suggestionsPopup.setBorder(BorderFactory.createEmptyBorder()); // remove border
        suggestionsPopup.setOpaque(false);

        suggestionsPopup.addKeyListener(new KeyAdapter() {
            // listener when one of the suggestions in the popUp is already selected
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_TAB) { // selecting suggestions with tab key
                    e.consume(); // consume the event to prevent it from being handled by other components
                    selectNextSuggestion();
                }
            }
        });
    }

    @Override
    public void keyTyped(KeyEvent e) {
        subsentence = textField.getText();
        if (e.getKeyChar() == ' '){
            if (checkStartWord(subsentence)) {
                autoCompletion.nextProbableWord(subsentence);
                //printSuggestions();
                showSuggestions();
                textField.requestFocus(); // ensures that the user can type immediately after not selecting a suggestion
            }
        }
        else if (e.getKeyChar() == '\t') { // pressing tab selects next suggestion
            if (suggestionsPopup.isVisible()) {
                e.consume(); // Consume the event to prevent it from being handled by other components
                selectNextSuggestion();
            }
        }
        // when enter is pressed, check if a suggestion is selected
        else if (e.getKeyChar() == '\n') {
            if (selectedSuggestionIndex > -1) {
                e.consume();
                addSuggestionToTextfield();
            }
        }
        else {
            hideSuggestions();
            setWarning();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        // if user uses deletes, and last char in the input field is a space, show suggestions again (if there are any)
        if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE){
            try {
                if (subsentence.charAt(subsentence.length()-1) == ' '){
                    autoCompletion.nextProbableWord(subsentence);
                    showSuggestions();
                    textField.requestFocus();
                }
            } catch (StringIndexOutOfBoundsException exception){
                // this if for when input field becomes blank again after deleting some input
                // nothing happens
            }
        }
    }

    private void selectNextSuggestion() {
        List<String> suggestions = autoCompletion.getFinalSuggestions();
        // if tab is pressed but there are no suggestions, just return
        if (suggestions.size() == 0){
            return;
        }

        // deselecting previous selection (reset everything)
        if (selectedSuggestionIndex > -1) {
            selectedSuggestion = (JMenuItem) suggestionsPopup.getComponent(selectedSuggestionIndex);
            selectedSuggestion.setSelected(false);
            selectedSuggestion.setBorder(BorderFactory.createEmptyBorder());
        }

        // increment by 1 and allow for looping through suggestions by doing modulus
        selectedSuggestionIndex = (selectedSuggestionIndex + 1) % suggestions.size();
        selectedSuggestion = (JMenuItem) suggestionsPopup.getComponent(selectedSuggestionIndex); // update
        selectedSuggestion.setSelected(true);
        selectedSuggestion.setBorder(BorderFactory.createLineBorder(Color.white));

    }

    /**
     * Method that checks whether input starts with a known starting word, since we do not
     * want to start suggesting words if input does not correspond to predefined questions
     * @param subSentence to be checked whether first word is a starting word
     * @return true if input starts with a recognized start word, false otherwise
     */
    public boolean checkStartWord(String subSentence){
        String[] words = subSentence.split("\\s+"); // tokenize
        return autoCompletion.getStartWords().contains(words[0].toLowerCase(Locale.ROOT));
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
                    selectedSuggestion = (JMenuItem) e.getSource();
                    addSuggestionToTextfield();
                }
            });
            suggestionsPopup.add(suggestionItem);
            suggestionItem.setOpaque(false);
            suggestionItem.setForeground(Color.white);
        }

        if (suggestionsPopup.getComponentCount() > 0) { // if there is (more than) one suggestion, show it
            suggestionsPopup.show(textField, 0, -25);
            warning.setSelected(false);
            warning.setToolTipText("");
            selectedSuggestionIndex = -1;
        }

        suggestionsPopup.setVisible(true);
    }

    private void hideSuggestions() {
        selectedSuggestionIndex = -1;
        suggestionsPopup.setVisible(false);
    }

    private void setWarning(){
        warning.setSelected(true);
        warning.setToolTipText("The question is incomplete and/or might not be recognized");
    }

    private void addSuggestionToTextfield(){
        textField.setText(textField.getText() + selectedSuggestion.getText()); // update textfield
        hideSuggestions(); // hide suggestions, but don't set warning because textfield includes valid subsentence
    }

    public boolean isSuggestionSelected() {
        return selectedSuggestionIndex > -1;
    }

//    private void printSuggestions() {
//        System.out.println("--- Suggestions (Only generates max 3 suggestions in total) ---");
//        System.out.println("- Most probable next word:");
//        for (String suggestion : autoCompletion.getSuggestions()) {
//            System.out.println(suggestion);
//        }
//        System.out.println("- Second most probable next word:");
//        for (String suggestion : autoCompletion.getSecondSuggestions()) {
//            System.out.println(suggestion);
//        }
//    }
}
