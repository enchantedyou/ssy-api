package cn.ssy.base.exception;

import java.util.Arrays;


/**
 * <p>
 * 文件功能说明：
 *       	参数为空异常		
 * </p>
 * 
 * @Author sunshaoyu
 *         <p>
 *         <li>2019年9月9日-下午4:06:28</li>
 *         <li>修改记录</li>
 *         <li>-----------------------------------------------------------</li>
 *         <li>标记：修订内容</li>
 *         <li>2019年9月9日-sunshaoyu：创建注释模板</li>
 *         <li>-----------------------------------------------------------</li>
 *         </p>
 */
public class NullParmException extends RuntimeException{

	private static final long serialVersionUID = 4477563633529824245L;

	public NullParmException(String... args){
		super("入参" + Arrays.toString(args) + "有一个或多个值为空");
	}
}
