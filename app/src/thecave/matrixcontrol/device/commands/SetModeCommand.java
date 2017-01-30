package thecave.matrixcontrol.device.commands;


import thecave.matrixcontrol.device.DeviceMode;
import thecave.matrixcontrol.device.HexUtils;

public class SetModeCommand extends AbstractCommand {
    private final String request;
    private final DeviceMode deviceMode;
    private int itemIndex;

    public SetModeCommand(DeviceMode mode) {
        this(mode, 255);
    }

    public SetModeCommand(DeviceMode mode, int itemIndex) {
        this.deviceMode = mode;
        this.request = "M" + mode.getModeChar() + HexUtils.toHexByte(itemIndex);
        this.itemIndex = itemIndex;
    }

    @Override
    public String getRequest() {
        return request;
    }

    public int getItemIndex() {
        return itemIndex;
    }

    public DeviceMode getDeviceMode() {
        return deviceMode;
    }

    @Override
    protected void parseResponse(String response) {
        if (deviceMode != DeviceMode.fromChar(response.charAt(0)))
            throw new IllegalStateException("Failed to set mode");

        itemIndex = HexUtils.parseHexByte(response, 1);
    }

    @Override
    public String toString() {
        return "SetModeCommand{" +
                "deviceMode=" + deviceMode +
                ", itemIndex=" + itemIndex +
                '}';
    }
}
