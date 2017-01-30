package thecave.matrixcontrol.gui;


public interface AppleSupport {

    interface QuitResponderDecision {
        void canQuit();

        void dontQuit();
    }

    interface QuitResponder {
        void canQuit(QuitResponderDecision decision);
    }

    void setQuitResponder(QuitResponder qr);

    void setChanged(boolean changed);

}
