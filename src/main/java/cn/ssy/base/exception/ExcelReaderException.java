package cn.ssy.base.exception;



/**
 * <p>
 * 文件功能说明：
 *       	Excel读取异常		
 * </p>
 * 
 * @Author sunshaoyu
 *         <p>
 *         <li>2019年10月31日-下午2:49:51</li>
 *         <li>修改记录</li>
 *         <li>-----------------------------------------------------------</li>
 *         <li>标记：修订内容</li>
 *         <li>2019年10月31日-sunshaoyu：创建注释模板</li>
 *         <li>-----------------------------------------------------------</li>
 *         </p>
 */
public class ExcelReaderException extends RuntimeException{

	private static final long serialVersionUID = -3868063355879482442L;

	public ExcelReaderException(String msg){
		super(msg);
	}
}
