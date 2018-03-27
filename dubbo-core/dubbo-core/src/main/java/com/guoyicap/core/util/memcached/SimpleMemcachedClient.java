package com.guoyicap.core.util.memcached;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.jeecgframework.core.util.LogUtil;

import com.guoyicap.core.enums.LogTypeEnum;

/**
 * SimpleMemcachedClient
 * 配置连接memcached服务器，获取socketIOPool实例
 * @author 王振广
 *
 */
public class SimpleMemcachedClient {
	 /*protected static MemcachedClient memcachedClient = new MemcachedClient();
	 protected static SockIOPool pool = null;
	 private static String poolname=null;//自定义socketIOPool实例名称 ,默认default
	 private static final String propertiesFile="memCached.properties";//配置文件路径 
	 private static final String SERVERS="memCached.servers";
	 private static final String WEIGHTS="memCached.weights";
	 static {
	    	//读取配置文件
	    	Properties properties=new Properties();
	    	File file=new File(propertiesFile);
	    	InputStream fileInputStream;
			try {
				fileInputStream = new FileInputStream(file);
				properties.load(fileInputStream);
			} catch (FileNotFoundException e) {
				LogUtil.error(LogTypeEnum.Log_Type_OTHER,"memCached.properties文件不存在",e);
			} catch (IOException e) {
				LogUtil.error(LogTypeEnum.Log_Type_OTHER,"memCached.properties文件读取失败",e);
			}
			//服务器列表
			String serversParam=properties.getProperty(SERVERS);
			String[] servers = { serversParam };//服务器数组
			//服务器权值
			String weightsParam=properties.getProperty(WEIGHTS);
			String[] weightsArray=weightsParam.split(",");
			Integer[] weights=new Integer[weightsArray.length];//权值数组
			//将权值String 转换为Integer
			for(int i=0;i<weightsArray.length;i++){
				weights[i]=Integer.valueOf(weightsArray[i]);
			}      	        

	        SockIOPool pool = SockIOPool.getInstance();//获取实例
			pool.setServers( servers );//设置服务器列表
			pool.setWeights(weights);//设置服务器权值
			pool.setFailover( true );//表示对于服务器出现问题时的自动修复。
			pool.setInitConn( 10 ); //初始的时候连接数，
			pool.setMinConn( 5 );//表示最小闲置连接数，
			pool.setMaxConn( 250 );//最大连接数，
			pool.setMaintSleep( 30 );//表示是否需要延时结束
			pool.setNagle( false );//Nagle是TCP对于socket创建的算法，
			pool.setSocketTO( 3000 );//socketTO是socket连接超时时间，
			pool.setAliveCheck( true );//aliveCheck表示心跳检查，确定服务器的状态。
			pool.initialize();//初始化连接池。
	    }*/
}
