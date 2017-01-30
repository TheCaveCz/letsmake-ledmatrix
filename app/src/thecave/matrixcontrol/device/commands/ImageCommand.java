package thecave.matrixcontrol.device.commands;


import thecave.matrixcontrol.device.DeviceImage;
import thecave.matrixcontrol.device.HexUtils;

public class ImageCommand extends AbstractCommand {
    private final int imageIndex;
    private final String request;
    private DeviceImage image;

    public ImageCommand(int imageIndex) {
        this.imageIndex = imageIndex;
        this.request = "I" + HexUtils.toHexByte(imageIndex);
    }

    public ImageCommand(int imageIndex, DeviceImage image) {
        this.imageIndex = imageIndex;
        this.image = image;

        this.request = "I" + HexUtils.toHexByte(imageIndex)
                + (image.isIgnored() ? '1' : '0')
                + HexUtils.toHexBytes(image.getByteData());
    }

    public int getImageIndex() {
        return imageIndex;
    }

    public DeviceImage getImage() {
        return image;
    }

    @Override
    public String getRequest() {
        return request;
    }

    @Override
    protected void parseResponse(String response) {
        if (imageIndex != HexUtils.parseHexByte(response, 0))
            throw new IllegalStateException("ImageId mismatch");

        if (image == null) image = new DeviceImage();
        image.setIgnored(HexUtils.parseHexDigit(response.charAt(2)) != 0);
        image.setByteData(HexUtils.parseHexBytes(response, 3, 8));
    }

    @Override
    public String toString() {
        return "ImageCommand{" +
                "imageIndex=" + imageIndex +
                ", image=" + image +
                '}';
    }
}
