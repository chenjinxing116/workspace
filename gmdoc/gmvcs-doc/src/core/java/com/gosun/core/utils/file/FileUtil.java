package com.gosun.core.utils.file;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * 文件操作工具类
 * 
 * @author Administrator
 * 
 */
public class FileUtil {

	/**
	 * 创建文件
	 * 
	 * @param fullFilePath
	 *            文件全路径
	 * @return 操作结果
	 * @throws IOException
	 */
	public static boolean createFile(String fullFilePath) throws IOException {
		File newFile = new File(fullFilePath);
		return newFile.createNewFile();
	}

	/**
	 * 删除文件
	 * 
	 * @param fullpathandname
	 *            带文件名的全路径
	 * @return
	 */
	public static boolean delFile(String fullpathandname) {
		File delfile = new File(fullpathandname);
		return delfile.delete();
	}

	/**
	 * 创建指定文件目录
	 * 
	 * @param folderPath
	 *            目录路径
	 * @return 操作结果
	 */
	public boolean mkdir(String folderPath) {
		File newpath = new File(folderPath);
		return newpath.mkdir();
	}

	/**
	 * 创建多级文件目录
	 * 
	 * @param folderPath
	 *            目录路径
	 * @return 操作结果
	 */
	public boolean mkdirs(String folderPath) {
		File newpath = new File(folderPath);
		return newpath.mkdirs();
	}

