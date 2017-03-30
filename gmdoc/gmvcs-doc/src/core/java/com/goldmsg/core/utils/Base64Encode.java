package com.goldmsg.core.utils;

/**
 * Created with IntelliJ IDEA.
 * User: Marshall
 * Date: 14-6-3
 * Time: 下午1:45
 * To change this template use File | Settings | File Templates.
 */
public class Base64Encode {

    private static char[] codec_table = { 'A', 'B', 'C', 'D', 'E', 'F', 'G',
            'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
            'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g',
            'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
            'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6',
            '7', '8', '9', '+', '/' };

    public static String encode(byte[] a) {
        int totalBits = a.length * 8;
        int nn = totalBits % 6;
        int curPos = 0;// process bits
        StringBuffer toReturn = new StringBuffer();
        while (curPos < totalBits) {
            int bytePos = curPos / 8;
            switch (curPos % 8) {
                case 0:
                    toReturn.append(codec_table[(a[bytePos] & 0xfc) >> 2]);
                    break;
                case 2:
                    toReturn.append(codec_table[(a[bytePos] & 0x3f)]);
                    break;
                case 4:
                    if (bytePos == a.length - 1) {
                        toReturn.append(codec_table[((a[bytePos] & 0x0f) << 2) & 0x3f]);
                    } else {
                        int pos = (((a[bytePos] & 0x0f) << 2) | ((a[bytePos + 1] & 0xc0) >> 6)) & 0x3f;
                        toReturn.append(codec_table[pos]);
                    }
                    break;
                case 6:
                    if (bytePos == a.length - 1) {
                        toReturn.append(codec_table[((a[bytePos] & 0x03) << 4) & 0x3f]);
                    } else {
                        int pos = (((a[bytePos] & 0x03) << 4) | ((a[bytePos + 1] & 0xf0) >> 4)) & 0x3f;
                        toReturn.append(codec_table[pos]);
                    }
                    break;
                default:
                    break;
            }
            curPos+=6;
        }
        if(nn==2)
        {
            toReturn.append("==");
        }
        else if(nn==4)
        {
            toReturn.append("=");
        }
        return toReturn.toString();

    }

    public static void main(String[] args) {
        System.out.println(Base64Encode.encode("aaaa".getBytes()));

        String host = Base64Encode.encode("192.168.21.85".getBytes());
        String sid = Base64Encode.encode("orcl".getBytes());
        String user = Base64Encode.encode("scott".getBytes());
        String pass = Base64Encode.encode("tiger".getBytes());
        String usertable = Base64Encode.encode("other".getBytes());
        String maxdep = Base64Encode.encode("999999".getBytes());

        String sqlString = String.format("REPLACE INTO `gmvcswxs`.`tmp_infos` (Id,DTDZ,DTID,DTZH,DTMM,DTMODE,DTJG) "
                + "VALUES ('10000','%s','%s','%s','%s','%s','%s')",
                host, sid, user, pass, usertable, maxdep);

        System.out.println(sqlString);
    }

}
