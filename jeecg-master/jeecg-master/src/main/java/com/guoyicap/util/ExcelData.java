package com.guoyicap.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExcelData {
	
	private List<Map<String,Object>> datamap;//数据map list
	private List<String> identities;
	private List<String> superfield;
	private String title;//标题 
	private int hidelines;//隐藏的行
	private List<String> writefield;
	public List<Map<String, Object>> getDatamap() {
		return datamap;
	}
	public void setDatamap(List<Map<String, Object>> datamap) {
		this.datamap = datamap;
	}
	public List<String> getIdentities() {
		return identities;
	}
	public void setIdentities(List<String> identities) {
		this.identities = identities;
	}
	public List<String> getSuperfield() {
		return superfield;
	}
	public void setSuperfield(List<String> superfield) {
		this.superfield = superfield;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getHidelines() {
		return hidelines;
	}
	public void setHidelines(int hidelines) {
		this.hidelines = hidelines;
	}
	public List<String> getWritefield() {
		return writefield;
	}
	public void setWritefield(List<String> writefield) {
		this.writefield = writefield;
	}
	/**
	 * 构造方法
	 */
	public ExcelData(){
		this.identities=new ArrayList<String>();
		this.writefield=new ArrayList<String>();
		this.superfield=new ArrayList<String>();
		this.hidelines=1;
		
	}
	
}
