package thecave.matrixcontrol.device;

import thecave.matrixcontrol.sbridge.SerialCommand;
import thecave.matrixcontrol.sbridge.SerialThread;

import java.util.HashMap;
import java.util.Map;

public class CommandResolverManager implements SerialThread.CommandListener {
    private Map<Class<?>, CommandResolver<? extends SerialCommand>> map = new HashMap<>();

    public <T extends SerialCommand> void add(Class<T> clazz, CommandResolver<T> resolver) {
        map.put(clazz, resolver);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void commandResolved(SerialCommand cmd) {
        CommandResolver c = map.get(cmd.getClass());
        if (c != null) {
            try {
                c.resolve(cmd);
            } catch (RuntimeException ignored) {

            }
        }
    }

}
