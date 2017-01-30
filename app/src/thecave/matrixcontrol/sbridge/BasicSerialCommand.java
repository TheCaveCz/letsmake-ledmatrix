package thecave.matrixcontrol.sbridge;

/**
 * dzindra: 28.01.17.
 */
public class BasicSerialCommand implements SerialCommand {
    private final String request;
    private String response;
    private Status status;

    public BasicSerialCommand(String request) {
        this.request = request;
    }

    @Override
    public String getRequest() {
        return request;
    }

    @Override
    public void setResponse(Status status, String response) {
        this.status = status;
        this.response = response;
    }

    public String getResponse() {
        return response;
    }

    public Status getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "BasicSerialCommand{" +
                "request='" + request + '\'' +
                ", response='" + response + '\'' +
                ", status=" + status +
                '}';
    }
}