	/**
	 * 获取目录下的文件
	 * 
	 * @param fullfolderpath
	 *            目录全路径
	 * @return 文件数组
	 */
	public static String[] getFolderContent(String fullfolderpath) {
		String[] tempList = (String[]) null;
		File file = new File(fullfolderpath);
		try {
			if (!file.exists()) {
				System.out.println("[FileOperate.getFolderContent]" + fullfolderpath + "路径不存在");
				return null;
			}
			if (!file.isDirectory()) {
				System.out.println("[FileOperate.getFolderContent]" + fullfolderpath + "不是文件夹");
				return null;
			}
			tempList = file.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tempList;
	}

	/**
	 * 删除目录下的所有文件
	 * 
	 * @param folderPath
	 *            目录路径
	 * @return 操作结果
	 */
	public static boolean deleteFolderFiles(String folderPath) {
		boolean flag = false;
		String[] tempList = getFolderContent(folderPath);
		if (tempList == null) {
			flag = false;
			System.out.println("error!the path not exist or is not a dir!");
			return flag;
		}
		File tempfile = null;
		for (int i = 0; i < tempList.length; i++) {
			if (folderPath.endsWith(File.separator)) {
				tempfile = new File(folderPath + tempList[i]);
			} else {
				tempfile = new File(folderPath + File.separator + tempList[i]);
			}
			if (tempfile.isFile()) {
				tempfile.delete();
			} else if (tempfile.isDirectory()) {
				deleteFolderFiles(folderPath + File.separator + tempList[i]);
				delFolder(folderPath + File.separator + tempList[i]);
				flag = true;
			}
		}
		return flag;
	}

	/**
	 * 删除目录
	 * 
	 * @param folderPath
	 *            目录路径
	 */
	public static void delFolder(String folderPath) {
		try {
			deleteFolderFiles(folderPath);
			String filePath = folderPath;
			filePath = filePath.toString();
			File myFilePath = new File(filePath);
			myFilePath.delete();
		} catch (Exception e) {
			System.out.println("failed in deleting the folder!");
		}
	}

	/**
	 * 复制文件
	 * 
	 * @param oldPathFile
	 *            原文件路径
	 * @param newPathFile
	 *            新文件路径
	 * @throws Exception
	 */
	public static void copyFile(String oldPathFile, String newPathFile) throws Exception {
		try {
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPathFile);
			if (oldfile.exists()) {
				InputStream inStream = new FileInputStream(oldPathFile);
				FileOutputStream fs = new FileOutputStream(newPathFile);
				byte[] buffer = new byte[1444];
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread;

					fs.write(buffer, 0, byteread);
				}

				fs.flush();
				fs.close();
				inStream.close();
			}
		} catch (Exception e) {
			System.out.println("复制单个文件操作出错");
			e.printStackTrace();
		}
	}

	/**
	 * 拷贝文件夹
	 * 
	 * @param oldPath
	 *            原文件夹路径
	 * @param newPath
	 *            新文件夹路径
	 */
	public static void copyFolder(String oldPath, String newPath) {
		try {
			new File(newPath).mkdirs();
			File a = new File(oldPath);
			String[] file = a.list();
			File temp = null;
			if (file == null) {
				return;
			}
			for (int i = 0; i < file.length; i++) {
				if (oldPath.endsWith(File.separator)) {
					temp = new File(oldPath + file[i]);
				} else {
					temp = new File(oldPath + File.separator + file[i]);
				}
				if (temp.isFile()) {
					FileInputStream input = new FileInputStream(temp);
					FileOutputStream output = new FileOutputStream(
							newPath + File.separator + temp.getName().toString());
					byte[] b = new byte[5120];
					int len;
					while ((len = input.read(b)) != -1) {
						output.write(b, 0, len);
					}
					output.flush();
					output.close();
					input.close();
				}
				if (temp.isDirectory())
					copyFolder(oldPath + File.separator + file[i], newPath + File.separator + file[i]);
			}
		} catch (Exception e) {
			System.out.println("复制整个文件夹内容操作出错");
		}
	}

	/**
	 * 剪切文件
	 * 
	 * @param oldPath
	 *            原文件路径
	 * @param newPath
	 *            新文件路径
	 * @throws Exception
	 */
	public static void moveFile(String oldPath, String newPath) throws Exception {
		copyFile(oldPath, newPath);
		delFile(oldPath);
	}

	/**
	 * 剪切目录
	 * 
	 * @param oldPath
	 *            原目录路径
	 * @param newPath
	 *            新目录路径
	 */
	public static void moveFolder(String oldPath, String newPath) {
		copyFolder(oldPath, newPath);
		delFolder(oldPath);
	}

	/**
	 * 相同文件检测
	 * 
	 * @param soursepath
	 *            源文件路径
	 * @param targetpath
	 *            目标文件路径
	 * @return
	 */
	public static boolean hasSameFile(String soursepath, String targetpath) {
		String[] sourse = getFolderContent(soursepath);
		String[] target = getFolderContent(targetpath);
		if (target != null) {
			for (int i = 0; i < sourse.length; i++) {
				for (int j = 0; j < target.length; j++) {
					if (sourse[i].equals(target[j])) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 读取文件内容
	 * 
	 * @param filePath
	 *            文件路径
	 * @return 返回文件字节数组
	 * @throws Exception
	 */
	public static byte[] readFileContent(String filePath) throws Exception {
		InputStream fin = null;
		ByteArrayOutputStream bos = null;
		try {
			fin = new FileInputStream(new File(filePath));
			bos = new ByteArrayOutputStream();
			byte[] content = new byte[10240];
			while (true) {
				int size = fin.read(content);
				if (size == -1)
					break;
				bos.write(content, 0, size);
			}
			int size;
			byte[] retValue = bos.toByteArray();
			return retValue;
		} catch (Exception ex) {
			throw ex;
		} finally {
			try {
				if (fin != null)
					fin.close();
			} catch (Exception localException3) {
			}
			try {
				if (bos != null)
					bos.close();
			} catch (Exception localException4) {
			}
		}
	}

	/**
	 * 获取压缩文件内容
	 * 
	 * @param zipfilepath
	 *            压缩文件路径
	 * @return 返回文件内容
	 */
	public static ArrayList getZipfileContent(String zipfilepath) {
		ArrayList al = new ArrayList();
		try {
			ZipFile zf = new ZipFile(zipfilepath);
			for (Enumeration entries = zf.entries(); entries.hasMoreElements();) {
				String zipentryname = ((ZipEntry) entries.nextElement()).getName();
				al.add(zipentryname);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return al;
	}

	/**
	 * 解压缩
	 * 
	 * @param zipfilepath
	 *            压缩文件所在路径
	 * @param targetpath
	 *            解压目标路径
	 */
	public static void unZip(String zipfilepath, String targetpath) {
		try {
			ZipFile zf = new ZipFile(zipfilepath);
			Enumeration all = zf.entries();
			while (all.hasMoreElements()) {
				ZipEntry ze = (ZipEntry) all.nextElement();
				String entryname = ze.getName();
				entryname = new String(entryname.getBytes("ISO8859-1"), "gb2312");
				if (entryname.endsWith("/")) {
					new File(targetpath + File.separator + entryname).mkdirs();
				} else {
					FileOutputStream os = new FileOutputStream(targetpath + File.separator + entryname);
					InputStream is = zf.getInputStream(ze);
					byte[] b = new byte[2048000];
					int len = 0;
					while ((len = is.read(b)) != -1) {
						os.write(b, 0, len);
					}
					is.close();
					os.close();
				}
			}
			zf.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 压缩文件
	 * 
	 * @param filenames
	 *            文件名称集合
	 * @param rootPath
	 *            文件路径
	 * @param zipPath
	 *            zip路径
	 * @param zipFileName
	 *            zip名称
	 */
	public static void zipFiles(String[] filenames, String rootPath, String zipPath, String zipFileName) {
		byte[] buf = new byte[1024];
		try {
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName));
			out.setMethod(8);

			for (int i = 0; i < filenames.length; i++) {
				FileInputStream in = new FileInputStream(rootPath + filenames[i]);

				out.putNextEntry(new ZipEntry(zipPath + filenames[i]));
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}

				out.closeEntry();
				in.close();
			}

			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取jar文件内容
	 * 
	 * @param jarFile
	 *            jar文件
	 * @param strFile
	 *            需要读取的文件
	 * @return 文件字节流
	 * @throws Exception
	 */
	public static byte[] getJarFileContent(File jarFile, String strFile) throws Exception {
		JarFile jar = null;
		try {
			if (!jarFile.isFile()) {
				return null;
			}
			jar = new JarFile(jarFile);
			JarEntry entry = null;
			entry = jar.getJarEntry(strFile);

			ByteArrayOutputStream out = new ByteArrayOutputStream();
			InputStream in = jar.getInputStream(entry);
			byte[] b = new byte[1024];
			int size = -1;
			while (true) {
				size = in.read(b);
				if (size == -1)
					break;
				out.write(b, 0, size);
			}
			in.close();
			byte[] returnBytes = out.toByteArray();
			out.close();
			return returnBytes;
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (jar != null)
				jar.close();
		}
	}

	/**
	 * 读取文件内容
	 * 
	 * @param filePath
	 *            文件路径
	 * @return 返回文件的大小字符串
	 * @throws Exception
	 */
	public static String getFileSize(File file) {
		long length = file.length();
		String fileSize = "0B";
		DecimalFormat df = new DecimalFormat("#.00");
		if (length < 1024) {
			fileSize = df.format((double) length) + "B";
		} else if (length < 1024 * 1024) {
			fileSize = df.format((double) length / 1024) + "KB";
		} else if (length < 1024 * 1024 * 1024) {
			fileSize = df.format((double) length / (1024 * 1024)) + "MB";
		} else {
			fileSize = df.format((double) length / (1024 * 1024 * 1024)) + "GB";
		}
		return fileSize;
	}
}