package thecave.matrixcontrol.gui.utils;

import thecave.matrixcontrol.device.Device;
import thecave.matrixcontrol.device.DeviceListener;

import javax.swing.*;


public class SwingDeviceListener implements DeviceListener {
    private final DeviceListener delegate;

    public SwingDeviceListener(DeviceListener delegate) {
        this.delegate = delegate;
    }

    @Override
    public void imageLoaded(final Device device, final int imageIndex) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                delegate.imageLoaded(device, imageIndex);
            }
        });
    }

    @Override
    public void animationLoaded(final Device device, final int animIndex) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                delegate.animationLoaded(device, animIndex);
            }
        });
    }

    @Override
    public void paramsChanged(final Device device) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                delegate.paramsChanged(device);
            }
        });
    }

    @Override
    public void commandFailed(final Device device, final String response) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                delegate.commandFailed(device, response);
            }
        });
    }

    @Override
    public void loadFinished(final Device device) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                delegate.loadFinished(device);
            }
        });
    }

    @Override
    public void saveFinished(final Device device) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                delegate.saveFinished(device);
            }
        });
    }
}
