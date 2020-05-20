package cn.ssy.base.entity.config;

/**
 * <p>
 * 文件功能说明：
 *       	Redis缓存配置		
 * </p>
 * 
 * @Author sunshaoyu
 *         <p>
 *         <li>2020年5月20日-下午3:10:14</li>
 *         <li>修改记录</li>
 *         <li>-----------------------------------------------------------</li>
 *         <li>标记：修订内容</li>
 *         <li>2020年5月20日-sunshaoyu：创建注释模板</li>
 *         <li>-----------------------------------------------------------</li>
 *         </p>
 */
public class RedisConfig {
	private int maxIdle;
	private int minIdle;
	private long maxWaitMillis;
	private boolean testOnBorrow;
	private int maxTotal;
	private String host;
	private String password;
	private int port;
	private int connectionTimeout;
	
	public RedisConfig(int maxIdle, int minIdle, long maxWaitMillis, boolean testOnBorrow, int maxTotal, String host, String password, int port, int connectionTimeout) {
		super();
		this.maxIdle = maxIdle;
		this.minIdle = minIdle;
		this.maxWaitMillis = maxWaitMillis;
		this.testOnBorrow = testOnBorrow;
		this.maxTotal = maxTotal;
		this.host = host;
		this.password = password;
		this.port = port;
		this.connectionTimeout = connectionTimeout;
	}
	
	public RedisConfig(){
		
	}

	public int getMaxIdle() {
		return maxIdle;
	}

	public void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
	}

	public int getMinIdle() {
		return minIdle;
	}

	public void setMinIdle(int minIdle) {
		this.minIdle = minIdle;
	}

	public long getMaxWaitMillis() {
		return maxWaitMillis;
	}

	public void setMaxWaitMillis(long maxWaitMillis) {
		this.maxWaitMillis = maxWaitMillis;
	}

	public boolean isTestOnBorrow() {
		return testOnBorrow;
	}

	public void setTestOnBorrow(boolean testOnBorrow) {
		this.testOnBorrow = testOnBorrow;
	}

	public int getMaxTotal() {
		return maxTotal;
	}

	public void setMaxTotal(int maxTotal) {
		this.maxTotal = maxTotal;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getConnectionTimeout() {
		return connectionTimeout;
	}

	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	@Override
	public String toString() {
		return "RedisConfig [maxIdle=" + maxIdle + ", minIdle=" + minIdle + ", maxWaitMillis=" + maxWaitMillis + ", testOnBorrow=" + testOnBorrow + ", maxTotal=" + maxTotal
				+ ", host=" + host + ", password=" + password + ", port=" + port + ", connectionTimeout=" + connectionTimeout + "]";
	}
}
