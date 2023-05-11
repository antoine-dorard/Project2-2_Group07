package Spelling_Checker;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.im.InputContext;
import java.util.HashMap;

//Keyboard related suggestions
public class KeyBoardChecker {
    private HashMap< Character , String > keyboardMap;

    public Integer checkerkeys(String word, String suggestion1){
        int sug1_Score = 0;
        if(word.length() == suggestion1.length()){
            for (int i = 0; i < word.length(); i++) {
                String keyrelated = keyboardMap.get(word.charAt(i));
                if(!keyrelated.contains(suggestion1.charAt(i) + "" )) {
                    sug1_Score++;
                }
            }
        }
        else{
            sug1_Score=1;
        }
        return sug1_Score;

    }
    public void selectKeyboard(){
        // Check if the keyboard is qwerty
        int keyCode = KeyEvent.VK_A;
        String keyText = KeyEvent.getKeyText(keyCode);
        //System.out.println(keyText);

        InputContext context = InputContext.getInstance();
        //System.out.println(context.getLocale().toString());
        if(context.getLocale().toString().equals("fr_FR")){
            getAzertyKeyboard();
        }
        else{ //the keyboard is then as default qwerty
            getQwertyKeyboard();
        }
    }

    public static void main(String[] args) throws AWTException {
        KeyBoardChecker main = new KeyBoardChecker();
        main.selectKeyboard();
        //String relatedWord = main.checkerkeys("jome","home","dome");
        //String relatedWord = main.checkerkeys("dome","rope","hope");
        Integer value = main.checkerkeys("beam","ream");
        System.out.println(value);

    }
    public void getQwertyKeyboard () {
        this.keyboardMap = new HashMap < >() ;
        keyboardMap.put('a', "qwsza");
        keyboardMap.put('b', "vghnb");
        keyboardMap.put('c', "xdfvc") ;
        keyboardMap.put('d', "serfcxd");
        keyboardMap.put('e', "w34rdse");
        keyboardMap.put('f', "drtgvcf");
        keyboardMap.put('g', "ftyhbvg");
        keyboardMap.put('h', "gyujnbh");
        keyboardMap.put('i', "u89okji");
        keyboardMap.put('j', "huikmnj");
        keyboardMap.put('k', "jiol,mk");
        keyboardMap.put('l', "kop;.,l");
        keyboardMap.put('m', "njk,m") ;
        keyboardMap.put('n', "bhjmn") ;
        keyboardMap.put('o', "i90plko");
        keyboardMap.put('p', "o0-[;lp");
        keyboardMap.put('q', "12 waq") ;
        keyboardMap.put('r', "e45tfdr");
        keyboardMap.put('s', "awedxzs");
        keyboardMap.put('t', "r56ygft");
        keyboardMap.put('u', "y78ijhu");
        keyboardMap.put('v', "cfgbv");
        keyboardMap.put('w', "q23esaw");
        keyboardMap.put('x', "zsdcx") ;
        keyboardMap.put('y', "t67uhgy");
        keyboardMap.put('z', "\\asxz") ;
        keyboardMap.put('-', "0p[=-") ;
    }
    public void getAzertyKeyboard () {
        this.keyboardMap = new HashMap < >() ;
        keyboardMap.put('a', "12zqsa");
        keyboardMap.put('b', "vgnhb") ;
        keyboardMap.put('c', "xdfvc") ;
        keyboardMap.put('d', "serfxcd") ;
        keyboardMap.put('e', "z34rdse") ;
        keyboardMap.put('f', "dcgtrvf") ;
        keyboardMap.put('g', "fvbtyhg") ;
        keyboardMap.put('h', "gbnuyjh") ;
        keyboardMap.put('i', "ujko_çi") ;
        keyboardMap.put('j', "hn,kiuj") ;
        keyboardMap.put('k', "jiol;,k") ;
        keyboardMap.put('l', "kopm:;l") ;
        keyboardMap.put('m', "lp^ù!:m") ;
        keyboardMap.put('n', "bhj,n") ;
        keyboardMap.put('o', "i09plko") ;
        keyboardMap.put('p', "oà)^ùmlp") ;
        keyboardMap.put('q', "azsw<q") ;
        keyboardMap.put('r', "edft45r") ;
        keyboardMap.put('s', "wxczqdes") ;
        keyboardMap.put('t', "r56ygft") ;
        keyboardMap.put('u', "y78ijhu") ;
        keyboardMap.put('v', "cfgbv") ;
        keyboardMap.put('w', "qsx<w") ;
        keyboardMap.put('x', "wscdx") ;
        keyboardMap.put('y', "tghu67y") ;
        keyboardMap.put('z', "qseda23z") ;
        keyboardMap.put('-', "57ty-");
    }
}

