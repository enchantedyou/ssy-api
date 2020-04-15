package cn.ssy.base.exception;



/**
 * <p>
 * 文件功能说明：
 *       	核心请求异常		
 * </p>
 * 
 * @Author sunshaoyu
 *         <p>
 *         <li>2020年4月15日-下午4:01:02</li>
 *         <li>修改记录</li>
 *         <li>-----------------------------------------------------------</li>
 *         <li>标记：修订内容</li>
 *         <li>2020年4月15日-sunshaoyu：创建注释模板</li>
 *         <li>-----------------------------------------------------------</li>
 *         </p>
 */
public class IcorePostException extends RuntimeException{

	private static final long serialVersionUID = 4477563633529824245L;

	public IcorePostException(String msg){
		super(msg);
	}
}
