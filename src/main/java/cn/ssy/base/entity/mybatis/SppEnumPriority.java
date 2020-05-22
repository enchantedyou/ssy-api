package cn.ssy.base.entity.mybatis;

public class SppEnumPriority {
    private String enumType;

    private Integer enumPriority;

    private Boolean isEnabled;

    private String groupId;

    public SppEnumPriority(String enumType, Integer enumPriority, Boolean isEnabled, String groupId) {
        this.enumType = enumType;
        this.enumPriority = enumPriority;
        this.isEnabled = isEnabled;
        this.groupId = groupId;
    }

    public SppEnumPriority() {
        super();
    }

    public String getEnumType() {
        return enumType;
    }

    public void setEnumType(String enumType) {
        this.enumType = enumType == null ? null : enumType.trim();
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
        this.groupId = groupId == null ? null : groupId.trim();
    }
}