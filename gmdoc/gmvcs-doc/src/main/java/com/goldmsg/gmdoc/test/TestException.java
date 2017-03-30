package com.goldmsg.gmdoc.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestException {
	public static void main(String []args){
		Exception e = new Exception();
		String str = new String("chenjinxing123213sdf111");
		Pattern   p   =   Pattern.compile("\\d[2,4]");     
        Matcher   m   =   p.matcher(str);
        String s="";
		while(m.find()){
			s+=m.group();
		}
		System.out.println(s);
	}

}
