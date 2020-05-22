package cn.ssy.base.entity.mybatis;

public class SmpSysDictKey {
    private String dictId;

    private String dictType;

    public SmpSysDictKey(String dictId, String dictType) {
        this.dictId = dictId;
        this.dictType = dictType;
    }

    public SmpSysDictKey() {
        super();
    }

    public String getDictId() {
        return dictId;
    }

    public void setDictId(String dictId) {
        this.dictId = dictId == null ? null : dictId.trim();
    }

    public String getDictType() {
        return dictType;
    }

    public void setDictType(String dictType) {
        this.dictType = dictType == null ? null : dictType.trim();
    }
}