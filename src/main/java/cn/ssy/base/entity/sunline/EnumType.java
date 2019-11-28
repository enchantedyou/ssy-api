package cn.ssy.base.entity.sunline;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

//枚举实体类
public class EnumType implements Serializable{

	private static final long serialVersionUID = -8374712277374090092L;

	private String enumId;
	
	private String EnumLocation;
	
	private String longname;
	
	private String base;
	
	private String maxLength;
	
	private String fullName;
	
	private Map<String, EnumElement> enumElementMap = new LinkedHashMap<String, EnumElement>();

	public String getEnumId() {
		return enumId;
	}

	public void setEnumId(String enumId) {
		this.enumId = enumId;
	}

	public String getLongname() {
		return longname;
	}

	public void setLongname(String longname) {
		this.longname = longname;
	}

	public String getBase() {
		return base;
	}

	public void setBase(String base) {
		this.base = base;
	}

	public String getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(String maxLength) {
		this.maxLength = maxLength;
	}

	public Map<String, EnumElement> getEnumElementMap() {
		return enumElementMap;
	}

	public void setEnumElementMap(Map<String, EnumElement> enumElementMap) {
		this.enumElementMap = enumElementMap;
	}

	public String getEnumLocation() {
		return EnumLocation;
	}

	public void setEnumLocation(String enumLocation) {
		EnumLocation = enumLocation;
	}
	
	public void addEnumElement(EnumElement enumElement) {
		this.enumElementMap.put(enumElement.getValue(), enumElement);
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	@Override
	public String toString() {
		String str = "\r\n[枚举]id:" + enumId + ",枚举类型:" + EnumLocation + ",描述:" + longname + ",类型:" + base + ",全名:" + fullName + ",最大长度:" + maxLength;
		for(String key : enumElementMap.keySet()){
			str += enumElementMap.get(key).toString();
		}
		return str;
	}
}
