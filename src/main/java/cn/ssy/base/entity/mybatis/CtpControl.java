package cn.ssy.base.entity.mybatis;



public class CtpControl {

	private String fieldName;

	private String fieldDesc;
	

	public CtpControl(String fieldName, String fieldDesc) {
		super();
		this.fieldName = fieldName;
		this.fieldDesc = fieldDesc;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldDesc() {
		return fieldDesc;
	}

	public void setFieldDesc(String fieldDesc) {
		this.fieldDesc = fieldDesc;
	}

	@Override
	public String toString() {
		return "CtpControl [fieldName=" + fieldName + ", fieldDesc=" + fieldDesc + "]";
	}
}