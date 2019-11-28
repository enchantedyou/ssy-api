package cn.ssy.base.core.utils;

/**
 * mysqlJDBC工具类修改版
 * 修改时间:2017/7/25
 * @author 孙绍禹
 *
 */
import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import cn.ssy.base.entity.consts.ApiConst;
import cn.ssy.base.entity.plugins.DynamicDataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;


public class JDBCUtils {
	
	private static Connection ct = null; // 数据库连接对象
	private static PreparedStatement ps = null; // 数据库语句对象
	private static ResultSet res = null; // 结果集

	//log4j日志
	private static final Logger logger = Logger.getLogger(JDBCUtils.class);
	
	static {
		jdbcInitialize();
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月7日-上午9:48:20</li>
	 *         <li>功能说明：jdbc初始化</li>
	 *         </p>
	 */
	public static void jdbcInitialize(){
		try {
			//加载数据源配置
			Map<String, String> configMap = CommonUtil.readPropertiesSettings(JDBCUtils.class.getClassLoader().getResource("db.properties").getPath());
			
			//装载本地数据源
			loadDynamicDatasource(ApiConst.DATASOURCE_LOCAL, configMap.get("driverClass"), configMap.get("jdbcUrl"), configMap.get("user"), configMap.get("password"));
			logger.info("本地装载数据源:" + ApiConst.DATASOURCE_LOCAL);
			
			//装载动态数据源
			ResultSet resultSet = executeQuery("select * from spp_datasource", ApiConst.DATASOURCE_LOCAL);
			while(resultSet.next()){
				String datasourceId = resultSet.getString("datasource_id");
				String driverClass = resultSet.getString("datasource_driver");
				String jdbcUrl = resultSet.getString("datasource_url");
				String userName = resultSet.getString("datasource_user");
				
				String password = resultSet.getString("datasource_pwd");
				loadDynamicDatasource(datasourceId, driverClass, jdbcUrl, userName, password);
				logger.info("装载动态数据源:" + datasourceId);
			}
		}catch(Exception e){ 
			CommonUtil.printLogError(e, logger);
			return;
		}
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月15日-下午7:52:57</li>
	 *         <li>功能说明：装载动态数据源</li>
	 *         </p>
	 * @param datasourceId
	 * @param driver
	 * @param url
	 * @param user
	 * @param pwd
	 * @throws PropertyVetoException
	 */
	private static void loadDynamicDatasource(String datasourceId,String driver,String url,String user,String pwd) throws PropertyVetoException{
		//实例化c3p0数据源
		ComboPooledDataSource c3p0Datasource = new ComboPooledDataSource();
		//装载本地数据源
		c3p0Datasource.setDriverClass(driver);
		c3p0Datasource.setJdbcUrl(url);
		c3p0Datasource.setUser(user);
		c3p0Datasource.setPassword(pwd);
		
		//设置c3p0特殊属性
		c3p0Datasource.setInitialPoolSize(ApiConst.INITIAL_POOL_SIZE);
		c3p0Datasource.setMaxPoolSize(ApiConst.MAX_POOL_SIZE);
		c3p0Datasource.setCheckoutTimeout(ApiConst.CHECK_TIME_OUT);
		
		//存储数据源
		DynamicDataSource.putDatasourcePool(datasourceId,c3p0Datasource);
	}
	
	
	/**
	 * 获取与数据库之间的连接
	 * 
	 * @return
	 * @throws SQLException 
	 */
	private static synchronized Connection getConnection(String datasourceId) throws SQLException {
		if(CommonUtil.compare(datasourceId, DynamicDataSource.getBeforeDatasource()) != 0){
			DynamicDataSource.setDataSourceKey(datasourceId);
			String curDatasource = DynamicDataSource.getDataSource();
			logger.info("获取数据源连接,当前数据源:"+curDatasource);
			DynamicDataSource.setBeforeDatasource(datasourceId);
			
			if(CommonUtil.isNull(curDatasource)){
				logger.error("获取数据源连接失败");
			}
		}
		ct = DynamicDataSource.getConnection();
		return ct;
	}

	
	/**
	 * 无参sql语句的查询
	 * 
	 * @param sql
	 * @return 返回查询到的结果集
	 * @throws SQLException 
	 */
	public static synchronized ResultSet executeQuery(String sql,String datasourceId) throws SQLException {
		ct = getConnection(datasourceId);
		if(ct != null){
			ps = ct.prepareStatement(sql);
			res = ps.executeQuery();
		}
		return res;
	}

	/**
	 * 重载查询方法: 含参sql语句的查询
	 * 
	 * @param sql
	 * @param parameter
	 * @return 返回查询到的结果集
	 * @throws SQLException 
	 */
	public static synchronized ResultSet executeQuery(String sql, String[] parameter,String datasourceId) throws SQLException {
		ct = getConnection(datasourceId);
		if(ct != null){
			ps = ct.prepareStatement(sql);
			if (null != parameter) {
				for (int i = 0; i < parameter.length; i++) {
					ps.setString(i + 1, parameter[i]);
				}
			}
			res = ps.executeQuery();
		}
		return res;
	}

	/**
	 * 含参sql语句的更新方法,含增丶删丶改
	 * 
	 * @param sql
	 * @param parameter
	 * @return 返回是否成功的真假值
	 * @throws SQLException 
	 */
	public static synchronized int executeUpdate(String sql, String[] parameter,String datasourceId) throws SQLException {
		try {
			ct = getConnection(datasourceId);
			if(ct != null){
				ps = ct.prepareStatement(sql);
				ct.setAutoCommit(false);// 取消自动提交
				
				if (null != parameter) {
					for (int i = 0; i < parameter.length; i++) {
						ps.setString(i + 1, parameter[i]);
					}
				}
				int effectNum = ps.executeUpdate();
				ct.commit();
				return effectNum;
			}
		} catch (Exception e) {
			//回滚
			if(ct != null){
				ct.rollback();
			}
			throw new SQLException(e);
		}
		return 0;
	}
	
	
	
	/**
	 * 含参sql语句的更新方法,含增丶删丶改
	 * 
	 * @param sql
	 * @param parameter
	 * @return 返回是否成功的真假值
	 * @throws SQLException 
	 */
	public static synchronized int executeUpdate(List<String> sqlList, String[] parameter,String datasourceId) throws SQLException {
		try {
			ct = getConnection(datasourceId);
			if(ct != null && sqlList != null){
				ct.setAutoCommit(false);// 取消自动提交
				int effectNum = 0;
				
				for(String sql : sqlList){
					ps = ct.prepareStatement(sql);
					if (null != parameter) {
						for (int i = 0; i < parameter.length; i++) {
							ps.setString(i + 1, parameter[i]);
						}
					}
					effectNum += ps.executeUpdate();
				}
				ct.commit();
				return effectNum;
			}
		} catch (Exception e) {
			//回滚
			if(ct != null){
				ct.rollback();
			}
			throw new SQLException(e);
		}
		return 0;
	}

	
	/**
	 * 批量更新: 无参sql语句的更新方法,含增丶删丶改,入参为SQL列表
	 * 
	 * @param sqlList
	 * @return 返回是否成功的真假值
	 * @throws SQLException 
	 */
	public static synchronized int executeUpdate(List<String> sqlList,String datasourceId) throws SQLException {
		try {
			ct = getConnection(datasourceId);
			if(ct != null && sqlList != null){
				ct.setAutoCommit(false);
				int effectNum = 0;
				
				for(String sql : sqlList){
					ps = ct.prepareStatement(sql);
					effectNum += ps.executeUpdate();
				}
				ct.commit();
				return effectNum;
			}
		} catch (Exception e) {
			//回滚
			if(ct != null){
				ct.rollback();
			}
			throw new SQLException(e);
		}
		return 0;
	}
	
	
	/**
	 * 重载更新方法: 无参sql语句的更新方法,含增丶删丶改
	 * 
	 * @param sql
	 * @return 返回是否成功的真假值
	 * @throws SQLException 
	 */
	public static synchronized int executeUpdate(String sql, String datasourceId) throws SQLException {
		try {
			ct = getConnection(datasourceId);
			if(ct != null){
				ct.setAutoCommit(false);
				int effectNum = 0;
				ps = ct.prepareStatement(sql);
				effectNum = ps.executeUpdate();
				
				ct.commit();
				return effectNum;
			}
		} catch (SQLException e) {
			//回滚
			if(ct != null){
				ct.rollback();
			}
			throw new SQLException(e);
		}
		return 0;
	}

	/**
	 * 重载更新方法: 多条含参sql语句的更新方法,含增丶删丶改
	 * 
	 * @param sql
	 * @param parameter
	 * @return 返回是否成功的真假值
	 * @throws SQLException 
	 */
	public static synchronized int executeUpdate(String[] sql, String[][] parameter,String datasourceId) throws SQLException {
		try {
			ct = getConnection(datasourceId);
			if(ct != null){
				ct.setAutoCommit(false);// 取消自动提交
				int effectNum = 0;
				for (int i = 0; i < sql.length; i++) {
					if (null != parameter) {
						ps = ct.prepareStatement(sql[i]);
						for (int j = 0; j < parameter.length; j++) {
							ps.setString(j + 1, parameter[i][j]);
						}
						effectNum += ps.executeUpdate();
					}
				}
				ct.commit();// 提交事务
				return effectNum;
			}
		} catch (SQLException e) {
			if(ct != null){
				//回滚
				ct.rollback();
			}
			throw new SQLException(e);
		}
		return 0;
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年10月18日-上午10:05:13</li>
	 *         <li>功能说明：关闭资源</li>
	 *         </p>
	 */
	public static synchronized void close(){
		close(res, ps, ct);
	}

	
	/**
	 * 关闭资源
	 * 
	 * @param res
	 * @param ps
	 * @param ct
	 */
	public static synchronized void close(ResultSet res, PreparedStatement ps,
			Connection ct) {
		if (res != null) {
			try {
				res.close();
			} catch (SQLException e) {
				CommonUtil.printLogError(e, logger);
			}
			res = null;// 将res置为空,使用垃圾回收机制回收
		}
		if (ps != null) {
			try {
				ps.close();
			} catch (SQLException e) {
				CommonUtil.printLogError(e, logger);
			}
			ps = null;
		}
		if (ct != null) {
			try {
				ct.close();
			} catch (SQLException e) {
				CommonUtil.printLogError(e, logger);
			}
			ct = null;
		}
	}

	/**
	 * 重载关闭资源的方法: 此方法适用于外部查询后关闭结果集,同时也会关闭Connection对象和PreparedStatement对象
	 * 
	 * @param res
	 */
	public static synchronized void close(ResultSet res) {
		if (res != null) {
			try {
				res.close();
			} catch (SQLException e) {
				CommonUtil.printLogError(e, logger);
			}
			res = null;// 将res置为空,使用垃圾回收机制回收
		}
		if (ps != null) {
			try {
				ps.close();
			} catch (SQLException e) {
				CommonUtil.printLogError(e, logger);
			}
			ps = null;
		}
		if (ct != null) {
			try {
				ct.close();
			} catch (SQLException e) {
				CommonUtil.printLogError(e, logger);
			}
			ct = null;
		}
	}
}
