package com.gosun.core.utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Base64
{
	public static String encode(byte[] content)
	  {
	    BASE64Encoder encode = new BASE64Encoder();
	    return encode.encode(content);
	  }

	  public static byte[] decode(String content)
	  {
	    try
	    {
	      BASE64Decoder decoder = new BASE64Decoder();
	      return decoder.decodeBuffer(content);
	    }
	    catch (Exception ex) {
	      throw new RuntimeException(ex.getMessage(), ex);
	    }
	  }
}
