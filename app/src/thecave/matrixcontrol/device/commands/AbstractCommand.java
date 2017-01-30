package thecave.matrixcontrol.device.commands;

import thecave.matrixcontrol.sbridge.SerialCommand;

public abstract class AbstractCommand implements SerialCommand {
    private String response;
    private Status status;

    public Status getStatus() {
        return status;
    }

    public String getResponse() {
        return response;
    }

    @Override
    public void setResponse(Status status, String response) {
        if (status == Status.SUCESS && response != null && response.length() > 0) {
            if (response.startsWith("+")) {
                this.response = response.substring(1);
                try {
                    parseResponse(this.response);
                    this.status = Status.SUCESS;
                } catch (RuntimeException e) {
                    this.status = Status.ERROR;
                    this.response = e.getMessage();
                }
            } else {
                this.status = Status.ERROR;
                this.response = response.startsWith("-") ? response.substring(1) : response;
            }
        } else {
            this.response = response;
            this.status = status;
        }
    }

    protected abstract void parseResponse(String response);

}
