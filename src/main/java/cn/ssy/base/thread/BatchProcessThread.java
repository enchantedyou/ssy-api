package cn.ssy.base.thread;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.apache.log4j.Logger;

import cn.ssy.base.core.utils.BatTaskUtil;
import cn.ssy.base.core.utils.CommonUtil;
import cn.ssy.base.core.utils.JDBCUtils;
import cn.ssy.base.entity.mybatis.TspTaskExecution;
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
public class BatchProcessThread implements Callable<TspTaskExecution>{
	
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
	public TspTaskExecution call() throws Exception {
		String sql = "select * from tsp_task_execution where system_code = '"+BatTaskUtil.batSettingMap.get("systemCode")+"' and corporate_code = '"+BatTaskUtil.batSettingMap.get("tenantId")+"' and task_num = ? and tran_id = ? and sub_system_code = '"+BatTaskUtil.batSettingMap.get("subSystemId")+"' order by tran_start_time desc limit 0,1";
		List<TspTranController> taskList = BatTaskUtil.getBatTaskList();
		Map<String, TwoTuple<TspTranController, Integer>> taskMap = new LinkedHashMap<String, TwoTuple<TspTranController, Integer>>();
		
		String beforeTask = null;
		int totalTaskNum = 0;
		if(CommonUtil.isNotNull(taskList)){
			for(int i = 0;i < taskList.size();i++){
				if(taskList.get(i).getExecutionCode().equals("1")){
					taskMap.put(String.valueOf(taskList.get(i).getTranGroupId() + "-" + taskList.get(i).getStepId()), new TwoTuple<TspTranController, Integer>(taskList.get(i), ++totalTaskNum));
				}else{
					taskMap.put(String.valueOf(taskList.get(i).getTranGroupId() + "-" + taskList.get(i).getStepId()), new TwoTuple<TspTranController, Integer>(taskList.get(i), -1));
				}
			}
		}
		
		TspTaskExecution tspTaskExecution = null;
		do{
			//DynamicDataSource.printC3p0PoolStatus();
			tspTaskExecution = CommonUtil.mappingResultSetSingle(JDBCUtils.executeQuery(sql, new String[]{taskNum,tranId}, BatTaskUtil.batSettingMap.get("datasource")), TspTaskExecution.class);
			if(CommonUtil.isNotNull(tspTaskExecution)){
				String taskGetStr = tspTaskExecution.getCurrentTranGroupId() + "-" + tspTaskExecution.getCurrentStep();
				if(CommonUtil.isNotNull(taskMap.get(taskGetStr))){
					TspTranController tsp = taskMap.get(taskGetStr).getFirst();
					logger.info("执行状态:"+tspTaskExecution.getTranState()+",交易日期:"+trxnDate+",步骤号:"+tspTaskExecution.getCurrentStep()+",组号:"+tspTaskExecution.getCurrentTranGroupId()+",交易描述:"+taskMap.get(taskGetStr).getFirst().getTranChineseName());
					String curTask = tsp.getTranCode();
					
					if(CommonUtil.isNotNull(curTask) && CommonUtil.compare(curTask, beforeTask) != 0 && taskMap.get(taskGetStr).getSecond() != -1){
						logger.info("批量任务["+curTask+"]"+taskMap.get(taskGetStr).getFirst().getTranChineseName()+"开始执行("+(taskMap.get(taskGetStr).getSecond())+"/"+totalTaskNum+")");
						beforeTask = curTask;
					}
				}
			}/*else{
				logger.info("批量任务["+tranId + "-" + trxnDate + "-" + taskNum + "]等待执行");
			}*/
			CommonUtil.systemPause(delay);
		}while(CommonUtil.isNull(tspTaskExecution) || (!BatTaskUtil.SUCCESS_STATE.equals(tspTaskExecution.getTranState()) && !BatTaskUtil.FAILURE_STATE.equals(tspTaskExecution.getTranState())));
		return tspTaskExecution;
	}
}
