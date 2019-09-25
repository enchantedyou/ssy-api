package cn.ssy.base.core.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.apache.http.HttpResponse;

/**
 * 
 * <p>
 * 文件功能说明：
 *       	网络请求相关处理核心封装	
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
public class NetworkCore {

	
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
	protected static String sendGet(String url) throws IOException {
		return UrlReader.sendGet(url);
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
	protected static void downloadUrl(String filePath, String url) throws IOException {
		UrlReader.downloadUrl(filePath, url);
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
	protected static HttpResponse doGet(String host, String path,
			Map<String, String> headers, Map<String, String> querys)
			throws Exception {
		return HttpUtils.doGet(host, path, headers, querys);
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
	protected static HttpResponse doPost(String host, String path, Map<String, String> headers, Map<String, String> querys, String body)
			throws Exception {
		return HttpUtils.doPost(host, path, headers, querys, body);
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
	protected static HttpResponse doPut(String host, String path,
			Map<String, String> headers, Map<String, String> querys, String body)
			throws Exception {
		return HttpUtils.doPut(host, path, headers, querys, body);
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
	protected static HttpResponse doDelete(String host, String path, Map<String, String> headers,
			Map<String, String> querys) throws Exception {
		return HttpUtils.doDelete(host, path, headers, querys);
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
	protected static String buildUrl(String host, String path,
			Map<String, String> querys) throws UnsupportedEncodingException {
		return HttpUtils.buildUrl(host, path, querys);
	}
	
	
	/**
	 * 
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年3月19日-上午10:06:55</li>
	 *         <li>功能说明：输入流转json字符串</li>
	 *         </p>
	 * @param inputStream	输入流
	 * @return	返回json字符串
	 */
	protected static String convertStreamToString(InputStream inputStream) throws Exception{
		return HttpUtils.convertStreamToString(inputStream);
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
	protected static String resolveResponse(HttpResponse httpResponse) throws Exception{
		return HttpUtils.resolveResponse(httpResponse);
	}


	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年3月20日-上午9:29:06</li>
	 *         <li>功能说明：获取编码集</li>
	 *         </p>
	 * @return	返回编码集
	 */
	protected static String getHttpCharacterEncode() {
		return HttpUtils.getHttpCharacterEncode();
	}


	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年3月20日-上午9:29:20</li>
	 *         <li>功能说明：设置编码集</li>
	 *         </p>
	 * @param httpCharacterEncode	编码格式
	 */
	protected static void setHttpCharacterEncode(String httpCharacterEncode) {
		HttpUtils.setHttpCharacterEncode(httpCharacterEncode);
	}
}
