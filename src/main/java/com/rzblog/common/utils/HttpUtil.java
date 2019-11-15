package com.rzblog.common.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/*
 * http请求方法类
 * 
 */
public class HttpUtil {

	// doget
	public static String doGet(String httpurl) {
		HttpURLConnection connection = null;
		InputStream is = null;
		BufferedReader br = null;
		String result = null;// 返回结果字符串
		try {
			// 创建远程url连接对象
			URL url = new URL(httpurl);
			// 通过远程url连接对象打开一个连接，强转成httpURLConnection类
			connection = (HttpURLConnection) url.openConnection();
			// 设置连接方式：get
			connection.setRequestMethod("GET");
			// 设置连接主机服务器的超时时间：15000毫秒
			connection.setConnectTimeout(15000);
			// 设置读取远程返回的数据时间：60000毫秒
			connection.setReadTimeout(60000);
			// 发送请求
			connection.connect();
			// 通过connection连接，获取输入流
			if (connection.getResponseCode() == 200) {
				is = connection.getInputStream();

				byte[] buffer = new byte[521];
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				for (int len = 0; (len = is.read(buffer)) > 0;) {
					baos.write(buffer, 0, len);
				}
				result = new String(baos.toByteArray(), "utf-8");
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 关闭资源
			if (null != br) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (null != is) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			connection.disconnect();// 关闭远程连接
		}

		return result;
	}

	// dopost
	public static String doPost(String httpUrl, String param) {
		HttpURLConnection connection = null;
		InputStream is = null;
		OutputStream os = null;
		BufferedReader br = null;
		String result = null;
		try {
			URL url = new URL(httpUrl);
			// 通过远程url连接对象打开连接
			connection = (HttpURLConnection) url.openConnection();
			// 设置连接请求方式
			connection.setRequestMethod("POST");
			// 设置连接主机服务器超时时间：15000毫秒
			connection.setConnectTimeout(15000);
			// 设置读取主机服务器返回数据超时时间：60000毫秒
			connection.setReadTimeout(60000);

			// 默认值为：false，当向远程服务器传送数据/写数据时，需要设置为true
			connection.setDoOutput(true);
			// 默认值为：true，当前向远程服务读取数据时，设置为true，该参数可有可无
			connection.setDoInput(true);
			// 设置传入参数的格式:请求参数应该是 name1=value1&name2=value2 的形式。
			connection.setRequestProperty("Content-Type", "application/json");
			// 设置鉴权信息：Authorization: Bearer da3efcbf-0845-4fe3-8aba-ee040be542c0
			// connection.setRequestProperty("Authorization", "Bearer
			// da3efcbf-0845-4fe3-8aba-ee040be542c0");
			// 通过连接对象获取一个输出流
			os = connection.getOutputStream();
			// 通过输出流对象将参数写出去/传输出去,它是通过字节数组写出的
			if (param != null) {
				os.write(param.getBytes());
			}

			// 通过连接对象获取一个输入流，向远程读取
			if (connection.getResponseCode() == 200) {

				is = connection.getInputStream();
				// 对输入流对象进行包装:charset根据工作项目组的要求来设置
				br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

				StringBuffer sbf = new StringBuffer();
				String temp = null;
				// 循环遍历一行一行读取数据
				while ((temp = br.readLine()) != null) {
					sbf.append(temp);
					sbf.append("\r\n");
				}
				result = sbf.toString();
				System.out.println(result);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 关闭资源
			if (null != br) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != os) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != is) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			// 断开与远程地址url的连接
			connection.disconnect();
		}
		return result;
	}

	// getContent，效果一样
	public static String getContent(String url, Map<String, String> heads, String charset, String method)
			throws Exception {
		URL httpUrl = new URL(url);
		HttpURLConnection httpURLConnection = (HttpURLConnection) httpUrl.openConnection();
		// 设置报头
		if (heads != null) {
			for (Map.Entry entry : heads.entrySet()) {
				httpURLConnection.setRequestProperty((String) entry.getKey(), (String) entry.getValue());
			}
		}
		httpURLConnection.setRequestMethod(method);
		httpURLConnection.setDoInput(true);
		httpURLConnection.setInstanceFollowRedirects(true);
		httpURLConnection.connect();// 握手

		String result = "";
		BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), charset));
		String line;
		while ((line = in.readLine()) != null) {
			result += line;
		}
		return result;
	}

	// 根据连接下载文件
	// 注意header是有针对性的
	// 更新下载策略，指定下载文件名，以：软件名+版本号+.exe为主
	public static boolean downloadFile(String fileLocal, HttpURLConnection urlCon) throws Exception {

		DataInputStream in = new DataInputStream(urlCon.getInputStream());

		DataOutputStream out = new DataOutputStream(new FileOutputStream(fileLocal));
		byte[] buffer = new byte[2048];
		int count = 0;
		while ((count = in.read(buffer)) > 0) {
			out.write(buffer, 0, count);
		}
		try {
			if (out != null) {
				out.close();
			}
			if (in != null) {
				in.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static JSONObject getJsonContent(String url, Map<String, String> heads, String charset, String method)
			throws Exception {
		String data = getContent(url, heads, charset, method);
		if (data == null) {
			return null;
		}
		return JSON.parseObject(data);
	}

	// 检查网络
	public static boolean checkNetWork(String url) {
		try {
			URL u = new URL(url);
			InputStream in = u.openStream();
			in.close();
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	// 总的判断
	public static boolean checkNetWorkOk() {
		return checkNetWork("http://www.baidu.com");
	}

}
