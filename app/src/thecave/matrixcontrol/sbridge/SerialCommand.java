package thecave.matrixcontrol.sbridge;


public interface SerialCommand {
    enum Status {
        SUCESS, ERROR, TIMEOUT;
    }

    String getRequest();

    void setResponse(Status status, String response);
}
