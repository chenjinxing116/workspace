package com.gosun.core.utils.net;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;

import com.gosun.core.utils.StringUtils;

/**
 * FTP工具类，使用Apache的FTP组件实现
 * 
 */
public class FtpUtil {

	private static final Logger log = Logger.getLogger(FtpUtil.class);
	public static final int BINARY_FILE_TYPE = FTP.BINARY_FILE_TYPE;
	public static final int ASCII_FILE_TYPE = FTP.ASCII_FILE_TYPE;
	public static final String ISO_8859_1 = "ISO-8859-1";

	public static final char FTP_PATH_CHAR = '/';

	/**
	 * 
	 * 下载文件
	 * 
	 * @param ftpUrl
	 *            请求信息 格式：ftp://user:password@ip:port/path/fileName
	 * @param storePath
	 *            文件存储路径
	 * @param maxSize
	 *            文件大小，允许ftp下载远程文件的最大值，单位为byte,超过此大小将不允许下载
	 * @return
	 * @throws Exception
	 * @Exception 异常对象
	 */
	public static File download(String ftpUrl, String storePath, long maxSize) throws Exception {

		FtpConfInfo infoConf = getInfo(ftpUrl);

		if (infoConf == null) {
			log.error("构建FTP配置信息失败，请检查:" + ftpUrl);
			return null;
		}
		FTPClient client = null;
		File file = null;
		try {
			client = connectServer(infoConf);
			if (client == null) {
				log.error("构建FTP客户端失败");
				return null;
			}

			log.debug("FTP服务器连接成功");

			if (infoConf.getLocation() != null) {
				String[] ss = infoConf.getLocation().split("/");
				for (String s : ss) {
					client.changeWorkingDirectory(s);
				}
			}
			log.debug("FTP切换目录成功download()");

			String fileName = downFile(infoConf, storePath == null ? "" : storePath, client, infoConf.getFileName(),
					maxSize);
			if (fileName == null) {
				return null;
			}
			file = new File(fileName);
			if (!file.isFile()) {
				log.error("下载的文件失败");
				return null;
			}
			System.out.println("文件下载成功准备重命名,file = " + fileName);
			String suffix = infoConf.getFileName().substring(infoConf.getFileName().lastIndexOf('.'));
			File tempFile = new File(storePath + File.separator + infoConf.getFileName());
			file.renameTo(tempFile);// 重命名保持唯一性
			file = tempFile;
		} catch (Exception e) {
			log.error("", e);
			throw e;
		} finally {
			// 关闭ftp
			closeServer(client);
		}
		return file;
	}

	/**
	 * FTP批量下载文件到本地目录
	 * 
	 * @param localPath
	 *            本地目录
	 * @param ftpUrlList
	 *            FTP文件列表
	 * @return localFilePathList 本地文件路径列表
	 */
	public static List<String> downloadFiles(String localPath, List<String> ftpUrlList, long maxSize) throws Exception {
		List<String> localFilePathList = null;
		if (ftpUrlList != null && ftpUrlList.size() > 0) {
			localFilePathList = new ArrayList<String>();
			FtpConfInfo infoConf = getInfo(ftpUrlList.get(0));
			if (infoConf == null) {
				log.error("构建FTP配置信息失败，请检查:" + ftpUrlList.get(0));
				return null;
			}
			FTPClient client = null;
			try {
				client = connectServer(infoConf);
				if (client == null) {
					log.error("构建FTP客户端失败");
					return null;
				}
				for (String ftpUrl : ftpUrlList) {
					infoConf = getInfo(ftpUrl);
					if (infoConf.getLocation() != null) {
						String[] ss = infoConf.getLocation().split("/");
						for (String s : ss) {
							client.changeWorkingDirectory(s);
						}
					}
					String fileName = downFile(infoConf, localPath + File.separator + infoConf.getLocation(), client,
							infoConf.getFileName(), maxSize);
					if (fileName != null) {
						localFilePathList.add(fileName);
					}
				}
			} catch (Exception e) {
				log.error("", e);
				throw e;
			} finally {
				// 关闭ftp
				closeServer(client);
			}
		}
		return localFilePathList;
	}

