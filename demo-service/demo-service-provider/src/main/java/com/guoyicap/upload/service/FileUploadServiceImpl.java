package com.guoyicap.upload.service;

import java.util.HashMap;
import java.util.Map;

import org.jeecgframework.core.util.FileUtils;
import org.jeecgframework.core.util.LogUtil;

import com.alibaba.dubbo.config.annotation.Service;
import com.anniweiya.fastdfs.FastDfsInfo;
import com.anniweiya.fastdfs.exception.FastDFSException;
import com.guoyicap.fastdfs.FileManager;
import com.guoyicap.util.MyException;


@Service(protocol = { "rmi" }, group = "guoyicap", version = "1.0.0", validation = "true", loadbalance = "random", timeout = 60000, connections=10)
public class FileUploadServiceImpl implements FileUploadService {

	@Override
	public FastDfsInfo upload(String localFileName) {
		Map<String, String> metaMap=new HashMap<String,String>();
		metaMap.put("author", "liangdongqiche");
		FastDfsInfo fastDfsInfo=null;
		try {
			fastDfsInfo = FileManager.upload(localFileName,FileUtils.getExtend(localFileName), metaMap);
		} catch (FastDFSException e) {
			LogUtil.error("文件上传失败",e);
			throw new MyException("文件上传失败",e);
		}
		return fastDfsInfo;
	}

	@Override
	public FastDfsInfo upload(byte[] data, String ext) {
		Map<String, String> metaMap=new HashMap<String,String>();
		metaMap.put("author", "liangdongqiche");
		FastDfsInfo fastDfsInfo=null;
		try {
			fastDfsInfo = FileManager.upload(data,ext, metaMap);
		} catch (FastDFSException e) {
			LogUtil.error("文件上传失败",e);
			throw new MyException("文件上传失败",e);
		}
		return fastDfsInfo;
	}

	@Override
	public byte[] download(String groupName, String remoteFileName) {
		try {
			return FileManager.downloadFile(groupName, remoteFileName);
		} catch (FastDFSException e) {
			LogUtil.error("文件下载失败",e);
			throw new MyException("文件下载失败",e);
		}
	}
	
	

	
}
