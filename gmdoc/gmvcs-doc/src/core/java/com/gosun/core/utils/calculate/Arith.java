package com.gosun.core.utils.calculate;

import java.io.PrintStream;
import java.math.BigDecimal;

/**
 * 计算工具类
 * @author Administrator
 *
 */
public class Arith
{
  private static final int DEF_DIV_SCALE = 10;

  public static double add(double v1, double v2)
  {
    BigDecimal b1 = new BigDecimal(Double.toString(v1));

    BigDecimal b2 = new BigDecimal(Double.toString(v2));

    return b1.add(b2).doubleValue();
  }

  public static double sub(double v1, double v2)
  {
    BigDecimal b1 = new BigDecimal(Double.toString(v1));

    BigDecimal b2 = new BigDecimal(Double.toString(v2));

    return b1.subtract(b2).doubleValue();
  }

  public static double mul(double v1, double v2)
  {
    BigDecimal b1 = new BigDecimal(Double.toString(v1));

    BigDecimal b2 = new BigDecimal(Double.toString(v2));

    return b1.multiply(b2).doubleValue();
  }

  public static double div(double v1, double v2)
  {
    return div(v1, v2, 10);
  }

  public static double div(double v1, double v2, int scale)
  {
    if (scale < 0)
    {
      throw new IllegalArgumentException(
        "The scale must be a positive integer or zero");
    }

    BigDecimal b1 = new BigDecimal(Double.toString(v1));

    BigDecimal b2 = new BigDecimal(Double.toString(v2));

    return b1.divide(b2, scale, 4).doubleValue();
  }

  public static double round(double v, int scale)
  {
    if (scale < 0)
    {
      throw new IllegalArgumentException(
        "The scale must be a positive integer or zero");
    }

    BigDecimal b = new BigDecimal(Double.toString(v));

    BigDecimal one = new BigDecimal("1");

    return b.divide(one, scale, 4).doubleValue();
  }

  public static String sround(double v, int scale)
  {
    String ret = null;

    if (scale < 0)
    {
      throw new IllegalArgumentException(
        "The scale must be a positive integer or zero");
    }

    BigDecimal b = new BigDecimal(Double.toString(v));

    BigDecimal one = new BigDecimal("1");

    v = b.divide(one, scale, 4).doubleValue();
    ret = Double.toString(v);
    int p = ret.indexOf(".");
    if (p >= 0)
    {
      int k = ret.substring(p + 1, ret.length()).length();
      if (k < scale)
      {
        for (int i = 0; i < scale - k; i++)
          ret = ret + "0";
      }
    }
    else
    {
      ret = ret + ".";
      for (int i = 0; i < scale; i++)
        ret = ret + "0";
    }
    return ret;
  }

  public static void main(String[] args)
    throws Exception
  {
    double test = 5.0D;
    System.out.print(sround(test, 9));
  }
}