package cn.ssy.base.entity.mybatis;

public class TspTranControllerKey {
    private String systemCode;

    private String corporateCode;

    private String tranGroupId;

    private Integer stepId;

    private String tranCode;

    public TspTranControllerKey(String systemCode, String corporateCode, String tranGroupId, Integer stepId, String tranCode) {
        this.systemCode = systemCode;
        this.corporateCode = corporateCode;
        this.tranGroupId = tranGroupId;
        this.stepId = stepId;
        this.tranCode = tranCode;
    }

    public TspTranControllerKey() {
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

    public String getTranGroupId() {
        return tranGroupId;
    }

    public void setTranGroupId(String tranGroupId) {
        this.tranGroupId = tranGroupId == null ? null : tranGroupId.trim();
    }

    public Integer getStepId() {
        return stepId;
    }

    public void setStepId(Integer stepId) {
        this.stepId = stepId;
    }

    public String getTranCode() {
        return tranCode;
    }

    public void setTranCode(String tranCode) {
        this.tranCode = tranCode == null ? null : tranCode.trim();
    }
}