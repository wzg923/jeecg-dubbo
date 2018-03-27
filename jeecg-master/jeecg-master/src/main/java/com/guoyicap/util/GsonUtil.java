package com.guoyicap.util;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import com.guoyicap.core.page.DateDeserializer;
import com.guoyicap.core.page.DateSerializer;

/**  
* @ClassName: GsonUtil  
* @Description: JSON转换工具类  
* @author A18ccms a18ccms_gmail_com  
* @date 2017年1月13日 下午3:02:09  
*    
*/
public class GsonUtil {
	
	/**
	 * @param String pattern 日期格式化format，"yyyy-MM-dd HH:mm:ss",若为空，则默认DateFormat.LONG
	 * 获取Gson 对象，
	 * @return
	 */
	public static Gson create(String pattern) {
        GsonBuilder gb = new GsonBuilder();
        if(pattern != null && !"".equals(pattern)){
        	gb.setDateFormat(pattern);
        }else{
        	gb.registerTypeAdapter(java.util.Date.class, new DateSerializer()).setDateFormat(DateFormat.LONG);
            gb.registerTypeAdapter(java.util.Date.class, new DateDeserializer()).setDateFormat(DateFormat.LONG);
        }
        gb.registerTypeAdapter(Double.class, new JsonSerializer<Double>() {  
        	  
            @Override  
            public JsonElement serialize(Double src, Type typeOfSrc, JsonSerializationContext context) {  
                if (src == src.longValue())  
                    return new JsonPrimitive(src.longValue());  
                return new JsonPrimitive(src);  
            }  
        });
        Gson gson = gb.create();
        return gson;
    }
	
	/**  
	* @Title: jsonToBean  
	* @Author:王振广
	* @Description: json转换为Bean
	* @param json
	* @param clazz
	* @return T    返回类型  
	* @throws  
	*/
	public static <T> T jsonToBean(String json,Class<T> clazz){
		Gson gson=GsonUtil.create(null);
		return gson.fromJson(json, clazz);
	}
	
	/**  
	* @Title: jsonToBean  
	* @Author:王振广
	* @Description: json转换为Bean
	* @param json
	* @param clazz
	* @param pattern 日期格式化
	* @return T    返回类型  
	* @throws  
	*/
	public static <T> T jsonToBean(String json,Class<T> clazz,String pattern){
		Gson gson=GsonUtil.create(pattern);
		return gson.fromJson(json, clazz);
	}

    /**  
    * @Title: jsonToList  
    * @Author:王振广
    * @Description: json转换为List<T>  
    * @param json
    * @param clazz
    * @return List<T>    返回类型  
    * @throws  
    */
    public static <T> List<T> jsonToList(String json, Class<T[]> clazz)
    {
    	Gson gson=GsonUtil.create(null);
        T[] array = gson.fromJson(json, clazz);
        return Arrays.asList(array);
    }
    
    /**  
     * @Title: jsonToList  
     * @Author:王振广
     * @Description: json转换为List<T>  
     * @param json
     * @param clazz
     * @param pattern 日期格式化
     * @return List<T>    返回类型  
     * @throws  
     */
    public static <T> List<T> jsonToList(String json, Class<T[]> clazz,String pattern)
    {
    	Gson gson=GsonUtil.create(pattern);
        T[] array = gson.fromJson(json, clazz);
        return Arrays.asList(array);
    }

    /**  
    * @Title: jsonToArrayList  
    * @Author:王振广
    * @Description: json转换为ArrayList<T>   
    * @param json
    * @param clazz
    * @return ArrayList<T>    返回类型  
    * @throws  
    */
    public static <T> ArrayList<T> jsonToArrayList(String json, Class<T> clazz)
    {
    	Gson gson=GsonUtil.create(null);
        Type type = new TypeToken<ArrayList<JsonObject>>(){}.getType();
        ArrayList<JsonObject> jsonObjects = gson.fromJson(json, type);

        ArrayList<T> arrayList = new ArrayList<>();
        for (JsonObject jsonObject : jsonObjects)
        {
            arrayList.add(gson.fromJson(jsonObject, clazz));
        }
        return arrayList;
    }
    
    /**  
     * @Title: jsonToArrayList  
     * @Author:王振广
     * @Description: json转换为ArrayList<T>   
     * @param json
     * @param clazz
     * @param pattern 日期格式化
     * @return ArrayList<T>    返回类型  
     * @throws  
     */
    public static <T> ArrayList<T> jsonToArrayList(String json, Class<T> clazz,String pattern)
    {
    	Gson gson=GsonUtil.create(pattern);
        Type type = new TypeToken<ArrayList<JsonObject>>(){}.getType();
        ArrayList<JsonObject> jsonObjects = gson.fromJson(json, type);

        ArrayList<T> arrayList = new ArrayList<>();
        for (JsonObject jsonObject : jsonObjects)
        {
            arrayList.add(gson.fromJson(jsonObject, clazz));
        }
        return arrayList;
    }
}
