package com.guoyidzy.demo.util;

public class Gps {
	//经度(Longitude)  纬度(Latitude)
	private Double wgLon;//经度
	private Double wgLat;//纬度
	
	
	public Gps(Double wgLat,Double wgLon) {
		this.wgLon = wgLon;
		this.wgLat = wgLat;
	}
	/**  
	* @Title: getWgLon  
	* @Author:王振广
	* @Description: 经度  
	* @return    设定文件  
	* @return Double    返回类型  
	* @throws  
	*/
	public Double getWgLon() {
		return wgLon;
	}
	public void setWgLon(Double wgLon) {
		this.wgLon = wgLon;
	}
	/**  
	* @Title: getWgLat  
	* @Author:王振广
	* @Description: 纬度 
	* @return    设定文件  
	* @return Double    返回类型  
	* @throws  
	*/
	public Double getWgLat() {
		return wgLat;
	}
	public void setWgLat(Double wgLat) {
		this.wgLat = wgLat;
	}
	
	@Override
	public String toString() {
		return "Gps [wgLon=" + wgLon + ", wgLat=" + wgLat + "]";
	}
	

	
}
