package cn.ssy.base.entity.mybatis;

public class TspTaskKey {
    private String subSystemCode;

    private String taskNum;

    private String systemCode;

    private String corporateCode;

    public TspTaskKey(String subSystemCode, String taskNum, String systemCode, String corporateCode) {
        this.subSystemCode = subSystemCode;
        this.taskNum = taskNum;
        this.systemCode = systemCode;
        this.corporateCode = corporateCode;
    }

    public TspTaskKey() {
        super();
    }

    public String getSubSystemCode() {
        return subSystemCode;
    }

    public void setSubSystemCode(String subSystemCode) {
        this.subSystemCode = subSystemCode == null ? null : subSystemCode.trim();
    }

    public String getTaskNum() {
        return taskNum;
    }

    public void setTaskNum(String taskNum) {
        this.taskNum = taskNum == null ? null : taskNum.trim();
    }

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode == null ? null : systemCode.trim();
    }

    public String getCorporateCode() {
        return corporateCode;
    }

    public void setCorporateCode(String corporateCode) {
        this.corporateCode = corporateCode == null ? null : corporateCode.trim();
    }
}