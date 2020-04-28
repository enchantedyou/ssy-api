package cn.ssy.base.core.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import org.apache.log4j.Logger;
import org.springframework.util.StopWatch;

import cn.ssy.base.entity.mybatis.AppDate;
import cn.ssy.base.entity.mybatis.TspFlowStepController;
import cn.ssy.base.entity.mybatis.TspTask;
import cn.ssy.base.entity.mybatis.TspTaskExecution;
import cn.ssy.base.entity.mybatis.TspTranController;
import cn.ssy.base.enums.E_ICOREMODULE;
import cn.ssy.base.exception.BatBusinessException;
import cn.ssy.base.thread.BatchProcessThread;


/**
 * <p>
 * 文件功能说明：
 *       	批量任务工具类		
 * </p>
 * 
 * @Author sunshaoyu
 *         <p>
 *         <li>2019年8月17日-上午11:16:40</li>
 *         <li>修改记录</li>
 *         <li>-----------------------------------------------------------</li>
 *         <li>标记：修订内容</li>
 *         <li>2019年8月17日-sunshaoyu：创建注释模板</li>
 *         <li>-----------------------------------------------------------</li>
 *         </p>
 */
public class BatTaskUtil {
	
	private static final Logger logger = Logger.getLogger(BatTaskUtil.class);
	
	static FutureTask<TspTaskExecution> listenerThreadTask = null;
	
	public final static String SUCCESS_STATE = "success";
	
	public final static String FAILURE_STATE = "failure";
	
