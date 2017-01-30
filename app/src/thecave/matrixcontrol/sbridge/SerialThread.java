package thecave.matrixcontrol.sbridge;

import com.fazecast.jSerialComm.SerialPort;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.util.LinkedList;
import java.util.List;

public class SerialThread extends Thread {
    private static final int AFTER_OPEN_DELAY = 100;
    private static final int READ_TIMEOUT = 1000;
    private static final String WELCOME_STRING = "+ready";

    private static Logger log = LogManager.getLogger(SerialThread.class);

    private final SerialPortFactory serialPortFactory;
    private SerialPort serialPort;
    private volatile boolean running = false;

    private final List<SerialCommand> cmdList = new LinkedList<>();
    private CommandListener listener;

    public SerialThread(SerialPortFactory serialPortFactory) {
        super("SerialThread");
        this.serialPortFactory = serialPortFactory;
    }

    public void open(CommandListener listener) throws SerialPortException {
        if (!running) {
            if (listener != null) {
                this.listener = listener;
            }

            createPort();
            running = true;
            start();
        }
    }

    public boolean isRunning() {
        return running;
    }

    public void stopRunning() {
        running = false;
        this.interrupt();
    }

    public void addCommand(SerialCommand cmd) {
        if (cmd.getRequest() == null) {
            throw new IllegalStateException("Command request must not be null");
        }
        synchronized (cmdList) {
            cmdList.add(cmd);
            cmdList.notify();
        }
    }

    public void setListener(CommandListener listener) {
        this.listener = listener;
    }

    private void closePort() {
        if (serialPort != null) {
            if (serialPort.isOpen()) {
                log.info("Closing existing port " + serialPort.getSystemPortName());
                serialPort.closePort();
                serialPort = null;
            } else {
                log.info("Discarding unopened port " + serialPort.getSystemPortName());
                serialPort = null;
            }

        }
    }

    private void createPort() throws SerialPortException {
        log.info("Opening serial port");
        SerialPort port = serialPortFactory.createPort();
        if (port == null) {
            throw new SerialPortException("Unable to create port");
        }

        closePort();

        if (!port.openPort()) {
            throw new SerialPortException("Unable to open port " + port.getSystemPortName());
        }

        byte[] buffer = new byte[256];
        int numRead;
        int retries = 30;
        while (true) {
            if (port.bytesAvailable() >= WELCOME_STRING.length()) {
                numRead = port.readBytes(buffer, buffer.length);
                if (numRead < 0) {
                    throw new SerialPortException("Failed to read initial data");
                }

                String initString = new String(buffer, 0, numRead).trim();
                log.trace("Device sent welcome string '" + initString + "'");
                if (initString.contains(WELCOME_STRING)) {
                    break;
                }
            }

            retries--;
            if (retries <= 0) {
                throw new SerialPortException("Device not responding");
            }

            try {
                Thread.sleep(AFTER_OPEN_DELAY);
            } catch (InterruptedException e) {
                throw new SerialPortException("Wait for data interrupted", e);
            }
        }

        serialPort = port;
    }

    private void resolveCommand(SerialCommand cmd) {
        CommandListener l = listener;
        if (l != null) {
            l.commandResolved(cmd);
        }
    }

    private boolean findNewline(byte[] buffer, int read) {
        while (read > 0) {
            read--;
            if (buffer[read] == '\n') {
                return true;
            }
        }
        return false;
    }

    @Override
    public void run() {
        log.info("Thread started");
        try {
            byte[] buffer = new byte[1024];

            while (running) {
                if (serialPort == null) {
                    break;
                    /*
                    try {
                        createPort();
                    } catch (SerialPortException e) {
                        log.warn("Can't open serial, trying again");
                        Thread.sleep(2000);
                        continue;
                    }*/

                }

                SerialCommand cmd;
                synchronized (cmdList) {
                    while (cmdList.size() == 0) {
                        cmdList.wait(1000);
                    }
                    cmd = cmdList.remove(0);
                }

                // drain serial buffer before sending command
                while (serialPort.bytesAvailable() > 0) {
                    int read = serialPort.readBytes(buffer, buffer.length);
                    if (read > 0) {
                        log.debug("Drained serial buffer, got {}", new String(buffer, 0, read));
                    }
                }

                log.trace("Sending command {}", cmd.getRequest());
                byte[] cmdBuf = (cmd.getRequest() + "\n").getBytes("UTF-8");
                int wrote = serialPort.writeBytes(cmdBuf, cmdBuf.length);
                if (wrote != cmdBuf.length) {
                    log.warn("Write failed: should write {} but wrote {}", cmdBuf.length, wrote);
                    if (wrote == -1) {
                        cmd.setResponse(SerialCommand.Status.ERROR, null);
                        resolveCommand(cmd);
                        closePort();
                        continue;
                    }
                }

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                long readStart = System.currentTimeMillis();
                boolean hasResponse = false;
                while (System.currentTimeMillis() - readStart < READ_TIMEOUT) {
                    if (serialPort.bytesAvailable() > 0) {
                        int read = serialPort.readBytes(buffer, buffer.length);
                        if (read > 0) {
                            baos.write(buffer, 0, read);
                            if (findNewline(buffer, read)) {
                                hasResponse = true;
                                byte[] cmdResponse = baos.toByteArray();
                                String responseString = new String(cmdResponse, 0, cmdResponse.length).trim();
                                log.trace("Got response '{}' -> '{}'", cmd.getRequest(), responseString);
                                cmd.setResponse(SerialCommand.Status.SUCESS, responseString);
                                break;
                            }
                        } else if (read < 0) {
                            log.warn("Read failed for command '{}'", cmd.getRequest());
                            hasResponse = true;
                            cmd.setResponse(SerialCommand.Status.ERROR, null);
                            closePort();
                            break;
                        }
                    }
                    Thread.sleep(50);
                }

                if (!hasResponse) {
                    log.warn("Read timed out for command '{}'", cmd.getRequest());
                    cmd.setResponse(SerialCommand.Status.TIMEOUT, null);
                }

                resolveCommand(cmd);

            }
            log.info("Thread exited");
        } catch (Exception ie) {
            log.warn("Thread failed with", ie);
        }
        closePort();
        running = false;
    }


    public interface CommandListener {
        void commandResolved(SerialCommand cmd);
    }
}
