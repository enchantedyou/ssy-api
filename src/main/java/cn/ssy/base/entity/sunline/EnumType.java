package cn.ssy.base.entity.sunline;

import java.util.ArrayList;
import java.util.List;

//枚举实体类
public class EnumType {

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
		return "EnumType [enumId=" + enumId + ", EnumLocation=" + EnumLocation + ", longname=" + longname + ", base=" + base + ", maxLength=" + maxLength + ", fullName="
				+ fullName + ", elementList=" + elementList + "]";
	}
}
