package thecave.matrixcontrol.device.commands;

import thecave.matrixcontrol.device.DeviceAnimation;
import thecave.matrixcontrol.device.HexUtils;

public class AnimationCommand extends AbstractCommand {
    private final int animIndex;
    private final String request;
    private DeviceAnimation animation;

    public AnimationCommand(int animIndex) {
        this.animIndex = animIndex;
        this.request = "A" + HexUtils.toHexByte(animIndex);
    }

    public AnimationCommand(int animIndex, DeviceAnimation animation) {
        this.animation = animation;
        this.animIndex = animIndex;

        this.request = "A" + HexUtils.toHexByte(animIndex)
                + animation.getTransition().getValue()
                + HexUtils.toHexByte(animation.getDelayFrames())
                + (animation.isLoop() ? '1' : '0')
                + HexUtils.toHexDigit(animation.getFramesCount() - 1)
                + HexUtils.toHexBytes(animation.getFilteredFrames());
    }

    public int getAnimIndex() {
        return animIndex;
    }

    public DeviceAnimation getAnimation() {
        return animation;
    }

    @Override
    public String getRequest() {
        return request;
    }

    @Override
    protected void parseResponse(String response) {
        if (animIndex != HexUtils.parseHexByte(response, 0))
            throw new IllegalStateException("AnimID mismatch");

        if (animation == null) animation = new DeviceAnimation();
        animation.setTransition(DeviceAnimation.Transition.fromValue(response.charAt(2)));
        animation.setDelayFrames(HexUtils.parseHexByte(response, 3));
        animation.setLoop(HexUtils.parseHexDigit(response.charAt(5)) != 0);

        int rawLen = HexUtils.parseHexDigit(response.charAt(6)) + 1;
        animation.setFilteredFrames(HexUtils.parseHexBytes(response, 7, 8), rawLen);
    }

    @Override
    public String toString() {
        return "AnimationCommand{" +
                "animIndex=" + animIndex +
                ", animation=" + animation +
                '}';
    }
}
