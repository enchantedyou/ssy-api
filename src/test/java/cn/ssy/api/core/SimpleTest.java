package cn.ssy.api.core;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;

import net.sf.json.JSONObject;

import org.junit.Before;
import org.junit.Test;

import cn.ssy.base.core.utils.BatTaskUtil;
import cn.ssy.base.core.utils.CommonUtil;
import cn.ssy.base.core.utils.JDBCUtils;
import cn.ssy.base.core.utils.RedisOperateUtil;
import cn.ssy.base.core.utils.SunlineUtil;
import cn.ssy.base.entity.consts.ApiConst;
import cn.ssy.base.enums.E_ICOREMODULE;
import cn.ssy.base.enums.E_LANGUAGE;
import cn.ssy.base.enums.E_LAYOUTTYPE;
import cn.ssy.base.enums.E_PACKAGETYPE;
import cn.ssy.base.exception.NullParmException;

public class SimpleTest{
	
	public String outputPath = "C:/Users/36045/Desktop/";
	
	@Before
	public void before(){
		SunlineUtil.sunlineInitializer("C:/sunline/sunlineWorkspace/icore3.0/", null, null, true);
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
		SunlineUtil.sunlineSearchDict("project_no_head");
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
		
		System.out.println(SunlineUtil.sunlineBuildCtTabJson("LnDrawdownInfo"));
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
		SunlineUtil.sunlineGatewayApiRelease(ApiConst.DATASOURCE_BATCORE, E_ICOREMODULE.LN);
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
		SunlineUtil.sunlineIntfDocumentGenerate("ln6073", outputPath);
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
		SunlineUtil.sunlineIntfExcelValidation(E_ICOREMODULE.LN, "6041", "C:/sunline/sunlineDocument/document/icore3.x/99-共享文档/04接口清单/LN-贷款", "C:/Users/36045/Desktop/");
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
	 *         <li>2019年10月31日-下午5:29:13</li>
	 *         <li>功能说明：删除产品</li>
	 *         </p>
	 * @throws IOException
	 */
	@Test
	public void test26() throws IOException{
		SunlineUtil.sunlineDeleteLnProduct(ApiConst.DATASOURCE_ICORE_LN_DIT, "L000000a");
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
		SunlineUtil.sunlineLnProductSync(ApiConst.DATASOURCE_ICORE_LN, ApiConst.DATASOURCE_ICORE_LN_DIT, false);
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
		RedisOperateUtil redisOperateUtil = new RedisOperateUtil();
		System.out.println(redisOperateUtil.getHashValue(ApiConst.REDIS_PROJECT_FILE_KEY, "LnServDict.d_schema.xml"));
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
	
	
	@Test
	public void test30() throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("key", 123);
		if(map.get("key") instanceof BigDecimal){
			System.out.println(new BigDecimal(String.valueOf(map.get("key"))));
		}
		System.out.println(map.get("key").getClass());
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年10月31日-下午6:29:46</li>
	 *         <li>功能说明：请求报文转换</li>
	 *         </p>
	 * @param param
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String requestMsgTransform(String param){
		if(CommonUtil.isNull(param)){
			throw new NullParmException("请求数据");
		}
		Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
		StringTokenizer tokenizer = new StringTokenizer(param,"&");
		
		while(tokenizer.hasMoreTokens()){
			String token = tokenizer.nextToken();
			if(token.contains("=")){
				String[] tokenArray = token.split("=");
				String key = tokenArray[0];
				String value = tokenArray[1];
				
				if(resultMap.keySet().contains(key)){
					List<String> list = new ArrayList<String>();
					if(resultMap.get(key) instanceof String){
						list.add(String.valueOf(resultMap.get(key)));
						list.add(value);
					}else if(resultMap.get(key) instanceof List<?>){
						list = (List<String>) resultMap.get(key);
						list.add(value);
					}
					resultMap.put(key, list);
				}else{
					if(value.contains(",")){
						List<String> list = new ArrayList<String>();
						String[] valueArray = value.split(",");
						for(String v : valueArray){
							list.add(v);
						}
						resultMap.put(key, list);
					}else{
						resultMap.put(key, value);
					}
				}
			}
		}
		return JSONObject.fromObject(resultMap).toString();
	}
}
