package cn.ssy.base.entity.plugins;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.PooledDataSource;


/**
 * <p>
 * 文件功能说明：
 *       	动态数据源		
 * </p>
 * 
 * @Author sunshaoyu
 *         <p>
 *         <li>2019年7月19日-下午2:31:43</li>
 *         <li>修改记录</li>
 *         <li>-----------------------------------------------------------</li>
 *         <li>标记：修订内容</li>
 *         <li>2019年7月19日-sunshaoyu：创建注释模板</li>
 *         <li>-----------------------------------------------------------</li>
 *         </p>
 */
public class DynamicDataSource{
	
	private static final Logger logger = Logger.getLogger(DynamicDataSource.class);
	
	private static final Map<String, ComboPooledDataSource> datasoucePoolMap = new HashMap<String, ComboPooledDataSource>();
	
	private static String beforeDatasource;
	
	private static final ThreadLocal<String> dataSourceThreadLoacl = new InheritableThreadLocal<String>();
	
    public static void setDataSourceKey(String dataSourceId){
    	dataSourceThreadLoacl.set(dataSourceId);
    }
    
    public static String getDataSourceKey(){
    	return dataSourceThreadLoacl.get();
    }
    
    public static Connection getConnection() throws SQLException{
		return datasoucePoolMap.get(getDataSourceKey()).getConnection();
    }
    
    public static ComboPooledDataSource getDataSource() {
		return datasoucePoolMap.get(getDataSourceKey());
    }
    
    public static void putDatasourcePool(String dataSourceId,ComboPooledDataSource datasource){
    	datasoucePoolMap.put(dataSourceId, datasource);
    }

	public static String getBeforeDatasourceKey() {
		return beforeDatasource;
	}

	public static void setBeforeDatasource(String beforeDatasource) {
		DynamicDataSource.beforeDatasource = beforeDatasource;
	}
	

	public static void printC3p0PoolStatus(){
		PooledDataSource pds = (PooledDataSource) getDataSource();  
		if(null != pds){  
            try {
            	logger.info("<c3p0数据源状态[共:"+pds.getNumConnectionsDefaultUser()+",忙:"+pds.getNumBusyConnectionsDefaultUser()+",空闲:"+pds.getNumIdleConnectionsDefaultUser()+",未关闭:"+pds.getNumUnclosedOrphanedConnectionsAllUsers()+"]>");
            } catch (SQLException e) {  
            	logger.error("c3p0数据源异常");
            }  
        }
	}
}
