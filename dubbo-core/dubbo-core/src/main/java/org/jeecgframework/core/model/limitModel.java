package org.jeecgframework.core.model;

import java.io.Serializable;

public class limitModel implements Serializable{

	private static final long serialVersionUID = 3451187333578536047L;
	
	//开始值
	private int startcount;
	
	private int rows ;

	public int getStartcount() {
		return startcount;
	}

	public void setStartcount(int startcount) {
		this.startcount = startcount;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}
	
	
}
