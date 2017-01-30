package thecave.matrixcontrol.device;

public enum DeviceMode {
    IMAGES('0'), ANIMATIONS('1'), TEXT('2');

    private final char modeChar;

    DeviceMode(char modeChar) {
        this.modeChar = modeChar;
    }

    public char getModeChar() {
        return modeChar;
    }

    public static DeviceMode fromChar(char mode) {
        for (DeviceMode dm : values()) {
            if (dm.modeChar == mode) {
                return dm;
            }
        }
        throw new IllegalArgumentException("Unknown mode " + mode);
    }

}
