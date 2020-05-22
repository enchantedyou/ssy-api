package cn.ssy.base.core.utils.mybatis;

import java.beans.PropertyVetoException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.managed.ManagedTransactionFactory;
import org.apache.log4j.Logger;

import cn.ssy.base.dao.mapper.SppDatasourceMapper;
import cn.ssy.base.entity.config.C3p0Config;
import cn.ssy.base.entity.config.DBConfig;
import cn.ssy.base.entity.consts.ApiConst;
import cn.ssy.base.entity.context.Application;
import cn.ssy.base.entity.mybatis.SppDatasource;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * <p>
 * 文件功能说明：
 *       	Mybatis助手类		
 * </p>
 * 
 * @Author sunshaoyu
 *         <p>
 *         <li>2020年5月21日-下午4:13:18</li>
 *         <li>修改记录</li>
 *         <li>-----------------------------------------------------------</li>
 *         <li>标记：修订内容</li>
 *         <li>2020年5月21日-sunshaoyu：创建注释模板</li>
 *         <li>-----------------------------------------------------------</li>
 *         </p>
 */
public class MybatisHelper {
	
	private final Logger logger = Logger.getLogger(MybatisHelper.class);
	private final Map<String, SqlSessionFactory> dataSouceMap = new HashMap<String, SqlSessionFactory>();
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2020年5月21日-下午4:42:11</li>
	 *         <li>功能说明：获取cp30数据源</li>
	 *         </p>
	 * @param driverClass
	 * @param jdbcUrl
	 * @param user
	 * @param password
	 * @return
	 * @throws PropertyVetoException
	 */
	private ComboPooledDataSource getC3p0DataSource(String driverClass, String jdbcUrl, String user, String password) throws PropertyVetoException{
		//设置连接信息
		ComboPooledDataSource comboPooledDataSource = new ComboPooledDataSource();
		comboPooledDataSource.setDriverClass(driverClass);
		comboPooledDataSource.setJdbcUrl(jdbcUrl);
		comboPooledDataSource.setUser(user);
		comboPooledDataSource.setPassword(password);
		
		//通用配置
		C3p0Config c3p0Config = Application.getContext().getC3p0();
		comboPooledDataSource.setInitialPoolSize(c3p0Config.getInitialPoolSize());
		comboPooledDataSource.setMaxPoolSize(c3p0Config.getMaxPoolSize());
		comboPooledDataSource.setCheckoutTimeout(c3p0Config.getCheckoutTimeout());
		comboPooledDataSource.setAutoCommitOnClose(c3p0Config.isAutoCommitOnClose());
		
		comboPooledDataSource.setPreferredTestQuery(c3p0Config.getPreferredTestQuery());
		comboPooledDataSource.setTestConnectionOnCheckin(c3p0Config.isTestConnectionOnCheckin());
		comboPooledDataSource.setMaxConnectionAge(c3p0Config.getMaxConnectionAge());
		comboPooledDataSource.setMaxIdleTime(c3p0Config.getMaxIdleTime());
		return comboPooledDataSource;
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2020年5月21日-下午5:07:27</li>
	 *         <li>功能说明：获取mybatis的SqlSession</li>
	 *         </p>
	 * @param dataSourceId
	 * @param autoCommit
	 * @return
	 */
	protected SqlSession getSqlSession(String dataSourceId, boolean autoCommit) {
		SqlSessionFactory sqlSessionFactory = dataSouceMap.get(dataSourceId);
		return null == sqlSessionFactory ? null : sqlSessionFactory.openSession(autoCommit);
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2020年5月22日-上午11:18:30</li>
	 *         <li>功能说明：获取mybatis的SqlSession,默认手动提交</li>
	 *         </p>
	 * @param dataSourceId
	 * @return
	 */
	protected SqlSession getSqlSession(String dataSourceId){
		return getSqlSession(dataSourceId, false);
	}
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2020年5月21日-下午5:34:47</li>
	 *         <li>功能说明：构建mybatis的session工厂</li>
	 *         </p>
	 * @param dataSourceId
	 * @param mybatisConf
	 */
	private void buildSqlSessionFactory(String dataSourceId, Configuration mybatisConf) {
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(mybatisConf);
		dataSouceMap.put(dataSourceId, sqlSessionFactory);
	}

	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2020年5月21日-下午5:23:39</li>
	 *         <li>功能说明：初始化mybatis助手</li>
	 *         </p>
	 * @throws PropertyVetoException
	 */
	protected void mybatisHelperInit() throws PropertyVetoException {
		SqlSession localSqlSession = getLocalSqlSession();
		
		try{
			SppDatasourceMapper sppDatasourceMapper = localSqlSession.getMapper(SppDatasourceMapper.class);
			List<SppDatasource> dataSourceList = sppDatasourceMapper.selectAll();
			
			for(SppDatasource ds : dataSourceList){
				Configuration mybatisConf = new Configuration();
				mybatisConfInit(mybatisConf);
				
				mybatisConf.setEnvironment(new Environment(ds.getDatasourceId(), new ManagedTransactionFactory(),
						getC3p0DataSource(ds.getDatasourceDriver(), ds.getDatasourceUrl(), ds.getDatasourceUser(), ds.getDatasourcePwd())));
				buildSqlSessionFactory(ds.getDatasourceId(), mybatisConf);
				logger.info("Loading dynamic data source:" + ds.getDatasourceId());
			}
		}finally{
			close(localSqlSession);
		}
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2020年5月21日-下午5:39:14</li>
	 *         <li>功能说明：关闭SqlSession</li>
	 *         </p>
	 * @param sqlSession
	 */
	public void close(SqlSession sqlSession){
		sqlSession.flushStatements();
		sqlSession.close();
		logger.info("Sql session " + sqlSession.toString() + "] closed successfully");
	}
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2020年5月21日-下午5:09:52</li>
	 *         <li>功能说明：获取本地数据源的SqlSession</li>
	 *         </p>
	 * @return
	 * @throws PropertyVetoException
	 */
	private SqlSession getLocalSqlSession() throws PropertyVetoException {
		Configuration mybatisConf = new Configuration();
		String localDataSourceId = ApiConst.DATASOURCE_LOCAL;
		DBConfig localDbConf = Application.getContext().getDatabase();
		mybatisConfInit(mybatisConf);
		
		mybatisConf.setEnvironment(new Environment(localDataSourceId, new ManagedTransactionFactory(),
				getC3p0DataSource(localDbConf.getDriverClass(), localDbConf.getJdbcUrl(), localDbConf.getUser(), localDbConf.getPassword())));
		buildSqlSessionFactory(localDataSourceId, mybatisConf);
		return getSqlSession(localDataSourceId);
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2020年5月21日-下午5:40:01</li>
	 *         <li>功能说明：初始化mybatis的公用全局配置</li>
	 *         </p>
	 * @param mybatisConf
	 */
	private void mybatisConfInit(Configuration mybatisConf){
		if(null == mybatisConf){
			throw new IllegalArgumentException("Mybatis global configuration is null");
		}
		mybatisConf.setDefaultExecutorType(ExecutorType.REUSE);
		mybatisConf.addMappers(ApiConst.MYBATIS_MAPPER_PACKAGE);
	}
}
