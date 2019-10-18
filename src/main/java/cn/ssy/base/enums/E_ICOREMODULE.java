package cn.ssy.base.enums;


/**
 * <p>
 * 文件功能说明：
 *       	核心服务模块枚举		
 * </p>
 * 
 * @Author sunshaoyu
 *         <p>
 *         <li>2019年8月3日-上午10:53:06</li>
 *         <li>修改记录</li>
 *         <li>-----------------------------------------------------------</li>
 *         <li>标记：修订内容</li>
 *         <li>2019年8月3日-sunshaoyu：创建注释模板</li>
 *         <li>-----------------------------------------------------------</li>
 *         </p>
 */
public enum E_ICOREMODULE {
	
	DP("dp","Dp",31,"存款","Deposit",false,null,E_PACKAGETYPE.ALL,"100","1001"),
	
	CO("co","Co",33,"押品","Collateral",false,null,E_PACKAGETYPE.SERV,"102","1021"),
	
	LN("ln","Ln",32,"贷款","Loan",true,new E_ICOREMODULE[]{E_ICOREMODULE.CO},E_PACKAGETYPE.ALL,"102","1021"),
	
	CC("cc","Cc",34,"类信用卡","Credit Card",false,null,E_PACKAGETYPE.ALL,"105","1051"),
	
	CM("cm","Cm",35,"公共","Common",false,null,E_PACKAGETYPE.ALL,"103","1031"),
	
	CB("cb","Cb",36,"临柜","Cabinet",false,null,E_PACKAGETYPE.ALL,"",""),
	
	AC("ac","Ac",37,"核算","Accounting",false,null,E_PACKAGETYPE.ALL,"",""),
	
	GL("gl","Gl",38,"总账","General ledger",false,null,E_PACKAGETYPE.ALL,"104","1041"),
	
	MK("mk","MK",50,"营销","Marketing",false,null,E_PACKAGETYPE.ALL,"203","2031");
	
	private String id;
	
	private String upperId;
	
	private Integer srvSign;
	
	private String moduleName;
	
	private String moduleDesc;
	
	private boolean isMultiModule;
	
	private E_ICOREMODULE[] attachModule;
	
	private E_PACKAGETYPE packageType;
	
	private String sysCode;
	
	private String subSysCode;

	E_ICOREMODULE(String id, String upperId, Integer srvSign, String moduleName, String moduleDesc,
		boolean isMultiModule,E_ICOREMODULE[] attachModule,E_PACKAGETYPE packageType,String sysCode,String subSysCode) {
		this.id = id;
		this.upperId = upperId;
		this.srvSign = srvSign;
		this.moduleName = moduleName;
		
		this.moduleDesc = moduleDesc;
		this.isMultiModule = isMultiModule;
		this.attachModule = attachModule;
		this.packageType = packageType;
		
		this.sysCode = sysCode;
		this.subSysCode = subSysCode;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getSrvSign() {
		return srvSign;
	}

	public void setSrvSign(Integer srvSign) {
		this.srvSign = srvSign;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getModuleDesc() {
		return moduleDesc;
	}

	public void setModuleDesc(String moduleDesc) {
		this.moduleDesc = moduleDesc;
	}

	public String getUpperId() {
		return upperId;
	}

	public void setUpperId(String upperId) {
		this.upperId = upperId;
	}

	public boolean isMultiModule() {
		return isMultiModule;
	}

	public void setMultiModule(boolean isMultiModule) {
		this.isMultiModule = isMultiModule;
	}

	public E_ICOREMODULE[] getAttachModule() {
		return attachModule;
	}

	public void setAttachModule(E_ICOREMODULE[] attachModule) {
		this.attachModule = attachModule;
	}

	public E_PACKAGETYPE getPackageType() {
		return packageType;
	}

	public void setPackageType(E_PACKAGETYPE packageType) {
		this.packageType = packageType;
	}

	public String getSysCode() {
		return sysCode;
	}

	public void setSysCode(String sysCode) {
		this.sysCode = sysCode;
	}

	public String getSubSysCode() {
		return subSysCode;
	}

	public void setSubSysCode(String subSysCode) {
		this.subSysCode = subSysCode;
	}
}
