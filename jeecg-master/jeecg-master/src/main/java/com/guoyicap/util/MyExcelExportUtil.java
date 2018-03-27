package com.guoyicap.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.util.BrowserUtils;
import org.jeecgframework.core.util.LogUtil;

/**
 * Excel导出工具类
 * 
 * @author guang
 *
 */
public class MyExcelExportUtil <T> {

	/**
	 * 导出Excel方法
	 * 
	 * @param request
	 * @param response
	 * @param title
	 *            标题，文件名
	 * @param dataList
	 *            查询的数据结果List<T>
	 * @param dataGrid
	 *            DataGrid参数
	 * @param dataFormat 日期格式化
	 */
	public void exportExcel(HttpServletRequest request, HttpServletResponse response, String title, List<T> dataList,
			DataGrid dataGrid,SimpleDateFormat dateFormat) {
		// 自定义Excel导出时，数据封装Bean
		ExcelData excelData = new ExcelData();
		excelData
				.setTitle((title == null || "".equals(title)) ? String.valueOf(new java.util.Date().getTime()) : title);
		// 跳转到404页面
		RequestDispatcher rd = request.getRequestDispatcher("common/404Front");

		List<Map<String, Object>> dataMapList = new ArrayList<Map<String, Object>>();
		// 定义要导出的列
		Map<String, Object> fieldMap = getExportSignMap(dataGrid);
		dataMapList.add(fieldMap);
		// 定义 导出的列 对应的表头名称
		dataMapList.add(getExportColumnMap(dataGrid));
		// 将查询数据封装到List<Map>中，获得想要得到的格式数据，方便Excel导出时安照规则读取
		try {
			getExcelDataMap(dataList, dataMapList);
		} catch (MyException e2) {
			LogUtil.error(e2.getMessage(), e2);
			// 跳转到404页面
			try {
				rd.forward(request, response);
			} catch (ServletException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		excelData.setDatamap(dataMapList);
		// 设置response 的contextType
		response.setContentType("application/vnd.ms-excel");
		OutputStream fOut = null;// 输出流

		try {
			// 根据浏览器进行转码，使其支持中文文件名
			String browse = BrowserUtils.checkBrowse(request);
			if ("MSIE".equalsIgnoreCase(browse.substring(0, 4))) {
				// 设置response 头信息
				response.setHeader("content-disposition",
						"attachment;filename=" + java.net.URLEncoder.encode(excelData.getTitle(), "UTF-8") + ".xls");
			} else {
				// 文件名转码
				String newtitle = new String(excelData.getTitle().getBytes("UTF-8"), "ISO8859-1");
				// 设置response 头信息
				response.setHeader("content-disposition", "attachment;filename=" + newtitle + ".xls");
			}
			// 产生工作簿对象
			Workbook workbook = null;
			// 导出Excel
			workbook = ExcelTemplate.export(excelData, dataList.size(), 60000,dateFormat);
			// 获得输出流
			fOut = response.getOutputStream();
			// 将Excel文件写入到输出流中
			workbook.write(fOut);

		} catch (UnsupportedEncodingException e1) {
			LogUtil.error("字符编码转换异常");
			// 跳转到404页面
			try {
				rd.forward(request, response);
			} catch (ServletException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			LogUtil.error("文件导出异常");
			// 跳转到404页面
			try {
				rd.forward(request, response);
			} catch (ServletException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} finally {
			try {
				// 刷新输出流
				fOut.flush();
				// 关闭输出流
				fOut.close();

			} catch (IOException e) {
				LogUtil.error("输出流关闭异常");

			}
		}

	}

	/**
	 * 定义 要导出的数据 列名
	 * 
	 * @param dataGrid
	 * @return
	 */
	private Map<String, Object> getExportSignMap(DataGrid dataGrid) {
		Map<String, Object> value = new HashMap<String, Object>();
		if (StringUtils.isNotBlank(dataGrid.getField())) {
			// dataGrid field
			String[] fields = dataGrid.getField().split(",");
			if (fields.length > 0) {
				/** 依次将field放入map中 */
				for (int i = 0; i < fields.length; i++) {
					value.put(i + "", fields[i]);// 存放规则 key：0下标，value：field值
				}
			}

		}
		return value;
	}

	/**
	 * 定义导出的文件标题 列:标题
	 * 
	 * @param fieldMap
	 * @return
	 */
	private Map<String, Object> getExportColumnMap(DataGrid dataGrid) {
		Map<String, Object> value = new HashMap<String, Object>();
		// field列
		String[] fields = dataGrid.getField().split(",");
		// title列标题
		String[] titles = dataGrid.getTitle().split(",");
		// 数组非空，且长度一致
		if (fields.length > 0 && titles.length > 0 && fields.length == titles.length) {
			/** 依次将field放入map中 */
			for (int i = 0; i < fields.length; i++) {
				value.put(fields[i], titles[i]);// 存放规则 key：0下标，value：field值
			}
		}
		return value;
	}

	/**数据封装，规则：key(列field):value（列value）
	 * @param dataList
	 * @param dataMapList
	 * @throws MyException
	 */
	private void getExcelDataMap(List<T> dataList, List<Map<String, Object>> dataMapList) throws MyException {
		// 遍历导出的结果
		if (dataList != null && dataList.size() > 0) {
			for (int i = 0; i < dataList.size(); i++) {
				T bean = dataList.get(i);// Bean
				if(bean instanceof HashMap){
					dataMapList.add((Map<String, Object>)bean);
				}else{
					Map<String, Object> value = new ConcurrentHashMap<String, Object>();// 自定义封装map
					try {
						value = BeanUtils.convertBeanToMap(bean);
						//如果导出的列存在id，则将id值替换为序号
						if(value.containsKey("id")){
							value.remove("id");
							value.put("id", i+1);
						}
						//bean转换后的Map value key键 迭代器
						Iterator iterator=value.keySet().iterator();
						while(iterator.hasNext()){
							String key=(String)iterator.next();//key键
							//如果定义导出的列中不包含该列key
							if(!dataMapList.get(1).keySet().contains(key)){
								//移除值
								value.remove(key);
							}
						}
						
					} catch (Exception e) {
						LogUtil.error("数据封装失败", e);
						throw new MyException("数据封装失败", e);
					}
					// 添加到 封装的导出结果list
					dataMapList.add(value);
				}

			}
		}

	}
}
