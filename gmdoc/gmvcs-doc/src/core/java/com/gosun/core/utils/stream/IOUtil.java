package com.gosun.core.utils.stream;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

/**
 * IO工具类
 * @author CaiRQ
 *
 */
public final class IOUtil {
	private IOUtil(){
		
	}
	
	/**
	 * 关闭输入流
	 * @param is
	 */
	public static void close(InputStream is){
		try {
			if(is != null){
				is.close();
			}
		} catch (Exception e) {
			
		}
	}
	
	/**
	 * 关闭输出流
	 * @param os
	 */
	public static void close(OutputStream os){
		try {
			if(os != null){
				os.close();
			}
		} catch (Exception e) {
			
		}
	}
	
	/**
	 * 关闭字符输入流
	 * @param reader
	 */
	public static void close(Reader reader){
		try {
			if(reader != null){
				reader.close();
			}
		} catch (Exception e) {
			
		}
	}
	
	/**
	 * 关闭字符输出流
	 * @param writer
	 */
	public static void close(Writer writer){
		try {
			if(writer != null){
				writer.close();
			}
		} catch (Exception e) {
			
		}
	}
}
