package cn.ssy.base.entity.mybatis;



public class TspServiceIn {

	private String systemCode;

	private String subSystemCode;

	private String outServiceCode;

	private String innerServiceCode;

	private String innerServiceImplCode;

	private String description;

	private String serviceCategory;

	private String routeKeys;

	private String serviceType;

	private String protocolId;

	private Integer isEnable;

	private String transactionMode;

	private String logLevel;

	private Integer timeout;

	private Integer aliasMapping;

	private Integer forceUnusedOdbCache;

	private String registerMode;

	public String getSystemCode() {
		return systemCode;
	}

	public void setSystemCode(String systemCode) {
		this.systemCode = systemCode;
	}

	public String getSubSystemCode() {
		return subSystemCode;
	}

	public void setSubSystemCode(String subSystemCode) {
		this.subSystemCode = subSystemCode;
	}

	public String getOutServiceCode() {
		return outServiceCode;
	}

	public void setOutServiceCode(String outServiceCode) {
		this.outServiceCode = outServiceCode;
	}

	public String getInnerServiceCode() {
		return innerServiceCode;
	}

	public void setInnerServiceCode(String innerServiceCode) {
		this.innerServiceCode = innerServiceCode;
	}

	public String getInnerServiceImplCode() {
		return innerServiceImplCode;
	}

	public void setInnerServiceImplCode(String innerServiceImplCode) {
		this.innerServiceImplCode = innerServiceImplCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getServiceCategory() {
		return serviceCategory;
	}

	public void setServiceCategory(String serviceCategory) {
		this.serviceCategory = serviceCategory;
	}

	public String getRouteKeys() {
		return routeKeys;
	}

	public void setRouteKeys(String routeKeys) {
		this.routeKeys = routeKeys;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getProtocolId() {
		return protocolId;
	}

	public void setProtocolId(String protocolId) {
		this.protocolId = protocolId;
	}

	public Integer getIsEnable() {
		return isEnable;
	}

	public void setIsEnable(Integer isEnable) {
		this.isEnable = isEnable;
	}

	public String getTransactionMode() {
		return transactionMode;
	}

	public void setTransactionMode(String transactionMode) {
		this.transactionMode = transactionMode;
	}

	public String getLogLevel() {
		return logLevel;
	}

	public void setLogLevel(String logLevel) {
		this.logLevel = logLevel;
	}

	public Integer getTimeout() {
		return timeout;
	}

	public void setTimeout(Integer timeout) {
		this.timeout = timeout;
	}

	public Integer getAliasMapping() {
		return aliasMapping;
	}

	public void setAliasMapping(Integer aliasMapping) {
		this.aliasMapping = aliasMapping;
	}

	public Integer getForceUnusedOdbCache() {
		return forceUnusedOdbCache;
	}

	public void setForceUnusedOdbCache(Integer forceUnusedOdbCache) {
		this.forceUnusedOdbCache = forceUnusedOdbCache;
	}

	public String getRegisterMode() {
		return registerMode;
	}

	public void setRegisterMode(String registerMode) {
		this.registerMode = registerMode;
	}

	@Override
	public String toString() {
		return "TspServiceIn [systemCode=" + systemCode + ", subSystemCode=" + subSystemCode + ", outServiceCode=" + outServiceCode + ", innerServiceCode=" + innerServiceCode + ", innerServiceImplCode=" + innerServiceImplCode + ", description=" + description + ", serviceCategory=" + serviceCategory + ", routeKeys=" + routeKeys + ", serviceType=" + serviceType + ", protocolId=" + protocolId + ", isEnable=" + isEnable + ", transactionMode=" + transactionMode + ", logLevel=" + logLevel + ", timeout=" + timeout + ", aliasMapping=" + aliasMapping + ", forceUnusedOdbCache=" + forceUnusedOdbCache + ", registerMode=" + registerMode + "]";
	}
}