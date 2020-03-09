package cn.ssy.base.entity.datastruct.impl;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

import cn.ssy.base.entity.datastruct.intf.LinkedList;

/**
 * <p>
 * 文件功能说明：
 *       	双向循环链表		
 * </p>
 * @param <T>
 * 
 * @Author sunshaoyu
 *         <p>
 *         <li>2019年8月11日-下午7:17:10</li>
 *         <li>修改记录</li>
 *         <li>-----------------------------------------------------------</li>
 *         <li>标记：修订内容</li>
 *         <li>2019年8月11日-sunshaoyu：创建注释模板</li>
 *         <li>-----------------------------------------------------------</li>
 *         </p>
 */
public class BilateralLinkedList<T extends Comparable<? super T>> implements LinkedList<T>{
	
	//链表节点静态内部类
	private class Node{
		Node before;	//前驱指针
		Node after;	//后驱指针
		T data;	//节点数据
		
		Node(T data) {
			this.data = data;
		}
	}
	
	//链表中数据的条数
	private int size;
	
	//链表头节点,不存储数据
	transient Node head;
	
	//双向循环链表数据迭代器
	private class BilateralDataIterator implements Iterator<T>{

		private Node temp;

		BilateralDataIterator(Node temp) {
			this.temp = temp;
		}
		
		@Override
		public boolean hasNext() {
			return temp != null && temp != head;
		}

		@Override
		public T next() {
			T data = temp.data;
			temp = temp.after;
			return data;
		}
	}
	
	
	//双向循环链表节点迭代器
	private class BilateralNodeIterator implements Iterator<Node>{

		private Node temp;

		BilateralNodeIterator(Node temp) {
			this.temp = temp;
		}
		
		@Override
		public boolean hasNext() {
			return temp != null && temp != head;
		}

