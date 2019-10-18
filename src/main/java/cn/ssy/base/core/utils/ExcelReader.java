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
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
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
					//遍历前两列列的数据
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
}
