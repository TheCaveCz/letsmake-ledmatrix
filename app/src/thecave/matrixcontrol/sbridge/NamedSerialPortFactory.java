package thecave.matrixcontrol.sbridge;

import com.fazecast.jSerialComm.SerialPort;

import java.util.Arrays;

public class NamedSerialPortFactory implements SerialPortFactory {
    private final String portName;

    public NamedSerialPortFactory(String portName) {
        this.portName = portName;
    }

    @Override
    public SerialPort createPort() {

        SerialPort port = null;
        for (SerialPort p : SerialPort.getCommPorts()) {
            if (portName.equals(p.getSystemPortName())) {
                port = p;
                port.setComPortParameters(115200, 8, 1, SerialPort.NO_PARITY);
                break;
            }
        }
        return port;
    }

    public static String[] getPortNames() {
        SerialPort[] ports = SerialPort.getCommPorts();
        String[] result = new String[ports.length];
        for (int i = 0; i < ports.length; i++) {
            result[i] = ports[i].getSystemPortName();
        }
        return result;
    }
}
