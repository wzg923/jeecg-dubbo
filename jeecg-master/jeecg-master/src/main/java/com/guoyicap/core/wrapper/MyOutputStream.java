package com.guoyicap.core.wrapper;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;

/**
* @ClassName: MyOutputStream
* @Description: 封装OutputStream
* @author 王振广
* @date 2016年5月3日 下午2:31:56
*
*/ 
public class MyOutputStream extends ServletOutputStream {

	private ServletOutputStream outputStream;

	public MyOutputStream(ServletOutputStream outputStream) {
		this.outputStream = outputStream;
	}

	@Override
	public void write(int b) throws IOException {
		outputStream.write(b);
		// System.out.println("output1");
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		super.write(b, off, len);
		// System.out.println("output2");
	}

	@Override
	public void write(byte[] b) throws IOException {
		super.write(b);
		// System.out.println("output3");
	}

	@Override
	public boolean isReady() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setWriteListener(WriteListener writeListener) {
		// TODO Auto-generated method stub
		
	}
	
	

}
