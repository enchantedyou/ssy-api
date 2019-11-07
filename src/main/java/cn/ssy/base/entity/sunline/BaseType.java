package cn.ssy.base.entity.sunline;

import java.io.Serializable;

/**
 * <p>
 * 文件功能说明：
 *       	基础引用类型		
 * </p>
 * 
 * @Author sunshaoyu
 *         <p>
 *         <li>2019年10月30日-下午1:27:34</li>
 *         <li>修改记录</li>
 *         <li>-----------------------------------------------------------</li>
 *         <li>标记：修订内容</li>
 *         <li>2019年10月30日-sunshaoyu：创建注释模板</li>
 *         <li>-----------------------------------------------------------</li>
 *         </p>
 */
public class BaseType implements Serializable{

	private static final long serialVersionUID = -4704486038192174332L;

	private String id;
	
	private String base;
	
	private String longname;
	
	private String maxLength;
	
	private String fractionDigits;

	public BaseType(String id, String base, String longname, String maxLength, String fractionDigits) {
		super();
		this.id = id;
		this.base = base;
		this.longname = longname;
		this.maxLength = maxLength;
		this.fractionDigits = fractionDigits;
	}
	
	public BaseType(){
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBase() {
		return base;
	}

	public void setBase(String base) {
		this.base = base;
	}

	public String getLongname() {
		return longname;
	}

	public void setLongname(String longname) {
		this.longname = longname;
	}

	public String getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(String maxLength) {
		this.maxLength = maxLength;
	}

	public String getFractionDigits() {
		return fractionDigits;
	}

	public void setFractionDigits(String fractionDigits) {
		this.fractionDigits = fractionDigits;
	}

	@Override
	public String toString() {
		return "BaseType [id=" + id + ", base=" + base + ", longname=" + longname + ", maxLength=" + maxLength + ", fractionDigits=" + fractionDigits + "]";
	}
}
