package com.guoyicap.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.jeecgframework.core.util.StringUtil;

/**
 * @describe xml相关的工具类 * 
 * @author 王振广
 * @date 2015-12-09
 * @version v1.0
 */
@SuppressWarnings("unchecked")
public class XmlUtil {
	
	/**
	* xml字符串转换成bean对象
	* 
	* @param xmlStr xml字符串
	* @param clazz 待转换的class
	* @return 转换后的对象
	*/
	public static Object xmlStrToBean(String xmlStr, Class clazz) {
		Object obj = null;
		try {
			// 将xml格式的数据转换成Map对象
			Map<String, Object> map = xmlStrToMap(xmlStr);
			//将map对象的数据转换成Bean对象
			obj = mapToBean(map, clazz);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return obj;
	}
	
	/**
	* 将xml格式的字符串转换成Map对象
	* 
	* @param xmlStr xml格式的字符串
	* @return Map对象
	* @throws Exception 异常
	*/
	public static Map<String, Object> xmlStrToMap(String xmlStr) throws Exception {
		if(StringUtil.isEmpty(xmlStr)) {
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		//将xml格式的字符串转换成Document对象
		Document doc = DocumentHelper.parseText(xmlStr);
		//获取根节点
		Element root = doc.getRootElement();
		//获取根节点下的所有元素
		List children = root.elements();
		//循环所有子元素
		if(children != null && children.size() > 0) {
			for(int i = 0; i < children.size(); i++) {
				Element child = (Element)children.get(i);
				
				map.put(child.getName(), getElementValue(child));
				
			}
		}else if("string".equals(root.getName())){
			String xmlStr2=root.getTextTrim();
			map=xmlStrToMap(xmlStr2);
		}
		else{
			map.put(root.getName(), root.getTextTrim());
		}
		return map;
	}
	
	/**  
	* @Title: getElementValue  
	* @Author:王振广
	* @Description: 递归解析 Element
	* @param element
	* @return    设定文件  
	* @return Object    返回类型  
	* @throws  
	*/
	public static Object getElementValue(Element element){
		
		if(element.hasContent() && element.isTextOnly()){
			return element.getTextTrim();
		}else if(element.hasContent() && !element.isTextOnly()){
			Map data=new HashMap();
			for (Object object :element.content()) {
				Element node=(Element)object;
				data.put(node.getName(), XmlUtil.getElementValue(node));
			}
			return data;
		}
		return null;
	}
	
	/**
	* 将Map对象通过反射机制转换成Bean对象
	* 
	* @param map 存放数据的map对象
	* @param clazz 待转换的class
	* @return 转换后的Bean对象
	* @throws Exception 异常
	*/
	public static Object mapToBean(Map<String, Object> map, Class clazz) throws Exception {
		Object obj = clazz.newInstance();
		if(map != null && map.size() > 0) {
			for(Map.Entry<String, Object> entry : map.entrySet()) {
				String propertyName = entry.getKey();
				Object value = entry.getValue();
				String setMethodName = "set"
						+ propertyName.substring(0, 1).toUpperCase()
						+ propertyName.substring(1);
				Field field = getClassField(clazz, propertyName);
				Class fieldTypeClass = field.getType();
				value = convertValType(value, fieldTypeClass);
				clazz.getMethod(setMethodName, field.getType()).invoke(obj, value);
			}
		}
		return obj;
	}
	
	/**
	* 将Object类型的值，转换成bean对象属性里对应的类型值
	* 
	* @param value Object对象值
	* @param fieldTypeClass 属性的类型
	* @return 转换后的值
	*/
	private static Object convertValType(Object value, Class fieldTypeClass) {
		Object retVal = null;
		if(Long.class.getName().equals(fieldTypeClass.getName())
				|| long.class.getName().equals(fieldTypeClass.getName())) {
			retVal = Long.parseLong(value.toString());
		} else if(Integer.class.getName().equals(fieldTypeClass.getName())
				|| int.class.getName().equals(fieldTypeClass.getName())) {
			retVal = Integer.parseInt(value.toString());
		} else if(Float.class.getName().equals(fieldTypeClass.getName())
				|| float.class.getName().equals(fieldTypeClass.getName())) {
			retVal = Float.parseFloat(value.toString());
		} else if(Double.class.getName().equals(fieldTypeClass.getName())
				|| double.class.getName().equals(fieldTypeClass.getName())) {
			retVal = Double.parseDouble(value.toString());
		} else {
			retVal = value;
		}
		return retVal;
	}

	/**
	* 获取指定字段名称查找在class中的对应的Field对象(包括查找父类)
	* 
	* @param clazz 指定的class
	* @param fieldName 字段名称
	* @return Field对象
	*/
	private static Field getClassField(Class clazz, String fieldName) {
		if( Object.class.getName().equals(clazz.getName())) {
			return null;
		}
		Field []declaredFields = clazz.getDeclaredFields();
		for (Field field : declaredFields) {
			if (field.getName().equals(fieldName)) {
				return field;
			}
		}

		Class superClass = clazz.getSuperclass();
		if(superClass != null) {// 简单的递归一下
			return getClassField(superClass, fieldName);
		}
		return null;
	} 
	
	/**  
	* @Title: SoapXmlStrToMap  
	* @Author:王振广
	* @Description: SOAP WSDL接口返回内容解析，将DocumentElement 中的内容解析为List<Map>
	* @param xmlStr
	* @return
	* @throws Exception    设定文件  
	* @return List<Map>    返回类型  
	* @throws  
	*/
	public static List<Map<String,Object>> SoapXmlStrToMap(String xmlStr) throws Exception{
		List<Map<String,Object>> data=new ArrayList<Map<String,Object>>();
		//将xml格式的字符串转换成Document对象
		//Document doc = DocumentHelper.parseText(strBuf.toString());
		Document doc = DocumentHelper.parseText(xmlStr);
		//获取根节点
		Element root = doc.getRootElement();
		//DocmentElement MessageElement
		Element elm1 =(Element) root.elements().get(1);
		
		//遍历DocumentElement子节点
		Iterator iter = elm1.elementIterator("DocumentElement");
		
		if(iter.hasNext()) {
			//获取根节点下的子节点DocumentElement
			Element recordEle = (Element) iter.next();
			//获取DocumentElement节点下所有子节点
			List children = recordEle.elements();
			//遍历子节点（dzyzidongtibiao）
			if(children != null && children.size() > 0) {
				for(int i = 0; i < children.size(); i++) {
					//获取对应下标位置的dzyzidongtibiao子节点
					Element child = (Element)children.get(i);
					//获取dzyzidongtibiao下所有子节点
					List childrens = child.elements();
					Map<String, Object> map = new LinkedHashMap<String, Object>();
					for(int j = 0; j < childrens.size(); j++) {
						//遍历子节点并转为map进行存储
						Element childs = (Element)childrens.get(j);
						map.put(childs.getName(), childs.getTextTrim());
					}
					
					//System.out.println(map);
					data.add(map);
				}
			}
		}
		
		
		return data;
		
	}
	
}


