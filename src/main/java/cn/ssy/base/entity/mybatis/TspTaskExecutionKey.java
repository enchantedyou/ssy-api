package cn.ssy.base.entity.mybatis;


public class TspTaskExecutionKey {
    private String taskNum;

    private String taskExeNum;

    private String tranDate;

    private String subSystemCode;

    private String systemCode;

    private String corporateCode;

    public TspTaskExecutionKey(String taskNum, String taskExeNum, String tranDate, String subSystemCode, String systemCode, String corporateCode) {
        this.taskNum = taskNum;
        this.taskExeNum = taskExeNum;
        this.tranDate = tranDate;
        this.subSystemCode = subSystemCode;
        this.systemCode = systemCode;
        this.corporateCode = corporateCode;
        System.out.println(toString());
    }

    public TspTaskExecutionKey() {
        super();
    }

    public String getTaskNum() {
        return taskNum;
    }

    public void setTaskNum(String taskNum) {
        this.taskNum = taskNum == null ? null : taskNum.trim();
    }

    public String getTaskExeNum() {
        return taskExeNum;
    }

    public void setTaskExeNum(String taskExeNum) {
        this.taskExeNum = taskExeNum == null ? null : taskExeNum.trim();
    }

    public String getTranDate() {
        return tranDate;
    }

    public void setTranDate(String tranDate) {
        this.tranDate = tranDate;
    }

    public String getSubSystemCode() {
        return subSystemCode;
    }

    public void setSubSystemCode(String subSystemCode) {
        this.subSystemCode = subSystemCode == null ? null : subSystemCode.trim();
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

	@Override
	public String toString() {
		return "TspTaskExecutionKey [taskNum=" + taskNum + ", taskExeNum=" + taskExeNum + ", tranDate=" + tranDate + ", subSystemCode=" + subSystemCode + ", systemCode="
				+ systemCode + ", corporateCode=" + corporateCode + "]";
	}
}