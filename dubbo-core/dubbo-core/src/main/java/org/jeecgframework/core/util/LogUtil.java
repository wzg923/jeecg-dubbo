package org.jeecgframework.core.util;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

/**
 * 
 * @author 张代浩
 *
 */
public class LogUtil {

	// private static BaseLogUtil objLog = new BaseLogUtil();
	private static final String LOGCONFIG = "log4j.properties";
	private static Logger objLog;
	// 是否开启Redis记录日志功能
	// private static final boolean
	// enableFlag=Boolean.valueOf(ResourceUtil.getConfigByName("log.redis.enable"));//不开启
	// 日志信息 主键id key
	public static final String logId = "log:id";
	// 日志信息 主键id KEY List
	public static final String logIdList = "log:id:list";
	// 日志信息 key
	public static final String logKey = "log:id:";
	// 日志信息 日志类型索引 key
	public static final String logTypeKey = "log:logType:";
	// 日志信息 用户名索引 key
	public static final String logUsernameKey = "log:username:";
	// 日志信息 日志日期索引 key
	public static final String logDateKey = "log:date:";
	// 日志 功能模块索引 key
	public static final String logModuleKey = "log:module:";
	// 日志 级别 状态 key
	public static final String logLevelKey = "log:level:";

	// 日志信息 访问日志、操作日志 主键id key
	public static final String opLogId = "oplog:id";
	// 日志信息 访问日志、操作日志 主键id key list
	public static final String opLogIdList = "oplog:id:list";
	// 操作日志信息 key
	public static final String oplogKey = "oplog:id:";
	// 操作日志信息 日志类型索引 key
	public static final String oplogTypeKey = "oplog:logType:";
	// 操作日志信息 用户名索引 key
	public static final String oplogUsernameKey = "oplog:username:";
	// 操作日志信息 日志日期索引 key
	public static final String oplogDateKey = "oplog:date:";
	// 访问url
	public static final String oplogAddress = "oplog:address:";
	// 访问页面
	public static final String oplogAddressName = "oplog:address:name:";

	// @Autowired
	// private static SentinelJedisUtil sentinelJedisUtil;//Redis 连接类
	// private static RedisClientTemplate redisClientTemplate = new
	// RedisClientTemplate();
	// private static RedisClientTemplate redisClientTemplate;

	// public static void setRedisClientTemplate(RedisClientTemplate
	// redisClientTemplate) {
	// LogUtil.redisClientTemplate = redisClientTemplate;
	// }

	private static Logger getLogger() {
		if (objLog == null) {
			// DOMConfigurator.configure(getConfigFile());
			objLog = LogManager.getLogger(LogUtil.class);
		}
		return objLog;
	}

	private static String getConfigFile() {
		String s = LogUtil.class.getClassLoader().getResource("").toString();
		String filePath = s + LOGCONFIG;
		return filePath;
	}


	/**
	 * @Title: info
	 * @Description: 记录信息，包含 业务模块
	 * @param @param
	 *            logType
	 * @param @param
	 *            logModule
	 * @param @param
	 *            message
	 * @param @param
	 *            exception 设定文件
	 * @return void 返回类型
	 */
	public static void info(String message, Throwable exception) {
		try {
			log("INFO", message, exception);
		} catch (Exception ex) {

		}
	}

	/**
	 * @Title: info
	 * @Description: 记录信息，包含 业务模块
	 * @param @param
	 *            logType
	 * @param @param
	 *            logModule
	 * @param @param
	 *            message 设定文件
	 * @return void 返回类型
	 */
	public static void info(String message) {
		try {
			log("INFO", message, null);
		} catch (Exception ex) {

		}
	}

	// /
	// / 记录信息
	// /
	// / 信息内容
	public static void info(Object message) {
		log("INFO", message);
	}
	// end Info

	// Start Error
	public static void trace(String message) {
		try {
			log("TRACE", message);
		} catch (Exception ex) {
		}
	}

	/**
	 * @Title: trace
	 * @Description: 异常跟踪
	 * @param logType
	 * @param message
	 * @param exception
	 *            设定文件
	 * @return void 返回类型
	 */
	public static void trace( String message, Throwable exception) {
		try {
			log("TRACE", message, exception);
		} catch (Exception ex) {
		}
	}



