package cn.ssy.base.enums;

/**
 * <p>
 * 文件功能说明：
 *       	结构模块枚举		
 * </p>
 * 
 * @Author sunshaoyu
 *         <p>
 *         <li>2019年12月19日-上午10:32:31</li>
 *         <li>修改记录</li>
 *         <li>-----------------------------------------------------------</li>
 *         <li>标记：修订内容</li>
 *         <li>2019年12月19日-sunshaoyu：创建注释模板</li>
 *         <li>-----------------------------------------------------------</li>
 *         </p>
 */
public enum E_STRUCTMODULE {
	
	BASE("base","基础层"),
	
	IOBUS("iobus","服务总线层"),
	
	BUSI("busi","业务层");
	
	private String id;
	
	private String desc;
	
	private E_STRUCTMODULE(String id, String desc) {
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
