package cn.ssy.base.core.network;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * <p>
 * 文件功能说明： http请求相关处理工具
 * </p>
 * 
 * @Author sunshaoyu
 *         <p>
 *         <li>2019年3月19日-上午9:40:28</li>
 *         <li>修改记录</li>
 *         <li>-----------------------------------------------------------</li>
 *         <li>标记：修订内容</li>
 *         <li>2019年3月19日-sunshaoyu：创建注释模板</li>
 *         <li>-----------------------------------------------------------</li>
 *         </p>
 */
public class HttpUtils {
	
	/**
	 * http字符编码
	 */
	private static String httpCharacterEncode = "utf-8";

	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年3月19日-上午9:40:53</li>
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
	static HttpResponse doGet(String host, String path,
			Map<String, String> headers, Map<String, String> querys)
			throws Exception {
		HttpClient httpClient = wrapClient(host);

		HttpGet request = new HttpGet(buildUrl(host, path, querys));
		for (Map.Entry<String, String> e : headers.entrySet()) {
			request.addHeader(e.getKey(), e.getValue());
		}

		return httpClient.execute(request);
	}

	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年3月19日-上午9:51:34</li>
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
	static HttpResponse doPost(String host, String path, Map<String, String> headers, Map<String, String> querys, String body)
			throws Exception {
		HttpClient httpClient = wrapClient(host);

		HttpPost request = new HttpPost(buildUrl(host, path, querys));
		for (Map.Entry<String, String> e : headers.entrySet()) {
			request.addHeader(e.getKey(), e.getValue());
		}

		if (StringUtils.isNotBlank(body)) {
			request.setEntity(new StringEntity(body, httpCharacterEncode));
		}

		return httpClient.execute(request);
	}

	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年3月19日-上午9:52:49</li>
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
	static HttpResponse doPut(String host, String path,
			Map<String, String> headers, Map<String, String> querys, String body)
			throws Exception {
		HttpClient httpClient = wrapClient(host);

		HttpPut request = new HttpPut(buildUrl(host, path, querys));
		for (Map.Entry<String, String> e : headers.entrySet()) {
			request.addHeader(e.getKey(), e.getValue());
		}

		if (StringUtils.isNotBlank(body)) {
			request.setEntity(new StringEntity(body, httpCharacterEncode));
		}

		return httpClient.execute(request);
	}


	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年3月19日-上午9:56:21</li>
	 *         <li>功能说明：发送http delete请求</li>
	 *         </p>
	 * @param host	请求主机
	 * @param path	请求路径
	 * @param headers	请求头
	 * @param querys	请求参数
	 * @return	返回http响应体
	 * @throws Exception
	 */
	static HttpResponse doDelete(String host, String path, Map<String, String> headers,
			Map<String, String> querys) throws Exception {
		HttpClient httpClient = wrapClient(host);

		HttpDelete request = new HttpDelete(buildUrl(host, path, querys));
		for (Map.Entry<String, String> e : headers.entrySet()) {
			request.addHeader(e.getKey(), e.getValue());
		}

		return httpClient.execute(request);
	}

	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年3月19日-上午9:57:35</li>
	 *         <li>功能说明：构建http请求的url</li>
	 *         </p>
	 * @param host	请求主机
	 * @param path	请求路径
	 * @param querys	请求参数
	 * @return	返回构建后的url
	 * @throws UnsupportedEncodingException
	 */
	static String buildUrl(String host, String path,
			Map<String, String> querys) throws UnsupportedEncodingException {
		StringBuilder sbUrl = new StringBuilder();
		sbUrl.append(host);
		if (!StringUtils.isBlank(path)) {
			sbUrl.append(path);
		}
		if (null != querys) {
			StringBuilder sbQuery = new StringBuilder();
			for (Map.Entry<String, String> query : querys.entrySet()) {
				if (0 < sbQuery.length()) {
					sbQuery.append("&");
				}
				if (StringUtils.isBlank(query.getKey())
						&& !StringUtils.isBlank(query.getValue())) {
					sbQuery.append(query.getValue());
				}
				if (!StringUtils.isBlank(query.getKey())) {
					sbQuery.append(query.getKey());
					if (!StringUtils.isBlank(query.getValue())) {
						sbQuery.append("=");
						sbQuery.append(URLEncoder.encode(query.getValue(),
								httpCharacterEncode));
					}
				}
			}
			if (0 < sbQuery.length()) {
				sbUrl.append("?").append(sbQuery);
			}
		}

		return sbUrl.toString();
	}
	
	
	
	/**
	 * 
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年3月19日-上午10:00:49</li>
	 *         <li>功能说明：输入流转字符串</li>
	 *         </p>
	 * @param inputStream	输入流
	 * @return	返回字符串
	 */
	static String convertStreamToString(InputStream inputStream) throws Exception{
		BufferedReader reader;
		reader = new BufferedReader(new InputStreamReader(inputStream, httpCharacterEncode));
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			sb.append(line + "\n");
		}
		reader.close();
		return sb.toString();
	}
	
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年3月19日-下午1:28:26</li>
	 *         <li>功能说明：解析响应内容</li>
	 *         </p>
	 * @param httpResponse	http响应体
	 * @return	返回解析后的字符串,编码格式为utf-8
	 * @throws Exception
	 */
	static String resolveResponse(HttpResponse httpResponse) throws Exception{
		String responseStr = new String();
		HttpEntity httpEntity = httpResponse.getEntity();
		if(null != httpEntity){
			InputStream instream = httpEntity.getContent();
			responseStr = convertStreamToString(instream);
		}
		return responseStr;
	}

	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年3月19日-上午9:58:55</li>
	 *         <li>功能说明：封装http请求客户端</li>
	 *         </p>
	 * @param host	请求主机
	 * @return	返回http请求客户端
	 */
	private static HttpClient wrapClient(String host) {
		HttpClient httpClient = new DefaultHttpClient();
		if (host.startsWith("https://")) {
			sslClient(httpClient);
		}
		return httpClient;
	}
	
	
	/**
	 * 
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年3月20日-上午9:24:51</li>
	 *         <li>功能说明：获取http字符编码</li>
	 *         </p>
	 * @return	返回http字符编码
	 */
	static String getHttpCharacterEncode() {
		return httpCharacterEncode;
	}


	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年3月20日-上午9:26:42</li>
	 *         <li>功能说明：设置http字符编码</li>
	 *         </p>
	 * @param httpCharacterEncode	编码格式
	 */
	static void setHttpCharacterEncode(String httpCharacterEncode) {
		if(null == httpCharacterEncode || httpCharacterEncode.length() == 0){
			httpCharacterEncode = "utf-8";
		}else{
			HttpUtils.httpCharacterEncode = httpCharacterEncode;
		}
	}


	@SuppressWarnings("deprecation")
	private static void sslClient(HttpClient httpClient) {
		try {
			SSLContext ctx = SSLContext.getInstance("TLS");
			X509TrustManager tm = new X509TrustManager() {
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkClientTrusted(X509Certificate[] xcs, String str) {

				}

				public void checkServerTrusted(X509Certificate[] xcs, String str) {

				}
			};
			ctx.init(null, new TrustManager[] { tm }, null);
			SSLSocketFactory ssf = new SSLSocketFactory(ctx);
			ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			ClientConnectionManager ccm = httpClient.getConnectionManager();
			SchemeRegistry registry = ccm.getSchemeRegistry();
			registry.register(new Scheme("https", 443, ssf));
		} catch (KeyManagementException ex) {
			throw new RuntimeException(ex);
		} catch (NoSuchAlgorithmException ex) {
			throw new RuntimeException(ex);
		}
	}
}