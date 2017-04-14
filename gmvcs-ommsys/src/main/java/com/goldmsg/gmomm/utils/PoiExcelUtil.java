package com.goldmsg.gmomm.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.gosun.core.utils.StringUtils;

public class PoiExcelUtil {
	/*
	 * 解析excel
	 * @Param filePath文件路径
	 * @Return  List<List<List<String>>> 
	 * */
	public static List<List<List<String>>> ExcelReader(String filePath) {
	        FileInputStream inStream = null;
	        List<List<List<String>>> fileContent = new ArrayList<List<List<String>>>();
	        try {
	            inStream = new FileInputStream(new File(filePath));
	            Workbook workBook = WorkbookFactory.create(inStream);
	            for(int i = 0 ; i < workBook.getNumberOfSheets(); i++){
	            	List<List<String>> sheets = new ArrayList<List<String>>();
	            	Sheet sheet = workBook.getSheetAt(i); 
	            	for(int j = 0 ; j < sheet.getPhysicalNumberOfRows(); j++){
	            	    List<String> cellList = new ArrayList<String>();
	            		Row row = sheet.getRow(j);
	            		for(int k= 0; k< row.getPhysicalNumberOfCells(); k++){
	            			String content = row.getCell(k).toString();
	            			if(!StringUtils.isBlank(content)){
	            				cellList.add(content);
	            			}else{
	            				cellList.add("");
	            			}	            			
	            		}
	            		sheets.add(cellList);
	            	}
	            	fileContent.add(sheets);
	            }             
	        } catch (Exception e) {
	            e.printStackTrace();
	        }finally{
	            try {
	                if(inStream!=null){
	                    inStream.close();
	                }                
	            } catch (IOException e) {                
	                e.printStackTrace();
	            }
	        }
	       return  fileContent;
	    }
}
