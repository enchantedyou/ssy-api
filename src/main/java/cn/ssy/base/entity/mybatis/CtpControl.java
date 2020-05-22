package cn.ssy.base.entity.mybatis;

public class CtpControl {
    private String fieldName;

    private String fieldDesc;

    public CtpControl(String fieldName, String fieldDesc) {
        this.fieldName = fieldName;
        this.fieldDesc = fieldDesc;
    }

    public CtpControl() {
        super();
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName == null ? null : fieldName.trim();
    }

    public String getFieldDesc() {
        return fieldDesc;
    }

    public void setFieldDesc(String fieldDesc) {
        this.fieldDesc = fieldDesc == null ? null : fieldDesc.trim();
    }
}