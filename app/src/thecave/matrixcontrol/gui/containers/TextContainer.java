package thecave.matrixcontrol.gui.containers;

import thecave.matrixcontrol.gui.TabContainer;
import thecave.matrixcontrol.device.Device;
import thecave.matrixcontrol.device.DeviceMode;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class TextContainer implements TabContainer {
    private final NeedsSaveHandler needsSaveHandler;
    private JPanel textPanel;
    private JTextField textValueField;
    private JSlider textSpeedSlider;
    private JButton textSendButton;
    private JButton textSwitchButton;

    private final Device device;

    public TextContainer(Device adevice, NeedsSaveHandler nsh) {
        this.needsSaveHandler = nsh;
        this.device = adevice;

        textSendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s = textValueField.getText() + " ";
                try {
                    if (s.getBytes("UTF-8").length > device.getMaxTextLen()) {
                        JOptionPane.showMessageDialog(textPanel, "Text is too long!", getTitle(), JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                } catch (Exception ignored) {
                }
                device.setText(s, textSpeedSlider.getValue());
                needsSaveHandler.setNeedsSaveFlag();
            }
        });
        textSwitchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                device.setMode(DeviceMode.TEXT);
            }
        });
    }

    @Override
    public String getTitle() {
        return "Text";
    }

    @Override
    public JPanel getRootPanel() {
        return textPanel;
    }

    @Override
    public void enableAll(boolean b) {
        textSendButton.setEnabled(b);
        textSwitchButton.setEnabled(b);
    }

    @Override
    public void refresh() {
        textSpeedSlider.setValue(device.getTextSpeed());
        textValueField.setText(device.getText());
    }
}
