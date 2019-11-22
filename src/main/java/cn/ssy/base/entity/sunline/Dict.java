package cn.ssy.base.entity.sunline;

import java.io.Serializable;

/**
 * <p>
 * 文件功能说明：
 *       	字典信息实体类		
 * </p>
 * 
 * @Author sunshaoyu
 *         <p>
 *         <li>2019年10月28日-下午2:13:33</li>
 *         <li>修改记录</li>
 *         <li>-----------------------------------------------------------</li>
 *         <li>标记：修订内容</li>
 *         <li>2019年10月28日-sunshaoyu：创建注释模板</li>
 *         <li>-----------------------------------------------------------</li>
 *         </p>
 */
public class Dict implements Serializable{

	private static final long serialVersionUID = 1484943384226783255L;

	private String id;
	
	private String dictType;
	
	private String longname;
	
	private String refType;
	
	private String desc;
	
	public Dict(){
		
	}
	
	public Dict(String id, String dictType, String longname, String refType, String desc) {
		super();
		this.id = id;
		this.dictType = dictType;
		this.longname = longname;
		this.refType = refType;
		this.desc = desc;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDictType() {
		return dictType;
	}

	public void setDictType(String dictType) {
		this.dictType = dictType;
	}

	public String getLongname() {
		return longname;
	}

	public void setLongname(String longname) {
		this.longname = longname;
	}

	public String getRefType() {
		return refType;
	}

	public void setRefType(String refType) {
		this.refType = refType;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Override
	public String toString() {
		return "[字典]id:" + id + ",字典类型:" + dictType + ",引用类型:" + refType + ",中文描述:" + desc + ",英文描述:" + longname;
	}
}
