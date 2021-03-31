package VueControleur;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class RotatableImageIcon extends ImageIcon {
    private double rotation;

    public RotatableImageIcon(BufferedImage image) {
        super(image);
    }

    @Override
    public synchronized void paintIcon(Component c, Graphics g, int x, int y) {
        if(rotation != 0) {
            g.translate(getIconWidth() / 2, getIconHeight() / 2);
            ((Graphics2D)g).rotate(rotation);
            g.translate(-(getIconWidth() / 2), -(getIconHeight() / 2));
        }
        super.paintIcon(c, g, x, y);
    }

    public void rotate(double rotation){
        this.rotation = rotation;
    }
}
