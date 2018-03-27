package com.guoyicap.util;

import java.beans.PropertyDescriptor;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil;
import org.jeecgframework.core.util.DateUtils;
import org.jeecgframework.core.util.LogUtil;

import com.guoyicap.core.page.BaseModel;

/**
 * 自定义HqlGenerateUtil，继承框架的Util工具类，增加自定义方法
 * 主要用来封装request parameterMap 到Bean
 * @author guang
 *
 */
public class FormUtil extends HqlGenerateUtil {
	private static final String BEGIN2="Begin";
	private static final String END2="End";

	/**
	  * 解析request.getParameterMap() 获得Map
	  * @param request
	  * @return Map
	  */
	 public static Map getParameterMap(HttpServletRequest request) {
	     // 参数Map
	     Map properties = request.getParameterMap();
	     // 返回值Map
	     Map returnMap = new HashMap();
	     Iterator entries = properties.entrySet().iterator();
	     Map.Entry entry;
	     String name = "";
	     String value = "";
	     while (entries.hasNext()) {
	         entry = (Map.Entry) entries.next();
	         name = (String) entry.getKey();
	         Object valueObj = entry.getValue();
	         if(null == valueObj){
	             value = "";
	         }else if(valueObj instanceof String[]){
	             String[] values = (String[])valueObj;
	             for(int i=0;i<values.length;i++){
	                 value = values[i] + ",";
	             }
	             value = value.substring(0, value.length()-1);
	         }else{
	             value = valueObj.toString();
	         }
	         returnMap.put(name, value);
	     }
	     return returnMap;
	 }
	 
	 /**
	  * 将request的parameterMap（）封装到目标Bean对象
	 * @param searchObj
	 * @param request
	 * @param dateFormat 日期格式化对象
	 */
	public static void installRequestMap2Bean(Object searchObj,HttpServletRequest request,String dateFormat){
		//从request中获取parameterMap，转换为普通HashMap
		Map requestMap=getParameterMap(request);
		//将Map封装到Bean
		installBean(searchObj,requestMap,dateFormat);
	 
	}
	
	/**
	  * 将request的parameterMap（）封装到目标Bean对象
	 * @param model   BaseModel
	 * @param request
	 * @param dateFormat 日期格式化对象
	 */
	public static void installRequestMap2Model(DataGrid dataGrid,BaseModel model,HttpServletRequest request,String dateFormat){
		//从request中获取parameterMap，转换为普通HashMap
		Map requestMap=getParameterMap(request);
		//将Map封装到Bean
		installBean(model,requestMap,dateFormat);
		// 当前页数
		int page = dataGrid.getPage();
		// 每页显示记录数
		int rows = dataGrid.getRows();
		model.setPage(page);
		model.setRows(rows);	 
	}
	 
	 /**
		 * 
		 * @author guang
		 * @date 2015年9月10日		  
		 * @param searchObj 要封装的目标Bean对象
		 * @param parameterMap
		 * @param dateFormat 日期格式化对象
		 */
		private static void installBean(Object dest,
				Map<String, String> parameterMap,String dateFormat) {
			//searchObj property集合
			PropertyDescriptor destDescriptors[] = PropertyUtils
					.getPropertyDescriptors(dest);
			//String aliasName;//別名name
			String name;//变量名
			String type;//变量类型
			
			
			for (int i = 0; i < destDescriptors.length; i++) {
				//如果别名alias非空，则在变量名前追加alias别名.  [alias].[name]，否则 [name]
				/*aliasName = (alias.equals("") ? "" : alias + ".")
						+ origDescriptors[i].getName();*/
				name = destDescriptors[i].getName();//变量名
				type = destDescriptors[i].getPropertyType().toString();//变量类型
				try {
					if (judgedIsUselessField(name)//判断是否是保留关键变量，过滤掉保留关键参数
							|| !PropertyUtils.isReadable(dest, name)) {
						continue;
					}
					/*// 如果规则包含这个属性
					if (ruleMap.containsKey(aliasName)) {
						addRuleToCriteria(ruleMap.get(aliasName), aliasName,
								origDescriptors[i].getPropertyType(), cq);
					}*/

					// 添加 判断是否有区间值
					String beginValue = null;
					String endValue = null;
					String beginKey=null;
					String endKey=null;
					if (parameterMap != null
							&& parameterMap.containsKey(name + BEGIN)) {
						beginValue = parameterMap.get(name + BEGIN).trim();//开始值value
						beginKey=name+BEGIN2;//beginKey [name]+[Begin]
						//parameterMap.put(beginKey, beginValue);//key去掉_，重新set值
						//parameterMap.remove(name + BEGIN);//remove old
					}
					if (parameterMap != null
							&& parameterMap.containsKey(name + END)) {
						endValue = parameterMap.get(name + END).trim();//结束值value
						endKey=name+END2;//endKey [name]+[End]						
						//parameterMap.put(endKey, endValue);//key去掉_，重新set值
						//parameterMap.remove(name + END);//remove old
					}
					//Object value = PropertyUtils.getSimpleProperty(searchObj, name);					
					
					Object value=null;//map 的key[name]对应的值value
					if (parameterMap != null
							&& parameterMap.containsKey(name)) {
						value=parameterMap.get(name).trim();//map value
					}
					if(value !=null){
						setProperty(dest,name,value,dateFormat);//值set
					}
					if(beginValue !=null){
						setProperty(dest,beginKey,beginValue,dateFormat);//区间值begin set
					}
					if(endValue !=null){
						setProperty(dest,endKey,endValue,dateFormat);//区间值end set
					}
					

				
				} catch (Exception e) {
					LogUtil.trace("request获取参数Map重新装填失败!", e);
				}
			}
		}
		
