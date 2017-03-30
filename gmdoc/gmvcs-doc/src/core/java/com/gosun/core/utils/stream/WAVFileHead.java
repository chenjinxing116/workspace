package com.gosun.core.utils.stream;

public class WAVFileHead
{
  byte[] RIFFFlag = { 82, 73, 70, 70 };
  int ChunkSize;
  byte[] WAVEFlag = { 87, 65, 86, 69 };
  byte[] fmtFlag = { 102, 109, 116, 32 };
  int SubchunkSize = 16;
  short AudioFormat = 1;
  short NumChannels = 1;
  int SampleRate;
  int ByteRate;
  short BlockAlign = 2;
  short BitPerSample = 16;
  byte[] dataFlag = { 100, 97, 116, 97 };
  int DataLength;

  public void setupData(int Sample, int Length)
  {
    this.SampleRate = (short)Sample;
    this.ByteRate = (Sample * 2);
    this.ChunkSize = (Length * 4 + 44);
    this.DataLength = (Length * 4);
  }

  public byte[] getHeadData()
  {
    byte[] Head = new byte[44];
    Head[0] = this.RIFFFlag[0];
    Head[1] = this.RIFFFlag[1];
    Head[2] = this.RIFFFlag[2];
    Head[3] = this.RIFFFlag[3];
    Head[4] = (byte)(this.ChunkSize & 0xFF);
    Head[5] = (byte)(this.ChunkSize >> 8 & 0xFF);
    Head[6] = (byte)(this.ChunkSize >> 16 & 0xFF);
    Head[7] = (byte)(this.ChunkSize >> 24 & 0xFF);
    Head[8] = this.WAVEFlag[0];
    Head[9] = this.WAVEFlag[1];
    Head[10] = this.WAVEFlag[2];
    Head[11] = this.WAVEFlag[3];
    Head[12] = this.fmtFlag[0];
    Head[13] = this.fmtFlag[1];
    Head[14] = this.fmtFlag[2];
    Head[15] = this.fmtFlag[3];
    Head[16] = (byte)(this.SubchunkSize & 0xFF);
    Head[17] = (byte)(this.SubchunkSize >> 8 & 0xFF);
    Head[18] = (byte)(this.SubchunkSize >> 16 & 0xFF);
    Head[19] = (byte)(this.SubchunkSize >> 24 & 0xFF);
    Head[20] = (byte)(this.AudioFormat & 0xFF);
    Head[21] = (byte)(this.AudioFormat >> 8 & 0xFF);
    Head[22] = (byte)(this.NumChannels & 0xFF);
    Head[23] = (byte)(this.NumChannels >> 8 & 0xFF);
    Head[24] = (byte)(this.SampleRate & 0xFF);
    Head[25] = (byte)(this.SampleRate >> 8 & 0xFF);
    Head[26] = (byte)(this.SampleRate >> 16 & 0xFF);
    Head[27] = (byte)(this.SampleRate >> 24 & 0xFF);
    Head[28] = (byte)(this.ByteRate & 0xFF);
    Head[29] = (byte)(this.ByteRate >> 8 & 0xFF);
    Head[30] = (byte)(this.ByteRate >> 16 & 0xFF);
    Head[31] = (byte)(this.ByteRate >> 24 & 0xFF);
    Head[32] = (byte)(this.BlockAlign & 0xFF);
    Head[33] = (byte)(this.BlockAlign >> 8 & 0xFF);
    Head[34] = (byte)(this.BitPerSample & 0xFF);
    Head[35] = (byte)(this.BitPerSample >> 8 & 0xFF);
    Head[36] = this.dataFlag[0];
    Head[37] = this.dataFlag[1];
    Head[38] = this.dataFlag[2];
    Head[39] = this.dataFlag[3];
    Head[40] = (byte)(this.DataLength & 0xFF);
    Head[41] = (byte)(this.DataLength >> 8 & 0xFF);
    Head[42] = (byte)(this.DataLength >> 16 & 0xFF);
    Head[43] = (byte)(this.DataLength >> 24 & 0xFF);
    return Head;
  }
}