	/**
	 * 判断是否有重名文件
	 * 
	 * @param ftpUrl
	 *            请求信息 格式：ftp://user:password@ip:port/path/fileName
	 * @param newName
	 * @return
	 * @throws Exception
	 */
	public static boolean checkName(String ftpUrl, String newName) throws Exception {
		FtpConfInfo infoConf = getInfo(ftpUrl);
		FTPClient ftpclient = null;
		if (infoConf.getLocation() != null) {
			ftpclient = connectServer(infoConf);
			String[] ss = infoConf.getLocation().split("/");
			for (String s : ss) {
				if (!existDirectory(ftpclient, s)) {
					closeServer(ftpclient);
					return true;
				}
				ftpclient.changeWorkingDirectory(s);
			}
		}
		if (ftpclient == null)
			throw new Exception("FTP链接不成功，请检查FTP链接参数！");
		if (newName == null)
			throw new Exception("检查重名方法的参数newName不允许为空！");
		else
			newName = new String(newName.getBytes(infoConf.getEncoding()), FtpUtil.ISO_8859_1);

		FTPFile[] files = listFiles(ftpclient, newName);
		closeServer(ftpclient);

		if (files.length > 0) {
			return false;
		} else {
			return true;
		}
	}

	public static void clearPath(String ftpUrl, String newName) throws Exception {
		FtpConfInfo infoConf = getInfo(ftpUrl);
		FTPClient ftpclient = null;
		if (infoConf.getLocation() != null) {
			ftpclient = connectServer(infoConf);
			String[] ss = infoConf.getLocation().split("/");
			for (String s : ss) {
				if (!existDirectory(ftpclient, s)) {
					closeServer(ftpclient);
					return;
				}
				ftpclient.changeWorkingDirectory(s);
			}
		}
		if (ftpclient == null)
			throw new Exception("FTP链接不成功，请检查FTP链接参数！");
		if (newName == null)
			throw new Exception("删除文件方法的参数newName不允许为空！");
		else
			newName = new String(newName.getBytes(infoConf.getEncoding()), FtpUtil.ISO_8859_1);

		FTPFile[] files = listFiles(ftpclient, null);

		for (FTPFile file : files) {
			if (file.isFile()) {
				if (!file.getName().equals(newName))
					ftpclient.deleteFile(file.getName());
			}
		}
		closeServer(ftpclient);
	}

	/**
	 * 上载文件到远程FTP路径中
	 * 
	 * @param ftpUrl
	 *            请求信息 格式：ftp://user:password@ip:port/path/fileName
	 * @param file
	 *            上传的文件
	 * @return 上传结果 false=失败;true=成功.
	 */
	public static boolean upload(String ftpUrl, File file, String newName) throws Exception {
		FtpConfInfo infoConf = getInfo(ftpUrl);
		if (infoConf == null) {
			log.error("构建FTP配置信息失败，请检查:" + ftpUrl);
			return false;
		}
		FTPClient ftpclient = connectServer(infoConf);
		if (ftpclient == null) {
			log.error("创建FTP客户端失败");
			return false;
		}
		if (newName == null)
			throw new Exception("上传文件方法的参数newName不允许为空！");
		if (infoConf.getLocation() != null) {
			String[] ss = infoConf.getLocation().split("/");
			for (String s : ss) {
				if (!existDirectory(ftpclient, s))
					ftpclient.mkd(s);
				ftpclient.changeWorkingDirectory(s);
			}
		}
		if (!file.exists()) {
			log.error("要上传的文件不存在");
			return false;
		}
		boolean storeResult = false;
		InputStream is = null;
		try {
			is = new FileInputStream(file);
			storeResult = ftpclient.storeFile(newName, is);
			if (!storeResult) {
				ftpclient.enterLocalPassiveMode();
				storeResult = ftpclient.storeFile(new String(newName.getBytes("GBK"), "iso-8859-1"), is);
			}
			log.debug("uploadFTP的当前目录" + ftpclient.printWorkingDirectory());
			ftpclient.logout();
			closeServer(ftpclient);
			if (storeResult)
				log.info("文件传输到FTP成功");
			else
				log.info("文件传输到FTP失败");
			return storeResult;
		} catch (Exception e) {
			log.error("", e);
			throw e;
		} finally {
			if (is != null)
				is.close();
		}
	}

