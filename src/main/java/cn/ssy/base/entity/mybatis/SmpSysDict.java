package cn.ssy.base.entity.mybatis;

import java.io.Serializable;

public class SmpSysDict extends SmpSysDictKey implements Serializable{
	private static final long serialVersionUID = 1339489825685828052L;

	private String dictName;

    private String parentDictType;

    private String parentDictId;

    private String status;

    private Long sortNo;

    private String timestamp;

    private String dictTypeName;

    public SmpSysDict(String dictId, String dictType, String dictName, String parentDictType, String parentDictId, String status, Long sortNo, String timestamp, String dictTypeName) {
        super(dictId, dictType);
        this.dictName = dictName;
        this.parentDictType = parentDictType;
        this.parentDictId = parentDictId;
        this.status = status;
        this.sortNo = sortNo;
        this.timestamp = timestamp;
        this.dictTypeName = dictTypeName;
    }

    public SmpSysDict() {
        super();
    }

    public String getDictName() {
        return dictName;
    }

    public void setDictName(String dictName) {
        this.dictName = dictName == null ? null : dictName.trim();
    }

    public String getParentDictType() {
        return parentDictType;
    }

    public void setParentDictType(String parentDictType) {
        this.parentDictType = parentDictType == null ? null : parentDictType.trim();
    }

    public String getParentDictId() {
        return parentDictId;
    }

    public void setParentDictId(String parentDictId) {
        this.parentDictId = parentDictId == null ? null : parentDictId.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public Long getSortNo() {
        return sortNo;
    }

    public void setSortNo(Long sortNo) {
        this.sortNo = sortNo;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp == null ? null : timestamp.trim();
    }

    public String getDictTypeName() {
        return dictTypeName;
    }

    public void setDictTypeName(String dictTypeName) {
        this.dictTypeName = dictTypeName == null ? null : dictTypeName.trim();
    }

    @Override
    public String toString() {
        return "SmpSysDict{" +
                "dictName='" + dictName + '\'' +
                ", parentDictType='" + parentDictType + '\'' +
                ", parentDictId='" + parentDictId + '\'' +
                ", status='" + status + '\'' +
                ", sortNo=" + sortNo +
                ", timestamp='" + timestamp + '\'' +
                ", dictTypeName='" + dictTypeName + '\'' +
                '}';
    }
}