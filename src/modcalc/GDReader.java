package modcalc;

public class GDReader {

    public static byte[] getBytes4(byte[] bytes, int offset) {
        return GDReader.getBytes(bytes, offset, 4);
    }

    public static int getUInt(byte[] bytes, int offset) {
        int size = 4;

        int val = 0;
        for (int i = size; i > 0; i = i - 1) {
            int temp = bytes[offset + i - 1];
            if (temp < 0) {
                temp = 256 + temp;
            }

            val = val * 256 + temp;
        }

        return val;
    }

    public static byte[] getBytes(byte[] bytes, int offset, int len) {
        // return Arrays.copyOfRange(bytes, offset, len);
        if (len <= 0) {
            return null;
        }

        byte[] b = new byte[len];

        for (int i = 0; i < len; i = i + 1) {
            b[i] = bytes[offset + i];
        }

        return b;
    }

}
