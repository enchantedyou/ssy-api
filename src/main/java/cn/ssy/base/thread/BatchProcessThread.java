package cn.ssy.base.thread;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.apache.log4j.Logger;

import cn.ssy.base.core.utils.BatTaskUtil;
import cn.ssy.base.core.utils.CommonUtil;
import cn.ssy.base.core.utils.JDBCUtils;
import cn.ssy.base.entity.mybatis.TspTranController;
import cn.ssy.base.entity.plugins.TwoTuple;


/**
 * <p>
 * 文件功能说明：
 *       	批量任务监听执行器		
 * </p>
 * 
 * @Author sunshaoyu
 *         <p>
 *         <li>2019年8月17日-下午2:36:17</li>
 *         <li>修改记录</li>
 *         <li>-----------------------------------------------------------</li>
 *         <li>标记：修订内容</li>
 *         <li>2019年8月17日-sunshaoyu：创建注释模板</li>
 *         <li>-----------------------------------------------------------</li>
 *         </p>
 */
public class BatchProcessThread implements Callable<Map<String, Object>>{
	
	private static final Logger logger = Logger.getLogger(BatchProcessThread.class);
	private String taskNum;
	private String trxnDate;
	private String tranId;
	private long delay = CommonUtil.isNull(BatTaskUtil.batSettingMap.get("fetchStatusDelay")) ? 2000L : Long.parseLong(BatTaskUtil.batSettingMap.get("fetchStatusDelay"));
	
	public BatchProcessThread(String taskNum, String trxnDate,String tranId) {
		super();
		this.taskNum = taskNum;
		this.trxnDate = trxnDate;
		this.tranId = tranId;
	}

	@Override
	public Map<String, Object> call() throws Exception {
		Map<String, Object> resMap = new HashMap<String, Object>();
		String sql = "select * from tsp_task_execution where system_code = '"+BatTaskUtil.batSettingMap.get("systemCode")+"' and corporate_code = '"+BatTaskUtil.batSettingMap.get("tenantId")+"' and task_num = ? and tran_id = ? and sub_system_code = '"+BatTaskUtil.batSettingMap.get("subSystemId")+"' order by tran_start_time desc limit 0,1";
		String tranState = null;
		String errorMsg = null;
		
		String errorStack = null;
		String beforeTask = null;
		String stepId = null;
		String groupId = null;
		
		List<TspTranController> taskList = BatTaskUtil.getBatTaskList();
		Map<String, TwoTuple<TspTranController, Integer>> taskMap = new LinkedHashMap<String, TwoTuple<TspTranController, Integer>>();
		int totalTaskNum = 0;
		if(CommonUtil.isNotNull(taskList)){
			for(int i = 0;i < taskList.size();i++){
				if(taskList.get(i).getExecutionCode().equals("1")){
					taskMap.put(String.valueOf(taskList.get(i).getStepId()), new TwoTuple<TspTranController, Integer>(taskList.get(i), ++totalTaskNum));
				}else{
					taskMap.put(String.valueOf(taskList.get(i).getStepId()), new TwoTuple<TspTranController, Integer>(taskList.get(i), -1));
				}
			}
		}
		
		do{
			ResultSet resultSet = JDBCUtils.executeQuery(sql, new String[]{taskNum,tranId}, BatTaskUtil.batSettingMap.get("datasource"));
			tranState = CommonUtil.fetchResultSetValue(resultSet, "tran_state");
			errorMsg = CommonUtil.fetchResultSetValue(resultSet, "error_message");
			errorStack = CommonUtil.fetchResultSetValue(resultSet, "error_stack");
			stepId = CommonUtil.fetchResultSetValue(resultSet, "current_step");
			groupId = CommonUtil.fetchResultSetValue(resultSet, "current_tran_group_id");
			
			if(CommonUtil.isNotNull(taskMap.get(stepId))){
				TspTranController tsp = taskMap.get(stepId).getFirst();
				logger.info("执行状态:"+tranState+",交易日期:"+trxnDate+",步骤号:"+stepId+",组号:"+groupId+",交易描述:"+taskMap.get(stepId).getFirst().getTranChineseName());
				String curTask = tsp.getTranCode();
				
				if(CommonUtil.isNotNull(curTask) && CommonUtil.compare(curTask, beforeTask) != 0 && taskMap.get(stepId).getSecond() != -1){
					logger.info("批量任务["+curTask+"]"+taskMap.get(stepId).getFirst().getTranChineseName()+"开始执行("+(taskMap.get(stepId).getSecond())+"/"+totalTaskNum+")");
					beforeTask = curTask;
				}
			}
			CommonUtil.systemPause(delay);
		}while(!BatTaskUtil.SUCCESS_STATE.equals(tranState) && !BatTaskUtil.FAILURE_STATE.equals(tranState));
		
		String taskState = tranState;
		resMap.put("taskState", taskState);
		resMap.put("tranDate", trxnDate);
		resMap.put("tranId", beforeTask);
		
		resMap.put("groupId", groupId);
		resMap.put("errorMsg", errorMsg);
		resMap.put("errorStack", errorStack);
		resMap.put("stepId", stepId);
		return resMap;
	}
}
