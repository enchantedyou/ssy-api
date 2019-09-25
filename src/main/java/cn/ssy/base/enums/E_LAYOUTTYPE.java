package cn.ssy.base.enums;

/**
 * <p>
 * 文件功能说明：
 *       	内管数据样式枚举		
 * </p>
 * 
 * @Author sunshaoyu
 *         <p>
 *         <li>2019年8月30日-下午3:25:13</li>
 *         <li>修改记录</li>
 *         <li>-----------------------------------------------------------</li>
 *         <li>标记：修订内容</li>
 *         <li>2019年8月30日-sunshaoyu：创建注释模板</li>
 *         <li>-----------------------------------------------------------</li>
 *         </p>
 */
public enum E_LAYOUTTYPE {
	
	FORM("form","表单"),
	
	TABLE("table","表格");
	
	private String id;
	
	private String desc;
	
	private E_LAYOUTTYPE(String id, String desc) {
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
