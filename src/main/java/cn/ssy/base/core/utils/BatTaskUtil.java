package cn.ssy.base.core.utils;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import org.apache.log4j.Logger;

import cn.ssy.base.core.utils.mybatis.MybatisUtil;
import cn.ssy.base.dao.mapper.AppDateMapper;
import cn.ssy.base.dao.mapper.TspFlowStepControllerMapper;
import cn.ssy.base.dao.mapper.TspTaskMapper;
import cn.ssy.base.dao.mapper.TspTranControllerMapper;
import cn.ssy.base.entity.config.BatchSubConfig;
import cn.ssy.base.entity.context.Application;
import cn.ssy.base.entity.mybatis.AppDate;
import cn.ssy.base.entity.mybatis.TspFlowStepController;
import cn.ssy.base.entity.mybatis.TspTaskExecutionWithBLOBs;
import cn.ssy.base.entity.mybatis.TspTaskKey;
import cn.ssy.base.entity.mybatis.TspTaskWithBLOBs;
import cn.ssy.base.entity.mybatis.TspTranController;
import cn.ssy.base.entity.mybatis.TspTranControllerKey;
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
	static FutureTask<TspTaskExecutionWithBLOBs> listenerThreadTask = null;
	public final static String SUCCESS_STATE = "success";
	public final static String FAILURE_STATE = "failure";
	public final static BatchSubConfig batConfig = Application.getContext().getBatch().getEnableBatchConfig();
	public final static MybatisUtil mybatisUtil = new MybatisUtil();
	
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
		List<TspTranController> taskList = getTspTranControllerMapper().selectAll();
		return taskList;
	}
	
	private static TspTranControllerMapper getTspTranControllerMapper(){
		return mybatisUtil.getMapper(batConfig.getDatasource(), TspTranControllerMapper.class);
	}
	
	private static TspFlowStepControllerMapper getTspFlowStepControllerMapper(){
		return mybatisUtil.getMapper(batConfig.getDatasource(), TspFlowStepControllerMapper.class);
	}
	
	private static AppDateMapper getAppDateMapper(){
		return mybatisUtil.getMapper(batConfig.getDatasource(), AppDateMapper.class);
	}
	
	private static TspTaskMapper getTspTaskMapper(){
		return mybatisUtil.getMapper(batConfig.getDatasource(), TspTaskMapper.class);
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
	public static TspTranController getBatTaskByTranCode(String tranGroupId, String tranCode, Integer stepId) throws SQLException{
		TspTranControllerKey key = new TspTranControllerKey(batConfig.getSystemCode(), batConfig.getBusiOrgId(), tranGroupId, stepId, tranCode);
		TspTranController record = getTspTranControllerMapper().selectByPrimaryKey(key);
		return record;
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
		TspFlowStepController record = getTspFlowStepControllerMapper().selectOne_odb1(tranCode, tranCode);
		
		if(CommonUtil.isNull(record)){
			return null;
		}else{
			return record.getTranFlowId();
		}
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
	 * @throws ParseException 
	 */
	public static void tryStartupTask(int days) throws SQLException, InterruptedException, ExecutionException, ParseException{
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
	 *         <li>2020年5月11日-下午1:08:10</li>
	 *         <li>功能说明：</li>
	 *         </p>
	 * @param endDate
	 * @throws SQLException
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws ParseException 
	 */
	public static void tryStartupTask(String endDate) throws SQLException, InterruptedException, ExecutionException, ParseException{
		AppDate appDate = getAppDateMapper().selectByPrimaryKey(batConfig.getBusiOrgId());
		while(CommonUtil.compare(appDate.getTrxnDate(), endDate) <= 0){
			appDate = getAppDateMapper().selectByPrimaryKey(batConfig.getBusiOrgId());
			long start = System.currentTimeMillis();
			BatTaskUtil.tryStartupTask();
			long end = System.currentTimeMillis();
			
			logger.info("\r\n" + CommonUtil.buildSplitLine(50) + "交易日期["+appDate.getTrxnDate()+"]的批量任务执行完成,耗时:" + (end - start) + "ms" + CommonUtil.buildSplitLine(50) + "\r\n");
			appDate = getAppDateMapper().selectByPrimaryKey(batConfig.getBusiOrgId());
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
	 * @throws ParseException 
	 */
	public static void tryStartupTask() throws SQLException, InterruptedException, ExecutionException, ParseException{
		List<TspFlowStepController> flowStepControlList = getTspFlowStepControllerMapper().selectAll();
		for(TspFlowStepController tspFlowStepController : flowStepControlList){
			AppDate appDate = getAppDateMapper().selectByPrimaryKey(batConfig.getBusiOrgId());
			//日切前判断
			if("Switch".equals(tspFlowStepController.getTranFlowId())){
				List<TspTaskWithBLOBs> taskList = getTspTaskMapper().selectAll_odb1(appDate.getTrxnDate());
				//当天有失败的批量,不日切
				if(CommonUtil.isNotNull(taskList)){
					logger.info(CommonUtil.buildSplitLine(30) + "交易日期["+ appDate.getTrxnDate() +"]存在执行失败的批量,不进行日切" + CommonUtil.buildSplitLine(30));
					continue;
				}
			}
			String taskNum = tspFlowStepController.getTranFlowId() + "_" + appDate.getTrxnDate() + "_" + appDate.getBusiOrgId(); 
			logger.info(CommonUtil.buildSplitLine(50) + taskNum + "  begin" + CommonUtil.buildSplitLine(50));
			
			TspTranController tspTranController = getTspTranControllerMapper().selectOne_odb1(tspFlowStepController.getTranGroupId());
			if(CommonUtil.isNull(tspTranController)){
				logger.info("批量交易组[" + tspFlowStepController.getTranGroupId() + "-" + tspFlowStepController.getFlowStepName() +"]对应的交易控制器不存在");
			}else{
				startupTask(tspTranController.getTranGroupId(),tspFlowStepController.getTranFlowId(), tspTranController.getTranCode(), tspTranController.getStepId(), taskNum);
			}
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
	 * @throws ParseException 
	 */
	private static void startupTask(String tranGroupId, String tranFlowId, String tranCode,Integer stepId,String taskNum) throws SQLException, InterruptedException, ExecutionException, ParseException{
		String trxnDate = getAppDateMapper().selectByPrimaryKey(batConfig.getBusiOrgId()).getTrxnDate();
		String dataArea = "{\"comm_req\":{\"initiator_system\":\""+batConfig.getSystemCode()+"\",\"trxn_branch\":\""+batConfig.getTrxnBranch()+"\",\"trxn_teller\":\"S####\",\"sponsor_system\":\""+batConfig.getSystemCode()+"\",\"busi_org_id\":\""+batConfig.getBusiOrgId()+"\",\"channel_id\":\""+batConfig.getChannelId()+"\"}}";
		logger.info("当前批量任务标识:" + taskNum);
		
		String dateformatDate = trxnDate.substring(0, 4) + "-" + trxnDate.substring(4,6) + "-" + trxnDate.substring(6);
		boolean startupSuccessInd = false;
		
		TspTaskWithBLOBs tspTask = getTspTaskMapper().selectByPrimaryKey(new TspTaskKey(batConfig.getSubSystemId(), taskNum, batConfig.getSystemCode(), batConfig.getBusiOrgId()));
		if(CommonUtil.isNotNull(tspTask)){
			if(!SUCCESS_STATE.equals(tspTask.getTranState())){
				tspTask.setTranState("onprocess");
				getTspTaskMapper().updateByPrimaryKey(tspTask);
				logger.info("任务["+ taskNum +"]执行失败,重新执行");
			}else{
				logger.info("任务["+ taskNum +"]已成功,跳过执行");
				return;
			}
		}else{
			logger.info("新建任务["+ taskNum +"]");
			tspTask = new TspTaskWithBLOBs();
			tspTask.setSystemCode(batConfig.getSystemCode());
			tspTask.setSubSystemCode(batConfig.getSubSystemId());
			tspTask.setTaskNum(taskNum);
			
			tspTask.setTranDate(dateformatDate);
			tspTask.setTransactionDate(trxnDate);
			tspTask.setTranState("onprocess");
			tspTask.setCorporateCode(batConfig.getBusiOrgId());
			
			tspTask.setDataArea(dataArea);
			tspTask.setTaskExeNum(String.valueOf(System.currentTimeMillis()));
			
			tspTask.setTaskCommitDate(trxnDate);
			tspTask.setTaskExeMode("1");
			tspTask.setTranFlowId(tranFlowId);
			startupSuccessInd = getTspTaskMapper().insert(tspTask) > 0;
		}
		
		if(startupSuccessInd){
			logger.info("步骤号为["+stepId+"]的批量交易["+tranCode+"]启动成功");
			
			//监听批量任务
			BatchProcessThread listener = new BatchProcessThread(taskNum, dateformatDate, tspTask.getTaskExeNum());
			listenerThreadTask = new FutureTask<>(listener);
			listenerThreadTask.run();
		}else{
			logger.error("步骤号为["+stepId+"]的批量交易["+tranCode+"]启动失败");
		}
		printBatchTasKExecuteRes();
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
	private static void printBatchTasKExecuteRes() throws SQLException, InterruptedException, ExecutionException{
		TspTaskExecutionWithBLOBs tspTaskExecution = listenerThreadTask.get();
		boolean isFailure = FAILURE_STATE.equals(tspTaskExecution.getTranState());
		//查询新的交易日期
		String appDate = tspTaskExecution.getTranDate();
		logger.info("交易日期:"+appDate+",批量["+tspTaskExecution.getTaskNum()+"]执行结果:" + tspTaskExecution.getTranState());
		if(isFailure){
			logger.info("错误信息:" + tspTaskExecution.getErrorMessage());
			logger.info("错误堆栈:" + tspTaskExecution.getErrorStack());
			throw new BatBusinessException("批量任务执行异常");
		}
	}
}
