package thecave.matrixcontrol.gui;

import thecave.matrixcontrol.gui.utils.IconHelper;
import thecave.matrixcontrol.gui.utils.SwingDeviceListener;
import thecave.matrixcontrol.device.*;
import thecave.matrixcontrol.gui.containers.AnimationContainer;
import thecave.matrixcontrol.gui.containers.ImageContainer;
import thecave.matrixcontrol.gui.containers.InfoContainer;
import thecave.matrixcontrol.gui.containers.TextContainer;
import thecave.matrixcontrol.sbridge.SerialThread;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class DeviceFrame extends JFrame implements DeviceListener, TabContainer.NeedsSaveHandler, AppleSupport.QuitResponder {

    private final ConnectionFrame connectionFrame;
    private final Device device;
    private boolean initialLoad = true;

    private JPanel mainPanel;
    private JButton discardButton;
    private JButton saveButton;
    private JLabel statusLabel;

    private JTabbedPane tabbedPane;

    private InfoContainer infoContainer;
    private ImageContainer imageContainer;
    private AnimationContainer animationContainer;
    private TextContainer textContainer;
    private boolean needsSave;

    private final AppleSupport appleSupport;

    private void addTab(TabContainer tc) {
        tabbedPane.addTab(tc.getTitle(), tc.getRootPanel());
    }

    DeviceFrame(final ConnectionFrame connectionFrame, SerialThread serialThread, AppleSupport appleSupport) {
        super("LedMatrix control");
        this.appleSupport = appleSupport;
        this.connectionFrame = connectionFrame;
        this.device = new Device(serialThread, new SwingDeviceListener(this));
        IconHelper.setIcon(this);

        addTab(imageContainer = new ImageContainer(device, this));
        addTab(animationContainer = new AnimationContainer(device, this));
        addTab(textContainer = new TextContainer(device, this));
        addTab(infoContainer = new InfoContainer(device, this));

        enableAll(false);
        resetNeedsSave();

        this.setContentPane(mainPanel);
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.setPreferredSize(new Dimension(800, 600));
        this.pack();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (askForQuit())
                    showParent();
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetNeedsSave();
                enableAll(false);
                statusLabel.setText("Saving... Don't exit now!");
                device.save();
            }
        });
        discardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int d = JOptionPane.showOptionDialog(DeviceFrame.this, "Do you really want to delete all changes since last EEPROM save?", getTitle(), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Yes", "No"}, "No");
                if (d != 0) return;

                resetNeedsSave();
                enableAll(false);
                initialLoad = true;
                statusLabel.setText("Reverting changes");
                device.revert();
            }
        });

        if (appleSupport != null) appleSupport.setQuitResponder(this);
        device.start();
    }

    private void enableAll(boolean b) {
        setCursor(b ? Cursor.getDefaultCursor() : Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        saveButton.setEnabled(b);
        discardButton.setEnabled(b);

        infoContainer.enableAll(b);
        imageContainer.enableAll(b);
        animationContainer.enableAll(b);
        textContainer.enableAll(b);
    }

    private void showParent() {
        if (appleSupport != null) appleSupport.setQuitResponder(null);
        device.stop();
        dispose();
        connectionFrame.showAgain();
    }

    @Override
    public void imageLoaded(Device device, int imageIndex) {
        if (initialLoad) {
            statusLabel.setText("Loaded image " + imageIndex + "/" + device.getMaxImages());
        }

        imageContainer.refreshImage(imageIndex);
        animationContainer.refresh();
    }

    @Override
    public void animationLoaded(Device device, int animIndex) {
        if (initialLoad) {
            statusLabel.setText("Loaded animation " + animIndex + "/" + device.getMaxAnimations());
        }

        animationContainer.refreshAnimation(animIndex);
    }

    @Override
    public void paramsChanged(Device device) {
        infoContainer.refresh();
        imageContainer.refresh();
        animationContainer.refresh();
        textContainer.refresh();
    }

    @Override
    public void commandFailed(Device device, String response) {
        JOptionPane.showMessageDialog(this, "Communication with device failed: " + response, getTitle(), JOptionPane.ERROR_MESSAGE);
        showParent();
    }

    @Override
    public void loadFinished(Device device) {
        initialLoad = false;
        statusLabel.setText("All loaded");

        infoContainer.refresh();
        imageContainer.refresh();
        animationContainer.refresh();
        textContainer.refresh();
        enableAll(true);
    }

    @Override
    public void saveFinished(Device device) {
        statusLabel.setText("Saved");
        enableAll(true);
    }

    @Override
    public void setNeedsSaveFlag() {
        needsSave = true;
        if (appleSupport != null) appleSupport.setChanged(true);
    }

    private void resetNeedsSave() {
        needsSave = false;
        if (appleSupport != null) appleSupport.setChanged(false);
    }

    private boolean askForQuit() {
        if (!needsSave) return true;

        int d = JOptionPane.showOptionDialog(DeviceFrame.this, "By disconnecting now you will lose all changes since last EEPROM save. Do you really want to disconnect?", getTitle(), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Yes", "No"}, "No");
        return d == 0;
    }

    @Override
    public void canQuit(AppleSupport.QuitResponderDecision decision) {
        if (askForQuit()) {
            decision.canQuit();
        } else {
            decision.dontQuit();
        }
    }
}
