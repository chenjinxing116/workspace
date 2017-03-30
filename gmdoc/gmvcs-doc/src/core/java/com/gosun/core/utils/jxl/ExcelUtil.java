package com.gosun.core.utils.jxl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtilsBean;

import com.gosun.core.utils.StringUtils;

import jxl.Sheet;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
/**
 * 使用jxl进行封装的excel操作工具类
 * @author linhua
 */
public class ExcelUtil {
	/**
	 * 创建excel文件，以输出流返回
	 * @param os 输出流
	 * @param sheetsName 一个excel文件中多个表的表名集合
	 * @param sheetHead  表的标题行中各列的名称集合
	 * @param sheetsdata  各个表的行数据  List<List<List<String>>> 多个表集合->多行的集合->多列的集合
	 * @throws IOException 
	 * @throws WriteException 
	 * @throws RowsExceededException 
	 */
	public static void createXls(OutputStream os,List sheetsName,List sheetHead,List<List> sheetsData) throws IOException, RowsExceededException, WriteException{
		WritableWorkbook book = Workbook.createWorkbook(os);
		if(sheetsName!=null&&sheetsName.size()>0){
			for(int s = 0;s<sheetsName.size();s++){
				WritableSheet sheet = book.createSheet((String)sheetsName.get(s), s);
				
	            // head  
	            WritableFont wfHead = new WritableFont(WritableFont.ARIAL, 12, WritableFont.BOLD);  
	            WritableCellFormat wcfHead = new WritableCellFormat(wfHead);
	            fontCenter(wcfHead);
	            //body
				WritableFont wfbody = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD);
				// 正常字体
				WritableCellFormat wcf = new WritableCellFormat(wfbody);
				fontCenter(wcf);
				
				sheet.getSettings().setVerticalFreeze(1);
				//head
	            if(sheetHead!=null&&sheetHead.size()>0){
	            	for (int i=0; i<sheetHead.size(); i++) { 
	            		sheet.setColumnView(i, 28); // 设置列宽度
	            		sheet.setRowView(0, 550);  //设置行高
		                Label labHead = new Label(i, 0, (String)sheetHead.get(i), wcfHead);
		                sheet.addCell(labHead);  
		            } 
	            }
	            
	            //data
	            if(sheetsData!=null&&sheetsData.size()>0){
	            	List sheetData = sheetsData.get(s);
	            	if(sheetData.get(0) instanceof List){
	            		// list中是list  
	                    for (int i=0; i<sheetData.size(); i++) {  
	                        List lst = (List) sheetData.get(i);  
	                        for (int j=0; j<lst.size(); j++) {
	                        		sheet.setRowView(i+1, 400);
	                                Label labData = new Label(j, i+1, lst.get(j).toString(), wcf);  
	                                sheet.addCell(labData);  
	                        }  
	                    }  
	            	}else if(sheetData.get(0) instanceof Object[]){
	            		 // list中是数组  
	                    for (int i=0; i<sheetData.size(); i++) {
	                    	Object[] obj = (Object[]) sheetData.get(i);
	                    	for (int j=0; j<obj.length; j++) {
	                    		sheet.setRowView(0, 400);
	                    		Label labData = new Label(j, i+1, obj[j].toString(), wcf);  
                                sheet.addCell(labData); 
	                    	}
	                    }
	            	}else if(sheetData.get(0) instanceof Map){
	            		 // list中是Map  
	                    for (int i=0; i<sheetData.size(); i++) {
	                    	Map m = (Map) sheetData.get(i);
	                    	 Set ks = m.keySet();
	                    	 Iterator itr = ks.iterator();
	                    	 for (int j=0; itr.hasNext(); j++) {
	                    		 Object key = itr.next();
	                    		 sheet.setRowView(0, 400);
	                    		 Label labData = new Label(j, i+1, m.get(key).toString(), wcf);
	                    		 sheet.addCell(labData);
	                    	 }
	                    }
	            	}else{
	            		
	            	}
	            }
			}
		}
	      book.write();  
	      book.close(); 
	}
	public static void fontCenter(WritableCellFormat wcf) {
		try {
			wcf.setAlignment(Alignment.CENTRE); // 水平居中对齐
			wcf.setVerticalAlignment(VerticalAlignment.CENTRE); // 竖直方向居中对齐
			wcf.setBorder(Border.ALL, BorderLineStyle.THIN, jxl.format.Colour.BLACK);
		} catch (WriteException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 检验读入的excel头部
	 * @param readHeader 读入的头部
	 * @param rightHeaderMap 正确的头部
	 * @return 检验正确返回true，错误返回false
	 */
	public static boolean compareHeader(List<String> readHeader, Map<String, String> rightHeaderMap){ 
		if (readHeader.size() != rightHeaderMap.size()){
			return false;
		}
		int index = 0;
		for (String rightHeadName : rightHeaderMap.keySet()){
			if (!readHeader.get(index++).trim().equals(rightHeadName)){
				System.out.println("readHeader" + readHeader.get(--index));
				System.out.println(rightHeadName);
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 解析excel中的数据
	 * @param is 输入流
	 * @return List<List<List<String>>>  多个表集合->多行的集合->多列的集合
	 * @throws IOException 
	 * @throws BiffException 
	 */
	public static List<List<List<String>>> readExcel(InputStream is) throws Exception {
		List<List<List<String>>> fileContent = new ArrayList<List<List<String>>>();
		Workbook wb;
		wb = Workbook.getWorkbook(is);
		int sheetNumber = wb.getNumberOfSheets();
		for (int i = 0; i < sheetNumber; i++){
			Sheet sheet = wb.getSheet(i);
			List<List<String>> sheetConetent = new ArrayList<List<String>>();
			for (int j = 0; j < sheet.getRows(); j++){
				List<String> rowContent = new ArrayList<String>();
				//第j行所有列	
				for (int k = 0; k < sheet.getColumns(); k++){
					String content = sheet.getCell(k, j).getContents();
					if (!StringUtils.isBlank(content)){
						rowContent.add(content.trim());
					}else{
						rowContent.add("");
					}
				}
				sheetConetent.add(rowContent);
			}
			fileContent.add(sheetConetent);
		}	
		return fileContent;
	}
	
	/**
	 * 把excel中的数据行转换为实体类
	 * @param rowContent 行数据
	 * @param fieldNameMap 实体类的属性名
	 * @param entity 要转换的实体
	 * @throws Exception
	 */
	public static <T> void convertRowContentToEntity(List<String> rowContent, Map<String, String> fieldNameMap, 
			T entity) throws Exception{
		Class<?> c = entity.getClass();
		int index = 0;
		for (String fieldName : fieldNameMap.values()){
			Field field = c.getDeclaredField(fieldName);
			Object fieldValue = convertStringToObject(rowContent.get(index++), field.getType());
			BeanUtilsBean.getInstance().setProperty(entity, fieldName, fieldValue);
		}
	}

	/**
	 * 把字符串转换为对应实体属性与的类型
	 * @param src 要转换的字符串
	 * @param type 属性域类型
	 * @return 转换完成的对象
	 * @throws Exception
	 */
	public static Object convertStringToObject(String src, Class<?> type) throws Exception {
		// TODO Auto-generated method stub
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		String className = type.getName();
		Object result = null;
		switch (className) {
		case "java.lang.String":
			result = src;
			break;
		case "java.lang.Integer":
		case "int":
			result = Integer.parseInt(src);
			break;
		case "java.lang.Double":
		case "double":
			result = Double.parseDouble(src);
			break;
		case "java.lang.Float":
		case "float":
			result = Float.parseFloat(src);
			break;
		case "java.util.Date":
			if (src.indexOf("/") > -1){
				src = src.replace("/", "-");
			}
			result = sdf.parse(src);
			break;
		default:
			break;
		}
		return result;
	}
}
