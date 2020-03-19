package cn.ssy.base.core.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.log4j.Logger;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import cn.ssy.base.core.network.api.NetworkApi;
import cn.ssy.base.entity.consts.ApiConst;
import cn.ssy.base.entity.mybatis.LnaLoan;
import cn.ssy.base.entity.mybatis.MspTransaction;
import cn.ssy.base.entity.mybatis.SmpSysDict;
import cn.ssy.base.entity.mybatis.TspServiceIn;
import cn.ssy.base.entity.plugins.TwoTuple;
import cn.ssy.base.entity.sunline.BaseType;
import cn.ssy.base.entity.sunline.Dict;
import cn.ssy.base.entity.sunline.EnumElement;
import cn.ssy.base.entity.sunline.EnumType;
import cn.ssy.base.entity.sunline.Max;
import cn.ssy.base.entity.sunline.Required;
import cn.ssy.base.enums.E_ICOREMODULE;
import cn.ssy.base.enums.E_LANGUAGE;
import cn.ssy.base.enums.E_LAYOUTTYPE;
import cn.ssy.base.enums.E_STRUCTMODULE;
import cn.ssy.base.exception.ConfigSettingException;
import cn.ssy.base.exception.NullParmException;

/**
 * <p>
 * 文件功能说明：
 *       	长亮开发的工具类		
 * </p>
 * 
 * @Author sunshaoyu
 *         <p>
 *         <li>2019年7月29日-下午4:21:21</li>
 *         <li>修改记录</li>
 *         <li>-----------------------------------------------------------</li>
 *         <li>标记：修订内容</li>
 *         <li>2019年7月29日-sunshaoyu：创建注释模板</li>
 *         <li>-----------------------------------------------------------</li>
 *         </p>
 */
public class SunlineUtil {
	
