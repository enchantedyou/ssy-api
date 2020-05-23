package cn.ssy.api.core;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.http.HttpResponse;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

import cn.ssy.base.core.network.api.NetworkApi;
import cn.ssy.base.core.utils.BatTaskUtil;
import cn.ssy.base.core.utils.CommonUtil;
import cn.ssy.base.core.utils.JDBCUtils;
import cn.ssy.base.core.utils.SunlineUtil;
import cn.ssy.base.entity.consts.ApiConst;
import cn.ssy.base.entity.mybatis.LnaLoan;
import cn.ssy.base.entity.plugins.Params;
import cn.ssy.base.entity.sunline.ComplexElement;
import cn.ssy.base.enums.E_ICOREMODULE;
import cn.ssy.base.enums.E_LANGUAGE;
import cn.ssy.base.enums.E_LAYOUTTYPE;
import cn.ssy.base.enums.E_STRUCTMODULE;

public class SimpleTest{
	
	final public static String outputPath = "C:/Users/DELL/Desktop/";
	
	//log4j日志
	private static final Logger logger = Logger.getLogger(SimpleTest.class);
	
	@Before
	public void before() throws SQLException{
		SunlineUtil.sunlineInitializer("D:/Sunline/sunlineWorkspace/icore3.0/",true);
	}
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月8日-上午10:47:19</li>
	 *         <li>功能说明：构建交易注册sql</li>
	 *         </p>
	 * @throws SQLException
	 * @throws IOException
	 */
	@Test
	public void test1() throws SQLException, IOException{
		SunlineUtil.sunlineBuildTspServiceInSql(E_ICOREMODULE.LN, "C:/sunline/sunlineDeveloper/文档/icore3.0研发/1#贷款接口清单.xlsx", "102", "1021","C:/Users/DELL/Desktop/");
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月14日-下午1:45:07</li>
	 *         <li>功能说明：字典搜索</li>
	 *         </p>
	 * @throws SQLException
	 * @throws IOException
	 */
	@Test
	public void test12() throws SQLException, IOException{
		SunlineUtil.sunlineSearchDict("Insf_rpym_method");
	}
	
	/**
	 * @throws SQLException 
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月30日-下午1:35:08</li>
	 *         <li>功能说明：启动批量</li>
	 *         </p>
	 */
	@Test
	public void test7() throws Exception{
		BatTaskUtil.tryStartupTask();
	}
	
	
	/**
	 * @throws SQLException 
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月30日-下午1:35:17</li>
	 *         <li>功能说明：构建内管表单json</li>
	 *         </p>
	 */
	@Test
	public void test8() throws SQLException{
		//System.out.println(SunlineUtil.sunlineBuildCtFormJson("IoLnLoanNormalOpenIn"));
		//System.out.println(SunlineUtil.sunlineBuildCtFormJson("LnQueryLoanInfoOut"));
		//System.out.println(SunlineUtil.sunlineBuildCtFormJson("IoLnWriteOffRepaymentIn"));
		System.out.println(SunlineUtil.sunlineBuildCtFormJson("LnProdInfoType"));
		//LnQueryLoanInfoOut
	}
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年12月12日-下午4:51:30</li>
	 *         <li>功能说明：构建内管域后事件json</li>
	 *         </p>
	 */
	@Test
	public void test37(){
		System.out.println(SunlineUtil.sunlineBuildCtEventsJson("dd_td_ind", true, false,"share_confirm_date","share_confirm_date_type","share_confirm_days"));
	}
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月30日-下午1:35:52</li>
	 *         <li>功能说明：构建内管表格json</li>
	 *         </p>
	 */
	@Test
	public void test9(){
		/*
		System.out.println(SunlineUtil.sunlineBuildCtTabJson("LnBasicInfo"))
		System.out.println(SunlineUtil.sunlineBuildCtTabJson("LnDrawdownInfo"));
		System.out.println(SunlineUtil.sunlineBuildCtTabJson("LnRepaymentInfo"));
		System.out.println(SunlineUtil.sunlineBuildCtTabJson("LnAccrualInfo"));
		System.out.println(SunlineUtil.sunlineBuildCtTabJson("LnMaturityInfo"));
		System.out.println(SunlineUtil.sunlineBuildCtTabJson("LnFieldControlInfo"));
		*/
		System.out.println(SunlineUtil.sunlineBuildCtTabJson("LnScheduleListQueryIn"));
	}
	
	
	/**
	 * @throws SQLException 
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月30日-下午1:36:03</li>
	 *         <li>功能说明：数据库解锁</li>
	 *         </p>
	 */
	@Test
	public void test10() throws SQLException{
		SunlineUtil.sunlineKillProcess(ApiConst.DATASOURCE_ICORE_LN_BAT);
	}

	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月30日-下午3:36:46</li>
	 *         <li>功能说明：内管后台接口校验</li>
	 *         </p>
	 */
	@Test
	public void test11(){
		String jsonFileName = "ln_prop_edit_maturity.json";
		String path = "C:/JavaDevelop/MyEclipseWorkSpace/sump-vue/src/views/ln/prod/" + jsonFileName;
		SunlineUtil.sunlineCtFlowtranValidation(E_ICOREMODULE.LN, "6011", E_LAYOUTTYPE.TABLE, "matInfos", path);
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2020年4月13日-下午9:06:55</li>
	 *         <li>功能说明：生成复合类型元素</li>
	 *         </p>
	 * @throws Exception
	 */
	@Test
	public void test15() throws Exception{
		String fieldStr = CommonUtil.readFileContent(outputPath + "/f.txt");
		List<ComplexElement> resultList = SunlineUtil.sunlineGenerateComplexElement(CommonUtil.parseStringToArray(fieldStr, "\r\n"));
		for(ComplexElement e : resultList){
			System.out.println(e);
		}
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2020年4月13日-下午9:07:11</li>
	 *         <li>功能说明：生成flowtran类型元素</li>
	 *         </p>
	 * @throws Exception
	 */
	@Test
	public void test14() throws Exception{
		System.out.println(SunlineUtil.sunlineGenerateFlowtranElement("PfFinancialProdMntIn"));
	}
	
	
	/**
	 * 
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2020年4月16日-下午7:56:41</li>
	 *         <li>功能说明：生成table类型元素</li>
	 *         </p>
	 * @throws Exception
	 */
	@Test
	public void test14p() throws Exception{
		String fieldStr = CommonUtil.readFileContent(outputPath + "/f.txt");
		String result = SunlineUtil.sunlineGenerateTableElement(CommonUtil.parseStringToArray(fieldStr, "\n"));
		System.out.println(result);
		//CommonUtil.writeFileContent(result, outputPath + "r.txt");
	}
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年9月9日-上午11:02:48</li>
	 *         <li>功能说明：谷歌翻译</li>
	 *         </p>
	 * @throws Exception
	 */
	@Test
	public void test16() throws Exception{
		System.out.println(CommonUtil.googleTranslate("企业级分布式服务平台", E_LANGUAGE.ZHCN, E_LANGUAGE.EN));
		System.out.println(CommonUtil.googleTranslate("交易服务平台", E_LANGUAGE.ZHCN, E_LANGUAGE.EN));
		System.out.println(CommonUtil.googleTranslate("应用开发平台", E_LANGUAGE.ZHCN, E_LANGUAGE.EN));
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年9月9日-下午7:42:20</li>
	 *         <li>功能说明：生成字段属性控制(临时方法)</li>
	 *         </p>
	 * @throws Exception
	 */
	@Test
	public void test17() throws Exception{
		System.out.println(SunlineUtil.sunlineTempBuildJsonControl("LnAccrualInfo"));
	}
	
	@Test
	public void test19() throws SQLException{
		String dataSource = ApiConst.DATASOURCE_ICORE_LN;
		List<Map<String, Object>> loanList = CommonUtil.resolveResultSetToList(JDBCUtils.executeQuery("select loan_no from lna_accrual  where advance_interest_method = 'PRINCIPAL' OR advance_interest_method = 'TOTAL'", dataSource));
		int total = loanList.size();
		int success = 0;
		for(Map<String, Object> map : loanList){
			System.out.println("处理:" + map.get("loan_no"));
			if(JDBCUtils.executeUpdate("update lna_repayment set early_rpym_interest_method = 'NO' where loan_no = ?",new String[]{map.get("loan_no").toString()},dataSource) > 0){
				success++;
			}
		}
		System.out.println("处理完成,总数量:" + total + ",成功数量:" + success);
	}
	
	
	/**
	 * @throws Exception 
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年9月29日-下午1:52:18</li>
	 *         <li>功能说明：网关接口api发布</li>
	 *         </p>
	 */
	@Test
	public void test20() throws Exception{
		SunlineUtil.sunlineGatewayApiRelease(ApiConst.DATASOURCE_ICORE_LN, E_ICOREMODULE.LN,"326999");
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年10月22日-上午10:51:46</li>
	 *         <li>功能说明：查询有效的借据信息列表</li>
	 *         </p>
	 * @throws Exception
	 */
	@Test
	public void test21() throws Exception{
		List<LnaLoan> loanList = SunlineUtil.sunlineGetEffectLoanList(ApiConst.DATASOURCE_ICORE_CBS);
		for(LnaLoan loanInfo : loanList){
			System.out.println(loanInfo);
		}
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年10月29日-上午9:57:52</li>
	 *         <li>功能说明：接口文档自动生成</li>
	 *         </p>
	 * @throws Exception
	 */
	@Test
	public void test23() throws Exception{
		SunlineUtil.sunlineIntfDocumentGenerate("ln6020", outputPath);
	}
	
	
	/**
	 * @throws SQLException 
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月16日-下午2:24:21</li>
	 *         <li>功能说明：接口文档字段校验</li>
	 *         </p>
	 */
	@Test
	public void test6() throws SQLException{
		SunlineUtil.sunlineIntfExcelValidation(ApiConst.DATASOURCE_ICORE_MK_FAT, "0054", "D:/Sunline/SunlineDocument/icore3.x/99-共享文档/04接口清单/", "C:/Users/DELL/Desktop/");
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年10月29日-上午10:58:59</li>
	 *         <li>功能说明：异常配置错误码排序</li>
	 *         </p>
	 * @throws Exception
	 */
	@Test
	public void test24() throws Exception{
		SunlineUtil.sunlineErrorXmlSort(outputPath, true);
	}
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年10月31日-下午5:29:13</li>
	 *         <li>功能说明：删除产品</li>
	 *         </p>
	 * @throws IOException
	 * @throws SQLException 
	 */
	@Test
	public void test26() throws IOException, SQLException{
		SunlineUtil.sunlineDeleteLnProduct(ApiConst.DATASOURCE_ICORE_LN_FAT, "L0000001-1");
	}
	
	
	/**
	 * @throws Exception 
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年10月31日-下午6:50:42</li>
	 *         <li>功能说明：贷款产品同步</li>
	 *         </p>
	 */
	@Test
	public void test27() throws Exception{
		SunlineUtil.sunlineLnProductSync(ApiConst.DATASOURCE_ICORE_LN_FAT, ApiConst.DATASOURCE_ICORE_MK, false);
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年11月4日-上午10:38:43</li>
	 *         <li>功能说明：字段引用字典校验</li>
	 *         </p>
	 * @throws Exception
	 */
	@Test
	public void test28() throws Exception{
		SunlineUtil.sunlineDictRefValidation(outputPath + File.separator + "ln/FieldRefDict");
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年11月4日-上午10:39:00</li>
	 *         <li>功能说明：字典引用枚举校验</li>
	 *         </p>
	 * @throws Exception
	 */
	@Test
	public void test29() throws Exception{
		SunlineUtil.sunlineEnumRefValidation(outputPath + File.separator + "ln");
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年11月13日-下午1:14:46</li>
	 *         <li>功能说明：检查未使用到的错误码</li>
	 *         </p>
	 * @throws Exception
	 */
	@Test
	public void test30() throws Exception{
		SunlineUtil.sunlineCheckUnusedErrorCode(outputPath);
	}
	
	/**
	 * @throws IOException 
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年10月31日-下午1:20:34</li>
	 *         <li>功能说明：全量脚本生成</li>
	 *         </p>
	 */
	@Test
	public void test25() throws IOException{
		SunlineUtil.sunlineFullSQLGenerate(outputPath);
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年11月13日-下午1:15:11</li>
	 *         <li>功能说明：执行全量脚本</li>
	 *         </p>
	 * @throws Exception
	 */
	@Test
	public void test31() throws Exception{
		File file = new File("C:/Users/DELL/Desktop/" + ApiConst.FULLSQL_MAINDIR_NAME);
		SunlineUtil.sunlineExecuteFullSql(file, ApiConst.DATASOURCE_ICORE_LN_BAT);
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年11月22日-下午2:00:43</li>
	 *         <li>功能说明：根据请求报文生成单元测试代码</li>
	 *         </p>
	 * @throws Exception
	 */
	@Test
	public void test32() throws Exception{
		System.out.println(SunlineUtil.sunlineBuildSetFromRequest(ApiConst.DATASOURCE_ICORE_LN));
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年11月22日-下午3:51:10</li>
	 *         <li>功能说明：构建字段不为空代码</li>
	 *         </p>
	 * @throws Exception
	 */
	@Test
	public void test33() throws Exception{
		System.out.println(SunlineUtil.sunlineBuildFieldNotNull("financial_prod_ccy_code","financial_prod_trxn_amt","financial_prod_branch_id"));
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年12月2日-下午1:37:19</li>
	 *         <li>功能说明：生成冲正服务的控制表信息</li>
	 *         </p>
	 * @throws Exception
	 */
	@Test
	public void test34() throws Exception{
		String[] flowtranArray = new String[]{"6172","6040","6020","6047","6038","6051","6050","6041","6044","6045","6046","6149","6153","6075"};
		String deleteSql = "delete from tsp_service_control where inner_service_code in (";
		List<String> serviceList = new ArrayList<String>();
		List<String> insertSqlList = new ArrayList<>();
		
		for(String flowtranId : flowtranArray){
			Document flowtran = SunlineUtil.sunlineSearchFlowtran(E_ICOREMODULE.LN, flowtranId);
			List<Element> list = CommonUtil.searchTargetAllXmlElement(flowtran.getRootElement(), "service");
			for(Element e : list){
				ResultSet resultSet = JDBCUtils.executeQuery("select * from tsp_service_in where inner_service_code = ?", new String[]{e.attributeValue("serviceName")}, ApiConst.DATASOURCE_ICORE_LN);
				while(resultSet.next()){
					if(!serviceList.contains(e.attributeValue("serviceName"))){
						insertSqlList.add("INSERT INTO tsp_service_control (`system_code`, `sub_system_code`, `service_invoke_id`, `inner_service_code`, `inner_service_impl_code`, `description`, `route_keys`, `cancel_service`, `confirm_service`, `service_transaction_mode`, `service_type`, `regist_call_log`, `service_executor_type`) VALUES ('102', '1021', '*', '"+e.attributeValue("serviceName")+"', '"+resultSet.getString("inner_service_impl_code")+"', '"+resultSet.getString("description")+"', '', '', '', 'Supports', 'try', '1', 'L');");
						serviceList.add(e.attributeValue("serviceName"));
						deleteSql += "'"+e.attributeValue("serviceName")+"',";
					}
				}
			}
		}
		deleteSql = deleteSql.substring(0,deleteSql.length() - 1) + ");";
		System.out.println(deleteSql);
		for(String sql : insertSqlList){
			System.out.println(sql);
		}
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年12月2日-下午1:37:48</li>
	 *         <li>功能说明：打印表每个字段的中文</li>
	 *         </p>
	 * @throws Exception
	 */
	@Test
	public void test35() throws Exception{
		Element table = SunlineUtil.sunlineGetTableType("lna_loan");
		List<Element> tableFieldList = CommonUtil.searchTargetAllXmlElement(table, "field");
		for(Element e : tableFieldList){
			System.out.println(e.attributeValue("id") + ":" + SunlineUtil.dictMap.get(e.attributeValue("id")).getDesc());
		}
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年12月19日-上午10:50:38</li>
	 *         <li>功能说明：获取合并请求文件</li>
	 *         </p>
	 * @throws Exception
	 */
	@Test
	public void test36() throws Exception{
		List<String> filePathList = SunlineUtil.sunlineGetMergedFiles(E_STRUCTMODULE.BUSI, "ln-busi", "827");
		for(String filePath : filePathList){
			System.out.println(filePath);
		}
		System.out.println("请求合并文件数量:" + filePathList.size());
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年12月20日-上午10:51:16</li>
	 *         <li>功能说明：多线程发起核心请求</li>
	 *         </p>
	 * @throws Exception
	 */
	@Test
	public void test38() throws Exception{
		final Params params = new Params();
		params.add("loan_amt", "50000");
		params.add("prod_id", "L0000002");
		final int concurrentNum = 2;
		Map<String, Object> responseMap = CommonUtil.multithreadingExecute(CommonUtil.getReflectMethod(SunlineUtil.class, "sunlineSendPostTrxnRequest", String.class, String.class, Params.class), concurrentNum, 50000, ApiConst.POSTMAN_LN_DEV, "326320", params);
		
		for(String threadId : responseMap.keySet()){
			System.out.println(threadId + "->" + responseMap.get(threadId));
		}
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2020年2月28日-下午3:03:13</li>
	 *         <li>功能说明：查询各模块包在nexus仓库的最新版本</li>
	 *         </p>
	 * @throws Exception
	 */
	@Test
	public void test39() throws Exception{
		Map<String, String> versionMap = SunlineUtil.sunlineQueryNexusJarVersion("serv", "cf","us","cl","cm","dp","ds","ln","pt");
		for(String md : versionMap.keySet()){
			System.out.println(md + "模块的最新版本为:" + versionMap.get(md));
		}
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2020年3月2日-上午10:53:35</li>
	 *         <li>功能说明：取稽核脚本sql文件</li>
	 *         </p>
	 * @throws Exception
	 */
	@Test
	public void test40() throws Exception{
		String url = "http://10.22.10.53:8012/";
		String path = "sql/cl/dml/";
		HttpResponse response = NetworkApi.doGet(url, path, new HashMap<String, String>() , new HashMap<String, String>());
		String html = CommonUtil.convertResponseToStr(response);
		org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(html);
		Elements aList = jsoupDoc.getElementsByTag("a");
		
		for(org.jsoup.nodes.Element a : aList){
			if(a.attr("href").equals(a.html()) && !a.html().contains("../")){
				logger.info("下载:" + a.html());
				CommonUtil.downloadUrl(url + a.html(), outputPath + "ln/sqlscriptTools/" + path + a.html());
			}
		}
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2020年3月4日-上午9:39:21</li>
	 *         <li>功能说明：生成前端页面开发增量脚本</li>
	 *         </p>
	 * @throws Exception
	 */
	@Test
	public void test41() throws Exception{
		List<String> sqlList = SunlineUtil.sunlineGenerateMenuSql(E_ICOREMODULE.LN, "5406", "326074", "3240", "贷款预约展期", "/views/ln/restructure/ln_precontract_extend.json");
		for(String sql : sqlList){
			System.out.println(sql);
		}
	}
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2020年3月9日-上午10:04:46</li>
	 *         <li>功能说明：批量替换json中的字段描述</li>
	 *         </p>
	 * @throws Exception
	 */
	@Test
	public void test42() throws Exception{
		int dealCount = 0;
		String path = "D:/JavaDevelop/MyEclipseWorkSpace/sump-vue/src/views/ln";
		String key = "\"control\"";
		Map<String, File> fileMap = CommonUtil.loadPathAllFiles(path);
		for(String fileName : fileMap.keySet()){
			String filePath = fileMap.get(fileName).getPath();
			String fileContent = CommonUtil.readFileContent(filePath);
			if(fileContent.contains(key)){
				/** 批量替换json中的字段描述 **/
				/*CommonUtil.writeFileContent(fileContent.replaceAll(key, "\"初始额度\""), filePath);
				dealCount++;*/
				/** 批量为json中的currency控件新增默认空值 **/
				//TwoTuple<Boolean, String> result = SunlineUtil.sunlineAddDefaultValForCurrency(JSONObject.fromObject(fileContent));
				/** 批量为json中的control及域后字段新增字段长度限制 **/
				String result = SunlineUtil.sunlineAddFieldLengthLimit(JSONObject.fromObject(fileContent));
				if(!JSONObject.fromObject(result).equals(JSONObject.fromObject(fileContent))){
					//CommonUtil.writeFileContent(result, filePath);
					dealCount++;
				}
				/** 加载字段名和字段描述的映射 **/
				//SunlineUtil.reloadCtpControl(JSONObject.fromObject(fileContent));
			}
		}
		logger.info("已处理:"+dealCount);
	}
	
	
	@Test
	public void test43() throws Exception{
		System.out.println(CommonUtil.buildTrxnSeq(24));
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2020年3月17日-下午1:53:13</li>
	 *         <li>功能说明：根据数据库中的表生成java类</li>
	 *         </p>
	 * @throws Exception
	 */
	@Test
	public void test44() throws Exception{
		String javaPackage = "cn.ssy.base.entity.mybatis";
		String tableName = "ctp_language_packet";
		String dataSource = ApiConst.DATASOURCE_ICORE_CT_FAT;
		
		String parseTableName = CommonUtil.parseHumpStr(tableName);
		String output = outputPath  + parseTableName.substring(0, 1).toUpperCase() + parseTableName.substring(1) + ".java";
		CommonUtil.generateTableJava(javaPackage, tableName, dataSource, output);
	}

	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2020年4月15日-下午7:12:38</li>
	 *         <li>功能说明：向核心发起请求</li>
	 *         </p>
	 * @throws Exception
	 */
	@Test
	public void test46() throws Exception{
		//开额度
		//com.alibaba.fastjson.JSONObject output = SunlineUtil.sunlineSendPostTrxnRequest(ApiConst.POSTMAN_CL_FAT, "592000").getSecond();
		//贷款开户
		Params params = new Params().add("prod_id", "L0000002-A").add("specify_amt", "1000");
		System.out.println(SunlineUtil.sunlineSendPostTrxnRequest(ApiConst.POSTMAN_LN_DEV, "326320", params));
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2020年4月15日-下午7:13:17</li>
	 *         <li>功能说明：生成交易参数脚本</li>
	 *         </p>
	 * @throws Exception
	 */
	@Test
	public void test47() throws Exception{
		System.out.println(SunlineUtil.sunlineGenerateTrxnSql(E_ICOREMODULE.LN, "ln6999"));
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2020年4月23日-上午11:26:05</li>
	 *         <li>功能说明：清库</li>
	 *         </p>
	 * @throws Exception
	 */
	@Test
	public void test48() throws Exception{
		String path = "D:/Sunline/sunlineDeveloper/记事本/工具脚本/ln_clear.sql";
		String fullSql = CommonUtil.readFileContent(path);
		System.out.println(fullSql);
		logger.info("生效记录数:" + JDBCUtils.executeUpdate(CommonUtil.parseStringToList(fullSql, "\r\n"), ApiConst.DATASOURCE_ICORE_LN_BAT));
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2020年5月12日-下午1:50:06</li>
	 *         <li>功能说明：快速开发模型生成</li>
	 *         </p>
	 * @throws Exception
	 */
	@Test
	public void test49() throws Exception {
		/**
		 * 生成复合类型
		 */
		String[] inFieldArray = CommonUtil.parseStringToArray(CommonUtil.readFileContent(outputPath + "/in.txt"), "\r\n");
		String[] outFieldArray = CommonUtil.parseStringToArray(CommonUtil.readFileContent(outputPath + "/out.txt"), "\r\n");
		final String complexTypeName = "ComLnBatch";
		final String complexTypeId = "LnBatStartup";
		final String complexLongname = "loan batch launcher ";
		SunlineUtil.sunlineWriteComplex(inFieldArray, complexTypeName, complexTypeId + "In", complexLongname + " input");
		SunlineUtil.sunlineWriteComplex(outFieldArray, complexTypeName, complexTypeId + "Out", complexLongname + " output");
		
		/**
		 * 生成服务
		 */
		final String serviceTypeName = "SrvLnBatch";
		final String serviceId = "startupBatch";
		final String serviceLongname = "Startup Batch";
		final String variableName = "batLauncher";
		SunlineUtil.sunlineWriteServiceType(complexTypeName, complexTypeId, complexLongname, serviceTypeName, serviceId, serviceLongname, variableName);
	
		/**
		 * 生成flowtran
		 */
		final String flowtranId = "ln6999";
		final String kind = "F";
		SunlineUtil.sunlineWriteFlowtran(inFieldArray, outFieldArray, serviceTypeName, serviceId, serviceLongname, variableName, flowtranId, kind);
	}
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2020年5月21日-下午3:55:40</li>
	 *         <li>功能说明：生成mybatis相关代码</li>
	 *         </p>
	 * @throws Exception
	 */
	@Test
	public void test50() throws Exception {
		String confPath = SimpleTest.class.getResource("/generatorConfig/LnGeneratorConfig.xml").getPath();
		CommonUtil.mybatisGeneratorProcess(confPath, true);
	}
	
	
	@Test
	public void test51() throws Exception {
		System.out.println(new SimpleDateFormat("yyyy-MM-dd").parse("2020-01-01"));
	}
}	