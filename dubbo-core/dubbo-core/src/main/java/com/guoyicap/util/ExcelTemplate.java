package com.guoyicap.util;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.Region;

@SuppressWarnings("deprecation")
public class ExcelTemplate {

	/**
	 * 
	 * <p>Discription:[方法功能中文描述]</p>
	 * @param excelData
	 * @param sizeOfData   导出数据的的长度
	 * @param sizeOfSheet  每个sheet表的容量 不能超过60000条
	 * @param dateFormat 日期格式化，默认：yyyy-MM-dd
	 * @return
	 * @author:[解珍品]
	 * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
	 */
	public static Workbook export(ExcelData excelData, int sizeOfData, int sizeOfSheet,SimpleDateFormat dateFormat) {
		Workbook wb = new HSSFWorkbook();
		int sheetNum = sizeOfData % sizeOfSheet == 0 ? sizeOfData / sizeOfSheet : sizeOfData / sizeOfSheet + 1;
		if(sheetNum==0){
			sheetNum=1;
		}
		for (int i = 0; i < sheetNum; i++) {
			HSSFSheet sheet = (HSSFSheet) wb.createSheet("sheet" + (i + 1));
			int begin = i * sizeOfSheet;
			int end = (i + 1) * sizeOfSheet > sizeOfData ? sizeOfData : (i + 1) * sizeOfSheet;
			createSheet(wb, sheet, excelData, begin, end,dateFormat);
		}
		return wb;
	}

	/**
	 * 
	 * <p>Discription:[创建sheet]</p>
	 * @param wb
	 * @param sheet
	 * @param excelData
	 * @param begin
	 * @param end
	 * @param dateFormat
	 * @author:[解珍品]
	 * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
	 */
	private static void createSheet(Workbook wb, HSSFSheet sheet, ExcelData excelData, int begin, int end,SimpleDateFormat dateFormat) {
		Row row = sheet.createRow(0);
		Row row1 = sheet.createRow(1);
		Row row2 = sheet.createRow(2);
		//设置字体格式
		Font font0 = createFonts(wb, Font.BOLDWEIGHT_BOLD, "宋体", false, (short) 200);
		Font font1 = createFonts(wb, Font.BOLDWEIGHT_NORMAL, "宋体", false, (short) 200);
		Font font2 = createFonts(wb, Font.BOLDWEIGHT_BOLD, "宋体", false, (short) 220);
		//首先定义五种样式
		CellStyle titleStyle = createCenterStyle(wb, font2);
		CellStyle propertyStyle = createCenterStyle(wb, font0);
		CellStyle dataLeftStyle = createLeftStyle(wb, font1);
		CellStyle dataCenterStyle = createCenterStyle(wb, font1);
		CellStyle dataRightStyle = createRightStyle(wb, font1);
		CellStyle[] cellStyle = { dataLeftStyle, dataCenterStyle, dataRightStyle };
		//创建隐藏列（隐藏列动态指定）
		createHideColums(sheet, excelData.getHidelines());
		//设置隐藏行(前两行)
		row.setZeroHeight(true);
		row1.setZeroHeight(true);

		List<Map<String, Object>> data = excelData.getDatamap();
		List<String> identities = excelData.getIdentities();
		//创建第一行各列，并写数据，写入验证信息
		createIdentities(sheet, row, propertyStyle, identities);
		//创建第二行，写入数据的对象属性名称
		createPropertyColumns(sheet, row1, propertyStyle, excelData);
		//创建第三行各列，写标题行
		for (int i = 0; i <= data.get(0).size(); i++) {
			if (i == excelData.getHidelines()) {
				Cell cell = createCell(row2, i, excelData.getTitle());
				cell.setCellStyle(titleStyle);
			} else {
				Cell cell = createCell(row2, i, "");
				cell.setCellStyle(titleStyle);
			}
		}
		//合并单元格
		Region region = new Region((short) 2, (short) excelData.getHidelines(), (short) 2, (short) (data.get(0).size()));
		sheet.addMergedRegion(region);

		if (excelData.getSuperfield() != null && excelData.getSuperfield().size() > 0) {
			//创建第四行，并写数据，主要是对应主表的数据
			createTitleColumns(sheet, propertyStyle, excelData);
		}
		//主要是写各列的标题
		createNameColumns(sheet, propertyStyle, excelData);
		//自此开始为正式数据 
		createDataColumns(sheet, cellStyle, excelData, begin, end,dateFormat);
	}

	protected static void createIdentities(HSSFSheet sheet, Row row, CellStyle cellStyle, List<String> identities) {
		for (int i = 0; i < identities.size(); i++) {
			sheet.setColumnWidth(i, 6000);
			row.setHeight((short) 300);
			Cell cell = createCell(row, i, identities.get(i));
			cell.setCellStyle(cellStyle);
		}
	}

