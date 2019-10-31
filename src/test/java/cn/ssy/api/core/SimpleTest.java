package cn.ssy.api.core;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import cn.ssy.base.core.utils.BatTaskUtil;
import cn.ssy.base.core.utils.CommonUtil;
import cn.ssy.base.core.utils.JDBCUtils;
import cn.ssy.base.core.utils.SunlineUtil;
import cn.ssy.base.entity.consts.ApiConst;
import cn.ssy.base.entity.mybatis.TspTranController;
import cn.ssy.base.enums.E_ICOREMODULE;
import cn.ssy.base.enums.E_LANGUAGE;
import cn.ssy.base.enums.E_LAYOUTTYPE;
import cn.ssy.base.enums.E_PACKAGETYPE;

public class SimpleTest{
	
	public String outputPath = "C:/Users/36045/Desktop/";
	
	@Before
	public void before(){
		SunlineUtil.sunlineInitializer("C:/sunline/sunlineWorkspace/icore3.0/", null, null);
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
		SunlineUtil.sunlineBuildTspServiceInSql(E_ICOREMODULE.LN, "C:/sunline/sunlineDeveloper/文档/icore3.0研发/1#贷款接口清单.xlsx", "102", "1021","C:/Users/36045/Desktop/");
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
		SunlineUtil.sunlineSearchDict("auto_dwdn_ind");
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月8日-上午10:47:15</li>
	 *         <li>功能说明：部署打包</li>
	 *         </p>
	 */
	@Test
	public void test2(){
		String dir = "C:/Users/36045/Desktop/ln/";
		File[] fileArr = new File(dir).listFiles();
		for(File file : fileArr){
			file.delete();
		}
		SunlineUtil.sunlinePackageServiceJar(E_ICOREMODULE.LN, E_PACKAGETYPE.ALL, "3.1.0.0-SNAPSHOT", dir,true);
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月8日-上午10:47:54</li>
	 *         <li>功能说明：获取请求报文</li>
	 *         </p>
	 */
	@Test
	public void test3(){
		List<String> requestList = SunlineUtil.sunlineGetYfditSuccessReq(6200);
		if(CommonUtil.isNotNull(requestList)){
			for(String req : requestList){
				System.out.println(req);
			}
		}
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月15日-下午7:21:40</li>
	 *         <li>功能说明：获取批量控制表插入语句</li>
	 *         </p>
	 */
	@Test
	public void test4(){
		List<String> sqlList = SunlineUtil.sunlineGetTranControlSql(E_ICOREMODULE.LN);
		if(CommonUtil.isNotNull(sqlList)){
			for(String sql : sqlList){
				System.out.println(sql);
			}
		}
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月15日-下午7:32:10</li>
	 *         <li>功能说明：获取批量分组表插入语句</li>
	 *         </p>
	 */
	@Test
	public void test5(){
		List<String> sqlList = SunlineUtil.sunlineGetTranGroupSql(E_ICOREMODULE.LN);
		if(CommonUtil.isNotNull(sqlList)){
			for(String sql : sqlList){
				System.out.println(sql);
			}
		}
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月30日-下午1:35:08</li>
	 *         <li>功能说明：启动批量</li>
	 *         </p>
	 */
	@Test
	public void test7(){
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
		System.out.println(SunlineUtil.sunlineBuildCtFormJson("LnRepaymentTrialOutType"));
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
		/*System.out.println(SunlineUtil.sunlineBuildCtTabJson("LnDrawdownInfo"));
		System.out.println(SunlineUtil.sunlineBuildCtTabJson("LnRepaymentInfo"));
		System.out.println(SunlineUtil.sunlineBuildCtTabJson("LnAccrualInfo"));
		System.out.println(SunlineUtil.sunlineBuildCtTabJson("LnMaturityInfo"));
		System.out.println(SunlineUtil.sunlineBuildCtTabJson("LnFieldControlInfo"));*/
		
		System.out.println(SunlineUtil.sunlineBuildCtTabJson("LnScheduleListQueryOut"));
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月30日-下午1:36:03</li>
	 *         <li>功能说明：杀死数据库死锁进程</li>
	 *         </p>
	 */
	@Test
	public void test10(){
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
		System.out.println(SunlineUtil.sunlineSendPostTrxnRequest("326020", CommonUtil.readFileContent("C:/Users/36045/Desktop/ln/body_dev.json")));
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
	public void test19(){
		String dataSource = ApiConst.DATASOURCE_ICORE_LN;
		List<Map<String, Object>> loanList = CommonUtil.resolveResultSetToList(JDBCUtils.executeQuery("select loan_no from lna_accrual  where advance_interest_method = 'PRINCIPAL' OR advance_interest_method = 'TOTAL'", dataSource));
		int total = loanList.size();
		int success = 0;
		for(Map<String, Object> map : loanList){
			System.out.println("处理:" + map.get("loan_no"));
			if(JDBCUtils.executeUpdate("update lna_repayment set early_rpym_interest_method = 'NO' where loan_no = ?",new String[]{map.get("loan_no").toString()},dataSource)){
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
		SunlineUtil.sunlineGatewayApiRelease(ApiConst.DATASOURCE_ICORE_LN_DIT, E_ICOREMODULE.LN,"");
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
	 *         <li>2019年10月23日-下午4:49:50</li>
	 *         <li>功能说明：</li>
	 *         </p>
	 */
	@Test
	public void test22(){
		List<TspTranController> taskList = BatTaskUtil.getBatTaskList();
		String tranChineseName = "";
		for(TspTranController task : taskList){
			if(task.getStepId().equals(131) && task.getTranGroupId().equals("830")){
				tranChineseName = task.getTranChineseName();
				break;
			}
		}
		System.out.println(tranChineseName);
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
		SunlineUtil.sunlineIntfDocumentGenerate("co6230", outputPath);
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
		SunlineUtil.sunlineIntfExcelValidation(E_ICOREMODULE.LN, "6009", "C:/sunline/sunlineDocument/document/icore3.x/99-共享文档/04接口清单/LN-贷款", "C:/Users/36045/Desktop/");
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
}
