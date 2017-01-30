package thecave.matrixcontrol.device;

import thecave.matrixcontrol.sbridge.SerialCommand;


interface CommandResolver<T extends SerialCommand> {
    void resolve(T cmd);
}