	protected static void createPropertyColumns(HSSFSheet sheet, Row row1, CellStyle cellStyle, ExcelData excelData) {
		List<Map<String, Object>> data = excelData.getDatamap();
		List<String> writefield = excelData.getWritefield();
		Map<String, Object> keys = data.get(0);
		for (int i = 0; i <= data.get(0).size(); i++) {
			sheet.setColumnWidth(i, 6000);
			row1.setHeight((short) 300);
			if (i == 0) {
				//获取打标记行的对应名
				Cell cell = createCell(row1, i, "flag");
				cell.setCellStyle(cellStyle);

			} else {
				boolean flag = false;
				for (String field : writefield) {
					if (keys.get((i - 1) + "").equals(field)) {
						Cell cell = createCell(row1, i, keys.get((i - 1) + "").toString());
						cell.setCellStyle(cellStyle);
						flag = true;
						break;
					}
				}
				if (!flag) {
					Cell cell = createCell(row1, i, keys.get((i - 1) + "").toString());
					cell.setCellStyle(cellStyle);
				}
			}
		}
	}

	protected static Cell createCell(Row row, int column, String value) {
		Cell cell = row.createCell(column);
		cell.setCellValue(value);
		cell.setCellType(Cell.CELL_TYPE_STRING);
		return cell;
	}

	/**
	 * 
	 * <p>Discription:[方法功能中文描述]</p>
	 * @param sheet
	 * @param cellStyle
	 * @param excelData
	 * @author:[创建者中文名字]
	 * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
	 */
	protected static void createTitleColumns(HSSFSheet sheet, CellStyle cellStyle, ExcelData excelData) {
		Row row3 = sheet.createRow(3);
		List<String> superfield = excelData.getSuperfield();
		List<Map<String, Object>> data = excelData.getDatamap();
		for (int i = 0; i < excelData.getHidelines(); i++) {
			Cell cell = createCell(row3, i, "");
			cell.setCellStyle(cellStyle);
		}
		if (null != superfield && superfield.size() > 0) {
			for (int i = 0; i < superfield.size(); i++) {
				Cell cell = createCell(row3, excelData.getHidelines() + i, superfield.get(i));
				cell.setCellStyle(cellStyle);
			}
			if (superfield.size() < data.get(0).size() - excelData.getHidelines()) {
				for (int i = superfield.size() + excelData.getHidelines(); i <= data.get(0).size(); i++) {
					Cell cell = createCell(row3, i, "");
					cell.setCellStyle(cellStyle);
				}
			}
		} else {
			for (int i = excelData.getHidelines(); i <= data.get(0).size(); i++) {
				Cell cell = createCell(row3, i, "");
				cell.setCellStyle(cellStyle);
			}
		}

	}

	/**
	 * 
	 * <p>Discription:[方法功能中文描述]</p>
	 * @param sheet
	 * @param cellStyle2
	 * @param excelData
	 * @author:[创建者中文名字]
	 * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
	 */
	protected static void createNameColumns(HSSFSheet sheet, CellStyle cellStyle2, ExcelData excelData) {
		Row row4 = null;
		if (excelData.getSuperfield() != null && excelData.getSuperfield().size() > 0) {
			row4 = sheet.createRow(4);
		} else {
			row4 = sheet.createRow(3);
		}
		List<Map<String, Object>> data = excelData.getDatamap();
		Map<String, Object> keys = data.get(1);
		for (int i = 0; i <= data.get(1).size(); i++) {
			if (i == 0) {
				Cell cell = createCell(row4, i, "");
				cell.setCellStyle(cellStyle2);
			} else {
				Cell cell = createCell(row4, i, keys.get(data.get(0).get((i - 1) + "")).toString());
				cell.setCellStyle(cellStyle2);
			}
		}
	}

