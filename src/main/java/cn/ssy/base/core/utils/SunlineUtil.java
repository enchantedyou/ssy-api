package cn.ssy.base.core.utils;

import java.io.File;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.log4j.Logger;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;

import cn.ssy.base.core.network.api.NetworkApi;
import cn.ssy.base.entity.consts.ApiConst;
import cn.ssy.base.entity.plugins.TwoTuple;
import cn.ssy.base.entity.sunline.BaseType;
import cn.ssy.base.entity.sunline.Dict;
import cn.ssy.base.entity.sunline.EnumElement;
import cn.ssy.base.entity.sunline.EnumType;
import cn.ssy.base.enums.E_ICOREMODULE;
import cn.ssy.base.enums.E_LANGUAGE;
import cn.ssy.base.enums.E_LAYOUTTYPE;
import cn.ssy.base.enums.E_PACKAGETYPE;
import cn.ssy.base.exception.NullParmException;
import cn.ssy.base.thread.DownLoadThread;

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
	public final static Map<String, String> projectFileMap = new LinkedHashMap<String, String>();
	//项目字典
	public final static Map<String, Dict> dictMap = new LinkedHashMap<>();
	//被使用到的枚举
	private final static Map<String, EnumType> enumMap = new LinkedHashMap<String, EnumType>();
	//基础引用类型
	private final static Map<String, BaseType> baseTypeMap = new LinkedHashMap<>();
	//字典优先级
	private static Map<String, Integer> dictPriorityMap = new LinkedHashMap<String, Integer>();
	//枚举优先级
	private static Map<String, Integer> enumPriorityMap = new LinkedHashMap<String, Integer>();
	//log4j日志
	private static final Logger logger = Logger.getLogger(SunlineUtil.class);
	//线程池
	private static final ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
	
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月5日-上午11:13:15</li>
	 *         <li>功能说明：工具初始化</li>
	 *         </p>
	 * @param projectPath	项目目录路径
	 * @param dictPriorityMap	字典优先级
	 * @param enumPriorityMap	枚举优先级
	 */
	public static void sunlineInitializer(String projectPath,Map<String, Integer> dictPriorityMap,Map<String, Integer> enumPriorityMap){
		if(CommonUtil.isNotNull(projectPath) && CommonUtil.isNull(projectFileMap)){
			//初始化项目文件
			loadProjectFile(new File(projectPath));
			logger.info("初始化项目文件>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		}
		if(CommonUtil.isNotNull(dictPriorityMap)){
			//初始化字典优先级
			SunlineUtil.dictPriorityMap = dictPriorityMap;
			logger.info("初始化字典优先级>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		}
		if(CommonUtil.isNotNull(enumPriorityMap)){
			//初始化枚举优先级
			SunlineUtil.enumPriorityMap = enumPriorityMap;
			logger.info("初始化枚举优先级>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		}
		if(CommonUtil.isNull(dictMap)){
			//初始化项目字典
			loadProjectDict();
			logger.info("初始化项目字典>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		}
		if(CommonUtil.isNull(enumMap)){
			//初始化项目枚举
			loadProjectEnum();
			logger.info("初始化项目枚举>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		}
		if(CommonUtil.isNull(baseTypeMap)){
			//初始化项目枚举
			loanProjectBaseType();
			CommonUtil.printSplitLine(150);
			logger.info("初始化基础引用类型>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
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
				}
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
	private static Map<String, Integer> getDefaultDictPriorityMap(){
		Map<String, Integer> map = new HashMap<>();
		map.put("MsDict", 1);
		map.put("SysDict", 2);
		map.put("ApDict", 3);
		
		map.put("ApBaseDict", 4);
		map.put("CtBaseDict", 5);
		map.put("LnSysDict", 6);
		map.put("LnBaseDict", 7);
		
		map.put("LnIobusDict", 8);
		map.put("LnServDict", 9);
		map.put("CoServDict", 9);
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
	private static Map<String, Integer> getDefaultEnumPriorityMap(){
		Map<String, Integer> map = new HashMap<>();
		map.put("MsEnumType", 1);
		map.put("EnumType", 2);
		map.put("LnBaseEnumType", 3);
		
		map.put("LnSysEnumType", 4);
		map.put("LnIobusEnumType", 5);
		map.put("LnServEnumType", 6);
		map.put("CoServEnumType", 6);
		
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
		if(CommonUtil.isNull(enumMap) && CommonUtil.isNotNull(projectFileMap)){
			Map<String, Integer> priorityMap = CommonUtil.nvl(enumPriorityMap, getDefaultEnumPriorityMap());
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
								EnumElement enumElement = new EnumElement(enumeration.attributeValue("id"), enumeration.attributeValue("value"), enumeration.attributeValue("longname"));
								curEnumType.addEnumElement(enumElement);
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
	 *         <li>功能说明：字典引用枚举校验,保证字典总是引用的最高优先级的枚举(校验字典)</li>
	 *         </p>
	 * @param outputPath	输出路径
	 */
	public static void sunlineRefEnumVerify(String outputPath){
		if(CommonUtil.isNull(outputPath)){
			return;
		}else{
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
						Attribute typeAttribute = element.attribute("type");
						String realEnumValue = CommonUtil.getRealType(typeAttribute.getValue());
						String dictId = element.attributeValue("id");
						String curRefEnumType = CommonUtil.getFirstDotLeftStr(typeAttribute.getValue());
						String trueRefEnumType = enumMap.get(realEnumValue).getEnumLocation();
						
						if(CommonUtil.isNotNull(curRefEnumType)
						&& !curRefEnumType.equals("MsEnumType")	
						&& CommonUtil.isNotNull(trueRefEnumType)
						&& CommonUtil.compare(trueRefEnumType, curRefEnumType) != 0){
							//字典的枚举引用类型不正确,替换
							typeAttribute.setValue(typeAttribute.getValue().replace(curRefEnumType, trueRefEnumType));
							isChanged = true;
							System.err.println("元素["+dictId+"]的引用类型发生更替:"+curRefEnumType+"."+realEnumValue+"->"+trueRefEnumType+"."+realEnumValue);
						}
					}
					
					if(isChanged){
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
	 *         <li>2019年7月31日-上午9:33:36</li>
	 *         <li>功能说明：加载项目字典</li>
	 *         </p>
	 */
	private static void loadProjectDict(){
		if(CommonUtil.isNull(dictMap) && CommonUtil.isNotNull(projectFileMap)){
			Map<String, Integer> priorityMap = CommonUtil.nvl(dictPriorityMap, getDefaultDictPriorityMap());
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
			List<Element> restrictionTypeList = baseTypeRoot.elements();
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
	 *         <li>功能说明：字典枚举校验(校验复合类型、flowtran、table、报表)</li>
	 *         </p>
	 * @param outputPath
	 */
	@SuppressWarnings("unchecked")
	public static void sunlineDictEnumValidation(String outputPath){
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
					CommonUtil.printSplitLine(200);
				}
			}
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
	@SuppressWarnings("unchecked")
	private static Map<String, String> traversingFlowtranXml(List<Element> flowtranEleList){
		Map<String, String> resMap = new HashMap<>();
		for(Element flowtranElement : flowtranEleList){
			if("interface".equals(flowtranElement.getName())){
				List<Element> inoutputList = flowtranElement.elements();
				for(Element inoutput : inoutputList){
					List<Element> filedList = inoutput.elements();
					for(Element field : filedList){
						resMap.putAll(verifyFieldElement(field));
						if("fields".equals(field.getName())){
							List<Element> subFieldList = field.elements();
							for(Element subField : subFieldList){
								resMap.putAll(verifyFieldElement(subField));
							}
						}
					}
				}
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
	@SuppressWarnings("unchecked")
	private static Map<String, String> traversingTableXml(List<Element> tableList){
		Map<String, String> resMap = new HashMap<>();
		for(Element table : tableList){
			List<Element> tableEleList = table.elements();
			for(Element tableElement : tableEleList){
				if("fields".equals(tableElement.getName())){
					List<Element> fieldList = tableElement.elements();
					for(Element field : fieldList){
						resMap.putAll(verifyFieldElement(field));
					}
				}
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
	@SuppressWarnings("unchecked")
	private static Map<String, String> traversingComplexXml(List<Element> ComplexList){
		Map<String, String> resMap = new HashMap<>();
		for(Element complexType : ComplexList){
			if("complexType".equals(complexType.getName())){
				List<Element> elementList = complexType.elements();
				for(Element element : elementList){
					resMap.putAll(verifyFieldElement(element));
				}
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
						logger.info("元素["+id+"]的字典发生更替:"+refValue+"->"+refAttribute.getValue());
					}
					if(!enumType.equals(typeAttribute.getValue())){
						String typeValue = typeAttribute.getValue();
						typeAttribute.setValue(enumType);
						logger.info("元素["+id+"]的枚举发生更替:"+typeValue+"->"+enumType);
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
			if("BaseType".equals(CommonUtil.getFirstDotLeftStr(dictInfo.getRefType()))){
				logger.info("字段["+dictFieldName+"]存在于:"+dictInfo.getDictType()+",引用类型:"+dictInfo.getRefType()+",描述:"+dictInfo.getDesc() + ",引用类型详情:" + String.valueOf(baseTypeMap.get(CommonUtil.getRealType(dictInfo.getRefType()))));
			}else{
				logger.info("字段["+dictFieldName+"]存在于:"+dictInfo.getDictType()+",引用类型:"+dictInfo.getRefType()+",描述:"+dictInfo.getDesc() + ",引用类型详情:" + String.valueOf(enumMap.get(CommonUtil.getRealType(dictInfo.getRefType()))));
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
	 * @param intfExcelPath	接口清单Exce文件目录
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
			List<Element> input = CommonUtil.searchXmlElementList(doc.getRootElement(), "input");
			List<Element> output = CommonUtil.searchXmlElementList(doc.getRootElement(), "output");
			
			buffer.append("\r\n" + CommonUtil.buildSplitLine(100) + "\r\n");
			buffer.append("输入接口校验开始\r\n");
			buffer.append(CommonUtil.buildSplitLine(100) + "\r\n");
			
			//输入接口校验
			List<Element> inputFieldList = getAllFieldList(input);
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
				if(CommonUtil.isNotNull(serialNo)){
					buffer.append("["+serialNo+"]输入字段["+id+"]已在flowtran中删除\r\n");
				}
			}
			buffer.append(CommonUtil.buildSplitLine(100) + "\r\n");
			buffer.append("输出接口校验开始\r\n");
			
			//输出接口校验
			List<Element> outputFieldList = getAllFieldList(output);
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
					if(CommonUtil.isNotNull(serialNo)){
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
	 *         <li>2019年8月16日-下午12:18:22</li>
	 *         <li>功能说明：获取当前字段列表的所有字段和子字段</li>
	 *         </p>
	 * @param fieldList	字段列表
	 * @return
	 */
	private static List<Element> getAllFieldList(List<Element> fieldList){
		if(CommonUtil.isNull(fieldList)){
			return null;
		}
		List<Element> resultList = new LinkedList<Element>();
		for(Element field : fieldList){
			List<Element> inputSubFieldList = CommonUtil.searchXmlElementList(field, "fields");
			
			if(CommonUtil.isNotNull(inputSubFieldList)){
				for(Element inputSubField : inputSubFieldList){
					resultList.add(inputSubField);
				}
			}
			else{
				resultList.add(field);
			}
		}
		return resultList;
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
	 *         <li>2019年8月7日-下午5:30:37</li>
	 *         <li>功能说明：icore3.0打包部署</li>
	 *         </p>
	 * @param module	模块
	 * @param packageType	打包模式
	 * @param version	版本号
	 * @param outputPath	输出路径
	 * @param isToZip	是否压缩打包为zip
	 */
	public static void sunlinePackageServiceJar(E_ICOREMODULE module,E_PACKAGETYPE packageType,String version,final String outputPath,boolean isToZip){
		if(CommonUtil.isNull(module) || CommonUtil.isNull(packageType) || CommonUtil.isNull(version) || CommonUtil.isNull(outputPath)){
			return;
		}else{
			File outputDir = new File(outputPath);
			if(!outputDir.exists()){
				logger.info("目录["+outputPath+"]不存在,创建目录");
				outputDir.mkdirs();
				logger.info("目录创建完成");
			}else if(!outputDir.isDirectory()){
				logger.error("["+outputDir.getPath() + "]必须是目录");
				return;
			}
		}
		
		String moduleId = module.getId();
		List<String> subModuleList = new ArrayList<String>();
		
		//根据打包类型定义子模块数组列表
		if(packageType == E_PACKAGETYPE.BASE){
			subModuleList.add(moduleId + "-base");
			subModuleList.add(moduleId + "-sys");
		}else if(packageType == E_PACKAGETYPE.IOBUS){
			subModuleList.add(moduleId + "-iobus");
		}else if(packageType == E_PACKAGETYPE.SERV){
			subModuleList.add(moduleId + "-serv");
			subModuleList.add(moduleId + "-batch");
			subModuleList.add(moduleId + "-tran");
		}else if(packageType == E_PACKAGETYPE.ALL){
			subModuleList.add(moduleId + "-serv");
			subModuleList.add(moduleId + "-batch");
			subModuleList.add(moduleId + "-tran");
			subModuleList.add(moduleId + "-base");
			subModuleList.add(moduleId + "-sys");
			subModuleList.add(moduleId + "-iobus");
			subModuleList.add(moduleId + "-iobus");
		}
		
		for(String subModule : subModuleList){
			String mavenMetaDataUrl = "http://10.22.10.10:8081/repository/maven-public/cn/sunline/icore/"+moduleId+"/"+subModule+"/"+version+"/maven-metadata.xml";
			//解析maven-metadata.xml
			Element root = CommonUtil.getUrlRootElement(mavenMetaDataUrl);
			if(CommonUtil.isNull(root)){
				logger.error("解析["+subModule+"]maven-metadata.xml失败");
			}else{
				//获取最新的版本号
				Element value = CommonUtil.searchXmlElement(root, "value");
				if(CommonUtil.isNull(value)){
					logger.error("获取["+subModule+"]的最新版本号失败");
				}else{
					String lastVersion = value.getText();
					final String nexusJarName = subModule + "-" + lastVersion + ".jar";
					final String downloadUrl = "http://10.22.10.10:8081/repository/maven-public/cn/sunline/icore/"+moduleId+"/"+subModule+"/"+version+"/"+nexusJarName;
					final String deployJarName = subModule + "-" + version + ".jar";
					//下载部署包
					cachedThreadPool.submit(new DownLoadThread(downloadUrl, outputPath + File.separator + deployJarName));
				}
			}
		}
		
		//如果含有附属模块,则对附属模块也进行打包
		E_ICOREMODULE[] attachModuleArray = module.getAttachModule();
		if(CommonUtil.isNotNull(attachModuleArray)){
			for(int i = 0;i < attachModuleArray.length;i++){
				E_ICOREMODULE attachModule = attachModuleArray[i];
				logger.info("["+module.getModuleName()+"]模块包含["+attachModule.getModuleName()+"],获取["+attachModule.getModuleName()+"]模块的包");
				
				if(i != attachModuleArray.length - 1){
					sunlinePackageServiceJar(attachModule, attachModule.getPackageType(), version, outputPath, false);
				}else{
					sunlinePackageServiceJar(attachModule, attachModule.getPackageType(), version, outputPath, isToZip);
				}
			}
			return;
		}
		
		//压缩为zip
		if(isToZip){
			if(module == E_ICOREMODULE.CO){
				moduleId = E_ICOREMODULE.LN.getId();
			}
			String zipFilePath = outputPath + File.separator + moduleId + "-" + version + ".zip";
			
			cachedThreadPool.shutdown();
			while(!cachedThreadPool.isTerminated()){
				CommonUtil.systemPause(5);
			}
			logger.info("压缩打包:" + zipFilePath);
			CommonUtil.toZip(outputPath,zipFilePath , true);
		}
	}
	
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月8日-上午10:46:20</li>
	 *         <li>功能说明：从2.0研发dit测试环境取20条请求成功的报文</li>
	 *         </p>
	 * @param trxnCode
	 * @return
	 */
	public static List<String> sunlineGetYfditSuccessReq(Integer trxnCode,String querySql){
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
	 */
	public static List<String> sunlineGetYfditSuccessReq(Integer trxnCode){
		return sunlineGetYfditSuccessReq(trxnCode, null);
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月15日-下午7:09:50</li>
	 *         <li>功能说明：生成批量交易控制表的插入sql</li>
	 *         </p>
	 * @param module 模块
	 * @return
	 */
	public static List<String> sunlineGetTranControlSql(E_ICOREMODULE module){
		List<String> sqlList = new ArrayList<String>();
		if(CommonUtil.isNull(module)){
			return sqlList;
		}
		String sql = "select * from ksys_jykzhq where pljioyma like ?";
		ResultSet resultSet = JDBCUtils.executeQuery(sql, new String[]{module.getId()+"%"}, ApiConst.DATASOURCE_YF);
		try {
			int colNum = CommonUtil.getColumnNameList(resultSet).size();
			while(resultSet.next()){
				StringBuffer buffer = new StringBuffer();
				for(int i = 1;i <= colNum;i++){
					String colValue = resultSet.getString(i);
					String curCol = CommonUtil.isNull(colValue) ? "NULL," : "'"+colValue+"',";
					
					if(i == 1){
						curCol = "'"+module.getSysCode()+"',";
					}
					buffer.append(curCol);
				}
				String value = buffer.toString().substring(0,buffer.toString().length() - 1);
				
				String insertSql = "INSERT INTO `DEV_LN`.`tsp_tran_controller` (`system_code`, `corporate_code`, `tran_group_id`, `step_id`, `tran_code`, `tran_chinese_name`, `execution_code`, `rely_tran_list`, `tran_run_conditions`, `reconnection_num`, `fail_interrupt_code`, `transactions_submit_num`, `data_split_mode`, `data_split_key`, `job_execution_mode`, `max_job_concurrency_num`, `log_level`, `job_split_condition`, `task_run_mode`, `is_skip`, `is_batch_file`, `tran_type`) VALUES ("+value+");";
				sqlList.add(insertSql);
			}
		}
		catch (SQLException e) {
			CommonUtil.printLogError(e, logger);
		}
		return sqlList;
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月15日-下午7:30:05</li>
	 *         <li>功能说明：生成批量交易分组表的插入sql</li>
	 *         </p>
	 * @param module	模块
	 * @return
	 */
	public static List<String> sunlineGetTranGroupSql(E_ICOREMODULE module){
		List<String> sqlList = new ArrayList<String>();
		if(CommonUtil.isNull(module)){
			return sqlList;
		}
		String sql = "select * from ksys_jyzkzq";
		ResultSet resultSet = JDBCUtils.executeQuery(sql, ApiConst.DATASOURCE_YF);
		try {
			int colNum = CommonUtil.getColumnNameList(resultSet).size();
			while(resultSet.next()){
				StringBuffer buffer = new StringBuffer();
				for(int i = 1;i <= colNum;i++){
					String colValue = resultSet.getString(i);
					String curCol = CommonUtil.isNull(colValue) ? "NULL," : "'"+colValue+"',";
					
					if(i == 1){
						curCol = "'"+module.getSysCode()+"',";
					}
					buffer.append(curCol);
				}
				String value = buffer.toString().substring(0,buffer.toString().length() - 1);
				
				String insertSql = "INSERT INTO `DEV_LN`.`tsp_tran_group_controller` (`system_code`, `corporate_code`, `tran_group_id`, `tran_group_desc`, `task_run_conditions`, `task_run_callback_service`) VALUES ("+value+");";
				sqlList.add(insertSql);
			}
		}
		catch (SQLException e) {
			CommonUtil.printLogError(e, logger);
		}
		return sqlList;
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
				String desc = field.attributeValue("desc");
				String realType = CommonUtil.getRealType(type);
				
				//设置公共字段信息
				Map<String, Object> fieldValueMap = new LinkedHashMap<>();
				fieldValueMap.put("label", desc);
				fieldValueMap.put("disabled", true);
				//添加规则
				List<Map<String, Object>> ruleList = new ArrayList<>();
				Map<String, Object> ruleMap = new LinkedHashMap<>();
				ruleMap.put("required", false);
				ruleList.add(ruleMap);
				
				fieldValueMap.put("rules", ruleList);
				if(ruleMap.get("required").equals(true)){
					ruleMap.put("message", "请输入" + desc);
				}
				fieldValueMap.put("span", "12");
				
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
				String desc = field.attributeValue("desc");
				String realType = CommonUtil.getRealType(type);
				
				//设置公共字段信息
				Map<String, Object> fieldValueMap = new LinkedHashMap<>();
				fieldValueMap.put("prop", id);
				fieldValueMap.put("label", desc);
				fieldValueMap.put("width", 150);
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
	 *         <li>2019年8月30日-下午1:23:56</li>
	 *         <li>功能说明：杀死锁进程</li>
	 *         </p>
	 */
	public static void sunlineKillProcess(String dataSourceId){
		ResultSet resultSet = JDBCUtils.executeQuery("show full PROCESSLIST", dataSourceId);
		try{
			Map<String, String> processMap = new HashMap<String, String>();
			int killNum = 0;
			while(resultSet.next()){
				String command = resultSet.getString("command");
				String id = resultSet.getString("id");
				
				
				if("Sleep".equals(command)){
					processMap.put(id, command);
				}
			}
			
			for(String id : processMap.keySet()){
				if(JDBCUtils.executeUpdate("kill " + id, dataSourceId)){
					killNum++;
				}
				logger.info("关闭进程" + id);
			}
			CommonUtil.printSplitLine(120);
			logger.info("成功关闭的进程数量:" + killNum);
		}catch(Exception e){
			logger.error("关闭进程异常");
		}
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
		Map<String, String> headers = CommonUtil.readPropertiesSettings(SunlineUtil.class.getResource("/ln_dev3.0.properties").getPath());
		headers.put("dserviceId", serviceCode);
		headers.put("api_id", serviceCode);
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
	public static void sunlineGatewayApiRelease(String dataSource,E_ICOREMODULE module,String serviceCode) throws Exception{
		if(CommonUtil.isNull(dataSource) || CommonUtil.isNull(module)){
			return;
		}
		String apiTamplateExcelPath = SunlineUtil.class.getResource("/tamplate/api_tamplate.xlsx").getPath();
		List<Map<String, String>> dataList = new ArrayList<>();
		List<Map<String, Object>> result = CommonUtil.resolveResultSetToList(JDBCUtils.executeQuery("select * from tsp_service_in where out_service_code like concat(?,'%')", new String[]{String.valueOf(module.getSrvSign())}, dataSource));
		for(Map<String, Object> map : result){
			Map<String, String> dataMap = new HashMap<>();
			dataMap.put("api", String.valueOf(map.get("out_service_code")));
			dataMap.put("中文名称", String.valueOf(map.get("description")));
			dataMap.put("描述", String.valueOf(map.get("description")));
			dataMap.put("后端服务地址", "rpc3load_alloc_type=NO&rpc3load_application="+map.get("sub_system_code")+"&rpc3load_service_id="+map.get("out_service_code")+"&rpc3load_group=01&rpc3load_service_type=concentrated&rpc3load_version=1.0");
			
			if(CommonUtil.isNotNull(serviceCode)){
				if(serviceCode.equals(String.valueOf(map.get("out_service_code")))){
					dataList.add(dataMap);
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
	 */
	public static List<Map<String, Object>> sunlineGetEffectLoanList(String dataSource){
		String sql = "select a.* from lna_loan a,lnf_basic b where a.loan_status = 'NORMAL' and a.settl_ind = 'N' and a.wrof_ind = 'N' and a.loan_classification = 1 and a.prod_id = b.prod_id limit 100";
		return CommonUtil.resolveResultSetToList(JDBCUtils.executeQuery(sql, dataSource));
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
		ExcelReader.writeIntfDocument(baseTypeMap, flowtranMap, twoTuple, intfDocTamplateExcelPath, outputPath);
		logger.info("生成接口文档:" + flowtranMap.get("longname")+".xlsx");
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
			//从字典中解析
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
}
