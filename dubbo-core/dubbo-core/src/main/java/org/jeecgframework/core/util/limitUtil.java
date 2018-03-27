package org.jeecgframework.core.util;

import org.jeecgframework.core.model.limitModel;

public class limitUtil {

	public static limitModel calculation(int startLimit , int rows){
		int start = startLimit==0?1:startLimit;
		rows = (rows==0?5:rows);
		int startCount = (start-1)*rows;

		limitModel limit = new limitModel();
		limit.setRows(rows);
		limit.setStartcount(startCount);
		return limit;
	}
}

