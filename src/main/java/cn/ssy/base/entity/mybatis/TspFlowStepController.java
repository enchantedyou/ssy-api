package cn.ssy.base.entity.mybatis;



public class TspFlowStepController {

	private String systemCode;

	private String corporateCode;

	private String tranFlowId;

	private String flowStepName;

	private Integer flowStepNum;

	private Integer executionNo;

	private String tranGroupId;

	private String isExecution;

	public String getSystemCode() {
		return systemCode;
	}

	public void setSystemCode(String systemCode) {
		this.systemCode = systemCode;
	}

	public String getCorporateCode() {
		return corporateCode;
	}

	public void setCorporateCode(String corporateCode) {
		this.corporateCode = corporateCode;
	}

	public String getTranFlowId() {
		return tranFlowId;
	}

	public void setTranFlowId(String tranFlowId) {
		this.tranFlowId = tranFlowId;
	}

	public String getFlowStepName() {
		return flowStepName;
	}

	public void setFlowStepName(String flowStepName) {
		this.flowStepName = flowStepName;
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
		this.tranGroupId = tranGroupId;
	}

	public String getIsExecution() {
		return isExecution;
	}

	public void setIsExecution(String isExecution) {
		this.isExecution = isExecution;
	}

	@Override
	public String toString() {
		return "TspFlowStepController [systemCode=" + systemCode + ", corporateCode=" + corporateCode + ", tranFlowId=" + tranFlowId + ", flowStepName=" + flowStepName + ", flowStepNum=" + flowStepNum + ", executionNo=" + executionNo + ", tranGroupId=" + tranGroupId + ", isExecution=" + isExecution + "]";
	}
}