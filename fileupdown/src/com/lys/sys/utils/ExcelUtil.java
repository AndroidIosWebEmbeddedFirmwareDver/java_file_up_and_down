package com.lys.sys.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;

import com.lys.sys.log.Log;


public class ExcelUtil {
	/**
	 * 解析Excel文件流
	 * 
	 * @param id
	 * @throws IOException
	 */
	@SuppressWarnings("deprecation")
	public static Map<Integer,List<Map<String, String>>> readXls(InputStream is) throws IOException {
		Map<Integer,List<Map<String, String>>> map = new HashMap<Integer,List<Map<String, String>>>();
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
		Log.in.info("解析Excel文件中...");
		// 循环工作表Sheet
		for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
			List<Map<String, String>> objs = new LinkedList<Map<String, String>>();
			HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
			if (hssfSheet == null) {
				continue;
			}
			int sheetmergerCount = hssfSheet.getNumMergedRegions();// 获得一个 sheet 中合并单元格的数量
			// 循环行Row //hssfSheet.getPhysicalNumberOfRows()
			for (int rowNum = 0; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
				Map<String, String> obj = new HashMap<String, String>();
				HSSFRow hssfRow = hssfSheet.getRow(rowNum);
				if (hssfRow != null) {
					for (int cellNum = 0; cellNum <= hssfRow.getLastCellNum(); cellNum++) {
						HSSFCell hssfCell = hssfRow.getCell((short) cellNum);
						if (hssfCell == null) {
							continue;
						}
						boolean flag=true;//是执行put
						int firstColumn = 0;
						int lastColumn = 0;
						int firstRow = 0;
						int lastRow = 0;
						for (int i = 0; i < sheetmergerCount; i++) {
							// 获得合并单元格加入list中
							CellRangeAddress ca = hssfSheet.getMergedRegion(i);
							// 获得合并单元格的起始行, 结束行, 起始列, 结束列
					        firstColumn = ca.getFirstColumn();  
					        lastColumn = ca.getLastColumn();  
					        firstRow = ca.getFirstRow();  
					        lastRow = ca.getLastRow();  
					        if(rowNum>= firstRow && rowNum <= lastRow){  
					            if(cellNum >= firstColumn && cellNum <= lastColumn){  
					            	if(!(cellNum==firstColumn&&rowNum==firstRow)){
					            		flag=false;
									}
					            	break;
					            }  
					        }
						}
						if(flag){
							obj.put(String.valueOf(cellNum), getValue(hssfCell));
						}
					}
				}
				// 循环列Cell
				objs.add(obj);
			}
			map.put(numSheet, objs);
		}
		Log.in.info("解析Excel文件完毕!");
		return map;
	}

	/**
	 * 获取Excel中cell值
	 * 
	 * @param hssfCell
	 * @return
	 */
	@SuppressWarnings("static-access")
	private static String getValue(HSSFCell hssfCell) {
		if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN) {
			return String.valueOf(hssfCell.getBooleanCellValue());
		} else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
			return String.valueOf(hssfCell.getNumericCellValue());
		} else {
			return String.valueOf(hssfCell.getStringCellValue());
		}
	}
}
