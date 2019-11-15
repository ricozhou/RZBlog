package com.rzblog.common.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.text.StrBuilder;

import com.rzblog.common.support.StrFormatter;
import com.rzblog.common.constant.CommonSymbolicConstant;
import com.rzblog.common.constant.RegularExpressionConstant;

/**
 * 字符串工具类
 * 
 * @author ricozhou
 */
public class StringUtils {
	/** 空字符串 */
	private static final String NULLSTR = "";

	/**
	 * 获取参数不为空值
	 * 
	 * @param value
	 *            defaultValue 要判断的value
	 * @return value 返回值
	 */
	public static <T> T nvl(T value, T defaultValue) {
		return value != null ? value : defaultValue;
	}

	/**
	 * * 判断一个Collection是否为空， 包含List，Set，Queue
	 * 
	 * @param coll
	 *            要判断的Collection
	 * @return true：为空 false：非空
	 */
	public static boolean isEmpty(Collection<?> coll) {
		return isNull(coll) || coll.isEmpty();
	}

	/**
	 * * 判断一个Collection是否非空，包含List，Set，Queue
	 * 
	 * @param coll
	 *            要判断的Collection
	 * @return true：非空 false：空
	 */
	public static boolean isNotEmpty(Collection<?> coll) {
		return !isEmpty(coll);
	}

	/**
	 * * 判断一个对象数组是否为空
	 * 
	 * @param objects
	 *            要判断的对象数组
	 ** @return true：为空 false：非空
	 */
	public static boolean isEmpty(Object[] objects) {
		return isNull(objects) || (objects.length == 0);
	}

	/**
	 * * 判断一个对象数组是否非空
	 * 
	 * @param objects
	 *            要判断的对象数组
	 * @return true：非空 false：空
	 */
	public static boolean isNotEmpty(Object[] objects) {
		return !isEmpty(objects);
	}

	/**
	 * * 判断一个Map是否为空
	 * 
	 * @param map
	 *            要判断的Map
	 * @return true：为空 false：非空
	 */
	public static boolean isEmpty(Map<?, ?> map) {
		return isNull(map) || map.isEmpty();
	}

	/**
	 * * 判断一个Map是否为空
	 * 
	 * @param map
	 *            要判断的Map
	 * @return true：非空 false：空
	 */
	public static boolean isNotEmpty(Map<?, ?> map) {
		return !isEmpty(map);
	}

	/**
	 * * 判断一个字符串是否为空串
	 * 
	 * @param str
	 *            String
	 * @return true：为空 false：非空
	 */
	public static boolean isEmpty(String str) {
		return isNull(str) || NULLSTR.equals(str.trim());
	}

