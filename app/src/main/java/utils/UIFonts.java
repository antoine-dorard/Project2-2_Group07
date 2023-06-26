package utils;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UIFonts {

    // Fonts
    private final String fontDir = "app\\src\\main\\resources\\fonts\\";

    // create the HashMaps to store all fonts and file-paths in.
    private HashMap<FontStyle, String> fontFileMap = new HashMap<>();
    private HashMap<FontStyle, Font> fontMap = new HashMap<>();


    public UIFonts() {
        // initialize the HashMap with all font file-paths assigned.
        initFontFileMap();
        // AFTER initializing the font-file HashMap, we initialize the font map.
        initFontMap();
    }

    public Font getFont(FontStyle fontStyle, int size){
        // allows the user to get a Font by font-style and resize it.
        return fontMap.get(fontStyle).deriveFont((float) size);
    }

    public enum FontStyle {
        THINITALIC,
        THIN,
        LIGHTITALIC,
        LIGHT,
        ITALIC,
        REGULAR,
        MEDIUMITALIC,
        MEDIUM,
        BOLDITALIC,
        BOLD,
        BLACKITALIC,
        BLACK
    }


    private void initFontMap() {
        // loop through the previously created FontFileMap (HashMap).
        for (Map.Entry<FontStyle, String> entry : fontFileMap.entrySet()) {
            // get the currently indexed font-style enum and font file-path.
            FontStyle fontStyle = entry.getKey();
            String fontFilePath = entry.getValue();
            // create a File from the font file-path.
            File fontFile = new File(fontFilePath);
            try {
                // create custom fonts from that file.
                Font font = Font.createFont(Font.TRUETYPE_FONT, fontFile);
                // assign them to an enum value in another HashMap 'fontMap'.
                fontMap.put(fontStyle, font);
            } catch (Exception e) {
                try {
                    Font font = new Font("Arial", Font.PLAIN, 14);
                    fontMap.put(fontStyle, font);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    private void initFontFileMap(){
        // assign file-path to .ttf font, to an enum value.
        fontFileMap.put(FontStyle.THINITALIC, fontDir + "Roboto-ThinItalic.ttf");
        fontFileMap.put(FontStyle.THIN, fontDir + "Roboto-Thin.ttf");
        fontFileMap.put(FontStyle.LIGHTITALIC, fontDir + "Roboto-LightItalic.ttf");
        fontFileMap.put(FontStyle.LIGHT, fontDir + "Roboto-Light.ttf");
        fontFileMap.put(FontStyle.ITALIC, fontDir + "Roboto-Italic.ttf");
        fontFileMap.put(FontStyle.REGULAR, fontDir + "Roboto-Regular.ttf");
        fontFileMap.put(FontStyle.MEDIUMITALIC, fontDir + "Roboto-MediumItalic.ttf");
        fontFileMap.put(FontStyle.MEDIUM, fontDir + "Roboto-Medium.ttf");
        fontFileMap.put(FontStyle.BOLDITALIC, fontDir + "Roboto-BoldItalic.ttf");
        fontFileMap.put(FontStyle.BOLD, fontDir + "Roboto-Bold.ttf");
        fontFileMap.put(FontStyle.BLACKITALIC, fontDir + "Roboto-BlackItalic.ttf");
        fontFileMap.put(FontStyle.BLACK, fontDir + "Roboto-Black.ttf");
    }

}