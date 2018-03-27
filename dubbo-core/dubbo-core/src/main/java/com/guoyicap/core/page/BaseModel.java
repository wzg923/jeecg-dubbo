package com.guoyicap.core.page;

import java.io.Serializable;
import java.util.Map;

import com.guoyicap.core.enums.SortDirection;

/**
 * @author guang
 *Base Model 查询条件model
 */
public class BaseModel implements Serializable{
	/**
	* @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
	*/ 
	private static final long serialVersionUID = 9115148579023340612L;
	private int page = 1;// 当前页
	private int rows = 10;// 每页显示记录数
	private String sort = null;// 排序字段名
	private String order = SortDirection.asc.name();// 按什么排序(asc,desc)
	private Map<String,Object> params;//自定义参数 
    private String ruleSql;//数据权限sql  inarea

	public String getRuleSql() {
		return ruleSql;
	}
	public void setRuleSql(String ruleSql) {
		this.ruleSql = ruleSql;
	}
	/**
	 * 当前页数
	 * @return
	 */
	public int getPage() {
		return page;
	}
	/**
	 * 当前页数
	 * @param page
	 */
	public void setPage(int page) {
		this.page = page;
	}
	/**
	 * 每页显示记录数
	 * @return
	 */
	public int getRows() {
		return rows;
	}
	/**
	 * 每页显示记录数
	 * @param rows
	 */
	public void setRows(int rows) {
		this.rows = rows;
	}
	/**排序字段
	 * @return
	 */
	public String getSort() {
		return sort;
	}
	/**排序字段
	 * @param sort
	 */
	public void setSort(String sort) {
		this.sort = sort;
	}
	/**排序方式
	 * @return
	 */
	public String getOrder() {
		return order;
	}
	/**排序方式
	 * @param order
	 */
	public void setOrder(String order) {
		this.order = order;
	}
	public Map<String,Object> getParams() {
		return params;
	}
	public void setParams(Map<String,Object> params) {
		this.params = params;
	}
	
	
}
