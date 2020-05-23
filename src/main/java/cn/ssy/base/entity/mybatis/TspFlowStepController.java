package cn.ssy.base.entity.mybatis;

public class TspFlowStepController extends TspFlowStepControllerKey {
    private String flowStepName;

    private String isExecution;

    public TspFlowStepController(String systemCode, String corporateCode, String tranFlowId, Integer flowStepNum, Integer executionNo, String tranGroupId, String flowStepName, String isExecution) {
        super(systemCode, corporateCode, tranFlowId, flowStepNum, executionNo, tranGroupId);
        this.flowStepName = flowStepName;
        this.isExecution = isExecution;
    }

    public TspFlowStepController() {
        super();
    }

    public String getFlowStepName() {
        return flowStepName;
    }

    public void setFlowStepName(String flowStepName) {
        this.flowStepName = flowStepName == null ? null : flowStepName.trim();
    }

    public String getIsExecution() {
        return isExecution;
    }

    public void setIsExecution(String isExecution) {
        this.isExecution = isExecution == null ? null : isExecution.trim();
    }
}