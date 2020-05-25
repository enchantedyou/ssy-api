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
	private final static Map<String, SqlSessionFactory> sqlSessionFactoryMap = new HashMap<String, SqlSessionFactory>();
	private final static Map<String, SppDatasource> datsSourceMap = new HashMap<String, SppDatasource>();
	private static boolean isLoaded = false;
	
	/**
	 * 是否一次性加载所有的数据源:
	 * <li>为true的时候一次性加载所有数据源并缓存session工厂</li>
	 * <li>为false的时候在有需要的时候才加载数据源并缓存session工厂</li>
	 */
	private boolean isLoadAllAtOnce;
	
	public MybatisHelper(boolean isLoadAllAtOnce) {
		this.isLoadAllAtOnce = isLoadAllAtOnce;
		
		if(!isLoaded){
			mybatisHelperInit();
			isLoaded = true;
		}
	}
	
	public MybatisHelper(){
		this.isLoadAllAtOnce = false;

		if(!isLoaded){
			mybatisHelperInit();
			isLoaded = true;
		}
	}
	
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
	 * @throws PropertyVetoException 
	 */
	protected SqlSession getSqlSession(String dataSourceId, boolean autoCommit) throws PropertyVetoException {
		SqlSessionFactory sqlSessionFactory = sqlSessionFactoryMap.get(dataSourceId);
		if(null == sqlSessionFactory){
			SppDatasource sppDatasource = datsSourceMap.get(dataSourceId);
			if(null == sppDatasource){
				loadSppDatasource();
			}
			loadSingleDataSource(datsSourceMap.get(dataSourceId));
			sqlSessionFactory = sqlSessionFactoryMap.get(dataSourceId);
		}
		
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
	 * @throws PropertyVetoException 
	 */
	protected SqlSession getSqlSession(String dataSourceId) throws PropertyVetoException{
		return getSqlSession(dataSourceId, true);
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
		sqlSessionFactoryMap.put(dataSourceId, sqlSessionFactory);
	}
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2020年5月21日-下午5:23:39</li>
	 *         <li>功能说明：初始化mybatis助手</li>
	 *         </p>
	 * @throws PropertyVetoException
	 */
	private void mybatisHelperInit() {
		logger.info("Start to initialize mybatis plugin");
		try{
			if(isLoadAllAtOnce){
				List<SppDatasource> dataSourceList = loadSppDatasource();
				for(SppDatasource ds : dataSourceList) {
					loadSingleDataSource(ds);
				}
			}else{
				logger.info("It is set to not be loaded at once, so the dynamic data source is loaded when the sql session is acquired");
			}
		}catch(PropertyVetoException e){
			throw new RuntimeException(e);
		}
	}


	/**
	 * 加载动态数据源
	 * @return
	 * @throws PropertyVetoException
	 */
	private List<SppDatasource> loadSppDatasource() throws PropertyVetoException {
		SqlSession localSqlSession = null;
		try{
			localSqlSession = getLocalSqlSession();
			SppDatasourceMapper sppDatasourceMapper = localSqlSession.getMapper(SppDatasourceMapper.class);
			List<SppDatasource> datasourceList = sppDatasourceMapper.selectAll();

			for(SppDatasource ds : datasourceList){
				datsSourceMap.put(ds.getDatasourceId(), ds);
			}
			return datasourceList;
		}finally {
			close(localSqlSession);
		}
	}


	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2020年5月22日-下午12:11:00</li>
	 *         <li>功能说明：加载单个数据源</li>
	 *         </p>
	 * @param ds
	 * @throws PropertyVetoException
	 */
	private void loadSingleDataSource(SppDatasource ds) throws PropertyVetoException {
		Configuration mybatisConf = new Configuration();
		mybatisConfInit(mybatisConf);
		
		mybatisConf.setEnvironment(new Environment(ds.getDatasourceId(), new ManagedTransactionFactory(),
				getC3p0DataSource(ds.getDatasourceDriver(), ds.getDatasourceUrl(), ds.getDatasourceUser(), ds.getDatasourcePwd())));
		buildSqlSessionFactory(ds.getDatasourceId(), mybatisConf);
		logger.info("Loading dynamic data source:" + ds.getDatasourceId());
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
		if(null != sqlSession){
			sqlSession.flushStatements();
			sqlSession.close();
			logger.info("Close sql session:" + sqlSession);
		}
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
