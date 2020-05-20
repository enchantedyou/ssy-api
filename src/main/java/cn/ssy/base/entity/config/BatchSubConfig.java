package cn.ssy.base.entity.config;

/**
 * <p>
 * 文件功能说明：
 *       	批量配置		
 * </p>
 * 
 * @Author sunshaoyu
 *         <p>
 *         <li>2020年5月20日-下午3:04:03</li>
 *         <li>修改记录</li>
 *         <li>-----------------------------------------------------------</li>
 *         <li>标记：修订内容</li>
 *         <li>2020年5月20日-sunshaoyu：创建注释模板</li>
 *         <li>-----------------------------------------------------------</li>
 *         </p>
 */
public class BatchSubConfig {
	private String systemCode;
	private String subSystemId;
	private String busiOrgId;
	private String channelId;
	private String trxnBranch;
	private String datasource;
	private String module;
	private String serverIp;
	private long fetchStatusDelay;
	private boolean enable;
	
	public BatchSubConfig(String systemCode, String subSystemId, String busiOrgId, String channelId, String trxnBranch, String datasource, String module, String serverIp,
			long fetchStatusDelay,boolean enable) {
		super();
		this.systemCode = systemCode;
		this.subSystemId = subSystemId;
		this.busiOrgId = busiOrgId;
		this.channelId = channelId;
		this.trxnBranch = trxnBranch;
		this.datasource = datasource;
		this.module = module;
		this.serverIp = serverIp;
		this.fetchStatusDelay = fetchStatusDelay;
		this.enable = enable;
	}
	
	public BatchSubConfig(){
		
	}
	
	public String getSystemCode() {
		return systemCode;
	}

	public void setSystemCode(String systemCode) {
		this.systemCode = systemCode;
	}

	public String getSubSystemId() {
		return subSystemId;
	}

	public void setSubSystemId(String subSystemId) {
		this.subSystemId = subSystemId;
	}

	public String getBusiOrgId() {
		return busiOrgId;
	}

	public void setBusiOrgId(String busiOrgId) {
		this.busiOrgId = busiOrgId;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getTrxnBranch() {
		return trxnBranch;
	}

	public void setTrxnBranch(String trxnBranch) {
		this.trxnBranch = trxnBranch;
	}

	public String getDatasource() {
		return datasource;
	}

	public void setDatasource(String datasource) {
		this.datasource = datasource;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getServerIp() {
		return serverIp;
	}

	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}

	public long getFetchStatusDelay() {
		return fetchStatusDelay;
	}

	public void setFetchStatusDelay(long fetchStatusDelay) {
		this.fetchStatusDelay = fetchStatusDelay;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	@Override
	public String toString() {
		return "BatchSubConfig [systemCode=" + systemCode + ", subSystemId=" + subSystemId + ", busiOrgId=" + busiOrgId + ", channelId=" + channelId + ", trxnBranch=" + trxnBranch
				+ ", datasource=" + datasource + ", module=" + module + ", serverIp=" + serverIp + ", fetchStatusDelay=" + fetchStatusDelay + ", enable=" + enable + "]";
	}
}
