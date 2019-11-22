package cn.ssy.base.exception;



/**
 * <p>
 * 文件功能说明：
 *       	配置设置异常		
 * </p>
 * 
 * @Author sunshaoyu
 *         <p>
 *         <li>2019年11月12日-下午4:04:09</li>
 *         <li>修改记录</li>
 *         <li>-----------------------------------------------------------</li>
 *         <li>标记：修订内容</li>
 *         <li>2019年11月12日-sunshaoyu：创建注释模板</li>
 *         <li>-----------------------------------------------------------</li>
 *         </p>
 */
public class ConfigSettingException extends RuntimeException{

	private static final long serialVersionUID = -7204590725468640590L;

	public ConfigSettingException(String args){
		super("非法的配置设置:" + args);
	}
}
