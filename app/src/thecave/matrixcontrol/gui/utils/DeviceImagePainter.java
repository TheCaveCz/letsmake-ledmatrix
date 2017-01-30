package thecave.matrixcontrol.gui.utils;


import thecave.matrixcontrol.device.DeviceImage;

import java.awt.*;

public class DeviceImagePainter {
    public static final Color COLOR_BG_SELECTED = Color.BLUE;
    public static final Color COLOR_BG = Color.BLACK;
    public static final Color COLOR_INACTIVE_SELECTED = new Color(100, 100, 255);
    public static final Color COLOR_INACTIVE = Color.DARK_GRAY;
    public static final Color COLOR_ACTIVE = Color.RED;
    public static final Color COLOR_ACTIVE_IGNORED = new Color(200, 100, 100);

    public static int getXSpacing(int width) {
        return (int) (0.015 * width);
    }

    public static int getYSpacing(int height) {
        return (int) (0.015 * height);
    }

    public static int getXSize(int width) {
        return (width - getXSpacing(width) * (DeviceImage.MAX_X + 1)) / DeviceImage.MAX_X;
    }

    public static int getYSize(int height) {
        return (height - getYSpacing(height) * (DeviceImage.MAX_Y + 1)) / DeviceImage.MAX_Y;
    }

    public static void paint(Graphics g, DeviceImage di, int xx, int yy, int w, int h, boolean selected, boolean ignored) {
        g.setColor(selected ? COLOR_BG_SELECTED : COLOR_BG);
        g.fillRect(xx, yy, w, h);

        if (di == null) return;

        int dx = getXSpacing(w);
        int dy = getYSpacing(h);
        int sx = getXSize(w);
        int sy = getYSize(h);

        for (int x = 0; x < DeviceImage.MAX_X; x++) {
            for (int y = 0; y < DeviceImage.MAX_Y; y++) {
                g.setColor(di.getPixel(x, y) ? (ignored ? COLOR_ACTIVE_IGNORED : COLOR_ACTIVE) : (selected ? COLOR_INACTIVE_SELECTED : COLOR_INACTIVE));
                g.fillOval(xx + (sx + dx) * x + dx, yy + (sy + dy) * y + dy, sx, sy);
            }
        }
    }
}
