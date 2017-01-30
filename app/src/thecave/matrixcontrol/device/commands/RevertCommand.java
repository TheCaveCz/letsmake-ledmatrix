package thecave.matrixcontrol.device.commands;


public class RevertCommand extends AbstractCommand {

    @Override
    public String getRequest() {
        return "X";
    }

    @Override
    protected void parseResponse(String response) {
    }
}
