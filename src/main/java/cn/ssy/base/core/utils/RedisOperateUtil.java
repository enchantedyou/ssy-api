package cn.ssy.base.core.utils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import net.sf.json.JSONArray;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;

/**
 * redis增删改查的工具类,包含:普通K-V操作方法,List K-V操作方法,Hash K-V操作方法,分页缓存等
 * 本工具类需导入的jar包:
 * jedis-2.8.2.jar
 * spring-data-redis-1.6.1.RELEASE.jar
 * commons-pool2-2.4.2.jar
 * 创建时间:2017/12/29
 * 修改时间:2018/6/7
 * @author 孙绍禹
 *
 */
@Service
public class RedisOperateUtil {
	
	private Logger logger = Logger.getLogger(RedisOperateUtil.class);
	private RedisTemplate<Serializable,Object> redisTemplate = new RedisTemplate<Serializable,Object>();
	
	
	public RedisOperateUtil(){
		initRedisTemplate();
	}
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年11月7日-下午2:42:21</li>
	 *         <li>功能说明：初始化RedisTemplate</li>
	 *         </p>
	 */
	public void initRedisTemplate(){
		Map<String, String> redisConfigMap = CommonUtil.readPropertiesSettings(RedisOperateUtil.class.getResource("/redis.properties").getPath());

		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setMaxIdle(Integer.parseInt(CommonUtil.nvl(redisConfigMap.get("maxIdle"), "300")));
		jedisPoolConfig.setMinIdle(Integer.parseInt(CommonUtil.nvl(redisConfigMap.get("minIdle"), "100")));
		jedisPoolConfig.setMaxWaitMillis(Long.parseLong(CommonUtil.nvl(redisConfigMap.get("maxWaitMillis"), "10000")));
		
		jedisPoolConfig.setMaxTotal(Integer.parseInt(CommonUtil.nvl(redisConfigMap.get("maxTotal"), "1000")));
		JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(jedisPoolConfig);
		jedisConnectionFactory.setHostName(redisConfigMap.get("host"));
		jedisConnectionFactory.setPort(Integer.parseInt(CommonUtil.nvl(redisConfigMap.get("port"), "6379")));
		
		jedisConnectionFactory.setUsePool(true);
		jedisConnectionFactory.setPassword(redisConfigMap.get("password"));
		JedisShardInfo jedisShardInfo = new JedisShardInfo(redisConfigMap.get("host"), Integer.parseInt(CommonUtil.nvl(redisConfigMap.get("port"), "6379")));
		jedisShardInfo.setConnectionTimeout(Integer.parseInt(CommonUtil.nvl(redisConfigMap.get("connectionTimeout"), "5000")));
		
		jedisShardInfo.setPassword(redisConfigMap.get("password"));
		jedisConnectionFactory.setShardInfo(jedisShardInfo);
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setHashKeySerializer(new StringRedisSerializer());
		
		redisTemplate.setValueSerializer(new SerializerUtil());
		redisTemplate.setConnectionFactory(jedisConnectionFactory);
		redisTemplate.afterPropertiesSet();
	}
	
	/*redis分页缓存 - 开始*/
	
	/**
	 * 填充分页缓存数据
	 * @param redisKey	将要存到redis中的key
	 * @param page	页码
	 * @param pageListData	当页链表数据
	 * @return	成功返回true,失败返回false
	 */
	public boolean setPagination(String redisKey,Integer page,List<? extends Object> pageListData){
		if(null == redisKey || redisKey.equals("") || null == page || page < 1 || null == pageListData || pageListData.isEmpty()){
			logger.info("设置分页缓存传入参数错误 - redisKey:"+redisKey+",page:"+page+",pageListData:"+pageListData.toString());
			return false;
		}
		if(putAndSetToHash(redisKey, String.valueOf(page), pageListData, 300)){
			logger.info("将第 "+page+" 页 "+redisKey+" 数据存入redis缓存成功");
			return true;
		}else{
			logger.info("将第 "+page+" 页 "+redisKey+" 数据存入redis缓存失败");
			return false;
		}
	}

	/**
	 * 从redis缓存中获取某页数据
	 * @param redisKey redis中的key
	 * @param page	页码
	 * @param clazz	数据类型
	 * @return	成功返回数据链表,失败返回null
	 */
	public List<? extends Object> getPagination(String redisKey,Integer page,Class<? extends Object> clazz) {
		if(null == redisKey || redisKey.equals("") || null == page || page < 1 || null == clazz)
			return null;
		Object obj = getHashValue(redisKey, String.valueOf(page));
		if(null == obj){
			logger.info("redis缓存中不存在第 "+page+" 页的 "+redisKey+" 数据");
			return null;
		}
		JSONArray array = JSONArray.fromObject(obj);
		if(null == array || array.isEmpty())
			return null;
		 @SuppressWarnings("deprecation")
		List<?> list = JSONArray.toList(array, clazz);
		 if(null == list || list.isEmpty()){
			 logger.info("redis缓存中不存在第 "+page+" 页的 "+redisKey+" 数据");
			 return null;
		 }else{
			 logger.info("成功从redis缓存中获取第 "+page+" 页的 "+redisKey+" 数据");
			 return list;
		 }
	}
	
	
	/**
	 * 使某个缓存失效
	 * @param key	键
	 */
	public void overdue(Serializable redisKey){
		if(null == redisKey || redisKey.equals(""))
			return;
		if(redisTemplate.hasKey(redisKey)){
			delete(redisKey);
			logger.info(redisKey+" 数据发生改变,删除分页缓存");
		}
	}

