package cn.ssy.base.core.network.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.log4j.Logger;

import cn.ssy.base.core.network.NetworkCore;


/**
 * 
 * <p>
 * 文件功能说明：
 *       	网络请求相关处理api接口
 * </p>
 * 
 * @Author sunshaoyu
 *         <p>
 *         <li>2019年3月19日-上午9:23:03</li>
 *         <li>修改记录</li>
 *         <li>-----------------------------------------------------------</li>
 *         <li>标记：修订内容</li>
 *         <li>2019年3月19日-sunshaoyu：创建注释模板</li>
 *         <li>-----------------------------------------------------------</li>
 *         </p>
 */
public class NetworkApi extends NetworkCore{
	
	private static final Logger logger = Logger.getLogger(NetworkApi.class);
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年3月19日-上午9:24:35</li>
	 *         <li>功能说明：发送get请求</li>
	 *         </p>
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static String sendGet(String url) {
		String responseContent = new String();
		try{
			responseContent = NetworkCore.sendGet(url);
		}catch(IOException e){
			logger.error("Send get request failed", e);
		}
		return responseContent;
	}
	
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年3月19日-上午9:25:26</li>
	 *         <li>功能说明：下载url中的内容</li>
	 *         </p>
	 * @param filePath
	 *            下载保存路径
	 * @param url
	 *            下载链接
	 * @return	返回请求内容
	 */
	public static void downloadUrl(String filePath, String url) {
		try{
			NetworkCore.downloadUrl(filePath, url);
		}catch(IOException e){
			logger.error("Download url content failed", e);
		}
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年3月19日-上午10:02:23</li>
	 *         <li>功能说明：发送http get请求</li>
	 *         </p>
	 * @param host
	 *            请求主机
	 * @param path
	 *            请求路径
	 * @param headers
	 *            请求头
	 * @param querys
	 *            请求参数
	 * @return	返回http响应体
	 * @throws Exception
	 */
	public static HttpResponse doGet(String host, String path,
			Map<String, String> headers, Map<String, String> querys){
		HttpResponse response = null;
		try{
			response = NetworkCore.doGet(host, path, headers, querys);
		}catch(Exception e){
			logger.error("Send get request failed", e);
		}
		return response;
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年3月19日-上午10:03:12</li>
	 *         <li>功能说明：发送http post请求</li>
	 *         </p>
	 * @param host	请求主机
	 * @param path	请求路径
	 * @param headers	请求头
	 * @param querys	请求参数
	 * @param body	请求体
	 * @return	返回http响应体
	 * @throws Exception
	 */
	public static HttpResponse doPost(String host, String path, Map<String, String> headers, Map<String, String> querys, String body) {
		HttpResponse response = null;
		try{
			response = NetworkCore.doPost(host, path, headers, querys, body);
		}catch(Exception e){
			logger.error("Send post request failed", e);
		}
		return response;
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年3月19日-上午10:04:01</li>
	 *         <li>功能说明：发送http put请求</li>
	 *         </p>
	 * @param host	请求主机
	 * @param path	请求路径
	 * @param headers	请求头
	 * @param querys	请求参数
	 * @param body	请求体
	 * @return	返回http响应体
	 * @throws Exception
	 */
	public static HttpResponse doPut(String host, String path,
			Map<String, String> headers, Map<String, String> querys, String body) {
		HttpResponse response = null;
		try{
			response = NetworkCore.doPut(host, path, headers, querys, body);
		}catch(Exception e){
			logger.error("Send put request failed", e);
		}
		return response;
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年3月19日-上午10:05:33</li>
	 *         <li>功能说明：发送http delete请求</li>
	 *         </p>
	 * @param host	请求主机
	 * @param path	请求路径
	 * @param headers	请求头
	 * @param querys	请求参数
	 * @return	返回http响应体
	 * @throws Exception
	 */
	public static HttpResponse doDelete(String host, String path, Map<String, String> headers,
			Map<String, String> querys) {
		HttpResponse response = null;
		try{
			response = NetworkCore.doDelete(host, path, headers, querys);
		}catch(Exception e){
			logger.error("Send delete request failed", e);
		}
		return response;
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年3月19日-上午10:06:22</li>
	 *         <li>功能说明：构建http请求的url</li>
	 *         </p>
	 * @param host	请求主机
	 * @param path	请求路径
	 * @param querys	请求参数
	 * @return	返回构建后的url
	 * @throws UnsupportedEncodingException
	 */
	public static String buildUrl(String host, String path,
			Map<String, String> querys) {
		String url = new String();
		try{
			url = NetworkCore.buildUrl(host, path, querys);
		}catch(Exception e){
			logger.error("Build url failed", e);
		}
		return url;
	}
	
	
	/**
	 * 
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年3月19日-上午10:06:55</li>
	 *         <li>功能说明：输入流转字符串</li>
	 *         </p>
	 * @param inputStream	输入流
	 * @return	返回转换后的字符串
	 */
	public static String convertStreamToString(InputStream inputStream) {
		String str = new String();
		try{
			str = NetworkCore.convertStreamToString(inputStream);
		}catch(Exception e){
			logger.error("Convert input stream to string failed", e);
		}
		return str;
	}
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年3月19日-下午1:29:16</li>
	 *         <li>功能说明：解析响应内容</li>
	 *         </p>
	 * @param httpResponse	http响应体
	 * @return	返回解析后的字符串,编码格式为utf-8
	 * @throws Exception
	 */
	public static String resolveResponse(HttpResponse httpResponse) {
		String responseStr = new String();
		try{
			responseStr = NetworkCore.resolveResponse(httpResponse);
		}catch(Exception e){
			logger.error("Parsing response content failed", e);
		}
		return responseStr;
	}


	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年3月20日-上午9:29:06</li>
	 *         <li>功能说明：获取编码集</li>
	 *         </p>
	 * @return	返回编码集
	 */
	public static String getHttpCharacterEncode() {
		return NetworkCore.getHttpCharacterEncode();
	}


	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年3月20日-上午9:29:20</li>
	 *         <li>功能说明：设置编码集</li>
	 *         </p>
	 * @param httpCharacterEncode	编码格式
	 */
	public static void setHttpCharacterEncode(String httpCharacterEncode) {
		NetworkCore.setHttpCharacterEncode(httpCharacterEncode);
	}
	
}
