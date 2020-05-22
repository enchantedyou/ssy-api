package cn.ssy.base.entity.mybatis;

public class SppDictPriority {
    private String dictType;

    private Integer dictPriority;

    private Boolean isEnabled;

    private String groupId;

    public SppDictPriority(String dictType, Integer dictPriority, Boolean isEnabled, String groupId) {
        this.dictType = dictType;
        this.dictPriority = dictPriority;
        this.isEnabled = isEnabled;
        this.groupId = groupId;
    }

    public SppDictPriority() {
        super();
    }

    public String getDictType() {
        return dictType;
    }

    public void setDictType(String dictType) {
        this.dictType = dictType == null ? null : dictType.trim();
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
        this.groupId = groupId == null ? null : groupId.trim();
    }
}