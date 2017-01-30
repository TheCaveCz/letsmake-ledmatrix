package thecave.matrixcontrol.device.commands;


import thecave.matrixcontrol.device.HexUtils;

public class TextCommand extends AbstractCommand {
    private final String request;
    private String text;
    private int speed;

    public TextCommand(String text, int speed) {
        this.text = text;
        this.request = "T" + HexUtils.toHexByte(speed) + text;
    }

    public TextCommand() {
        this.request = "T";
    }

    @Override
    public String getRequest() {
        return request;
    }

    public String getText() {
        return text;
    }

    public int getSpeed() {
        return speed;
    }

    @Override
    protected void parseResponse(String response) {
        speed = HexUtils.parseHexByte(response, 0);
        text = response.substring(2);
    }

    @Override
    public String toString() {
        return "TextCommand{" +
                "text='" + text + '\'' +
                ", speed=" + speed +
                '}';
    }
}
