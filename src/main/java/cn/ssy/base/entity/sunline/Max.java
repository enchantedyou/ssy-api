package cn.ssy.base.entity.sunline;

import java.io.Serializable;

/**
 * <p>
 * 文件功能说明：
 *       	前端json中的规则之一,用来限制字段最大长度		
 * </p>
 * 
 * @Author sunshaoyu
 *         <p>
 *         <li>2020年3月12日-上午10:51:06</li>
 *         <li>修改记录</li>
 *         <li>-----------------------------------------------------------</li>
 *         <li>标记：修订内容</li>
 *         <li>2020年3月12日-sunshaoyu：创建注释模板</li>
 *         <li>-----------------------------------------------------------</li>
 *         </p>
 */
public class Max implements Serializable{

	private static final long serialVersionUID = 3441227797991442922L;
	
	private Integer max;
	
	private String message;

	public Max(String max) {
		super();
		try{
			this.max = Integer.parseInt(max);
		}catch(Exception e){
		}
		this.message = "长度不能超过"+max+"个字符";
	}

	public Integer getMax() {
		return max;
	}

	public void setMax(Integer max) {
		this.max = max;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "Max [max=" + max + ", message=" + message + "]";
	}
}