	/*redis分页缓存 - 结束*/
	
	
	/**
	 * 将从redis缓存中获取的JSONArray数据转为链表
	 * @param key	键
	 * @param clazz	链表中对象的类
	 * @return	转换成功返回链表,失败返回null
	 */
	@SuppressWarnings({ "unchecked", "deprecation" })
	public List<? extends Object> parseToList(Serializable key,Class<? extends Object> clazz){
		try{
			JSONArray array = JSONArray.fromObject(getList(key));
			if(array == null || array.isEmpty() || array.getJSONArray(0) == null || array.getJSONArray(0).isEmpty()){
				return null;
			}else{
				return JSONArray.toList(array.getJSONArray(0), clazz);
			}
		}catch(Exception e){
			return null;
		}
	}
	
	
	/**
	 * 获取redis缓存中某个哈希表的所有数据并转map
	 * @param key	哈希表的key
	 * @param clazz	哈希表值的类
	 * @return	成功返回哈希表结果,失败返回null
	 */
	public Map<? extends Object, ? extends Object> parseToMap(Serializable key){
		try{
			//获取该哈希表中的所有的key
			Set<Object> mapKeySet = redisTemplate.opsForHash().keys(key);
			//初始化一个新的哈希表
			Map<Object, Object> map = new HashMap<Object, Object>();
			//遍历key并填值
			for(Object _key:mapKeySet){
				map.put(_key, getHashValue(key, _key));
			}
			return map;
		}catch(Exception e){
			return null;
		}
	}
	
	
	/**
	 * 指定某个缓存的数据的过期时间
	 * @param key	缓存中的key
	 * @param timeout	过期时间,计时单位为秒
	 * @return	返回是否设置成功的布尔值
	 */
	public boolean expire(Serializable key,long timeout){
		try{
			if(timeout > 0){
				redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
			}
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	
	/**
	 * 获取缓存中某个key的有效剩余时间,单位为秒
	 * @param key	缓存中的key
	 * @return	返回缓存某个key的剩余时间,0为永久
	 */
	public long getExpire(Serializable key){
		return redisTemplate.getExpire(key,TimeUnit.SECONDS);
	}
	
	
	/*普通K-V操作方法->开始*/
	
	/**
	 * 为redis缓存中的添加一个键值对或设置某键值对的值
	 * @param key	缓存中某键值对的key或一个新的key
	 * @param value	要设置或添加的值
	 * @param timeout 缓存有效时间,单位为秒
	 * @return	返回是否添加或设置成功的布尔值
	 */
	public boolean addandSetValue(Serializable key,Object value,long timeout){
		try{
			redisTemplate.opsForValue().set(key, value,timeout,TimeUnit.SECONDS);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	
	/**
	 * 从redis缓存中获取某个key的值
	 * @param key	缓存中某键值对的key
	 * @return	成功返回查询到的值,失败返回null
	 */
	public Object getValue(Serializable key){
		try{
			return redisTemplate.opsForValue().get(key);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	/*普通K-V操作方法->结束*/
	
	
	
	/*List K-V操作方法->开始*/
	
	
	/**
	 * 将整个链表插入缓存
	 * @param key	链表的key
	 * @param value	要添加的值
	 * @param timeout 缓存有效时间,单位为秒
	 * @return	返回是否添加成功的布尔值
	 */
	public boolean pushAllAsList(Serializable key,Object value,long timeout){
		try{
			redisTemplate.opsForList().leftPushAll(key, value);
			if(timeout > 0)
				expire(key, timeout);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 从链表的左边插入一个值
	 * @param key	链表的key
	 * @param value	要添加的值
	 * @param timeout 缓存有效时间,单位为秒
	 * @return	返回是否添加成功的布尔值
	 */
	public boolean addValueToList(Serializable key,Object value,long timeout){
		try{
			redisTemplate.opsForList().leftPush(key, value);
			if(timeout > 0)
				expire(key, timeout);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	
	/**
	 * 重置某个链表的指定下标的值
	 * @param key	链表的key
	 * @param index	指定下标
	 * @param value	要替换的值
	 * @param timeout 缓存有效时间,单位为秒
	 * @return	返回是否替换成功的布尔值
	 */
	public boolean setValueFromList(Serializable key,long index,Object value,long timeout){
		try{
			redisTemplate.opsForList().set(key, index, value);
			if(timeout > 0)
				expire(key, timeout);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	
	
	/**
	 * 根据下标获取列表中的值，下标是从0开始的
	 * @param key	链表的key
	 * @param index	下标
	 * @return	如果获取成功返回该元素,否则返回null
	 */
	public Object getListValue(Serializable key,long index){
		try{
			return redisTemplate.opsForList().index(key, index);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	
	/**
	 * 获取缓存中的某个链表
	 * @param key	链表的key
	 * @return	成功返回链表,失败返回null
	 */
	public Object getList(Serializable key){
		try{
			return redisTemplate.opsForList().range(key, 0, -1);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	
	
	/**
	 * 从存储在键中的列表中删除等于值的元素的第一个计数事件。
	 * 计数参数以下列方式影响操作：
	 * @param key
	 * @param count
	 * 	count> 0：删除等于从头到尾移动的值的元素。
		count <0：删除等于从尾到头移动的值的元素。
		count = 0：删除等于value的所有元素。
	 * @param value 要移除的值
	 * @return 返回是否移除成功的布尔值
	 */
	public boolean removeFromList(Serializable key,long count,Object value){
		try{
			redisTemplate.opsForList().remove(key, count, value);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	/*List K-V操作方法->结束*/
	
	
	/*Hash K-V操作方法->开始*/
	
	
	/**
	 * 向redis缓存中的某个哈希表添加或设置(如果该_key已存在)值,不设置过期时间
	 * @param key	哈希表的key
	 * @param _key	哈希表中某个值的key
	 * @param value	要添加或设置的值
	 * @return	返回是否添加或设置成功的布尔值
	 */
	public boolean putAndSetToHash(Serializable key,Object _key,Object value){
		return putAndSetToHash(key, _key, value, -1);
	}
	
	/**
	 * 向redis缓存中的某个哈希表添加或设置(如果该_key已存在)值
	 * @param key	哈希表的key
	 * @param _key	哈希表中某个值的key
	 * @param value	要添加或设置的值
	 * @param timeout 缓存有效时间,单位为秒
	 * @return	返回是否添加或设置成功的布尔值
	 */
	public boolean putAndSetToHash(Serializable key,Object _key,Object value,long timeout){
		try{
			redisTemplate.opsForHash().put(key,_key, value);
			if(timeout > 0)
				expire(key, timeout);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	
	/**
	 * 查询某个哈希表的某个键是否有值
	 * @param key	哈希表的key
	 * @param _key	哈希表某个值的key
	 * @return 如果含有_key对应的值,返回true,否则返回false
	 */
	public boolean hashKeyFromHash(Serializable key,Object _key){
		try{
			return redisTemplate.opsForHash().hasKey(key, _key);
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	
	/**
	 * 从redis缓存中移除某个哈希表的某个值
	 * @param key	哈希表的key
	 * @param _key	哈希表中某个值的key
	 * @return	返回是否移除成功的布尔值
	 */
	public boolean removeFromHash(Serializable key,Object _key){
		try{
			redisTemplate.opsForHash().delete(key, _key);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	
	
	/**
	 * 将整个哈希表存入redis缓存
	 * @param key	哈希表的key
	 * @param value	值,类型为哈希表
	 * @param timeout 缓存有效时间,单位为秒
	 * @return 返回是否添加成功的布尔值
	 */
	public boolean pushAllAsHash(Serializable key,Map<? extends Object, ? extends Object> value,long timeout){
		try{
			redisTemplate.opsForHash().putAll(key, value);
			if(timeout > 0)
				expire(key, timeout);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	
	/**
	 * 从redis缓存中获取哈希表中的某个键的值
	 * @param key	哈希表的key
	 * @param _key	哈希表中某个值的key
	 * @return 成功则返回获取到的值,否则返回null
	 */
	public Object getHashValue(Serializable key,Object _key){
		try{
			return redisTemplate.opsForHash().get(key, _key);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	
	public Object getHashEntries(Serializable key){
		try{
			return redisTemplate.opsForHash().entries(key);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	/*Hash K-V操作方法->结束*/
	
	/**
	 * 删除整个元素
	 * @param key	元素的key
	 * @return	返回是否删除成功的布尔值
	 */
	public boolean delete(Serializable key){
		try{
			redisTemplate.delete(key);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	
	/**
	 * 判断redis缓存中是否存在键为key的数据
	 * @param key	键
	 * @return	返回是否存在的布尔值
	 */
	public boolean hasKey(Serializable key){
		return redisTemplate.hasKey(key);
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年12月20日-下午1:08:34</li>
	 *         <li>功能说明：获取redis信息</li>
	 *         </p>
	 * @return
	 */
	public Properties getRedisInfo(){
		return redisTemplate.execute(
			new RedisCallback<Properties>() {
			@Override
			public Properties doInRedis(RedisConnection redisConnection) throws DataAccessException {
				return redisConnection.info();
			}
		});
	}
}
