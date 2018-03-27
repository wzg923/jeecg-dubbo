package com.guoyicap.util;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
* @ClassName: Page
* @Description: TODO(分页查询 对象)
* @author 王振广
* @date 2015年12月16日 下午1:02:42
*
* @param <T>
*/ 
public class Page <T> implements Serializable{
	/**
	* @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
	*/ 
	private static final long serialVersionUID = 8730105383370857156L;
	/**
	 * 结果list
	 */
	private List<T> records;
	/**
	 * 分页记录数
	 */
	private Integer pageSize;
	/**
	 * 当前页数
	 */
	private Integer pageNo;
	/**
	 * 总数量total
	 */
	private Integer total;
	/**
	* 总分页数
	*/ 
	private Integer totalPage;
	/**其他自定义数据*/
	private Map<String,Object> params;
	
	public Page(){}
	/**
	 * @param records
	 * @param total
	 */
	public Page (List<T> records,Integer total){
		setRecords(records);
		setTotal(total);
	}
	
	/**
	* <p>Title: </p>
	* <p>Description: </p>
	* @param records 记录list
	* @param total	总记录数
	* @param pageSize 分页大小
	* @param pageNo	当前页码
	*/
	public Page (List<T> records,Integer total,Integer pageSize, Integer pageNo){
		setRecords(records);
		setTotal(total);
		setPageSize(pageSize);
		setPageNo(pageNo);
		this.totalPage = ((Double) Math.ceil(Double.valueOf(total)
				/ Double.valueOf(pageSize)))
					.intValue();
	}
	public List<T> getRecords() {
		return records;
	}
	public void setRecords(List<T> records) {
		this.records = records;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	public Integer getPageNo() {
		return pageNo;
	}
	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}
	public Map<String, Object> getParams() {
		return params;
	}
	public void setParams(Map<String, Object> params) {
		this.params = params;
	}
	/**
	* @return totalPage
	*/ 
	public Integer getTotalPage() {
		return totalPage;
	}
	/**
	* @param totalPage 要设置的 totalPage
	*/ 
	public void setTotalPage(Integer totalPage) {
		this.totalPage = totalPage;
	}
	
}
