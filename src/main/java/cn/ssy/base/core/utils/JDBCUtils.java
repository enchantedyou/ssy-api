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
import java.util.ArrayList;
import java.util.List;

import cn.ssy.base.dao.factory.MapperFactory;
import org.apache.log4j.Logger;

import cn.ssy.base.entity.config.C3p0Config;
import cn.ssy.base.entity.config.DBConfig;
import cn.ssy.base.entity.consts.ApiConst;
import cn.ssy.base.entity.context.Application;
import cn.ssy.base.entity.mybatis.SppDatasource;
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
			DBConfig dbConfig = Application.getContext().getDatabase();
			
			//装载本地数据源
			SppDatasource localDatasource = new SppDatasource();
			localDatasource.setDatasourceId(ApiConst.DATASOURCE_LOCAL);
			localDatasource.setDatasourceDriver(dbConfig.getDriverClass());
			localDatasource.setDatasourceUrl(dbConfig.getJdbcUrl());
			localDatasource.setDatasourceUser(dbConfig.getUser());
			localDatasource.setDatasourcePwd(dbConfig.getPassword());
			loadDynamicDatasource(localDatasource);
			logger.info("装载本地数据源:" +localDatasource.getDatasourceId());
			
			//装载动态数据源
			List<SppDatasource> dataSourceList = MapperFactory.getSppDatasourceMapper().selectAll();
			for(SppDatasource dataSource : dataSourceList){
				loadDynamicDatasource(dataSource);
				logger.info("装载动态数据源:" + dataSource.getDatasourceId());
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
	 * @throws PropertyVetoException
	 */
	private static void loadDynamicDatasource(SppDatasource datasource) throws PropertyVetoException{
		C3p0Config c3p0Config = Application.getContext().getC3p0();
		
		//实例化c3p0数据源
		ComboPooledDataSource c3p0Datasource = new ComboPooledDataSource();
		//装载本地数据源
		c3p0Datasource.setDriverClass(datasource.getDatasourceDriver());
		c3p0Datasource.setJdbcUrl(datasource.getDatasourceUrl());
		c3p0Datasource.setUser(datasource.getDatasourceUser());
		c3p0Datasource.setPassword(datasource.getDatasourcePwd());
		
		//设置c3p0特殊属性
		c3p0Datasource.setInitialPoolSize(c3p0Config.getInitialPoolSize());
		c3p0Datasource.setMaxPoolSize(c3p0Config.getMaxPoolSize());
		c3p0Datasource.setCheckoutTimeout(c3p0Config.getCheckoutTimeout());
		c3p0Datasource.setAutoCommitOnClose(c3p0Config.isAutoCommitOnClose());
		
		c3p0Datasource.setPreferredTestQuery(c3p0Config.getPreferredTestQuery());
		c3p0Datasource.setTestConnectionOnCheckin(c3p0Config.isTestConnectionOnCheckin());
		//c3p0连接回收配置
		c3p0Datasource.setMaxConnectionAge(c3p0Config.getMaxConnectionAge());
		c3p0Datasource.setMaxIdleTime(c3p0Config.getMaxIdleTime());
		//存储数据源
		DynamicDataSource.putDatasourcePool(c3p0Datasource, datasource);
	}
	
	
	/**
	 * 获取与数据库之间的连接
	 * 
	 * @return
	 * @throws SQLException 
	 */
	private static Connection getConnection(String datasourceId) throws SQLException {
		if(CommonUtil.compare(datasourceId, DynamicDataSource.getBeforeDatasourceKey()) != 0){
			DynamicDataSource.setDataSourceKey(datasourceId);
			String curDatasource = DynamicDataSource.getDataSourceKey();
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
	public static ResultSet executeQuery(String sql,String datasourceId) throws SQLException {
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
	public static ResultSet executeQuery(String sql, String[] parameter,String datasourceId) throws SQLException {
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
	public static int executeUpdate(String sql, String[] parameter,String datasourceId) throws SQLException {
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
			if(ct != null && !ct.getAutoCommit()){
				ct.rollback();
			}
			throw new SQLException(e);
		} finally{
			close();
		}
		return 0;
	}
	
	
	
	/**
	 * 含参sql语句的更新方法,含增丶删丶改
	 * 
	 * @param parameter
	 * @return 返回是否成功的真假值
	 * @throws SQLException 
	 */
	public static int executeUpdate(List<String> sqlList, String[] parameter,String datasourceId) throws SQLException {
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
			if(ct != null && !ct.getAutoCommit()){
				ct.rollback();
			}
			throw new SQLException(e);
		} finally{
			close();
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
	public static int executeUpdate(List<String> sqlList,String datasourceId) throws SQLException {
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
			if(ct != null && !ct.getAutoCommit()){
				ct.rollback();
			}
			throw new SQLException(e);
		} finally{
			close();
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
	public static int executeUpdate(String sql, String datasourceId) throws SQLException {
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
			if(ct != null && !ct.getAutoCommit()){
				ct.rollback();
			}
			throw new SQLException(e);
		} finally{
			close();
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
	public static int executeUpdate(String[] sql, String[][] parameter,String datasourceId) throws SQLException {
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
			if(ct != null && !ct.getAutoCommit()){
				//回滚
				ct.rollback();
			}
			throw new SQLException(e);
		} finally{
			close();
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
	public static void close(){
		close(res, ps, ct);
	}

	
	/**
	 * 关闭资源
	 * 
	 * @param res
	 * @param ps
	 * @param ct
	 */
	public synchronized static void close(ResultSet res, PreparedStatement ps, Connection ct) {
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
	public static void close(ResultSet res) {
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
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2020年4月15日-下午7:14:20</li>
	 *         <li>功能说明：获取表的主键名列表</li>
	 *         </p>
	 * @param dataSource	数据源
	 * @param tableName	表名
	 * @return
	 * @throws SQLException
	 */
	public static List<String> getPrimaryKeyList(String dataSource, String tableName) throws SQLException {
		res = getConnection(dataSource).getMetaData().getPrimaryKeys(null, null, tableName);
		List<String> pkList = new ArrayList<String>();
		try{
			while(res.next()){
				pkList.add(res.getString("COLUMN_NAME"));
			}
		}catch(SQLException e){
			CommonUtil.printLogError(e, logger);
		}finally{
			close();
		}
		return pkList;
	}
}
