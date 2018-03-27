package com.jeecg.demo.model;

import java.io.Serializable;
import java.util.Date;

import com.guoyicap.core.page.BaseModel;


/**  
* @ClassName: GpsManageListModel  
* @Description: gps列表返回参数
* @author 姜屹征
* @date 2017年6月20日 上午10:29:09  
*    
*/
public class GpsManageListModel extends BaseModel implements Serializable{
	
	private static final long serialVersionUID = -5084013878630982226L;
	private Integer id;
	//gps品牌
	private String brand;
	//gps编号
    private String gpsCode;
    //gps型号
    private String gpsmodel;
    //gps类型 0无线 1有线
    private Integer gpsType;
    //状态  0未使用   1使用中   2报损   3转捷信  4故障
    private Integer status;
    //Sim卡号
    private String sim;
    //电量
    private Integer powerval;
    //设备类型：0 新设备 1老设备
    private Integer type;
    //入库时间
    private Date createTime;
    
	public String getGpsmodel() {
		return gpsmodel;
	}
	public void setGpsmodel(String gpsmodel) {
		this.gpsmodel = gpsmodel;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
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
	public Integer getGpsType() {
		return gpsType;
	}
	public void setGpsType(Integer gpsType) {
		this.gpsType = gpsType;
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
	public Integer getPowerval() {
		return powerval;
	}
	public void setPowerval(Integer powerval) {
		this.powerval = powerval;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
    

}