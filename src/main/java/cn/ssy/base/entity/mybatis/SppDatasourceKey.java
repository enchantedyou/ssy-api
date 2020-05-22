package cn.ssy.base.entity.mybatis;

public class SppDatasourceKey {
    private String datasourceId;

    private String datasourceType;

    public SppDatasourceKey(String datasourceId, String datasourceType) {
        this.datasourceId = datasourceId;
        this.datasourceType = datasourceType;
    }

    public SppDatasourceKey() {
        super();
    }

    public String getDatasourceId() {
        return datasourceId;
    }

    public void setDatasourceId(String datasourceId) {
        this.datasourceId = datasourceId == null ? null : datasourceId.trim();
    }

    public String getDatasourceType() {
        return datasourceType;
    }

    public void setDatasourceType(String datasourceType) {
        this.datasourceType = datasourceType == null ? null : datasourceType.trim();
    }

	@Override
	public String toString() {
		return "SppDatasourceKey [datasourceId=" + datasourceId + ", datasourceType=" + datasourceType + "]";
	} 
}