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
	//额度开发环境
	public static final String DATASOURCE_CL = "ICORE3.0CL";
	//营销中心开发环境
	public static final String DATASOURCE_ICORE_MK = "ICORE3.0MK";
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
	//公共研发FAT还款
	public static final String DATASOURCE_ICORE_CM_FAT = "ICORE3.0CM_FAT";
	//内管研发测试环境
	public static final String DATASOURCE_ICORE_CT_DIT = "ICORE3.0CT_DIT";
	//内管研发FAT环境
	public static final String DATASOURCE_ICORE_CT_FAT = "ICORE3.0CT_FAT";
	//中台营销FAT环境
	public static final String DATASOURCE_ICORE_MK_FAT = "ICORE3.0MK_FAT";
	//3.1集中式数据库
	public static final String DATASOURCE_ICORE_CBS = "ICORE3.0CBS";
	//3.1贷款日终批量环境
	public static final String DATASOURCE_ICORE_LN_BAT = "ICORE3.0LN_BAT";
	//3.1存款开发环境
	public static final String DATASOURCE_ICORE_DP = "ICORE3.0DP";
	//3.1存款日终
	public static final String DATASOURCE_ICORE_DP_BAT = "ICORE3.0DP_BAT";
	//3.1SUMP-FAT环境
	public static final String DATASOURCE_ICORE_SMP_FAT = "ICORE3.0SMP_FAT";
	
	//贷款DEV环境报文json
	public static final String POSTMAN_LN_DEV = "/postman/贷款DEV.postman_collection.json";
	//贷款DIT环境报文json
	public static final String POSTMAN_LN_DIT = "/postman/贷款DIT.postman_collection.json";
	//贷款FAT环境报文json
	public static final String POSTMAN_LN_FAT = "/postman/贷款FAT.postman_collection.json";
	//贷款CBS集中式环境网关
	public static final String POSTMAN_CBS = "/postman/ICORE3.0集中式.postman_collection.json";
	//额度FAT环境报文json
	public static final String POSTMAN_CL_FAT = "/postman/额度FAT.postman_collection.json";
	
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
	
	//线程并发数上限
	public static final int MAX_THREAD_CONCURRENT = 200;
	//默认通配符
	public static final String DEFAULT_WILDCARD = "*";
	//mybatis的mapper包
	public static final String MYBATIS_MAPPER_PACKAGE = "cn.ssy.base.dao.mapper";
}