	/**
	 * * 判断一个字符串是否为非空串
	 * 
	 * @param str
	 *            String
	 * @return true：非空串 false：空串
	 */
	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}

	/**
	 * * 判断一个对象是否为空
	 * 
	 * @param object
	 *            Object
	 * @return true：为空 false：非空
	 */
	public static boolean isNull(Object object) {
		return object == null;
	}

	/**
	 * * 判断一个对象是否非空
	 * 
	 * @param object
	 *            Object
	 * @return true：非空 false：空
	 */
	public static boolean isNotNull(Object object) {
		return !isNull(object);
	}

	/**
	 * * 判断一个对象是否是数组类型（Java基本型别的数组）
	 * 
	 * @param object
	 *            对象
	 * @return true：是数组 false：不是数组
	 */
	public static boolean isArray(Object object) {
		return isNotNull(object) && object.getClass().isArray();
	}

	/**
	 * 去空格
	 */
	public static String trim(String str) {
		return (str == null ? "" : str.trim());
	}

	/**
	 * 截取字符串
	 * 
	 * @param str
	 *            字符串
	 * @param start
	 *            开始
	 * @return 结果
	 */
	public static String substring(final String str, int start) {
		if (str == null) {
			return NULLSTR;
		}

		if (start < 0) {
			start = str.length() + start;
		}

		if (start < 0) {
			start = 0;
		}
		if (start > str.length()) {
			return NULLSTR;
		}

		return str.substring(start);
	}

	/**
	 * 截取字符串
	 * 
	 * @param str
	 *            字符串
	 * @param start
	 *            开始
	 * @param end
	 *            结束
	 * @return 结果
	 */
	public static String substring(final String str, int start, int end) {
		if (str == null) {
			return NULLSTR;
		}

		if (end < 0) {
			end = str.length() + end;
		}
		if (start < 0) {
			start = str.length() + start;
		}

		if (end > str.length()) {
			end = str.length();
		}

		if (start > end) {
			return NULLSTR;
		}

		if (start < 0) {
			start = 0;
		}
		if (end < 0) {
			end = 0;
		}

		return str.substring(start, end);
	}

	public static String uncapitalize(String str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0) {
			return str;
		}
		return new StrBuilder(strLen).append(Character.toLowerCase(str.charAt(0))).append(str.substring(1)).toString();
	}

	/**
	 * 是否包含字符串
	 * 
	 * @param str
	 *            验证字符串
	 * @param strs
	 *            字符串组
	 * @return 包含返回true
	 */
	public static boolean inStringIgnoreCase(String str, String... strs) {
		if (str != null && strs != null) {
			for (String s : strs) {
				if (str.equalsIgnoreCase(trim(s))) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 将下划线大写方式命名的字符串转换为驼峰式。如果转换前的下划线大写方式命名的字符串为空，则返回空字符串。
	 * 例如：HELLO_WORLD->HelloWorld
	 * 
	 * @param name
	 *            转换前的下划线大写方式命名的字符串
	 * @return 转换后的驼峰式命名的字符串
	 */
	public static String convertToCamelCase(String name) {
		StringBuilder result = new StringBuilder();
		// 快速检查
		if (name == null || name.isEmpty()) {
			// 没必要转换
			return "";
		} else if (!name.contains("_")) {
			// 不含下划线，仅将首字母大写
			return name.substring(0, 1).toUpperCase() + name.substring(1);
		}
		// 用下划线将原始字符串分割
		String[] camels = name.split("_");
		for (String camel : camels) {
			// 跳过原始字符串中开头、结尾的下换线或双重下划线
			if (camel.isEmpty()) {
				continue;
			}
			// 首字母大写
			result.append(camel.substring(0, 1).toUpperCase());
			result.append(camel.substring(1).toLowerCase());
		}
		return result.toString();
	}

	/**
	 * 字符串数据处理
	 */
	public static String valueAsStr(Object value) {
		if (value instanceof String) {
			return (String) value;
		} else if (value != null) {
			return value.toString();
		} else {
			return null;
		}
	}

	/**
	 * 整型数据处理
	 */
	public static Integer valueAsInt(Object value) {
		if (value instanceof Integer) {
			return (Integer) value;
		} else if (value instanceof Number) {
			return ((Number) value).intValue();
		} else if (value instanceof String) {
			if ("NaN".equals(value)) {
				return null;
			}
			return Integer.valueOf((String) value);
		} else if (value instanceof Boolean) {
			return ((Boolean) value) ? 1 : 0;
		} else {
			return null;
		}
	}

	// cmd特殊字符转义
	public static String replaceSpecialCharForCmd(String inputDir) {
		// return inputDir.replaceAll(" ", "").replaceAll("\n",
		// "").replaceAll("\"", "\"\"\"").replaceAll("&", "\"&\"")
		// .replaceAll("(", "\"(\"").replaceAll(")", "\")\"").replaceAll("[",
		// "\"[\"").replaceAll("]", "\"]\"")
		// .replaceAll("{", "\"{\"").replaceAll("}", "\"}\"").replaceAll("^",
		// "\"^\"").replaceAll("=", "\"=\"")
		// .replaceAll(";", "\";\"").replaceAll("!", "\"!\"").replaceAll("'",
		// "\"'\"").replaceAll("+", "\"+\"")
		// .replaceAll(",", "\",\"").replaceAll("`", "\"`\"").replaceAll("~",
		// "\"~\"");
		// return inputDir.replaceAll(" ", "").replaceAll("\n",
		// "").replaceAll("\"", "\"\"\"").replaceAll("\\&", "\"\\&\"")
		// .replaceAll("\\(", "\"\\(\"").replaceAll("\\)",
		// "\"\\)\"").replaceAll("\\[", "\"\\[\"").replaceAll("\\]", "\"\\]\"")
		// .replaceAll("\\{", "\"\\{\"").replaceAll("\\}",
		// "\\\"}\"").replaceAll("\\^", "\"\\^\"").replaceAll("\\=", "\"\\=\"")
		// .replaceAll("\\;", "\"\\;\"").replaceAll("\\!",
		// "\"\\!\"").replaceAll("\\'", "\"\\'\"").replaceAll("\\+", "\"\\+\"")
		// .replaceAll("\\,", "\"\\,\"").replaceAll("\\`",
		// "\"\\`\"").replaceAll("\\~", "\"\\~\"");
		return inputDir.trim().replaceAll("\\s*", "").replaceAll(" ", "").replaceAll("\n", "").replaceAll("\"",
				"\"\"\"");
		// final String specialChars = "&()[]{}^=;!'+,`~";
		// char c;
		// StringBuilder sb = new StringBuilder(inputDir.length() * 3);
		// for (int i = 0; i < inputDir.length(); ++i) {
		// c = inputDir.charAt(i);
		// if (specialChars.indexOf(c) >= 0) {
		// sb.append('"').append(c).append('"');
		// } else {
		// sb.append(c);
		// }
		// }
		// return sb.toString();
	}

	public static String getNotNullString(String str) {
		return str != null ? str : CommonSymbolicConstant.EMPTY_STRING;
	}

	// 格式化字符串为一行
	public static String formatStringToOneString(String content) {
		return getNotNullString(content).replaceAll(CommonSymbolicConstant.LINEBREAK3,
				CommonSymbolicConstant.EMPTY_STRING);
	}

	// 匹配结果返回数组
	public static String[] GetRegResult(String pattern, String str) {
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(str);

		ArrayList<String> tempList = new ArrayList<String>();
		while (m.find()) {
			tempList.add(m.group());
		}
		String[] res = new String[tempList.size()];
		int i = 0;
		for (String temp : tempList) {
			res[i] = temp;
			i++;
		}
		return res;
	}

	// 匹配结果返回list
	public static List<String> GetRegResultToList(String pattern, String str) {
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(str);

		ArrayList<String> tempList = new ArrayList<String>();
		while (m.find()) {
			tempList.add(m.group());
		}
		return tempList;
	}

	// 判断是否为空和null
	public static boolean notNullNotEmpty(String string) {
		return string != null && !CommonSymbolicConstant.EMPTY_STRING.equals(string);
	}

	// 根据正则表达式替换字符串
	public static String replaceString(String str, String reg, String replacement) {
		Pattern pattern = Pattern.compile(reg);
		Matcher matcher = pattern.matcher(str);
		return matcher.replaceAll(replacement);
	}

	/**
	 * 格式化文本, {} 表示占位符<br>
	 * 此方法只是简单将占位符 {} 按照顺序替换为参数<br>
	 * 如果想输出 {} 使用 \\转义 { 即可，如果想输出 {} 之前的 \ 使用双转义符 \\\\ 即可<br>
	 * 例：<br>
	 * 通常使用：format("this is {} for {}", "a", "b") -> this is a for b<br>
	 * 转义{}： format("this is \\{} for {}", "a", "b") -> this is \{} for a<br>
	 * 转义\： format("this is \\\\{} for {}", "a", "b") -> this is \a for b<br>
	 * 
	 * @param template
	 *            文本模板，被替换的部分用 {} 表示
	 * @param params
	 *            参数值
	 * @return 格式化后的文本
	 */
	public static String format(String template, Object... params) {
		if (isEmpty(params) || isEmpty(template)) {
			return template;
		}
		return StrFormatter.format(template, params);
	}

	// 获取uuid
	public static String getUUID() {
		String uuid = UUID.randomUUID() + "";
		return uuid.replaceAll("-", "");
	}

	// 验证ip
	// checkip
	public static boolean checkIp(String ip) {
		return ip.matches(RegularExpressionConstant.REGULAR_EXPRESSION_IP);
	}
}