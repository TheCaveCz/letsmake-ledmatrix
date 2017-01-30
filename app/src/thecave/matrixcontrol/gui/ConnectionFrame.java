package thecave.matrixcontrol.gui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import thecave.matrixcontrol.device.commands.InfoCommand;
import thecave.matrixcontrol.gui.utils.IconHelper;
import thecave.matrixcontrol.sbridge.NamedSerialPortFactory;
import thecave.matrixcontrol.sbridge.SerialCommand;
import thecave.matrixcontrol.sbridge.SerialPortFactory;
import thecave.matrixcontrol.sbridge.SerialThread;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutionException;

public class ConnectionFrame extends JFrame implements SerialThread.CommandListener {
    private static Logger log = LogManager.getLogger(ConnectionFrame.class);

    private final AppleSupport appleSupport;
    private JPanel mainPanel;
    private JButton reloadButton;
    private JComboBox<String> portCombo;
    private JButton connectButton;
    private JLabel statusLabel;

    public ConnectionFrame(AppleSupport appleSupport) throws HeadlessException {
        super("LedMatrix control");
        this.appleSupport = appleSupport;

        IconHelper.setIcon(this);
        reloadButtonAction();

        this.setContentPane(mainPanel);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.pack();

        reloadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reloadButtonAction();
            }
        });
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = portCombo.getSelectedIndex();
                if (index < 0) return;

                connectAction(portCombo.getItemAt(index));
            }
        });
    }

    private void enableAll(boolean b) {
        setCursor(b ? Cursor.getDefaultCursor() : Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        connectButton.setEnabled(b);
        reloadButton.setEnabled(b);
        portCombo.setEnabled(b);
        if (b) statusLabel.setText("Ready to connect");
    }

    private void showError(String error) {
        JOptionPane.showMessageDialog(ConnectionFrame.this, "Unable to connect: " + error, getTitle(), JOptionPane.ERROR_MESSAGE);
    }

    private void reloadButtonAction() {
        portCombo.setModel(new DefaultComboBoxModel<>(NamedSerialPortFactory.getPortNames()));
//        portCombo.setSelectedIndex(2);//TODO : debug!!
    }

    private void connectAction(String portName) {
        if (portName == null) return;


        final SerialPortFactory spf = new NamedSerialPortFactory(portName);
        final SerialThread st = new SerialThread(spf);
        enableAll(false);
        statusLabel.setText("Connecting to " + portName);

        new SwingWorker<Object, Object>() {

            @Override
            protected Object doInBackground() throws Exception {
                st.open(ConnectionFrame.this);
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                    statusLabel.setText("Connected, probing device");
                    st.addCommand(new ProbeCommand(st));
                    return;
                } catch (ExecutionException e) {
                    log.warn("Failed to connect", e);
                    showError(e.getCause().getMessage());
                } catch (InterruptedException ignored) {
                }
                enableAll(true);
            }
        }.execute();
    }

    @Override
    public void commandResolved(final SerialCommand cmd) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    if (!(cmd instanceof ProbeCommand)) throw new IllegalStateException("Received invalid response");

                    ProbeCommand ic = (ProbeCommand) cmd;
                    if (ic.getStatus() != SerialCommand.Status.SUCESS) {
                        ic.getSerialThread().stopRunning();
                        throw new IllegalStateException(ic.getResponse() == null ? "Device is not responding" : ic.getResponse());
                    }

                    connectionFinished(ic);
                } catch (Exception e) {
                    log.warn("Unable to probe device", e);
                    showError(e.getMessage());
                    enableAll(true);
                }
            }
        });
    }

    private void connectionFinished(ProbeCommand probeCommand) {
        statusLabel.setText("Connected to " + probeCommand.getName() + " " + probeCommand.getVersion());
        setVisible(false);

        DeviceFrame df = new DeviceFrame(this, probeCommand.getSerialThread(), appleSupport);
        df.setLocationRelativeTo(null);
        df.setVisible(true);
    }

    void showAgain() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                reloadButtonAction();
                enableAll(true);
                setVisible(true);
            }
        });
    }

    private static class ProbeCommand extends InfoCommand {
        private final SerialThread serialThread;

        ProbeCommand(SerialThread serialThread) {
            this.serialThread = serialThread;
        }

        SerialThread getSerialThread() {
            return serialThread;
        }

        @Override
        protected void parseResponse(String response) {
            super.parseResponse(response);

            if (!"TheCave-LedMatrix".equals(getName()))
                throw new IllegalStateException("This device is not LedMatrix!");

            if (!"1.0".equals(getVersion()))
                throw new IllegalStateException("Unknown LedMatrix version");
        }
    }
}
