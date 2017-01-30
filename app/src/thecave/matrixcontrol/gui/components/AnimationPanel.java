package thecave.matrixcontrol.gui.components;

import thecave.matrixcontrol.device.DeviceAnimation;
import thecave.matrixcontrol.gui.utils.DeviceImagePainter;

import javax.swing.*;
import java.awt.*;


public class AnimationPanel extends JPanel {
    private DeviceAnimation deviceAnimation = new DeviceAnimation();
    private final ImageSource imageSource;
    private int spacing = 0;
    private int animationIndex;
    private boolean selected;
    private int selectedFrame = -1;
    private boolean showUnused = true;

    public AnimationPanel(ImageSource imageSource) {
        super();

        this.imageSource = imageSource;
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (deviceAnimation.getTransition() == DeviceAnimation.Transition.UNUSED && showUnused) {
            g.setColor(selected ? DeviceImagePainter.COLOR_INACTIVE_SELECTED : Color.DARK_GRAY);
            g.fillRect(0, 0, getWidth(), getHeight());
            return;
        }

        int sx = (getWidth() - spacing * (DeviceAnimation.MAX_FRAMES - 1)) / DeviceAnimation.MAX_FRAMES;

        for (int i = 0; i < DeviceAnimation.MAX_FRAMES; i++) {
            DeviceImagePainter.paint(g, imageSource.getImageWithId(deviceAnimation.getFrameAt(i)), (sx + spacing) * i, 0, sx, getHeight(), selected || selectedFrame == i, false);
        }
    }

    public DeviceAnimation getDeviceAnimation() {
        return deviceAnimation;
    }

    public void setDeviceAnimation(DeviceAnimation deviceAnimation) {
        this.deviceAnimation.copyFrom(deviceAnimation);
        repaint();
    }

    public int getSpacing() {
        return spacing;
    }

    public void setSpacing(int spacing) {
        this.spacing = spacing;
    }

    public int getAnimationIndex() {
        return animationIndex;
    }

    public void setAnimationIndex(int animationIndex) {
        this.animationIndex = animationIndex;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        repaint();
    }

    public void setSelectedFrame(int selectedFrame) {
        if (this.selectedFrame != selectedFrame) {
            this.selectedFrame = selectedFrame;
            repaint();
        }
    }

    public void setShowUnused(boolean showUnused) {
        this.showUnused = showUnused;
    }

}
