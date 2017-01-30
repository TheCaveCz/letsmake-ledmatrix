package thecave.matrixcontrol.device.commands;


public class InfoCommand extends AbstractCommand {
    private String name;
    private String version;

    @Override
    public String getRequest() {
        return "?";
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    @Override
    protected void parseResponse(String response) {
        String[] split = response.split("\\/");
        if (split.length != 2) throw new IllegalStateException("Unknown version string '" + response + "'");

        name = split[0];
        version = split[1];
    }

    @Override
    public String toString() {
        return "InfoCommand{" +
                "name='" + name + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}
