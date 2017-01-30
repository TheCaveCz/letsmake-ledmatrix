package thecave.matrixcontrol.gui;


import javax.swing.*;

public interface TabContainer {
    interface NeedsSaveHandler {
        void setNeedsSaveFlag();
    }

    String getTitle();

    JPanel getRootPanel();

    void enableAll(boolean b);

    void refresh();

}
