package cn.ssy.base.core.utils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.util.Assert;

/**
 * 自定义序列化 时间:2018/6/7
 * 
 * @author 孙绍禹
 * 
 */
public class SerializerUtil implements RedisSerializer<Object> {

	static final byte[] EMPTY_ARRAY = new byte[0];
	// 编码
	private final Charset charset;

	public SerializerUtil() {
		this(Charset.forName("UTF8"));
	}

	public SerializerUtil(Charset charset) {
		Assert.notNull(charset);
		this.charset = charset;
	}

	/**
	 * 对象系列化
	 */
	@Override
	public byte[] serialize(Object object) {
		try {
			JSONObject jsonObject = JSONObject.fromObject(object);
			String jsonString = jsonObject.toString();
			return (jsonString == null ? EMPTY_ARRAY : jsonString
					.getBytes(charset));
		} catch (Exception e) {
			try {
				JSONArray jsonArray = JSONArray.fromObject(object);
				String jsonString = jsonArray.toString();
				return (jsonString == null ? EMPTY_ARRAY : jsonString
						.getBytes(charset));
			} catch (Exception ex) {
				return String.valueOf(object).getBytes();
			}
		}
	}

	/**
	 * 对象反序列化
	 */
	@Override
	public Object deserialize(byte[] bytes) throws SerializationException { // 反序列化
		String objectStr = null;
		Object object = null;
		if (bytes == null) {
			return object;
		}
		try {
			objectStr = new String(bytes, charset); // byte数组转换为String
			JSONObject jsonObject = JSONObject.fromObject(objectStr); // String转化为JSONObject
			object = jsonObject; // 返回的是JSONObject类型 取数据时候需要再次转换一下
		} catch (Exception e) {
			try {
				objectStr = new String(bytes, charset); // byte数组转换为String
				return JSONArray.fromObject(objectStr);
			} catch (Exception ex) {
				try {
					return new String(bytes, "UTF-8");
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
				}
			}
		}
		return object;
	}

}
