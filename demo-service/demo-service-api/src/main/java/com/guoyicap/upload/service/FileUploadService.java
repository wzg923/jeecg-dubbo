package com.guoyicap.upload.service;

import com.anniweiya.fastdfs.FastDfsInfo;

/**  
* @ClassName: FileUploadService  
* @Description: 文件服务器 上传文件service
* @author 王振广
* @date 2017年5月31日 上午8:52:57  
*    
*/
public interface FileUploadService {
	
	/**  
	* @Title: upload  
	* @Author:王振广
	* @Description: 上传本地图片
	* @param fileName
	* @return    设定文件  
	* @return String    返回类型  
	* @throws  
	*/
	public FastDfsInfo upload(String localFileName);
	
	
	/**  
	* @Title: upload  
	* @Author:王振广
	* @Description: 上传二进制文件  
	* @param data
	* @param ext
	* @return    设定文件  
	* @return String    返回类型  
	* @throws  
	*/
	public FastDfsInfo upload(byte[] data,String ext);
	
	/**  
	* @Title: download  
	* @Author:王振广
	* @Description: 下载文件
	* @param groupName
	* @param remoteFilename
	* @return    设定文件  
	* @return byte[]    返回类型  
	* @throws  
	*/
	public byte[] download(String groupName, String remoteFileName);

}
