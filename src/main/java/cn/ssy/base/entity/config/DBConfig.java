package cn.ssy.base.entity.config;

/**
 * <p>
 * 文件功能说明：
 *       	数据源配置		
 * </p>
 * 
 * @Author sunshaoyu
 *         <p>
 *         <li>2020年5月20日-下午3:07:43</li>
 *         <li>修改记录</li>
 *         <li>-----------------------------------------------------------</li>
 *         <li>标记：修订内容</li>
 *         <li>2020年5月20日-sunshaoyu：创建注释模板</li>
 *         <li>-----------------------------------------------------------</li>
 *         </p>
 */
public class DBConfig {
	private String driverClass;
	private String jdbcUrl;
	private String user;
	private String password;
	
	public DBConfig(String driverClass, String jdbcUrl, String user, String password) {
		super();
		this.driverClass = driverClass;
		this.jdbcUrl = jdbcUrl;
		this.user = user;
		this.password = password;
	}
	
	public DBConfig(){
		
	}

	public String getDriverClass() {
		return driverClass;
	}

	public void setDriverClass(String driverClass) {
		this.driverClass = driverClass;
	}

	public String getJdbcUrl() {
		return jdbcUrl;
	}

	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "DBConfig [driverClass=" + driverClass + ", jdbcUrl=" + jdbcUrl + ", user=" + user + ", password=" + password + "]";
	}
}
