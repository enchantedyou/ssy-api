package cn.ssy.base.entity.mybatis;



public class SppDatasource {

	private String datasourceId;

	private String datasourceType;

	private String tlsqlInd;

	private String platformTablePrefix;

	private String datasourceDesc;

	private String datasourceDriver;

	private String datasourceUrl;

	private String datasourceUser;

	private String datasourcePwd;

	private String dataCreateUser;

	private String dataCreateTime;

	private String dataUpdateUser;

	private String dataUpdateTime;

	private Integer dataVersion;

	public String getDatasourceId() {
		return datasourceId;
	}

	public void setDatasourceId(String datasourceId) {
		this.datasourceId = datasourceId;
	}

	public String getDatasourceType() {
		return datasourceType;
	}

	public void setDatasourceType(String datasourceType) {
		this.datasourceType = datasourceType;
	}

	public String getTlsqlInd() {
		return tlsqlInd;
	}

	public void setTlsqlInd(String tlsqlInd) {
		this.tlsqlInd = tlsqlInd;
	}

	public String getPlatformTablePrefix() {
		return platformTablePrefix;
	}

	public void setPlatformTablePrefix(String platformTablePrefix) {
		this.platformTablePrefix = platformTablePrefix;
	}

	public String getDatasourceDesc() {
		return datasourceDesc;
	}

	public void setDatasourceDesc(String datasourceDesc) {
		this.datasourceDesc = datasourceDesc;
	}

	public String getDatasourceDriver() {
		return datasourceDriver;
	}

	public void setDatasourceDriver(String datasourceDriver) {
		this.datasourceDriver = datasourceDriver;
	}

	public String getDatasourceUrl() {
		return datasourceUrl;
	}

	public void setDatasourceUrl(String datasourceUrl) {
		this.datasourceUrl = datasourceUrl;
	}

	public String getDatasourceUser() {
		return datasourceUser;
	}

	public void setDatasourceUser(String datasourceUser) {
		this.datasourceUser = datasourceUser;
	}

	public String getDatasourcePwd() {
		return datasourcePwd;
	}

	public void setDatasourcePwd(String datasourcePwd) {
		this.datasourcePwd = datasourcePwd;
	}

	public String getDataCreateUser() {
		return dataCreateUser;
	}

	public void setDataCreateUser(String dataCreateUser) {
		this.dataCreateUser = dataCreateUser;
	}

	public String getDataCreateTime() {
		return dataCreateTime;
	}

	public void setDataCreateTime(String dataCreateTime) {
		this.dataCreateTime = dataCreateTime;
	}

	public String getDataUpdateUser() {
		return dataUpdateUser;
	}

	public void setDataUpdateUser(String dataUpdateUser) {
		this.dataUpdateUser = dataUpdateUser;
	}

	public String getDataUpdateTime() {
		return dataUpdateTime;
	}

	public void setDataUpdateTime(String dataUpdateTime) {
		this.dataUpdateTime = dataUpdateTime;
	}

	public Integer getDataVersion() {
		return dataVersion;
	}

	public void setDataVersion(Integer dataVersion) {
		this.dataVersion = dataVersion;
	}

	@Override
	public String toString() {
		return "SppDatasource [datasourceId=" + datasourceId + ", datasourceType=" + datasourceType + ", tlsqlInd=" + tlsqlInd + ", platformTablePrefix=" + platformTablePrefix + ", datasourceDesc=" + datasourceDesc + ", datasourceDriver=" + datasourceDriver + ", datasourceUrl=" + datasourceUrl + ", datasourceUser=" + datasourceUser + ", datasourcePwd=" + datasourcePwd + ", dataCreateUser=" + dataCreateUser + ", dataCreateTime=" + dataCreateTime + ", dataUpdateUser=" + dataUpdateUser + ", dataUpdateTime=" + dataUpdateTime + ", dataVersion=" + dataVersion + "]";
	}
}