package com.guoyicap.core.model;

import java.io.Serializable;

/**
* @ClassName: ReturnPaginationModel
* @Description: 返回分页信息
* @author 王振广
* @date 2016年10月11日 下午5:58:36
*
*/ 
public class ReturnPaginationModel implements Serializable{
	/**
	* @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
	*/ 
	private static final long serialVersionUID = 7573137855806257508L;
	private Integer currentpage;//页码
	private Integer totalnum;//总记录数
	private Integer pagesize;//每页记录数
	private Integer totalpage;//总页数
	private Object info;//数据
	
	public ReturnPaginationModel(){}
	public ReturnPaginationModel(int currentpage,int totalnum,int pagesize,int totalpage,Object info){
		this.currentpage=currentpage;
		this.totalnum=totalnum;
		this.pagesize=pagesize;
		this.totalpage=totalpage;
		this.info=info;
	}
	
	public Integer getCurrentpage() {
		return currentpage;
	}
	public void setCurrentpage(Integer currentpage) {
		this.currentpage = currentpage;
	}
	public Integer getTotalnum() {
		return totalnum;
	}
	public void setTotalnum(Integer totalnum) {
		this.totalnum = totalnum;
	}
	public Integer getPagesize() {
		return pagesize;
	}
	public void setPagesize(Integer pagesize) {
		this.pagesize = pagesize;
	}
	public Integer getTotalpage() {
		return totalpage;
	}
	public void setTotalpage(Integer totalpage) {
		this.totalpage = totalpage;
	}
	/**
	* @return info
	*/ 
	public Object getInfo() {
		return info;
	}
	/**
	* @param info 要设置的 info
	*/ 
	public void setInfo(Object info) {
		this.info = info;
	}
	
	
}
