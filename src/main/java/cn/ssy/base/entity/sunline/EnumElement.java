package cn.ssy.base.entity.sunline;

import java.io.Serializable;

/**
 * <p>
 * 文件功能说明：
 *       	枚举元素		
 * </p>
 * 
 * @Author sunshaoyu
 *         <p>
 *         <li>2019年10月31日-上午10:34:22</li>
 *         <li>修改记录</li>
 *         <li>-----------------------------------------------------------</li>
 *         <li>标记：修订内容</li>
 *         <li>2019年10月31日-sunshaoyu：创建注释模板</li>
 *         <li>-----------------------------------------------------------</li>
 *         </p>
 */
public class EnumElement implements Serializable{
	
	private static final long serialVersionUID = 7318002592881963378L;

	private String id;
	
	private String value;
	
	private String longname;
	
	public EnumElement(String id, String value, String longname) {
		super();
		this.id = id;
		this.value = value;
		this.longname = longname;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getLongname() {
		return longname;
	}

	public void setLongname(String longname) {
		this.longname = longname;
	}

	@Override
	public String toString() {
		return "\r\n[枚举元素]id:" + id + ",枚举值:" + value + ",描述:" + longname;
	}
}