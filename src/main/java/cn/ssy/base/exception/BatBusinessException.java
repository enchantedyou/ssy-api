package cn.ssy.base.exception;



/**
 * <p>
 * 文件功能说明：
 *       	批量异常		
 * </p>
 * 
 * @Author sunshaoyu
 *         <p>
 *         <li>2020年3月18日-下午3:14:17</li>
 *         <li>修改记录</li>
 *         <li>-----------------------------------------------------------</li>
 *         <li>标记：修订内容</li>
 *         <li>2020年3月18日-sunshaoyu：创建注释模板</li>
 *         <li>-----------------------------------------------------------</li>
 *         </p>
 */
public class BatBusinessException extends RuntimeException{

	private static final long serialVersionUID = 4477563633529824245L;

	public BatBusinessException(String msg){
		super(msg);
	}
}
