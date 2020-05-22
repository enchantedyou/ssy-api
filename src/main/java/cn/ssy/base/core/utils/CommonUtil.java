package cn.ssy.base.core.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLConnection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import cn.ssy.base.core.network.api.NetworkApi;
import cn.ssy.base.entity.consts.ApiConst;
import cn.ssy.base.entity.plugins.Params;
import cn.ssy.base.enums.E_LANGUAGE;
import cn.ssy.base.exception.NullParmException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;

/**
 * <p>
 * 文件功能说明： 公共工具类
 * </p>
 * 
 * @Author sunshaoyu
 *         <p>
 *         <li>2019年3月25日-下午2:22:45</li>
 *         <li>修改记录</li>
 *         <li>-----------------------------------------------------------</li>
 *         <li>标记：修订内容</li>
 *         <li>2019年3月25日-sunshaoyu：创建注释模板</li>
 *         <li>-----------------------------------------------------------</li>
 *         </p>
 */
@SuppressWarnings("restriction")
public class CommonUtil {
	
	private static final Logger logger = Logger.getLogger(CommonUtil.class);

	/**
	 * <p>
	 * 文件功能说明：
	 *       	AES加解密		
	 * </p>
	 * 
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2020年1月2日-下午1:36:42</li>
	 *         <li>修改记录</li>
	 *         <li>-----------------------------------------------------------</li>
	 *         <li>标记：修订内容</li>
	 *         <li>2020年1月2日-sunshaoyu：创建注释模板</li>
	 *         <li>-----------------------------------------------------------</li>
	 *         </p>
	 */
	public static class AES{
		
