package cn.ssy.base.entity.mybatis;



public class SppEnumPriority {

	private String enumType;

	private Integer enumPriority;

	private Boolean isEnabled;

	private String groupId;

	public String getEnumType() {
		return enumType;
	}

	public void setEnumType(String enumType) {
		this.enumType = enumType;
	}

	public Integer getEnumPriority() {
		return enumPriority;
	}

	public void setEnumPriority(Integer enumPriority) {
		this.enumPriority = enumPriority;
	}

	public Boolean getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(Boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	@Override
	public String toString() {
		return "SppEnumPriority [enumType=" + enumType + ", enumPriority=" + enumPriority + ", isEnabled=" + isEnabled + ", groupId=" + groupId + "]";
	}
}