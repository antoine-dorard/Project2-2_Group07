package utils;

import java.awt.*;
import java.util.HashMap;


public class UIColors {

    // create the HashMap to store all Colors in.
    private HashMap<UIColor, Color> colors = new HashMap<>();

    public UIColors() {

        initColors();
    }

    public enum UIColor {
        BG_NAVIGATION,
        BG_CHAT_TEXT,
        BG_PRIMARY,
        BG_SECONDARY,
        FG_HEADER,
        FG_SUBHEADER,
        FG_NAVIGATION,
        BTN_BG_COLORED,
        BTN_BG_COLORED_PRESSED,
        BTN_BG_RED,
        BTN_BG_RED_PRESSED,
        BTN_FG_ON,
        BTN_FG_OFF,
        BORDER_COLORED,
        BORDER_GREY,
        TABLE_BG,
        TABLE_BG_SELECTION,
        TABLE_FG,
        TABLE_FG_HEADER,
        TABLE_FG_SELECTION,
        TEXT_BG,
        TEXT_BG_SELECTION,
        TEXT_FG,
        TEXT_FG_SELECTION,
        CHAT_FG_BOT,
        CHAT_FG_USER
    }


    public Color getColor(UIColor uiColor) {
        return colors.get(uiColor);
    }


    private void initColors(){
        colors.put(UIColor.BG_NAVIGATION, new Color(20, 24, 28));
        colors.put(UIColor.BG_CHAT_TEXT, new Color(21, 26,30));
        colors.put(UIColor.BG_PRIMARY, new Color(33, 39, 44));
        colors.put(UIColor.BG_SECONDARY, new Color(43, 51, 61));

        colors.put(UIColor.FG_HEADER, new Color(255,255,255));
        colors.put(UIColor.FG_SUBHEADER, new Color(255,255,255));
        colors.put(UIColor.FG_NAVIGATION, new Color(255,255,255));

        colors.put(UIColor.BTN_BG_COLORED, new Color(96,151,182));
        colors.put(UIColor.BTN_BG_COLORED_PRESSED, new Color(54, 100, 126));
        colors.put(UIColor.BTN_BG_RED, new Color(169, 78, 78));
        colors.put(UIColor.BTN_BG_RED_PRESSED, new Color(140, 56, 56));
        colors.put(UIColor.BTN_FG_ON, new Color(255,255,255));
        colors.put(UIColor.BTN_FG_OFF, new Color(188, 188, 188));

        colors.put(UIColor.BORDER_COLORED, new Color(96,151,182));
        colors.put(UIColor.BORDER_GREY, new Color(68,68,68));

        colors.put(UIColor.TABLE_BG, new Color(33, 39, 44));
        colors.put(UIColor.TABLE_BG_SELECTION, new Color(96,151,182, 50));
        colors.put(UIColor.TABLE_FG, new Color(188, 188, 188));
        colors.put(UIColor.TABLE_FG_HEADER, new Color(255,255,255));
        colors.put(UIColor.TABLE_FG_SELECTION, new Color(255,255,255));

        colors.put(UIColor.TEXT_BG, new Color(33, 39, 44));
        colors.put(UIColor.TEXT_BG_SELECTION, new Color(96,151,182));
        colors.put(UIColor.TEXT_FG, new Color(255,255,255));
        colors.put(UIColor.TEXT_FG_SELECTION, new Color(255,255,255));

        colors.put(UIColor.CHAT_FG_BOT, new Color(96,151,182));
        colors.put(UIColor.CHAT_FG_USER, new Color(255,255,255));
    }
}
