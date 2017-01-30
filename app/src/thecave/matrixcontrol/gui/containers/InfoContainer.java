package thecave.matrixcontrol.gui.containers;

import thecave.matrixcontrol.gui.TabContainer;
import thecave.matrixcontrol.device.Device;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class InfoContainer implements TabContainer {
    private final NeedsSaveHandler needsSaveHandler;
    private JPanel infoContainer;
    private JLabel modelLabel;
    private JLabel versionLabel;
    private JLabel imagesLabel;
    private JLabel animsLabel;
    private JLabel textLenLabel;
    private JSlider brightnessSlider;
    private JLabel modeLabel;

    private final Device device;
    private boolean brightnessSliderSettingValue;


    public InfoContainer(Device adevice, NeedsSaveHandler nsh) {
        this.needsSaveHandler = nsh;
        this.device = adevice;

        brightnessSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (!brightnessSlider.getValueIsAdjusting() && !brightnessSliderSettingValue) {
                    device.setBrightness(brightnessSlider.getValue());
                    needsSaveHandler.setNeedsSaveFlag();
                }
            }
        });
    }

    public void refresh() {
        modelLabel.setText(device.getName());
        versionLabel.setText(device.getVersion());
        modeLabel.setText(device.getMode() != null ? device.getMode().toString().toLowerCase() : "unknown");
        imagesLabel.setText(device.getMaxImages() + "");
        animsLabel.setText(device.getMaxAnimations() + "");
        textLenLabel.setText(device.getMaxTextLen() + "");
        brightnessSliderSettingValue = true;
        brightnessSlider.setValue(device.getBrightness());
        brightnessSliderSettingValue = false;
    }

    @Override
    public String getTitle() {
        return "Device info";
    }

    @Override
    public JPanel getRootPanel() {
        return infoContainer;
    }

    @Override
    public void enableAll(boolean b) {
        brightnessSlider.setEnabled(b);
    }

}
