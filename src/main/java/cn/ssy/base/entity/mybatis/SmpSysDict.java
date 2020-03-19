package cn.ssy.base.entity.mybatis;

import java.io.Serializable;



public class SmpSysDict implements Serializable{

	private static final long serialVersionUID = -925074621721540985L;

	private String dictType;

	private String dictId;

	private String dictName;

	private String parentDictType;

	private String parentDictId;

	private String status;

	private Long sortNo;

	private String timestamp;

	private String dictTypeName;

	public String getDictType() {
		return dictType;
	}

	public void setDictType(String dictType) {
		this.dictType = dictType;
	}

	public String getDictId() {
		return dictId;
	}

	public void setDictId(String dictId) {
		this.dictId = dictId;
	}

	public String getDictName() {
		return dictName;
	}

	public void setDictName(String dictName) {
		this.dictName = dictName;
	}

	public String getParentDictType() {
		return parentDictType;
	}

	public void setParentDictType(String parentDictType) {
		this.parentDictType = parentDictType;
	}

	public String getParentDictId() {
		return parentDictId;
	}

	public void setParentDictId(String parentDictId) {
		this.parentDictId = parentDictId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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
		this.timestamp = timestamp;
	}

	public String getDictTypeName() {
		return dictTypeName;
	}

	public void setDictTypeName(String dictTypeName) {
		this.dictTypeName = dictTypeName;
	}

	@Override
	public String toString() {
		return "SmpSysDict [dictType=" + dictType + ", dictId=" + dictId + ", dictName=" + dictName + ", parentDictType=" + parentDictType + ", parentDictId=" + parentDictId + ", status=" + status + ", sortNo=" + sortNo + ", timestamp=" + timestamp + ", dictTypeName=" + dictTypeName + "]";
	}
}