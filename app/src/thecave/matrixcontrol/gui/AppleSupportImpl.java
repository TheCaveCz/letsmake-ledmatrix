package thecave.matrixcontrol.gui;

import com.apple.eawt.AboutHandler;
import com.apple.eawt.AppEvent;
import com.apple.eawt.AppEvent.AboutEvent;
import com.apple.eawt.AppEvent.QuitEvent;
import com.apple.eawt.Application;
import com.apple.eawt.OpenFilesHandler;
import com.apple.eawt.QuitHandler;
import com.apple.eawt.QuitResponse;
import thecave.matrixcontrol.gui.utils.IconHelper;

import javax.swing.*;
import java.awt.*;

/**
 * @author Jindra
 */
public class AppleSupportImpl implements AppleSupport {
    //private MainFrame mainFrame;
    private final Application application;
    private QuitResponder quitResponder;

    public AppleSupportImpl() {
        application = Application.getApplication();
        application.setAboutHandler(new AboutHandler() {
            @Override
            public void handleAbout(AboutEvent aboutEvent) {
                JOptionPane.showMessageDialog(null, "LedMatrix control\n(c) The Cave, 2017\nhttps://www.thecave.cz", "About", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        application.setQuitHandler(new QuitHandler() {
            @Override
            public void handleQuitRequestWith(QuitEvent quitEvent, final QuitResponse quitResponse) {
                if (quitResponder == null) {
                    quitResponse.performQuit();
                    return;
                }

                quitResponder.canQuit(new QuitResponderDecision() {
                    @Override
                    public void canQuit() {
                        quitResponse.performQuit();
                    }

                    @Override
                    public void dontQuit() {
                        quitResponse.cancelQuit();
                    }
                });
            }
        });

        try {
            Image img = IconHelper.readIcon();
            if (img != null)
                application.setDockIconImage(img);
        } catch (Exception ignored) {
        }
    }

    @Override
    public void setQuitResponder(QuitResponder qr) {
        quitResponder = qr;
    }

    //    @Override
//    public void setMainFrame(MainFrame mf) {
//        mainFrame = mf;
//
//        application.setQuitHandler(new QuitHandler() {
//
//            @Override
//            public void handleQuitRequestWith(QuitEvent qe, QuitResponse qr) {
//                if (mainFrame.confirmQuit()) {
//                    qr.performQuit();
//                } else {
//                    qr.cancelQuit();
//                }
//            }
//        });
//
//        application.setAboutHandler(new AboutHandler() {
//
//            @Override
//            public void handleAbout(AboutEvent ae) {
//                mainFrame.showAboutMenu();
//            }
//        });
//    }

    @Override
    public void setChanged(boolean changed) {
        if (changed) {
            application.disableSuddenTermination();
        } else {
            application.enableSuddenTermination();
        }

    }

}
