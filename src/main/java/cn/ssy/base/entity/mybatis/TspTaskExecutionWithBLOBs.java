package cn.ssy.base.entity.mybatis;


public class TspTaskExecutionWithBLOBs extends TspTaskExecution {
    private String dataArea;

    private String errorMessage;

    private String errorStack;

    public TspTaskExecutionWithBLOBs(String taskNum, String taskExeNum, String tranDate, String subSystemCode, String systemCode, String corporateCode, String taskCommitDate, String transactionDate, String tranFlowId, Integer flowStepNum, String tranGroupId, String tranId, String tranGroupExeTime, Long totalCost, String tranState, String taskExeMode, String taskInterruptFlag, String taskCommitTime, Integer taskPriority, String tranStartTime, Long tranStartTimestamp, String tranEndTime, Long tranEndTimestamp, Integer currentFlowStepNum, Integer currentExecutionNum, String currentTranGroupId, Integer currentStep, String serviceCode, String dataArea, String errorMessage, String errorStack) {
        super(taskNum, taskExeNum, tranDate, subSystemCode, systemCode, corporateCode, taskCommitDate, transactionDate, tranFlowId, flowStepNum, tranGroupId, tranId, tranGroupExeTime, totalCost, tranState, taskExeMode, taskInterruptFlag, taskCommitTime, taskPriority, tranStartTime, tranStartTimestamp, tranEndTime, tranEndTimestamp, currentFlowStepNum, currentExecutionNum, currentTranGroupId, currentStep, serviceCode);
        this.dataArea = dataArea;
        this.errorMessage = errorMessage;
        this.errorStack = errorStack;
    }

    public TspTaskExecutionWithBLOBs() {
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