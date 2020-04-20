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

	public String getMenuCode() {
		return menuCode;
	}

	public void setMenuCode(String menuCode) {
		this.menuCode = menuCode;
	}

	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public String getMenuUpperId() {
		return menuUpperId;
	}

	public void setMenuUpperId(String menuUpperId) {
		this.menuUpperId = menuUpperId;
	}

	public String getMenuGroup() {
		return menuGroup;
	}

	public void setMenuGroup(String menuGroup) {
		this.menuGroup = menuGroup;
	}

	public String getMenuDefaultInd() {
		return menuDefaultInd;
	}

	public void setMenuDefaultInd(String menuDefaultInd) {
		this.menuDefaultInd = menuDefaultInd;
	}

	public String getMenuDesc() {
		return menuDesc;
	}

	public void setMenuDesc(String menuDesc) {
		this.menuDesc = menuDesc;
	}

	public String getPageId() {
		return pageId;
	}

	public void setPageId(String pageId) {
		this.pageId = pageId;
	}

	public String getPageDisplayScene() {
		return pageDisplayScene;
	}

	public void setPageDisplayScene(String pageDisplayScene) {
		this.pageDisplayScene = pageDisplayScene;
	}

	public String getOutputPageId() {
		return outputPageId;
	}

	public void setOutputPageId(String outputPageId) {
		this.outputPageId = outputPageId;
	}

	public String getDataCreateTime() {
		return dataCreateTime;
	}

	public void setDataCreateTime(String dataCreateTime) {
		this.dataCreateTime = dataCreateTime;
	}

	public String getDataUpdateTime() {
		return dataUpdateTime;
	}

	public void setDataUpdateTime(String dataUpdateTime) {
		this.dataUpdateTime = dataUpdateTime;
	}

	public String getDataCreateUser() {
		return dataCreateUser;
	}

	public void setDataCreateUser(String dataCreateUser) {
		this.dataCreateUser = dataCreateUser;
	}

	public String getDataUpdateUser() {
		return dataUpdateUser;
	}

	public void setDataUpdateUser(String dataUpdateUser) {
		this.dataUpdateUser = dataUpdateUser;
	}

	public Long getDataVersion() {
		return dataVersion;
	}

	public void setDataVersion(Long dataVersion) {
		this.dataVersion = dataVersion;
	}

	@Override
	public String toString() {
		return "CtpMenu [menuCode=" + menuCode + ", menuId=" + menuId + ", menuUpperId=" + menuUpperId + ", menuGroup=" + menuGroup + ", menuDefaultInd=" + menuDefaultInd + ", menuDesc=" + menuDesc + ", pageId=" + pageId + ", pageDisplayScene=" + pageDisplayScene + ", outputPageId=" + outputPageId + ", dataCreateTime=" + dataCreateTime + ", dataUpdateTime=" + dataUpdateTime + ", dataCreateUser=" + dataCreateUser + ", dataUpdateUser=" + dataUpdateUser + ", dataVersion=" + dataVersion + "]";
	}
}