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
	
	DP("dp","Dp",31,"存款","Deposit","100","1001"),
	
	CO("co","Co",33,"押品","Collateral","102","1021"),
	
	LN("ln","Ln",32,"贷款","Loan","102","1021"),
	
	CL("cl","Cl",59,"额度","Credit limit","204","2041"),
	
	CC("cc","Cc",34,"类信用卡","Credit Card","105","1051"),
	
	CM("cm","Cm",35,"公共","Common","103","1031"),
	
	GL("gl","Gl",38,"总账","General ledger","104","1041"),
	
	MK("mk","MK",50,"营销","Marketing","203","2031"),
	
	PF("pf","PF",56,"产品工厂","Product factory","203","2031");
	
	private String id;
	
	private String upperId;
	
	private Integer srvSign;
	
	private String moduleName;
	
	private String moduleDesc;
	
	private String sysCode;
	
	private String subSysCode;

	E_ICOREMODULE(String id, String upperId, Integer srvSign, String moduleName, String moduleDesc,String sysCode,String subSysCode) {
		this.id = id;
		this.upperId = upperId;
		this.srvSign = srvSign;
		this.moduleName = moduleName;
		
		this.moduleDesc = moduleDesc;
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
