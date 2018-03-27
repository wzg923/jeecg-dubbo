package com.guoyicap.core.wrapper;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
* @ClassName: EncryptHttpServletResponseWrapper
* @Description: response 内容加密输出  封装
* @author 王振广
* @date 2016年5月3日 下午2:15:58
*
*/ 
public class MyHttpServletResponseWrapper extends HttpServletResponseWrapper {
	private MyPrintWriter myPrintWriter;
	private MyOutputStream myOutputStream;

	public MyHttpServletResponseWrapper(HttpServletResponse response) {
		super(response);
		// TODO Auto-generated constructor stub
		
//		myOutputStream = new MyOutputStream();
//		myPrintWriter = new MyPrintWriter(output);
	}

	@Override
	public PrintWriter getWriter() throws IOException {
		myPrintWriter = new MyPrintWriter(super.getWriter());
		return myPrintWriter;
	}

	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		myOutputStream = new MyOutputStream(super.getOutputStream());
		return myOutputStream;
	}
	
	public MyPrintWriter getMyWriter() {  
        return myPrintWriter;  
    }  
  
    public MyOutputStream getMyOutputStream() {  
        return myOutputStream;  
    }  
	
	/*private ResponsePrintWriter writer;
    private ByteArrayOutputStream output;
	
	
	public MyHttpServletResponseWrapper(HttpServletResponse httpServletResponse)
    {
        super(httpServletResponse);
        output = new ByteArrayOutputStream();
        writer = new ResponsePrintWriter(output);
    }
	
	
    private class ResponsePrintWriter extends PrintWriter
    {
        ByteArrayOutputStream output;

        public ResponsePrintWriter(ByteArrayOutputStream output)
        {
            super(output);
            this.output = output;
        }

        public ByteArrayOutputStream getByteArrayOutputStream()
        {
            return output;
        }
    }

   



    public void finalize() throws Throwable
    {
        super.finalize();
        output.close();
        writer.close();
    }

    public String getContent()
    {
        try
        {
            writer.flush();
            return writer.getByteArrayOutputStream().toString("UTF-8");
        }
        catch(UnsupportedEncodingException e)
        {
            return "UnsupportedEncoding";
        }
    }

    public void close() throws IOException
    {
        writer.close();
    }

    public PrintWriter getWriter() throws IOException
    {
        return writer;
    }*/

}
