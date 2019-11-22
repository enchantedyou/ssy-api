package cn.ssy.base.entity.sunline;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

//枚举实体类
public class EnumType implements Serializable{

	private static final long serialVersionUID = -8374712277374090092L;

	private String enumId;
	
	private String EnumLocation;
	
	private String longname;
	
	private String base;
	
	private String maxLength;
	
	private String fullName;
	
	private List<EnumElement> elementList = new ArrayList<EnumElement>();

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

	public List<EnumElement> getElementList() {
		return elementList;
	}

	public void setElementList(List<EnumElement> elementList) {
		this.elementList = elementList;
	}

	public String getEnumLocation() {
		return EnumLocation;
	}

	public void setEnumLocation(String enumLocation) {
		EnumLocation = enumLocation;
	}
	
	public void addEnumElement(EnumElement enumElement) {
		this.elementList.add(enumElement);
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
		for(EnumElement e : elementList){
			str += e.toString();
		}
		return str;
	}
}
