package cn.ssy.base.entity.plugins;

/**
 * <p>
 * 文件功能说明：
 *    		回调函数   			
 * </p>
 * 
 * @Author sunshaoyu
 *         <p>
 *         <li>2019年12月31日-下午4:22:58</li>
 *         <li>修改记录</li>
 *         <li>-----------------------------------------------------------</li>
 *         <li>标记：修订内容</li>
 *         <li>2019年12月31日-sunshaoyu：创建注释模板</li>
 *         <li>-----------------------------------------------------------</li>
 *         </p>
 */
public interface Callback<T> {

	public T call();
}
