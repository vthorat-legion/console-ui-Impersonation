package com.legion.utils;

import java.io.File;

import java.io.FileInputStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import org.apache.poi.ss.usermodel.Sheet;

import org.apache.poi.ss.usermodel.Workbook;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class SpreadSheetUtils {

	public static ArrayList<HashMap<String, String>> readExcel(String fileNameAndPath,String sheetName) throws IOException {
				
		File file =    new File(fileNameAndPath);
	    FileInputStream inputStream = new FileInputStream(file);
	    Workbook workbook = null;
	    ArrayList<HashMap <String, String>> spreadSheetValue = new ArrayList<HashMap <String, String>>();
	    String fileExtensionName = fileNameAndPath.substring(fileNameAndPath.indexOf("."));
	    if(fileExtensionName.equals(".xlsx")){
	    	workbook = new XSSFWorkbook(inputStream);
	    }
	    else if(fileExtensionName.equals(".xls")){
	    	workbook = new HSSFWorkbook(inputStream);
	    }

	    Sheet spreadSheet = workbook.getSheet(sheetName);

	    int rowCount = spreadSheet.getLastRowNum()-spreadSheet.getFirstRowNum();
	    for (int i = 1; i < rowCount+1; i++) {
	        Row row = spreadSheet.getRow(i);
	        HashMap <String, String> spreadSheetRow = new HashMap <String, String>();
	        if(! isEmptyRow(row))
	        {
	        	for (int j = 0; j < row.getLastCellNum(); j++) {
	        		if(row.getCell(j) == null)  {
	        			spreadSheetRow.put(spreadSheet.getRow(0).getCell(j).getStringCellValue().trim(), "");
	        		}
	        		else {
	        			row.getCell(j).setCellType(Cell.CELL_TYPE_STRING);
			            spreadSheetRow.put(spreadSheet.getRow(0).getCell(j).getStringCellValue().trim(), row.getCell(j).getStringCellValue());
	        		}
	        		
		        }
	        	spreadSheetValue.add(spreadSheetRow);
	        }
	    } 
	    return spreadSheetValue;
	}
	
	  public static boolean isEmptyRow(Row row){
		     boolean isEmptyRow = true;
		     //System.out.println(row);
		     if(row != null)
		     {
		    	 for(int cellNum = row.getFirstCellNum(); cellNum < row.getLastCellNum(); cellNum++){
		            Cell cell = row.getCell(cellNum);
		            if(cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK && StringUtils.isNotBlank(cell.toString())){
		            isEmptyRow = false;
		            }    
		         }
		     }
		     return isEmptyRow;
		   }
}
