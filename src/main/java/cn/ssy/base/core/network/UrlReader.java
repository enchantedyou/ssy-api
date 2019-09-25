package cn.ssy.base.core.network;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * 
 * <p>
 * 文件功能说明： 对某些网址的内容进行读取或下载的处理包
 * </p>
 * 
 * @Author sunshaoyu
 *         <p>
 *         <li>2019年3月7日-下午5:03:49</li>
 *         <li>修改记录</li>
 *         <li>-----------------------------------------------------------</li>
 *         <li>标记：修订内容</li>
 *         <li>2019年3月7日-sunshaoyu：创建注释模板</li>
 *         <li>-----------------------------------------------------------</li>
 *         </p>
 */
public class UrlReader {
	
	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年3月11日-下午13:20:21</li>
	 *         <li>功能说明：发送get请求</li>
	 *         </p>
	 *         </p>
	 * @param url
	 *            请求链接
	 * @return	返回请求内容
	 */
	static String sendGet(String url) throws IOException {
		
		if(null == url)
			throw new NullPointerException();
		
		String result = "";
		BufferedReader reader = null;
		URL realurl = new URL(url);
		URLConnection connection = realurl.openConnection();
		
		connection.setRequestProperty("accept", "*/*");
		connection.setRequestProperty("connection", "Keep-Alive");
		connection.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
		connection.connect();
		
		reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
		String line;
		while ((line = reader.readLine()) != null) {
			result += line;
		}
		
		reader.close();
		reader = null;
		
		return result;
	}

	
	/**
	 * @Author sunshaoyu
	 *         <p>
	 *         <li>2019年3月11日-下午13:21:10</li>
	 *         <li>功能说明：下载</li>
	 *         </p>
	 *         </p>
	 * @param filePath
	 *            下载保存路径
	 * @param url
	 *            下载链接
	 * @return	返回请求内容
	 */
	static void downloadUrl(String filePath, String url) throws IOException {
		
		BufferedInputStream in = null;
		FileOutputStream out = null;
		URL realurl = new URL(url);
		
		URLConnection connection = realurl.openConnection();
		connection.setRequestProperty("accept", "*/*");
		connection.setRequestProperty("connection", "Keep-Alive");
		connection.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
		connection.connect();

		in = new BufferedInputStream(connection.getInputStream());
		out = new FileOutputStream(new File(filePath));

		byte[] buffer = new byte[4096];
		Integer len = 0;
		while ((len = in.read(buffer)) != -1) {
			out.write(buffer, 0, len);
		}
		
		in.close();
		out.close();
		in = null;
		out = null;
	}
}
