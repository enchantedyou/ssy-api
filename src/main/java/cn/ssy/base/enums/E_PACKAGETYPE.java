package cn.ssy.base.enums;

/**
 * <p>
 * 文件功能说明：
 *       	部署包打包类型		
 * </p>
 * 
 * @Author sunshaoyu
 *         <p>
 *         <li>2019年8月7日-下午4:05:27</li>
 *         <li>修改记录</li>
 *         <li>-----------------------------------------------------------</li>
 *         <li>标记：修订内容</li>
 *         <li>2019年8月7日-sunshaoyu：创建注释模板</li>
 *         <li>-----------------------------------------------------------</li>
 *         </p>
 */
public enum E_PACKAGETYPE {
	
	BASE("base","底层"),
	
	IOBUS("iobus","外调服务层"),
	
	SERV("serv","业务层"),
	
	ALL("all","所有");
	
	private String id;
	
	private String desc;
	
	private E_PACKAGETYPE(String id, String desc) {
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
