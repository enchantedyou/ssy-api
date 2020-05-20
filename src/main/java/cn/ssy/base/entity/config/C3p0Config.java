package cn.ssy.base.entity.config;

/**
 * <p>
 * 文件功能说明：
 *       	c3p0数据源特殊配置		
 * </p>
 * 
 * @Author sunshaoyu
 *         <p>
 *         <li>2020年5月20日-下午5:25:47</li>
 *         <li>修改记录</li>
 *         <li>-----------------------------------------------------------</li>
 *         <li>标记：修订内容</li>
 *         <li>2020年5月20日-sunshaoyu：创建注释模板</li>
 *         <li>-----------------------------------------------------------</li>
 *         </p>
 */
public class C3p0Config {
	
	private int initialPoolSize;
	private int maxPoolSize;
	private int checkoutTimeout;
	private boolean autoCommitOnClose;
	private String preferredTestQuery;
	private boolean testConnectionOnCheckin;
	private int maxConnectionAge;
	private int maxIdleTime;
	
	public C3p0Config(int initialPoolSize, int maxPoolSize, int checkoutTimeout, boolean autoCommitOnClose, String preferredTestQuery, boolean testConnectionOnCheckin,
			int maxConnectionAge, int maxIdleTime) {
		super();
		this.initialPoolSize = initialPoolSize;
		this.maxPoolSize = maxPoolSize;
		this.checkoutTimeout = checkoutTimeout;
		this.autoCommitOnClose = autoCommitOnClose;
		this.preferredTestQuery = preferredTestQuery;
		this.testConnectionOnCheckin = testConnectionOnCheckin;
		this.maxConnectionAge = maxConnectionAge;
		this.maxIdleTime = maxIdleTime;
	}
	
	public C3p0Config(){
		
	}

	public int getInitialPoolSize() {
		return initialPoolSize;
	}

	public void setInitialPoolSize(int initialPoolSize) {
		this.initialPoolSize = initialPoolSize;
	}

	public int getMaxPoolSize() {
		return maxPoolSize;
	}

	public void setMaxPoolSize(int maxPoolSize) {
		this.maxPoolSize = maxPoolSize;
	}

	public int getCheckoutTimeout() {
		return checkoutTimeout;
	}

	public void setCheckoutTimeout(int checkoutTimeout) {
		this.checkoutTimeout = checkoutTimeout;
	}

	public boolean isAutoCommitOnClose() {
		return autoCommitOnClose;
	}

	public void setAutoCommitOnClose(boolean autoCommitOnClose) {
		this.autoCommitOnClose = autoCommitOnClose;
	}

	public String getPreferredTestQuery() {
		return preferredTestQuery;
	}

	public void setPreferredTestQuery(String preferredTestQuery) {
		this.preferredTestQuery = preferredTestQuery;
	}

	public boolean isTestConnectionOnCheckin() {
		return testConnectionOnCheckin;
	}

	public void setTestConnectionOnCheckin(boolean testConnectionOnCheckin) {
		this.testConnectionOnCheckin = testConnectionOnCheckin;
	}

	public int getMaxConnectionAge() {
		return maxConnectionAge;
	}

	public void setMaxConnectionAge(int maxConnectionAge) {
		this.maxConnectionAge = maxConnectionAge;
	}

	public int getMaxIdleTime() {
		return maxIdleTime;
	}

	public void setMaxIdleTime(int maxIdleTime) {
		this.maxIdleTime = maxIdleTime;
	}

	@Override
	public String toString() {
		return "C3p0Config [initialPoolSize=" + initialPoolSize + ", maxPoolSize=" + maxPoolSize + ", checkoutTimeout=" + checkoutTimeout + ", autoCommitOnClose="
				+ autoCommitOnClose + ", preferredTestQuery=" + preferredTestQuery + ", testConnectionOnCheckin=" + testConnectionOnCheckin + ", maxConnectionAge="
				+ maxConnectionAge + ", maxIdleTime=" + maxIdleTime + "]";
	}
}
