package cn.ssy.api.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.dom4j.Document;
import org.dom4j.Element;
import org.junit.Before;
import org.junit.Test;

import cn.ssy.base.core.utils.BatTaskUtil;
import cn.ssy.base.core.utils.CommonUtil;
import cn.ssy.base.core.utils.JDBCUtils;
import cn.ssy.base.core.utils.SunlineUtil;
import cn.ssy.base.entity.consts.ApiConst;
import cn.ssy.base.enums.E_ICOREMODULE;
import cn.ssy.base.enums.E_LANGUAGE;
import cn.ssy.base.enums.E_LAYOUTTYPE;
import cn.ssy.base.enums.E_STRUCTMODULE;

public class SimpleTest{
	
	public String outputPath = "C:/Users/DELL/Desktop/";
	
	//log4j日志
	//private static final Logger logger = Logger.getLogger(SimpleTest.class);
	
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
		SunlineUtil.sunlineSearchDict("dwdn_fund_source");
	}
	
	
	/**
	 * @throws SQLException 
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月8日-上午10:47:54</li>
	 *         <li>功能说明：获取请求报文</li>
	 *         </p>
	 */
	@Test
	public void test3() throws SQLException{
		List<String> requestList = SunlineUtil.sunlineGetYfditSuccessReq(6200);
		if(CommonUtil.isNotNull(requestList)){
			for(String req : requestList){
				System.out.println(req);
			}
		}
	}
	
	
	/**
	 * @throws SQLException 
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月15日-下午7:21:40</li>
	 *         <li>功能说明：获取批量控制表插入语句</li>
	 *         </p>
	 */
	@Test
	public void test4() throws SQLException{
		List<String> sqlList = SunlineUtil.sunlineGetTranControlSql(E_ICOREMODULE.LN);
		if(CommonUtil.isNotNull(sqlList)){
			for(String sql : sqlList){
				System.out.println(sql);
			}
		}
	}
	
	
	/**
	 * @throws SQLException 
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月15日-下午7:32:10</li>
	 *         <li>功能说明：获取批量分组表插入语句</li>
	 *         </p>
	 */
	@Test
	public void test5() throws SQLException{
		List<String> sqlList = SunlineUtil.sunlineGetTranGroupSql(E_ICOREMODULE.LN);
		if(CommonUtil.isNotNull(sqlList)){
			for(String sql : sqlList){
				System.out.println(sql);
			}
		}
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
	public void test7() throws SQLException{
		BatTaskUtil.startupTask("ap05",40,"");
		BatTaskUtil.printBatchTastExecuteRes();
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月30日-下午1:35:17</li>
	 *         <li>功能说明：构建内管表单json</li>
	 *         </p>
	 */
	@Test
	public void test8(){
		//System.out.println(SunlineUtil.sunlineBuildCtFormJson("IoLnLoanNormalOpenIn"));
		//System.out.println(SunlineUtil.sunlineBuildCtFormJson("LnQueryLoanInfoOut"));
		//System.out.println(SunlineUtil.sunlineBuildCtFormJson("IoLnWriteOffRepaymentIn"));
		System.out.println(SunlineUtil.sunlineBuildCtFormJson("LnLoanRepayOut"));
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
		System.out.println(SunlineUtil.sunlineBuildCtEventsJson("rpym_type", true, false,"overpayment_amt","loan_rpym_amt","rpym_amt"));
	}
	
	/**
	 * limit_ind
	 */
	
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
		System.out.println(SunlineUtil.sunlineBuildCtTabJson("IoLnJointIn"));
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
		SunlineUtil.sunlineKillProcess(ApiConst.DATASOURCE_ICORE_LN);
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
	 * @throws Exception 
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年9月6日-下午3:14:46</li>
	 *         <li>功能说明：单次请求测试</li>
	 *         </p>
	 */
	@Test
	public void test15() throws Exception{
		System.out.println(SunlineUtil.sunlineSendPostTrxnRequest("326020", CommonUtil.readFileContent("C:/Users/DELL/Desktop/ln/body_dev.json")));
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年9月6日-下午4:35:13</li>
	 *         <li>功能说明：多线程测试</li>
	 *         </p>
	 */
	@Test
	public void test14() throws Exception{
		Method method = CommonUtil.getReflectMethod(SunlineUtil.class, "sunlineSendPostTrxnRequest", String.class,String.class);
		Map<String, Object> returnMap = CommonUtil.multithreadingExecute(method, 5, 0, "326020",CommonUtil.readFileContent("C:/sunline/sunlineDeveloper/记事本/json/body_atp.json"));
		for(String thread : returnMap.keySet()){
			System.out.println("线程[" + thread + "]执行结果:");
			System.out.println(returnMap.get(thread));
		}
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
	public void test18() throws Exception{
		double crit = 0.04;
		double sunderArmor = 0.7199;
		double critEffect = 1.75;
		double initHarm = 30000;
		
		double totalHarm = 0;
		Random rand = new Random();
		System.out.println("会心:" + (crit * 100) + "%,破防:" + (sunderArmor * 100) + "%,会效:" + (critEffect * 100) + "%,初始伤害:" + initHarm);
		System.out.println(CommonUtil.buildSplitLine(120));
		
		for(int i = 0;i < 10;i++){
			double curHarm = 0;
			if(rand.nextDouble() < crit){
				curHarm = initHarm * critEffect * (1 + sunderArmor);
				System.err.println("驱夜断愁·会心:" + curHarm);
			}else{
				curHarm = initHarm * (1 + sunderArmor);
				System.out.println("驱夜断愁:" + curHarm);
			}
			totalHarm += curHarm;
		}
		CommonUtil.systemPause(5);
		System.out.println(CommonUtil.buildSplitLine(120));
		System.out.println("伤害总计:" + totalHarm);
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
		SunlineUtil.sunlineGatewayApiRelease(ApiConst.DATASOURCE_ICORE_LN, E_ICOREMODULE.LN,"329999");
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
		List<Map<String, Object>> loanList = SunlineUtil.sunlineGetEffectLoanList(ApiConst.DATASOURCE_ICORE_LN_DIT);
		for(Map<String, Object> map : loanList){
			System.out.println(map.toString());
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
		SunlineUtil.sunlineIntfDocumentGenerate("ln6153", outputPath);
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月16日-下午2:24:21</li>
	 *         <li>功能说明：接口文档字段校验</li>
	 *         </p>
	 */
	@Test
	public void test6(){
		SunlineUtil.sunlineIntfExcelValidation(E_ICOREMODULE.LN, "6021", "D:/Sunline/SunlineDocument/icore3.x/99-共享文档/04接口清单/LN-贷款", "C:/Users/DELL/Desktop/");
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
		SunlineUtil.sunlineDeleteLnProduct(ApiConst.DATASOURCE_ICORE_LN, "L0000002-G");
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
		SunlineUtil.sunlineLnProductSync(ApiConst.DATASOURCE_ICORE_LN, ApiConst.DATASOURCE_ICORE_LN_DIT, false,"L0000003-A");
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
		SunlineUtil.sunlineDictRefValidation(outputPath + File.separator + "ln");
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
		SunlineUtil.sunlineExecuteFullSql(file, ApiConst.DATASOURCE_LOCAL_CHECK);
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年11月22日-下午2:00:43</li>
	 *         <li>功能说明：根据请求报文生产set语句</li>
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
	 *         <li>功能说明：</li>
	 *         </p>
	 * @throws Exception
	 */
	@Test
	public void test35() throws Exception{
		Element table = SunlineUtil.sunlineGetTableType("lnf_fee");
		List<Element> tableFieldList = CommonUtil.searchTargetAllXmlElement(table, "field");
		for(Element e : tableFieldList){
			System.out.println("<element id=\""+e.attributeValue("id")+"\" longname=\""+e.attributeValue("longname")+"\" type=\""+e.attributeValue("type")+"\" ref=\""+e.attributeValue("ref")+"\" required=\"false\" multi=\"false\" range=\"false\" array=\"false\" final=\"false\" override=\"false\" allowSubType=\"true\"/>");
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
		List<String> filePathList = SunlineUtil.sunlineGetMergedFiles(E_STRUCTMODULE.BUSI, "sump-vue", "1123");
		for(String filePath : filePathList){
			System.out.println(filePath);
		}
		System.out.println("请求合并文件数量:" + filePathList.size());
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年12月20日-上午10:51:16</li>
	 *         <li>功能说明：</li>
	 *         </p>
	 * @throws Exception
	 */
	@Test
	public void test38() throws Exception{
		for(String fileName : SunlineUtil.projectFileMap.keySet()){
			String filePath = SunlineUtil.projectFileMap.get(fileName);
			if(filePath.contains("ln-serv")){
				filePath = "D:/Sunline/sunlineWorkspace/icore3.0/ln-busi/ln-serv/src/main/java/cn/sunline/icore/ln/serv/drawdown/LnLoanOpenStandardCheck.java";
				BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filePath))));
				String line = "";
				int lineCount = 0;
				while((line = reader.readLine()) != null){
					System.out.println(line);
					lineCount++;
				}
				System.out.println(lineCount);
				reader.close();
				break;
			}
		}
	}
}
