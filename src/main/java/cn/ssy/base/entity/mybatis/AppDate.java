package cn.ssy.base.entity.mybatis;



public class AppDate {

	private String busiOrgId;

	private String lastDate;

	private String trxnDate;

	private String nextDate;

	private String balSheetDate;

	private String dataCreateTime;

	private String dataUpdateTime;

	private String dataCreateUser;

	private String dataUpdateUser;

	private Long dataVersion;

	public String getBusiOrgId() {
		return busiOrgId;
	}

	public void setBusiOrgId(String busiOrgId) {
		this.busiOrgId = busiOrgId;
	}

	public String getLastDate() {
		return lastDate;
	}

	public void setLastDate(String lastDate) {
		this.lastDate = lastDate;
	}

	public String getTrxnDate() {
		return trxnDate;
	}

	public void setTrxnDate(String trxnDate) {
		this.trxnDate = trxnDate;
	}

	public String getNextDate() {
		return nextDate;
	}

	public void setNextDate(String nextDate) {
		this.nextDate = nextDate;
	}

	public String getBalSheetDate() {
		return balSheetDate;
	}

	public void setBalSheetDate(String balSheetDate) {
		this.balSheetDate = balSheetDate;
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
		return "AppDate [busiOrgId=" + busiOrgId + ", lastDate=" + lastDate + ", trxnDate=" + trxnDate + ", nextDate=" + nextDate + ", balSheetDate=" + balSheetDate + ", dataCreateTime=" + dataCreateTime + ", dataUpdateTime=" + dataUpdateTime + ", dataCreateUser=" + dataCreateUser + ", dataUpdateUser=" + dataUpdateUser + ", dataVersion=" + dataVersion + "]";
	}
}