	// /
	// /
	// /
	// / 信息内容
	// / 异常对象
	/**
	 * @Title: error
	 * @Description: 记录一个错误信息
	 * @param logType
	 * @param message
	 * @param exception
	 *            设定文件
	 * @return void 返回类型
	 */
	public static void error(String message, Exception exception) {
		try {
			log("ERROR", message, exception);
		} catch (Exception ex) {

		}
	}
	
	/**
	* @Title: error
	* @author: 王振广
	* @Description: 记录一个错误信息
	* @param  message
	* @param  cause  
	* @return void    返回类型
	*/ 
	public static void error(String message, Throwable cause) {
		try {
			log("ERROR", message, new Exception(cause));
		} catch (Exception ex) {

		}
	}

	// /
	// / 记录一个错误信息
	// /
	// / 信息内容
	public static void error(String message) {
		try {
			log("ERROR", message);
		} catch (Exception ex) {

		}
	}

	// end Error

	// Start Warning

	// /
	// / 记录一个警告信息
	// /
	// / 信息内容
	// / 异常对象

	public static void warning(String message, Exception exception) {
		try {
			log("WARN", message, exception);
		} catch (Exception ex) {

		}
	}

	// /
	// / 记录一个警告信息
	// /
	// / 信息内容
	public static void warning(String message) {
		try {
			log("WARN", message);
		} catch (Exception ex) {

		}
	}

	// end Warning

	// Start Fatal

	// /
	// / 记录一个程序致命性错误
	// /
	// / 信息内容
	// / 异常对象

	public static void fatal(String message, Exception exception) {
		try {
			log("FATAL", message, exception );
		} catch (Exception ex) {

		}
	}

	// /
	// / 记录一个程序致命性错误
	// /
	// / 信息内容
	public static void fatal(String message) {
		try {
			log("FATAL", message);
		} catch (Exception ex) {

		}
	}

	// end Fatal

	// Start Debug

	// /
	// / 记录调试信息
	// /
	// / 信息内容
	// / 异常对象
	public static void debug(String message, Exception exception) {
		try {
			log("DEBUG", message, exception);
		} catch (Exception ex) {

		}
	}


	// /
	// / 记录调试信息
	// /
	// / 信息内容
	public static void debug(String message) {
		try {
			log("DEBUG", message);
		} catch (Exception ex) {

		}
	}
	// end Debug

	public static void log(String level, Object msg) {
		log(level, msg, null);
	}

	public static void log(String level, Throwable e) {
		log(level, null, e);
	}

	public static void log(final String level, Object msg, Throwable e) {
		try {
			StringBuilder sb = new StringBuilder();
			Throwable t = new Throwable();
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			t.printStackTrace(pw);
			String input = sw.getBuffer().toString();
			StringReader sr = new StringReader(input);
			BufferedReader br = new BufferedReader(sr);
			for (int i = 0; i < 4; i++)
				br.readLine();
			String line = br.readLine();
			// at
			// com.media.web.action.DicManageAction.list(DicManageAction.java:89)
			int paren = line.indexOf("at ");
			line = line.substring(paren + 3);
			paren = line.indexOf('(');
			String invokeInfo = line.substring(0, paren);
			int period = invokeInfo.lastIndexOf('.');
			//add 增加时间戳打印 start
			sb.append('[').append(DateUtils.gettimestamp()).append(']');
			//add 增加时间戳打印  end
			sb.append('[');
			sb.append(invokeInfo.substring(0, period));
			sb.append(':');
			sb.append(invokeInfo.substring(period + 1));
			sb.append("():");
			paren = line.indexOf(':');
			period = line.lastIndexOf(')');
			sb.append(line.substring(paren + 1, period));
			sb.append(']');
			
			sb.append(msg);

			getLogger().log((Priority) Level.toLevel(level), sb.toString(), e);


		} catch (Exception ex) {
			// org.jeecgframework.core.util.LogUtil.info(ex.getLocalizedMessage());
			getLogger().error(ex);// 控制台打印
		}
	}



}