		/**
		 * Bean setProperty值
		 * @param dest 目标Bean
		 * @param name 变量名
		 * @param value 变量值
		 * @param dataFormat 日期格式化样式 形如yyyy-MM-dd,参考DataUtils静态变量
		 */
		public static void setProperty(Object dest,String name,Object value,String dataFormat){
			try{
				if (value != null) {
					// dest 目标对象 对应属性的数据类型
					Class clazz = PropertyUtils.getPropertyType(dest, name);
					if (null == clazz) {
						return;
					}
					// 数据类型 className
					String className = clazz.getName();
					// 不处理
					if (className.equalsIgnoreCase("java.sql.Timestamp")) {
						if (value == null || value.equals("")) {
							return;
						} else {
							value=DateUtils.getTimestamp(DateUtils.str2Date(value.toString(), dataFormat).getTime());
							//value = DateUtils.str2Timestamp(value.toString());
						}
					}
					// String类型
					if (className.equalsIgnoreCase("java.lang.String")) {
						if (value == null) {
							value = null;
						}
					}
					if (className.equalsIgnoreCase("java.lang.Long")) {
						if (value == null) {
							value = null;
						} else {
							value = Long.valueOf(value.toString());
						}
					}
					if (className.equalsIgnoreCase("java.lang.Double")) {
						if (value == null) {
							value = null;
						} else {
							value = Double.valueOf(value.toString());
						}
					}
					if (className.equalsIgnoreCase("java.lang.Float")) {
						if (value == null) {
							value = null;
						} else {
							value = Float.valueOf(value.toString());
						}
					}
					if (className.equalsIgnoreCase("java.lang.Integer")) {
						if (value == null) {
							value = null;
						} else {
							value = Integer.valueOf(value.toString());
						}
					}
					if (className.equalsIgnoreCase("java.math.BigDecimal")) {
						if (value == null) {
							value = null;
						} else {
							value = BigDecimal.valueOf(Double.valueOf(value.toString()));
						}
					}
					if (className.equalsIgnoreCase("java.util.Date")) {
						if (value == null) {
							value = null;
						} else {
							try{
							
								value = DateUtils.str2Date(value.toString(), dataFormat);
							}catch(Exception e1){								
								LogUtil.trace("日期格式化失败", e1);
							}
						}
					}
					PropertyUtils.setSimpleProperty(dest, name, value);

				}
			}catch(Exception e){
				LogUtil.trace("setProperty出错", e);
			}
			
		}
		
		
		/**
		 * 判断是否是无用参数字段，保留字段如class/ids/page/rows/sort/order
		 * 修改 王振广 2015.9.10 private 修饰符改为protected
		 * @param name
		 * @return
		 */
		protected static boolean judgedIsUselessField(String name) {
			return "class".equals(name) || "ids".equals(name)
					|| "page".equals(name) || "rows".equals(name)
					|| "sort".equals(name) || "order".equals(name);
		}
		

}
