package com.gosun.core.utils.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;

/**
 * 文件流工具类
 * 
 * @author Administrator
 * 
 */
public class FileStream {
	/**
	 * zip压缩
	 * 
	 * @param filePaths 文件路径集合
	 * @param out 输出流
	 */
	public static void zipTransfer(String[] filePaths, OutputStream out) {
		JarOutputStream jarOut = null;
		FileInputStream fileIn = null;
		try {
			jarOut = new JarOutputStream(out);

			for (int i = 0; i < filePaths.length; i++) {
				File file = new File(filePaths[i]);
				ZipEntry entry = new JarEntry(file.getName());
				jarOut.putNextEntry(entry);
				fileIn = new FileInputStream(file);
				transfer(fileIn, jarOut);
				try {
					fileIn.close();
					fileIn = null;
				} catch (Exception localException1) {
				}
			}
		} catch (Exception ex) {
			throw new RuntimeException(ex.getMessage(), ex);
		} finally {
			try {
				if (fileIn != null)
					fileIn.close();
			} catch (Exception localException2) {
			}
		}
	}

	/**
	 * 转换文件为流
	 * 
	 * @param filePath 文件路径
	 * @param out 输出流
	 */
	public static void transfer(String filePath, OutputStream out) {
		FileInputStream fin = null;
		try {
			fin = new FileInputStream(filePath);
			transfer(fin, out);
		} catch (Exception ex) {
			throw new RuntimeException(ex.getMessage(), ex);
		} finally {
			try {
				if (fin != null)
					fin.close();
			} catch (Exception localException1) {
			}
		}
	}

	public static void transfer(InputStream in, OutputStream out) {
		try {
			int idx = -1;
			byte[] content = new byte[10240];
			while (true) {
				idx = in.read(content);
				if (idx == -1)
					break;
				out.write(content, 0, idx);
			}
		} catch (Exception ex) {
			throw new RuntimeException(ex.getMessage(), ex);
		}
	}
}