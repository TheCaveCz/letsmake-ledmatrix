package thecave.matrixcontrol.gui.utils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class IconHelper {

    public static void setIcon(JFrame frame) {
        try {
            frame.setIconImage(readIcon());
        } catch (Exception ignored) {
        }

    }

    public static Image readIcon() throws IOException {
        return ImageIO.read(IconHelper.class.getResource("/thecave/matrixcontrol/resources/icon.png"));
    }
}
