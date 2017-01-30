//
//  LedMatrix
//  part of Let's make series
//
//  The Cave, 2017
//  https://thecave.cz
//
//  Licensed under MIT License (see LICENSE file for details)
//

package thecave.matrixcontrol;


import thecave.matrixcontrol.gui.AppleSupport;
import thecave.matrixcontrol.gui.ConnectionFrame;

import javax.swing.*;

public class Main {

    private static AppleSupport setupAppleSuport() {
        try {
            if (Class.forName("com.apple.eawt.Application") != null) {
                Class<?> c = Class.forName("thecave.matrixcontrol.gui.AppleSupportImpl");
                return (AppleSupport) c.newInstance();

            }
        } catch (Exception ignored) {
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        final AppleSupport appleSupport = setupAppleSuport();

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                ConnectionFrame mainForm = new ConnectionFrame(appleSupport);
                mainForm.setLocationRelativeTo(null);
                mainForm.setVisible(true);
            }
        });
    }
}
