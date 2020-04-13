package cn.ssy.base.entity.mybatis;

import java.sql.Date;



public class TspTask {

	private String systemCode;

	private String corporateCode;

	private String taskNum;

	private String taskExeNum;

	private String taskCommitDate;

	private Date tranDate;

	private String transactionDate;

	private String tranFlowId;

	private Integer flowStepNum;

	private String tranGroupId;

	private String tranId;

	private Long totalCost;

	private String tranState;

	private String taskExeMode;

	private String taskInterruptFlag;

	private String taskCommitTime;

	private Integer taskPriority;

	private String tranStartTime;

	private Long tranStartTimestamp;

	private String tranEndTime;

	private Long tranEndTimestamp;

	private String vmId;

	private String ipAddress;

	private String serverHostName;

	private String dataArea;

	private Integer startFlowStepNum;

	private Integer startExecutionNo;

	private String startTranGroupId;

	private Integer startStepNum;

	private String errorMessage;

	private String errorStack;

	private String serviceCode;

	private String subSystemCode;

	public String getSystemCode() {
		return systemCode;
	}

	public void setSystemCode(String systemCode) {
		this.systemCode = systemCode;
	}

	public String getCorporateCode() {
		return corporateCode;
	}

	public void setCorporateCode(String corporateCode) {
		this.corporateCode = corporateCode;
	}

	public String getTaskNum() {
		return taskNum;
	}

	public void setTaskNum(String taskNum) {
		this.taskNum = taskNum;
	}

	public String getTaskExeNum() {
		return taskExeNum;
	}

	public void setTaskExeNum(String taskExeNum) {
		this.taskExeNum = taskExeNum;
	}

	public String getTaskCommitDate() {
		return taskCommitDate;
	}

	public void setTaskCommitDate(String taskCommitDate) {
		this.taskCommitDate = taskCommitDate;
	}

	public Date getTranDate() {
		return tranDate;
	}

	public void setTranDate(Date tranDate) {
		this.tranDate = tranDate;
	}

	public String getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}

	public String getTranFlowId() {
		return tranFlowId;
	}

	public void setTranFlowId(String tranFlowId) {
		this.tranFlowId = tranFlowId;
	}

	public Integer getFlowStepNum() {
		return flowStepNum;
	}

	public void setFlowStepNum(Integer flowStepNum) {
		this.flowStepNum = flowStepNum;
	}

	public String getTranGroupId() {
		return tranGroupId;
	}

	public void setTranGroupId(String tranGroupId) {
		this.tranGroupId = tranGroupId;
	}

	public String getTranId() {
		return tranId;
	}

	public void setTranId(String tranId) {
		this.tranId = tranId;
	}

	public Long getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(Long totalCost) {
		this.totalCost = totalCost;
	}

	public String getTranState() {
		return tranState;
	}

	public void setTranState(String tranState) {
		this.tranState = tranState;
	}

	public String getTaskExeMode() {
		return taskExeMode;
	}

	public void setTaskExeMode(String taskExeMode) {
		this.taskExeMode = taskExeMode;
	}

	public String getTaskInterruptFlag() {
		return taskInterruptFlag;
	}

	public void setTaskInterruptFlag(String taskInterruptFlag) {
		this.taskInterruptFlag = taskInterruptFlag;
	}

	public String getTaskCommitTime() {
		return taskCommitTime;
	}

	public void setTaskCommitTime(String taskCommitTime) {
		this.taskCommitTime = taskCommitTime;
	}

	public Integer getTaskPriority() {
		return taskPriority;
	}

	public void setTaskPriority(Integer taskPriority) {
		this.taskPriority = taskPriority;
	}

	public String getTranStartTime() {
		return tranStartTime;
	}

	public void setTranStartTime(String tranStartTime) {
		this.tranStartTime = tranStartTime;
	}

	public Long getTranStartTimestamp() {
		return tranStartTimestamp;
	}

	public void setTranStartTimestamp(Long tranStartTimestamp) {
		this.tranStartTimestamp = tranStartTimestamp;
	}

	public String getTranEndTime() {
		return tranEndTime;
	}

	public void setTranEndTime(String tranEndTime) {
		this.tranEndTime = tranEndTime;
	}

	public Long getTranEndTimestamp() {
		return tranEndTimestamp;
	}

	public void setTranEndTimestamp(Long tranEndTimestamp) {
		this.tranEndTimestamp = tranEndTimestamp;
	}

	public String getVmId() {
		return vmId;
	}

	public void setVmId(String vmId) {
		this.vmId = vmId;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getServerHostName() {
		return serverHostName;
	}

	public void setServerHostName(String serverHostName) {
		this.serverHostName = serverHostName;
	}

	public String getDataArea() {
		return dataArea;
	}

	public void setDataArea(String dataArea) {
		this.dataArea = dataArea;
	}

	public Integer getStartFlowStepNum() {
		return startFlowStepNum;
	}

	public void setStartFlowStepNum(Integer startFlowStepNum) {
		this.startFlowStepNum = startFlowStepNum;
	}

	public Integer getStartExecutionNo() {
		return startExecutionNo;
	}

	public void setStartExecutionNo(Integer startExecutionNo) {
		this.startExecutionNo = startExecutionNo;
	}

	public String getStartTranGroupId() {
		return startTranGroupId;
	}

	public void setStartTranGroupId(String startTranGroupId) {
		this.startTranGroupId = startTranGroupId;
	}

	public Integer getStartStepNum() {
		return startStepNum;
	}

	public void setStartStepNum(Integer startStepNum) {
		this.startStepNum = startStepNum;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorStack() {
		return errorStack;
	}

	public void setErrorStack(String errorStack) {
		this.errorStack = errorStack;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getSubSystemCode() {
		return subSystemCode;
	}

	public void setSubSystemCode(String subSystemCode) {
		this.subSystemCode = subSystemCode;
	}

	@Override
	public String toString() {
		return "TspTask [systemCode=" + systemCode + ", corporateCode=" + corporateCode + ", taskNum=" + taskNum + ", taskExeNum=" + taskExeNum + ", taskCommitDate=" + taskCommitDate + ", tranDate=" + tranDate + ", transactionDate=" + transactionDate + ", tranFlowId=" + tranFlowId + ", flowStepNum=" + flowStepNum + ", tranGroupId=" + tranGroupId + ", tranId=" + tranId + ", totalCost=" + totalCost + ", tranState=" + tranState + ", taskExeMode=" + taskExeMode + ", taskInterruptFlag=" + taskInterruptFlag + ", taskCommitTime=" + taskCommitTime + ", taskPriority=" + taskPriority + ", tranStartTime=" + tranStartTime + ", tranStartTimestamp=" + tranStartTimestamp + ", tranEndTime=" + tranEndTime + ", tranEndTimestamp=" + tranEndTimestamp + ", vmId=" + vmId + ", ipAddress=" + ipAddress + ", serverHostName=" + serverHostName + ", dataArea=" + dataArea + ", startFlowStepNum=" + startFlowStepNum + ", startExecutionNo=" + startExecutionNo + ", startTranGroupId=" + startTranGroupId + ", startStepNum=" + startStepNum + ", errorMessage=" + errorMessage + ", errorStack=" + errorStack + ", serviceCode=" + serviceCode + ", subSystemCode=" + subSystemCode + "]";
	}
}