	//项目文件hash表,存储Java代码文件和Xml配置文件
	public static Map<String, String> projectFileMap = new LinkedHashMap<String, String>();
	//项目字典
	public static Map<String, Dict> dictMap = new LinkedHashMap<String, Dict>();
	//被使用到的枚举
	public static Map<String, EnumType> enumMap = new LinkedHashMap<String, EnumType>();
	//基础引用类型
	public static Map<String, BaseType> baseTypeMap = new LinkedHashMap<String, BaseType>();
	//字典优先级
	private static Map<String, String> dictPriorityMap = new LinkedHashMap<String, String>();
	//枚举优先级
	private static Map<String, String> enumPriorityMap = new LinkedHashMap<String, String>();
	//内管枚举(用于获取枚举值的中文描述)
	public static Map<String, SmpSysDict> ctEnumMap = new LinkedHashMap<String, SmpSysDict>();
	//log4j日志
	private static final Logger logger = Logger.getLogger(SunlineUtil.class);
	//redis操作工具
	private static final RedisOperateUtil redisOperateUtil = new RedisOperateUtil();
	//字段引用字典校验缓存
	private static final StringBuffer fieldRefReportBuffer = new StringBuffer();
	
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月5日-上午11:13:15</li>
	 *         <li>功能说明：工具初始化</li>
	 *         </p>
	 * @param projectPath	项目目录路径
	 * @param redisFirst	优先从redis缓存中获取数据
	 * @throws SQLException 
	 */
	@SuppressWarnings("unchecked")
	public static void sunlineInitializer(String projectPath,boolean redisFirst) throws SQLException{
		if(!redisFirst){
			redisOperateUtil.delete(ApiConst.REDIS_PROJECT_FILE_KEY);
			redisOperateUtil.delete(ApiConst.REDIS_PROJECT_DICT_KEY);
			redisOperateUtil.delete(ApiConst.REDIS_PROJECT_ENUM_KEY);
			redisOperateUtil.delete(ApiConst.REDIS_PROJECT_BASETYPE_KEY);
			redisOperateUtil.delete(ApiConst.REDIS_CT_DICT_KEY);
		}
		
		//重新加载字典和枚举标志
		if(CommonUtil.isNotNull(projectPath) && CommonUtil.isNull(projectFileMap)){
			//初始化项目文件
			projectFileMap = (Map<String, String>) redisOperateUtil.getHashEntries(ApiConst.REDIS_PROJECT_FILE_KEY);
			if(!redisFirst || CommonUtil.isNull(projectFileMap)){
				loadProjectFile(new File(projectPath));
				redisOperateUtil.pushAllAsHash(ApiConst.REDIS_PROJECT_FILE_KEY, projectFileMap, ApiConst.REDIS_DEFAULT_TIMEOUT_SEC);
			}
			logger.info("初始化项目文件完成>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		}
		//读取字典和枚举优先级
		Map<String, String> tmpDictPriorityMap = CommonUtil.readPropertiesSettings(SunlineUtil.class.getResource("/priority/dictPriority.properties").getPath());
		Map<String, String> tmpEnumPriorityMap = CommonUtil.readPropertiesSettings(SunlineUtil.class.getResource("/priority/enumPriority.properties").getPath());
		if(CommonUtil.isNotNull(tmpDictPriorityMap)){
			//初始化字典优先级
			SunlineUtil.dictPriorityMap = tmpDictPriorityMap;
			logger.info("初始化字典优先级完成>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		}
		if(CommonUtil.isNotNull(tmpEnumPriorityMap)){
			//初始化枚举优先级
			SunlineUtil.enumPriorityMap = tmpEnumPriorityMap;
			logger.info("初始化枚举优先级完成>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		}
		//优先级检查
		checkPrioritySetting();
		if(CommonUtil.isNull(dictMap)){
			//初始化项目字典
			dictMap = (Map<String, Dict>) redisOperateUtil.getHashEntries(ApiConst.REDIS_PROJECT_DICT_KEY);
			if(!redisFirst || CommonUtil.isNull(dictMap)){
				loadProjectDict();
				redisOperateUtil.pushAllAsHash(ApiConst.REDIS_PROJECT_DICT_KEY, dictMap, ApiConst.REDIS_DEFAULT_TIMEOUT_SEC);
			}
			logger.info("初始化项目字典完成>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		}
		if(CommonUtil.isNull(enumMap)){
			//初始化项目枚举
			enumMap = (Map<String, EnumType>) redisOperateUtil.getHashEntries(ApiConst.REDIS_PROJECT_ENUM_KEY);
			if(!redisFirst || CommonUtil.isNull(enumMap)){
				loadProjectEnum();
				redisOperateUtil.pushAllAsHash(ApiConst.REDIS_PROJECT_ENUM_KEY, enumMap, ApiConst.REDIS_DEFAULT_TIMEOUT_SEC);
			}
			logger.info("初始化项目枚举完成>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		}
		if(CommonUtil.isNull(baseTypeMap)){
			//初始化基础类型
			baseTypeMap = (Map<String, BaseType>) redisOperateUtil.getHashEntries(ApiConst.REDIS_PROJECT_BASETYPE_KEY);
			if(!redisFirst || CommonUtil.isNull(baseTypeMap)){
				loanProjectBaseType();
				redisOperateUtil.pushAllAsHash(ApiConst.REDIS_PROJECT_BASETYPE_KEY, baseTypeMap, ApiConst.REDIS_DEFAULT_TIMEOUT_SEC);
			}
			logger.info("初始化基础类型完成>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		}
		if(CommonUtil.isNull(ctEnumMap)){
			//初始化内管枚举
			ctEnumMap = (Map<String, SmpSysDict>) redisOperateUtil.getHashEntries(ApiConst.REDIS_CT_DICT_KEY);
			if(!redisFirst || CommonUtil.isNull(ctEnumMap)){
				loanCtDict();
				redisOperateUtil.pushAllAsHash(ApiConst.REDIS_CT_DICT_KEY, ctEnumMap, ApiConst.REDIS_DEFAULT_TIMEOUT_SEC);
			}
			logger.info("初始化内管枚举完成>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		}
		CommonUtil.printSplitLine(150);
		logger.info("项目文件数量:" + projectFileMap.size());
		logger.info("项目字典数量:" + dictMap.size());
		logger.info("项目枚举数量:" + enumMap.size());
		
		logger.info("基础类型数量:" + baseTypeMap.size());
		logger.info("内管枚举数量:" + ctEnumMap.size());
		CommonUtil.printSplitLine(150);
		
		if(redisFirst){
			logger.info("Redis缓存数据占用内存:" + redisOperateUtil.getRedisInfo().get("used_memory_human"));
		}
		CommonUtil.printSplitLine(150);
	}
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年7月29日-下午4:31:07</li>
	 *         <li>功能说明：加载有效项目文件</li>
	 *         </p>
	 * @param file
	 */
	private static void loadProjectFile(File file) {
		if (file != null) {
			if (file.isDirectory()) {
				// 列举此目录下所有文件及文件夹
				File[] list = file.listFiles();
				for (int i = 0; i < list.length; i++) {
					loadProjectFile(list[i]);
				}
			} else {
				if((CommonUtil.isRegexMatches("^.*?xml$", file.getName())
				 || CommonUtil.isRegexMatches("^.*?java$", file.getName()))
				 && file.getPath().contains("src")){
					projectFileMap.put(file.getName(), file.getPath());
				}else if(CommonUtil.isRegexMatches("^.*?xlsx$", file.getName()) && file.getPath().contains(ApiConst.FULLSQL_MAINDIR_NAME)){
					projectFileMap.put(file.getName(), file.getPath());
				}
			}
		}
	}
	
	
	/**
	 * @throws SQLException 
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年11月14日-下午3:37:25</li>
	 *         <li>功能说明：加载内管枚举</li>
	 *         </p>
	 */
	private static void loanCtDict() throws SQLException {
		List<SmpSysDict> dictList = CommonUtil.mappingResultSetList(JDBCUtils.executeQuery("select * from smp_sys_dict", ApiConst.DATASOURCE_ICORE_CT_DIT), SmpSysDict.class);
		for(SmpSysDict dict : dictList){
			ctEnumMap.put(dict.getDictType() + "." + dict.getDictId(), dict);
		}
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年11月12日-下午3:56:33</li>
	 *         <li>功能说明：检查优先级设置(字典和枚举)</li>
	 *         </p>
	 */
	private static void checkPrioritySetting(){
		//检查字典优先级设置
		for(String dictType : dictPriorityMap.keySet()){
			String priorityValue = dictPriorityMap.get(dictType);
			if(!(projectFileMap.containsKey(dictType + ApiConst.DICTFILE_SUFFIX) || !CommonUtil.isRegexMatches("^[-\\+]?[\\d]*$", priorityValue))){
				throw new ConfigSettingException("[字典优先级]" + dictType + "-" + priorityValue);
			}
		}
		//检查枚举优先级设置
		for(String enumType : enumPriorityMap.keySet()){
			String priorityValue = enumPriorityMap.get(enumType);
			if(!(projectFileMap.containsKey(enumType + ApiConst.ENUMFILE_SUFFIX) || !CommonUtil.isRegexMatches("^[-\\+]?[\\d]*$", priorityValue))){
				throw new ConfigSettingException("[枚举优先级]" + enumType + "-" + priorityValue);
			}
		}
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年7月31日-下午1:30:10</li>
	 *         <li>功能说明：获取默认字典优先级哈希表</li>
	 *         </p>
	 * @return
	 */
	private static Map<String, String> getDefaultDictPriorityMap(){
		Map<String, String> map = new HashMap<>();
		map.put("MsDict", "1");
		map.put("SysDict", "2");
		/*map.put("ApDict", "3");
		
		map.put("ApBaseDict", "4");
		map.put("CtBaseDict", "5");*/
		map.put("LnSysDict", "6");
		map.put("LnBaseDict", "7");
		
		map.put("LnIobusDict", "8");
		map.put("LnServDict", "9");
		map.put("CoServDict", "9");
		return map;
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月1日-上午11:04:23</li>
	 *         <li>功能说明：获取枚举优先级哈希表</li>
	 *         </p>
	 * @return
	 */
	private static Map<String, String> getDefaultEnumPriorityMap(){
		Map<String, String> map = new HashMap<>();
		map.put("MsEnumType", "1");
		map.put("EnumType", "2");
		map.put("LnBaseEnumType", "3");
		
		map.put("LnSysEnumType", "4");
		map.put("LnIobusEnumType", "5");
		map.put("LnServEnumType", "6");
		map.put("CoServEnumType", "6");
		
		return map;
	}
	
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月1日-下午2:27:24</li>
	 *         <li>功能说明：加载项目枚举</li>
	 *         </p>
	 */
	@SuppressWarnings("unchecked")
	private static void loadProjectEnum(){
		if(CommonUtil.isNotNull(projectFileMap)){
			enumMap.clear();
			Map<String, String> priorityMap = CommonUtil.nvl(enumPriorityMap, getDefaultEnumPriorityMap());
			for(String fileName : projectFileMap.keySet()){
				//从枚举中解析
				if(CommonUtil.isRegexMatches("^.*?EnumType.*.xml$", fileName)){
					//解析枚举xml
					Document doc = CommonUtil.getXmlDocument(projectFileMap.get(fileName));
					Element root = doc.getRootElement();
					List<Element> restrictionTypeList = root.elements();
					
					for(Element restrictionType : restrictionTypeList){
						if("restrictionType".equals(restrictionType.getName())){
							String enumId = restrictionType.attributeValue("id");
							EnumType beforeEnumType = enumMap.get(enumId);
							
							//设置新枚举信息
							EnumType curEnumType = new EnumType();
							curEnumType.setBase(restrictionType.attributeValue("base"));
							curEnumType.setEnumId(enumId);
							curEnumType.setEnumLocation(CommonUtil.getFirstDotLeftStr(fileName));
							curEnumType.setLongname(restrictionType.attributeValue("longname"));
							curEnumType.setMaxLength(restrictionType.attributeValue("maxLength"));
							curEnumType.setFullName(curEnumType.getEnumLocation() + "." + curEnumType.getEnumId());
							
							//设置枚举元素信息
							List<Element> enumerationList = restrictionType.elements();
							for(Element enumeration : enumerationList){
								if("enumeration".equals(enumeration.getName())){
									EnumElement enumElement = new EnumElement(enumeration.attributeValue("id"), enumeration.attributeValue("value"), enumeration.attributeValue("longname"));
									curEnumType.addEnumElement(enumElement);
								}
							}
							
							//枚举重复性校验
							if(CommonUtil.isNull(beforeEnumType) || CommonUtil.compare(priorityMap.get(curEnumType.getEnumLocation()),priorityMap.get(beforeEnumType.getEnumLocation())) < 0){
								enumMap.put(enumId, curEnumType);
								if(CommonUtil.isNotNull(beforeEnumType)){
									logger.warn("枚举["+beforeEnumType.getFullName()+"]优先级低于["+curEnumType.getFullName()+"],应当被移除");
								}
							}else if(CommonUtil.compare(priorityMap.get(curEnumType.getEnumLocation()),priorityMap.get(beforeEnumType.getEnumLocation())) == 0){
								logger.warn("枚举["+beforeEnumType.getFullName()+"]优先级等于["+curEnumType.getFullName()+"],应当被移到公用系统部分");
							}
						}
					}
				}
			}
			CommonUtil.printSplitLine(200);
		}
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月1日-下午2:12:35</li>
	 *         <li>功能说明：字典引用类型校验,保证字典总是引用的最高优先级的枚举(校验字典)</li>
	 *         </p>
	 * @param outputPath	输出路径
	 */
	public static void sunlineEnumRefValidation(String outputPath){
		if(CommonUtil.isNull(outputPath)){
			return;
		}else{
			StringBuffer reportBuffer = new StringBuffer();
			for(String fileName : projectFileMap.keySet()){
				//从字典中解析
				if(CommonUtil.isRegexMatches("^.*?Dict.*.xml$", fileName)){
					Document doc = CommonUtil.getXmlDocument(projectFileMap.get(fileName));
					Element root = doc.getRootElement();
					List<Element> elementList = CommonUtil.searchXmlElementList(root, "complexType");
					if(CommonUtil.isNull(elementList)){
						continue;
					}
					boolean isChanged = false;
					logger.info("校验:"+fileName);
					
					for(Element element : elementList){
						if(CommonUtil.isNull(element.attributeValue("type")) || !element.attributeValue("type").contains(".")){
							continue;
						}
						Attribute typeAttribute = element.attribute("type");
						String realEnumValue = CommonUtil.getRealType(typeAttribute.getValue());
						String dictId = element.attributeValue("id");
						String curRefEnumTypeLocation = CommonUtil.getFirstDotLeftStr(typeAttribute.getValue());
						EnumType trueRefEnumType = enumMap.get(realEnumValue);
						
						if(CommonUtil.isNotNull(curRefEnumTypeLocation)
						&& !curRefEnumTypeLocation.equals("MsEnumType")
						&& CommonUtil.isNotNull(trueRefEnumType)
						&& CommonUtil.compare(trueRefEnumType.getEnumLocation(), curRefEnumTypeLocation) != 0){
							//字典的枚举引用类型不正确,替换
							typeAttribute.setValue(typeAttribute.getValue().replace(curRefEnumTypeLocation, trueRefEnumType.getEnumLocation()));
							isChanged = true;
							String replaceMsg = "元素["+dictId+"]的引用类型发生更替:"+curRefEnumTypeLocation+"."+realEnumValue+"->"+trueRefEnumType.getEnumLocation()+"."+realEnumValue;
							logger.error(replaceMsg);
							reportBuffer.append("["+fileName+"]").append(replaceMsg).append("\r\n");
						}
					}
					
					if(isChanged){
						//写入到文件
						CommonUtil.writeDocumentXml(doc, outputPath + "/" + fileName);
						String line = CommonUtil.buildSplitLine(200);
						logger.info(line);
						reportBuffer.append(line).append("\r\n");
					}
				}
			}
			CommonUtil.writeFileContent(reportBuffer.toString(), outputPath + "/dictRefReporter.info");
		}
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年7月31日-上午9:33:36</li>
	 *         <li>功能说明：加载项目字典</li>
	 *         </p>
	 */
	private static void loadProjectDict(){
		if(CommonUtil.isNotNull(projectFileMap)){
			dictMap.clear();
			Map<String, String> priorityMap = CommonUtil.nvl(dictPriorityMap, getDefaultDictPriorityMap());
			for(String fileName : projectFileMap.keySet()){
				//从字典中解析
				if(CommonUtil.isRegexMatches("^.*?Dict.*.xml$", fileName)){
					Element root = CommonUtil.getXmlRootElement(projectFileMap.get(fileName));
					List<Element> elementList = CommonUtil.searchXmlElementList(root, "complexType");
					
					if(CommonUtil.isNull(elementList)){
						continue;
					}
					for(Element element : elementList){
						String type = element.attributeValue("type");
						String dictType = CommonUtil.getFirstDotLeftStr(fileName);
						String id = element.attributeValue("id");
						Dict beforeDictInfo = dictMap.get(id);
						Dict dictInfo = new Dict(id, dictType, element.attributeValue("longname"), type, element.attributeValue("desc"));
						
						//字典重复性校验
						if(CommonUtil.isNull(beforeDictInfo) || CommonUtil.compare(priorityMap.get(dictType),priorityMap.get(beforeDictInfo.getDictType())) < 0){
							dictMap.put(id, dictInfo);
							if(CommonUtil.isNotNull(beforeDictInfo)){
								logger.warn("字典["+beforeDictInfo.getDictType()+"."+id+"]优先级低于["+dictType+"."+id+"],应当被移除");
							}
						}
					}
				}
			}
			CommonUtil.printSplitLine(150);
		}
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年10月30日-下午1:30:12</li>
	 *         <li>功能说明：加载项目基础引用类型</li>
	 *         </p>
	 */
	@SuppressWarnings("unchecked")
	private static void loanProjectBaseType(){
		if(CommonUtil.isNotNull(projectFileMap)){
			Element baseTypeRoot = CommonUtil.getXmlRootElement(projectFileMap.get("BaseType.u_schema.xml"));
			Element msTypeRoot = CommonUtil.getXmlRootElement(projectFileMap.get("MsType.u_schema.xml"));
			List<Element> rootList = new ArrayList<>();
			rootList.add(msTypeRoot);
			rootList.add(baseTypeRoot);
			
			for(Element root : rootList){
				List<Element> restrictionTypeList = root.elements();
				for(Element restrictionType : restrictionTypeList){
					BaseType baseType = new BaseType();
					baseType.setId(restrictionType.attributeValue("id"));
					baseType.setBase(restrictionType.attributeValue("base"));
					baseType.setLongname(restrictionType.attributeValue("longname"));
					
					baseType.setMaxLength(restrictionType.attributeValue("maxLength"));
					baseType.setFractionDigits(restrictionType.attributeValue("fractionDigits"));
					baseTypeMap.put(restrictionType.attributeValue("id"), baseType);
				}
			}
		}
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年7月31日-上午10:57:26</li>
	 *         <li>功能说明：根据map替换文件内容</li>
	 *         </p>
	 * @param inputFilePath
	 * @param outputPath
	 * @param map
	 */
	public static void sunlineReplaceStrByMap(String inputFilePath,String outputPath,Map<String, String> map){
		if(CommonUtil.isNull(inputFilePath) || CommonUtil.isNull(outputPath) || CommonUtil.isNull(map)){
			return;
		}else{
			String content = CommonUtil.readFileContent(inputFilePath);
			for(String before : map.keySet()){
				content = content.replaceAll(before, map.get(before));
			}
			CommonUtil.writeFileContent(content, outputPath + File.separator + new File(inputFilePath).getName());
		}
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年7月30日-下午5:19:50</li>
	 *         <li>功能说明：在文件的头部插入内容,根据字符串数组的顺序插入,每一个字符串算作一行</li>
	 *         </p>
	 * @param dirPath	源文件目录
	 * @param content	要插入的字符串或数组
	 */
	public static void sunlineInsertHeadContent(String dirPath,String... content){
		if(CommonUtil.isNull(dirPath) || CommonUtil.isNull(content)){
			return;
		}else{
			File[] fileArray = new File(dirPath).listFiles();
			for(File file : fileArray){
				if(file.isDirectory()){
					sunlineInsertHeadContent(file.getPath(),content);
				}else{
					String fileContent = CommonUtil.readFileContent(file.getPath());
					for(int i = content.length - 1;i >= 0 ;i--){
						fileContent = content[i] + "\r\n" + fileContent;
					}
					CommonUtil.writeFileContent(fileContent, file.getPath());
				}
			}
		}
	}
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年7月31日-上午9:11:56</li>
	 *         <li>功能说明：字典引用校验(校验复合类型、flowtran、table、报表),保证字段引用的字典属于最高优先级</li>
	 *         </p>
	 * @param outputPath
	 */
	@SuppressWarnings("unchecked")
	public static void sunlineDictRefValidation(String outputPath){
		if(CommonUtil.isNull(outputPath)){
			return;
		}else{
			for(String fileName : projectFileMap.keySet()){
				if(!"xml".equals(CommonUtil.getRealType(fileName))){
					continue;
				}
				Map<String, String> resMap = new HashMap<>();
				Document doc = CommonUtil.getXmlDocument(projectFileMap.get(fileName));
				Element root = doc.getRootElement();
				List<Element> xmlElementList = root.elements();
				//校验flowtran
				if(CommonUtil.isRegexMatches("^.*?flowtrans.*.xml$", fileName)){
					logger.info("校验:"+fileName);
					resMap = traversingFlowtranXml(xmlElementList);
				}
				//校验table
				else if(CommonUtil.isRegexMatches("^Tab.*?tables.*.xml$", fileName)){
					logger.info("校验:"+fileName);
					resMap = traversingTableXml(xmlElementList);
				}
				//校验复合类型
				else if(CommonUtil.isRegexMatches("^Com.*?.xml$", fileName)){
					logger.info("校验:"+fileName);
					resMap = traversingComplexXml(xmlElementList);
				}
				//校验报表
				else if(CommonUtil.isRegexMatches("^.*?TellerTransactionReport.*.xml$",fileName)){
					logger.info("校验:"+fileName);
					resMap = traversingFlowtranXml(xmlElementList);
				}
				if(CommonUtil.isNotNull(resMap)){
					//写入文件
					sunlineReplaceStrByMap(projectFileMap.get(fileName), outputPath, resMap);
					String line = CommonUtil.buildSplitLine(200);
					logger.info(line);
					fieldRefReportBuffer.append("<--------").append(fileName).append("校验结束-------->\r\n");
					fieldRefReportBuffer.append(line).append("\r\n");
				}
			}
			CommonUtil.writeFileContent(fieldRefReportBuffer.toString(), outputPath + "/fieldRefReporter.info");
		}
	}
	
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年7月31日-上午9:57:25</li>
	 *         <li>功能说明：遍历flowtran的字段</li>
	 *         </p>
	 * @param flowtranEleList
	 */
	private static Map<String, String> traversingFlowtranXml(List<Element> flowtranEleList){
		Map<String, String> resMap = new HashMap<>();
		
		for(Element flowtranElement : flowtranEleList){
			List<Element> fieldList = CommonUtil.searchTargetAllXmlElement(flowtranElement, "field");
			for(Element field : fieldList){
				resMap.putAll(verifyFieldElement(field));
			}
		}
		return resMap;
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年7月31日-上午10:03:53</li>
	 *         <li>功能说明：遍历table</li>
	 *         </p>
	 * @param tableList
	 */
	private static Map<String, String> traversingTableXml(List<Element> tableList){
		Map<String, String> resMap = new HashMap<>();
		for(Element table : tableList){
			List<Element> fieldList = CommonUtil.searchTargetAllXmlElement(table, "field");
			for(Element field : fieldList){
				resMap.putAll(verifyFieldElement(field));
			}
		}
		return resMap;
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年7月31日-上午10:00:37</li>
	 *         <li>功能说明：遍历复合类型字段</li>
	 *         </p>
	 * @param ComplexList
	 */
	private static Map<String, String> traversingComplexXml(List<Element> ComplexList){
		Map<String, String> resMap = new HashMap<>();
		for(Element complexType : ComplexList){
			List<Element> fieldList = CommonUtil.searchTargetAllXmlElement(complexType, "element");
			for(Element field : fieldList){
				resMap.putAll(verifyFieldElement(field));
			}
		}
		return resMap;
	}
	
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年7月31日-上午9:24:26</li>
	 *         <li>功能说明：校验每一个字段元素</li>
	 *         </p>
	 * @param element
	 */
	private static Map<String, String> verifyFieldElement(Element element){
		Map<String, String> resMap = new HashMap<>();
		if(CommonUtil.isNull(element)){
			return resMap;
		}else{
			String id = element.attributeValue("id");
			Dict dictInfo = dictMap.get(id);
			
			if(CommonUtil.isNotNull(dictInfo)){
				
				Element beforeElement = element.createCopy();
				String dictName = dictInfo.getDictType();
				String enumType = dictInfo.getRefType();
				Attribute refAttribute = element.attribute("ref");
				Attribute typeAttribute = element.attribute("type");
				
				if(CommonUtil.isNull(dictName) || CommonUtil.isNull(enumType)
					|| CommonUtil.isNull(refAttribute) || CommonUtil.isNull(typeAttribute)
					|| element.asXML().contains("MsDict")){
					return resMap;
				}else{
					if(!dictName.equals(CommonUtil.getFirstDotLeftStr(refAttribute.getValue()))){
						String refValue = refAttribute.getValue();
						refAttribute.setValue(refValue.replace(CommonUtil.getFirstDotLeftStr(refValue), dictName));
						String msg = "元素["+id+"]的字典发生更替:"+refValue+"->"+refAttribute.getValue();
						logger.info(msg);
						fieldRefReportBuffer.append(msg).append("\r\n");
					}
					if(!enumType.equals(typeAttribute.getValue())){
						String typeValue = typeAttribute.getValue();
						typeAttribute.setValue(enumType);
						String msg = "元素["+id+"]的枚举发生更替:"+typeValue+"->"+enumType;
						logger.info(msg);
						fieldRefReportBuffer.append(msg).append("\r\n");
					}
					if(!beforeElement.asXML().equals(element.asXML())){
						resMap.put(beforeElement.asXML(), element.asXML());
					}
				}
			}
		}
		return resMap;
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月7日-上午10:15:34</li>
	 *         <li>功能说明：功能说明：获取flowtran的document列表</li>
	 *         </p>
	 * @param module
	 * @return
	 */
	public static List<Document> sunlineGetFlowtranDocList(E_ICOREMODULE module){
		if(CommonUtil.isNull(module)){
			return null;
		}
		List<Document> flowtranList = new ArrayList<Document>();
		
		for(String fileName : projectFileMap.keySet()){
			if(CommonUtil.isRegexMatches("^"+module.getId()+".*?flowtrans.*.xml$", fileName)){
				Document doc = CommonUtil.getXmlDocument(projectFileMap.get(fileName));
				flowtranList.add(doc);
			}
		}
		return flowtranList;
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月7日-上午10:17:49</li>
	 *         <li>功能说明：获取复合类型的document列表</li>
	 *         </p>
	 * @param module
	 * @return
	 */
	public static List<Document> sunlineGetComplexTypeDocList(E_ICOREMODULE module){
		if(CommonUtil.isNull(module)){
			return null;
		}
		List<Document> complexTypeList = new ArrayList<Document>();
		
		for(String fileName : projectFileMap.keySet()){
			if(CommonUtil.isRegexMatches("^Com"+module.getUpperId()+".*?.xml$", fileName)){
				Document doc = CommonUtil.getXmlDocument(projectFileMap.get(fileName));
				complexTypeList.add(doc);
			}
		}
		return complexTypeList;
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月7日-上午10:22:00</li>
	 *         <li>功能说明：获取表配置的document列表</li>
	 *         </p>
	 * @param module
	 * @return
	 */
	public static List<Document> sunlineGetTableDocList(E_ICOREMODULE module){
		if(CommonUtil.isNull(module)){
			return null;
		}
		List<Document> tableList = new ArrayList<Document>();
		
		for(String fileName : projectFileMap.keySet()){
			if(CommonUtil.isRegexMatches("^Tab"+module.getUpperId()+".*?tables.*.xml$", fileName)){
				Document doc = CommonUtil.getXmlDocument(projectFileMap.get(fileName));
				tableList.add(doc);
			}
		}
		return tableList;
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月7日-上午10:23:03</li>
	 *         <li>功能说明：获取报表的document列表</li>
	 *         </p>
	 * @param module
	 * @return
	 */
	public static List<Document> sunlineGetTransactionReportDocList(E_ICOREMODULE module){
		if(CommonUtil.isNull(module)){
			return null;
		}
		List<Document> transactionReportList = new ArrayList<Document>();
		
		for(String fileName : projectFileMap.keySet()){
			if(CommonUtil.isRegexMatches("^"+module.getId()+".*?TellerTransactionReport.*.xml$", fileName)){
				Document doc = CommonUtil.getXmlDocument(projectFileMap.get(fileName));
				transactionReportList.add(doc);
			}
		}
		return transactionReportList;
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2020年3月12日-上午10:37:34</li>
	 *         <li>功能说明：获取字段的基础类型</li>
	 *         </p>
	 * @param fieldId	字段id
	 * @return	返回基础类型实体类
	 */
	public static BaseType sunlineGetFieldBaseType(String fieldId){
		try{
			return SunlineUtil.baseTypeMap.get(CommonUtil.getRealType(SunlineUtil.dictMap.get(fieldId).getRefType()));
		}catch(Exception e){
		}
		return null;
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2020年3月12日-上午11:36:54</li>
	 *         <li>功能说明：获取字段的枚举类型</li>
	 *         </p>
	 * @param fieldId	字段id
	 * @return	返回枚举类型实体类
	 */
	public static EnumType sunlineGetFieldEnumType(String fieldId){
		try{
			return enumMap.get(CommonUtil.getRealType(SunlineUtil.dictMap.get(fieldId).getRefType()));
		}catch(Exception e){
		}
		return null;
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月7日-上午10:27:01</li>
	 *         <li>功能说明：获取服务接口的document的双元组,第一个元组是普通服务接口,第二个元组是io服务接口</li>
	 *         </p>
	 * @param module
	 * @return
	 */
	public static TwoTuple<List<Document>, List<Document>> sunlineGetServiceTypeDocTwoTuple(E_ICOREMODULE module){
		if(CommonUtil.isNull(module)){
			return null;
		}
		List<Document> normalServiceList = new ArrayList<Document>();
		List<Document> ioServiceList = new ArrayList<Document>();
		
		for(String fileName : projectFileMap.keySet()){
			if(CommonUtil.isRegexMatches("^Srv"+module.getUpperId()+".*.serviceType.xml$", fileName)){
				Document doc = CommonUtil.getXmlDocument(projectFileMap.get(fileName));
				normalServiceList.add(doc);
			}else if(CommonUtil.isRegexMatches("^SrvIo.*?"+module.getUpperId()+".*.serviceType.xml$", fileName)){
				Document doc = CommonUtil.getXmlDocument(projectFileMap.get(fileName));
				ioServiceList.add(doc);
			}
		}
		return new TwoTuple<List<Document>, List<Document>>(normalServiceList, ioServiceList);
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年7月29日-下午4:42:46</li>
	 *         <li>功能说明：根据类名获取类的属性配置元素</li>
	 *         </p>
	 * @param className
	 */
	public static Element sunlineGetComplexType(String className){
		if(CommonUtil.isNull(className)){
			return null;
		}else{
			for(String fileName : projectFileMap.keySet()){
				//从复合类型中解析
				if(CommonUtil.isRegexMatches("^Com.*?.xml$", fileName)){
					Element root = CommonUtil.getXmlRootElement(projectFileMap.get(fileName));
					Element target = CommonUtil.searchXmlElement(root, "complexType", "id", className);
					if(CommonUtil.isNotNull(target)){
						return target;
					}
				}
			}
		}
		return null;
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年12月2日-下午1:40:49</li>
	 *         <li>功能说明：根据表名获取表的属性配置元素</li>
	 *         </p>
	 * @param tableName
	 * @return
	 */
	public static Element sunlineGetTableType(String tableName){
		if(CommonUtil.isNull(tableName)){
			return null;
		}else{
			for(String fileName : projectFileMap.keySet()){
				//从表类型中解析
				if(CommonUtil.isRegexMatches("^Tab.*?.tables.xml$", fileName)){
					Element root = CommonUtil.getXmlRootElement(projectFileMap.get(fileName));
					Element target = CommonUtil.searchXmlElement(root, "table", "id", tableName);
					if(CommonUtil.isNotNull(target)){
						return target;
					}
				}
			}
		}
		return null;
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月1日-下午2:52:30</li>
	 *         <li>功能说明：移除未引用的枚举(枚举去重)</li>
	 *         </p>
	 * @param outputPath
	 */
	@SuppressWarnings("unchecked")
	public static void sunlineRemoveUnusedEnum(String outputPath){
		if(CommonUtil.isNull(outputPath)){
			return;
		}else{
			for(String fileName : projectFileMap.keySet()){
				//从枚举中解析
				if(CommonUtil.isRegexMatches("^.*?EnumType.*.xml$", fileName)){
					//解析枚举xml
					Document doc = CommonUtil.getXmlDocument(projectFileMap.get(fileName));
					Element root = doc.getRootElement();
					List<Element> restrictionTypeList = root.elements();
					boolean isRemove = false;
					
					for(Element restrictionType : restrictionTypeList){
						if("restrictionType".equals(restrictionType.getName())){
							String curEnumType = CommonUtil.getFirstDotLeftStr(fileName);
							String enumId = restrictionType.attributeValue("id");
							String trueEnumType = enumMap.get(enumId).getEnumLocation();
							
							if(CommonUtil.compare(curEnumType, trueEnumType) != 0){
								//移除当前枚举节点
								root.remove(restrictionType);
								logger.info("枚举类型["+curEnumType+"]的元素:"+enumId+"已被移除");
								isRemove = true;
							}
						}
					}
					if(isRemove){
						//写入到文件
						CommonUtil.writeDocumentXml(doc, outputPath + "/" + fileName);
						CommonUtil.printSplitLine(200);
					}
				}
			}
		}
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月1日-上午10:13:58</li>
	 *         <li>功能说明：字典搜索</li>
	 *         </p>
	 * @return
	 */
	public static Dict sunlineSearchDict(String dictFieldName){
		if(CommonUtil.isNotNull(dictFieldName)){
			dictFieldName = dictFieldName.toLowerCase();
		}
		Dict dictInfo = dictMap.get(dictFieldName);
		if(CommonUtil.isNotNull(dictInfo)){
			String basicType = CommonUtil.getFirstDotLeftStr(dictInfo.getRefType());
			if("BaseType".equals(basicType) || "MsType".equals(basicType)){
				logger.info(dictInfo.toString());
				logger.info(String.valueOf(baseTypeMap.get(CommonUtil.getRealType(dictInfo.getRefType()))));
			}else{
				logger.info(dictInfo.toString());
				logger.info(String.valueOf(enumMap.get(CommonUtil.getRealType(dictInfo.getRefType()))));
			}
		}else{
			logger.info("未搜索到相关字典");
		}
		return dictInfo;
	}
	
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月7日-上午10:55:59</li>
	 *         <li>功能说明：获取io服务id列表</li>
	 *         </p>
	 * @param module
	 * @return
	 */
	public static List<String> sunlineGetIoServiceIdList(E_ICOREMODULE module){
		if(CommonUtil.isNull(module)){
			return null;
		}
		
		List<String> ioServiceIdList = new ArrayList<String>();
		TwoTuple<List<Document>, List<Document>> twotuple = sunlineGetServiceTypeDocTwoTuple(module);
		List<Document> ioServiceList = twotuple.getSecond();
		
		//解析io服务接口
		for(Document doc : ioServiceList){
			List<Element> serviceTypeList = CommonUtil.searchXmlElementList(doc.getRootElement(), "serviceType");
			for(Element serviceType : serviceTypeList){
				String ioServiceId = serviceType.attributeValue("id");
				if(CommonUtil.isNotNull(ioServiceId)){
					ioServiceIdList.add(ioServiceId);
				}
			}
		}
		
		return ioServiceIdList;
	}
	
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月16日-上午9:38:14</li>
	 *         <li>功能说明：获取指定flowtran</li>
	 *         </p>
	 * @param module	模块
	 * @param flowtranId	flowtran id
	 * @return
	 */
	public static Document sunlineSearchFlowtran(E_ICOREMODULE module,String flowtranId){
		if(CommonUtil.isNull(module) || CommonUtil.isNull(flowtranId)){
			return null;
		}
		List<Document> flowtranList = sunlineGetFlowtranDocList(module);
		for(Document flowtran : flowtranList){
			if(flowtran.getRootElement().attributeValue("id").equals(module.getId()+flowtranId)){
				return flowtran;
			}
		}
		return null;
	}
	
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月7日-下午3:09:26</li>
	 *         <li>功能说明：构建icore3.0服务注册表插入SQL语句</li>
	 *         </p>
	 * @param module	模块
	 * @param intfExcelPath	接口清单Excel文件目录
	 * @param sysCode	系统编号
	 * @param subSysCode	子系统编号
	 * @param ouputPath	输出路径
	 * @return
	 */
	public static List<String> sunlineBuildTspServiceInSql(E_ICOREMODULE module,String intfExcelPath,String sysCode,String subSysCode,String ouputPath){
		if(CommonUtil.isNull(module) || CommonUtil.isNull(intfExcelPath) || CommonUtil.isNull(sysCode) || CommonUtil.isNull(subSysCode)){
			return null;
		}
		//定义插入SQL语句结果列表
		List<String> sqlList = new ArrayList<String>();
		
		//解析接口清单Excel,注册交易
		logger.info("解析["+module.getModuleName()+"]模块接口清单Excel,注册交易");
		List<Map<String, Object>> intfExcelList = ExcelReader.getExcelInfo(intfExcelPath);
		for(Map<String, Object> rowMap : intfExcelList){
			if(module.getId().equals(String.valueOf(rowMap.get("内部交易码")).substring(0,2))){
				String insertSql = "INSERT INTO `tsp_service_in` (`system_code`, `sub_system_code`, `out_service_code`, `inner_service_code`, `inner_service_impl_code`, `description`, `service_category`, `route_keys`, `service_type`, `protocol_id`, `is_enable`, `transaction_mode`, `log_level`, `timeout`, `alias_mapping`, `force_unused_odb_cache`, `register_mode`) VALUES ('"+sysCode+"', '"+subSysCode+"', '"+CommonUtil.getFirstDotLeftStr(String.valueOf(rowMap.get("外部交易码")))+"', '"+rowMap.get("内部交易码")+"', '*', '"+rowMap.get("接口描述")+"', 'T', '', '', 'rpc', '1', 'A', '', '0', '0', '0', '0');";
				sqlList.add(insertSql);
			}
		}
		
		//解析服务接口,注册服务
		logger.info("解析["+module.getModuleName()+"]模块服务接口,注册服务");
		TwoTuple<List<Document>, List<Document>> serviceTypeDocTuple = sunlineGetServiceTypeDocTwoTuple(module);
		List<Document> serviceTypeDocList = new ArrayList<Document>();
		serviceTypeDocList.addAll(serviceTypeDocTuple.getFirst());
		serviceTypeDocList.addAll(serviceTypeDocTuple.getSecond());
		
		//定义服务类型为check的标志
		String[] checkSrvIndArray = new String[]{"query","qry","check","cal","get","trial","inquiry","display"};
		//将普通接口和io接口整合起来解析
		for(Document doc : serviceTypeDocList){
			Element root = doc.getRootElement();
			List<Element> serviceList = CommonUtil.searchXmlElementList(root, "serviceType");
			for(Element service : serviceList){
				//获取服务节点id和服务描述,定义insert语句,组装服务编码
				String serviceId = service.attributeValue("id");
				String insertSql = null;
				String serviceDesc = service.attributeValue("longname");
				String serviceCode = root.attributeValue("id") + "." + serviceId;
				
				if(CommonUtil.isNull(serviceId)){
					continue;
				}else if(CommonUtil.isContainsIgnoreCase(serviceId, checkSrvIndArray)){
					//业务服务类型为check
					insertSql = "INSERT INTO `tsp_service_in` (`system_code`, `sub_system_code`, `out_service_code`, `inner_service_code`, `inner_service_impl_code`, `description`, `service_category`, `route_keys`, `service_type`, `protocol_id`, `is_enable`, `transaction_mode`, `log_level`, `timeout`, `alias_mapping`, `force_unused_odb_cache`, `register_mode`) VALUES ('"+sysCode+"', '"+subSysCode+"', '"+serviceCode+"', '"+serviceCode+"', '"+root.attributeValue("id")+"Impl', '"+serviceDesc+"', 'S', '', 'check', 'rpc', '1', 'A', '', '0', '0', '0', '0');";
				}else{
					//业务服务类型为try
					insertSql = "INSERT INTO `tsp_service_in` (`system_code`, `sub_system_code`, `out_service_code`, `inner_service_code`, `inner_service_impl_code`, `description`, `service_category`, `route_keys`, `service_type`, `protocol_id`, `is_enable`, `transaction_mode`, `log_level`, `timeout`, `alias_mapping`, `force_unused_odb_cache`, `register_mode`) VALUES ('"+sysCode+"', '"+subSysCode+"', '"+serviceCode+"', '"+serviceCode+"', '"+root.attributeValue("id")+"Impl', '"+serviceDesc+"', 'S', '', 'try', 'rpc', '1', 'A', '', '0', '0', '0', '0');";
				}
				sqlList.add(insertSql);
			}
		}
		
		if(CommonUtil.isNotNull(ouputPath)){
			String path = ouputPath + File.separator + "tsp_service_in_full.sql";
			logger.info("输出SQL脚本到文件");
			CommonUtil.writeFileContent(sqlList, path);
		}
		
		return sqlList;
	}
	
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月16日-上午9:32:15</li>
	 *         <li>功能说明：接口文档校验</li>
	 *         </p>
	 */
	public static void sunlineIntfExcelValidation(E_ICOREMODULE module,String flowtranId,String intfExcelPath,String outputPath){
		if(CommonUtil.isNull(module) || CommonUtil.isNull(flowtranId) || CommonUtil.isNull(intfExcelPath)){
			return;
		}
		File[] fileArr = new File(intfExcelPath).listFiles();
		for(File file : fileArr){
			if(file.getName().contains(String.valueOf(flowtranId))){
				intfExcelPath = file.getPath();
			}
		}
		
		StringBuffer buffer = new StringBuffer();
		
		//解析接口文档
		TwoTuple<List<Map<String, Object>>, List<Map<String, Object>>> intfExcelTwotuple = ExcelReader.resolverIntfExcel(intfExcelPath);
		List<Map<String, Object>> inputRowMapList = intfExcelTwotuple.getFirst();
		List<Map<String, Object>> outputRowMapList = intfExcelTwotuple.getSecond();
		
		//获取flowtran配置
		Document doc = sunlineSearchFlowtran(module, flowtranId);
		if(CommonUtil.isNull(doc)){
			logger.error("未搜索到相关flowtran");
		}else{
			//获取输入输出字段
			Element input = CommonUtil.searchXmlElement(doc.getRootElement(), "input");
			Element output = CommonUtil.searchXmlElement(doc.getRootElement(), "output");
			
			buffer.append("\r\n" + CommonUtil.buildSplitLine(100) + "\r\n");
			buffer.append("输入接口校验开始\r\n");
			buffer.append(CommonUtil.buildSplitLine(100) + "\r\n");
			
			//输入接口校验
			List<Element> inputFieldList = CommonUtil.searchTargetAllXmlElement(input, "field");
			if(CommonUtil.isNotNull(inputFieldList)){
				ListIterator<Element> iterator = inputFieldList.listIterator();
				while(iterator.hasNext()){
					if(intfSingleFieldValidation(inputRowMapList, iterator.next())){
						iterator.remove();
					}
				}
			}
			
			//输入接口校验结果
			for(Element field : inputFieldList){
				String id = field.attributeValue("id");
				buffer.append("输入字段["+id+"]未被添加进接口文档\r\n");
			}
			for(Map<String, Object> rowMap : inputRowMapList){
				String id = String.valueOf(rowMap.get("名称"));
				String serialNo = String.valueOf(rowMap.get("序号"));
				if(CommonUtil.isNotNull(serialNo) && !CommonUtil.isContainChinese(id)){
					buffer.append("["+serialNo+"]输入字段["+id+"]已在flowtran中删除\r\n");
				}
			}
			buffer.append(CommonUtil.buildSplitLine(100) + "\r\n");
			buffer.append("输出接口校验开始\r\n");
			
			//输出接口校验
			List<Element> outputFieldList = CommonUtil.searchTargetAllXmlElement(output, "field");
			if(CommonUtil.isNotNull(outputFieldList)){
				ListIterator<Element> iterator = outputFieldList.listIterator();
				while(iterator.hasNext()){
					if(intfSingleFieldValidation(outputRowMapList, iterator.next())){
						iterator.remove();
					}
				}
			}
			buffer.append(CommonUtil.buildSplitLine(100) + "\r\n");
			
			//输出接口校验结果
			if(CommonUtil.isNotNull(outputFieldList)){
				for(Element field : outputFieldList){
					String id = field.attributeValue("id");
					buffer.append("输出字段["+id+"]未被添加进接口文档\r\n");
				}
				for(Map<String, Object> rowMap : outputRowMapList){
					String id = String.valueOf(rowMap.get("名称"));
					String serialNo = String.valueOf(rowMap.get("序号"));
					if(CommonUtil.isNotNull(serialNo) && !CommonUtil.isContainChinese(id)){
						buffer.append("["+serialNo+"]输出字段["+id+"]已在flowtran中删除\r\n");
					}
				}
				buffer.append(CommonUtil.buildSplitLine(100) + "\r\n");
			}
			
			logger.info(buffer.toString());
			if(CommonUtil.isNotNull(outputPath)){
				CommonUtil.writeFileContent(buffer.toString(), outputPath + File.separator + module.getId() + flowtranId + "接口校验.txt");
			}
		}
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月16日-下午12:23:10</li>
	 *         <li>功能说明：校验单个字段</li>
	 *         </p>
	 * @param rowMapList
	 * @param fieldList
	 * @param field
	 */
	private static boolean intfSingleFieldValidation(List<Map<String, Object>> rowMapList,Element field) {
		if(CommonUtil.isNull(rowMapList)|| CommonUtil.isNull(field)){
			return false;
		}
		
		String id = field.attributeValue("id");
		//校验当前字段
		for(Map<String, Object> rowMap : rowMapList){
			if(id.equals(rowMap.get("名称"))){
				rowMapList.remove(rowMap);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月8日-上午10:46:20</li>
	 *         <li>功能说明：从2.0研发dit测试环境取20条请求成功的报文</li>
	 *         </p>
	 * @param trxnCode
	 * @return
	 * @throws SQLException 
	 */
	public static List<String> sunlineGetYfditSuccessReq(Integer trxnCode,String querySql) throws SQLException{
		if(CommonUtil.isNull(trxnCode)){
			return null;
		}else{
			//取20条请求成功的报文
			String defaultSql = "SELECT a.* FROM mss_packet_extend a,mss_packet b WHERE a.trxn_seq = b.trxn_seq AND b.trxn_code = ? AND b.return_code = '0000' ORDER BY b.end_time DESC LIMIT 0,20";
			querySql = CommonUtil.nvl(querySql, defaultSql);
			
			ResultSet resultSet = JDBCUtils.executeQuery(querySql, new String[]{String.valueOf(trxnCode)},ApiConst.DATASOURCE_YF);
			String defaultFieldName = "request_text";
			List<String> reqList = CommonUtil.fetchResultSetToList(resultSet, querySql.equals(defaultSql) ? defaultFieldName : "request");
			
			if(CommonUtil.isNotNull(reqList)){
				for(int i = 0;i < reqList.size();i++){
					JSONObject req = JSONObject.fromObject(reqList.get(i));
					//修改sys
					JSONObject reqSys = req.getJSONObject("sys");
					reqSys.put("servicecode", "ln"+reqSys.getString("prcscd"));
					//修改comm_req
					JSONObject reqComm = req.getJSONObject("comm_req");
					reqComm.element("sponsor_system", "102");
					reqComm.element("channel_id", "101");
					
					reqList.set(i, req.toString());
				}
			}else if(querySql.equals(defaultSql)){
				querySql = "SELECT request FROM mss_packet WHERE trxn_code = ? AND return_code = '0000' ORDER BY end_time DESC LIMIT 0,20";
				return sunlineGetYfditSuccessReq(trxnCode, querySql);
			}
			return reqList;
		}
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月9日-上午10:51:20</li>
	 *         <li>功能说明：从2.0研发dit测试环境取20条请求成功的报文</li>
	 *         </p>
	 * @param trxnCode
	 * @return
	 * @throws SQLException 
	 */
	
	public static List<String> sunlineGetYfditSuccessReq(Integer trxnCode) throws SQLException{
		return sunlineGetYfditSuccessReq(trxnCode, null);
	}
	

	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月23日-下午12:28:31</li>
	 *         <li>功能说明：构建内管表单json数据</li>
	 *         </p>
	 * @param className
	 * @return
	 */
	public static String sunlineBuildCtFormJson(String className){
		if(CommonUtil.isNull(className)){
			return null;
		}
		
		List<Element> elementList = CommonUtil.searchTargetAllXmlElement(sunlineGetComplexType(className), "element");
		if(CommonUtil.isNotNull(elementList)){
			Map<String, Map<String, Object>> jsonMap = new LinkedHashMap<String, Map<String, Object>>();
			for(Element field : elementList){
				//获取字段基础信息
				String type = field.attributeValue("type");
				String id = field.attributeValue("id");
				String desc = CommonUtil.isNull(dictMap.get(id)) ? field.attributeValue("desc") : dictMap.get(id).getDesc();
				String realType = CommonUtil.getRealType(type);
				
				//设置公共字段信息
				Map<String, Object> fieldValueMap = new LinkedHashMap<>();
				fieldValueMap.put("label", desc);
				fieldValueMap.put("disabled", false);
				//添加规则
				List<Map<String, Object>> ruleList = new ArrayList<>();
				Map<String, Object> ruleMap = new LinkedHashMap<>();
				ruleMap.put("required", false);
				ruleList.add(ruleMap);
				
				fieldValueMap.put("rules", ruleList);
				/*if(ruleMap.get("required").equals(true)){
					ruleMap.put("message", "请输入" + desc);
				}*/
				//fieldValueMap.put("width", "220px");
				
				//枚举处理
				if(CommonUtil.isNotNull(type) && type.contains(".E_")){
					Map<String, Object> dictMap = new LinkedHashMap<>();
					
					dictMap.put("dictType", realType);
					dictMap.put("format", "value-label");
					dictMap.put("dictKey", new String[]{realType});
					fieldValueMap.put("control", "select");
					fieldValueMap.put("dict", dictMap);
				}
				//日期处理
				else if("U_DATE".equals(realType)){
					fieldValueMap.put("control", "dateTimePicker");
					fieldValueMap.put("valueFormat", "yyyyMMdd");
				}
				//金额处理
				else if("U_MONEY".equals(realType) || "U_INTERESTRATE".equals(realType) || "U_INTEREST".equals(realType)){
					Map<String, Object> formatMap = new HashMap<>();
					formatMap.put("decimal", 2);
					formatMap.put("decimalPoint", ".");
					formatMap.put("thousandsPoint", ",");
					fieldValueMap.put("format", formatMap);
					fieldValueMap.put("control", "currency");
				}
				//普通值处理
				else{
					fieldValueMap.put("control", "text");
				}
				
				jsonMap.put(id, fieldValueMap);
			}
			return JSONArray.fromObject(jsonMap).toString();
		}
		return null;
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月23日-下午1:31:15</li>
	 *         <li>功能说明：构建内管表格json数据</li>
	 *         </p>
	 * @param className
	 * @return
	 */
	public static String sunlineBuildCtTabJson(String className){
		if(CommonUtil.isNull(className)){
			return null;
		}
		
		List<Element> elementList = CommonUtil.searchTargetAllXmlElement(sunlineGetComplexType(className), "element");
		if(CommonUtil.isNotNull(elementList)){
			List<Map<String, Object>> tabList = new ArrayList<>();
			for(Element field : elementList){
				//获取字段基础信息
				String type = field.attributeValue("type");
				String id = field.attributeValue("id");
				String desc = CommonUtil.isNull(dictMap.get(id)) ? field.attributeValue("desc") : dictMap.get(id).getDesc();
				String realType = CommonUtil.getRealType(type);
				
				//设置公共字段信息
				Map<String, Object> fieldValueMap = new LinkedHashMap<>();
				fieldValueMap.put("prop", id);
				fieldValueMap.put("label", desc);
				//fieldValueMap.put("width", 150);
				fieldValueMap.put("disabled", true);
				
				//枚举处理
				if(CommonUtil.isNotNull(type) && type.contains(".E_")){
					Map<String, Object> dictMap = new LinkedHashMap<>();
					
					dictMap.put("dictType", realType);
					dictMap.put("format", "value-label");
					dictMap.put("dictKey", new String[]{realType});
					fieldValueMap.put("type", "select");
					
					fieldValueMap.put("dict", dictMap);
				}
				//日期处理
				else if("U_DATE".equals(realType)){
					fieldValueMap.put("type", "dateTimePicker");
				}
				//普通值处理
				else{
					fieldValueMap.put("type", "text");
				}
				//表格禁止输入
				fieldValueMap.put("type", "string");
				tabList.add(fieldValueMap);
			}
			return JSONArray.fromObject(tabList).toString();
		}
		return null;
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年12月12日-下午4:50:13</li>
	 *         <li>功能说明：生成内管域后事件json</li>
	 *         </p>
	 * @param fieldName	该字段后的域后事件
	 * @param disabled	是否置灰
	 * @param required	是否必输
	 * @param controlFields	被控制的字段列表
	 * @return
	 */
	public static String sunlineBuildCtEventsJson(String fieldName,boolean disabled,boolean required,String... controlFields){
		if(CommonUtil.isNull(fieldName)){
			throw new NullParmException("域后事件字段名");
		}else{
			Dict dictInfo = dictMap.get(fieldName);
			if(CommonUtil.isNotNull(dictInfo)){
				if("BaseType".equals(CommonUtil.getFirstDotLeftStr(dictInfo.getRefType()))){
					return null;
				}else{
					Map<String, Object> eventsMap = new LinkedHashMap<>();
					EnumType enumType = enumMap.get(CommonUtil.getRealType(dictInfo.getRefType()));
					Map<String, EnumElement> enumElementMap = enumType.getEnumElementMap();
					Map<String, Object> conditionSubMap = new LinkedHashMap<>();
					
					for(String v : enumElementMap.keySet()){
						//从内管获取枚举中文
						/*String longnameCN = SunlineUtil.ctEnumMap.get(enumType.getEnumId() + "." + v);
						if(CommonUtil.isNull(longnameCN)){
							continue;
						}*/
						
						Map<String, Object> controlMap = new LinkedHashMap<>();
						for(String controlField : controlFields){
							Map<String, Object> controlSubMap = new LinkedHashMap<>();
							//置灰选项
							controlSubMap.put("disabled", disabled);
							//添加规则
							List<Map<String, Object>> ruleList = new ArrayList<>();
							Map<String, Object> ruleMap = new LinkedHashMap<>();
							ruleMap.put("required", required);
							ruleList.add(ruleMap);
							
							if(disabled){
								controlSubMap.put("value", "");
							}
							controlSubMap.put("rules", ruleList);
							controlMap.put(controlField, controlSubMap);
						}
						Map<String, Object> controlParentMap = new LinkedHashMap<>();
						controlParentMap.put("control", controlMap);
						conditionSubMap.put(v, controlParentMap);
					}
					Map<String, Object> conditionMap = new LinkedHashMap<>();
					conditionMap.put("condition", conditionSubMap);
					eventsMap.put("events", conditionMap);
					return JSONObject.fromObject(eventsMap).toString();
				}
			}
		}
		return null;
	}
	
	
	/**
	 * @throws SQLException 
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月30日-下午1:23:56</li>
	 *         <li>功能说明：杀死锁进程</li>
	 *         </p>
	 */
	public static void sunlineKillProcess(final String dataSourceId) throws SQLException{
		ResultSet resultSet = JDBCUtils.executeQuery("show full PROCESSLIST", dataSourceId);
		Map<String, String> processMap = new HashMap<String, String>();
		while(resultSet.next()){
			String command = resultSet.getString("command");
			String id = resultSet.getString("id");
			
			
			if("Sleep".equals(command)){
				processMap.put(id, command);
			}
		}
		
		ExecutorService threadPool = Executors.newFixedThreadPool(10);
		for(final String id : processMap.keySet()){
			threadPool.submit(new Runnable() {
				
				@Override
				public void run() {
					try{
						JDBCUtils.executeUpdate("kill " + id, dataSourceId);
						logger.info("关闭进程" + id + "成功");
					}catch(SQLException e){
						logger.error("关闭进程" + id + "失败\t" + e.getMessage());
					}
				}
			});
		}
		threadPool.shutdown();
		while(!threadPool.isTerminated());
		CommonUtil.printSplitLine(120);
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月30日-下午3:13:21</li>
	 *         <li>功能说明：内管与flowtran字段同步性校验</li>
	 *         </p>
	 */
	@SuppressWarnings("unchecked")
	public static void sunlineCtFlowtranValidation(E_ICOREMODULE module,String flowtranId,E_LAYOUTTYPE layoutType,String elementId,String jsonFilePath){
		if(CommonUtil.isNull(module) || CommonUtil.isNull(flowtranId) || CommonUtil.isNull(jsonFilePath) || CommonUtil.isNull(layoutType)){
			return;
		}
		Document flowtran = sunlineSearchFlowtran(module, flowtranId);
		List<Element> elementList = null;
		if(CommonUtil.isNotNull(elementId)){
			elementList = CommonUtil.searchXmlElementList(flowtran.getRootElement(), "fields", "id", elementId);
		}else{
			elementList = CommonUtil.searchXmlElementList(flowtran.getRootElement(), "input");
		}
		
		//读取json文件
		String json = CommonUtil.readFileContent(jsonFilePath);
		List<Object> keyList = new ArrayList<>();
		
		if(layoutType == E_LAYOUTTYPE.TABLE){
			JSONArray jsonArray = JSONObject.fromObject(json).getJSONObject("layout").getJSONObject("datagrid").getJSONArray("thead");
			
			Iterator<JSONObject> iterator = jsonArray.iterator();
			while(iterator.hasNext()){
				Object key = iterator.next().get("prop");
				if(CommonUtil.isNotNull(key)){
					keyList.add(key);
				}
			}
		}else if(layoutType == E_LAYOUTTYPE.FORM){
			JSONObject jsonObject = JSONObject.fromObject(json).getJSONObject("layout").getJSONObject("form").getJSONArray("controls").getJSONObject(0);
			for(Object key : jsonObject.keySet()){
				if(CommonUtil.isNotNull(key)){
					keyList.add(key);
				}
			}
		}
		
		StringBuffer buffer = new StringBuffer();
		buffer.append("校验开始\r\n");
		buffer.append(CommonUtil.buildSplitLine(150) + "\r\n");
		for(Element e : elementList){
			String id = e.attributeValue("id");
			boolean isfound = false;
			for(Object key : keyList){
				if(key.equals(id)){
					isfound = true;
				}
			}
			if(!isfound){
				if(CommonUtil.isNull(elementId)){
					buffer.append("内管页面缺少字段" + id + "\r\n");
				}else{
					buffer.append("内管页面"+ elementId +"缺少字段" + id + "\r\n");
				}
			}
		}
		buffer.append(CommonUtil.buildSplitLine(150) + "\r\n");
		buffer.append("校验结束\r\n");
		
		CommonUtil.writeFileContent(buffer.toString(), "C:/Users/36045/Desktop/" + flowtranId + "_" + elementId + ".txt");
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年9月6日-下午2:42:01</li>
	 *         <li>功能说明：发送post交易请求</li>
	 *         </p>
	 * @param serviceCode	外部服务码
	 * @param body	请求体
	 * @throws Exception 
	 */
	public static String sunlineSendPostTrxnRequest(String serviceCode,String body) throws Exception{
		if(CommonUtil.isNull(serviceCode)){
			return null;
		}
		String host = "http://10.22.12.46:9009/";
		String path = "/gateway/";
		
		//封装请求头
		Map<String, String> headers = new HashMap<>();
		headers.put("dserviceId", serviceCode);
		headers.put("api_id", serviceCode);
		headers.put("dapplication", "1021");
		
		headers.put("dgroup", "01");
		headers.put("dversion", "1.0");
		String trxnSeq = CommonUtil.buildTrxnSeq(25);
		headers.put("busiseqno", trxnSeq);
		
		headers.put("callseqno", trxnSeq);
		headers.put("Content-Type", "application/json");
		//封装查询条件
		Map<String, String> querys = new HashMap<String, String>();

		HttpResponse response = NetworkApi.doPost(host, path, headers, querys, body);
		// 获取响应结果
		HttpEntity httpEntity = response.getEntity();
		if (httpEntity != null) {
			InputStream instreams = httpEntity.getContent();
			String str = CommonUtil.convertStreamToJson(instreams);
			return str;
		}
		return null;
	}
	
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年9月9日-下午7:37:59</li>
	 *         <li>功能说明：生成字段属性控制(临时方法)</li>
	 *         </p>
	 * @param className
	 * @return
	 */
	public static String sunlineTempBuildJsonControl(String className){
		if(CommonUtil.isNull(className)){
			return null;
		}
		List<Element> elementList = CommonUtil.searchTargetAllXmlElement(sunlineGetComplexType(className), "element");
		Map<String, Object> controlMap = new LinkedHashMap<>();
		for(Element field : elementList){
			Map<String, Object> valueMap = new LinkedHashMap<>();
			valueMap.put("disabled", true);
			if(valueMap.get("disabled").equals(true)){
				valueMap.put("value", "");
			}
			
			//添加规则
			List<Map<String, Object>> ruleList = new ArrayList<>();
			Map<String, Object> ruleMap = new LinkedHashMap<>();
			ruleMap.put("required", true);
			if(ruleMap.get("required").equals(true)){
				ruleMap.put("message", "请输入" + field.attributeValue("desc"));
			}
			ruleList.add(ruleMap);
			
			valueMap.put("rules", ruleList);
			controlMap.put(field.attributeValue("id"), valueMap);
		}
		return JSONObject.fromObject(controlMap).toString();
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年9月29日-下午1:12:21</li>
	 *         <li>功能说明：网关API发布</li>
	 *         </p>
	 * @param dataSource
	 * @param apiCode
	 * @throws Exception 
	 */
	public static void sunlineGatewayApiRelease(String dataSource,E_ICOREMODULE module,String... serviceCode) throws Exception{
		if(CommonUtil.isNull(dataSource) || CommonUtil.isNull(module)){
			return;
		}
		String apiTamplateExcelPath = SunlineUtil.class.getResource("/tamplate/api_tamplate.xlsx").getPath();
		List<Map<String, String>> dataList = new ArrayList<>();
		List<TspServiceIn> serviceInList = CommonUtil.mappingResultSetList(JDBCUtils.executeQuery("select * from tsp_service_in where service_category = 'T'", dataSource), TspServiceIn.class);
		for(TspServiceIn tspServiceIn : serviceInList){
			Map<String, String> dataMap = new HashMap<>();
			dataMap.put("api", tspServiceIn.getOutServiceCode());
			dataMap.put("中文名称", tspServiceIn.getDescription());
			dataMap.put("描述", tspServiceIn.getDescription());
			dataMap.put("后端服务地址", "rpc3load_alloc_type=NO&rpc3load_application="+tspServiceIn.getSubSystemCode()+"&rpc3load_service_id="+tspServiceIn.getOutServiceCode()+"&rpc3load_group=01&rpc3load_service_type=concentrated&rpc3load_version=1.0");
			
			if(CommonUtil.isNotNull(serviceCode)){
				for(String code : serviceCode){
					if(code.equals(tspServiceIn.getOutServiceCode())){
						dataList.add(dataMap);
					}
				}
			}else{
				dataList.add(dataMap);
			}
		}
		ExcelReader.writeGatewayApi(apiTamplateExcelPath, dataList);
		logger.info("生成网关API数量:" + dataList.size());
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年10月22日-上午10:49:55</li>
	 *         <li>功能说明：查询有效的借据信息列表,上限为100</li>
	 *         </p>
	 * @param dataSource
	 * @return
	 * @throws SQLException 
	 */
	public static List<LnaLoan> sunlineGetEffectLoanList(String dataSource) throws SQLException{
		String sql = "select a.* from lna_loan a,lnf_basic b where a.loan_status = 'NORMAL' and a.settl_ind = 'N' and a.wrof_ind = 'N' and a.loan_classification = 1 and a.prod_id = b.prod_id limit 100";
		return CommonUtil.mappingResultSetList(JDBCUtils.executeQuery(sql, dataSource), LnaLoan.class);
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年10月28日-下午1:31:27</li>
	 *         <li>功能说明：生成接口文档</li>
	 *         </p>
	 * @param flowtranId 流文件id,如ln6001
	 * @param outputPath 接口文档输出路径
	 * @throws Exception
	 */
	public static void sunlineIntfDocumentGenerate(String flowtranId,String outputPath) throws Exception{
		if(CommonUtil.isNull(flowtranId) || CommonUtil.isNull(outputPath)){
			throw new NullParmException("flowtran id","文档输出路径");
		}
		//接口文档模板Excel路径
		String intfDocTamplateExcelPath = SunlineUtil.class.getResource("/tamplate/intf_doc_tamplate.xlsx").getPath();
		//解析flotation
		//定义flowtran信息哈希表
		Map<String, String> flowtranMap = new LinkedHashMap<>();
		//设置flowtran信息
		Element flowtranRoot = CommonUtil.getXmlRootElement(projectFileMap.get(flowtranId+".flowtrans.xml"));
		flowtranMap.put("id", flowtranId);
		flowtranMap.put("kind", flowtranRoot.attributeValue("kind"));
		flowtranMap.put("longname", CommonUtil.googleTranslate(flowtranRoot.attributeValue("longname"), E_LANGUAGE.EN, E_LANGUAGE.ZHCN));
		//获取输入输出字段
		TwoTuple<Map<String, String>, Map<String, String>> twoTuple = sunlineGetFlowtranInOutputField(flowtranId);
		//Excel写入
		ExcelReader.writeIntfDocument(flowtranMap, twoTuple, intfDocTamplateExcelPath, outputPath);
		logger.info("生成接口文档:" + flowtranMap.get("id") + "_" + flowtranMap.get("longname")+"V0.1.xlsx");
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年10月28日-下午1:45:43</li>
	 *         <li>功能说明：获取flowtran的输入输出字段</li>
	 *         </p>
	 * @param flowtranId	flowtran id,如ln6001
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public static TwoTuple<Map<String, String>, Map<String, String>> sunlineGetFlowtranInOutputField(String flowtranId) throws Exception{
		if(CommonUtil.isNull(flowtranId)){
			throw new NullParmException("flowtran id");
		}
		//解析flowtran
		Element flowtranRoot = CommonUtil.getXmlRootElement(projectFileMap.get(flowtranId+".flowtrans.xml"));
		//定义输入输出哈希表
		Map<String, String> inputMap = new LinkedHashMap<>();
		Map<String, String> outputMap = new LinkedHashMap<>();
		//解析输入字段
		List<Element> input = flowtranRoot.element("interface").element("input").elements();
		for(Element field : input){
			if("field".equals(field.getName())){
				if("true".equals(field.attributeValue("multi"))){
					inputMap.put(field.attributeValue("id"), "list");
				}else{
					inputMap.put(field.attributeValue("id"), field.attributeValue("type"));
				}
			}else if("fields".equals(field.getName())){
				String fieldsId = field.attributeValue("id");
				inputMap.put(fieldsId, "list");
				List<Element> subFieldList = field.elements();
				for(Element subField : subFieldList){
					inputMap.put(fieldsId + "." + subField.attributeValue("id"), subField.attributeValue("type"));
				}
			}
		}
		//解析输出字段
		List<Element> output = flowtranRoot.element("interface").element("output").elements();
		for(Element field : output){
			if("field".equals(field.getName())){
				if("true".equals(field.attributeValue("multi"))){
					outputMap.put(field.attributeValue("id"), "list");
				}else{
					outputMap.put(field.attributeValue("id"), field.attributeValue("type"));
				}
			}else if("fields".equals(field.getName())){
				String fieldsId = field.attributeValue("id");
				outputMap.put(fieldsId, "list");
				List<Element> subFieldList = field.elements();
				for(Element subField : subFieldList){
					outputMap.put(fieldsId + "." + subField.attributeValue("id"), subField.attributeValue("type"));
				}
			}
		}
		return new TwoTuple<Map<String,String>, Map<String,String>>(inputMap, outputMap);
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年10月29日-上午10:56:48</li>
	 *         <li>功能说明：异常类配置根据错误码排序</li>
	 *         </p>
	 * @param outputPath	文件输出路径
	 */
	@SuppressWarnings("unchecked")
	public static void sunlineErrorXmlSort(String outputPath,boolean isAsc){
		if(CommonUtil.isNull(outputPath)){
			throw new NullParmException("文件输出路径");
		}
		for(String fileName : projectFileMap.keySet()){
			boolean isOrdered = true;
			String beforeId = null;
			//从错误配置中解析
			if(CommonUtil.isRegexMatches("^.*?Error.error.xml$", fileName)){
				Document doc = CommonUtil.getXmlDocument(projectFileMap.get(fileName));
				Element root = doc.getRootElement();
				Element errors = root.element("errors");
				List<Element> errorList = errors.elements();
				
				Map<String, Element> errorMap = new LinkedHashMap<>();
				List<String> errorIdList = new ArrayList<>();
				for(Element e : errorList){
					String curId = e.attributeValue("id");
					if(CommonUtil.isNotNull(beforeId) && (isAsc ? CommonUtil.compare(curId, beforeId) < 0 : CommonUtil.compare(curId, beforeId) > 0)){
						isOrdered = false;
					}
					errorMap.put(curId, e);
					errorIdList.add(curId);
					beforeId = curId;
				}
				
				if(!isOrdered){
					errors.clearContent();
					Collections.sort(errorIdList);
					if(!isAsc){
						Collections.reverse(errorIdList);
					}
					
					for(String id : errorIdList){
						errors.addText("\r\n\t\t");
						errors.add(errorMap.get(id));
					}
					errors.addText("\r\n\t");
					CommonUtil.writeDocumentXml(doc, outputPath + File.separator + fileName);
				}else{
					logger.info(fileName + "的错误码有序,不处理");
				}
			}
		}
	}
	
	
	/**
	 * @throws IOException 
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年10月31日-下午1:26:10</li>
	 *         <li>功能说明：根据全量Excel生成全量dml脚本</li>
	 *         </p>
	 */
	public static void sunlineFullSQLGenerate(String outputPath) throws IOException{
		if(CommonUtil.isNull(outputPath)){
			throw new NullParmException("文件输出目录");
		}
		for(String fileName : projectFileMap.keySet()){
			//全量脚本Excel中解析
			if(CommonUtil.isRegexMatches("^.*?xlsx$", fileName)){
				String filePath = projectFileMap.get(fileName);
				String outputFilePath = outputPath + File.separator + filePath.substring(filePath.indexOf(ApiConst.FULLSQL_MAINDIR_NAME)).replace(".xlsx", ".sql");
				String outputFileDir = outputFilePath.substring(0,outputFilePath.lastIndexOf("\\"));
				
				//创建输出路径
				File outputDir = new File(outputFileDir);
				if(!outputDir.exists()){
					outputDir.mkdirs();
				}
				CommonUtil.writeFileContent(ExcelReader.extractSqlFromExcel(filePath), outputFilePath);
			}
		}
		String srcDir = outputPath + File.separator + ApiConst.FULLSQL_MAINDIR_NAME;
		String zipFilePath = srcDir + ".zip";
		logger.info("全量脚本压缩打包:" + zipFilePath);
		CommonUtil.toZip(srcDir, zipFilePath, true);
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年11月1日-下午4:20:14</li>
	 *         <li>功能说明：删除贷款产品</li>
	 *         </p>
	 * @param dataSource	数据源
	 * @param prodId	产品编号
	 * @throws SQLException 
	 */
	public static void sunlineDeleteLnProduct(String dataSource,String... prodId) throws SQLException{
		if(CommonUtil.isNull(dataSource) || CommonUtil.isNull(prodId)){
			throw new NullParmException("数据源","产品编号");
		}
		for(String id : prodId){
			logger.info("删除产品["+id+"]的基础属性:" + JDBCUtils.executeUpdate("delete from lnf_basic where prod_id = ?", new String[]{id}, dataSource));
			logger.info("删除产品["+id+"]的放款属性:" + JDBCUtils.executeUpdate("delete from lnf_drawdown where prod_id = ?", new String[]{id}, dataSource));
			logger.info("删除产品["+id+"]的还款属性:" + JDBCUtils.executeUpdate("delete from lnf_repayment where prod_id = ?", new String[]{id}, dataSource));
			logger.info("删除产品["+id+"]的计息属性:" + JDBCUtils.executeUpdate("delete from lnf_accrual where prod_id = ?", new String[]{id}, dataSource));
			logger.info("删除产品["+id+"]的到期属性:" + JDBCUtils.executeUpdate("delete from lnf_maturity where prod_id = ?", new String[]{id}, dataSource));
			logger.info("删除产品["+id+"]的缺省值属性:" + JDBCUtils.executeUpdate("delete from lnf_field_control where prod_id = ?", new String[]{id}, dataSource));
			CommonUtil.printSplitLine(150);
		}
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年11月1日-下午1:47:40</li>
	 *         <li>功能说明：贷款产品同步</li>
	 *         </p>
	 * @param fromDataSource	源数据源
	 * @param toDataSource	目的数据源
	 * @param isCheckUnlinked	是否检查为关联产品,为true时只检查不同步,为false时只同步不检查,且会移除未关联的产品属性
	 * @throws Exception 
	 */
	public static void sunlineLnProductSync(String fromDataSource,String toDataSource,boolean isCheckUnlinked,String...prodId) throws Exception{
		if(CommonUtil.isNull(fromDataSource) || CommonUtil.isNull(toDataSource)){
			throw new NullParmException("源数据源","目的数据源");
		}
		
		sunlineLnProductSync(fromDataSource, toDataSource, "lnf_basic", isCheckUnlinked,prodId);
		sunlineLnProductSync(fromDataSource, toDataSource, "lnf_drawdown", isCheckUnlinked,prodId);
		sunlineLnProductSync(fromDataSource, toDataSource, "lnf_repayment", isCheckUnlinked,prodId);
		sunlineLnProductSync(fromDataSource, toDataSource, "lnf_accrual", isCheckUnlinked,prodId);
		sunlineLnProductSync(fromDataSource, toDataSource, "lnf_maturity", isCheckUnlinked,prodId);
		sunlineLnProductSync(fromDataSource, toDataSource, "lnf_field_control", isCheckUnlinked,prodId);
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年11月1日-下午2:41:42</li>
	 *         <li>功能说明：贷款产品同步子方法</li>
	 *         </p>
	 * @param fromDataSource
	 * @param toDataSource
	 * @param tableName
	 * @throws Exception
	 */
	private static void sunlineLnProductSync(String fromDataSource,String toDataSource, String tableName, boolean isCheckUnlinked,String...prodId) throws Exception{
		//获取要处理的产品号
		List<String> prodList = new ArrayList<>();
		if(CommonUtil.isNull(prodId)){
			List<Map<String, Object>> resultList = CommonUtil.resolveResultSetToList(JDBCUtils.executeQuery("select prod_id from " + tableName, fromDataSource));
			for(Map<String, Object> map : resultList){
				prodList.add(String.valueOf(map.get("prod_id")));
			}
		}else{
			prodList.addAll(Arrays.asList(prodId));
		}
		//没有产品编号,不处理
		if(CommonUtil.isNull(prodList)){
			return;
		}
		//生成inSQL
		String inSql = new String("(");
		for(String pd : prodList){
			inSql += "'" + pd + "',";
		}
		inSql = inSql.substring(0, inSql.length() - 1) + ");";
		
		List<Map<String, Object>> fromResList = CommonUtil.resolveResultSetToList(JDBCUtils.executeQuery("select prod_id from "+tableName+" where prod_id not in (select DISTINCT a.prod_id from lnf_basic a join lnf_repayment b join lnf_drawdown c join lnf_maturity d join lnf_accrual e join lnf_field_control f on a.prod_id = b.prod_id and b.prod_id = c.prod_id and c.prod_id = d.prod_id and d.prod_id = e.prod_id and e.prod_id = f.prod_id);", fromDataSource));
		List<Map<String, Object>> toResList = CommonUtil.resolveResultSetToList(JDBCUtils.executeQuery("select prod_id from "+tableName+" where prod_id not in (select DISTINCT a.prod_id from lnf_basic a join lnf_repayment b join lnf_drawdown c join lnf_maturity d join lnf_accrual e join lnf_field_control f on a.prod_id = b.prod_id and b.prod_id = c.prod_id and c.prod_id = d.prod_id and d.prod_id = e.prod_id and e.prod_id = f.prod_id);", toDataSource));
		
		//检查未关联的产品
		if(CommonUtil.isNotNull(fromResList)){
			for(Map<String, Object> rowMap : fromResList){
				logger.info("源数据源[" + fromDataSource + "]未关联的产品属性["+tableName+"]的产品编号:" + rowMap.get("prod_id"));
			}
		}else{
			logger.info("源数据源[" + fromDataSource + "]无未关联的产品属性["+tableName+"]");
		}
		if(CommonUtil.isNotNull(toResList)){
			for(Map<String, Object> rowMap : toResList){
				logger.info("目标数据源[" + toResList + "]未关联的产品属性["+tableName+"]的产品编号:" + rowMap.get("prod_id"));
			}
		}else{
			logger.info("目标数据源[" + toDataSource + "]无未关联的产品属性["+tableName+"]");
		}
		//移除目标数据源的相关产品
		List<String> sqlList = new ArrayList<String>();
		sqlList.add("delete from " + tableName + " where prod_id in " + inSql);
		List<String> insertSqlList = CommonUtil.generateInsertSQL(JDBCUtils.executeQuery("select * from " + tableName + " where prod_id in " + inSql, fromDataSource));
		sqlList.addAll(insertSqlList);
		logger.info("处理贷款产品的[" + tableName + "]属性,待处理记录数:"+sqlList.size()+",实际处理记录数:" + JDBCUtils.executeUpdate(sqlList, toDataSource));
		CommonUtil.printSplitLine(150);
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年11月12日-下午5:29:28</li>
	 *         <li>功能说明：</li>
	 *         </p>
	 */
	public static void sunlineCheckUnusedErrorCode(String outputPath){
		StringBuffer buffer = new StringBuffer();
		for(String fileName : projectFileMap.keySet()){
			//从错误配置中解析
			if(CommonUtil.isRegexMatches("^.*?Error.error.xml$", fileName)){
				Element root = CommonUtil.getXmlRootElement(projectFileMap.get(fileName));
				List<Element> errorList = CommonUtil.searchTargetAllXmlElement(root, "error");
				for(Element error : errorList){
					boolean isUsed = false;
					String checkStr = CommonUtil.getFirstDotLeftStr(fileName) + "." + fileName.split("Error")[0] + ".";
					for(String javaFile : projectFileMap.keySet()){
						if(CommonUtil.isRegexMatches("^Ln.*?.java$", javaFile) || CommonUtil.isRegexMatches("^Co.*?.java$", javaFile)){
							if(CommonUtil.readFileContent(projectFileMap.get(javaFile)).contains(checkStr + error.attributeValue("id") + "(")){
								logger.info(fileName + "->错误码["+error.attributeValue("id")+"]被["+javaFile+"]使用");
								isUsed = true;
								break;
							}
						}
					}
					if(!isUsed){
						buffer.append(fileName).append("-").append(error.attributeValue("id")).append("\r\n");
					}
				}
			}
		}
		CommonUtil.writeFileContent(buffer.toString(), outputPath + File.separator + "unusedErrorCode.txt");
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年11月22日-下午1:48:36</li>
	 *         <li>功能说明：根据请求报文构建set语句</li>
	 *         </p>
	 * @param request
	 * @throws SQLException 
	 */
	@SuppressWarnings("unchecked")
	public static String sunlineBuildSetFromRequest(String dataSource) throws SQLException{
		if(CommonUtil.isNull(dataSource)){
			throw new NullParmException("请求报文","数据源ID");
		}else{
			String request = CommonUtil.readFileContent(SunlineUtil.class.getResource("/json/request.json").getPath());
			JSONObject json = JSONObject.fromObject(request);
			//获取服务码
			String serviceCode = json.getJSONObject("sys").getString("servicecode");
			List<MspTransaction> mspTransactionList = CommonUtil.mappingResultSetList(JDBCUtils.executeQuery("select * from msp_transaction where trxn_code like ?",new String[]{"%" + serviceCode.substring(serviceCode.length() - 4)}, dataSource), MspTransaction.class);
			String trxnCode = String.valueOf(mspTransactionList.get(0).getTrxnCode());
			//获取flowtran
			Element flowtranRoot = CommonUtil.getXmlRootElement(projectFileMap.get(trxnCode + ".flowtrans.xml"));
			Element serviceElement = CommonUtil.searchXmlElement(flowtranRoot, "service");
			String[] serviceType = serviceElement.attributeValue("id").split("\\.");
			//获取服务
			Element serviceRoot = CommonUtil.getXmlRootElement(projectFileMap.get(serviceType[0] + ".serviceType.xml"));
			Element serviceInterface = CommonUtil.searchXmlElement(serviceRoot, "service", "id", serviceType[1]).element("interface");
			Element inputField = CommonUtil.searchXmlElement(serviceInterface, "input").element("field");
			String inputName = inputField.attributeValue("id");
			
			String complexType = inputField.attributeValue("type").split("\\.")[1];
			List<Element> complexElementList = sunlineGetComplexType(complexType).elements();
			//构建输入语句
			StringBuffer buffer = new StringBuffer();
			buffer.append(complexType + " " + inputName + " = " + "BizUtil.getInstance("+complexType+".class);\r\n");
			JSONObject input = json.getJSONObject("input");
			for(Object key : input.keySet()){
				Object value = input.get(key);
				Dict dictInfo = dictMap.get(key);
				String baseStatement = inputName + ".set" + key.toString().substring(0,1).toUpperCase() + key.toString().substring(1);
				
				if(CommonUtil.isNull(dictInfo)){
					boolean isList = false;
					String listSubType = null;
					for(Element e : complexElementList){
						if(e.getName().equals("element") && key.equals(e.attributeValue("id")) && e.attributeValue("multi").equals("true")){
							isList = true;
							listSubType = e.attributeValue("type").split("\\.")[1];
							break;
						}
					}
					if(isList){
						/**
						 * 解析输入的列表
						 */
						//实例化Options
						buffer.append("\r\nOptions<"+listSubType+"> "+listSubType+"Options = new DefaultOptions<"+listSubType+">();\r\n");
						JSONArray jsonArray = input.getJSONArray(String.valueOf(key));
						for(int i = 0;i < jsonArray.size();i++){
							JSONObject subJsonObj = jsonArray.getJSONObject(i);
							//实例化子类
							String subVarName = listSubType+"SubInstance"+(i+1);
							buffer.append(listSubType + " "+subVarName+" = BizUtil.getInstance("+listSubType+".class);\r\n");
							for(Object subKey : subJsonObj.keySet()){
								generateSetStatement(buffer, subJsonObj.get(subKey), dictMap.get(subKey), subVarName + ".set" + subKey.toString().substring(0,1).toUpperCase() + subKey.toString().substring(1));
							}
							buffer.append(listSubType+"Options"+".add("+subVarName+");\r\n");
						}
						buffer.append(baseStatement + "("+listSubType+"Options"+");\r\n");
					}else{
						continue;
					}
				}else{
					generateSetStatement(buffer, value, dictInfo, baseStatement);
				}
			}
			return buffer.toString();
		}
	}

	private static void generateSetStatement(StringBuffer buffer, Object value, Dict dictInfo, String baseStatement) {
		if(CommonUtil.isNull(value)){
			buffer.append(baseStatement + "(null);\r\n");
		}else if("BaseType".equals(CommonUtil.getFirstDotLeftStr(dictInfo.getRefType()))){
			//如果字段是基础引用类型
			if("decimal".equals(baseTypeMap.get(CommonUtil.getRealType(dictInfo.getRefType())).getBase())){
				buffer.append(baseStatement + "(new BigDecimal("+value+"));\r\n");
			}else if("long".equals(baseTypeMap.get(CommonUtil.getRealType(dictInfo.getRefType())).getBase())){
				buffer.append(baseStatement + "("+value+"L);\r\n");
			}else{
				buffer.append(baseStatement + "(\""+value+"\");\r\n");
			}
		}else{
			//如果字段是枚举
			EnumType enumType = enumMap.get(CommonUtil.getRealType(dictInfo.getRefType()));
			if(CommonUtil.isNull(enumType)){
				buffer.append(baseStatement + "(\""+value+"\");\r\n");
			}else{
				buffer.append(baseStatement + "("+enumType.getEnumId()+"."+enumType.getEnumElementMap().get(value).getId()+");\r\n");
			}
		}
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年11月22日-下午3:46:54</li>
	 *         <li>功能说明：构建"字段不为空"代码</li>
	 *         </p>
	 * @param fieldId	字段id列表
	 * @return
	 */
	public static String sunlineBuildFieldNotNull(String...fieldId){
		StringBuffer buffer = new StringBuffer();
		if(CommonUtil.isNull(fieldId)){
			throw new NullParmException("字段id列表");
		}else{
			for(String id : fieldId){
				Dict dictInfo = dictMap.get(id);
				if(CommonUtil.isNotNull(dictInfo)){
					buffer.append("BizUtil.fieldNotNull(input.get" + id.substring(0,1).toUpperCase() + id.substring(1) + "(), " + dictInfo.getDictType() + ".A." + id + ".getId(), " + dictInfo.getDictType() + ".A." + id + ".getLongName());\r\n");
				}
			}
		}
		return buffer.toString();
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年11月13日-下午1:12:47</li>
	 *         <li>功能说明：执行全量SQL</li>
	 *         </p>
	 * @param file
	 * @throws SQLException 
	 */
	public static void sunlineExecuteFullSql(File file, String dataSource) throws SQLException {
		if (file != null) {
			if (file.isDirectory()) {
				// 列举此目录下所有文件及文件夹
				File[] list = file.listFiles();
				for (int i = 0; i < list.length; i++) {
					sunlineExecuteFullSql(list[i], dataSource);
				}
			} else {
				String sql = CommonUtil.readFileContent(file.getPath());
				logger.info("执行[" + file.getName() + "]\r\n" + sql);
				logger.info("生效记录条数:" + JDBCUtils.executeUpdate(Arrays.asList(sql.split("\n")), dataSource));
			}
		}
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年12月19日-上午10:28:18</li>
	 *         <li>功能说明：获取合并代码时提交的文件列表</li>
	 *         </p>
	 * @param structModule	结构模块
	 * @param moduleFullName	子模块全名
	 * @param mergeNo	合并编号
	 * @return
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	public static List<String> sunlineGetMergedFiles(E_STRUCTMODULE structModule, String moduleFullName,String... mergeNoArr) throws IllegalStateException, IOException{
		List<String> filePathList = new LinkedList<String>();
		if(CommonUtil.isNotNull(mergeNoArr)){
			for(String mergeNo : mergeNoArr){
				String url = "http://e-git.yfb.sunline.cn/icore3.0/"+structModule.getId()+"/"+moduleFullName+"/merge_requests/"+mergeNo+"/diffs.json";
				Map<String, String> headers = new HashMap<String,String>();
				headers.put("Cookie", "remember_user_token=W1sxODBdLCIkMmEkMTAkVnNxWk5zNnVtZlNJYXEzLk1HalhYdSIsIjE1NzY1NTE3MDQuNzU0NzMyIl0%3D--403841547402c2e7f6a41dd47de1ab212ea565fd; _gitlab_session=a05d4b3ae546edc7160ad2691ff3b7d9");
				HttpResponse httpResponse = NetworkApi.doGet(url, "", headers, new HashMap<String,String>());
				if(CommonUtil.isNotNull(httpResponse)){
					HttpEntity httpEntity = httpResponse.getEntity();
					String responseContent = CommonUtil.convertStreamToJson(httpEntity.getContent());
					String html = JSONObject.fromObject(responseContent).getString("html");
					org.jsoup.nodes.Document doc = Jsoup.parse(html);
					
					String reg = "\\</i> (?<scope>.*?)\\ </a>";
					Pattern pattern = Pattern.compile(reg);
					parseMergeFilesHtml(filePathList, doc, pattern, "edit-file");
					parseMergeFilesHtml(filePathList, doc, pattern, "renamed-file");
					parseMergeFilesHtml(filePathList, doc, pattern, "new-file");
				}
			}
		}
		return filePathList;
	}

	
	private static void parseMergeFilesHtml(List<String> filePathList, org.jsoup.nodes.Document doc, Pattern pattern, String elementClass) {
		Elements liList = doc.getElementsByClass(elementClass);
		for(org.jsoup.nodes.Element e : liList){
			Matcher matcher = pattern.matcher(e.html());
			if(matcher.find()){
				filePathList.add(matcher.group("scope"));
			}
		}
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2020年2月28日-下午2:58:58</li>
	 *         <li>功能说明：从nexus仓库获取各模块jar包的最新版本</li>
	 *         </p>
	 * @param subModule 子模块
	 * @param modules	模块
	 * @return
	 */
	public static Map<String, String> sunlineQueryNexusJarVersion(String subModule, String... modules){
		Map<String, String> resultMap = new LinkedHashMap<>();
		if(CommonUtil.isNotNull(subModule) && CommonUtil.isNotNull(modules)){
			for(String md : modules){
				Element root = CommonUtil.getUrlRootElement("http://nexus.odc.sunline.cn/repository/odc-pub/cn/sunline/icore/"+md+"/"+md+"-"+subModule+"/maven-metadata.xml");
				resultMap.put(md + "-" + subModule, CommonUtil.searchXmlElement(root, "latest").getText());
			}
		}
		return resultMap;
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2020年3月3日-下午5:47:38</li>
	 *         <li>功能说明：生成前端开发增量脚本</li>
	 *         </p>
	 * @param menuCode
	 * @param serviceCode
	 * @param menuUpperId
	 * @param menuDesc
	 * @param pageId
	 * @param isList
	 * @return
	 */
	public static List<String> sunlineGenerateMenuSql(String menuCode, String serviceCode, String menuUpperId, String menuDesc, String pageId, String... isList){
		List<String> sqlList = new LinkedList<>();
		sqlList.add("-- "+menuCode+"-"+menuDesc+" --");
		sqlList.add("delete from ctp_menu where menu_code = '" + menuCode + "';");
		sqlList.add("delete from ctp_trxn_mapping where ct_trxn_code = '" + serviceCode + "';");
		
		sqlList.add("INSERT INTO `ctp_menu` (`menu_code`, `menu_id`, `menu_upper_id`, `menu_group`, `menu_default_ind`, `menu_desc`, `page_id`, `page_display_scene`, `output_page_id`, `data_create_time`, `data_update_time`, `data_create_user`, `data_update_user`, `data_version`) VALUES ('"+menuCode+"', '"+serviceCode+"', '"+menuUpperId+"', 'LN', 'N', '"+menuDesc+"', '"+pageId+"', NULL, NULL, 'S####', '20170301 09:30:11 233', NULL, NULL, '0');");
		sqlList.add("INSERT INTO `ctp_trxn_mapping` (`ct_trxn_code`, `trxn_code_name`, `backend_trxn_code`, `trxn_control_ind`, `cumulative_limit_ind`, `service_executor_id`, `package_mapping_id`, `register_server_id`, `external_scene_id`, `trxn_version`, `data_create_time`, `data_update_time`, `data_create_user`, `data_update_user`, `data_version`) VALUES ('"+serviceCode+"', '"+menuDesc+"', '"+serviceCode+"', 'N', NULL, 'rpc', NULL, 'ln', '01', '1.0', 'S####', '20170301 09:30:11 233', NULL, NULL, '0');");
		sqlList.add("-- "+serviceCode+"-"+menuDesc+" --");
		sqlList.add("delete from smp_sys_trans where trans_cd = '" + serviceCode + "';");
		
		String outList = CommonUtil.isNull(isList) ? "NULL" : "'"+isList[0]+"'";
		sqlList.add("INSERT INTO smp_sys_trans(`trans_cd`, `trans_name`, `service_cd`, `encap_cd`, `trans_status`, `deal_cnt`, `scene_id`, `dcn_id`, `version_id`, `app_id`, `timeout`, `group_id`, `isrecord`, `islist`, `system_id`, `is_approval`) VALUES ('"+serviceCode+"', '"+menuDesc+"', 'sunflow', '"+serviceCode+"', 'Y', '0', '01', 'adm', '1.0', '1021', '120000', 'LN', NULL, "+outList+", NULL, 'false');");
		sqlList.add("commit;");
		return sqlList;
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2020年3月12日-上午9:23:23</li>
	 *         <li>功能说明：为前端界面为初始化未空的currency控件初始化为空</li>
	 *         </p>
	 * @param jsonObj	JSON对象
	 * @return	返回二元组,<该json发生改变返回true,返回美化后的json>
	 * @throws IOException
	 */
	public static TwoTuple<Boolean, String> sunlineAddDefaultValForCurrency(JSONObject jsonObj) throws IOException{
		boolean isChange = false;
		try{
			//处理主字段
			JSONObject form = jsonObj.getJSONObject("layout").getJSONObject("form");
			if(form.containsKey("controlsGroup")){
				JSONArray controlsGroup = jsonObj.getJSONObject("layout").getJSONObject("form").getJSONArray("controlsGroup");
				for(int i = 0;i < controlsGroup.size();i++){
					JSONArray controls = controlsGroup.getJSONObject(i).getJSONArray("controls");
					isChange = dealAddDefaultValFormControls(isChange, controls);
				}
			}else{
				JSONArray controls = form.getJSONArray("controls");
				isChange = dealAddDefaultValFormControls(isChange, controls);
			}
		}catch(Exception e){
		}
		//使用jackson对json进行美化
		return new TwoTuple<Boolean, String>(isChange, CommonUtil.fastjsonFormat(jsonObj.toString()));
	}

	private static boolean dealAddDefaultValFormControls(boolean isChange, JSONArray controls) {
		for(int j = 0;j < controls.size();j++){
			JSONObject control = controls.getJSONObject(j);
			
			for(Object key : control.keySet()){
				JSONObject fieldJson = control.getJSONObject(String.valueOf(key));
				if((fieldJson.containsKey("control") && fieldJson.getString("control").equals("currency"))
				&& !fieldJson.containsKey("value")){
					fieldJson.put("value", "");
					isChange = true;
				}
			}
		}
		return isChange;
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2020年3月12日-下午2:55:32</li>
	 *         <li>功能说明：为前端及域后字段新增长度限制</li>
	 *         </p>
	 * @param jsonObj	页面json对象
	 * @return
	 * @throws IOException
	 */
	public static TwoTuple<Boolean, String> sunlineAddFieldLengthLimit(JSONObject jsonObj) throws IOException{
		int changeCount = 0;
		JSONArray controls = null;
		try{
			JSONObject form = jsonObj.getJSONObject("layout").getJSONObject("form");
			if(!form.isNullObject()){
				if(form.containsKey("controlsGroup")){
					JSONArray controlsGroup = jsonObj.getJSONObject("layout").getJSONObject("form").getJSONArray("controlsGroup");
					for(int i = 0;i < controlsGroup.size();i++){
						controls = controlsGroup.getJSONObject(i).getJSONArray("controls");
						changeCount += dealAddFieldLengthFormControls(controls);
						//处理域后事件的字段
						changeCount = addFieldLenLimitForEvents(changeCount, controls);
					}
				}else{
					controls = form.getJSONArray("controls");
					changeCount += dealAddFieldLengthFormControls(controls);
					//处理域后事件的字段
					changeCount = addFieldLenLimitForEvents(changeCount, controls);
				}
			}
		}catch(Exception e){
			CommonUtil.printLogError(e, logger);
		}
		//使用jackson对json进行美化
		return new TwoTuple<Boolean, String>(changeCount > 0, CommonUtil.fastjsonFormat(jsonObj.toString()));
	}

	private static int addFieldLenLimitForEvents(int changeCount, JSONArray controls) {
		if(CommonUtil.isNotNull(controls)){
			for(int i = 0;i < controls.size();i++){
				JSONObject control = controls.getJSONObject(i);
				for(Object key : control.keySet()){
					if(control.getJSONObject(String.valueOf(key)).containsKey("events")){
						JSONObject condition = control.getJSONObject(String.valueOf(key)).getJSONObject("events").getJSONObject("condition");
						for(Object conditionKey : condition.keySet()){
							JSONObject subControl = condition.getJSONObject(String.valueOf(conditionKey)).getJSONObject("control");
							for(Object subControlKey : subControl.keySet()){
								String max = getFieldMaxLength(subControlKey);
								if(CommonUtil.isNull(max)){
									continue;
								}
								changeCount += replaceFieldMaxLength(subControl.getJSONObject(String.valueOf(subControlKey)), max, subControlKey);
							}
						}
					}
				}
			}
		}
		return changeCount;
	}

	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2020年3月12日-上午10:40:21</li>
	 *         <li>功能说明：为字段添加默认长度限制</li>
	 *         </p>
	 * @param isChange
	 * @param controls
	 * @return
	 */
	private static int dealAddFieldLengthFormControls(JSONArray controls) {
		int changeCount = 0;
		for(int j = 0;j < controls.size();j++){
			JSONObject control = controls.getJSONObject(j);
			for(Object key : control.keySet()){
				String max = getFieldMaxLength(key);
				if(CommonUtil.isNull(max)){
					continue;
				}
				JSONObject fieldJson = control.getJSONObject(String.valueOf(key));
				changeCount += replaceFieldMaxLength(fieldJson, max, key);
			}
		}
		return changeCount;
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2020年3月12日-下午2:41:07</li>
	 *         <li>功能说明：获取字段最大长度</li>
	 *         </p>
	 * @param key	字段id
	 * @return
	 */
	private static String getFieldMaxLength(Object key){
		//获取当前字段的长度
		BaseType baseType = sunlineGetFieldBaseType(String.valueOf(key));
		String max = null;
		if(CommonUtil.isNull(baseType)){
			EnumType enumType = sunlineGetFieldEnumType(String.valueOf(key));
			if(CommonUtil.isNotNull(enumType)){
				max = enumType.getMaxLength();
			}
		}else{
			max = baseType.getMaxLength();
		}
		
		if(CommonUtil.isNull(max)){
			String ref = CommonUtil.isNull(dictMap.get(String.valueOf(key))) ? "" : dictMap.get(String.valueOf(key)).getRefType();
			String kBaseLen = ref.contains("KBaseType.U_LEIXIN") ? ref.split("KBaseType.U_LEIXIN")[1] : "";;
			//例外
			if(ref.contains("E_YESORNO") || ref.contains("E_DEBITCREDIT") || String.valueOf(key).contains("_ind")){
				max = "1";
			}else if(CommonUtil.isContainsIgnoreCase(String.valueOf(key), new String[]{"_bal"})){
				max = "21";
			}else if(CommonUtil.isNotNull(kBaseLen)){
				max = kBaseLen;
			}else{
				logger.error("未查找到字段["+key+"]的最大长度");
			}
		}
		return max;
	}
	
	
	
	@SuppressWarnings({ "deprecation", "unchecked" })
	private static int replaceFieldMaxLength(JSONObject fieldJson, String max, Object key){
		Object rules = fieldJson.get("rules");
		int changeCount = 0;
		BaseType baseType = sunlineGetFieldBaseType(String.valueOf(key));
		boolean isDecimal = CommonUtil.isNotNull(baseType) && ("decimal".equals(baseType.getBase()) || "inputNumber".equals(baseType.getBase()));
		
		//如果rules不是采用的规范写法,纠正为数组
		if(CommonUtil.isNull(rules) || fieldJson.containsKey("required")){
			//获取是否必输
			boolean required = CommonUtil.isNull(fieldJson.get("required")) ? false : fieldJson.getBoolean("required");
			List<JSONObject> fixedRules = new LinkedList<>();
			fixedRules.add(JSONObject.fromObject(new Required(required)));
			
			//20200313:研发中心bug,移除掉decimal字段的最大长度限制
			if(!isDecimal){
				fixedRules.add(JSONObject.fromObject(new Max(max)));
			}
			fieldJson.remove("required");
			
			fieldJson.put("rules", JSONArray.fromObject(fixedRules));
			changeCount++;
		}
		//如果rules采用的规范写法,在数组中新增或替换max规则
		else if(CommonUtil.isJsonArray(rules.toString())){
			JSONObject pendingRemoveObj = null;
			List<JSONObject> ruleList = JSONArray.toList(JSONArray.fromObject(rules), JSONObject.class);
			boolean isExist = false;
			for(JSONObject ruleObj : ruleList){
				//含max规则,如果不一致则替换属性
				if(ruleObj.containsKey("max")){
					if(!String.valueOf(ruleObj.get("max")).equals(max)){
						logger.info("字段["+key+"]长度改变:" + ruleObj.get("max") + "->" + max);
						ruleObj.put("max", Integer.parseInt(max));
						ruleObj.put("message", "长度不能超过"+max+"个字符");
					}
					
					//20200313:研发中心bug,移除掉decimal字段的最大长度限制
					if(isDecimal){
						pendingRemoveObj = ruleObj;
					}
					
					isExist = true;
					break;
				}
			}
			//不含max属性,则新增
			if(!isExist && !isDecimal){
				ruleList.add(JSONObject.fromObject(new Max(max)));
				changeCount++;
			}
			//字段为金额类,则删除
			if(CommonUtil.isNotNull(pendingRemoveObj)){
				ruleList.remove(pendingRemoveObj);
				changeCount++;
			}
			fieldJson.put("rules", JSONArray.fromObject(ruleList));
		}
		return changeCount;
	}
}
