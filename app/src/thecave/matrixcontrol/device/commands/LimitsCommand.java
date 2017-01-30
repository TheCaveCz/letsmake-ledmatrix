package thecave.matrixcontrol.device.commands;

import thecave.matrixcontrol.device.HexUtils;

public class LimitsCommand extends AbstractCommand {
    private int maxImages;
    private int maxAnimations;
    private int maxTextLength;

    @Override
    public String getRequest() {
        return "L";
    }

    public int getMaxImages() {
        return maxImages;
    }

    public int getMaxAnimations() {
        return maxAnimations;
    }

    public int getMaxTextLength() {
        return maxTextLength;
    }

    @Override
    protected void parseResponse(String response) {
        maxImages = HexUtils.parseHexByte(response, 0);
        maxAnimations = HexUtils.parseHexByte(response, 2);
        maxTextLength = HexUtils.parseHexByte(response, 4);
    }

    @Override
    public String toString() {
        return "LimitsCommand{" +
                "maxImages=" + maxImages +
                ", maxAnimations=" + maxAnimations +
                ", maxTextLength=" + maxTextLength +
                '}';
    }
}