	/**
	 * 删除FTP上的文件
	 * 
	 * @param ftpUrl
	 *            eg：请求信息 格式：ftp://user:password@ip:port/path/fileName
	 * @return 删除文件结果 false=失败;true=成功.
	 * @throws Exception
	 */
	public static boolean delete(String ftpUrl) throws Exception {
		boolean deleteResult = false;
		FtpConfInfo infoConf = getInfo(ftpUrl);
		if (infoConf == null) {
			log.error("构建FTP配置信息失败，请检查:" + ftpUrl);
			return false;
		}
		FTPClient ftpclient = connectServer(infoConf);
		if (ftpclient == null) {
			log.error("构建FTP客户端失败");
			return false;
		}
		if (infoConf.getFileName() != null) {
			if (infoConf.getLocation() != null) {
				deleteResult = ftpclient.deleteFile(infoConf.getLocation() + "/" + infoConf.getFileName());
			} else {
				deleteResult = ftpclient.deleteFile(infoConf.getFileName());
			}
		}
		ftpclient.logout();
		closeServer(ftpclient);
		return deleteResult;
	}

	/**
	 * 获得FTP对象信息
	 * 
	 * @param ftpInfo
	 * @return FtpConfInfo
	 */
	private static FtpConfInfo getInfo(String ftpInfo) {
		if (ftpInfo == null) {
			return null;
		}

		String regEx = "^ftp://([\\w]+:[\\S]*@)?[\\S]+/[^\\/:*?\"<>|]*$";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(ftpInfo);
		if (!m.find()) {
			return null;
		}

		String str = ftpInfo.substring("ftp://".length());
		String serverInfo = str.substring(0, str.indexOf("/"));
		String fileName = str.substring(str.indexOf("/") + 1);
		// if path exist
		String path = null;
		if (fileName.indexOf("/") > -1) {
			path = fileName.substring(0, fileName.lastIndexOf("/"));
			fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
		}
		// 解析ftp用户名、密码、IP、端口号 特别是密码含有特殊字符的如：
		int k = serverInfo.indexOf(":");
		String serverName = serverInfo.substring(0, k);
		String server = serverInfo.substring(k + 1);
		int j = server.lastIndexOf("@");
		String serverPwd = server.substring(0, j);

		String serverIpPort = server.substring(j + 1);
		int i = serverIpPort.indexOf(":");
		String serverIp = null;
		String serverPort = null;
		if (i == -1) {
			serverIp = serverIpPort;
		} else {
			serverIp = serverIpPort.substring(0, i);
			serverPort = serverIpPort.substring(i + 1);
		}
		FtpConfInfo conf = new FtpConfInfo();

		conf.setUser(serverName);
		conf.setPassword(serverPwd);
		conf.setServer(serverIp);
		conf.setLocation(path);
		conf.setFileName(fileName);
		conf.setMaxWorkTime(60 * 1000l);// 默认60秒完成
		if (!StringUtils.isBlank(serverPort)) {
			try {
				conf.setPort(Integer.parseInt(serverPort));
			} catch (ClassCastException e) {
				// 设置默认端口 21
				conf.setPort(21);
			}

		} else {
			// 设置默认端口 21
			conf.setPort(21);
		}

		return conf;

	}

