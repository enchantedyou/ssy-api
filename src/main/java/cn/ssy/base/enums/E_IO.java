package cn.ssy.base.enums;


/**
 * <p>
 * 文件功能说明：
 *       	输入输出枚举		
 * </p>
 * 
 * @Author sunshaoyu
 *         <p>
 *         <li>2019年10月31日-下午3:26:38</li>
 *         <li>修改记录</li>
 *         <li>-----------------------------------------------------------</li>
 *         <li>标记：修订内容</li>
 *         <li>2019年10月31日-sunshaoyu：创建注释模板</li>
 *         <li>-----------------------------------------------------------</li>
 *         </p>
 */
public enum E_IO {
	
	INPUT("I","输入"),
	
	OUTPUT("O","输出");
	
	private String id;
	
	private String desc;
	
	private E_IO(String id, String desc) {
		this.id = id;
		this.desc = desc;
	}

	public String getId() {
		return id;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public void setId(String id) {
		this.id = id;
	}
}
