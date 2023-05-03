package Spelling_Checker;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.im.InputContext;
import java.util.HashMap;

//Keyboard related suggestions
public class KeyBoardChecker {
    private HashMap< Character , String > keyboardMap;

    public String checkerkeys(String word, String suggestion1,String suggestion2){
        // Check if the keyboard is qwerty
        int keyCode = KeyEvent.VK_A;
        String keyText = KeyEvent.getKeyText(keyCode);
        System.out.println(keyText);

        InputContext context = InputContext.getInstance();
        System.out.println(context.getLocale().toString());
        if(context.getLocale().toString().equals("fr_FR")){
            getAzertyKeyboard();
        }
        else{
            getQwertyKeyboard();
        }
        int sug1_Score = 0;
        int sug2_Score = 0;
        for (int i = 0; i < word.length(); i++) {
            String keyrelated = keyboardMap.get(word.charAt(i));
            if(keyrelated.contains(suggestion1.charAt(i) + "")) {
                sug1_Score++;
            } else if(keyrelated.contains(suggestion2.charAt(i) + "")) {
                sug2_Score++;
            }
        }
        if(sug1_Score>sug2_Score){
            return suggestion1;
        }
        else if(sug1_Score<sug2_Score){
            return suggestion2;
        }
        else{ //the words are as likely
            return suggestion1;
        }

    }

    public static void main(String[] args) throws AWTException {
        KeyBoardChecker main = new KeyBoardChecker();
        //String relatedWord = main.checkerkeys("jome","home","dome");
        //String relatedWord = main.checkerkeys("dome","rope","hope");
        String relatedWord = main.checkerkeys("xat","cat","bat");
        System.out.println(relatedWord);

    }
    public void getQwertyKeyboard () {
        this.keyboardMap = new HashMap < >() ;
        keyboardMap.put('a', "qwsz");
        keyboardMap.put('b', "vghn");
        keyboardMap.put('c', "xdfv") ;
        keyboardMap.put('d', "serfcx");
        keyboardMap.put('e', "w34rds");
        keyboardMap.put('f', "drtgvc");
        keyboardMap.put('g', "ftyhbv");
        keyboardMap.put('h', "gyujnb");
        keyboardMap.put('i', "u89okj");
        keyboardMap.put('j', "huikmn");
        keyboardMap.put('k', "jiol,m");
        keyboardMap.put('l', "kop;.,");
        keyboardMap.put('m', "njk,") ;
        keyboardMap.put('n', "bhjm") ;
        keyboardMap.put('o', "i90plk");
        keyboardMap.put('p', "o0-[;l");
        keyboardMap.put('q', "12 wa") ;
        keyboardMap.put('r', "e45tfd");
        keyboardMap.put('s', "awedxz");
        keyboardMap.put('t', "r56ygf");
        keyboardMap.put('u', "y78ijh");
        keyboardMap.put('v', "cfgb");
        keyboardMap.put('w', "q23esa");
        keyboardMap.put('x', "zsdc") ;
        keyboardMap.put('y', "t67uhg");
        keyboardMap.put('z', "\\asx") ;
        keyboardMap.put('-', "0p[=") ;
    }
    public void getAzertyKeyboard () {
        this.keyboardMap = new HashMap < >() ;
        keyboardMap.put('a', "12zqs");
        keyboardMap.put('b', "vgnh") ;
        keyboardMap.put('c', "xdfv") ;
        keyboardMap.put('d', "serfxc") ;
        keyboardMap.put('e', "z34rds") ;
        keyboardMap.put('f', "dcgtrv") ;
        keyboardMap.put('g', "fvbtyh") ;
        keyboardMap.put('h', "gbnuyj") ;
        keyboardMap.put('i', "ujko_ç") ;
        keyboardMap.put('j', "hn,kiu") ;
        keyboardMap.put('k', "jiol;,") ;
        keyboardMap.put('l', "kopm:;") ;
        keyboardMap.put('m', "lp^ù!:") ;
        keyboardMap.put('n', "bhj,") ;
        keyboardMap.put('o', "i09plk") ;
        keyboardMap.put('p', "oà)^ùml") ;
        keyboardMap.put('q', "azsw<") ;
        keyboardMap.put('r', "edft45") ;
        keyboardMap.put('s', "wxczqde") ;
        keyboardMap.put('t', "r56ygf") ;
        keyboardMap.put('u', "y78ijh") ;
        keyboardMap.put('v', "cfgb") ;
        keyboardMap.put('w', "qsx<") ;
        keyboardMap.put('x', "wscd") ;
        keyboardMap.put('y', "tghu67") ;
        keyboardMap.put('z', "qseda23") ;
        keyboardMap.put('-', "57ty");
    }
}

