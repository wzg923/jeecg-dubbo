package com.guoyicap.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.apache.commons.lang.StringUtils;

/**
 * 数值类处理
 * 
 * @author 王振广
 * @date 2015-12-11
 * @version v1.0
 *
 */
public class NumberUtil {

	/**
	 * 转换为BigDecimal
	 * 
	 * @param s
	 * @return
	 */
	public static BigDecimal parseBigDecimal(String s) {
		if (s == null || "".equals(s)) {
			return null;
		}
		s = delcommafy(s);
		// 负号 处理
		String sysble = "";
		if (s.indexOf("-") == 0) {
			s = s.substring(1);
			sysble = "-";
		}
		if (StringUtils.isNumeric(s.replace(".", ""))) {
			return BigDecimal.valueOf(Double.valueOf(sysble + s));
		}
		return null;
	}

	/**
	 * 转换为Double
	 * 
	 * @param s
	 * @return
	 */
	public static Double parseDouble(String s) {
		if (s == null || "".equals(s)) {
			return null;
		}
		s = delcommafy(s);
		// 负号 处理
		String sysble = "";
		if (s.indexOf("-") == 0) {
			s = s.substring(1);
			sysble = "-";
		}
		if (StringUtils.isNumeric(s.replace(".", ""))) {
			return Double.valueOf(sysble + s);
		}
		return null;
	}

	/**
	 * 转换为Integer
	 * 
	 * @param s
	 * @return
	 */
	public static Integer parseInteger(String s) {
		if (s == null || "".equals(s)) {
			return null;
		}
		s = delcommafy(s);
		// 负号 处理
		String sysble = "";
		if (s.indexOf("-") == 0) {
			s = s.substring(1);
			sysble = "-";
		}
		if (StringUtils.isNumeric(s.replace(".", ""))) {
			return Integer.valueOf(sysble+s);
		}
		return null;
	}

	/**
	 * 取消千分位
	 * 
	 * @param s
	 * @return
	 */
	public static String delcommafy(String s) {
		if (s == null || "".equals(s)) {
			return "";
		}
		s = s.replaceAll(",", "");
		return s;
	}

	/**
	 * 金额格式化，将字符串格式化为带千分位分隔符的字符串
	 * 
	 * @param s
	 * @param len
	 * @return
	 */
	public static String formatMoney(String s, int len) {
		if (s == null || s.length() < 1) {
			return "";
		}
		NumberFormat formater = null;
		double num = Double.parseDouble(s);
		if (len == 0) {
			formater = new DecimalFormat("###,###");

		} else {
			StringBuffer buff = new StringBuffer();
			buff.append("###,###.");
			for (int i = 0; i < len; i++) {
				buff.append("#");
			}
			formater = new DecimalFormat(buff.toString());
		}
		String result = formater.format(num);
		if (result.indexOf(".") == -1) {
			result = result + ".";
			if (len > 0) {
				for (int i = 0; i < len; i++) {
					result += "0";
				}
			}
		} else {
			result = result;
		}
		return result;
	}

	/**
	 * @Title: formatNumber
	 * @Description: 数字格式化
	 * @param s
	 *            数字String
	 * @param len
	 *            小数位数
	 * @return String 返回类型
	 */
	public static String formatNumber(String s, int len) {
		if (s == null || s.length() < 1) {
			return "";
		}
		NumberFormat formater = null;
		double num = Double.parseDouble(s);
		if (len == 0) {
			formater = new DecimalFormat("####");

		} else {
			StringBuffer buff = new StringBuffer();
			buff.append("####.");
			for (int i = 0; i < len; i++) {
				buff.append("#");
			}
			formater = new DecimalFormat(buff.toString());
		}
		String result = formater.format(num);
		if (result.indexOf(".") == -1) {
			result = result + ".";
			if (len > 0) {
				for (int i = 0; i < len; i++) {
					result += "0";
				}
			}
		} else {
			int length = result.length() - result.indexOf(".") - 1;
			if (length < len) {
				for (int i = 0; i < len - length; i++) {
					result += "0";
				}
			} else {
				result = result;
			}
		}
		return result;
	}

	/**
	 * 使用BigDecimal，保留小数点后两位
	 */
	public static String formatBigDecimal(String value, int len) {
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(len, RoundingMode.HALF_UP);
		return bd.toString();
	}
	
	
	/**
	* @Title: formatBigDecimal
	* @author: 王振广
	* @Description: BigDecimal 金额转字符串
	* @param @param value
	* @param @param len
	* @param @return    设定文件
	* @return String    返回类型
	*/ 
	public static String formatBigDecimal(BigDecimal value, int len) {
		//BigDecimal bd = new BigDecimal(value);
		value = value.setScale(len, RoundingMode.HALF_UP);
		return value.toString();
	}

	public static void main(String[] args) {
		String s1 = NumberUtil.formatNumber("0.1", 2);
		String s2 = NumberUtil.formatNumber("1234.1", 10);
		String s3 = NumberUtil.formatNumber(BigDecimal.valueOf(12345.264).toString(), 2);
		String s4 = NumberUtil.formatNumber(Double.valueOf("25644.125").toString(), 2);

		String s5 = NumberUtil.formatBigDecimal("1234.3", 2);
		System.out.println(s1);

	}
}
