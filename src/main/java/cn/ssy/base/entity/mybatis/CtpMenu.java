package cn.ssy.base.entity.mybatis;

public class CtpMenu {
    private String menuCode;

    private String menuId;

    private String menuUpperId;

    private String menuGroup;

    private String menuDefaultInd;

    private String menuDesc;

    private String pageId;

    private String pageDisplayScene;

    private String outputPageId;

    private String dataCreateTime;

    private String dataUpdateTime;

    private String dataCreateUser;

    private String dataUpdateUser;

    private Long dataVersion;

    public CtpMenu(String menuCode, String menuId, String menuUpperId, String menuGroup, String menuDefaultInd, String menuDesc, String pageId, String pageDisplayScene, String outputPageId, String dataCreateTime, String dataUpdateTime, String dataCreateUser, String dataUpdateUser, Long dataVersion) {
        this.menuCode = menuCode;
        this.menuId = menuId;
        this.menuUpperId = menuUpperId;
        this.menuGroup = menuGroup;
        this.menuDefaultInd = menuDefaultInd;
        this.menuDesc = menuDesc;
        this.pageId = pageId;
        this.pageDisplayScene = pageDisplayScene;
        this.outputPageId = outputPageId;
        this.dataCreateTime = dataCreateTime;
        this.dataUpdateTime = dataUpdateTime;
        this.dataCreateUser = dataCreateUser;
        this.dataUpdateUser = dataUpdateUser;
        this.dataVersion = dataVersion;
    }

    public CtpMenu() {
        super();
    }

    public String getMenuCode() {
        return menuCode;
    }

    public void setMenuCode(String menuCode) {
        this.menuCode = menuCode == null ? null : menuCode.trim();
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId == null ? null : menuId.trim();
    }

    public String getMenuUpperId() {
        return menuUpperId;
    }

    public void setMenuUpperId(String menuUpperId) {
        this.menuUpperId = menuUpperId == null ? null : menuUpperId.trim();
    }

    public String getMenuGroup() {
        return menuGroup;
    }

    public void setMenuGroup(String menuGroup) {
        this.menuGroup = menuGroup == null ? null : menuGroup.trim();
    }

    public String getMenuDefaultInd() {
        return menuDefaultInd;
    }

    public void setMenuDefaultInd(String menuDefaultInd) {
        this.menuDefaultInd = menuDefaultInd == null ? null : menuDefaultInd.trim();
    }

    public String getMenuDesc() {
        return menuDesc;
    }

    public void setMenuDesc(String menuDesc) {
        this.menuDesc = menuDesc == null ? null : menuDesc.trim();
    }

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId == null ? null : pageId.trim();
    }

    public String getPageDisplayScene() {
        return pageDisplayScene;
    }

    public void setPageDisplayScene(String pageDisplayScene) {
        this.pageDisplayScene = pageDisplayScene == null ? null : pageDisplayScene.trim();
    }

    public String getOutputPageId() {
        return outputPageId;
    }

    public void setOutputPageId(String outputPageId) {
        this.outputPageId = outputPageId == null ? null : outputPageId.trim();
    }

    public String getDataCreateTime() {
        return dataCreateTime;
    }

    public void setDataCreateTime(String dataCreateTime) {
        this.dataCreateTime = dataCreateTime == null ? null : dataCreateTime.trim();
    }

    public String getDataUpdateTime() {
        return dataUpdateTime;
    }

    public void setDataUpdateTime(String dataUpdateTime) {
        this.dataUpdateTime = dataUpdateTime == null ? null : dataUpdateTime.trim();
    }

    public String getDataCreateUser() {
        return dataCreateUser;
    }

    public void setDataCreateUser(String dataCreateUser) {
        this.dataCreateUser = dataCreateUser == null ? null : dataCreateUser.trim();
    }

    public String getDataUpdateUser() {
        return dataUpdateUser;
    }

    public void setDataUpdateUser(String dataUpdateUser) {
        this.dataUpdateUser = dataUpdateUser == null ? null : dataUpdateUser.trim();
    }

    public Long getDataVersion() {
        return dataVersion;
    }

    public void setDataVersion(Long dataVersion) {
        this.dataVersion = dataVersion;
    }
}