	public final static Map<String, String> batSettingMap = CommonUtil.readPropertiesSettings(BatTaskUtil.class.getResource("/bat.properties").getPath());
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月17日-下午12:46:05</li>
	 *         <li>功能说明：获取批量任务列表</li>
	 *         </p>
	 * @return
	 * @throws SQLException 
	 */
	public static List<TspTranController> getBatTaskList() throws SQLException{
		String sql = "select * from tsp_tran_controller order by cast(tran_group_id as signed),step_id;";
		ResultSet resultSet = JDBCUtils.executeQuery(sql, batSettingMap.get("datasource"));
		List<TspTranController> taskList = CommonUtil.mappingResultSetList(resultSet, TspTranController.class);
		return taskList;
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月17日-下午12:48:43</li>
	 *         <li>功能说明：根据批量交易码和步骤号获取批量信息</li>
	 *         </p>
	 * @param tranCode	批量交易码
	 * @return
	 * @throws SQLException 
	 */
	public static TspTranController getBatTaskByTranCode(String tranCode, Integer stepId) throws SQLException{
		String sql = "select * from tsp_tran_controller where step_id = ? and tran_code = ? and execution_code = '1' order by step_id;";
		ResultSet resultSet = JDBCUtils.executeQuery(sql, new String[]{String.valueOf(stepId),tranCode},batSettingMap.get("datasource"));
		return CommonUtil.mappingResultSetSingle(resultSet, TspTranController.class);
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2020年3月14日-下午4:45:11</li>
	 *         <li>功能说明：根据批量交易码和步骤号获取流程信息</li>
	 *         </p>
	 * @param tranCode
	 * @param stpId
	 * @return
	 * @throws SQLException
	 */
	public static String getTranFlowId(String tranCode, Integer stepId) throws SQLException{
		String sql = "select b.tran_flow_id from tsp_tran_controller a,tsp_flow_step_controller b where a.tran_group_id = b.tran_group_id and a.tran_code = ? and a.step_id = ?";
		ResultSet resultSet = JDBCUtils.executeQuery(sql, new String[]{tranCode, String.valueOf(stepId)}, batSettingMap.get("datasource"));
		String tranFlowId = null;
		if(resultSet.next()){
			tranFlowId = resultSet.getString("tran_flow_id");
		}
		JDBCUtils.close();
		return tranFlowId;
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2020年4月26日-上午11:03:50</li>
	 *         <li>功能说明：</li>
	 *         </p>
	 * @param days
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 * @throws SQLException 
	 */
	public static void tryStartupTask(int days) throws SQLException, InterruptedException, ExecutionException{
		int totalDays = days;
		while(days-- > 0){
			long start = System.currentTimeMillis();
			BatTaskUtil.tryStartupTask();
			long end = System.currentTimeMillis();
			
			logger.info("\r\n" + CommonUtil.buildSplitLine(50) + "第["+(totalDays - days)+"]天批量任务执行完成,耗时:" + (end - start) + "ms" + CommonUtil.buildSplitLine(50) + "\r\n");
			CommonUtil.systemPause(1000);
		}
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2020年3月16日-下午4:42:54</li>
	 *         <li>功能说明：尝试启动贷款批量</li>
	 *         </p>
	 * @throws SQLException 
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	public static void tryStartupTask() throws SQLException, InterruptedException, ExecutionException{
		List<TspFlowStepController> flowStepControlList = CommonUtil.mappingResultSetList(JDBCUtils.executeQuery("select * from tsp_flow_step_controller order by cast(tran_group_id as signed)", batSettingMap.get("datasource")), TspFlowStepController.class);
		for(TspFlowStepController tspFlowStepController : flowStepControlList){
			AppDate appDate = CommonUtil.mappingResultSetSingle(JDBCUtils.executeQuery("select * from app_date", batSettingMap.get("datasource")), AppDate.class);
			//日切前判断
			if("Switch".equals(tspFlowStepController.getTranFlowId())){
				List<TspTask> taskList = CommonUtil.mappingResultSetList(JDBCUtils.executeQuery("select * from tsp_task where transaction_date = ? and tran_state = 'failure'", new String[]{appDate.getTrxnDate()}, batSettingMap.get("datasource")), TspTask.class);
				//当天有失败的批量,不日切
				if(CommonUtil.isNotNull(taskList)){
					logger.info(CommonUtil.buildSplitLine(30) + "交易日期["+ appDate.getTrxnDate() +"]存在执行失败的批量,不进行日切" + CommonUtil.buildSplitLine(30));
					continue;
				}
			}
			String taskNum = tspFlowStepController.getTranFlowId() + "_" + appDate.getTrxnDate() + "_" + appDate.getBusiOrgId(); 
			logger.info(CommonUtil.buildSplitLine(50) + taskNum + "  begin" + CommonUtil.buildSplitLine(50));
			
			TspTranController tspTranController = CommonUtil.mappingResultSetSingle(JDBCUtils.executeQuery("select * from tsp_tran_controller where tran_group_id = ? order by cast(step_id as signed) limit 1", new String[]{tspFlowStepController.getTranGroupId()}, batSettingMap.get("datasource")), TspTranController.class);
			startupTask(tspTranController.getTranGroupId(), tspTranController.getTranCode(), tspTranController.getStepId(), taskNum);
			logger.info(CommonUtil.buildSplitLine(50) + taskNum + "  end" + CommonUtil.buildSplitLine(50));
		}
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月17日-下午1:09:28</li>
	 *         <li>功能说明：启动贷款批量</li>
	 *         </p>
	 * @param tranCode
	 * @throws SQLException 
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	private static void startupTask(String tranGroupId, String tranCode,Integer stepId,String taskNum) throws SQLException, InterruptedException, ExecutionException{
		String trxnDate = CommonUtil.fetchResultSetValue(JDBCUtils.executeQuery("select * from app_date", batSettingMap.get("datasource")), "trxn_date");
		TspTranController bat = getBatTaskByTranCode(tranCode, stepId);
		if(CommonUtil.isNull(bat) || CommonUtil.isNull(trxnDate)){
			logger.error("批量交易码为["+tranCode+"]的批量不存在或不可执行");
			return;
		}
		long timestamp = System.currentTimeMillis();
		String dataArea = "{\"input\":{},\"sys\":{\"tran_flow_id\":\"CoreEOD\",\"servicecode\":\"onTimeSyscRemoteDirectory\",\"error_id\":null,\"pljypich\":\"onTimeSyscRemoteDirectorybatch.5B7C307FF6D44EDA97B596A794118898\"},\"comm_req\":{\"initiator_system\":\""+batSettingMap.get("systemCode")+"\",\"trxn_branch\":\""+batSettingMap.get("trxnBranch")+"\",\"trxn_teller\":\"S####\",\"sponsor_system\":\""+batSettingMap.get("systemCode")+"\",\"timerName\":\"onTimeSyscRemoteDirectory\",\"jiaoyirq\":\""+trxnDate+"\",\"busi_org_id\":\""+batSettingMap.get("tenantId")+"\",\"channel_id\":\""+batSettingMap.get("channelId")+"\"}}";
		logger.info("当前批量任务标识:" + taskNum);
		String dateformatDate = trxnDate.substring(0, 4) + "-" + trxnDate.substring(4,6) + "-" + trxnDate.substring(6);
		
		
		boolean startupSuccessInd = false;
		String[] checkParameter = new String[]{taskNum, trxnDate};
		String querySql = "select * from tsp_task where sub_system_code = '"+batSettingMap.get("subSystemId")+"' and task_num = ? and transaction_date = ? and system_code = '"+batSettingMap.get("systemCode")+"' and corporate_code = '"+batSettingMap.get("tenantId")+"'";
		
		TspTask tspTask = CommonUtil.mappingResultSetSingle(JDBCUtils.executeQuery(querySql,checkParameter , batSettingMap.get("datasource")), TspTask.class);
		if(CommonUtil.isNotNull(tspTask)){
			if(!SUCCESS_STATE.equals(tspTask.getTranState())){
				logger.info("任务["+ taskNum +"]执行失败,重新执行");
				String updateSql = "update tsp_task set tran_state = 'onprocess' where sub_system_code = '"+batSettingMap.get("subSystemId")+"' and task_num = ? and transaction_date = ? and system_code = '"+batSettingMap.get("systemCode")+"' and corporate_code = '"+batSettingMap.get("tenantId")+"'";
				startupSuccessInd = JDBCUtils.executeUpdate(updateSql,checkParameter, batSettingMap.get("datasource")) > 0;
				String updateSql2 = "update tsp_task_execution set tran_state = 'onprocess' where sub_system_code = '"+batSettingMap.get("subSystemId")+"' and task_num = ? and transaction_date = ? and system_code = '"+batSettingMap.get("systemCode")+"' and corporate_code = '"+batSettingMap.get("tenantId")+"'";
				startupSuccessInd = JDBCUtils.executeUpdate(updateSql2,checkParameter, batSettingMap.get("datasource")) > 0;
			}else{
				logger.info("任务["+ taskNum +"]已成功,跳过执行");
				return;
			}
		}else{
			logger.info("新建任务["+ taskNum +"]");
			String taskSql = "INSERT INTO `tsp_task` (`system_code`, `corporate_code`, `task_num`, `task_exe_num`, `task_commit_date`, `tran_date`, `transaction_date`, `tran_flow_id`, `flow_step_num`, `tran_group_id`, `tran_id`, `total_cost`, `tran_state`, `task_exe_mode`, `task_interrupt_flag`, `task_commit_time`, `task_priority`, `tran_start_time`, `tran_start_timestamp`, `tran_end_time`, `tran_end_timestamp`, `vm_id`, `ip_address`, `server_host_name`, `data_area`, `start_flow_step_num`, `start_execution_no`, `start_tran_group_id`, `start_step_num`, `error_message`, `error_stack`, `service_code`, `sub_system_code`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
			String[] taskParameter = new String[]{E_ICOREMODULE.LN.getSysCode(),batSettingMap.get("tenantId"),taskNum,String.valueOf(timestamp),trxnDate,dateformatDate,trxnDate,getTranFlowId(tranCode, stepId),String.valueOf(bat.getStepId()),bat.getTranGroupId(),tranCode,null,"onprocess","1",null,CommonUtil.getCurSysTime(),null,null,null,null,null,null,null,null,dataArea,"0","0",null,"0","","",""+batSettingMap.get("serverIp")+"#"+batSettingMap.get("module")+"#bat",batSettingMap.get("subSystemId")};
			startupSuccessInd = JDBCUtils.executeUpdate(taskSql, taskParameter, batSettingMap.get("datasource")) > 0;
		}
		
		if(startupSuccessInd){
			logger.info("步骤号为["+stepId+"]的批量交易["+tranCode+"]启动成功");
			
			//监听批量任务
			BatchProcessThread listener = new BatchProcessThread(taskNum, dateformatDate,tranCode);
			listenerThreadTask = new FutureTask<>(listener);
			listenerThreadTask.run();
		}else{
			logger.error("步骤号为["+stepId+"]的批量交易["+tranCode+"]启动失败");
		}
		printBatchTastExecuteRes();
	}
	
	
	
	/**
	 * @throws SQLException 
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月17日-下午2:45:30</li>
	 *         <li>功能说明：批量任务执行结果打印</li>
	 *         </p>
	 */
	private static boolean printBatchTastExecuteRes() throws SQLException, InterruptedException, ExecutionException{
		boolean isFailure = true;
		TspTaskExecution tspTaskExecution = listenerThreadTask.get();
		isFailure = FAILURE_STATE.equals(tspTaskExecution.getTranState());
		//查询新的交易日期
		ResultSet resultSet = JDBCUtils.executeQuery("select * from app_date", batSettingMap.get("datasource"));
		Object appDate = tspTaskExecution.getTranDate();
		if(resultSet.next()){
			appDate = resultSet.getString("trxn_date");
		}
		JDBCUtils.close();
		logger.info("交易日期:"+appDate+",批量["+tspTaskExecution.getTaskNum()+"]执行结果:" + tspTaskExecution.getTranState());
		if(isFailure){
			logger.info("错误信息:" + tspTaskExecution.getErrorMessage());
			logger.info("错误堆栈:" + tspTaskExecution.getErrorStack());
			throw new BatBusinessException("批量任务执行异常");
		}
		return !isFailure;
	}
}
