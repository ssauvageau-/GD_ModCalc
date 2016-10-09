package modcalc;

public class DDSPixelFormat {
    
  public static final int HEADER_SIZE   = 32; // 8 * int

  public static final int ALPHA_PIXELS  = 0x01;
  public static final int ALPHA         = 0x02;
  public static final int FOUR_CC       = 0x04;
  public static final int PALETTE_IDX   = 0x0020;
  public static final int RGB           = 0x0040;
  public static final int YUV           = 0x0200;
  public static final int LUMINANCE     = 0x020000;
  
  public static final int R_BITMASK     = 0xFF0000;
  public static final int G_BITMASK     = 0x00FF00;
  public static final int B_BITMASK     = 0x0000FF;
  
  public int size;
  public int flags;
  public int fourCC;
  public int rgbBitCount;
  public int rBitMask;
  public int gBitMask;
  public int bBitMask;
  public int aBitMask;

}
