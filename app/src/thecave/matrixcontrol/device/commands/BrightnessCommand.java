package thecave.matrixcontrol.device.commands;


import thecave.matrixcontrol.device.HexUtils;

public class BrightnessCommand extends AbstractCommand {
    private int brightness;
    private final String request;

    public BrightnessCommand(int brightness) {
        this.brightness = brightness;
        this.request = "B" + HexUtils.toHexDigit(brightness);
    }

    public BrightnessCommand() {
        this.brightness = 15;
        this.request = "B";
    }

    public int getBrightness() {
        return brightness;
    }

    @Override
    public String getRequest() {
        return request;
    }

    @Override
    protected void parseResponse(String response) {
        brightness = HexUtils.parseHexDigit(response.charAt(0));
    }

    @Override
    public String toString() {
        return "BrightnessCommand{" +
                "brightness=" + brightness +
                '}';
    }
}
