package org.jeecgframework.core.util;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.guoyicap.core.util.io.SerializeUtil;
/**
* @ClassName: ContextHolderUtils 
* @Description: TODO(上下文工具类) 
* @author  张代浩 
* @date 2012-12-15 下午11:27:39 
*
 */
public class ContextHolderUtils {
	/**
	 * SpringMvc下获取request
	 * 
	 * @return
	 */
	public static HttpServletRequest getRequest() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		return request;

	}
	/**
	 * SpringMvc下获取session
	 * 
	 * @return
	 */
	public static HttpSession getSession() {
		HttpSession session = getRequest().getSession();
		return session;

	}
	
	/**
	*Session 共享时调用，设置session中属性值
	*/
	public static void setAttribute(String name,Object object) {
		HttpSession session = getRequest().getSession();
		if(object instanceof String 
				|| object instanceof Integer
				|| object instanceof Long
				|| object instanceof Float
				|| object instanceof Double
				|| object instanceof BigDecimal
				|| object instanceof Character
				|| object instanceof List
				|| object instanceof Map
				|| object instanceof Set
				|| object instanceof Byte
				|| object instanceof Boolean
				|| object instanceof Date) {
			
			session.setAttribute(name, object);
			
		}else if(object.getClass().getName().equals("int")
				||object.getClass().getName().equals("double")
				||object.getClass().getName().equals("long")
				||object.getClass().getName().equals("short")
				||object.getClass().getName().equals("byte")
				||object.getClass().getName().equals("boolean")
				||object.getClass().getName().equals("char")
				||object.getClass().getName().equals("float")) {
			
			session.setAttribute(name, object);
			
		}else {
			byte[] objectByte=SerializeUtil.serialize(object);
			session.setAttribute(name, objectByte);
		}		
		
	}
	
	/**
	*Session 共享时调用，获取session中属性值
	*/
	public static Object getAttribute(String name) {
		HttpSession session = getRequest().getSession();
		Object object = session.getAttribute(name);		
		if(object instanceof String 
				|| object instanceof Integer
				|| object instanceof Long
				|| object instanceof Float
				|| object instanceof Double
				|| object instanceof BigDecimal
				|| object instanceof Character
				|| object instanceof List
				|| object instanceof Map
				|| object instanceof Set
				|| object instanceof Byte
				|| object instanceof Boolean
				|| object instanceof Date) {
			
			return object;
			
		}else if(object.getClass().getName().equals("int")
				||object.getClass().getName().equals("double")
				||object.getClass().getName().equals("long")
				||object.getClass().getName().equals("short")
				||object.getClass().getName().equals("byte")
				||object.getClass().getName().equals("boolean")
				||object.getClass().getName().equals("char")
				||object.getClass().getName().equals("float")) {
			
			return object;
			
		}else {
			return SerializeUtil.unserialize((byte[])object);
		}		
	}

}
