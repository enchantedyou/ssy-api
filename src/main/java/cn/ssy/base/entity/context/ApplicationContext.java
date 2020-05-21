package cn.ssy.base.entity.context;

import cn.ssy.base.entity.config.BatchConfig;
import cn.ssy.base.entity.config.C3p0Config;
import cn.ssy.base.entity.config.DBConfig;
import cn.ssy.base.entity.config.RedisConfig;


/**
 * <p>
 * 文件功能说明：
 *       	应用上下文,全局配置		
 * </p>
 * 
 * @Author sunshaoyu
 *         <p>
 *         <li>2020年5月20日-下午3:03:26</li>
 *         <li>修改记录</li>
 *         <li>-----------------------------------------------------------</li>
 *         <li>标记：修订内容</li>
 *         <li>2020年5月20日-sunshaoyu：创建注释模板</li>
 *         <li>-----------------------------------------------------------</li>
 *         </p>
 */
public class ApplicationContext {

	private BatchConfig batch;
	private DBConfig database;
	private RedisConfig redis;
	private C3p0Config c3p0;
	
	public ApplicationContext(BatchConfig batch, DBConfig database, RedisConfig redis, C3p0Config c3p0) {
		super();
		this.batch = batch;
		this.database = database;
		this.redis = redis;
		this.c3p0 = c3p0;
	}

	public ApplicationContext(){
		
	}

	public BatchConfig getBatch() {
		return batch;
	}

	public void setBatch(BatchConfig batch) {
		this.batch = batch;
	}

	public DBConfig getDatabase() {
		return database;
	}

	public void setDatabase(DBConfig database) {
		this.database = database;
	}

	public RedisConfig getRedis() {
		return redis;
	}

	public void setRedis(RedisConfig redis) {
		this.redis = redis;
	}
	
	public C3p0Config getC3p0() {
		return c3p0;
	}

	public void setC3p0(C3p0Config c3p0) {
		this.c3p0 = c3p0;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("批量配置:").append(batch).append("\r\n");
		buffer.append("数据源配置:").append(database).append("\r\n");
		buffer.append("Redis缓存配置:").append(redis).append("\r\n");
		buffer.append("c3p0数据源配置:").append(c3p0).append("\r\n");
		return buffer.toString();
	}
}