package cn.ssy.base.entity.mybatis;


public class TspTaskWithBLOBs extends TspTask {
    private String dataArea;

    private String errorMessage;

    private String errorStack;

    public TspTaskWithBLOBs(String subSystemCode, String taskNum, String systemCode, String corporateCode, String taskExeNum, String taskCommitDate, String tranDate, String transactionDate, String tranFlowId, Integer flowStepNum, String tranGroupId, String tranId, Long totalCost, String tranState, String taskExeMode, String taskInterruptFlag, String taskCommitTime, Integer taskPriority, String tranStartTime, Long tranStartTimestamp, String tranEndTime, Long tranEndTimestamp, String vmId, String ipAddress, String serverHostName, Integer startFlowStepNum, Integer startExecutionNo, String startTranGroupId, Integer startStepNum, String serviceCode, String dataArea, String errorMessage, String errorStack) {
        super(subSystemCode, taskNum, systemCode, corporateCode, taskExeNum, taskCommitDate, tranDate, transactionDate, tranFlowId, flowStepNum, tranGroupId, tranId, totalCost, tranState, taskExeMode, taskInterruptFlag, taskCommitTime, taskPriority, tranStartTime, tranStartTimestamp, tranEndTime, tranEndTimestamp, vmId, ipAddress, serverHostName, startFlowStepNum, startExecutionNo, startTranGroupId, startStepNum, serviceCode);
        this.dataArea = dataArea;
        this.errorMessage = errorMessage;
        this.errorStack = errorStack;
    }

    public TspTaskWithBLOBs() {
        super();
    }

    public String getDataArea() {
        return dataArea;
    }

    public void setDataArea(String dataArea) {
        this.dataArea = dataArea == null ? null : dataArea.trim();
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage == null ? null : errorMessage.trim();
    }

    public String getErrorStack() {
        return errorStack;
    }

    public void setErrorStack(String errorStack) {
        this.errorStack = errorStack == null ? null : errorStack.trim();
    }
}