	/**
	 * 
	 * <p>Discription:[方法功能中文描述]</p>
	 * @param sheet
	 * @param cellStyle
	 * @param excelData
	 * @param begin
	 * @param end
	 * @param dateFormat 日期格式化
	 * @author:[创建者中文名字]
	 * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
	 */
	protected static void createDataColumns(HSSFSheet sheet, CellStyle[] cellStyle, ExcelData excelData,int begin, int end,SimpleDateFormat dateFormat) {
		//SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf;
		if(dateFormat==null){
			sdf=new SimpleDateFormat("yyyy-MM-dd");
		}else{
			sdf=dateFormat;
		}
		List<Map<String, Object>> data = excelData.getDatamap();
		int l = 0;
		if(excelData.getSuperfield()!=null&&excelData.getSuperfield().size()>0){
			l=5;
		}else{
			l=4;
		}
		for (int i = begin+2; i < end+2; i++) {
			//创建一行   
			Row rowData = sheet.createRow(l++);
			rowData.setHeight((short) 300);
			for (int k = 0; k <= data.get(i).size(); k++) {
				if (k == 0) {
					Cell cell = createCell(rowData, k, "true");
					cell.setCellStyle(cellStyle[1]);
				} else {
					if(data.get(i).get(data.get(0).get((k - 1) + "")) instanceof String){
						Cell cell = createCell(rowData, k, data.get(i).get(data.get(0).get((k - 1) + "")).toString());
						cell.setCellStyle(cellStyle[0]);
					}else if(data.get(i).get(data.get(0).get((k - 1) + "")) instanceof BigDecimal){
						//String value=data.get(i).get(data.get(0).get((k - 1) + ""))==null?"":NumberFormatTool.decimalFormat(data.get(i).get(data.get(0).get((k - 1) + "")).toString());
						//Cell cell = createCell(rowData, k, value);
						Cell cell = rowData.createCell(k);
						cell.setCellValue(new BigDecimal(data.get(i).get(data.get(0).get((k - 1) + "")).toString()).doubleValue());
						//cell.setCellValue(value);
						cell.setCellType(Cell.CELL_TYPE_NUMERIC);
						cell.setCellStyle(cellStyle[2]);
					}else if(data.get(i).get(data.get(0).get((k - 1) + "")) instanceof Date){
						String value=data.get(i).get(data.get(0).get((k - 1) + ""))==null?"":sdf.format(data.get(i).get(data.get(0).get((k - 1) + "")));
						Cell cell = createCell(rowData, k, value);
						cell.setCellStyle(cellStyle[1]);
					}else{
						Cell cell = createCell(rowData, k, data.get(i).get(data.get(0).get((k - 1) + ""))==null?"":data.get(i).get(data.get(0).get((k - 1) + "")).toString());
						cell.setCellStyle(cellStyle[2]);
					}
					
				}
			}
		}
		
	}

	/**
	 * 
	 * <p>Discription:[创建隐藏列]</p>
	 * @param sheet
	 * @param hideLines
	 * @author:[xiezhp]
	 * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
	 */
	protected static void createHideColums(HSSFSheet sheet, int hideLines) {
		for (int i = 0; i < hideLines; i++) {
			sheet.setColumnHidden(i, true);
		}
	}

	/**
	 * 
	 * <p>Discription:[设置单元格样式]</p>
	 * @param wb
	 * @param row
	 * @param column
	 * @param value
	 * @param font
	 * @param flag
	 * @author:[xiezhp]
	 * @update:[日期YYYY-MM-DD] [更改人姓名][变更描述]
	 */
	public static CellStyle createCenterStyle(Workbook wb, Font font) {
		CellStyle cellStyle = wb.createCellStyle();
		HSSFDataFormat format = ((HSSFWorkbook) wb).createDataFormat();
		cellStyle.setDataFormat(format.getFormat("@"));
		cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);//下边框        
		cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框        
		cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框        
		cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		//不可进行编辑
		/*cellStyle.setLocked(false);*/
		cellStyle.setFont(font);
		return cellStyle;
	}

	protected static CellStyle createLeftStyle(Workbook wb, Font font) {
		CellStyle cellStyle = wb.createCellStyle();
		HSSFDataFormat format = ((HSSFWorkbook) wb).createDataFormat();
		cellStyle.setDataFormat(format.getFormat("@"));
		cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);//下边框        
		cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框        
		cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框        
		cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		//可以进行编辑
		/*cellStyle.setLocked(true);*/
		cellStyle.setFont(font);
		return cellStyle;
	}

	protected static CellStyle createRightStyle(Workbook wb, Font font) {
		CellStyle cellStyle = wb.createCellStyle();
		HSSFDataFormat format = ((HSSFWorkbook) wb).createDataFormat();
		cellStyle.setDataFormat(format.getFormat("@"));
		cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);//下边框        
		cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框        
		cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框        
		cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		/*cellStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);*/
		cellStyle.setFont(font);
		return cellStyle;
	}

	/**  
	 * 设置字体  
	 *   
	 * @param wb  
	 * @return  
	 */
	protected static Font createFonts(Workbook wb, short bold, String fontName, boolean isItalic, short hight) {
		Font font = wb.createFont();
		font.setFontName(fontName);
		font.setBoldweight(bold);
		font.setItalic(isItalic);
		font.setFontHeight(hight);
		return font;
	}

}
