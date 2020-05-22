package cn.ssy.base.entity.mybatis;

public class SmpSysTrans {
    private String transCd;

    private String transName;

    private String serviceCd;

    private String encapCd;

    private String transStatus;

    private Long dealCnt;

    private String sceneId;

    private String dcnId;

    private String versionId;

    private String appId;

    private Integer timeout;

    private String groupId;

    private String isrecord;

    private String islist;

    private String systemId;

    private String isApproval;

    public SmpSysTrans(String transCd, String transName, String serviceCd, String encapCd, String transStatus, Long dealCnt, String sceneId, String dcnId, String versionId, String appId, Integer timeout, String groupId, String isrecord, String islist, String systemId, String isApproval) {
        this.transCd = transCd;
        this.transName = transName;
        this.serviceCd = serviceCd;
        this.encapCd = encapCd;
        this.transStatus = transStatus;
        this.dealCnt = dealCnt;
        this.sceneId = sceneId;
        this.dcnId = dcnId;
        this.versionId = versionId;
        this.appId = appId;
        this.timeout = timeout;
        this.groupId = groupId;
        this.isrecord = isrecord;
        this.islist = islist;
        this.systemId = systemId;
        this.isApproval = isApproval;
    }

    public SmpSysTrans() {
        super();
    }

    public String getTransCd() {
        return transCd;
    }

    public void setTransCd(String transCd) {
        this.transCd = transCd == null ? null : transCd.trim();
    }

    public String getTransName() {
        return transName;
    }

    public void setTransName(String transName) {
        this.transName = transName == null ? null : transName.trim();
    }

    public String getServiceCd() {
        return serviceCd;
    }

    public void setServiceCd(String serviceCd) {
        this.serviceCd = serviceCd == null ? null : serviceCd.trim();
    }

    public String getEncapCd() {
        return encapCd;
    }

    public void setEncapCd(String encapCd) {
        this.encapCd = encapCd == null ? null : encapCd.trim();
    }

    public String getTransStatus() {
        return transStatus;
    }

    public void setTransStatus(String transStatus) {
        this.transStatus = transStatus == null ? null : transStatus.trim();
    }

    public Long getDealCnt() {
        return dealCnt;
    }

    public void setDealCnt(Long dealCnt) {
        this.dealCnt = dealCnt;
    }

    public String getSceneId() {
        return sceneId;
    }

    public void setSceneId(String sceneId) {
        this.sceneId = sceneId == null ? null : sceneId.trim();
    }

    public String getDcnId() {
        return dcnId;
    }

    public void setDcnId(String dcnId) {
        this.dcnId = dcnId == null ? null : dcnId.trim();
    }

    public String getVersionId() {
        return versionId;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId == null ? null : versionId.trim();
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId == null ? null : appId.trim();
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId == null ? null : groupId.trim();
    }

    public String getIsrecord() {
        return isrecord;
    }

    public void setIsrecord(String isrecord) {
        this.isrecord = isrecord == null ? null : isrecord.trim();
    }

    public String getIslist() {
        return islist;
    }

    public void setIslist(String islist) {
        this.islist = islist == null ? null : islist.trim();
    }

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId == null ? null : systemId.trim();
    }

    public String getIsApproval() {
        return isApproval;
    }

    public void setIsApproval(String isApproval) {
        this.isApproval = isApproval == null ? null : isApproval.trim();
    }
}