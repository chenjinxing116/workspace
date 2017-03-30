package com.goldmsg.core.helpers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import org.springframework.core.io.support.PropertiesLoaderUtils;

public final class PropertiesHelper {

	private static final String PROPNAME = "application.properties";

	private static Properties p;

	static {
		try {
			p = PropertiesLoaderUtils.loadAllProperties(PROPNAME);
		} catch (IOException e) {
			p = new Properties();
		}
	}

	private PropertiesHelper() {
		throw new UnsupportedOperationException("PropertiesHelper is a helper class,can't be initated");
	}

	public static String getProperty(String key) {
		return p.getProperty(key);
	}

	public static boolean getBoolean(String key) {
		return Boolean.valueOf(p.getProperty(key)).booleanValue();
	}

	public static boolean getBoolean(String key, boolean defaultValue) {
		String setting = p.getProperty(key);
		return (setting == null) ? defaultValue : Boolean.valueOf(setting).booleanValue();
	}

	public static int getInt(String key, int defaultValue) {
		String propValue = p.getProperty(key);
		return (propValue == null) ? defaultValue : Integer.parseInt(propValue);
	}

	public static long getLong(String key, long defaultValue) {
		String propValue = p.getProperty(key);
		return (propValue == null) ? defaultValue : Long.parseLong(propValue);
	}

	public static String getString(String key, String defaultValue) {
		String propValue = p.getProperty(key);
		return (propValue == null) ? defaultValue : propValue;
	}

	public static Integer getInteger(String key) {
		String propValue = p.getProperty(key);
		return (propValue == null) ? null : Integer.valueOf(propValue);
	}

	public static Map<String, String> toMap(String key, String delim) {
		Map<String, String> map = new HashMap<String, String>();
		String propValue = p.getProperty(key);
		if (propValue != null) {
			StringTokenizer tokens = new StringTokenizer(propValue, delim);
			while (tokens.hasMoreTokens()) {
				map.put(tokens.nextToken(), tokens.hasMoreElements() ? tokens.nextToken() : "");
			}
		}
		return map;
	}

	public static String[] toStringArray(String key, String delim) {
		return toStringArrayByValue(p.getProperty(key), delim);
	}

	private static String[] toStringArrayByValue(String propValue, String delim) {
		if (propValue != null) {
			return split(delim, propValue);
		} else {
			return null;
		}
	}

	private static String[] split(String seperators, String list) {
		return split(seperators, list, false);
	}

	private static String[] split(String seperators, String list, boolean include) {
		StringTokenizer tokens = new StringTokenizer(list, seperators, include);
		String[] result = new String[tokens.countTokens()];
		int i = 0;
		while (tokens.hasMoreTokens()) {
			result[i++] = tokens.nextToken();
		}
		return result;
	}

	/**
	 * replace a property by a starred version
	 * 
	 * @param props
	 *            p to check
	 * @param key
	 *            proeprty to mask
	 * @return cloned and masked p
	 */
	public static Properties maskOut(String key) {
		Properties clone = (Properties) p.clone();
		if (clone.get(key) != null) {
			clone.setProperty(key, "****");
		}
		return clone;
	}

}
