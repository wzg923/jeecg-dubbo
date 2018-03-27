package com.guoyicap.util;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.jeecgframework.core.util.LogUtil;
import org.jeecgframework.web.cgform.util.QueryParamUtil;
import org.springframework.web.util.HtmlUtils;

/**
 * @author guang
 * String 转义工具类
 */
public class StringEscapeUtil {
	
	/**
	 * SQL转义<p>
	 * 功能： escapeSql 提供sql转移功能，防止sql注入攻击，<p>
	 * 例如典型的万能密码攻击' ' or 1=1 ' '<p>
	 * @param sql
	 * @return
	 */
	public static String escapeSql(String str) {
		// sql=StringEscapeUtils.escapeSql(sql);
		//edit 修改 特殊字符替换  by wangzhenguang end<-
		if (str == null) {
			return null;
		}
		str = StringUtils.replace(str, "'", "\"");
		str = StringUtils.replace(str, ",", "");
		str = StringUtils.replace(str, ";", "");
		//str = StringUtils.replace(str, ":", "");
		//str = StringUtils.replace(str, "\"", "\"\"");
		//str = StringUtils.replace(str, "%", "");
		str = StringUtils.replace(str, "$", "");
		//str = StringUtils.replace(str, "+", "");
		//edit 修改 特殊字符替换 end<-
		if (QueryParamUtil.sql_inj(str)) {
			// System.out.println("请注意,填入的参数可能存在SQL注入!");
			// throw new RuntimeException("请注意,填入的参数可能存在SQL注入!");
			return QueryParamUtil.escapeSqlInject(str);
		}
		return StringEscapeUtils.escapeSql(str);
	}

	/**
	 * Html脚本转义
	 * @param html
	 * @return
	 */
	public static String htmlEscape(String html){
		return HtmlUtils.htmlEscape(html);
	}
	
	/**
	 * Html脚本反转义
	 * @param escapeHtml
	 * @return
	 */
	public static String htmlUnescape(String escapeHtml){
		return HtmlUtils.htmlUnescape(escapeHtml);
	}
	
	/**javascript脚本转义
	 * @param javaScript
	 * @return
	 */
	public static String escapeJavascript(String javaScript){
		return StringEscapeUtils.escapeJavaScript(javaScript);
	}
	
	/**javascript脚本反转义
	 * @param escapeJavaScript
	 * @return
	 */
	public static String unescapeJavascript(String escapeJavaScript){
		return StringEscapeUtils.unescapeJavaScript(escapeJavaScript);
	}
	
	/**把字符串转为unicode编码
	 * @param string
	 * @return
	 */
	public static String escapeJava(String string){
		return StringEscapeUtils.escapeJava(string);
	}
	
	/**把unicode编码字符串反转义
	 * @param string
	 * @return
	 */
	public static String unescapeJava(String string){
		return StringEscapeUtils.unescapeJava(string);
	}
	
	/**xml脚本转义
	 * @param xml
	 * @return
	 */
	public static String escapeXml(String xml){
		return StringEscapeUtils.escapeXml(xml);
	}
	
	/**xml脚本反转义
	 * @param xml
	 * @return
	 */
	public static String unescapeXml(String xml){
		return StringEscapeUtils.unescapeXml(xml);
	}
}
