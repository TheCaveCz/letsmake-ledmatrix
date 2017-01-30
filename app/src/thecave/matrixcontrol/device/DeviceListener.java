package thecave.matrixcontrol.device;


public interface DeviceListener {
    void imageLoaded(Device device, int imageIndex);

    void animationLoaded(Device device, int animIndex);

    void paramsChanged(Device device);

    void commandFailed(Device device, String response);

    void loadFinished(Device device);

    void saveFinished(Device device);
}
