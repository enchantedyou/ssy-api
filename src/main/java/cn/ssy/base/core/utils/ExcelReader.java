package cn.ssy.base.core.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import cn.ssy.base.entity.plugins.TwoTuple;


/**
 * Excel解析工具
 * 时间:2018/12/31
 * @author 孙绍禹
 *
 */
public class ExcelReader {

	//log4j日志
	private static final Logger logger = Logger.getLogger(ExcelReader.class);
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年3月30日-下午12:10:34</li>
	 *         <li>功能说明：获取excel中的所有数据</li>
	 *         </p>
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public static List<Map<String, Object>> getExcelInfo(String filePath){
		if(CommonUtil.isNull(filePath))
			return null;
		//获取Workbook对象
		Workbook workbook = null;
		try {
			workbook = getWorkbook(filePath);
		}
		catch (IOException e) {
			CommonUtil.printLogError(e, logger);
		}
		//结果数组表
		List<Map<String, Object>> resList = new LinkedList<Map<String, Object>>();
		// excel工作表
		List<Sheet> sheetList = getAllSheets(workbook);
		for(int index = 0;index < sheetList.size();index++){
			//获取最大行数
			Integer maxRowNum = sheetList.get(index).getPhysicalNumberOfRows();
			
			//获取第一行(一般为标题),以得出最大列数
			Row row = sheetList.get(index).getRow(0);
			if(CommonUtil.isNull(row))
				continue;
			//获取最大列数
			Integer maxColNum = row.getPhysicalNumberOfCells();
			//获取所有列的列名
			List<String> colNameList = getAllColNames(row);
			
			//遍历每一行的数据
			for(int i = 1;i <= maxRowNum;i++){
				//有序哈希表,用于存放每一行的数据
				Map<String, Object> rowMap = new LinkedHashMap<String, Object>();
				//获取第i+1行的数据
				row = sheetList.get(index).getRow(i);
				if(row != null){
					rowMap = getRowMap(row, maxColNum, colNameList);
					resList.add(rowMap);
				}else{
					continue;
				}
			}
		}
		return resList;
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年3月30日-下午12:08:32</li>
	 *         <li>功能说明：获取对应类型的cell中的值</li>
	 *         </p>
	 * @param cell	cell对象
	 * @return
	 */
	public static Object getCallObjectData(Cell cell){
		Object cellData = null;
		CellType cellType = cell.getCellTypeEnum();

		if(CellType.BLANK == cellType || CellType._NONE == cellType){
			cellData = "";
		}else if(CellType.BOOLEAN == cellType){
			cellData = cell.getBooleanCellValue();
		}else if(CellType.ERROR == cellType){
			cellData = cell.getErrorCellValue();
		}else if(CellType.FORMULA == cellType){
			cellData = cell.getCellFormula();
		}else if(CellType.NUMERIC == cellType){
			cellData = cell.getNumericCellValue();
		}else if(CellType.STRING == cellType){
			cellData = cell.getStringCellValue();
		}
		
		return cellData;
	}

	
	/**
	 * 根据excel文件类型获取Workbook对象
	 * 
	 * @param filePath
	 *            excel文件路径
	 * @return 
	 *         如果是xls类型的excel文件返回HSSFWorkbook对象,如果是xlsx类型的excel文件返回XSSFWorkbook对象
	 * @throws IOException
	 */
	public static Workbook getWorkbook(String filePath) throws IOException {
		if (CommonUtil.isNull(filePath)) {
			return null;
		}

		String excelSuffix = filePath.substring(filePath.lastIndexOf(".") + 1);
		FileInputStream in = null;
		in = new FileInputStream(new File(filePath));

		switch (excelSuffix) {
		case "xls":
			return new HSSFWorkbook(in);
		case "xlsx":
			return new XSSFWorkbook(in);
		default:
			return null;
		}
	}

	/**
	 * 获取一个工作簿中的所有工作空间
	 * 
	 * @param workbook
	 *            workbook对象
	 * @return 返回包含所有工作空间的list集合
	 */
	public static List<Sheet> getAllSheets(Workbook workbook) {
		if (null == workbook)
			throw new NullPointerException("workbook为空");
		List<Sheet> sheetList = new ArrayList<Sheet>();
		Iterator<Sheet> sheetIterator = workbook.sheetIterator();
		while(sheetIterator.hasNext()){
			sheetList.add(sheetIterator.next());
		}
		return sheetList;
	}
	
	
	
	/**
	 * 获取本行所有列的数据,一般用于获取列名
	 * @param row	行对象
	 * @return	返回含有所有列数据的list集合
	 */
	public static List<String> getAllColNames(Row row){
		if(CommonUtil.isNull(row)){
			return null;
		}else{
			List<String> colNameList = new ArrayList<String>();
			Iterator<Cell> cellIterator = row.iterator();
			while(cellIterator.hasNext()){
				colNameList.add(cellIterator.next().getStringCellValue());
			}
			return colNameList;
		}
	}
	
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月16日-上午9:56:24</li>
	 *         <li>功能说明：解析接口文档</li>
	 *         </p>
	 * @param filePath	文档路径
	 * @return
	 */
	public static TwoTuple<List<Map<String, Object>>, List<Map<String, Object>>> resolverIntfExcel(String filePath){
		if(CommonUtil.isNull(filePath))
			return null;
		//获取Workbook对象
		Workbook workbook = null;
		try {
			workbook = getWorkbook(filePath);
		}
		catch (IOException e) {
			CommonUtil.printLogError(e, logger);
		}
		//输入字段列表
		List<Map<String, Object>> inputList = new LinkedList<Map<String, Object>>();
		//输入字段列表
		List<Map<String, Object>> outputList = new LinkedList<Map<String, Object>>();
		
		// excel工作表
		List<Sheet> sheetList = getAllSheets(workbook);
		for(int index = 0;index < sheetList.size();index++){
			//如果工作表不是公用接口,则结束本次循环
			if(!sheetList.get(index).getSheetName().equals("公用接口")){
				continue;
			}
			
			//定义数据集获取标志
			boolean isInputStart = false;
			boolean isOutputStart = false;
			//定义输入输出扫描开始行号
			int inputStartRowNum = -1;
			int outputStartRowNum = -1;
			
			//获取最大行数
			Integer maxRowNum = sheetList.get(index).getPhysicalNumberOfRows();
			
			//获取第一行(一般为标题),以得出最大列数
			Row row = sheetList.get(index).getRow(0);
			if(CommonUtil.isNull(row))
				continue;
			//获取最大列数
			Integer maxColNum = row.getPhysicalNumberOfCells();
			
			//遍历每一行的数据
			for(int i = 1;i <= maxRowNum;i++){
				//获取第i+1行的数据
				row = sheetList.get(index).getRow(i);
				if(row != null){
					int curRowNum = i;
					
					//获取第0列的数据,指定起始扫描位置
					Cell cell = row.getCell(0);
					if(CommonUtil.isNotNull(cell)){
						Object cellData = getCallObjectData(cell);
						
						if("输入".equals(String.valueOf(cellData).replaceAll(" ", ""))){
							logger.info("标记输入行扫描位置");
							inputStartRowNum = curRowNum + 2;
						}else if("输出".equals(String.valueOf(cellData).replaceAll(" ", ""))){
							logger.info("标记输出行扫描位置");
							outputStartRowNum = curRowNum + 2;
						}
					}
					
					if(inputStartRowNum == i){
						logger.info("输入行扫描开始");
						isInputStart = true;
					}else if(outputStartRowNum == i){
						logger.info("输出行扫描开始");
						isOutputStart = true;
						isInputStart = false;
					}
					
					//输入扫描开始
					if(isInputStart){
						if(outputStartRowNum != -1 && i >= (outputStartRowNum - 2)){
							//标题栏跳过
							continue;
						}
						
						List<String> inputColList = getAllColNames(sheetList.get(index).getRow(inputStartRowNum -1));
						Map<String, Object> rowMap = getRowMap(row, maxColNum, inputColList);
						inputList.add(rowMap);
					}
					//输出扫描开始
					else if(isOutputStart){
						
						List<String> outputColList = getAllColNames(sheetList.get(index).getRow(outputStartRowNum -1));
						Map<String, Object> rowMap = getRowMap(row, maxColNum, outputColList);
						outputList.add(rowMap);
					}
				}
			}
		}
		return new TwoTuple<List<Map<String,Object>>, List<Map<String,Object>>>(inputList, outputList);
	}


	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月16日-上午11:16:01</li>
	 *         <li>功能说明：获取当前行map形式的数据</li>
	 *         </p>
	 * @param row	当前行对象
	 * @param maxColNum	最好列数
	 * @param colList	列名列表
	 */
	private static Map<String, Object> getRowMap(Row row, Integer maxColNum, List<String> colList) {
		Map<String, Object> rowMap = new HashMap<String, Object>();
		//遍历每一列的数据
		for(int j = 0;j < maxColNum;j++){
			Cell cell = row.getCell(j);
			if(CommonUtil.isNotNull(cell)){
				Object cellData = getCallObjectData(cell);
				String colName = colList.get(j);
				if(CommonUtil.isNotNull(cellData) && CommonUtil.isNotNull(colName)){
					rowMap.put(colName, cell == null ? "" : cellData);
				}
			}
		}
		return rowMap;
	}
	
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年9月29日-下午1:20:00</li>
	 *         <li>功能说明：写入网关接口Excel文档</li>
	 *         </p>
	 * @param filePath
	 * @return
	 * @throws Exception 
	 */
	public static void writeGatewayApi(String filePath,List<Map<String, String>> dataList) throws Exception{
		//获取Workbook对象
		Workbook workbook = getWorkbook(filePath);
		// excel工作表
		List<Sheet> sheetList = getAllSheets(workbook);
		if(CommonUtil.isNotNull(sheetList)){
			Sheet tamplateSheet = sheetList.get(0);
			Row row = tamplateSheet.getRow(0);
			
			//获取最大行数
			Integer maxRowNum = tamplateSheet.getPhysicalNumberOfRows();
			
			int curIndex = 1;
			for(Map<String, String> map : dataList){
				logger.info(map.toString());
				Sheet tmpSheet = workbook.cloneSheet(0);
				//遍历每一行的数据
				for(int i = 0;i <= maxRowNum;i++){
					//获取第i+1行的数据
					row = tmpSheet.getRow(i);
					//遍历前两列的数据
					if(CommonUtil.isNotNull(row)){
						Cell cell = row.getCell(0);
						if(CommonUtil.isNotNull(cell)){
							String colName = String.valueOf(getCallObjectData(cell));
							if(CommonUtil.isNotNull(map.get(colName))){
								row.getCell(1).setCellValue(map.get(colName));
							}
						}
					}
				}
				workbook.setSheetName(curIndex++, map.get("api"));
			}
			//删除第0个sheet
			workbook.removeSheetAt(0);
		}
		String outputPath = "C:/Users/36045/Desktop/api.xlsx";
	    FileOutputStream stream= FileUtils.openOutputStream(new File(outputPath));
	    workbook.write(stream);
	    stream.close();
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年10月28日-下午2:24:10</li>
	 *         <li>功能说明：生成接口文档</li>
	 *         </p>
	 * @param baseTypeMap	基础类型哈希表
	 * @param flowtranMap	flowtran信息哈希表
	 * @param fieldTwoTuple	输入输出字段二元组
	 * @param tamplatePath	接口文档模板Excel路径
	 * @param outputPath	文档输出路径
	 * @throws IOException 
	 */
	public static void writeIntfDocument(Map<String, Map<String, String>> baseTypeMap,Map<String, String> flowtranMap,TwoTuple<Map<String, String>, Map<String, String>> fieldTwoTuple,String tamplatePath,String outputPath) throws IOException{
		//获取Workbook对象
		Workbook workbook = getWorkbook(tamplatePath);
		Sheet tamplateSheet = workbook.cloneSheet(3);
		//第一行第二列设置交易码
		tamplateSheet.getRow(0).getCell(1).setCellValue(flowtranMap.get("id"));
		//第一行第四列设置交易类别
		switch(flowtranMap.get("kind")){
		case "M":tamplateSheet.getRow(0).getCell(7).setCellValue("普通维护交易");break;
		case "Q":tamplateSheet.getRow(0).getCell(7).setCellValue("查询交易");break;
		case "F":tamplateSheet.getRow(0).getCell(7).setCellValue("金融交易");break;
		case "P":tamplateSheet.getRow(0).getCell(7).setCellValue("参数维护交易");break;
		}
		//第二行第二列设置交易描述
		tamplateSheet.getRow(1).getCell(1).setCellValue(flowtranMap.get("longname"));
		
		int curRowNum = 6;
		int inputIndex = 0;
		int outputIndex = 0;
		int digit = 0;
		for(String key : fieldTwoTuple.getFirst().keySet()){
			Row curRow = tamplateSheet.createRow(curRowNum);
			boolean isList = false;
			if("list".equals(fieldTwoTuple.getFirst().get(key))){
				inputIndex++;
				digit = 0;
				isList = true;
			}else if(key.contains(".")){
				curRow.createCell(0).setCellValue(inputIndex + "." + (++digit));
				key = key.substring(key.lastIndexOf(".") + 1);
			}else{
				digit = 0;
				curRow.createCell(0).setCellValue(++inputIndex);
			}
			
			if(isList){
				curRow.createCell(1).setCellValue(key);
			}else{
				curRow.createCell(1).setCellValue(key);
				curRow.createCell(2).setCellValue(SunlineUtil.dictMap.get(key).getDesc());
				Map<String, String> baseTypeSubMap = baseTypeMap.get(CommonUtil.getRealType(SunlineUtil.dictMap.get(key).getRefType()));
				
				String baseType = CommonUtil.isNull(baseTypeSubMap) ? "string" : baseTypeSubMap.get("base");
				String baseMaxLength = CommonUtil.isNull(baseTypeSubMap) ? "20" : baseTypeSubMap.get("maxLength");
				String baseFractionDigits = CommonUtil.isNull(baseTypeSubMap) ? "" : baseTypeSubMap.get("fractionDigits");
				curRow.createCell(3).setCellValue(baseType);
				
				curRow.createCell(4).setCellValue(baseMaxLength);
				curRow.createCell(5).setCellValue(baseFractionDigits);
				curRow.createCell(6).setCellValue("O");
			}
			curRowNum++;
		}
		
		copyRows(5, 6, curRowNum, tamplateSheet);
		tamplateSheet.getRow(curRowNum).getCell(0).setCellValue("输                   出");
		tamplateSheet.getRow(curRowNum + 1).getCell(6).setCellValue("");
		curRowNum += 2;
		
		for(String key : fieldTwoTuple.getSecond().keySet()){
			Row curRow = tamplateSheet.createRow(curRowNum);
			boolean isList = false;
			if("list".equals(fieldTwoTuple.getSecond().get(key))){
				outputIndex++;
				isList = true;
				digit = 0;
			}else if(key.contains(".")){
				curRow.createCell(0).setCellValue(outputIndex + "." + (++digit));
				key = key.substring(key.lastIndexOf(".") + 1);
			}else{
				digit = 0;
				curRow.createCell(0).setCellValue(++outputIndex);
			}
			
			if(isList){
				curRow.createCell(1).setCellValue(key);
			}else{
				curRow.createCell(1).setCellValue(key);
				curRow.createCell(2).setCellValue(SunlineUtil.dictMap.get(key).getDesc());
				Map<String, String> baseTypeSubMap = baseTypeMap.get(CommonUtil.getRealType(SunlineUtil.dictMap.get(key).getRefType()));
				
				String baseType = CommonUtil.isNull(baseTypeSubMap) ? "string" : baseTypeSubMap.get("base");
				String baseMaxLength = CommonUtil.isNull(baseTypeSubMap) ? "20" : baseTypeSubMap.get("maxLength");
				String baseFractionDigits = CommonUtil.isNull(baseTypeSubMap) ? "" : baseTypeSubMap.get("fractionDigits");
				curRow.createCell(3).setCellValue(baseType);
				
				curRow.createCell(4).setCellValue(baseMaxLength);
				curRow.createCell(5).setCellValue(baseFractionDigits);
			}
			curRowNum++;
		}
		
		//移除后重命名
		workbook.removeSheetAt(3);
		workbook.setSheetName(3, workbook.getSheetName(3).substring(0,4));
		//写入文件
	    FileOutputStream stream= FileUtils.openOutputStream(new File(outputPath + "/" + flowtranMap.get("longname")+".xlsx"));
	    workbook.write(stream);
	    stream.close();
	}
	
	
	
	public static void copyRows(int startRow, int endRow, int pPosition,  
            Sheet sheet) {  
        int pStartRow = startRow - 1;  
        int pEndRow = endRow - 1;  
        int targetRowFrom;  
        int targetRowTo;  
        int columnCount;  
        CellRangeAddress region = null;  
        int i;  
        int j;  
        if (pStartRow == -1 || pEndRow == -1) {  
            return;  
        }  
        // 拷贝合并的单元格  
        for (i = 0; i < sheet.getNumMergedRegions(); i++) {  
            region = sheet.getMergedRegion(i);  
            if ((region.getFirstRow() >= pStartRow)  
                    && (region.getLastRow() <= pEndRow)) {  
                targetRowFrom = region.getFirstRow() - pStartRow + pPosition;  
                targetRowTo = region.getLastRow() - pStartRow + pPosition;  
                CellRangeAddress newRegion = region.copy();  
                newRegion.setFirstRow(targetRowFrom);  
                newRegion.setFirstColumn(region.getFirstColumn());  
                newRegion.setLastRow(targetRowTo);  
                newRegion.setLastColumn(region.getLastColumn());  
                sheet.addMergedRegion(newRegion);  
            }  
        }  
        // 设置列宽  
        for (i = pStartRow; i <= pEndRow; i++) {  
            Row sourceRow = sheet.getRow(i);  
            columnCount = sourceRow.getLastCellNum();  
            if (sourceRow != null) {  
            	Row newRow = sheet.createRow(pPosition - pStartRow + i);  
                newRow.setHeight(sourceRow.getHeight());  
                for (j = 0; j < columnCount; j++) {  
                    Cell templateCell = sourceRow.getCell(j);  
                    if (templateCell != null) {  
                    	Cell newCell = newRow.createCell(j);  
                        copyCell(templateCell, newCell);  
                    }  
                }  
            }  
        }  
    }  
  
  
	@SuppressWarnings("deprecation")
	private static void copyCell(Cell srcCell, Cell distCell) {  
        distCell.setCellStyle(srcCell.getCellStyle());  
        if (srcCell.getCellComment() != null) {  
            distCell.setCellComment(srcCell.getCellComment());  
        }  
        int srcCellType = srcCell.getCellType();  
        distCell.setCellType(srcCellType);  
        if (srcCellType == HSSFCell.CELL_TYPE_NUMERIC) {  
            if (HSSFDateUtil.isCellDateFormatted(srcCell)) {  
                distCell.setCellValue(srcCell.getDateCellValue());  
            } else {  
                distCell.setCellValue(srcCell.getNumericCellValue());  
            }  
        } else if (srcCellType == HSSFCell.CELL_TYPE_STRING) {  
            distCell.setCellValue(srcCell.getRichStringCellValue());  
        } else if (srcCellType == HSSFCell.CELL_TYPE_BLANK) {  
            // nothing21  
        } else if (srcCellType == HSSFCell.CELL_TYPE_BOOLEAN) {  
            distCell.setCellValue(srcCell.getBooleanCellValue());  
        } else if (srcCellType == HSSFCell.CELL_TYPE_ERROR) {  
            distCell.setCellErrorValue(srcCell.getErrorCellValue());  
        } else if (srcCellType == HSSFCell.CELL_TYPE_FORMULA) {  
            distCell.setCellFormula(srcCell.getCellFormula());  
        } else { // nothing29  
  
        }  
    }  
}
