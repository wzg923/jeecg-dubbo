package com.guoyicap.fastdfs;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.csource.fastdfs.FileInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anniweiya.fastdfs.FastDFSTemplate;
import com.anniweiya.fastdfs.FastDfsInfo;
import com.anniweiya.fastdfs.exception.FastDFSException;

@Component
public class FileManager implements Serializable{
	 private static final long serialVersionUID = 1L;  
	  
	    private static Logger logger  = Logger.getLogger(FileManager.class);  
	    
	    @Autowired
	    private FastDFSTemplate fastDFSTemplate;
	    
	    private static FastDFSTemplate fastDFSTemplateInstance;
	    
	    public FastDFSTemplate getFastDFSTemplate() {
			return fastDFSTemplate;
		}

		public void setFastDFSTemplate(FastDFSTemplate fastDFSTemplate) {
			this.fastDFSTemplate = fastDFSTemplate;
		}
		
		@PostConstruct
		public void init (){
			fastDFSTemplateInstance=this.fastDFSTemplate;
		}
	      
	public static String upload(FastDFSFile file) throws FastDFSException {
		logger.info("File Name: " + file.getName() + "     File Length: " + file.getContent().length);

		Map<String, String> metaList = new HashMap<String, String>();
		if (file.getWidth() != null && !"".equals(file.getWidth())) {
			metaList.put("width", file.getWidth());
		}
		if (file.getHeight() != null && !"".equals(file.getHeight())) {
			metaList.put("heigth", file.getHeight());
		}
		if (file.getAuthor() != null && !"".equals(file.getAuthor())) {
			metaList.put("author", file.getAuthor());
		}
		long startTime = System.currentTimeMillis();
		FastDfsInfo uploadResults = null;
		try {
			uploadResults = fastDFSTemplateInstance.upload(file.getContent(), file.getExt(), metaList);
		} catch (FastDFSException e) {
			logger.error("IO Exception when uploadind the file: " + file.getName(), e);
			throw e;
		}
		logger.info("upload_file time used: " + (System.currentTimeMillis() - startTime) + " ms");

		String groupName = uploadResults.getGroup();
		String remoteFileName = uploadResults.getPath();
		String fileAbsolutePath = uploadResults.getFileAbsolutePath();

		logger.info("upload file successfully!!!  " + "group_name: " + groupName + ", remoteFileName:" + " "
				+ remoteFileName);

		return fileAbsolutePath;

	}
	    
	    /**  
	    * @Title: upload  
	    * @Author:王振广
	    * @Description: 上传文件 
	    * @param fileName  本地文件名
	    * @param ext 扩展名（可为空）
	    * @param metaList 其他参数（可为空）
	    * @return
	    * @throws FastDFSException    设定文件  
	    * @return FastDfsInfo    返回类型  
	    * @throws  
	    */
	    public static FastDfsInfo upload(String fileName,String ext,Map<String, String> metaList) throws FastDFSException{  
	        logger.info("File Name: " + fileName );  
	          
	        //Map<String, String> metaList=new HashMap<String,String>(); 
	        //metaList.put("width", file.getWidth()); 
	        //metaList.put("heigth", file.getHeight());  
	        //metaList.put("author", file.getAuthor());
	        long startTime = System.currentTimeMillis();  
	        FastDfsInfo uploadResults = null;  
	        try {
	            uploadResults = fastDFSTemplateInstance.upload(fileName,ext,metaList);  
	        } catch (FastDFSException e) {  
	            logger.error("IO Exception when uploadind the file: " + fileName, e);  
	            throw e;
	        }  
	        logger.info("upload_file time used: " + (System.currentTimeMillis() - startTime) + " ms");  
	          
	        String groupName        = uploadResults.getGroup();  
	        String remoteFileName   = uploadResults.getPath();  
	        String fileAbsolutePath = uploadResults.getFileAbsolutePath(); 
	          
	        logger.info( "upload file successfully!!!  " +"group_name: " + groupName + ", remoteFileName:"  
	                + " " + remoteFileName);  
	          
	        return uploadResults;  
	          
	    }
	    
	    
	    /**  
	    * @Title: upload  
	    * @Author:王振广
	    * @Description: 上传二进制文件 
	    * @param data  文件数据
	    * @param ext 扩展名
	    * @param values 属性
	    * @return 
	    * @throws FastDFSException    设定文件  
	    * @return FastDfsInfo    返回类型  
	    * @throws  
	    */
	    public static FastDfsInfo upload(byte[] data,String ext,Map<String, String> values) throws FastDFSException{  
	        long startTime = System.currentTimeMillis();  
	        FastDfsInfo uploadResults = null;  
	        try {
	            uploadResults = fastDFSTemplateInstance.upload(data, ext, values);  
	        } catch (FastDFSException e) {  
	            logger.error("IO Exception when uploadind the file", e);  
	            throw e;
	        }  
	        logger.info("upload_file time used: " + (System.currentTimeMillis() - startTime) + " ms");  
	          
	        String groupName        = uploadResults.getGroup();  
	        String remoteFileName   = uploadResults.getPath();  
	        String fileAbsolutePath = uploadResults.getFileAbsolutePath(); 
	          
	        logger.info( "upload file successfully!!!  " +"group_name: " + groupName + ", remoteFileName:"  
	                + " " + remoteFileName);  
	        return uploadResults;  
	          
	    }
	      
	    public static FileInfo getFile(String groupName, String remoteFileName) throws FastDFSException{  
	        try {  
	            return fastDFSTemplateInstance.getFileInfo(groupName, remoteFileName);  
	        } catch (FastDFSException e) {  
	            logger.error("FastDFSException: Get File from Fast DFS failed", e);  
	            throw e;
	        } 
	    }  
	      
	    public static void deleteFile(String groupName, String remoteFileName) throws FastDFSException {  
	    	fastDFSTemplateInstance.deleteFileInfo(groupName, remoteFileName);  
	    }  
	    
	    public static byte[] downloadFile(String groupName,String remoteFileName) throws FastDFSException{
	    	return fastDFSTemplateInstance.loadFile(groupName, remoteFileName);
	    }
	      
}
