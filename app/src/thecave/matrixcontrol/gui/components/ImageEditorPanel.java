package thecave.matrixcontrol.gui.components;

import thecave.matrixcontrol.device.DeviceImage;
import thecave.matrixcontrol.gui.utils.DeviceImagePainter;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


public class ImageEditorPanel extends JPanel {
    private DeviceImage deviceImage = new DeviceImage();
    private boolean editable;
    private boolean selected;
    private int imageIndex;
    private boolean showIgnored;

    private ChangeEvent changeEvent;
    private MouseListener mouseListener;
    private ChangeListener changeListener;

    public ImageEditorPanel() {
    }

    @Override
    protected void paintComponent(Graphics g) {
        DeviceImagePainter.paint(g, deviceImage, 0, 0, getWidth(), getHeight(), selected, deviceImage.isIgnored() && !editable && showIgnored);
    }

    private void clickedAt(MouseEvent e) {
        if (!isEnabled()) return;

        if (!editable)
            return;

        int dx = DeviceImagePainter.getXSpacing(getWidth());
        int dy = DeviceImagePainter.getYSpacing(getHeight());
        int sx = DeviceImagePainter.getXSize(getWidth());
        int sy = DeviceImagePainter.getYSize(getHeight());

        int x = (e.getX() - dx) / (sx + dx);
        int y = (e.getY() - dy) / (sy + dy);

        boolean v = !deviceImage.getPixel(x, y);
        if ((e.getModifiersEx() & InputEvent.SHIFT_DOWN_MASK) != 0) {
            for (int i = 0; i < DeviceImage.MAX_Y; i++) {
                deviceImage.setPixel(x, i, v);
            }
        } else if ((e.getModifiersEx() & InputEvent.CTRL_DOWN_MASK) != 0) {
            for (int i = 0; i < DeviceImage.MAX_X; i++) {
                deviceImage.setPixel(i, y, v);
            }
        } else {
            deviceImage.setPixel(x, y, v);
        }

        fireStateChanged();
        repaint();
    }

    public void setDeviceImage(DeviceImage deviceImage) {
        this.deviceImage.copyFrom(deviceImage);
        repaint();
    }

    public DeviceImage getDeviceImage() {
        return deviceImage;
    }

    public boolean isEditable() {
        return editable;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        if (this.selected != selected) {
            this.selected = selected;
            repaint();
        }
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
        if (editable) {
            if (mouseListener == null) {
                mouseListener = new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (!SwingUtilities.isLeftMouseButton(e)) return;

                        clickedAt(e);
                    }
                };
            }
            addMouseListener(mouseListener);
        } else if (mouseListener != null) {
            removeMouseListener(mouseListener);
            mouseListener = null;
        }
    }

    public void setChangeListener(ChangeListener changeListener) {
        this.changeListener = changeListener;
    }

    private void fireStateChanged() {
        if (changeListener != null) {
            if (changeEvent == null) {
                changeEvent = new ChangeEvent(this);
            }
            changeListener.stateChanged(changeEvent);
        }
    }

    public int getImageIndex() {
        return imageIndex;
    }

    public void setImageIndex(int imageIndex) {
        this.imageIndex = imageIndex;
    }

    public void setShowIgnored(boolean showIgnored) {
        this.showIgnored = showIgnored;
    }
}
