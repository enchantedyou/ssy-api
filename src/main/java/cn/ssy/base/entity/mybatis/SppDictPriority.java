package cn.ssy.base.entity.mybatis;



public class SppDictPriority {

	private String dictType;

	private Integer dictPriority;

	private Boolean isEnabled;

	private String groupId;

	public String getDictType() {
		return dictType;
	}

	public void setDictType(String dictType) {
		this.dictType = dictType;
	}

	public Integer getDictPriority() {
		return dictPriority;
	}

	public void setDictPriority(Integer dictPriority) {
		this.dictPriority = dictPriority;
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
		return "SppDictPriority [dictType=" + dictType + ", dictPriority=" + dictPriority + ", isEnabled=" + isEnabled + ", groupId=" + groupId + "]";
	}
}