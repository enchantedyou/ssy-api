package cn.ssy.base.core.utils.mybatis;

import org.apache.ibatis.session.SqlSession;

/**
 * <p>
 * 文件功能说明：
 *       	mybatis助手回调类		
 * </p>
 * 
 * @Author sunshaoyu
 *         <p>
 *         <li>2020年5月21日-下午8:32:37</li>
 *         <li>修改记录</li>
 *         <li>-----------------------------------------------------------</li>
 *         <li>标记：修订内容</li>
 *         <li>2020年5月21日-sunshaoyu：创建注释模板</li>
 *         <li>-----------------------------------------------------------</li>
 *         </p>
 */
public interface MybatisHelperCallback {

	public void call(final SqlSession sqlsession);
}
