package thecave.matrixcontrol.gui.components;

import thecave.matrixcontrol.device.DeviceImage;


public interface ImageSource {
    int getImageCount();

    DeviceImage getImageWithId(int imageIndex);
}
