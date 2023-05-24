package utils;

import javax.swing.*;
import java.awt.*;

// Custom Icon implementation to represent a colored button
public class ColorIcon implements Icon {
    private final Color color;

    public ColorIcon(Color color) {
        this.color = color;
    }

    public void paintIcon(Component c, Graphics g, int x, int y) {
        g.setColor(color);
        g.fillRect(x, y, getIconWidth(), getIconHeight());
    }

    public int getIconWidth() {
        return 20;  // Adjust the width as desired
    }

    public int getIconHeight() {
        return 20;  // Adjust the height as desired
    }
}