	/**
	 * @param ftpClient
	 * @param path
	 * @return false=失败;true=成功.
	 * @throws IOException
	 */
	private static boolean existDirectory(FTPClient ftpClient, String path) throws IOException {
		boolean flag = false;
		FTPFile[] listFiles;
		try {
			String Localpath = ftpClient.printWorkingDirectory();
			log.debug(Localpath);
			listFiles = listFiles(ftpClient, null);
		} catch (Exception e) {
			log.error("检查FTP路径发送错误", e);
			return false;
		}
		if (listFiles == null)
			return false;
		for (FTPFile ffile : listFiles) {
			boolean isFile = ffile.isFile();
			if (!isFile) {
				if (ffile.getName().equalsIgnoreCase(path)) {
					flag = true;
					break;
				}
			}
		}
		return flag;
	}

	/**
	 * (私有方法)连接FTP，返回FTPClient连接[返回根目录]，使用完连接后，调用closeServer关闭连接
	 * 
	 * @param conf
	 * @return FTPClient
	 * @throws SocketException
	 * @throws IOException
	 */
	private static FTPClient connectServer(FtpConfInfo conf) throws SocketException, IOException {
		FTPClient ftpClient = new FTPClient();
		ftpClient.setConnectTimeout(5000);// 超时设置,建议采用配置
		ftpClient.setDataTimeout(10 * 1000);// 设置数据响应超时时间默认10秒

		try {
			ftpClient.connect(conf.getServer(), conf.getPort());
		} catch (SocketException e) {
			log.error(
					"FTP服务器连接超时,请检查FTP服务器地址及端口配置是否正确:FTPServer[" + conf.getServer() + "]--Port[" + conf.getPort() + "]",
					e);
			throw e;
		} catch (IOException e) {
			log.error(
					"FTP服务器连接超时,请检查FTP服务器地址及端口配置是否正确:FTPServer[" + conf.getServer() + "]--Port[" + conf.getPort() + "]",
					e);
			throw e;
		}

		if (FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {

			if (ftpClient.login(conf.getUser(), conf.getPassword())) {
				ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
				ftpClient.sendCommand("FEAT");
				String featRS = ftpClient.getReplyString();
				String encoding = "GBK";
				if (conf.getLocation() != null) {
					conf.setLocation(new String(conf.getLocation().getBytes(encoding), FtpUtil.ISO_8859_1));
				}
				if (conf.getFileName() != null) {
					conf.setFileName(new String(conf.getFileName().getBytes("GBK"), FtpUtil.ISO_8859_1));
				}
				conf.setEncoding(encoding);// 将获取的编码信息带出来
				ftpClient.setActivePortRange(9000, 9100);// 主动模式下设置客户方端口范围9000-9100
				return ftpClient;
			}
			closeServer(ftpClient);
			log.error("FTP的用户名和密码不对");
			log.error(conf.toString());
			return null;
		}
		closeServer(ftpClient);
		log.error("FTP的连接失败");
		log.error(conf.toString());
		return null;
	}

	/**
	 * (私有方法)关闭FTP连接
	 * 
	 * @param ftpClient
	 * @throws IOException
	 */
	private static void closeServer(FTPClient ftpClient) throws IOException {
		if (ftpClient != null && ftpClient.isConnected()) {
			ftpClient.disconnect();
		}
	}

	/**
	 * 取得ftp文件
	 * 
	 * @param ftp
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	private static FTPFile[] listFiles(FTPClient ftp, String fileName) throws IOException {
		FTPFile[] files = null;
		if (!StringUtils.isBlank(fileName)) {
			files = ftp.listFiles(fileName);
			if (files.length != 1) {
				// 如果没有不能下载文件，再用被动模式试一次（lidahu）
				ftp.enterLocalPassiveMode();
				files = ftp.listFiles(fileName);
			}
		} else {
			files = ftp.listFiles();
			// 以被动模式再试一次
			if (files == null || files.length == 0) {
				ftp.enterLocalPassiveMode();
				files = ftp.listFiles();
			}
		}
		return files;
	}

	/**
	 * (私有方法) 下载文件
	 * 
	 * @param ftp
	 * @param fileName
	 * @param maxSize
	 *            为-1不检查文件大小
	 * @return String
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	private static String downFile(FtpConfInfo ftpConfInfo, String path, FTPClient ftp, String fileName, long maxSize)
			throws UnsupportedEncodingException, IOException, Exception {
		if (ftp == null || fileName == null || fileName.length() < 1) {
			log.error("文件名为空或不存在");
			return null;
		}
		FTPFile[] files = listFiles(ftp, fileName);
		if (files.length != 1) {
			log.error("PATH:" + path + "|FileName:" + fileName + "当前文件不存在或有多个,实际文件个数为:" + files.length);
			return null;
		}

		FTPFile file = files[0];
		if (!file.isFile()) {
			log.error(file.getName() + "不是文件！");
			return null;
		}
		long lRemoteSize = file.getSize();
		long maxTime = lRemoteSize / 1024 / 50;// 50K一秒,
		ftpConfInfo.setMaxWorkTime(maxTime * 1000);

		if (path == null || path.equals("")) {
			String temp = System.getProperty("user.dir");
			path = temp;
		}
		File f = new File(path);
		if (!f.exists()) {
			boolean mkResult = f.mkdirs();
			if (!mkResult)
				log.warn("make dir failed");
		}
		path = path + File.separator + fileName;
		try {
			File localFile = new File(new String(path.getBytes("ISO-8859-1"), "GBK"));
			OutputStream output = new FileOutputStream(localFile);
			ftp.retrieveFile(fileName, output);
			output.close();
		} catch (Exception e) {
			log.error("下载文件" + path + "失败", e);
			throw new Exception("FTP传输失败，中断连接");
		} finally {
			FtpUtil.closeServer(ftp);
		}
		return path;
	}

	public static String repairFtpString(String ftpString) {
		if (!StringUtils.isBlank(ftpString) && FTP_PATH_CHAR != ftpString.charAt(ftpString.length() - 1)) {
			return ftpString + FTP_PATH_CHAR;
		} else {
			return ftpString;
		}
	}

	static class RFS extends Thread {
		private FTPClient ftp = null;
		volatile long count = 0;
		private String path = null;
		private String fileName = null;
		private FtpConfInfo ftpConfInfo = null;

		RFS(FtpConfInfo ftpConfInfo, FTPClient ftp, String path, String fileName) {
			this.ftp = ftp;
			this.path = path;
			this.ftpConfInfo = ftpConfInfo;
			this.fileName = fileName;
		}

		public void myInterrupt() throws IOException {
			Thread.currentThread().interrupt();
			FtpUtil.closeServer(ftp);

		}

		private void copy(FtpConfInfo ftpConfInfo, InputStream is, OutputStream os) throws IOException, Exception {
			long timeA = System.currentTimeMillis();
			byte[] buffer = new byte[1024];
			count = 0;
			int n = 0;
			while (-1 != (n = is.read(buffer))) {
				os.write(buffer, 0, n);
				count += n;
			}
			long copyTime = (System.currentTimeMillis() - timeA);
			if (copyTime < 1) {
				copyTime = 1;
			}
			log.debug("传输速率为(byte/毫秒):" + (count / copyTime) + "传输时间(毫秒)" + copyTime);
		}

		public void run() {
			InputStream stO = null;
			OutputStream stD = null;
			try {
				stO = new BufferedInputStream(ftp.retrieveFileStream(fileName));
				stD = new FileOutputStream(path);
				log.debug("准备从FTP拷贝文件");
				copy(ftpConfInfo, stO, stD);
				stD.flush();
				log.debug("文件拷贝完成");
				ftp.completePendingCommand();
			} catch (Exception e) {
				log.error("文件传输异常", e);
			} finally {
				if (stO != null) {
					try {
						stO.close();
					} catch (IOException e) {
						log.error("FTP关闭输入流异常", e);
					}
				}
				if (stD != null) {
					try {
						stD.flush();
						stD.close();
					} catch (IOException e) {
						log.error("FTP关闭输出流异常", e);
					}
				}
				stO = null;
				stD = null;
			}
		}
	}
}