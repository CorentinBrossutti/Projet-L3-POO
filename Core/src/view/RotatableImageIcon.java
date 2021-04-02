package view;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Une icône Swing dont la rotation peut changer
 */
public class RotatableImageIcon extends ImageIcon {
    private double targetRotation;

    public RotatableImageIcon(BufferedImage image) {
        super(image);
    }

    @Override
    public synchronized void paintIcon(Component c, Graphics g, int x, int y) {
        if (targetRotation != 0) {
            g.translate(getIconWidth() / 2, getIconHeight() / 2);
            ((Graphics2D) g).rotate(targetRotation);
            g.translate(-(getIconWidth() / 2), -(getIconHeight() / 2));
        }
        super.paintIcon(c, g, x, y);
    }

    public void rotate(double rotation) {
        this.targetRotation = rotation;
    }
}
