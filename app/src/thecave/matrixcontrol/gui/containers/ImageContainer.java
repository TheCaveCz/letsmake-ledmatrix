package thecave.matrixcontrol.gui.containers;

import thecave.matrixcontrol.gui.components.ImageEditorPanel;
import thecave.matrixcontrol.gui.TabContainer;
import thecave.matrixcontrol.gui.utils.WrapLayout;
import thecave.matrixcontrol.device.Device;
import thecave.matrixcontrol.device.DeviceImage;
import thecave.matrixcontrol.device.DeviceMode;
import thecave.matrixcontrol.device.HexUtils;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;


public class ImageContainer implements TabContainer {
    private final NeedsSaveHandler needsSaveHandler;
    private JPanel imagePanel;
    private JPanel imageOverviewPanel;
    private JCheckBox imageIgnoredCheckbox;
    private JButton imageSwitchButton;
    private JButton imageSaveButton;
    private ImageEditorPanel imageEditorPanel;
    private JFormattedTextField imageCodeTextField;

    private int imageCurrent;
    private boolean imageCodeSettingValue;
    private final Device device;
    private MouseListener imageDoubleClickListener = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() != 2 || !SwingUtilities.isLeftMouseButton(e)) return;
            if (!(e.getSource() instanceof ImageEditorPanel)) return;

            ImageEditorPanel iep = (ImageEditorPanel) e.getSource();
            if (!iep.getParent().isEnabled()) return;
            editImage(iep.getImageIndex());
        }
    };


    public ImageContainer(Device adevice, NeedsSaveHandler nsh) {
        this.needsSaveHandler = nsh;
        this.device = adevice;

        imageOverviewPanel.setLayout(new WrapLayout(WrapLayout.LEFT));
        imageEditorPanel.setEditable(true);
        try {
            MaskFormatter mf = new MaskFormatter("HHHHHHHHHHHHHHHH");
            mf.setPlaceholderCharacter('0');
            mf.setCommitsOnValidEdit(true);
            imageCodeTextField.setFormatterFactory(new DefaultFormatterFactory(mf));
        } catch (Exception ignored) {
        }

        imageEditorPanel.setChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                refreshImageCode();
            }
        });
        imageSwitchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                device.setMode(DeviceMode.IMAGES, imageCurrent);
            }
        });
        imageSaveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DeviceImage di = new DeviceImage();
                di.copyFrom(imageEditorPanel.getDeviceImage());
                di.setIgnored(imageIgnoredCheckbox.isSelected());
                device.setImage(imageCurrent, di);
                needsSaveHandler.setNeedsSaveFlag();
            }
        });
        imageCodeTextField.addPropertyChangeListener("value", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (imageCodeSettingValue) return;
                if (!(evt.getNewValue() instanceof String)) return;
                String s = (String) evt.getNewValue();
                if (s.length() != 16) return;

                try {
                    imageEditorPanel.getDeviceImage().setByteData(HexUtils.parseHexBytes(s, 0, 8));
                    imageEditorPanel.repaint();
                } catch (Exception ignored) {
                }
            }
        });
    }

    private void editImage(int imageId) {
        ImageEditorPanel iep = getImageEditorPanel(imageCurrent);
        if (iep != null) iep.setSelected(false);

        iep = getImageEditorPanel(imageId);
        if (iep != null) iep.setSelected(true);

        imageCurrent = imageId;
        DeviceImage img = device.getImages()[imageId];

        imageIgnoredCheckbox.setSelected(img.isIgnored());
        imageEditorPanel.setDeviceImage(img);

        refreshImageCode();
    }

    private void refreshImageCode() {
        imageCodeSettingValue = true;
        imageCodeTextField.setValue(HexUtils.toHexBytes(imageEditorPanel.getDeviceImage().getByteData()));
        imageCodeSettingValue = false;
    }

    private void createImageList() {
        imageOverviewPanel.removeAll();
        for (int i = 0; i < device.getMaxImages(); i++) {
            ImageEditorPanel iep = new ImageEditorPanel();
            iep.setMinimumSize(new Dimension(98, 98));
            iep.setPreferredSize(iep.getMinimumSize());
            iep.setSelected(i == imageCurrent);
            iep.setImageIndex(i);
            iep.addMouseListener(imageDoubleClickListener);
            imageOverviewPanel.add(iep);
        }
    }

    private ImageEditorPanel getImageEditorPanel(int imageId) {
        try {
            return (ImageEditorPanel) imageOverviewPanel.getComponent(imageId);
        } catch (Exception e) {
            return null;
        }
    }

    public void refreshImage(int imageIndex) {
        if (imageIndex == imageCurrent) {
            editImage(imageCurrent);
        }
        ImageEditorPanel iep = getImageEditorPanel(imageIndex);
        if (iep != null) iep.setDeviceImage(device.getImages()[imageIndex]);
    }

    @Override
    public String getTitle() {
        return "Images";
    }

    @Override
    public JPanel getRootPanel() {
        return imagePanel;
    }

    @Override
    public void enableAll(boolean b) {
        imageSaveButton.setEnabled(b);
        imageSwitchButton.setEnabled(b);
        imageOverviewPanel.setEnabled(b);
    }

    @Override
    public void refresh() {
        if (imageOverviewPanel.getComponentCount() == 0)
            createImageList();
    }
}
