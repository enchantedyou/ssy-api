package cn.ssy.base.entity.sunline;

import java.io.Serializable;

/**
 * <p>
 * 文件功能说明：
 *       	复合类型元素	
 * </p>
 * 
 * @Author sunshaoyu
 *         <p>
 *         <li>2020年5月12日-上午11:25:49</li>
 *         <li>修改记录</li>
 *         <li>-----------------------------------------------------------</li>
 *         <li>标记：修订内容</li>
 *         <li>2020年5月12日-sunshaoyu：创建注释模板</li>
 *         <li>-----------------------------------------------------------</li>
 *         </p>
 */
public class ComplexElement implements Serializable{
	private static final long serialVersionUID = 1484943384226783255L;

	private String id;
	private String longname;
	private String type;
	private String ref;
	
	private String required = "false";
	private String desc;
	private String multi = "false";
	private String range = "false";
	
	private String array = "false";
	private String _final = "false";
	private String override = "false";
	private String allowSubType = "false";
	
	public ComplexElement(String id, String longname, String type, String ref, String desc) {
		super();
		this.id = id;
		this.longname = longname;
		this.type = type;
		this.ref = ref;
		this.desc = desc;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLongname() {
		return longname;
	}
	public void setLongname(String longname) {
		this.longname = longname;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getRef() {
		return ref;
	}
	public void setRef(String ref) {
		this.ref = ref;
	}
	public String getRequired() {
		return required;
	}
	public void setRequired(String required) {
		this.required = required;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getMulti() {
		return multi;
	}
	public void setMulti(String multi) {
		this.multi = multi;
	}
	public String getRange() {
		return range;
	}
	public void setRange(String range) {
		this.range = range;
	}
	public String getArray() {
		return array;
	}
	public void setArray(String array) {
		this.array = array;
	}
	public String get_final() {
		return _final;
	}
	public void set_final(String _final) {
		this._final = _final;
	}
	public String getOverride() {
		return override;
	}
	public void setOverride(String override) {
		this.override = override;
	}
	public String getAllowSubType() {
		return allowSubType;
	}
	public void setAllowSubType(String allowSubType) {
		this.allowSubType = allowSubType;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@Override
	public String toString() {
		return "<element id=\""+this.id+"\" longname=\""+this.longname+"\" type=\""+this.type+"\" ref=\""+this.ref+"\" required=\"false\" desc=\""+this.desc+"\" multi=\"false\" range=\"false\" array=\"false\" final=\"false\" override=\"false\" allowSubType=\"true\"/>";
	}
}
