package cn.ssy.base.entity.sunline;

import java.io.Serializable;

/**
 * <p>
 * 文件功能说明：
 *       	必输		
 * </p>
 * 
 * @Author sunshaoyu
 *         <p>
 *         <li>2020年3月12日-上午10:58:26</li>
 *         <li>修改记录</li>
 *         <li>-----------------------------------------------------------</li>
 *         <li>标记：修订内容</li>
 *         <li>2020年3月12日-sunshaoyu：创建注释模板</li>
 *         <li>-----------------------------------------------------------</li>
 *         </p>
 */
public class Required implements Serializable{

	private static final long serialVersionUID = 3441227797991442922L;
	
	private boolean required;
	
	private String message;
	
	public Required(boolean required) {
		super();
		this.required = required;
	}
	
	public Required(boolean required, String message) {
		super();
		this.required = required;
		if(this.required){
			this.message = message;
		}
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "Required [required=" + required + ", message=" + message + "]";
	}
}
