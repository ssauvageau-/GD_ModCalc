package modcalc;

public class DDSHeader {

    public static final int HEADER_SIZE = 92 + DDSPixelFormat.HEADER_SIZE; // 23 * int + 1 * pixel format

    public static final int VERSION_DEFAULT = 0x20;
    public static final int VERSION_REVERSED = 0x52;

    public static final int HEADER_CAPS = 0x01;
    public static final int HEADER_HEIGHT = 0x02;
    public static final int HEADER_WIDTH = 0x04;
    public static final int HEADER_PITCH = 0x08;
    public static final int HEADER_PIXELFORMAT = 0x1000;
    public static final int HEADER_MIPMAP_COUNT = 0x20000;
    public static final int HEADER_LINEAR_SIZE = 0x80000;
    public static final int HEADER_DEPTH = 0x800000;

    public static final int CAPS_COMPLEX = 0x08;
    public static final int CAPS_TEXTURE = 0x1000;
    public static final int CAPS_MIPMAP = 0x400000;

    public static final int CAPS2_CUBEMAP = 0x0200;
    public static final int CAPS2_CUBE_POS_X = 0x0400;
    public static final int CAPS2_CUBE_NEG_X = 0x0800;
    public static final int CAPS2_CUBE_POS_Y = 0x1000;
    public static final int CAPS2_CUBE_NEG_Y = 0x2000;
    public static final int CAPS2_CUBE_POS_Z = 0x4000;
    public static final int CAPS2_CUBE_NEG_Z = 0x8000;
    public static final int CAPS2_VOLUME = 0x200000;

    public byte[] version;   // 4 bytes
    public int size;
    public int flags;
    public int height;
    public int width;
    public int linearSize;
    public int depth;
    public int num_mipmap;
    public int[] reserved1;  // 11 reserved ints
    public DDSPixelFormat pixelFormat;
    public int caps;
    public int caps2;
    public int caps3;
    public int caps4;
    public int reserved2;
    

    public DDSHeader() {
        version = new byte[4];
        pixelFormat = new DDSPixelFormat();
        reserved1 = new int[11];
    }

}
