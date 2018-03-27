package com.jeecg.demo.model;

import java.io.Serializable;
import java.util.Date;

import com.guoyicap.core.page.BaseModel;
/**  
* @ClassName: GpsManageModel  
* @Description:gps列表请求参数
* @author 姜屹征
* @date 2017年6月20日 上午10:36:09  
*    
*/
public class GpsManageModel extends BaseModel implements Serializable{
	
	private static final long serialVersionUID = -5084013878630982226L;
	//gps品牌
	private String brand;
	//gps编号
    private String gpsCode;;
 	//gps型号
    private String gpsmodel;
    //gps类型 0无线 1有线
    private Integer gpstype;
    //状态
    private Integer status;
    //sim号
    private String sim;
	private int pagesize;
	private int currentpage;
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getGpsCode() {
		return gpsCode;
	}
	public void setGpsCode(String gpsCode) {
		this.gpsCode = gpsCode;
	}
	public Integer getGpstype() {
		return gpstype;
	}
	public void setGpstype(Integer gpstype) {
		this.gpstype = gpstype;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getSim() {
		return sim;
	}
	public void setSim(String sim) {
		this.sim = sim;
	}
	public int getPagesize() {
		return pagesize;
	}
	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}
	public int getCurrentpage() {
		return currentpage;
	}
	public void setCurrentpage(int currentpage) {
		this.currentpage = currentpage;
	}
	public String getGpsmodel() {
		return gpsmodel;
	}
	public void setGpsmodel(String gpsmodel) {
		this.gpsmodel = gpsmodel;
	}
	
}