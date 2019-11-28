package cn.ssy.base.entity.consts;

/**
 * <p>
 * 文件功能说明：
 *       	定义公共常量		
 * </p>
 * 
 * @Author sunshaoyu
 *         <p>
 *         <li>2019年8月15日-下午5:27:30</li>
 *         <li>修改记录</li>
 *         <li>-----------------------------------------------------------</li>
 *         <li>标记：修订内容</li>
 *         <li>2019年8月15日-sunshaoyu：创建注释模板</li>
 *         <li>-----------------------------------------------------------</li>
 *         </p>
 */
public class ApiConst {

	//研发数据源
	public static final String DATASOURCE_YF = "YFDIT";
	//3.0贷款数据源
	public static final String DATASOURCE_ICORE_LN = "ICORE3.0LN";
	//本地数据源
	public static final String DATASOURCE_LOCAL = "LOCAL";
	//KTBPOC数据源
	public static final String DATASOURCE_KTBPOC = "KTBPOC";
	//ATP测试环境
	public static final String DATASOURCE_ATPCORE = "ATPCORE";
	//BAT批量环境
	public static final String DATASOURCE_BATCORE = "BATCORE";
	//贷款研发测试环境
	public static final String DATASOURCE_ICORE_LN_DIT = "ICORE3.0LN_DIT";
	//贷款研发FAT环境
	public static final String DATASOURCE_ICORE_LN_FAT = "ICORE3.0LN_FAT";
	//内管研发测试环境
	public static final String DATASOURCE_ICORE_CT_DIT = "ICORE3.0CT_DIT";
	//本地CBS稽核数据库
	public static final String DATASOURCE_LOCAL_CBS = "LOCALCBS";
	
	//c3p0初始线程数
	public static final int INITIAL_POOL_SIZE = 20;
	//c3p0最大线程数
	public static final int MAX_POOL_SIZE = 1000;
	//超时毫秒
	public static final int CHECK_TIME_OUT = 5000;
	//线程并发数上限
	public static final int MAX_THREAD_CONCURRENT = 1000;
	
	//回调线程默认成功返回
	public static final int CALLABLE_SUCCESS_RETURN = 0;
	//回调线程默认失败返回
	public static final int CALLABLE_ERROR_RETURN = 9999;
	//全量脚本主目录名
	public static final String FULLSQL_MAINDIR_NAME = "ln-dbscripts";
	//字典文件名后缀
	public static final String DICTFILE_SUFFIX = ".d_schema.xml";
	//枚举文件名后缀
	public static final String ENUMFILE_SUFFIX = ".e_schema.xml";
	
	//redis的key:项目文件
	public static final String REDIS_PROJECT_FILE_KEY = "projectFile";
	//redis的key:项目字典
	public static final String REDIS_PROJECT_DICT_KEY = "projectDict";
	//redis的key:项目枚举
	public static final String REDIS_PROJECT_ENUM_KEY = "projectEnum";
	//redis的key:基础引用类型
	public static final String REDIS_PROJECT_BASETYPE_KEY = "projectBaseType";
	//redis的key:内管枚举
	public static final String REDIS_CT_DICT_KEY = "ctDict";
	//redis默认过期时间(一天)
	public static final long REDIS_DEFAULT_TIMEOUT_SEC = 60 * 60 * 24;
}
