package cn.ssy.base.thread;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import org.apache.log4j.Logger;

import cn.ssy.base.core.utils.BatTaskUtil;
import cn.ssy.base.core.utils.CommonUtil;
import cn.ssy.base.core.utils.JDBCUtils;
import cn.ssy.base.entity.consts.ApiConst;
import cn.ssy.base.entity.mybatis.TspTranController;


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
	
	private String groupId;
	
	public BatchProcessThread(String taskNum, String trxnDate,String tranId,String groupId) {
		super();
		this.taskNum = taskNum;
		this.trxnDate = trxnDate;
		this.tranId = tranId;
		this.groupId = groupId;
	}

	@Override
	public Map<String, Object> call() throws Exception {
		Map<String, Object> resMap = new HashMap<String, Object>();
		String sql = "select * from tsp_task_execution where system_code = '102' and corporate_code = '025' and task_num = ? and tran_date = ? and tran_id = ? and tran_group_id = ? and sub_system_code = '1022';";
		String tranState = null;
		String errorMsg = null;
		
		String errorStack = null;
		String beforeTask = null;
		String stepId = null;
		
		do{
			ResultSet resultSet = JDBCUtils.executeQuery(sql, new String[]{taskNum,trxnDate,tranId,groupId}, ApiConst.DATASOURCE_ICORE_LN);
			tranState = CommonUtil.fetchResultSetValue(resultSet, "tran_state");
			errorMsg = CommonUtil.fetchResultSetValue(resultSet, "error_message");
			errorStack = CommonUtil.fetchResultSetValue(resultSet, "error_stack");
			stepId = CommonUtil.fetchResultSetValue(resultSet, "current_step");
			
			TspTranController tsp = new TspTranController();
			if(CommonUtil.isNotNull(stepId)){
				tsp = BatTaskUtil.getBatTaskByTranCode(Integer.parseInt(stepId));
				logger.info("执行状态:"+tranState+",交易日期:"+trxnDate+",步骤号:"+stepId+",组号:"+groupId+",批量标识:"+taskNum);
			}
			
			String curTask = tsp.getTranCode();
			if(CommonUtil.compare(curTask, beforeTask) != 0){
				logger.info("批量任务["+curTask+"]开始执行");
				beforeTask = curTask;
			}
			
			CommonUtil.systemPause(1500);
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
