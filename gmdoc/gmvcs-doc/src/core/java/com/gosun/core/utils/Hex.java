package com.gosun.core.utils;

public class Hex
{
  public static String encode(byte[] buffer)
  {
    String dump = "";
    try {
      int dataLen = buffer.length;
      for (int i = 0; i < dataLen; i++) {
        dump = dump + Character.forDigit(buffer[i] >> 4 & 0xF, 16);
        dump = dump + Character.forDigit(buffer[i] & 0xF, 16);
      }
    }
    catch (Throwable localThrowable) {
    }
    return dump.toUpperCase();
  }

  public static byte[] decode(String hexStr)
  {
    byte[] bytes = new byte[hexStr.length() / 2];
    for (int i = 0; i < bytes.length; i++) {
      bytes[i] = 
        (byte)(0xFF & 
        Integer.parseInt(hexStr
        .substring(i * 2, i * 2 + 2), 16));
    }
    return bytes;
  }
}