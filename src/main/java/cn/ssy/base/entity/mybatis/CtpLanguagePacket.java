package cn.ssy.base.entity.mybatis;



public class CtpLanguagePacket {

	private String languageResourceType;

	private String languageResourceKey;

	private String uiLanguage;

	private String languageResourceValue;

	private String dataCreateTime;

	private String dataUpdateTime;

	private String dataCreateUser;

	private String dataUpdateUser;

	private Long dataVersion;

	public String getLanguageResourceType() {
		return languageResourceType;
	}

	public void setLanguageResourceType(String languageResourceType) {
		this.languageResourceType = languageResourceType;
	}

	public String getLanguageResourceKey() {
		return languageResourceKey;
	}

	public void setLanguageResourceKey(String languageResourceKey) {
		this.languageResourceKey = languageResourceKey;
	}

	public String getUiLanguage() {
		return uiLanguage;
	}

	public void setUiLanguage(String uiLanguage) {
		this.uiLanguage = uiLanguage;
	}

	public String getLanguageResourceValue() {
		return languageResourceValue;
	}

	public void setLanguageResourceValue(String languageResourceValue) {
		this.languageResourceValue = languageResourceValue;
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
		return "CtpLanguagePacket [languageResourceType=" + languageResourceType + ", languageResourceKey=" + languageResourceKey + ", uiLanguage=" + uiLanguage + ", languageResourceValue=" + languageResourceValue + ", dataCreateTime=" + dataCreateTime + ", dataUpdateTime=" + dataUpdateTime + ", dataCreateUser=" + dataCreateUser + ", dataUpdateUser=" + dataUpdateUser + ", dataVersion=" + dataVersion + "]";
	}
}