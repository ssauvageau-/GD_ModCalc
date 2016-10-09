package modcalc;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class DDSLoader {

    public static BufferedImage getImage(byte[] bytes) throws Exception {
        TEXHeader texHdr;
        byte[] ddsBytes;
        DDSHeader ddsHdr;

        texHdr = DDSLoader.getTEXHeader(bytes);
        ddsBytes = DDSLoader.getDDSBytes(bytes, texHdr);
        ddsHdr = DDSLoader.getDDSHeader(ddsBytes);

        DDSLoader.fixDDSHeader(ddsBytes, ddsHdr);

        //DDSLoader.writeFile(file.getCanonicalPath(), ddsBytes);
        int[] pixels = DDSReader.read(ddsBytes, DDSReader.ARGB, 0);
        int width = DDSReader.getWidth(ddsBytes);
        int height = DDSReader.getHeight(ddsBytes);
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        image.setRGB(0, 0, width, height, pixels, 0, width);

        return image;
    }

    private static TEXHeader getTEXHeader(byte[] bytes) throws Exception {
        TEXHeader header = new TEXHeader();

        header.version = GDReader.getBytes4(bytes, 0);
        header.unknown = GDReader.getUInt(bytes, 4);
        header.size = GDReader.getUInt(bytes, 8);

        if ((header.version[3] > 2)) {
            throw new Exception();
        }

        return header;
    }

    private static byte[] getDDSBytes(byte[] bytes, TEXHeader texHdr) {
        byte[] b = GDReader.getBytes(bytes, 12, texHdr.size);

        return b;
    }

    private static DDSHeader getDDSHeader(byte[] bytes) throws Exception {
        DDSHeader header = new DDSHeader();

        int offset = 0;

        header.version = GDReader.getBytes4(bytes, offset);
        offset = offset + 4;

        header.size = GDReader.getUInt(bytes, offset);
        offset = offset + 4;

        if ((header.size != DDSHeader.HEADER_SIZE)) {
            throw new Exception();
        }

        header.flags = GDReader.getUInt(bytes, offset);
        offset = offset + 4;

        header.height = GDReader.getUInt(bytes, offset);
        offset = offset + 4;

        header.width = GDReader.getUInt(bytes, offset);
        offset = offset + 4;

        header.linearSize = GDReader.getUInt(bytes, offset);
        offset = offset + 4;

        header.depth = GDReader.getUInt(bytes, offset);
        offset = offset + 4;

        header.num_mipmap = GDReader.getUInt(bytes, offset);
        offset = offset + 4;

        for (int i = 0; i < header.reserved1.length; i = i + 1) {
            header.reserved1[i] = GDReader.getUInt(bytes, offset);
            offset = offset + 4;
        }

        // PixelFormat, offset 76
        header.pixelFormat.size = GDReader.getUInt(bytes, offset);
        offset = offset + 4;

        if ((header.pixelFormat.size != DDSPixelFormat.HEADER_SIZE)) {
            throw new Exception();
        }

        header.pixelFormat.flags = GDReader.getUInt(bytes, offset);
        offset = offset + 4;

        header.pixelFormat.fourCC = GDReader.getUInt(bytes, offset);
        offset = offset + 4;

        header.pixelFormat.rgbBitCount = GDReader.getUInt(bytes, offset);
        offset = offset + 4;

        header.pixelFormat.rBitMask = GDReader.getUInt(bytes, offset);
        offset = offset + 4;

        header.pixelFormat.gBitMask = GDReader.getUInt(bytes, offset);
        offset = offset + 4;

        header.pixelFormat.bBitMask = GDReader.getUInt(bytes, offset);
        offset = offset + 4;

        header.pixelFormat.aBitMask = GDReader.getUInt(bytes, offset);
        offset = offset + 4;

        // DDS Header, part 2, offset 108
        header.caps = GDReader.getUInt(bytes, offset);
        offset = offset + 4;

        header.caps2 = GDReader.getUInt(bytes, offset);
        offset = offset + 4;

        header.caps3 = GDReader.getUInt(bytes, offset);
        offset = offset + 4;

        header.caps4 = GDReader.getUInt(bytes, offset);
        offset = offset + 4;

        header.reserved2 = GDReader.getUInt(bytes, offset);
        //offset = offset + 4;

        return header;
    }

    private static void fixDDSHeader(byte[] bytes, DDSHeader header) {
        if (header.version[3] == DDSHeader.VERSION_REVERSED) {
            bytes[3] = DDSHeader.VERSION_DEFAULT;
        }

        int flags = header.flags
                | DDSHeader.HEADER_CAPS
                | DDSHeader.HEADER_HEIGHT
                | DDSHeader.HEADER_WIDTH
                | DDSHeader.HEADER_PIXELFORMAT;

        int caps = header.caps | DDSHeader.CAPS_TEXTURE;

        if (header.num_mipmap > 1) {
            header.flags = header.flags | DDSHeader.HEADER_MIPMAP_COUNT;
            header.caps = header.caps | DDSHeader.CAPS_COMPLEX | DDSHeader.CAPS_MIPMAP;
        }

        if ((header.caps2 & DDSHeader.CAPS2_CUBEMAP) != 0) {
            caps = caps | DDSHeader.CAPS_COMPLEX;
        }

        if (header.depth > 1) {
            flags = flags | DDSHeader.HEADER_DEPTH;
            caps = caps | DDSHeader.CAPS_COMPLEX;
        }

        if (((header.pixelFormat.flags & DDSPixelFormat.FOUR_CC) != 0)
                && (header.linearSize != 0)) {
            flags = flags | DDSHeader.HEADER_LINEAR_SIZE;
        }

        if ((((header.pixelFormat.flags & DDSPixelFormat.RGB) == DDSPixelFormat.RGB)
                || ((header.pixelFormat.flags & DDSPixelFormat.YUV) == DDSPixelFormat.YUV)
                || ((header.pixelFormat.flags & DDSPixelFormat.LUMINANCE) == DDSPixelFormat.LUMINANCE)
                || ((header.pixelFormat.flags & DDSPixelFormat.ALPHA) == DDSPixelFormat.ALPHA))
                && (header.linearSize != 0)) {
            flags = flags | DDSHeader.HEADER_PITCH;
        }

        /*
    if ((header.pixelFormat.flags & DDSPixelFormat.RGB) != 0) {
      if ((header.pixelFormat.rBitMask & header.pixelFormat.gBitMask & header.pixelFormat.bBitMask) == 0) {
        DDSLoader.writeBytes(bytes,  92, DDSPixelFormat.R_BITMASK);
        DDSLoader.writeBytes(bytes,  96, DDSPixelFormat.G_BITMASK);
        DDSLoader.writeBytes(bytes, 100, DDSPixelFormat.B_BITMASK);
        //header.pixelFormat.rBitMask = DDSPixelFormat.R_BITMASK;
        //header.pixelFormat.gBitMask = DDSPixelFormat.G_BITMASK;
        //header.pixelFormat.bBitMask = DDSPixelFormat.B_BITMASK;
      }
    }
         */
        DDSLoader.writeBytes(bytes, 8, flags);

        DDSLoader.writeBytes(bytes, 80, header.pixelFormat.flags);

        DDSLoader.writeBytes(bytes, 92, DDSPixelFormat.R_BITMASK);
        DDSLoader.writeBytes(bytes, 96, DDSPixelFormat.G_BITMASK);
        DDSLoader.writeBytes(bytes, 100, DDSPixelFormat.B_BITMASK);
        DDSLoader.writeBytes(bytes, 104, DDSPixelFormat.B_BITMASK);

        DDSLoader.writeBytes(bytes, 108, caps);
    }

    private static void writeBytes(byte[] bytes, int offset, byte[] value) {
        for (int i = 0; i < value.length; i = i + 1) {
            bytes[i + offset] = value[i];
        }
    }

    private static void writeBytes(byte[] bytes, int offset, int value) {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putInt(0, value);

        byte[] b = buffer.array();

        DDSLoader.writeBytes(bytes, offset, b);
    }
}
