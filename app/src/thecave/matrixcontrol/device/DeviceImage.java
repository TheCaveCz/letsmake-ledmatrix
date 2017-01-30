package thecave.matrixcontrol.device;


public class DeviceImage {
    public static final int MAX_X = 8;
    public static final int MAX_Y = 8;

    private boolean ignored;
    private boolean[] imageData;

    public DeviceImage() {
        imageData = new boolean[MAX_X * MAX_Y];
    }

    public void copyFrom(DeviceImage img) {
        System.arraycopy(img.imageData, 0, imageData, 0, imageData.length);
        ignored = img.ignored;
    }

    public int[] getByteData() {
        int[] result = new int[MAX_X * MAX_Y / 8];
        for (int x = 0; x < MAX_X; x++) {
            int data = 0;
            for (int y = 0; y < MAX_Y; y++) {
                data <<= 1;
                data |= imageData[x * MAX_Y + y] ? 1 : 0;
            }
            result[x] = data;
        }
        return result;
    }

    public void setByteData(int[] byteData) {
        if (byteData.length * 8 != imageData.length)
            throw new IllegalArgumentException("Invalid image data length");

        for (int x = 0; x < MAX_X; x++) {
            for (int y = 0; y < MAX_Y; y++) {
                imageData[x * MAX_Y + y] = (byteData[x] & (1 << (MAX_Y - y - 1))) != 0;
            }
        }
    }

    public void setPixel(int x, int y, boolean value) {
        if (x >= 0 && x < MAX_X && y >= 0 && y < MAX_Y)
            imageData[x * MAX_Y + y] = value;
    }

    public boolean getPixel(int x, int y) {
        return x >= 0 && x < MAX_X && y >= 0 && y < MAX_Y && imageData[x * MAX_Y + y];
    }

    public boolean isIgnored() {
        return ignored;
    }

    public void setIgnored(boolean ignored) {
        this.ignored = ignored;
    }

    public String debugString() {
        StringBuilder sb = new StringBuilder();
        for (int y = 0; y < MAX_Y; y++) {
            for (int x = 0; x < MAX_X; x++) {
                sb.append(imageData[x * MAX_Y + y] ? 'X' : ' ');
            }
            sb.append('\n');
        }
        return sb.toString();
    }
}