		/**
		 * @Author sunshaoyu
		 *         <p>
		 *         <li>2020年1月2日-下午1:10:08</li>
		 *         <li>功能说明：aes加密</li>
		 *         </p>
		 * @param content	待加密的内容
		 * @param key	秘钥
		 * @return
		 * @throws Exception
		 */
		public static String encrypt(String content, String key) throws Exception {
	        byte[] raw = key.getBytes();
	        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
	        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");//"算法/模式/补码方式"
	        IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes());//使用CBC模式，需要一个向量iv，可增加加密算法的强度
	        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
	        byte[] encrypted = cipher.doFinal(content.getBytes());
	        return new BASE64Encoder().encode(encrypted);//此处使用BASE64做转码功能，同时能起到2次加密的作用。
	    }
		
		
		/**
		 * @Author sunshaoyu
		 *         <p>
		 *         <li>2020年1月2日-下午1:10:28</li>
		 *         <li>功能说明：aes解密</li>
		 *         </p>
		 * @param encrypted	加密后的内容
		 * @param key	秘钥
		 * @return
		 * @throws Exception
		 */
		public static String decrypt(String encrypted, String key) throws Exception {
		    byte[] raw = key.getBytes("UTF-8");
		    SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		    IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes());
		    cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
		    byte[] encrypted1 = new BASE64Decoder().decodeBuffer(encrypted);//先用base64解密
		    byte[] original = cipher.doFinal(encrypted1);
		    String originalString = new String(original,"UTF-8");
		    return originalString;
	    }
	}

	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年3月25日-下午3:28:41</li>
	 *         <li>功能说明：判断对象是否为null或空值</li>
	 *         </p>
	 * @param obj
	 *            要判断的对象
	 * @return 为空返回true,否则返回false
	 */
	public static boolean isNull(Object obj) {
		if (null == obj){
			return true;
		} else if (obj instanceof String) {
			obj = String.valueOf(obj);
			return ((String) obj).length() == 0 || ((String) obj).equals("null");
		} else if (obj instanceof Map) {
			obj = Map.class.cast(obj);
			Map<?, ?> tmpObj = (Map<?, ?>) obj;
			return tmpObj.isEmpty();
		} else if (obj instanceof Collection<?>) {
			obj = Collection.class.cast(obj);
			Collection<?> tmpObj = (Collection<?>) obj;
			return tmpObj.isEmpty();
		} else if (obj.getClass().isArray()) {
			return Array.getLength(obj) == 0;
		}
		return false;
	}
	
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年7月19日-上午11:35:51</li>
	 *         <li>功能说明：如果value为空,取默认值,否则返回value</li>
	 *         </p>
	 * @param value
	 * @param defaultValue
	 * @return
	 */
	public static <T> T nvl(T value,T defaultValue){
		return isNull(value) ? defaultValue : value;
	}
	

	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年3月25日-下午3:29:57</li>
	 *         <li>功能说明：判断对象是否不为null且不为空值</li>
	 *         </p>
	 * @param obj
	 *            要判断的对象
	 * @return 不为空返回true,否则返回false
	 */
	public static boolean isNotNull(Object obj) {
		return !(isNull(obj));
	}
	
	
	/***
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年3月14日-下午2:22:48</li>
	 *         <li>功能说明：生成随机字符串</li>
	 *         </p>
	 * @param len
	 *            生成字符串的长度
	 * @param model
	 *            生成模式: 1:纯数字; 2:纯大写字母; 3:纯小写字母; 其余则为任意字符串
	 * @return
	 */
	public static String randStr(int len, int model) {

		Random rand = new Random();
		StringBuffer buffer = new StringBuffer();
		String dict = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		int start = -1, end = -1;

		switch (model) {
			case 1:
				start = 52;
				end = 61;
				break;
			case 2:
				start = 26;
				end = 52;
				break;
			case 3:
				start = 0;
				end = 26;
				break;
			default:
				start = 0;
				end = 61;
				break;
		}

		for (int i = 0; i < len; i++) {
			int index = rand.nextInt(end - start) + start;
			buffer.append(dict.charAt(index));
		}
		return buffer.toString();
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年4月8日-下午1:24:05</li>
	 *         <li>功能说明：生成随机字符串</li>
	 *         </p>
	 * @param len	生成字符串的长度
	 * @return	返回随机字符串
	 */
	public static String randStr(int len) {
		return randStr(len, 4);
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月7日-下午2:43:49</li>
	 *         <li>功能说明：检查字符串中是否包含数组中的某个标志</li>
	 *         </p>
	 * @param str
	 * @param indArr
	 * @return
	 */
	public static boolean isContainsIgnoreCase(String str,String[] indArr){
		if(isNull(str) || isNull(indArr)){
			return false;
		}else{
			for(String indentity : indArr){
				if(str.toLowerCase().contains(indentity.toLowerCase())){
					return true;
				}
			}
			return false;
		}
	}
	

	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年3月25日-下午3:33:36</li>
	 *         <li>功能说明：获取json对象中对应类型的key的值</li>
	 *         </p>
	 * @param json
	 *            json对象
	 * @param valueType
	 *            要获取的值的类型
	 * @param key
	 *            要获取的值的key
	 * @return
	 * @throws ParseException
	 */
	public static Object getJsonValueByType(JSONObject json,
			Class<?> valueType, Object key) throws ParseException {
		Object value = json.get(key);
		String typeName = valueType.getSimpleName();

		if ("Integer".equalsIgnoreCase(typeName)) {
			value = Integer.parseInt(String.valueOf(value));
		} else if ("Boolean".equalsIgnoreCase(typeName)) {
			value = Boolean.parseBoolean(String.valueOf(value));
		} else if ("String".equalsIgnoreCase(typeName)) {
			value = xssFilter(String.valueOf(String.valueOf(value)));
		} else if ("Long".equalsIgnoreCase(typeName)) {
			value = Long.parseLong(String.valueOf(value));
		} else if ("Double".equalsIgnoreCase(typeName)) {
			value = Double.parseDouble(String.valueOf(value));
		} else if ("Short".equalsIgnoreCase(typeName)) {
			value = Short.parseShort(String.valueOf(value));
		} else if ("Byte".equalsIgnoreCase(typeName)) {
			value = Short.parseShort(String.valueOf(value));
		} else if ("Float".equalsIgnoreCase(typeName)) {
			value = Float.parseFloat(String.valueOf(value));
		} else {
			value = valueType.cast(value);
		}
		return value;
	}

	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年3月27日-下午3:54:22</li>
	 *         <li>功能说明：防xss跨站脚本攻击过滤</li>
	 *         </p>
	 * @param paramValue
	 *            要过滤的字符串
	 * @return 返回过滤后的字符串
	 */
	public static String xssFilter(String paramValue) {

		if (isNull(paramValue))
			return paramValue;
		StringBuilder sb = new StringBuilder(paramValue.length() + 16);
		for (int i = 0; i < paramValue.length(); i++) {
			char c = paramValue.charAt(i);
			switch (c) {
			case '>':
				sb.append("&gt;");
				break;
			case '<':
				sb.append("&lt;");
				break;
			case '\'':
				sb.append("&#039;");
				break;
			case '\"':
				sb.append("&quot");
				break;
			case '&':
				sb.append("&quot");
				break;
			case '©':
				sb.append("&copy");
				break;
			default:
				sb.append(c);
				break;
			}
		}
		return sb.toString();
	}

	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年3月27日-下午4:13:57</li>
	 *         <li>功能说明：设置session的值</li>
	 *         </p>
	 * @param session
	 *            session
	 * @param sessionKey
	 *            session的键
	 * @param mapKey
	 *            session值的map的键
	 * @param mapValue
	 *            session值的map的值
	 */
	public static void setSessionMap(HttpSession session, String sessionKey,
			String mapKey, Object mapValue) {
		Params sessionMap = (Params) session.getAttribute(sessionKey);
		if (null == sessionMap)
			sessionMap = new Params();
		sessionMap.add(mapKey, mapValue);
		session.setAttribute(sessionKey, sessionMap);
	}

	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年3月27日-下午4:15:09</li>
	 *         <li>功能说明：获取session的值</li>
	 *         </p>
	 * @param session
	 *            session
	 * @param sessionKey
	 *            session的键
	 * @param mapKey
	 *            session值的map的键
	 * @return 返回session值
	 */
	public static Object getSessionMapValue(HttpSession session,
			String sessionKey, String mapKey) {
		Params sessionMap = (Params) session.getAttribute(sessionKey);
		return CommonUtil.isNull(sessionMap) ? null : sessionMap.get(mapKey);
	}

	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年3月30日-下午12:43:17</li>
	 *         <li>功能说明：判断字符串是否包含中文</li>
	 *         </p>
	 * @param str	字符串
	 * @return
	 */
	public static boolean isContainChinese(String str) {
		if (isNull(str)) {
			return false;
		}
		char[] c = str.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] >= 0x4E00 && c[i] <= 0x29FA5) {
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年4月3日-下午2:44:42</li>
	 *         <li>功能说明：获取请求者的ip地址</li>
	 *         </p>
	 * @param request	http请求
	 * @return	返回真实ip地址
	 */
	public static String getIpAddress(HttpServletRequest request) {
		if(isNull(request)){
			return null;
		}
		String ip = request.getHeader("X-Forwarded-For");
		if (CommonUtil.isNotNull(ip) && !"unKnown".equalsIgnoreCase(ip)) {
			int index = ip.indexOf(",");
			if (index != -1) {
				return ip.substring(0, index);
			} else {
				return ip;
			}
		}
		ip = request.getHeader("X-Real-IP");
		if (CommonUtil.isNotNull(ip) && !"unKnown".equalsIgnoreCase(ip)) {
			return ip;
		}
		return request.getRemoteAddr();
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年4月3日-下午3:05:51</li>
	 *         <li>功能说明：获取当前系统时间</li>
	 *         </p>
	 * @return	返回当前系统时间,格式为yyyy-MM-dd HH:mm:ss.SSS
	 */
	public static String getCurSysTime(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		return sdf.format(new Date());
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年4月9日-下午7:57:53</li>
	 *         <li>功能说明：计算时间消耗，时间格式为 yyyy-MM-dd HH:mm:ss.SSS</li>
	 *         </p>
	 * @param beginTime 开始时间
	 * @param endTime	结束时间
	 * @return	返回消耗时长
	 */
	public static String calculateTimeConsume(String beginTime,String endTime){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		try{
			Date begin = sdf.parse(beginTime);
			Date end = sdf.parse(endTime);
			return String.valueOf(end.getTime() - begin.getTime());
		}catch(Exception e){
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年4月8日-下午3:54:56</li>
	 *         <li>功能说明：生成交易流水</li>
	 *         </p>
	 * @return	返回交易流水
	 */
	public synchronized static String buildTrxnSeq(int len){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		len = len - 17 >= 0 ? len - 17 : 0;
		return sdf.format(new Date()) + randStr(len,1);
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年4月16日-下午4:25:55</li>
	 *         <li>功能说明：向页面响应json类型的数据</li>
	 *         </p>
	 * @param response HttpServletResponse对象
	 * @param data	要响应的数据
	 */
	public static void responseJson(HttpServletResponse response,Object data){
		if(isNotNull(response)){
			response.setCharacterEncoding("UTF-8");  
			response.setContentType("application/json; charset=utf-8");
			JSONObject responseJSONObject = JSONObject.fromObject(data);
			PrintWriter out = null;  
		    try {  
		        out = response.getWriter();  
		        out.print(responseJSONObject.toString());
		    } catch (IOException e) {  
		    	logger.error(e.getMessage(), e);
		    } finally {  
		        if (out != null) {  
		            out.close();  
		        }  
		    }  
		}
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年4月18日-下午4:35:10</li>
	 *         <li>功能说明：判断是否为ajax请求</li>
	 *         </p>
	 * @param request HttpServletRequest请求
	 * @return
	 */
	public static boolean isAjax(HttpServletRequest request) {
		if(isNull(request)){
			return false;
		}else{
			return "XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With"));
		}
    }
	
	
	/**
	 * @Author sunline
	 *         <p>
	 *         <li>2019年4月19日-下午1:56:40</li>
	 *         <li>功能说明：重载比较两个值的大小，o1=o2返回0，o1>o2返回大于0的值，o1<o2返回小于0 的值</li>
	 *         </p>
	 * @param o1	待比较的第一个值
	 * @param o2	待比较的第二个值
	 * @return
	 */
	public static <T extends Comparable<? super T>> int compare(T o1, T o2){
		return compare(o1, o2, false, true);
	}
	
	
	/**
	 * @Author sunline
	 *         <p>
	 *         <li>2019年4月19日-下午1:53:13</li>
	 *         <li>功能说明：比较两个值的大小，o1=o2返回0，o1>o2返回大于0的值，o1<o2返回小于0 的值</li>
	 *         </p>
	 * @param o1	待比较的第一个值
	 * @param o2	待比较的第二个至
	 * @param ignoreCase	是否忽略大小写
	 * @param ignoreNullAndEmpty	是否忽略空对象
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Comparable<? super T>> int compare(T o1, T o2,
			boolean ignoreCase, boolean ignoreNullAndEmpty) {
		if (o1 == o2) {
			return 0;
		}
		if (o1 == null) {
			if ((ignoreNullAndEmpty) && (String.class.isAssignableFrom(o2.getClass())) && ("".equals(o2))) {
				return 0;
			}
			return -1;
		}
		if (o2 == null) {
			if ((ignoreNullAndEmpty) && (String.class.isAssignableFrom(o1.getClass())) && ("".equals(o1))) {
				return 0;
			}
			return 1;
		}
		if ((ignoreCase) && (String.class.isAssignableFrom(o1.getClass()))
				&& (String.class.isAssignableFrom(o2.getClass()))) {
			return ((String) o1).compareToIgnoreCase((String) o2);
		}

		if ((o1 != null) && (o1.getClass().isEnum())) {
			o1 = (T) String.valueOf(o1);
		}

		if ((o2 != null) && (o2.getClass().isEnum())) {
			o2 = (T) String.valueOf(o2);
		}

		return o1.compareTo(o2);
	}
	
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年4月23日-下午7:48:25</li>
	 *         <li>功能说明：判断字符串是否可以被转换为JSONObject</li>
	 *         </p>
	 * @param content
	 * @return
	 */
	public static boolean isJsonObject(String content) {
	    if(isNull(content))
	        return false;
	    try {
	        JSONObject.fromObject(content);
	        return true;
	    } catch (Exception e) {
	        return false;
	    }
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年4月23日-下午7:51:33</li>
	 *         <li>功能说明：判断字符串是否可以被转换为JSONArray</li>
	 *         </p>
	 * @param content
	 * @return
	 */
	public static boolean isJsonArray(String content) {
	    if(isNull(content))
	        return false;
	    try {
	        JSONArray.fromObject(content);
	        return true;
	    } catch (Exception e) {
	        return false;
	    }
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年4月24日-下午1:57:33</li>
	 *         <li>功能说明：判断类的类型是否是基础类型或String类型</li>
	 *         </p>
	 * @param clazz	对象的类
	 * @return	如果是返回该类型，如果不是返回null
	 */
	public static boolean isBaseClass(Class<?> clazz) {
		
		if(clazz.equals(Byte.class)
			|| clazz.equals(Short.class)
			|| clazz.equals(Integer.class)
			|| clazz.equals(Long.class)
			|| clazz.equals(Float.class)
			|| clazz.equals(Double.class)
			|| clazz.equals(Boolean.class)
			|| clazz.equals(Character.class)
			|| clazz.equals(String.class)){
			return true;
		}else{
			return false;
		}
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年4月24日-下午3:21:55</li>
	 *         <li>功能说明：获取泛型</li>
	 *         </p>
	 * @param type
	 * @return
	 */
	public static List<Class<?>> getGenericType(Type type){
		
		List<Class<?>> classList = new ArrayList<>();
		
		if(type instanceof ParameterizedType){
			ParameterizedType pType = (ParameterizedType)type;
			Type[] typeArr = pType.getActualTypeArguments();
            for(Type clazz : typeArr){
                if(clazz instanceof Class){
                   classList.add((Class<?>)clazz);
                }
            }
		}
		
		return classList;
	}
	
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年7月17日-下午3:12:47</li>
	 *         <li>功能说明：二分搜索</li>
	 *         </p>
	 * @param target	目标数据
	 * @param dataList	数据list
	 * @return
	 */
	public static <T extends Comparable<T>> boolean binarySearch(T target,List<T> dataList){
		if(isNull(target) || isNull(dataList)){
			return false;
		}else{
			Collections.sort(dataList);
			return binarySearch(target, dataList, 0, dataList.size() - 1);
		}
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年7月17日-下午3:13:10</li>
	 *         <li>功能说明：二分搜索</li>
	 *         </p>
	 * @param target 目标数据
	 * @param dataList	数据list
	 * @param low	数据list最低下标
	 * @param high	数据list最高下标
	 * @return
	 */
	private static <T extends Comparable<T>> boolean binarySearch(T target,List<T> dataList,int low,int high){
		if(isNull(target) || isNull(dataList)){
			return false;
		}else{
			T lowData = dataList.get(0);
			T highData = dataList.get(dataList.size() - 1);
			if(target.compareTo(lowData) < 0 || target.compareTo(highData) > 0 || low > high || low < 0 || high < 0){
				return false;
			}
			
			Integer middle = (low + high) / 2;
			if(target.compareTo(dataList.get(middle)) < 0){
				return binarySearch(target, dataList, low, middle - 1);
			}else if(target.compareTo(dataList.get(middle)) > 0){
				return binarySearch(target, dataList, middle + 1, high);
			}else{
				return true;
			}
		}
	}
	
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年7月20日-下午6:40:57</li>
	 *         <li>功能说明：通过log4j打印错误信息</li>
	 *         </p>
	 * @param e
	 * @param logger
	 */
	public static void printLogError(Exception e,Logger logger){
		logger.error(e);
		//表层错误堆栈
		String errorTrace = new String("\r\n");
		StackTraceElement[] stackTrace = e.getStackTrace();
		
		for(StackTraceElement trace : stackTrace){
			errorTrace += "\t" + trace + "\r\n";
		}
		logger.error(errorTrace);
		if(isNotNull(e.getCause()) && isNotNull(e.getCause().getStackTrace())){
			//底层错误堆栈
			logger.error("Caused by: " + e.getCause());
			errorTrace = new String("\r\n");
			
			stackTrace = e.getCause().getStackTrace();
			for(StackTraceElement trace : stackTrace){
				errorTrace += "\t" + trace + "\r\n";
			}
			logger.error(errorTrace);
		}
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年7月22日-下午4:38:30</li>
	 *         <li>功能说明：根据分隔符将字符串转list</li>
	 *         </p>
	 * @param sourceStr
	 * @param splitStr
	 * @return
	 */
	public static List<String> parseStringToList(String sourceStr,String splitStr){
		if(isNull(sourceStr) || isNull(splitStr)){
			throw new NullParmException("源字符串","分割字符串");
		}
		List<String> list = new ArrayList<>();
		StringTokenizer st = new StringTokenizer(sourceStr,splitStr);
		while(st.hasMoreTokens()){
			list.add(st.nextToken());
		}
		return list;
	}
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2020年4月16日-下午8:01:24</li>
	 *         <li>功能说明：根据分隔符将字符串转数组</li>
	 *         </p>
	 * @param sourceStr
	 * @param splitStr
	 * @return
	 */
	public static String[] parseStringToArray(String sourceStr,String splitStr){
		return parseStringToList(sourceStr, splitStr).toArray(new String[]{});
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年7月23日-下午2:55:51</li>
	 *         <li>功能说明：字段的拷贝，如果字段有值则不拷贝</li>
	 *         </p>
	 * @param dest
	 * @param src
	 */
	public static void copyProperties(Object dest, Object src){
		if(CommonUtil.isNull(dest) || CommonUtil.isNull(src)){
			throw new NullParmException("待拷贝对象","被拷贝对象");
		}else{
			Field[] destFileds = dest.getClass().getDeclaredFields();
			Field[] srcFields = src.getClass().getDeclaredFields();
			
			try{
				for(Field destField : destFileds){
					for(Field srcField : srcFields){
						if(srcField.getName().equals(destField.getName())
						&& srcField.getType().equals(destField.getType())){
							
							String destSetterMethodName = "set" + destField.getName().substring(0,1).toUpperCase() + destField.getName().substring(1);
							Method setter = dest.getClass().getMethod(destSetterMethodName,destField.getType());
							String destGetterMethodName = "get" + destField.getName().substring(0,1).toUpperCase() + destField.getName().substring(1);
							Method destGetter = dest.getClass().getMethod(destGetterMethodName);
							if(setter != null && CommonUtil.isNull(destGetter.invoke(dest))){
								String srcGetterMethodName = "get" + srcField.getName().substring(0,1).toUpperCase() + srcField.getName().substring(1);
								Method getter = src.getClass().getMethod(srcGetterMethodName);
								if(getter != null){
									setter.invoke(dest,getter.invoke(src));
								}
							}
						}
					}
				}
			}catch(NoSuchMethodException | SecurityException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e){
				printLogError(e, logger);
			}
		}
	}
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年7月29日-上午10:43:48</li>
	 *         <li>功能说明：获取配置的根节点</li>
	 *         </p>
	 * @param xmlPath
	 * @return
	 */
	public static Element getXmlRootElement(String xmlPath){
		try{
			SAXReader reader = new SAXReader();
			Document doc = reader.read(new File(xmlPath));
			return doc.getRootElement();
		}catch(Exception e){
			printLogError(e, logger);
			return null;
		}
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月7日-下午4:53:40</li>
	 *         <li>功能说明：从url中获取xml的根节点</li>
	 *         </p>
	 * @param url
	 * @return
	 */
	public static Element getUrlRootElement(String url){
		try{
			SAXReader reader = new SAXReader();
			Document doc = reader.read(url);
			return doc.getRootElement();
		}catch(Exception e){
			printLogError(e, logger);
			return null;
		}
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年7月30日-下午5:11:45</li>
	 *         <li>功能说明：获取配置的document</li>
	 *         </p>
	 * @param xmlPath
	 * @return
	 */
	public static Document getXmlDocument(String xmlPath){
		try{
			SAXReader reader = new SAXReader();
			Document doc = reader.read(new File(xmlPath));
			return doc;
		}catch(Exception e){
			printLogError(e, logger);
			return null;
		}
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年7月29日-下午4:27:45</li>
	 *         <li>功能说明：获取类型,可以是文件名,路径,引用等等</li>
	 *         </p>
	 * @param fileName
	 * @return
	 */
	public static String getRealType(String str){
		if(isNull(str) || !str.contains(".")){
			throw new NullParmException("目标字符串");
		}else{
			return str.substring(str.lastIndexOf(".") + 1);
		}
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年7月31日-上午9:41:44</li>
	 *         <li>功能说明：获取文件名,去除类型</li>
	 *         </p>
	 * @param str
	 * @return
	 */
	public static String getFileNameExceptType(String str){
		if(isNull(str) || !str.contains(".")){
			throw new NullParmException("目标字符串");
		}else{
			return str.substring(0,str.lastIndexOf("."));
		}
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月8日-下午3:51:06</li>
	 *         <li>功能说明：获取字符串中第一个小数点左边的内容</li>
	 *         </p>
	 * @param str
	 * @return
	 */
	public static String getFirstDotLeftStr(String str){
		if(isNull(str) || !str.contains(".")){
			throw new NullParmException("目标字符串");
		}else{
			return str.substring(0,str.indexOf("."));
		}
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年7月29日-下午4:39:59</li>
	 *         <li>功能说明：根据正则表达式检查输入字符串是否匹配</li>
	 *         </p>
	 * @param regex
	 * @param input
	 * @return
	 */
	public static boolean isRegexMatches(String regex,String input){
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(input);
		return matcher.matches();
	}
	
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年7月30日-下午4:14:37</li>
	 *         <li>功能说明：写入xml文件</li>
	 *         </p>
	 * @param root
	 * @param outputPath
	 */
	public static void writeDocumentXml(Document doc,String outputPath){
		if(isNull(doc) || isNull(outputPath)){
			throw new NullParmException("Document对象","格式化标识");
		}
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding("UTF-8");
		format.setIndent("\t");
		XMLWriter writer = null;
		
		try{
			File xmlFile = new File(outputPath);
			if(!xmlFile.exists()){
				xmlFile.createNewFile();
			}
			writer = new XMLWriter(new FileOutputStream(xmlFile), format);
		    writer.write(doc);  
			logger.info("输出文件:" + xmlFile.getName());
		}catch(Exception e){
			printLogError(e, logger);
		}finally{
			try {
				writer.flush();
				writer.close();
			}
			catch (IOException e) {
				printLogError(e, logger);
			}
		}
	}
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年7月31日-下午12:27:29</li>
	 *         <li>功能说明：系统暂停</li>
	 *         </p>
	 * @param millis
	 */
	public static void systemPause(long millis){
		try {
			Thread.sleep(millis);
		}
		catch (InterruptedException e) {
			printLogError(e, logger);
		}
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月1日-下午1:33:39</li>
	 *         <li>功能说明：读文件内容</li>
	 *         </p>
	 * @param filePath
	 * @return
	 */
	public static String readFileContent(String filePath){
		if(isNull(filePath)){
			throw new NullParmException("文件路径");
		}else{
			FileInputStream in = null;
			try{
				File file = new File(filePath);
				if(file.exists()){
					in = new FileInputStream(file);
					byte[] buffer = new byte[(int) file.length()];
					in.read(buffer);
					return new String(buffer, "utf-8");
				}
			}catch(Exception e){
				printLogError(e, logger);
			}finally{
				if(in != null){
					try {
						in.close();
					}
					catch (IOException e) {
						printLogError(e, logger);
					}
				}
			}
		}
		return new String();
	}
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2020年4月15日-下午4:25:36</li>
	 *         <li>功能说明：通过输入流读取文件内容</li>
	 *         </p>
	 * @param filePath
	 * @return
	 */
	public static String readFileContent(InputStream inputStream){
		try{
			return convertStreamToJson(inputStream);
		}catch(Exception e){
			printLogError(e, logger);
		}finally{
			if(inputStream != null){
				try {
					inputStream.close();
				}
				catch (IOException e) {
					printLogError(e, logger);
				}
			}
		}
		return null;
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2020年3月22日-下午8:01:40</li>
	 *         <li>功能说明：读取文件,以字节数组的形式返回</li>
	 *         </p>
	 * @param filePath
	 * @return
	 */
	public static byte[] readFile(String filePath){
		if(isNull(filePath)){
			throw new NullParmException("文件路径");
		}else{
			FileInputStream in = null;
			try{
				File file = new File(filePath);
				if(file.exists()){
					in = new FileInputStream(file);
					byte[] buffer = new byte[(int) file.length()];
					in.read(buffer);
					return buffer;
				}
			}catch(Exception e){
				printLogError(e, logger);
			}finally{
				if(in != null){
					try {
						in.close();
					}
					catch (IOException e) {
						printLogError(e, logger);
					}
				}
			}
		}
		return null;
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月7日-下午3:15:18</li>
	 *         <li>功能说明：写文件内容</li>
	 *         </p>
	 * @param contentList
	 * @param outputPath
	 */
	public static void writeFileContent(List<String> contentList,String outputPath){
		if(isNull(contentList) || isNull(outputPath)){
			throw new NullParmException("字符串数组列表","输出文件路径");
		}else{
			FileOutputStream out = null;
			try{
				File file = new File(outputPath);
				if(!file.exists()){
					file.createNewFile();
				}
				//构建字符串内容
				StringBuffer buffer = new StringBuffer();
				for(String str : contentList){
					buffer.append(str).append("\r\n");
				}
				out = new FileOutputStream(file);
				out.write(buffer.toString().getBytes("utf-8"));
				logger.info("输出文件:"+outputPath);
			}catch(Exception e){
				printLogError(e, logger);
			}finally{
				if(out != null){
					try {
						out.flush();
						out.close();
					}
					catch (IOException e) {
						printLogError(e, logger);
					}
				}
			}
		}
	}
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2020年3月22日-下午8:04:02</li>
	 *         <li>功能说明：写文件,以字节流的形式输出</li>
	 *         </p>
	 * @param buffer
	 * @param outputPath
	 */
	public static void writeFile(byte[] buffer,String outputPath){
		if(isNull(buffer) || isNull(outputPath)){
			throw new NullParmException("文件字节流","输出文件路径");
		}else{
			FileOutputStream out = null;
			try{
				File file = new File(outputPath);
				if(!file.exists()){
					file.createNewFile();
				}
				out = new FileOutputStream(file);
				out.write(buffer);
				logger.info("输出文件:"+outputPath);
			}catch(Exception e){
				printLogError(e, logger);
			}finally{
				if(out != null){
					try {
						out.flush();
						out.close();
					}
					catch (IOException e) {
						printLogError(e, logger);
					}
				}
			}
		}
	}
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月1日-下午1:34:18</li>
	 *         <li>功能说明：写文件内容</li>
	 *         </p>
	 * @param content
	 */
	public static void writeFileContent(String content,String outputPath){
		if(isNull(content) || isNull(outputPath)){
			throw new NullParmException("待写入字符串","输出文件路径");
		}else{
			FileOutputStream out = null;
			try{
				File file = new File(outputPath);
				if(!file.exists()){
					file.createNewFile();
				}
				out = new FileOutputStream(file);
				out.write(content.getBytes("utf-8"));
				logger.info("输出文件:"+outputPath);
			}catch(Exception e){
				printLogError(e, logger);
			}finally{
				if(out != null){
					try {
						out.flush();
						out.close();
					}
					catch (IOException e) {
						printLogError(e, logger);
					}
				}
			}
		}
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月1日-下午2:44:58</li>
	 *         <li>功能说明：打印分隔符</li>
	 *         </p>
	 * @param num
	 */
	public static void printSplitLine(int num){
		systemPause(5);
		logger.info(buildSplitLine(num));
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月16日-下午12:25:29</li>
	 *         <li>功能说明：获取分隔符</li>
	 *         </p>
	 * @param num
	 */
	public static String buildSplitLine(int num){
		String str = new String();
		if(num > 0){
			for(int i = 0;i < num;i++){
				str += "-";
			}
		}
		return str;
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月2日-下午18:05:36</li>
	 *         <li>功能说明：从url下载</li>
	 *         </p>
	 * @param url
	 * @param outputPath
	 * @throws IOException 
	 */
	public static void downloadUrl(String url,String outputPath) throws IOException{
		if(isNull(url) || isNull(outputPath)){
			throw new NullParmException("下载路径","输出文件路径");
		}else{
			BufferedInputStream in = null;
			FileOutputStream out = null;
			URL realurl = new URL(url);
			URLConnection connection = realurl.openConnection();
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			
			connection.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			connection.connect();
			in = new BufferedInputStream(connection.getInputStream());
			File file = new File(outputPath);
			
			if(!file.exists()){
				file.createNewFile();
			}
			out = new FileOutputStream(file);
			
			byte[] buffer = new byte[4096];
			Integer len = 0;
			while ((len = in.read(buffer)) != -1) {
				out.write(buffer,0,len);
			}
			
			//关闭资源
			if(in != null){
				in.close();
			}
			if(out != null){
				out.close();
			}
		}
	}
	
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月3日-上午11:42:55</li>
	 *         <li>功能说明：递归搜索xml元素节点,返回满足条件的第一个节点或元素</li>
	 *         </p>
	 * @param element
	 * @param elementName
	 * @param attrName
	 * @param attrValue
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Element searchXmlElement(Element element,String elementName,String attrName,String attrValue){
		if(isNull(elementName) || isNull(element)){
			throw new NullParmException("dom4j元素名称","dom4j元素对象");
		}else if((isNull(attrName) || isNull(attrValue)) && elementName.equals(element.getName())){
			return element;
		}else if(elementName.equals(element.getName()) && attrValue.equals(element.attributeValue(attrName))){
			return element;
		}else{
			List<Element> elementList = element.elements();
			if(isNotNull(elementList)){
				for(Element subElement : elementList){
					Element resultElement = searchXmlElement(subElement, elementName, attrName, attrValue);
					if(isNotNull(resultElement)){
						return resultElement;
					}
				}
			}
		}
		return null;
	}
	
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月7日-下午5:05:02</li>
	 *         <li>功能说明：归搜索xml元素节点,返回满足条件的第一个节点或元素</li>
	 *         </p>
	 * @param element
	 * @param elementName
	 * @return
	 */
	public static Element searchXmlElement(Element element,String elementName){
		return searchXmlElement(element, elementName, null, null);
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月3日-上午11:54:34</li>
	 *         <li>功能说明：递归搜索xml元素节点,返回满足条件的子节点列表</li>
	 *         </p>
	 * @param element
	 * @param elementName
	 * @return
	 */
	public static List<Element> searchXmlElementList(Element element,String elementName){
		return searchXmlElementList(element, elementName, null, null);
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月3日-下午12:01:48</li>
	 *         <li>功能说明：递归搜索xml元素节点,返回满足条件的节点的子节点列表</li>
	 *         </p>
	 * @param element
	 * @param elementName
	 * @param attrName
	 * @param attrValue
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<Element> searchXmlElementList(Element element,String elementName,String attrName,String attrValue){
		if(isNull(elementName) || isNull(element)){
			throw new NullParmException("dom4j元素名称","dom4j元素对象");
		}else if((isNull(attrName) || isNull(attrValue)) && elementName.equals(element.getName())){
			return element.elements();
		}else if(elementName.equals(element.getName()) && attrValue.equals(element.attributeValue(attrName))){
			return element.elements();
		}else{
			List<Element> elementList = element.elements();
			if(isNotNull(elementList)){
				for(Element subElement : elementList){
					List<Element> resultElementList = searchXmlElementList(subElement, elementName,attrName,attrValue);
					if(isNotNull(resultElementList)){
						return resultElementList;
					}
				}
			}
		}
		return null;
	}
	
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月21日-下午1:32:19</li>
	 *         <li>功能说明：查询根元素element下所有元素名为elementName的元素</li>
	 *         </p>
	 * @param element
	 * @param elementName
	 * @return
	 */
	public static List<Element> searchTargetAllXmlElement(Element element,String elementName){
		return searchTargetAllXmlElement(null, element, elementName);
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月21日-下午1:22:28</li>
	 *         <li>功能说明：查询根元素element下所有元素名为elementName的元素</li>
	 *         </p>
	 * @param elementList
	 * @param element
	 * @param elementName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static List<Element> searchTargetAllXmlElement(List<Element> list,Element element,String elementName){
		if(isNull(elementName) || isNull(element)){
			throw new NullParmException("dom4j元素名称","dom4j元素对象");
		}else if(isNull(list)){
			list = new LinkedList<Element>();
		}
		
		List<Element> elementList = element.elements();
		if(isNotNull(elementList)){
			for(Element e : elementList){
				if(e.getName().equals(elementName)){
					list.add(e);
				}else{
					list = searchTargetAllXmlElement(list, e, elementName);
				}
			}
		}
		return list;
	}
	
	
	/**
	 * 
	 * 递归压缩方法
	 * 
	 * @param sourceFile
	 *            源文件
	 * @param zos
	 *            zip输出流
	 * @param name
	 *            压缩后的名称
	 * @param KeepDirStructure
	 *            是否保留原来的目录结构,true:保留目录结构;
	 *            false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
	 * @throws Exception
	 */

	private static void compressCore(File sourceFile, ZipOutputStream zos,String sourceFileName, String zipFileName,boolean KeepDirStructure) throws Exception {
		
		byte[] buf = new byte[1024];
		if (sourceFile.isFile()) {
			// 向zip输出流中添加一个zip实体，构造器中name为zip实体的文件的名字
			zos.putNextEntry(new ZipEntry(sourceFileName));
			// copy文件到zip输出流中
			int len;
			FileInputStream in = new FileInputStream(sourceFile);
			while ((len = in.read(buf)) != -1) {
				zos.write(buf, 0, len);
			}
			zos.closeEntry();
			in.close();
		} else {
			File[] listFiles = sourceFile.listFiles();
			if (listFiles == null || listFiles.length == 0) {
				// 需要保留原来的文件结构时,需要对空文件夹进行处理
				if (KeepDirStructure) {
					zos.putNextEntry(new ZipEntry(sourceFileName + "/"));
					zos.closeEntry();
				}
			} else {
				for (File file : listFiles) {
					if(!file.getName().equals(zipFileName)){
						// 判断是否需要保留原来的文件结构
						if (KeepDirStructure) {
							compressCore(file, zos, sourceFileName + "/" + file.getName(),zipFileName,KeepDirStructure);
						} else {
							compressCore(file, zos, file.getName(),zipFileName,KeepDirStructure);
						}
					}
				}
			}
		}
	}


	/**
	 * 压缩目录或文件为zip
	 * @param srcDir
	 * @param zipDir
	 * @param KeepDirStructure
	 * @throws RuntimeException
	 */
	public static void toZip(String srcDir, String zipDir,boolean KeepDirStructure) throws RuntimeException {
		ZipOutputStream zos = null;
		try {
			File zipFile = new File(zipDir);
			zos = new ZipOutputStream(new FileOutputStream(zipFile));
			File sourceFile = new File(srcDir);
			compressCore(sourceFile, zos, getFileNameExceptType(zipFile.getName()),zipFile.getName(),KeepDirStructure);
		} catch (Exception e) {
			printLogError(e, logger);
		} finally {
			if (zos != null) {
				try {
					zos.close();
				} catch (IOException e) {
					printLogError(e, logger);
				}
			}
		}
	}
	
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月8日-上午10:33:14</li>
	 *         <li>功能说明：获取当前数据源的指定表的所有列的列名</li>
	 *         </p>
	 * @return
	 */
	public static List<String> getColumnNameList(ResultSet resultSet){
		List<String> colList = new ArrayList<String>();
		if(isNull(resultSet)){
			return colList;
		}else{
			try{
				ResultSetMetaData rsmd = resultSet.getMetaData();
				for(int i = 1;i <= rsmd.getColumnCount();i++){
					colList.add(rsmd.getColumnName(i).toLowerCase());
				}
			}catch(Exception e){
				printLogError(e, logger);
			}
		}
		return colList;
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月17日-上午11:56:49</li>
	 *         <li>功能说明：获取当前数据源的指定表的所有列的列名,以驼峰法表示</li>
	 *         </p>
	 * @param resultSet
	 * @return
	 */
	public static List<String> getColumnHumpNameList(ResultSet resultSet){
		List<String> colList = getColumnNameList(resultSet);
		List<String> humpColList = new ArrayList<String>();
		if(isNotNull(colList)){
			for(String col : colList){
				humpColList.add(parseHumpStr(col));
			}
		}
		return humpColList;
	}
	
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月17日-下午12:06:42</li>
	 *         <li>功能说明：字符串转驼峰法表示,例如:loan_no转驼峰法loanNo</li>
	 *         </p>
	 * @param str
	 * @return
	 */
	public static String parseHumpStr(String str){
		List<String> list = CommonUtil.parseStringToList(str.toLowerCase(), "_");
		StringBuffer buffer = new StringBuffer();
		for(String s : list){
			buffer.append(s.substring(0,1).toUpperCase()+s.substring(1,s.length()));
		}
		String field = buffer.toString();
		field = field.substring(0,1).toLowerCase()+field.substring(1,field.length());
		return field;
	}
	
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月8日-上午10:26:43</li>
	 *         <li>功能说明：解析ResultSet,将指定字段的数据组装成数组列表</li>
	 *         </p>
	 * @param set
	 * @param field
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> fetchResultSetToList(ResultSet resultSet,String field){
		List<T> dataList = new ArrayList<T>();
		if(isNotNull(resultSet) && isNotNull(field)){
			try {
				while(resultSet.next()){
					dataList.add((T) resultSet.getObject(field));
				}
			}
			catch (SQLException e) {
				printLogError(e, logger);
			}
		}
		return dataList;
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月17日-下午12:26:10</li>
	 *         <li>功能说明：获取类成员的字段名和类型</li>
	 *         </p>
	 * @param clazz
	 * @return
	 */
	public static Map<String,Class<?>> getClassFieldMap(Class<?> clazz){
		Map<String,Class<?>> fieldMap = new HashMap<String,Class<?>>();
		if(isNull(clazz)){
			return fieldMap;
		}
		
		Field[] fieldArr = clazz.getDeclaredFields();
		for(Field field : fieldArr){
			fieldMap.put(field.getName(), field.getType());
		}
		
		return fieldMap;
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月17日-下午12:51:17</li>
	 *         <li>功能说明：获取resultSet中记录的条数</li>
	 *         </p>
	 * @param resultSet
	 * @return
	 */
	public static int getResultSetRecordNum(ResultSet resultSet){
		try {
			resultSet.last();  
			int rowCount = resultSet.getRow();
			resultSet.beforeFirst();
			return rowCount;
		}
		catch (SQLException e) {
			printLogError(e, logger);
		}
		return 0;
	}
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2020年3月4日-下午8:47:41</li>
	 *         <li>功能说明：映射ResultSet结果到单个实体类</li>
	 *         </p>
	 * @param resultSet
	 * @param objClass
	 * @return
	 */
	public static <T> T mappingResultSetSingle(ResultSet resultSet,Class<T> objClass){
		if(isNull(resultSet) || isNull(objClass)){
			throw new NullParmException("ResultSet对象","待映射的对象类");
		}else if(getResultSetRecordNum(resultSet) > 1){
			throw new TooManyResultsException("The query returned too many results, it must be one");
		}
		
		try {
			T obj = null;
			Map<String, Class<?>> fieldMap = getClassFieldMap(objClass);
			List<String> colList = getColumnNameList(resultSet);
			
			if(resultSet.next()){
				obj = setObjFieldMapping(resultSet, objClass, fieldMap, colList);
			}
			return obj;
		}
		catch (Exception e) {
			printLogError(e, logger);
		}finally{
			JDBCUtils.close();
		}
		return null;
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2020年3月4日-下午8:49:31</li>
	 *         <li>功能说明：映射ResultSet结果到列表</li>
	 *         </p>
	 * @param resultSet
	 * @param objClass
	 * @return
	 */
	public static <T> List<T> mappingResultSetList(ResultSet resultSet,Class<T> objClass){
		if(isNull(resultSet) || isNull(objClass)){
			throw new NullParmException("ResultSet对象","待映射的对象类");
		}
		
		try {
			List<T> resList = new LinkedList<T>();
			T obj = null;
			Map<String, Class<?>> fieldMap = getClassFieldMap(objClass);
			List<String> colList = getColumnNameList(resultSet);
			
			while(resultSet.next()){
				obj = setObjFieldMapping(resultSet, objClass, fieldMap, colList);
				resList.add(obj);
			}
			return resList;
		}
		catch (Exception e) {
			printLogError(e, logger);
		}finally{
			JDBCUtils.close();
		}
		return null;
	}


	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2020年3月4日-下午8:52:05</li>
	 *         <li>功能说明：从ResultSet设置对象属性</li>
	 *         </p>
	 * @param resultSet
	 * @param objClass
	 * @param fieldMap
	 * @param colList
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws SQLException
	 */
	private static <T> T setObjFieldMapping(ResultSet resultSet, Class<T> objClass, Map<String, Class<?>> fieldMap, List<String> colList) throws InstantiationException,
			IllegalAccessException, NoSuchMethodException, InvocationTargetException, SQLException {
		T obj = objClass.newInstance();
		for(String col : colList){
			String fieldName = parseHumpStr(col);
			Class<?> fieldClazz = fieldMap.get(fieldName);
			if(isNotNull(fieldClazz)){
				Method setter = objClass.getMethod("set"+fieldName.substring(0,1).toUpperCase()+fieldName.substring(1), fieldClazz);
				setter.invoke(obj, resultSet.getObject(col));
			}
		}
		return obj;
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年8月19日-下午1:31:22</li>
	 *         <li>功能说明：从ResultSet中获取第一行指定字段的值</li>
	 *         </p>
	 * @param set
	 * @param fieldName
	 * @return
	 */
	public static String fetchResultSetValue(ResultSet resultSet,String fieldName){
		if(isNull(resultSet) || isNull(fieldName)){
			throw new NullParmException("ResultSet对象","字段名称");
		}
		try{
			if(resultSet.next()){
				String value = resultSet.getString(fieldName);;
				resultSet.beforeFirst();
				return value;
			}
		}catch(Exception e){
			printLogError(e, logger);
		}finally{
			JDBCUtils.close();
		}
		return null;
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年9月6日-下午1:56:42</li>
	 *         <li>功能说明：将properties文件里的配置读取成Map</li>
	 *         </p>
	 * @param settingPath
	 * @return
	 */
	public static Map<String, String> readPropertiesSettings(String settingPath) {
		Map<String, String> configMap = new LinkedHashMap<>();
		if(isNotNull(settingPath)){
			InputStream in = null;
			try{
				in = new FileInputStream(settingPath);
				Properties pt = new Properties();
				pt.load(in);
				
				for(Object key : pt.keySet()){
					configMap.put(String.valueOf(key), pt.getProperty(String.valueOf(key)));
				}
			}catch(Exception e){
				printLogError(e, logger);
			}finally{
				if(in != null){
					try {
						in.close();
					}
					catch (IOException e) {
						printLogError(e, logger);
					}
				}
			}
		}
		return configMap;
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年9月6日-下午2:14:13</li>
	 *         <li>功能说明：将ResultSet解析为map</li>
	 *         </p>
	 * @param resultSet
	 * @return
	 */
	public static List<Map<String, Object>> resolveResultSetToList(ResultSet resultSet){
		List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		if(isNotNull(resultSet)){
			List<String> colList = getColumnNameList(resultSet);
			try {
				while(resultSet.next()){
					Map<String, Object> resultMap = new LinkedHashMap<>();
					
					for(String colName : colList){
						resultMap.put(colName, resultSet.getObject(colName));
					}
					resultList.add(resultMap);
				}
			}
			catch (SQLException e) {
				printLogError(e, logger);
			}
		}
		return resultList;
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年9月6日-下午2:47:08</li>
	 *         <li>功能说明：将输入流转换为json</li>
	 *         </p>
	 * @param inputStream
	 * @return
	 */
	public static String convertStreamToJson(InputStream inputStream) {
		BufferedReader reader;
		try {
			reader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
			StringBuilder sb = new StringBuilder();
			String line = null;
			try {
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return sb.toString();
		} catch (Exception e) {
			printLogError(e, logger);
			return "{}";
		}
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年9月7日-下午1:52:29</li>
	 *         <li>功能说明：获取反射方法类</li>
	 *         </p>
	 * @param clazz	对象的类
	 * @param methodName	方法名称
	 * @param args	参数类型
	 * @return
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	public static Method getReflectMethod(Class<?> clazz,String methodName,Class<?>... args) throws NoSuchMethodException, SecurityException{
		if(isNull(clazz) || isNull(methodName)){
			throw new NullParmException("方法所在的类","方法名称");
		}
		return clazz.getMethod(methodName, args);
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年9月7日-下午3:06:34</li>
	 *         <li>功能说明：多线程执行任务</li>
	 *         </p>
	 * @param method	方法对象
	 * @param concurrentNum	并发数
	 * @param timeout 超时毫秒数,0为不设置超时时间
	 * @param args	方法实参
	 * @return
	 * @throws InterruptedException 
	 */
	public static Map<String, Object> multithreadingExecute(final Method method,int concurrentNum,long timeoutMillis,final Object... args) throws InterruptedException{
		//合法性校验
		if(isNull(method)){
			throw new NullParmException("Method对象");
		}
		//形参与实参数量不匹配
		else if(method.getParameterCount() != args.length){
			throw new IllegalArgumentException("The number of forms participating in the actual parameter does not match");
		}
		
		//无返回值标志
		final boolean noReturnInd = "void".equals(method.getReturnType().getName());
		//返回结果列表
		final Map<String, Object> returnObjMap = new HashMap<String, Object>();
		
		//执行成功线程返回结果列表
		final List<Object> successThreadReturnList = new LinkedList<Object>();
		//执行失败线程返回结果列表
		final List<Object> errorThreadReturnList = new LinkedList<Object>();
		
		//定义请求线程池
		ExecutorService threadPool = Executors.newFixedThreadPool(ApiConst.MAX_THREAD_CONCURRENT);
		List<FutureTask<Object>> futureTaskList = new ArrayList<FutureTask<Object>>();
		
		logger.info("并发任务["+ method.getName() +"]启动,并发数:" + concurrentNum + ",入参:" + Arrays.toString(args));
		//启动线程
		for(int i = 0;i < concurrentNum;i++){
			FutureTask<Object> futureTask = new FutureTask<Object>(new Callable<Object>() {

				@Override
				public Object call() {
					
					Object returnObj = null;
					String curThread = Thread.currentThread().toString();
					logger.info("线程[" + curThread + "]开始执行");
					long start = System.currentTimeMillis();
					boolean successInd = false;
					
					try {
						returnObj = method.invoke(method.getDeclaringClass().newInstance(), args);
						successInd = true;
					}catch (Exception e) {
						returnObj = e.getCause().getMessage();
					}
					
					logger.info("线程[" + Thread.currentThread() + "]执行完成,耗时:" + (System.currentTimeMillis() - start) + "ms");
					
					//设置线程执行结果,如果方法的返回值类型为void,则使用默认返回类型和值
					if(successInd){
						if(noReturnInd){
							returnObj = ApiConst.CALLABLE_SUCCESS_RETURN;
						}
						successThreadReturnList.add(returnObj);
					}else{
						if(noReturnInd){
							returnObj = ApiConst.CALLABLE_ERROR_RETURN;
						}
						errorThreadReturnList.add(returnObj);
					}
					
					returnObjMap.put(curThread, returnObj);
					return returnObj;
				}
			});
			futureTaskList.add(futureTask);
			threadPool.submit(futureTask);
		}
		printSplitLine(120);
		
		//等待线程执行完成或超时
		boolean isComplete = awaitThreadPoolFinish(threadPool, timeoutMillis);
		if(isComplete){
			logger.info("并发任务["+ method.getName() +"]执行完成,作业总数:" + concurrentNum + ",成功作业数:" + successThreadReturnList.size() + ",失败作业数:" + errorThreadReturnList.size());
		}else{
			logger.error("并发任务["+ method.getName() +"]执行超时");
		}
		
		printSplitLine(120);
		return returnObjMap;
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年9月9日-上午10:58:16</li>
	 *         <li>功能说明：谷歌翻译</li>
	 *         </p>
	 * @param content	要翻译的内容
	 * @param from	源内容语种
	 * @param to	目的语种
	 * @return
	 * @throws Exception 
	 */
	public static String googleTranslate(String content,E_LANGUAGE from,E_LANGUAGE to) throws Exception{
		if(isNull(content) || isNull(from) || isNull(to)){
			throw new NullParmException("待翻译字符串","源语种","目标语种");
		}
		//谷歌翻译接口
		String host = "http://translate.google.cn/";
		String path = "/translate_a/single";
		
		//查询参数
		Map<String, String> querys = new HashMap<String,String>();
		querys.put("client", "gtx");
		querys.put("sl", from.getId());
		querys.put("tl", to.getId());
		querys.put("dt", "t");
		querys.put("q", content);
		
		HttpResponse response = NetworkApi.doGet(host, path, new HashMap<String,String>(),querys);
		HttpEntity httpEntity = response.getEntity();
		if (httpEntity != null) {
			InputStream instreams = httpEntity.getContent();
			String json = convertStreamToJson(instreams);
			
			//解析翻译结果
			JSONArray jsonArray = JSONArray.fromObject(json);
			return String.valueOf(jsonArray.getJSONArray(0).getJSONArray(0).get(0));
		}
		return null;
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年11月1日-下午2:01:26</li>
	 *         <li>功能说明：根据ResultSet生成inset语句</li>
	 *         </p>
	 * @param resultSet
	 * @return
	 * @throws Exception 
	 */
	public static List<String> generateInsertSQL(ResultSet resultSet) throws Exception{
		List<String> insertSqlList = new ArrayList<String>();
		if(CommonUtil.isNotNull(resultSet)){
			String tableName = resultSet.getMetaData().getTableName(1);
			List<String> colNameList = getColumnNameList(resultSet);
			List<Map<String, Object>> dataList = resolveResultSetToList(resultSet);

			//生成列字段
			String colField = new String("(");
			for(String colName : colNameList){
				colField += colName + ", ";
			}
			colField = colField.substring(0, colField.lastIndexOf(", ")) + ")";
			
			
			for(Map<String, Object> rowMap : dataList){
				String insertSql = new String("insert into " + tableName + colField + " values (");
				for(String key : rowMap.keySet()){
					Object value = rowMap.get(key);
					if(null == value){
						insertSql += "NULL, ";
					}else{
						insertSql += "'"+value+"', ";
					}
				}
				insertSql = insertSql.substring(0, insertSql.lastIndexOf(", ")) + ");";
				insertSqlList.add(insertSql);
			}
		}
		return insertSqlList;
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2020年1月3日-下午2:10:51</li>
	 *         <li>功能说明：对map进行排序</li>
	 *         </p>
	 * @param map	map
	 * @param comparator	比较器,通过重写比较器的compare方法决定对键或值排序
	 * @return
	 */
	public static <K,V> Map<K, V> sortMap(Map<K, V> map, Comparator<Entry<K, V>> comparator) {
		if(CommonUtil.isNull(map)){
			return map;
		}
		Map<K, V> sortedMap = new LinkedHashMap<>();
		List<Entry<K, V>> entryList = new ArrayList<>(map.entrySet());
		Collections.sort(entryList, comparator);
		Iterator<Entry<K, V>> iterator = entryList.iterator();

		while(iterator.hasNext()){
			Entry<K, V> entry = iterator.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2020年1月14日-下午2:05:43</li>
	 *         <li>功能说明：等待线程池的所有线程执行完成</li>
	 *         </p>
	 * @param threadPool
	 * @param timeout
	 * @return 正常完成返回true,超时返回false
	 */
	public static boolean awaitThreadPoolFinish(ExecutorService threadPool, long timeout){
		threadPool.shutdown();
		long start = System.currentTimeMillis();
		while(!threadPool.isTerminated()){
			if(timeout != 0 && (System.currentTimeMillis() - start) > timeout){
				threadPool.shutdownNow();
				return false;
			}
		}
		return true;
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2020年3月2日-上午10:56:25</li>
	 *         <li>功能说明：</li>
	 *         </p>
	 * @param response	HttpResponse转字符串
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	public static String convertResponseToStr(HttpResponse response) throws IllegalStateException, IOException{
		HttpEntity httpEntity = response.getEntity();
		if (httpEntity != null) {
			InputStream instreams = httpEntity.getContent();
			return convertStreamToJson(instreams);
		}
		return null;
	}
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2020年3月6日-下午3:55:24</li>
	 *         <li>功能说明：加载路径下的所有文件</li>
	 *         </p>
	 * @param path	目录路径
	 * @return	返回以文件名为key,文件路径为value的map
	 */
	public static Map<String, File> loadPathAllFiles(String path){
		Map<String, File> fileMap = new LinkedHashMap<>();
		loadPathAllFilesSub(new File(path), fileMap);
		return fileMap;
	}
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2020年3月6日-下午3:57:55</li>
	 *         <li>功能说明：加载路径下的所有文件(递归子方法)</li>
	 *         </p>
	 * @param file
	 * @param fileMap
	 */
	private static void loadPathAllFilesSub(File file, Map<String, File> fileMap){
		if (file.isDirectory()) {
			File[] list = file.listFiles();
			for (int i = 0; isNotNull(list) && i < list.length; i++) {
				loadPathAllFilesSub(list[i], fileMap);
			}
		} else {
			fileMap.put(file.getName(), file);
		}
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2020年3月11日-下午5:14:31</li>
	 *         <li>功能说明：fastjson美化</li>
	 *         </p>
	 * @param jsonString
	 * @return
	 */
	public static String fastjsonFormat(String jsonString) {
		if(isJsonArray(jsonString)){
			com.alibaba.fastjson.JSONArray fastjsonArray = com.alibaba.fastjson.JSONArray.parseArray(jsonString);
		    return JSON.toJSONString(fastjsonArray, true);
		}else{
			com.alibaba.fastjson.JSONObject fastjsonObj = com.alibaba.fastjson.JSONObject.parseObject(jsonString, Feature.OrderedField);
		    return JSON.toJSONString(fastjsonObj, true);
		}
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2020年3月16日-下午3:58:08</li>
	 *         <li>功能说明：搜索json中所有key=attrKey,value=attrValue的json对象</li>
	 *         </p>
	 * @param jsonList
	 * @param jsonObj
	 * @param attrKey
	 * @param attrValue
	 * @return
	 */
	public static List<JSONObject> searchJsonAllTargetEqAttr(List<JSONObject> jsonList, JSONObject jsonObj, String attrKey, String attrValue){
		jsonList = CommonUtil.nvl(jsonList, new LinkedList<JSONObject>());
		for(Object jsonKey : jsonObj.keySet()){
			try{
				JSONObject jsonValue = jsonObj.getJSONObject(String.valueOf(jsonKey));
				if(jsonValue.containsKey(attrKey) && jsonValue.getString(attrKey).equals(attrValue)){
					jsonList.add(jsonValue);
				}else{
					jsonList = searchJsonAllTargetEqAttr(jsonList, jsonValue, attrKey, attrValue);
				}
			}catch(Exception e1){
				try{
					JSONArray jsonValue = jsonObj.getJSONArray(String.valueOf(jsonKey));
					for(int i = 0;i < jsonValue.size();i++){
						jsonList = searchJsonAllTargetEqAttr(jsonList, jsonValue.getJSONObject(i), attrKey, attrValue);
					}
				}catch(Exception e2){
					//普通键值对
				}
			}
		}
		return jsonList;
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2020年3月17日-下午1:51:22</li>
	 *         <li>功能说明：根据数据库中的表生成java类</li>
	 *         </p>
	 * @param javaPackage
	 * @param tableName
	 * @param dataSource
	 * @param outputPath
	 * @throws SQLException
	 */
	public static void generateTableJava(String javaPackage, String tableName, String dataSource, String outputPath) throws SQLException {
		final String nextLine = "\r\n\r\n";
		final String tabSpace = "\t";
		StringBuffer fieldBuffer = new StringBuffer();
		StringBuffer getsetBuffer = new StringBuffer();
		
		StringBuffer toStringBuffer = new StringBuffer();
		ResultSet resultSet = JDBCUtils.executeQuery("select * from " + tableName, dataSource);
		ResultSetMetaData metaData = resultSet.getMetaData();
		int colCount = metaData.getColumnCount();
		
		tableName = CommonUtil.parseHumpStr(tableName);
		tableName = tableName.substring(0, 1).toUpperCase() + tableName.substring(1);
		fieldBuffer.append("package ").append(javaPackage).append(";").append(nextLine).append(nextLine).append("public class ").append(tableName).append(" {").append(nextLine);
		toStringBuffer.append(tabSpace).append("@Override\r\n\tpublic String toString() {\r\n\t\treturn \"").append(tableName).append(" [");

		for(int i = 1;i <= colCount;i++){
			//定义字段
			fieldBuffer.append(tabSpace).append("private").append(" ").append(CommonUtil.getRealType(metaData.getColumnClassName(i))).append(" ").append(CommonUtil.parseHumpStr(metaData.getColumnName(i))).append(";").append(nextLine);
			//定义get和set方法
			String fieldName = CommonUtil.parseHumpStr(metaData.getColumnName(i));
			String fieldType = CommonUtil.getRealType(metaData.getColumnClassName(i));
			getsetBuffer.append(tabSpace).append("public").append(" ").append(fieldType).append(" ").append("get").append(fieldName.substring(0, 1).toUpperCase()).append(fieldName.substring(1)).append("() {").append("\r\n");
			getsetBuffer.append(tabSpace).append(tabSpace).append("return ").append(fieldName).append(";").append("\r\n\t}").append(nextLine);
		
			getsetBuffer.append(tabSpace).append("public").append(" void ").append("set").append(fieldName.substring(0, 1).toUpperCase()).append(fieldName.substring(1)).append("(").append(fieldType).append(" ").append(fieldName).append(") {\r\n");
			getsetBuffer.append(tabSpace).append(tabSpace).append("this.").append(fieldName).append(" = ").append(fieldName).append(";\r\n\t}").append(nextLine);
			//定义toString方法
			toStringBuffer.append(fieldName).append("=\" + ").append(fieldName).append(" + \", ");
		}
		toStringBuffer = toStringBuffer.replace(toStringBuffer.lastIndexOf(", "), toStringBuffer.lastIndexOf(", ") + 2, "");
		toStringBuffer.append("]\";\r\n\t}\r\n}");
		String javaFileContent = fieldBuffer.append(getsetBuffer).append(toStringBuffer).toString();
		writeFileContent(javaFileContent, outputPath);
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2020年4月15日-下午7:23:45</li>
	 *         <li>功能说明：获取两个值中较大的一个</li>
	 *         </p>
	 * @param a
	 * @param b
	 * @return
	 */
	public static <T extends Comparable<T>> T getMax(T a, T b){
		return compare(a, b) > 0 ? a : b;
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2020年4月15日-下午7:24:05</li>
	 *         <li>功能说明：获取两个值中较小的一个</li>
	 *         </p>
	 * @param a
	 * @param b
	 * @return
	 */
	public static <T extends Comparable<T>> T getMin(T a, T b){
		return compare(a, b) < 0 ? a : b;
	}
	
	public static void encrypt(String path){
		Map<String, File> fileMap = CommonUtil.loadPathAllFiles(path);
		for(String fileName : fileMap.keySet()){
			byte[] buffer = CommonUtil.readFile(fileMap.get(fileName).getPath());
			for(int i = 0;i < buffer.length;i++){
				buffer[i] = (byte) (buffer[i] ^ 981130 ^ 971213);
			}
			writeFile(buffer, fileMap.get(fileName).getPath());
		}
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2020年5月12日-上午11:06:27</li>
	 *         <li>功能说明：检查对象是否为空,如果为空则抛出异常</li>
	 *         </p>
	 * @param obj	对象
	 * @param objDesc	对象描述
	 */
	public static void checkObjIsNull(Object obj, String objDesc){
		if(isNull(obj)){
			throw new RuntimeException("[" + objDesc + "]不存在");
		}
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2020年5月21日-下午3:10:34</li>
	 *         <li>功能说明：执行mybatis生成代码</li>
	 *         </p>
	 * @param confPath
	 * @throws Exception
	 */
	public static void mybatisGeneratorProcess(String confPath, boolean overwrite) throws Exception{
		List<String> warnings = new ArrayList<String>();
        File configFile = new File(confPath);
        
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(configFile);
        DefaultShellCallback callback = new DefaultShellCallback(overwrite);
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
        myBatisGenerator.generate(null);
	}
}
