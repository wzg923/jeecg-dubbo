package org.jeecgframework.core.aop;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecgframework.core.util.LogUtil;



/**
 * JS缓存压缩
 * JEECG开源社区
 * 论坛：www.jeecg.org
 * @author  张代浩
 */
public class GZipFilter implements Filter {
	//默认为true,true打开GZIP压缩功能
	private static boolean GZIPEnabled=true;
	//认为false,true打开GZIP压缩功能日志,可以在后台看到压缩比例信息
	private static boolean LogStats=false;
	//(默认为true,true打开GZIP缓存功能)
	private static boolean CacheEnabled=true;

	@Override
    public void destroy() {
    }
      /**
       * 判断浏览器是否支持GZIP
       * @param request
       * @return
       */
      private static boolean isGZipEncoding(HttpServletRequest request){
        boolean flag=false;
        String encoding=request.getHeader("Accept-Encoding");
        if(encoding!=null&&encoding.indexOf("gzip")!=-1){
          flag=true;
        }
         return flag;
      }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpServletRequest req=(HttpServletRequest)request;
        if(GZIPEnabled && isGZipEncoding(req)){
            Wrapper wrapper = new Wrapper(resp);
            chain.doFilter(request, wrapper);           
            byte[] gzipData = gzip(wrapper.getResponseData());
            //使浏览器保存压缩和非压缩两份网页存档
            resp.addHeader("Vary", "User-Agent");//add by guang 添加  2016/4/20
            
            resp.addHeader("Content-Encoding", "gzip");           
            resp.setContentLength(gzipData.length);
            //压缩比例 ： （压缩前-压缩后）/压缩前
            if(LogStats){
            	 double originLength=wrapper.getResponseData().length;
                 double gzipLength=gzipData.length;
                 double gzipPercent=Double.valueOf(Math.round((originLength-gzipLength)/originLength*10000))/100;
                 LogUtil.info("压缩比例："+gzipPercent+"%");
            }           
            //静态资源文件缓存机制
            if(CacheEnabled){
            	CacheResource(request, response, chain);
            }            
            ServletOutputStream output = response.getOutputStream();
            output.write(gzipData);
            output.flush();
        } else {
            chain.doFilter(request, response);
        }        

    }
    @Override
	public void init(FilterConfig filterConfig) throws ServletException {}
    
    /**
     * 提高系统访问性能，主键缓存
     */
    public void CacheResource(ServletRequest request, ServletResponse response,
            FilterChain chain){
    	//1.强转httpservlet，方便调用方法   
        HttpServletRequest req = (HttpServletRequest) request;  
        HttpServletResponse res = (HttpServletResponse) response;  
        //2.获取资源文件名的URI   
        String uri = req.getRequestURI();  
        //3.获得文件扩展名,lastIndexOf(".")+1 获得.最后一次出现的索引的后一位：jpg   
        uri = uri.substring(uri.lastIndexOf(".")+1);  
        //System.out.println( uri );//测试获取后缀是否正确      
        //4断相应后缀文件，设定缓存时间   
        long date = 0;  
        //System.out.println( new Date().getTime());//测试当前时间用   
          
        //判断URI获取的后缀名是否与JPG相等，不考虑大小写   
        if(uri.equalsIgnoreCase("jpg")){  
            //读取XML里的JPG配置的参数，这里设定了时间   
            //获取当前系统时间 + 需要缓存的时间(小时),Long 防止溢出，因为单位是毫秒   
            date = System.currentTimeMillis()+5*60*60*1000;  
        }  
          
        if(uri.equalsIgnoreCase("gif")){  
            //读取XML里的JPG配置的参数，这里设定了时间   
            //获取当前系统时间 + 需要缓存的时间(小时),Long 防止溢出，因为单位是毫秒   
            date = System.currentTimeMillis()+5*60*60*1000;  
        }  
        
        //判断URI获取的后缀名是否与PNG相等，不考虑大小写   
        if(uri.equalsIgnoreCase("png")){  
            //读取XML里的JPG配置的参数，这里设定了时间   
            //获取当前系统时间 + 需要缓存的时间(小时),Long 防止溢出，因为单位是毫秒   
            date = System.currentTimeMillis()+5*60*60*1000;  
        } 
        if(uri.equalsIgnoreCase("css")){  
            //读取XML里的JPG配置的参数，这里设定了时间   
            //获取当前系统时间 + 需要缓存的时间(小时),Long 防止溢出，因为单位是毫秒   
            date = System.currentTimeMillis()+5*60*60*1000;  
        }  
          
        if(uri.equalsIgnoreCase("js")){  
            //读取XML里的JPG配置的参数，这里设定了时间   
            //获取当前系统时间 + 需要缓存的时间(小时),Long 防止溢出，因为单位是毫秒   
            date = System.currentTimeMillis()+5*60*60*1000;  
        }  
        //设置缓存时间   
        res.setDateHeader("Expires", date);  
    }

    private byte[] gzip(byte[] data) {
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream(10240);
        GZIPOutputStream output = null;
        try {
            output = new GZIPOutputStream(byteOutput);
            output.write(data);
        } catch (IOException e) {
        } finally {
            try {
                output.close();
            } catch (IOException e) {
            }
        }
        return byteOutput.toByteArray();
    }

}
