package thecave.matrixcontrol.device;


public class HexUtils {

    public static int parseHexDigit(char digit) {
        int val = Character.getNumericValue(digit);
        if (val < 0 || val > 15) {
            throw new NumberFormatException(digit + " is not a hex digit");
        } else {
            return val;
        }
    }

    public static int parseHexByte(String s, int start) {
        return Integer.valueOf(s.substring(start, start + 2), 16);
    }

    public static int[] parseHexBytes(String s, int start, int length) {
        int[] result = new int[length];
        for (int i = 0; i < length; i++) {
            result[i] = parseHexByte(s, start + i * 2);
        }
        return result;
    }

    public static String toHexByte(int i) {
        if (i < 0 || i > 255) throw new IllegalArgumentException("Value is not in byte range");
        return String.format("%02x", i).toUpperCase();
    }

    public static char toHexDigit(int i) {
        if (i < 0 || i > 15) throw new IllegalArgumentException("Value is not in hex digit range");
        return Character.toUpperCase(Character.forDigit(i, 16));
    }

    public static String toHexBytes(int[] data) {
        StringBuilder sb = new StringBuilder();
        for (int i : data) {
            sb.append(toHexByte(i));
        }
        return sb.toString();
    }

}
