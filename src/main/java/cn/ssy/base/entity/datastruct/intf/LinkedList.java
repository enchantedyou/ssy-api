package cn.ssy.base.entity.datastruct.intf;

import java.util.Collection;

/**
 * <p>
 * 文件功能说明：
 *       	链表接口		
 * </p>
 * 
 * @Author sunshaoyu
 *         <p>
 *         <li>2019年8月11日-下午9:28:35</li>
 *         <li>修改记录</li>
 *         <li>-----------------------------------------------------------</li>
 *         <li>标记：修订内容</li>
 *         <li>2019年8月11日-sunshaoyu：创建注释模板</li>
 *         <li>-----------------------------------------------------------</li>
 *         </p>
 */
public interface LinkedList<T extends Comparable<? super T>> extends Iterable<T>{

	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月11日-下午9:29:58</li>
	 *         <li>功能说明：获取链表中的数据量</li>
	 *         </p>
	 * @return
	 */
	public int size();
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月11日-下午9:30:22</li>
	 *         <li>功能说明：链表中的数据量为0返回true</li>
	 *         </p>
	 * @return
	 */
	public boolean isEmpty();
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月11日-下午9:31:00</li>
	 *         <li>功能说明：从链表的尾部插入一条数据</li>
	 *         </p>
	 * @param data
	 */
	public boolean add(T data);
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月11日-下午9:39:50</li>
	 *         <li>功能说明：从指定位置插入元一条数据</li>
	 *         </p>
	 * @param i
	 * @param data	
	 */
	public boolean add(int i,T data);
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月11日-下午9:40:23</li>
	 *         <li>功能说明：从指定位置插入一个集合</li>
	 *         </p>
	 * @param i
	 * @param c
	 */
	public boolean addAll(int i,Collection<T> c);
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月11日-下午9:41:07</li>
	 *         <li>功能说明：插入一个集合</li>
	 *         </p>
	 * @param c
	 */
	public boolean addAll(Collection<T> c);
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月11日-下午9:42:00</li>
	 *         <li>功能说明：移除从头节点往后检索发现的第一条与data相等的数据</li>
	 *         </p>
	 * @param data
	 * @return
	 */
	public boolean remove(T data);
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月11日-下午9:42:57</li>
	 *         <li>功能说明：移除下标为i的数据</li>
	 *         </p>
	 * @param i
	 * @return
	 */
	public boolean remove(int i);
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月11日-下午9:46:50</li>
	 *         <li>功能说明：移除所有与data相等的数据</li>
	 *         </p>
	 * @param data
	 * @return
	 */
	public boolean removeAll(T data);
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月11日-下午9:45:42</li>
	 *         <li>功能说明：移除所有数据</li>
	 *         </p>
	 * @return
	 */
	public boolean removeAll();
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月11日-下午9:47:23</li>
	 *         <li>功能说明：链表中包含data返回true</li>
	 *         </p>
	 * @param data
	 * @return
	 */
	public boolean contains(T data);
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月12日-上午9:25:42</li>
	 *         <li>功能说明：获取链表中下标为i的数据</li>
	 *         </p>
	 * @param i
	 * @return
	 */
	public T get(int i);
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月12日-上午9:26:05</li>
	 *         <li>功能说明：获取链表中与data相等的数据</li>
	 *         </p>
	 * @param data
	 * @return
	 */
	public T get(T data);
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月12日-下午2:36:43</li>
	 *         <li>功能说明：双向循环链表转数组</li>
	 *         </p>
	 * @param	instance	元素泛型实例
	 * @return
	 */
	public T[] toArray(T[] arr);
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月12日-下午3:30:08</li>
	 *         <li>功能说明：排序</li>
	 *         </p>
	 * @param isAsc	是否升序
	 */
	public void sort(boolean isAsc);
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月13日-下午7:09:09</li>
	 *         <li>功能说明：替换下标为i的节点的数据</li>
	 *         </p>
	 * @param i
	 * @param data
	 */
	public void set(int i,T data);
}
