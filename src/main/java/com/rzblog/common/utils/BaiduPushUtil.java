package com.rzblog.common.utils;

import javax.net.ssl.HttpsURLConnection;
import java.io.OutputStream;
import java.net.HttpURLConnection;

/**
 * 百度站长推送工具类
 *
 */
public class BaiduPushUtil extends RestClientUtil {

	/**
	 * 推送链接到百度站长平台
	 *
	 */
	public static String doPush(String urlString, String params) {
		HttpURLConnection connection = null;
		try {
			connection = openConnection(urlString);
			connection.setRequestMethod("POST");
			// (如果不设此项,json数据 ,当WEB服务默认的不是这种类型时可能抛java.io.EOFException)
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			connection.setRequestProperty("Action", "1000");
			connection.setRequestProperty("User-Agent", USER_AGENT);
			connection.setRequestProperty("Connection", "keep-alive");
			// connection.setRequestProperty("Cookie", COOKIE);
			connection.setDoOutput(true);
			connection.setDoInput(true);
			// 设置连接超时时间，单位毫秒
			connection.setConnectTimeout(10000);
			// 设置读取数据超时时间，单位毫秒
			connection.setReadTimeout(10000);
			// Post 请求不能使用缓存
			connection.setUseCaches(false);
			if (params != null) {
				final OutputStream outputStream = connection.getOutputStream();
				writeOutput(outputStream, params);
				outputStream.close();
			}
			if (connection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
				return readInput(connection.getInputStream(), "UTF-8");
			} else {
				return readInput(connection.getErrorStream(), "UTF-8");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		} finally {
			if (null != connection) {
				connection.disconnect();
			}
		}
	}
}
