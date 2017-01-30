package thecave.matrixcontrol.device.commands;

import thecave.matrixcontrol.device.DeviceMode;
import thecave.matrixcontrol.device.HexUtils;

public class GetModeCommand extends AbstractCommand {
    private DeviceMode deviceMode;
    private int itemIndex;

    @Override
    public String getRequest() {
        return "M";
    }

    @Override
    protected void parseResponse(String response) {
        deviceMode = DeviceMode.fromChar(response.charAt(0));
        itemIndex = HexUtils.parseHexByte(response, 1);
    }

    public DeviceMode getDeviceMode() {
        return deviceMode;
    }

    public int getItemIndex() {
        return itemIndex;
    }

    @Override
    public String toString() {
        return "GetModeCommand{" +
                "deviceMode=" + deviceMode +
                ", itemIndex=" + itemIndex +
                '}';
    }
}
