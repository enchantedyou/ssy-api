package cn.ssy.base.entity.mybatis;

public class TspTranController extends TspTranControllerKey {
    private String tranChineseName;

    private String executionCode;

    private String relyTranList;

    private String tranRunConditions;

    private Integer reconnectionNum;

    private String failInterruptCode;

    private Integer transactionsSubmitNum;

    private String dataSplitMode;

    private String jobExecutionMode;

    private Integer maxJobConcurrencyNum;

    private Integer logLevel;

    private String jobSplitCondition;

    private String taskRunMode;

    private String isSkip;

    private String isBatchFile;

    private String tranType;

    private String dataSplitKey;

    public TspTranController(String systemCode, String corporateCode, String tranGroupId, Integer stepId, String tranCode, String tranChineseName, String executionCode, String relyTranList, String tranRunConditions, Integer reconnectionNum, String failInterruptCode, Integer transactionsSubmitNum, String dataSplitMode, String jobExecutionMode, Integer maxJobConcurrencyNum, Integer logLevel, String jobSplitCondition, String taskRunMode, String isSkip, String isBatchFile, String tranType) {
        super(systemCode, corporateCode, tranGroupId, stepId, tranCode);
        this.tranChineseName = tranChineseName;
        this.executionCode = executionCode;
        this.relyTranList = relyTranList;
        this.tranRunConditions = tranRunConditions;
        this.reconnectionNum = reconnectionNum;
        this.failInterruptCode = failInterruptCode;
        this.transactionsSubmitNum = transactionsSubmitNum;
        this.dataSplitMode = dataSplitMode;
        this.jobExecutionMode = jobExecutionMode;
        this.maxJobConcurrencyNum = maxJobConcurrencyNum;
        this.logLevel = logLevel;
        this.jobSplitCondition = jobSplitCondition;
        this.taskRunMode = taskRunMode;
        this.isSkip = isSkip;
        this.isBatchFile = isBatchFile;
        this.tranType = tranType;
    }

    public TspTranController(String systemCode, String corporateCode, String tranGroupId, Integer stepId, String tranCode, String tranChineseName, String executionCode, String relyTranList, String tranRunConditions, Integer reconnectionNum, String failInterruptCode, Integer transactionsSubmitNum, String dataSplitMode, String jobExecutionMode, Integer maxJobConcurrencyNum, Integer logLevel, String jobSplitCondition, String taskRunMode, String isSkip, String isBatchFile, String tranType, String dataSplitKey) {
        super(systemCode, corporateCode, tranGroupId, stepId, tranCode);
        this.tranChineseName = tranChineseName;
        this.executionCode = executionCode;
        this.relyTranList = relyTranList;
        this.tranRunConditions = tranRunConditions;
        this.reconnectionNum = reconnectionNum;
        this.failInterruptCode = failInterruptCode;
        this.transactionsSubmitNum = transactionsSubmitNum;
        this.dataSplitMode = dataSplitMode;
        this.jobExecutionMode = jobExecutionMode;
        this.maxJobConcurrencyNum = maxJobConcurrencyNum;
        this.logLevel = logLevel;
        this.jobSplitCondition = jobSplitCondition;
        this.taskRunMode = taskRunMode;
        this.isSkip = isSkip;
        this.isBatchFile = isBatchFile;
        this.tranType = tranType;
        this.dataSplitKey = dataSplitKey;
    }

    public TspTranController() {
        super();
    }

    public String getTranChineseName() {
        return tranChineseName;
    }

    public void setTranChineseName(String tranChineseName) {
        this.tranChineseName = tranChineseName == null ? null : tranChineseName.trim();
    }

    public String getExecutionCode() {
        return executionCode;
    }

    public void setExecutionCode(String executionCode) {
        this.executionCode = executionCode == null ? null : executionCode.trim();
    }

    public String getRelyTranList() {
        return relyTranList;
    }

    public void setRelyTranList(String relyTranList) {
        this.relyTranList = relyTranList == null ? null : relyTranList.trim();
    }

    public String getTranRunConditions() {
        return tranRunConditions;
    }

    public void setTranRunConditions(String tranRunConditions) {
        this.tranRunConditions = tranRunConditions == null ? null : tranRunConditions.trim();
    }

    public Integer getReconnectionNum() {
        return reconnectionNum;
    }

    public void setReconnectionNum(Integer reconnectionNum) {
        this.reconnectionNum = reconnectionNum;
    }

    public String getFailInterruptCode() {
        return failInterruptCode;
    }

    public void setFailInterruptCode(String failInterruptCode) {
        this.failInterruptCode = failInterruptCode == null ? null : failInterruptCode.trim();
    }

    public Integer getTransactionsSubmitNum() {
        return transactionsSubmitNum;
    }

    public void setTransactionsSubmitNum(Integer transactionsSubmitNum) {
        this.transactionsSubmitNum = transactionsSubmitNum;
    }

    public String getDataSplitMode() {
        return dataSplitMode;
    }

    public void setDataSplitMode(String dataSplitMode) {
        this.dataSplitMode = dataSplitMode == null ? null : dataSplitMode.trim();
    }

    public String getJobExecutionMode() {
        return jobExecutionMode;
    }

    public void setJobExecutionMode(String jobExecutionMode) {
        this.jobExecutionMode = jobExecutionMode == null ? null : jobExecutionMode.trim();
    }

    public Integer getMaxJobConcurrencyNum() {
        return maxJobConcurrencyNum;
    }

    public void setMaxJobConcurrencyNum(Integer maxJobConcurrencyNum) {
        this.maxJobConcurrencyNum = maxJobConcurrencyNum;
    }

    public Integer getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(Integer logLevel) {
        this.logLevel = logLevel;
    }

    public String getJobSplitCondition() {
        return jobSplitCondition;
    }

    public void setJobSplitCondition(String jobSplitCondition) {
        this.jobSplitCondition = jobSplitCondition == null ? null : jobSplitCondition.trim();
    }

    public String getTaskRunMode() {
        return taskRunMode;
    }

    public void setTaskRunMode(String taskRunMode) {
        this.taskRunMode = taskRunMode == null ? null : taskRunMode.trim();
    }

    public String getIsSkip() {
        return isSkip;
    }

    public void setIsSkip(String isSkip) {
        this.isSkip = isSkip == null ? null : isSkip.trim();
    }

    public String getIsBatchFile() {
        return isBatchFile;
    }

    public void setIsBatchFile(String isBatchFile) {
        this.isBatchFile = isBatchFile == null ? null : isBatchFile.trim();
    }

    public String getTranType() {
        return tranType;
    }

    public void setTranType(String tranType) {
        this.tranType = tranType == null ? null : tranType.trim();
    }

    public String getDataSplitKey() {
        return dataSplitKey;
    }

    public void setDataSplitKey(String dataSplitKey) {
        this.dataSplitKey = dataSplitKey == null ? null : dataSplitKey.trim();
    }
}