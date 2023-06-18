package backend.autocompletion;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class AutoCompletionKeyAdapter extends KeyAdapter {
    AutoCompletion autoCompletion;
    JTextField textField;
    String subsentence;

    public AutoCompletionKeyAdapter(JTextField textField){
        this.autoCompletion = new AutoCompletion();
        this.textField = textField;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        subsentence = textField.getText();
        if (e.getKeyChar() == ' '){
            autoCompletion.nextProbableWord(subsentence);
            printSuggestions();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    private void printSuggestions() {
        System.out.println("--- Suggestions (Note: only generates Max 3 suggestions in total) ---");
        System.out.println("- Most probable next word:");
        for (String suggestion : autoCompletion.suggestions) {
            System.out.println(suggestion);
        }
        System.out.println("- Second most probable next word:");
        for (String suggestion : autoCompletion.secondSuggestions) {
            System.out.println(suggestion);
        }
    }
}
