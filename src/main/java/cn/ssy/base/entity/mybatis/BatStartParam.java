package cn.ssy.base.entity.mybatis;

/**
 * <p>
 * 文件功能说明：
 *       	批量启动参数		
 * </p>
 * 
 * @Author sunshaoyu
 *         <p>
 *         <li>2020年3月16日-下午4:40:10</li>
 *         <li>修改记录</li>
 *         <li>-----------------------------------------------------------</li>
 *         <li>标记：修订内容</li>
 *         <li>2020年3月16日-sunshaoyu：创建注释模板</li>
 *         <li>-----------------------------------------------------------</li>
 *         </p>
 */
public class BatStartParam {

	private String tranCode;
	
	private Integer stepId;
	
	private String taskNum;

	public BatStartParam(String tranCode, Integer stepId, String taskNum) {
		super();
		this.tranCode = tranCode;
		this.stepId = stepId;
		this.taskNum = taskNum;
	}

	public String getTranCode() {
		return tranCode;
	}

	public void setTranCode(String tranCode) {
		this.tranCode = tranCode;
	}

	public Integer getStepId() {
		return stepId;
	}

	public void setStepId(Integer stepId) {
		this.stepId = stepId;
	}

	public String getTaskNum() {
		return taskNum;
	}

	public void setTaskNum(String taskNum) {
		this.taskNum = taskNum;
	}

	@Override
	public String toString() {
		return "[tranCode:" + tranCode + "-stepId:" + stepId + "-taskNum:" + taskNum + "]";
	}
}
