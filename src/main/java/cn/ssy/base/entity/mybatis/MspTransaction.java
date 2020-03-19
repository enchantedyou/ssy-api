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

	public String getTrxnCode() {
		return trxnCode;
	}

	public void setTrxnCode(String trxnCode) {
		this.trxnCode = trxnCode;
	}

	public String getTrxnType() {
		return trxnType;
	}

	public void setTrxnType(String trxnType) {
		this.trxnType = trxnType;
	}

	public String getTrxnDesc() {
		return trxnDesc;
	}

	public void setTrxnDesc(String trxnDesc) {
		this.trxnDesc = trxnDesc;
	}

	public String getAllowReversal() {
		return allowReversal;
	}

	public void setAllowReversal(String allowReversal) {
		this.allowReversal = allowReversal;
	}

	public String getFlowTrxnId() {
		return flowTrxnId;
	}

	public void setFlowTrxnId(String flowTrxnId) {
		this.flowTrxnId = flowTrxnId;
	}

	public String getRegisterPacketInd() {
		return registerPacketInd;
	}

	public void setRegisterPacketInd(String registerPacketInd) {
		this.registerPacketInd = registerPacketInd;
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
		this.logLevel = logLevel;
	}

	public String getEnableInd() {
		return enableInd;
	}

	public void setEnableInd(String enableInd) {
		this.enableInd = enableInd;
	}

	public String getGlobalParmMntnInd() {
		return globalParmMntnInd;
	}

	public void setGlobalParmMntnInd(String globalParmMntnInd) {
		this.globalParmMntnInd = globalParmMntnInd;
	}

	public String getReversalInd() {
		return reversalInd;
	}

	public void setReversalInd(String reversalInd) {
		this.reversalInd = reversalInd;
	}

	public String getRegisterTrxnInd() {
		return registerTrxnInd;
	}

	public void setRegisterTrxnInd(String registerTrxnInd) {
		this.registerTrxnInd = registerTrxnInd;
	}

	public String getDbTrxnSpreadType() {
		return dbTrxnSpreadType;
	}

	public void setDbTrxnSpreadType(String dbTrxnSpreadType) {
		this.dbTrxnSpreadType = dbTrxnSpreadType;
	}

	public String getReadWriteSeparait() {
		return readWriteSeparait;
	}

	public void setReadWriteSeparait(String readWriteSeparait) {
		this.readWriteSeparait = readWriteSeparait;
	}

	public String getForceCommit() {
		return forceCommit;
	}

	public void setForceCommit(String forceCommit) {
		this.forceCommit = forceCommit;
	}

	public String getDataCreateTime() {
		return dataCreateTime;
	}

	public void setDataCreateTime(String dataCreateTime) {
		this.dataCreateTime = dataCreateTime;
	}

	public String getDataUpdateTime() {
		return dataUpdateTime;
	}

	public void setDataUpdateTime(String dataUpdateTime) {
		this.dataUpdateTime = dataUpdateTime;
	}

	public String getDataCreateUser() {
		return dataCreateUser;
	}

	public void setDataCreateUser(String dataCreateUser) {
		this.dataCreateUser = dataCreateUser;
	}

	public String getDataUpdateUser() {
		return dataUpdateUser;
	}

	public void setDataUpdateUser(String dataUpdateUser) {
		this.dataUpdateUser = dataUpdateUser;
	}

	public Long getDataVersion() {
		return dataVersion;
	}

	public void setDataVersion(Long dataVersion) {
		this.dataVersion = dataVersion;
	}

	@Override
	public String toString() {
		return "MspTransaction [trxnCode=" + trxnCode + ", trxnType=" + trxnType + ", trxnDesc=" + trxnDesc + ", allowReversal=" + allowReversal + ", flowTrxnId=" + flowTrxnId + ", registerPacketInd=" + registerPacketInd + ", overTime=" + overTime + ", logLevel=" + logLevel + ", enableInd=" + enableInd + ", globalParmMntnInd=" + globalParmMntnInd + ", reversalInd=" + reversalInd + ", registerTrxnInd=" + registerTrxnInd + ", dbTrxnSpreadType=" + dbTrxnSpreadType + ", readWriteSeparait=" + readWriteSeparait + ", forceCommit=" + forceCommit + ", dataCreateTime=" + dataCreateTime + ", dataUpdateTime=" + dataUpdateTime + ", dataCreateUser=" + dataCreateUser + ", dataUpdateUser=" + dataUpdateUser + ", dataVersion=" + dataVersion + "]";
	}
}