package com.goldmsg.gmomm.utils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.goldmsg.gmomm.system.ApplicationProperties;

@Repository
public class FileUploadUtil {

	@Autowired
	static ApplicationProperties props;

	/*
	 * @param HttpServletRequest
	 * 
	 * @return String 上传文件并返回文件的存储路径
	 */
	public static String upload(HttpServletRequest request) {
		String path = "";
		// 创建一个通用的多部分解析器
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		// 判断 request 是否有文件上传,即多部分请求
		if (multipartResolver.isMultipart(request)) {
			// 转换成多部分request
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			// 取得request中的所有文件名
			Iterator<String> iter = multiRequest.getFileNames();
			while (iter.hasNext()) {
				// 取得上传文件
				MultipartFile file = multiRequest.getFile(iter.next());
				if (file != null) {
					// 取得当前上传文件的文件名称
					String myFileName = file.getOriginalFilename();
					// 如果名称不为“”,说明该文件存在，否则说明该文件不存在
					if (!myFileName.trim().equals("")) {
						// 重命名上传后的文件名
						SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");
						String date = df.format(new Date());
						String fileName = date + "_" + file.getOriginalFilename();
						// 定义上传路径
						String tempPath = props.getUploadTempdir();
						path = tempPath + "/" + fileName;
						File localFile = new File(path);
						if (!localFile.getParentFile().exists()) {
							localFile.getParentFile().mkdirs();
						}
						try {
							file.transferTo(localFile);
						} catch (IllegalStateException | IOException e) {
							// TODO Auto-generated catch block
							path = "";
							e.printStackTrace();
						}
					}
				}
			}
		}
		return path;
	}

	/*
	 * @param HttpServletRequest
	 * 
	 * @return Boolean 检查判断是否是Excel文件
	 */
	public static boolean isExcel(HttpServletRequest request) {
		boolean flag = true;
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		// 判断 request 是否有文件上传,即多部分请求
		if (multipartResolver.isMultipart(request)) {
			// 转换成多部分request
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			// 取得request中的所有文件名
			Iterator<String> iter = multiRequest.getFileNames();
			while (iter.hasNext()) {
				// 取得上传文件
				MultipartFile file = multiRequest.getFile(iter.next());
				String filName = file.getOriginalFilename();
				String extension = filName.substring(filName.lastIndexOf(".") + 1);
				if (!"xls".equals(extension) && !"xlsx".equals(extension)) {
					flag = false;
				}
			}
		}
		return flag;
	}

	public static ApplicationProperties getProps() {
		return props;
	}

	public static void setProps(ApplicationProperties props) {
		FileUploadUtil.props = props;
	}

}
