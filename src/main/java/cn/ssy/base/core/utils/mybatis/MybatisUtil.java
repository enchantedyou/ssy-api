package cn.ssy.base.core.utils.mybatis;

import java.beans.PropertyVetoException;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import cn.ssy.base.entity.consts.ApiConst;

/**
 * <p>
 * 文件功能说明：
 *       	Mybatis工具类		
 * </p>
 * 
 * @Author sunshaoyu
 *         <p>
 *         <li>2020年5月21日-下午8:26:39</li>
 *         <li>修改记录</li>
 *         <li>-----------------------------------------------------------</li>
 *         <li>标记：修订内容</li>
 *         <li>2020年5月21日-sunshaoyu：创建注释模板</li>
 *         <li>-----------------------------------------------------------</li>
 *         </p>
 */
public class MybatisUtil extends MybatisHelper{
	
	private final Logger logger = Logger.getLogger(MybatisUtil.class);
	private static boolean isLoad = false;
	private SqlSession currentSqlSession;
	private Map<Integer, SqlSession> sqlSessionMap = new HashMap<Integer, SqlSession>();//<hashCode,SqlSession>

	public MybatisUtil() {
		super();
		if(!isLoad){
			logger.info("Start to initialize mybatis plugin");
			try {
				mybatisHelperInit();
			}
			catch (PropertyVetoException e) {
				throw new RuntimeException(e);
			}
			isLoad = true;
		}
	}

	public void execute(String dataSourceId, MybatisHelperCallback callbackImpl){
		final SqlSession sqlSession = getSqlSession(dataSourceId);
		putCurrentSqlSession(sqlSession);
		try{
			callbackImpl.call(sqlSession);
		}finally{
			close(sqlSession);
		}
	}
	
	public void execute(MybatisHelperCallback callbackImpl){
		execute(ApiConst.DATASOURCE_LOCAL, callbackImpl);
	}
	
	public <T> T getMapper(String dataSourceId, Class<T> mapperClass){
		SqlSession sqlSession = getSqlSession(dataSourceId);
		if(null == sqlSession){
			throw new RuntimeException("Unable to get sql session which data source id is " + dataSourceId);
		}
		putCurrentSqlSession(sqlSession);
		return sqlSession.getMapper(mapperClass);
	}
	
	private void putCurrentSqlSession(SqlSession sqlSession){
		currentSqlSession = sqlSession;
		sqlSessionMap.put(currentSqlSession.hashCode(), currentSqlSession);
	}
	
	private void checkCurrentSqlSession(){
		checkCurrentSqlSession(currentSqlSession);
	}
	
	private void checkCurrentSqlSession(SqlSession sqlSession){
		if(null == currentSqlSession){
			throw new RuntimeException("There is no available sql session");
		}
	}
	
	public void close() {
		checkCurrentSqlSession();
		super.close(currentSqlSession);
	}
	
	public SqlSession getCurrentSqlSession(){
		checkCurrentSqlSession();
		return currentSqlSession;
	}
	
	public void commit(){
		for(int hashCode : sqlSessionMap.keySet()){
			SqlSession sqlSession = sqlSessionMap.get(hashCode);
			checkCurrentSqlSession(sqlSession);
			sqlSession.commit();
			
			logger.info("Sql session " + sqlSession + "has committed successfully");
			close(sqlSession);
			sqlSessionMap.remove(sqlSession);
		}
	}
	
	public void rollback(){
		for(int hashCode : sqlSessionMap.keySet()){
			SqlSession sqlSession = sqlSessionMap.get(hashCode);
			checkCurrentSqlSession(sqlSession);
			sqlSession.rollback();
			
			logger.info("Sql session " + sqlSession + "has committed successfully");
			close(sqlSession);
			sqlSessionMap.remove(sqlSession);
		}
	}
}
