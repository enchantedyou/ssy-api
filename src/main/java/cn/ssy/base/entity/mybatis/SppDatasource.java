package cn.ssy.base.entity.mybatis;

public class SppDatasource extends SppDatasourceKey {
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

    public SppDatasource(String datasourceId, String datasourceType, String tlsqlInd, String platformTablePrefix, String datasourceDesc, String datasourceDriver, String datasourceUrl, String datasourceUser, String datasourcePwd, String dataCreateUser, String dataCreateTime, String dataUpdateUser, String dataUpdateTime, Integer dataVersion) {
        super(datasourceId, datasourceType);
        this.tlsqlInd = tlsqlInd;
        this.platformTablePrefix = platformTablePrefix;
        this.datasourceDesc = datasourceDesc;
        this.datasourceDriver = datasourceDriver;
        this.datasourceUrl = datasourceUrl;
        this.datasourceUser = datasourceUser;
        this.datasourcePwd = datasourcePwd;
        this.dataCreateUser = dataCreateUser;
        this.dataCreateTime = dataCreateTime;
        this.dataUpdateUser = dataUpdateUser;
        this.dataUpdateTime = dataUpdateTime;
        this.dataVersion = dataVersion;
    }

    public SppDatasource() {
        super();
    }

    public String getTlsqlInd() {
        return tlsqlInd;
    }

    public void setTlsqlInd(String tlsqlInd) {
        this.tlsqlInd = tlsqlInd == null ? null : tlsqlInd.trim();
    }

    public String getPlatformTablePrefix() {
        return platformTablePrefix;
    }

    public void setPlatformTablePrefix(String platformTablePrefix) {
        this.platformTablePrefix = platformTablePrefix == null ? null : platformTablePrefix.trim();
    }

    public String getDatasourceDesc() {
        return datasourceDesc;
    }

    public void setDatasourceDesc(String datasourceDesc) {
        this.datasourceDesc = datasourceDesc == null ? null : datasourceDesc.trim();
    }

    public String getDatasourceDriver() {
        return datasourceDriver;
    }

    public void setDatasourceDriver(String datasourceDriver) {
        this.datasourceDriver = datasourceDriver == null ? null : datasourceDriver.trim();
    }

    public String getDatasourceUrl() {
        return datasourceUrl;
    }

    public void setDatasourceUrl(String datasourceUrl) {
        this.datasourceUrl = datasourceUrl == null ? null : datasourceUrl.trim();
    }

    public String getDatasourceUser() {
        return datasourceUser;
    }

    public void setDatasourceUser(String datasourceUser) {
        this.datasourceUser = datasourceUser == null ? null : datasourceUser.trim();
    }

    public String getDatasourcePwd() {
        return datasourcePwd;
    }

    public void setDatasourcePwd(String datasourcePwd) {
        this.datasourcePwd = datasourcePwd == null ? null : datasourcePwd.trim();
    }

    public String getDataCreateUser() {
        return dataCreateUser;
    }

    public void setDataCreateUser(String dataCreateUser) {
        this.dataCreateUser = dataCreateUser == null ? null : dataCreateUser.trim();
    }

    public String getDataCreateTime() {
        return dataCreateTime;
    }

    public void setDataCreateTime(String dataCreateTime) {
        this.dataCreateTime = dataCreateTime == null ? null : dataCreateTime.trim();
    }

    public String getDataUpdateUser() {
        return dataUpdateUser;
    }

    public void setDataUpdateUser(String dataUpdateUser) {
        this.dataUpdateUser = dataUpdateUser == null ? null : dataUpdateUser.trim();
    }

    public String getDataUpdateTime() {
        return dataUpdateTime;
    }

    public void setDataUpdateTime(String dataUpdateTime) {
        this.dataUpdateTime = dataUpdateTime == null ? null : dataUpdateTime.trim();
    }

    public Integer getDataVersion() {
        return dataVersion;
    }

    public void setDataVersion(Integer dataVersion) {
        this.dataVersion = dataVersion;
    }

	@Override
	public String toString() {
		return "SppDatasource [tlsqlInd=" + tlsqlInd + ", platformTablePrefix=" + platformTablePrefix + ", datasourceDesc=" + datasourceDesc + ", datasourceDriver="
				+ datasourceDriver + ", datasourceUrl=" + datasourceUrl + ", datasourceUser=" + datasourceUser + ", datasourcePwd=" + datasourcePwd + ", dataCreateUser="
				+ dataCreateUser + ", dataCreateTime=" + dataCreateTime + ", dataUpdateUser=" + dataUpdateUser + ", dataUpdateTime=" + dataUpdateTime + ", dataVersion="
				+ dataVersion + ", " + super.toString() + "]";
	}
    
    
}