package cn.ssy.base.entity.mybatis;

public class MspTransaction {
    private String trxnCode;

    private String trxnType;

    private String trxnDesc;

    private String allowReversal;

    private String flowTrxnId;

    private String registerPacketInd;

    private Long overTime;

    private String logLevel;

    private String enableInd;

    private String globalParmMntnInd;

    private String reversalInd;

    private String registerTrxnInd;

    private String dbTrxnSpreadType;

    private String readWriteSeparait;

    private String forceCommit;

    private String dataCreateTime;

    private String dataUpdateTime;

    private String dataCreateUser;

    private String dataUpdateUser;

    private Long dataVersion;

    public MspTransaction(String trxnCode, String trxnType, String trxnDesc, String allowReversal, String flowTrxnId, String registerPacketInd, Long overTime, String logLevel, String enableInd, String globalParmMntnInd, String reversalInd, String registerTrxnInd, String dbTrxnSpreadType, String readWriteSeparait, String forceCommit, String dataCreateTime, String dataUpdateTime, String dataCreateUser, String dataUpdateUser, Long dataVersion) {
        this.trxnCode = trxnCode;
        this.trxnType = trxnType;
        this.trxnDesc = trxnDesc;
        this.allowReversal = allowReversal;
        this.flowTrxnId = flowTrxnId;
        this.registerPacketInd = registerPacketInd;
        this.overTime = overTime;
        this.logLevel = logLevel;
        this.enableInd = enableInd;
        this.globalParmMntnInd = globalParmMntnInd;
        this.reversalInd = reversalInd;
        this.registerTrxnInd = registerTrxnInd;
        this.dbTrxnSpreadType = dbTrxnSpreadType;
        this.readWriteSeparait = readWriteSeparait;
        this.forceCommit = forceCommit;
        this.dataCreateTime = dataCreateTime;
        this.dataUpdateTime = dataUpdateTime;
        this.dataCreateUser = dataCreateUser;
        this.dataUpdateUser = dataUpdateUser;
        this.dataVersion = dataVersion;
    }

    public MspTransaction() {
        super();
    }

    public String getTrxnCode() {
        return trxnCode;
    }

    public void setTrxnCode(String trxnCode) {
        this.trxnCode = trxnCode == null ? null : trxnCode.trim();
    }

    public String getTrxnType() {
        return trxnType;
    }

    public void setTrxnType(String trxnType) {
        this.trxnType = trxnType == null ? null : trxnType.trim();
    }

    public String getTrxnDesc() {
        return trxnDesc;
    }

    public void setTrxnDesc(String trxnDesc) {
        this.trxnDesc = trxnDesc == null ? null : trxnDesc.trim();
    }

    public String getAllowReversal() {
        return allowReversal;
    }

    public void setAllowReversal(String allowReversal) {
        this.allowReversal = allowReversal == null ? null : allowReversal.trim();
    }

    public String getFlowTrxnId() {
        return flowTrxnId;
    }

    public void setFlowTrxnId(String flowTrxnId) {
        this.flowTrxnId = flowTrxnId == null ? null : flowTrxnId.trim();
    }

    public String getRegisterPacketInd() {
        return registerPacketInd;
    }

    public void setRegisterPacketInd(String registerPacketInd) {
        this.registerPacketInd = registerPacketInd == null ? null : registerPacketInd.trim();
    }

    public Long getOverTime() {
        return overTime;
    }

    public void setOverTime(Long overTime) {
        this.overTime = overTime;
    }

    public String getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel == null ? null : logLevel.trim();
    }

    public String getEnableInd() {
        return enableInd;
    }

    public void setEnableInd(String enableInd) {
        this.enableInd = enableInd == null ? null : enableInd.trim();
    }

    public String getGlobalParmMntnInd() {
        return globalParmMntnInd;
    }

    public void setGlobalParmMntnInd(String globalParmMntnInd) {
        this.globalParmMntnInd = globalParmMntnInd == null ? null : globalParmMntnInd.trim();
    }

    public String getReversalInd() {
        return reversalInd;
    }

    public void setReversalInd(String reversalInd) {
        this.reversalInd = reversalInd == null ? null : reversalInd.trim();
    }

    public String getRegisterTrxnInd() {
        return registerTrxnInd;
    }

    public void setRegisterTrxnInd(String registerTrxnInd) {
        this.registerTrxnInd = registerTrxnInd == null ? null : registerTrxnInd.trim();
    }

    public String getDbTrxnSpreadType() {
        return dbTrxnSpreadType;
    }

    public void setDbTrxnSpreadType(String dbTrxnSpreadType) {
        this.dbTrxnSpreadType = dbTrxnSpreadType == null ? null : dbTrxnSpreadType.trim();
    }

    public String getReadWriteSeparait() {
        return readWriteSeparait;
    }

    public void setReadWriteSeparait(String readWriteSeparait) {
        this.readWriteSeparait = readWriteSeparait == null ? null : readWriteSeparait.trim();
    }

    public String getForceCommit() {
        return forceCommit;
    }

    public void setForceCommit(String forceCommit) {
        this.forceCommit = forceCommit == null ? null : forceCommit.trim();
    }

    public String getDataCreateTime() {
        return dataCreateTime;
    }

    public void setDataCreateTime(String dataCreateTime) {
        this.dataCreateTime = dataCreateTime == null ? null : dataCreateTime.trim();
    }

    public String getDataUpdateTime() {
        return dataUpdateTime;
    }

    public void setDataUpdateTime(String dataUpdateTime) {
        this.dataUpdateTime = dataUpdateTime == null ? null : dataUpdateTime.trim();
    }

    public String getDataCreateUser() {
        return dataCreateUser;
    }

    public void setDataCreateUser(String dataCreateUser) {
        this.dataCreateUser = dataCreateUser == null ? null : dataCreateUser.trim();
    }

    public String getDataUpdateUser() {
        return dataUpdateUser;
    }

    public void setDataUpdateUser(String dataUpdateUser) {
        this.dataUpdateUser = dataUpdateUser == null ? null : dataUpdateUser.trim();
    }

    public Long getDataVersion() {
        return dataVersion;
    }

    public void setDataVersion(Long dataVersion) {
        this.dataVersion = dataVersion;
    }
}