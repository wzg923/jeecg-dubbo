package com.guoyicap.core.util.ftp;

import java.io.File;
import java.io.FileInputStream;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.jeecgframework.core.util.ResourceUtil;

/**  
* @ClassName: FTPUpload  
* @Description: FTP 服务器上传文件
* @author A18ccms a18ccms_gmail_com  
* @date 2016年12月2日 下午4:28:45  
*    
*/
public class FTPUpload {
    private FTPClient ftp;
    private String path;
    private String server;
    private int port;
    private String username;//用户名
    private String pwd;//密码
    public FTPUpload(){
        this.path=ResourceUtil.getConfigByName("ftp.basePath");
        this.server=ResourceUtil.getConfigByName("ftp.server");
        this.port=ResourceUtil.getConfigByName("ftp.port")==null?22:Integer.valueOf(ResourceUtil.getConfigByName("ftp.port"));
        this.username=ResourceUtil.getConfigByName("ftp.username");
        this.pwd=ResourceUtil.getConfigByName("ftp.pwd");
        try {
            connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 
     * @param path
     *            上传到ftp服务器哪个路径下
     * @param addr
     *            地址
     * @param port
     *            端口号
     * @param username
     *            用户名
     * @param password
     *            密码
     * @return
     * @throws Exception
     */
    private boolean connect() throws Exception {
        boolean result = false;
        ftp = new FTPClient();
        int reply;
        ftp.connect(server, port);
        ftp.login(username, pwd);
        ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
        reply = ftp.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftp.disconnect();
            return result;
        }
        ftp.changeWorkingDirectory(path);
        result = true;
        return result;
    }

    /**
     * 
     * @param file
     *            上传的文件或文件夹
     * @throws Exception
     */
    private void upload(File file) throws Exception {
        if (file.isDirectory()) {
            ftp.makeDirectory(file.getName());
            ftp.changeWorkingDirectory(file.getName());
            String[] files = file.list();
            for (int i = 0; i < files.length; i++) {
                File file1 = new File(file.getPath() + "\\" + files[i]);
                if (file1.isDirectory()) {
                    upload(file1);
                    ftp.changeToParentDirectory();
                } else {
                    File file2 = new File(file.getPath() + "\\" + files[i]);
                    FileInputStream input = new FileInputStream(file2);
                    ftp.storeFile(file2.getName(), input);
                    input.close();
                }
            }
        } else {
            File file2 = new File(file.getPath());
            FileInputStream input = new FileInputStream(file2);
            ftp.storeFile(file2.getName(), input);
            input.close();
        }
    }

    public static void main(String[] args) throws Exception {
        FTPUpload t = new FTPUpload();
        File file = new File("D://ad1.png");
        t.upload(file);
    }
}
