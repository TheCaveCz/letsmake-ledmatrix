package thecave.matrixcontrol.device.commands;


public class SaveCommand extends AbstractCommand {

    @Override
    public String getRequest() {
        return "W";
    }

    @Override
    protected void parseResponse(String response) {

    }
}
