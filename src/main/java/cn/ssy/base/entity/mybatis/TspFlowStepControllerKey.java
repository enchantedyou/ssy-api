package cn.ssy.base.entity.mybatis;

public class TspFlowStepControllerKey {
    private String systemCode;

    private String corporateCode;

    private String tranFlowId;

    private Integer flowStepNum;

    private Integer executionNo;

    private String tranGroupId;

    public TspFlowStepControllerKey(String systemCode, String corporateCode, String tranFlowId, Integer flowStepNum, Integer executionNo, String tranGroupId) {
        this.systemCode = systemCode;
        this.corporateCode = corporateCode;
        this.tranFlowId = tranFlowId;
        this.flowStepNum = flowStepNum;
        this.executionNo = executionNo;
        this.tranGroupId = tranGroupId;
    }

    public TspFlowStepControllerKey() {
        super();
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

    public String getTranFlowId() {
        return tranFlowId;
    }

    public void setTranFlowId(String tranFlowId) {
        this.tranFlowId = tranFlowId == null ? null : tranFlowId.trim();
    }

    public Integer getFlowStepNum() {
        return flowStepNum;
    }

    public void setFlowStepNum(Integer flowStepNum) {
        this.flowStepNum = flowStepNum;
    }

    public Integer getExecutionNo() {
        return executionNo;
    }

    public void setExecutionNo(Integer executionNo) {
        this.executionNo = executionNo;
    }

    public String getTranGroupId() {
        return tranGroupId;
    }

    public void setTranGroupId(String tranGroupId) {
        this.tranGroupId = tranGroupId == null ? null : tranGroupId.trim();
    }
}