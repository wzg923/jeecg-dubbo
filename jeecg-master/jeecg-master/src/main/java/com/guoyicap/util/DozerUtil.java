package com.guoyicap.util;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

/**
 * Dozer 工具类
 * 与Spring集成，Bean注入 配置文件 Spring-mvc-context.xml,扩展时需修改要加载mapper文件路径
 * @author guang
 *
 */

public class DozerUtil {
	
	//MappingFiles 集合
	private static List myMappingFiles;
	//DozerBeanMapper 对象
	private static DozerBeanMapper mapper;
	/**
	 * 获得DozerBeanMapper 实例
	 * @return Mapper
	 */
	public static Mapper getMapper(){
		if(myMappingFiles==null){
			myMappingFiles=new ArrayList();	
			myMappingFiles.add("config/dozerBeanMapping.xml");//添加需要加载的配置文件
			//myMappingFiles.add("someOtherDozerBeanMappings.xml");
			
		}
		//实例化 mapper
		if(mapper==null){
			mapper = new DozerBeanMapper();
		}		
		 return mapper;
	}
	
	
}
