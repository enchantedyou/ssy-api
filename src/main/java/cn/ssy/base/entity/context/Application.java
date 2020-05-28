package cn.ssy.base.entity.context;

import cn.ssy.base.core.utils.mybatis.MybatisUtil;
import org.apache.log4j.Logger;
import org.yaml.snakeyaml.Yaml;


/**
 * <p>
 * 文件功能说明：
 *       	全局应用		
 * </p>
 * 
 * @Author sunshaoyu
 *         <p>
 *         <li>2020年5月20日-下午4:47:41</li>
 *         <li>修改记录</li>
 *         <li>-----------------------------------------------------------</li>
 *         <li>标记：修订内容</li>
 *         <li>2020年5月20日-sunshaoyu：创建注释模板</li>
 *         <li>-----------------------------------------------------------</li>
 *         </p>
 */
public class Application {

	private static final Logger logger = Logger.getLogger(Application.class);
	private static ApplicationContext applicationContext;
	private static MybatisUtil mybatisUtil;

	/**
	 * @Description	获取应用上下文
	 * @Author sunshaoyu
	 * @Date 2020/5/25-15:10
	 * @return cn.ssy.base.entity.context.ApplicationContext
	 */
	public static ApplicationContext getContext(){
		if(null == applicationContext){
			logger.info("Load global configuration");
			applicationContext = new Yaml().loadAs(Application.class.getResourceAsStream("/application.yml"), ApplicationContext.class);
		}
		return applicationContext;
	}

	/**
	 * @Description	获取全局mybatis工具
	 * @Author sunshaoyu
	 * @Date 2020/5/25-16:11
	 * @return cn.ssy.base.core.utils.mybatis.MybatisUtil
	 */
	public static MybatisUtil getMybatisUtil(){
		if(null == mybatisUtil){
			mybatisUtil = new MybatisUtil();
		}
		return mybatisUtil;
	}
}