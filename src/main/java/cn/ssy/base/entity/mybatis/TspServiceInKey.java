package cn.ssy.base.entity.mybatis;

public class TspServiceInKey {
    private String systemCode;

    private String subSystemCode;

    private String outServiceCode;

    public TspServiceInKey(String systemCode, String subSystemCode, String outServiceCode) {
        this.systemCode = systemCode;
        this.subSystemCode = subSystemCode;
        this.outServiceCode = outServiceCode;
    }

    public TspServiceInKey() {
        super();
    }

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode == null ? null : systemCode.trim();
    }

    public String getSubSystemCode() {
        return subSystemCode;
    }

    public void setSubSystemCode(String subSystemCode) {
        this.subSystemCode = subSystemCode == null ? null : subSystemCode.trim();
    }

    public String getOutServiceCode() {
        return outServiceCode;
    }

    public void setOutServiceCode(String outServiceCode) {
        this.outServiceCode = outServiceCode == null ? null : outServiceCode.trim();
    }
}