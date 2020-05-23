package cn.ssy.base.entity.mybatis;


public class TspTaskExecution extends TspTaskExecutionKey {
    private String taskCommitDate;

    private String transactionDate;

    private String tranFlowId;

    private Integer flowStepNum;

    private String tranGroupId;

    private String tranId;

    private String tranGroupExeTime;

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

    private Integer currentFlowStepNum;

    private Integer currentExecutionNum;

    private String currentTranGroupId;

    private Integer currentStep;

    private String serviceCode;

    public TspTaskExecution(String taskNum, String taskExeNum, String tranDate, String subSystemCode, String systemCode, String corporateCode, String taskCommitDate, String transactionDate, String tranFlowId, Integer flowStepNum, String tranGroupId, String tranId, String tranGroupExeTime, Long totalCost, String tranState, String taskExeMode, String taskInterruptFlag, String taskCommitTime, Integer taskPriority, String tranStartTime, Long tranStartTimestamp, String tranEndTime, Long tranEndTimestamp, Integer currentFlowStepNum, Integer currentExecutionNum, String currentTranGroupId, Integer currentStep, String serviceCode) {
        super(taskNum, taskExeNum, tranDate, subSystemCode, systemCode, corporateCode);
        this.taskCommitDate = taskCommitDate;
        this.transactionDate = transactionDate;
        this.tranFlowId = tranFlowId;
        this.flowStepNum = flowStepNum;
        this.tranGroupId = tranGroupId;
        this.tranId = tranId;
        this.tranGroupExeTime = tranGroupExeTime;
        this.totalCost = totalCost;
        this.tranState = tranState;
        this.taskExeMode = taskExeMode;
        this.taskInterruptFlag = taskInterruptFlag;
        this.taskCommitTime = taskCommitTime;
        this.taskPriority = taskPriority;
        this.tranStartTime = tranStartTime;
        this.tranStartTimestamp = tranStartTimestamp;
        this.tranEndTime = tranEndTime;
        this.tranEndTimestamp = tranEndTimestamp;
        this.currentFlowStepNum = currentFlowStepNum;
        this.currentExecutionNum = currentExecutionNum;
        this.currentTranGroupId = currentTranGroupId;
        this.currentStep = currentStep;
        this.serviceCode = serviceCode;
    }

    public TspTaskExecution() {
        super();
    }

    public String getTaskCommitDate() {
        return taskCommitDate;
    }

    public void setTaskCommitDate(String taskCommitDate) {
        this.taskCommitDate = taskCommitDate == null ? null : taskCommitDate.trim();
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate == null ? null : transactionDate.trim();
    }

    public String getTranFlowId() {
        return tranFlowId;
    }

    public void setTranFlowId(String tranFlowId) {
        this.tranFlowId = tranFlowId == null ? null : tranFlowId.trim();
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
        this.tranGroupId = tranGroupId == null ? null : tranGroupId.trim();
    }

    public String getTranId() {
        return tranId;
    }

    public void setTranId(String tranId) {
        this.tranId = tranId == null ? null : tranId.trim();
    }

    public String getTranGroupExeTime() {
        return tranGroupExeTime;
    }

    public void setTranGroupExeTime(String tranGroupExeTime) {
        this.tranGroupExeTime = tranGroupExeTime == null ? null : tranGroupExeTime.trim();
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
        this.tranState = tranState == null ? null : tranState.trim();
    }

    public String getTaskExeMode() {
        return taskExeMode;
    }

    public void setTaskExeMode(String taskExeMode) {
        this.taskExeMode = taskExeMode == null ? null : taskExeMode.trim();
    }

    public String getTaskInterruptFlag() {
        return taskInterruptFlag;
    }

    public void setTaskInterruptFlag(String taskInterruptFlag) {
        this.taskInterruptFlag = taskInterruptFlag == null ? null : taskInterruptFlag.trim();
    }

    public String getTaskCommitTime() {
        return taskCommitTime;
    }

    public void setTaskCommitTime(String taskCommitTime) {
        this.taskCommitTime = taskCommitTime == null ? null : taskCommitTime.trim();
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
        this.tranStartTime = tranStartTime == null ? null : tranStartTime.trim();
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
        this.tranEndTime = tranEndTime == null ? null : tranEndTime.trim();
    }

    public Long getTranEndTimestamp() {
        return tranEndTimestamp;
    }

    public void setTranEndTimestamp(Long tranEndTimestamp) {
        this.tranEndTimestamp = tranEndTimestamp;
    }

    public Integer getCurrentFlowStepNum() {
        return currentFlowStepNum;
    }

    public void setCurrentFlowStepNum(Integer currentFlowStepNum) {
        this.currentFlowStepNum = currentFlowStepNum;
    }

    public Integer getCurrentExecutionNum() {
        return currentExecutionNum;
    }

    public void setCurrentExecutionNum(Integer currentExecutionNum) {
        this.currentExecutionNum = currentExecutionNum;
    }

    public String getCurrentTranGroupId() {
        return currentTranGroupId;
    }

    public void setCurrentTranGroupId(String currentTranGroupId) {
        this.currentTranGroupId = currentTranGroupId == null ? null : currentTranGroupId.trim();
    }

    public Integer getCurrentStep() {
        return currentStep;
    }

    public void setCurrentStep(Integer currentStep) {
        this.currentStep = currentStep;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode == null ? null : serviceCode.trim();
    }
}