		@Override
		public Node next() {
			Node returnNode = temp;
			temp = temp.after;
			return returnNode;
		}
	}
	
	
	/**
	 * 构造方法
	 */
	public BilateralLinkedList() {
		this.size = 0;
		head = new Node(null);
		head.after = head;
		head.before = head;
	}

	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月11日-下午9:51:16</li>
	 *         <li>功能说明：检查下标是否合法</li>
	 *         </p>
	 * @param index
	 * @return
	 */
	private boolean checkIndexValidation(int index){
		return index >=0 && index < size;
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月11日-下午9:52:56</li>
	 *         <li>功能说明：搜索第一个与data相等的节点</li>
	 *         </p>
	 * @param data
	 * @return	返回该数据所在的节点
	 */
	private Node searchNode(T data){
		Iterator<Node> iterator = nodeIterator();
		while(iterator.hasNext()){
			Node node = iterator.next();
			if(data != null && data.equals(node.data)){
				return node;
			}
		}
		return null;
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月11日-下午10:09:34</li>
	 *         <li>功能说明：返回下标为i的链表节点</li>
	 *         </p>
	 * @param i
	 * @return
	 */
	private Node searchNode(int i){
		if(!checkIndexValidation(i)){
			throw new NoSuchElementException();
		}

		int index = 0;
		Iterator<Node> iterator = nodeIterator();
		while(iterator.hasNext()){
			Node node = iterator.next();
			if(index == i){
				return node;
			}
			index++;
		}
		return null;
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月12日-上午9:43:47</li>
	 *         <li>功能说明：在指定节点插入一条数据<li>
	 *         </p>
	 * @param node
	 * @param data
	 */
	private boolean insert(Node node,T data){
		if(node == null){
			return false;
		}
		
		Node newNode = new Node(data);
		newNode.after = node;
		newNode.before = node.before;
		
		node.before.after = newNode;
		node.before = newNode;
		this.size++;
		return true;
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月12日-下午2:01:35</li>
	 *         <li>功能说明：删除从前往后检索发现的第一个数据与data相等的节点</li>
	 *         </p>
	 * @param data	待删除的数据
	 * @param isDeleteAll	是否删除所有数据与data相等的节点
	 * @return
	 */
	private boolean delete(T data,boolean isDeleteAll){
		if(data == null){
			return false;
		}
		
		Node current = head.after;
		while(current != head){
			if(data.equals(current.data)){
				
				current.before.after = current.after;
				current.after.before = current.before;
				current.data = null;
				this.size--;
				
				if(!isDeleteAll){
					return true;
				}
			}
			current = current.after;
		}
		return false;
	}
	
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月12日-下午2:21:23</li>
	 *         <li>功能说明：删除下标为i的节点</li>
	 *         </p>
	 * @param i
	 * @return
	 */
	private boolean delete(int i){
		if(!checkIndexValidation(i)){
			throw new NoSuchElementException();
		}
		
		Node node = searchNode(i);
		node.before.after = node.after;
		node.after.before = node.before;
		
		node.data = null;
		node = null;
		this.size--;
		
		return true;
	}
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月11日-下午8:13:45</li>
	 *         <li>功能说明：获取链表的数据条数</li>
	 *         </p>
	 * @return
	 */
	@Override
	public int size(){
		return this.size;
	}
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月11日-下午8:14:56</li>
	 *         <li>功能说明：如果链表中的数据条数为0,返回true</li>
	 *         </p>
	 * @return
	 */
	@Override
	public boolean isEmpty(){
		return this.size == 0;
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月11日-下午8:24:57</li>
	 *         <li>功能说明：从尾部插入一条数据</li>
	 *         </p>
	 * @param data
	 */
	public boolean add(T data){
		return insert(head, data);
	}
	
	
	@Override
	public boolean add(int i, T data) {
		if(!checkIndexValidation(i)){
			throw new NoSuchElementException();
		}
		Node temp = searchNode(i);
		return insert(temp, data);
	}


	@Override
	public Iterator<T> iterator() {
		Node temp = head == null ? null : head.after;
		return new BilateralDataIterator(temp);
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月11日-下午10:03:20</li>
	 *         <li>功能说明：链表节点遍历器</li>
	 *         </p>
	 * @return
	 */
	private Iterator<Node> nodeIterator() {
		Node temp = head == null ? null : head.after;
		return new BilateralNodeIterator(temp);
	}


	@SuppressWarnings("unchecked")
	@Override
	public boolean addAll(int i, Collection<T> c) {
		if(!checkIndexValidation(i)){
			throw new NoSuchElementException();
		}else if(c == null || c.isEmpty()){
			return false;
		}
		
		int len = c.size();
		T[] arr = (T[]) c.toArray(new Object[]{});
		
		for(int j = len - 1;j >= 0;j--){
			add(i, arr[j]);
		}
		return true;
	}


	@Override
	public boolean addAll(Collection<T> c) {
		for(T data : c){
			add(data);
		}
		return true;
	}


	@Override
	public boolean remove(T data) {
		return delete(data, false);
	}


	@Override
	public boolean remove(int i) {
		return delete(i);
	}


	@Override
	public boolean removeAll(T data) {
		return delete(data, true);
	}


	@Override
	public boolean removeAll() {
		Node current = head.after;
		Node after = null;
		
		while(current != head){
			after = current.after;
			current.before = null;
			current.after = null;
			current.data = null;
			
			current = after;
			size--;
		}
		head = null;
		return !isEmpty();
	}


	@Override
	public boolean contains(T data) {
		return searchNode(data) != null;
	}


	@Override
	public T get(int i) {
		Node node = searchNode(i);
		return node == null ? null : node.data;
	}


	@Override
	public T get(T data) {
		Node node = searchNode(data);
		return node == null ? null : node.data;
	}
	
	
	@Override
	public void set(int i, T data) {
		if(!checkIndexValidation(i)){
			throw new NoSuchElementException();
		}
		Node node = searchNode(i);
		node.data = data;
	}


	@SuppressWarnings("unchecked")
	@Override
	public T[] toArray(T[] arr) {
		arr = (T[]) Array.newInstance(arr.getClass().getComponentType(), size);
		Iterator<T> iterator = iterator();
		int index = 0;
		while(iterator.hasNext()){
			arr[index++] = iterator.next();
		}
		return arr;
	}


	@Override
	public void sort(boolean isAsc) {
		shellSort(isAsc);
	}
	
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月13日-下午7:21:04</li>
	 *         <li>功能说明：希尔排序</li>
	 *         </p>
	 * @param isAsc	是否升序
	 */
	private void shellSort(boolean isAsc){
		int len = size;
		for(int gap = len >> 1;gap > 0;gap >>= 1){
			for(int start = gap;start < len;start++){
				T temp = get(start);
				int i = -1;
				for(i = start - gap;i >= 0;i -= gap){
					T curData = get(i);
					if(isAsc ? (compare(curData, temp) > 0) : (compare(get(i), temp) < 0)){
						set(i + gap, curData);
					}else{
						break;
					}
				}
				set(i + gap, temp);
			}
		}
	}
	
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月9日-下午8:54:47</li>
	 *         <li>功能说明：比较方法,设定null为最小值;a>b返回大于0 的值,a<b返回小于0的值,a=b返回等于0的值</li>
	 *         </p>
	 * @param a	待比较的第一个值
	 * @param b	待比较的第二个值
	 * @return
	 */
	private int compare(T a,T b){
		if(null == a && null == b){
			return 0;
		}else if(null == a && null != b){
			return -1;
		}else if(null != a && null == b){
			return 1;
		}else{
			return a.compareTo(b);
		}
	}

	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("[");
		for(int i = 0;i < size;i++){
			buffer.append(String.valueOf(get(i)));
			if(i != size - 1){
				buffer.append(", ");
			}
		}
		buffer.append("]");
		return buffer.toString();
	}
}
