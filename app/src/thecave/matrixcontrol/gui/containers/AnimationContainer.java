package thecave.matrixcontrol.gui.containers;


import thecave.matrixcontrol.device.DeviceAnimation;
import thecave.matrixcontrol.device.DeviceImage;
import thecave.matrixcontrol.device.DeviceMode;
import thecave.matrixcontrol.gui.TabContainer;
import thecave.matrixcontrol.device.Device;
import thecave.matrixcontrol.gui.components.AnimationPanel;
import thecave.matrixcontrol.gui.components.ImagePickerDialog;
import thecave.matrixcontrol.gui.components.ImageSource;
import thecave.matrixcontrol.gui.utils.VerticalFlowLayout;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;

public class AnimationContainer implements TabContainer, ImageSource {
    private final NeedsSaveHandler needsSaveHandler;
    private JPanel animationRootPanel;
    private JPanel animationOverviewPanel;
    private JButton animShowButton;
    private JButton animSaveButton;
    private AnimationPanel animationPanel;
    private JComboBox<DeviceAnimation.Transition> transitionCombo;
    private JSlider speedSlider;
    private JCheckBox loopCheckbox;
    private JLabel sliderValueLabel;


    private final Device device;
    private int animationCurrent;
    private MouseListener animDoubleClickListener = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() != 2 || !SwingUtilities.isLeftMouseButton(e)) return;
            if (!(e.getSource() instanceof AnimationPanel)) return;

            AnimationPanel iep = (AnimationPanel) e.getSource();
            if (!iep.getParent().isEnabled()) return;
            editAnimation(iep.getAnimationIndex());
        }
    };

    public AnimationContainer(Device adevice, NeedsSaveHandler nsh) {
        this.needsSaveHandler = nsh;
        this.device = adevice;

        transitionCombo.setModel(new DefaultComboBoxModel<>(DeviceAnimation.Transition.values()));
        animationOverviewPanel.setLayout(new VerticalFlowLayout(VerticalFlowLayout.LEFT, VerticalFlowLayout.CENTER));
        animationPanel.setShowUnused(false);
        refreshSliderLabel();

        animationPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() != 1 || !animationPanel.isEnabled())
                    return;

                if (SwingUtilities.isLeftMouseButton(e)) {
                    changeAnimFrame(framePosFromMouseEvent(e));
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    removeAnimFrame();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                animationPanel.setSelectedFrame(-1);
            }
        });
        animationPanel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (!animationPanel.isEnabled()) return;

                animationPanel.setSelectedFrame(framePosFromMouseEvent(e));
            }
        });
        animShowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                device.setMode(DeviceMode.ANIMATIONS, animationCurrent);
            }
        });
        animSaveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DeviceAnimation anim = new DeviceAnimation();
                anim.copyFrom(animationPanel.getDeviceAnimation());
                anim.setTransition(getSelectedTransition());
                anim.setLoop(loopCheckbox.isSelected());
                anim.setDelayFrames(speedSlider.getValue());
                device.setAnimation(animationCurrent, anim);
                needsSaveHandler.setNeedsSaveFlag();
            }
        });
        transitionCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshAnimEditors();
            }
        });
        speedSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                refreshSliderLabel();
            }
        });
    }

    private int framePosFromMouseEvent(MouseEvent e) {
        int f = (int) (e.getPoint().getX() / animationPanel.getWidth() * DeviceAnimation.MAX_FRAMES);
        return Math.min(animationPanel.getDeviceAnimation().getFramesCount(), f);
    }

    private void changeAnimFrame(int framePos) {
        int picked = ImagePickerDialog.pickDeviceImage(
                SwingUtilities.getWindowAncestor(animationRootPanel),
                this,
                animationPanel.getDeviceAnimation().getFrameAt(framePos)
        );

        if (picked == ImagePickerDialog.RESULT_CANCELLED) return;

        animationPanel.getDeviceAnimation().setFrameAt(framePos, picked);
        animationPanel.repaint();
    }

    private void removeAnimFrame() {
        int cnt = animationPanel.getDeviceAnimation().getFramesCount();
        if (cnt == 1) return;
        animationPanel.getDeviceAnimation().setFrameAt(cnt - 1, DeviceAnimation.UNUSED_FRAME_ID);
        animationPanel.setSelectedFrame(-1);
        animationPanel.repaint();
    }

    public void refreshAnimation(int animIndex) {
        if (animationOverviewPanel.getComponentCount() == 0)
            createAnimationPanels();

        if (animIndex == animationCurrent) {
            editAnimation(animationCurrent);
        }

        AnimationPanel ap = getAnimationPanel(animIndex);
        if (ap != null) ap.setDeviceAnimation(device.getAnimations()[animIndex]);
    }

    private void createAnimationPanels() {
        animationOverviewPanel.removeAll();
        for (int i = 0; i < device.getMaxAnimations(); i++) {
            AnimationPanel ap = new AnimationPanel(this);
            ap.setMinimumSize(new Dimension(34 * 8 + ap.getSpacing() * 7, 33));
            ap.setPreferredSize(ap.getMinimumSize());
            ap.setSelected(i == animationCurrent);
            ap.setAnimationIndex(i);
            ap.addMouseListener(animDoubleClickListener);
            animationOverviewPanel.add(ap);
        }
    }

    private void editAnimation(int animationIndex) {
        AnimationPanel iep = getAnimationPanel(animationCurrent);
        if (iep != null) iep.setSelected(false);

        iep = getAnimationPanel(animationIndex);
        if (iep != null) iep.setSelected(true);

        animationCurrent = animationIndex;

        DeviceAnimation da = device.getAnimations()[animationIndex];

        loopCheckbox.setSelected(da.isLoop());
        speedSlider.setValue(da.getDelayFrames());
        transitionCombo.setSelectedItem(da.getTransition());
        animationPanel.setDeviceAnimation(da);
        refreshAnimEditors();
        refreshSliderLabel();
    }

    private void refreshSliderLabel() {
        sliderValueLabel.setText("Delay between frames: " + speedSlider.getValue() * 10 + " ms");
    }

    private DeviceAnimation.Transition getSelectedTransition() {
        return (DeviceAnimation.Transition) transitionCombo.getSelectedItem();
    }

    private void refreshAnimEditors() {
        boolean e = getSelectedTransition() != DeviceAnimation.Transition.UNUSED;
        speedSlider.setEnabled(e);
        loopCheckbox.setEnabled(e);
    }

    private AnimationPanel getAnimationPanel(int animId) {
        try {
            return (AnimationPanel) animationOverviewPanel.getComponent(animId);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String getTitle() {
        return "Animations";
    }

    public JPanel getRootPanel() {
        return animationRootPanel;
    }

    @Override
    public void enableAll(boolean b) {
        animationOverviewPanel.setEnabled(b);
        animSaveButton.setEnabled(b);
        transitionCombo.setEnabled(b);
        animShowButton.setEnabled(b);
        animationPanel.setEnabled(b);
    }

    @Override
    public void refresh() {
        animationOverviewPanel.repaint();
    }

    @Override
    public DeviceImage getImageWithId(int imageIndex) {
        if (imageIndex < 0 || imageIndex >= device.getMaxImages()) return null;

        return device.getImages()[imageIndex];
    }

    @Override
    public int getImageCount() {
        return device.getMaxImages();
    }

    private void createUIComponents() {
        animationPanel = new AnimationPanel(this);
    }
}
