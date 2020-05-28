package cn.ssy.base.core.utils.mybatis;

import cn.ssy.base.core.utils.CommonUtil;
import cn.ssy.base.entity.consts.ApiConst;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import java.beans.PropertyVetoException;
import java.util.HashMap;
import java.util.Map;

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
	private SqlSession currentSqlSession;

	public MybatisUtil(boolean isLoadAllAtOnce) {
		super(isLoadAllAtOnce);
	}
	
	public MybatisUtil(){
		super();
	}

	public <T> T getMapper(String dataSourceId, Class<T> mapperClass){
		SqlSession sqlSession;
		try{
			sqlSession = getSqlSession(dataSourceId);
			putCurrentSqlSession(sqlSession, mapperClass);

			//关闭当前session
			/*if(currentSqlSession != null && sqlSession != currentSqlSession){
				close();
			}*/
			if(null == sqlSession){
				throw new RuntimeException("Unable to get sql session which data source id is " + dataSourceId);
			}
		}catch(PropertyVetoException e){
			throw new RuntimeException(e);
		}
		return sqlSession.getMapper(mapperClass);
	}

	public <T> T getLocalMapper(Class<T> mapperClass){
		return getMapper(ApiConst.DATASOURCE_LOCAL, mapperClass);
	}
	
	private void putCurrentSqlSession(SqlSession sqlSession, Class<?> mapperClass){
		currentSqlSession = sqlSession;
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
		checkCurrentSqlSession(currentSqlSession);
		currentSqlSession.commit();
	}
	
	public void rollback(){
		checkCurrentSqlSession(currentSqlSession);
		currentSqlSession.rollback();
	}
}
