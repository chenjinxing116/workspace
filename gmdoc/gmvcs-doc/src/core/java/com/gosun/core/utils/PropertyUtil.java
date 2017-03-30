package com.gosun.core.utils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class PropertyUtil {
	public static String getPropertyValue(String key, String propertyFilePath) {
		Properties props = new Properties();
		try {
			InputStream in = new BufferedInputStream(new FileInputStream(propertyFilePath));
			props.load(in);
			in.close();
			return props.getProperty(key);
		} catch (Exception e) {
			return null;
		}
	}

}
