package cn.ssy.base.entity.sunline;

import java.io.Serializable;

/**
 * <p>
 * 文件功能说明：
 *       	格式化vue json的实体类		
 * </p>
 * 
 * @Author sunshaoyu
 *         <p>
 *         <li>2020年5月12日-下午3:42:09</li>
 *         <li>修改记录</li>
 *         <li>-----------------------------------------------------------</li>
 *         <li>标记：修订内容</li>
 *         <li>2020年5月12日-sunshaoyu：创建注释模板</li>
 *         <li>-----------------------------------------------------------</li>
 *         </p>
 */
public class FormatVueJsonCode implements Serializable{
	private static final long serialVersionUID = 8855678772254812612L;

	private String control;
	
	private String label;
	
	private BaseType baseType;

	public FormatVueJsonCode(String control, String label, BaseType baseType) {
		super();
		this.control = control;
		this.label = label;
		this.baseType = baseType;
	}

	public String getControl() {
		return control;
	}

	public void setControl(String control) {
		this.control = control;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public BaseType getBaseType() {
		return baseType;
	}

	public void setBaseType(BaseType baseType) {
		this.baseType = baseType;
	}

	@Override
	public String toString() {
		return "FormatVueJsonCode [control=" + control + ", label=" + label + ", baseType=" + baseType + "]";
	}
}
