package com.gosun.core.utils.stream;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class VoxToWav
{
  public static final int V2W_OK = 0;
  public static final int V2W_FAIL_CANNOT_OPEN_SOURCE = 1;
  public static final int V2W_FAIL_SOURCEEMPTY = 2;
  public static final int V2W_FAIL_CANNOT_OPEN_DEST = 3;
  public static final int V2W_FAIL_CANNOT_READ_SOURCE = 4;
  public static final int V2W_FAIL_CANNOT_WRITE_DEST = 5;
  public static final int V2W_NOT_EXISTS_SOURCE = 6;
  public static final int V2W_NOT_EXISTS_DEST = 7;
  public static final int INBUFF_SIZE = 131072;
  int[] indsft = { 
    -1, -1, -1, -1, 2, 4, 6, 8 };

  int[] stpsz = { 
    16, 17, 19, 21, 23, 25, 28, 31, 34, 37, 41, 45, 50, 55, 60, 66, 73, 80, 
    88, 97, 107, 118, 130, 143, 
    157, 173, 190, 209, 230, 253, 279, 307, 337, 371, 408, 449, 494, 544, 598, 
    658, 724, 796, 876, 963, 1060, 1166, 1282, 1411, 1552, 1707, 1878, 2066, 
    2272, 2499, 2749, 3024, 3327, 3660, 4026, 4428, 4871, 5358, 5894, 6484, 
    7132, 7845, 8630, 9493, 10442, 11487, 12635, 13899, 15289, 16818, 18500, 
    20350, 22385, 24623, 27086, 29794 };

  int[][] nbl2bit = { 
    new int[4], 
    { 
    0, 0, 0, 1 }, { 
    0, 0, 1 }, { 
    0, 0, 1, 1 }, { 
    0, 1 }, { 
    0, 1, 0, 1 }, { 
    0, 1, 1 }, { 
    0, 1, 1, 1 }, { 
    1 }, { 
    1, 0, 0, 1 }, { 
    1, 0, 1 }, { 
    1, 0, 1, 1 }, { 
    1, 1 }, { 
    1, 1, 0, 1 }, { 
    1, 1, 1 }, { 
    1, 1, 1, 1 } };

  int[] sgns = { 
    1, -1 };

  int ssindex = 0;

  byte[] WavHead = { 
    82, 73, 70, 70, 0, 0, 0, 0, 87, 65, 86, 69, 102, 109, 
    116, 32, 16, 
    0, 0, 0, 1, 0, 1 };
  int diff;
  short incoded;

  public int voxToWav(String voxFile, String wavFile, int nRate)
    throws Exception
  {
    FileInputStream fin = null;
    FileOutputStream fout = null;
    try {
      fin = new FileInputStream(new File(voxFile));
      fout = new FileOutputStream(new File(wavFile));
      return voxToWav(fin, fout, nRate);
    }
    catch (Exception ex) {
      throw ex;
    }
    finally {
      try {
        if (fin != null)
          fin.close();
      }
      catch (Exception localException3)
      {
      }
      try {
        if (fout != null)
          fout.close();
      }
      catch (Exception localException4)
      {
      }
    }
  }

  public int voxToWav(String srcFile, OutputStream fout, int nRate)
    throws Exception
  {
    FileInputStream fin = null;
    try
    {
      fin = new FileInputStream(new File(srcFile));

      return voxToWav(fin, fout, nRate);
    }
    catch (Exception ex) {
      throw ex;
    }
    finally {
      try {
        if (fin != null)
          fin.close();
      }
      catch (Exception localException2)
      {
      }
    }
  }

  public int voxToWav(InputStream voxIn, OutputStream wavOut, int nRate)
    throws Exception
  {
	int VoxLength;
	DataInputStream din = new DataInputStream(voxIn);
    DataOutputStream dout = new DataOutputStream(wavOut);
    try
    {
      VoxLength = din.available();
    }
    catch (Exception e)
    {
      throw e;
    }
    
    WAVFileHead head = new WAVFileHead();
    head.setupData(nRate, VoxLength);
    try {
      dout.write(head.getHeadData());
    }
    catch (Exception ex) {
      throw ex;
    }

    short iVal = 0;
    byte[] code = new byte[1];

    byte[] inBuff = new byte[131072];
    byte[] outBuff = new byte[524288];
    do {
      try {
        VoxLength = din.read(inBuff);
      }
      catch (Exception ex) {
        throw ex;
      }

      if (VoxLength <= 0) {
        break;
      }
      int outPos = 0;
      for (int i = 0; i < VoxLength; i++) {
        code[0] = inBuff[i];

        short tCode = code[0];
        if (tCode < 0) {
          tCode = (short)(tCode + 256);
        }
        short incoded = (short)(tCode / 16);
        int diff = this.sgns[this.nbl2bit[incoded][0]] * (
          this.stpsz[this.ssindex] * this.nbl2bit[incoded][1] + 
          this.stpsz[this.ssindex] / 2 * this.nbl2bit[incoded][2] + this.stpsz[this.ssindex] / 4 * 
          this.nbl2bit[incoded][3] + this.stpsz[this.ssindex] / 8);
        this.ssindex += this.indsft[(incoded % 8)];
        if (this.ssindex < 0) {
          this.ssindex = 0;
        }
        if (this.ssindex > 48) {
          this.ssindex = 48;
        }
        iVal = (short)(iVal + diff);

        if (iVal > 2047) {
          iVal = 2047;
        }
        else if (iVal < -2047) {
          iVal = -2047;
        }

        byte[] b = new byte[2];
        b[0] = (byte)(iVal * 16 & 0xFF);
        b[1] = (byte)(iVal * 16 >> 8 & 0xFF);

        outBuff[(outPos++)] = b[0];
        outBuff[(outPos++)] = b[1];

        incoded = (short)(tCode % 16);

        diff = this.sgns[this.nbl2bit[incoded][0]] * (
          this.stpsz[this.ssindex] * this.nbl2bit[incoded][1] + 
          this.stpsz[this.ssindex] / 2 * this.nbl2bit[incoded][2] + this.stpsz[this.ssindex] / 4 * 
          this.nbl2bit[incoded][3] + this.stpsz[this.ssindex] / 8);

        this.ssindex += this.indsft[(incoded % 8)];

        if (this.ssindex < 0) {
          this.ssindex = 0;
        }
        if (this.ssindex > 48) {
          this.ssindex = 48;
        }
        iVal = (short)(iVal + diff);

        if (iVal > 2047) {
          iVal = 2047;
        }
        else if (iVal < -2047) {
          iVal = -2047;
        }

        b[0] = (byte)(iVal * 16 & 0xFF);
        b[1] = (byte)(iVal * 16 >> 8 & 0xFF);

        outBuff[(outPos++)] = b[0];
        outBuff[(outPos++)] = b[1];
      }
      try
      {
        if (outPos > 0)
          dout.write(outBuff, 0, outPos);
      }
      catch (Exception ex) {
        throw ex;
      }
    }

    while (VoxLength >= 131072);

    return 0;
  }
}