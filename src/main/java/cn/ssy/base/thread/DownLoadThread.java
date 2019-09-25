package cn.ssy.base.thread;

import java.io.IOException;
import java.util.concurrent.Callable;

import org.apache.log4j.Logger;

import cn.ssy.base.core.utils.CommonUtil;

/**
 * <p>
 * 文件功能说明：
 *       	下载线程		
 * </p>
 * 
 * @Author sunshaoyu
 *         <p>
 *         <li>2019年8月14日-下午2:17:59</li>
 *         <li>修改记录</li>
 *         <li>-----------------------------------------------------------</li>
 *         <li>标记：修订内容</li>
 *         <li>2019年8月14日-sunshaoyu：创建注释模板</li>
 *         <li>-----------------------------------------------------------</li>
 *         </p>
 */
public class DownLoadThread implements Callable<Boolean>{
	
	private String url;
	
	private String outputPath;
	
	private static final Logger logger = Logger.getLogger(DownLoadThread.class);
	
	public DownLoadThread(String url, String outputPath) {
		super();
		this.url = url;
		this.outputPath = outputPath;
	}

	@Override
	public synchronized Boolean call() {
		try {
			logger.info("下载:" + url);
			CommonUtil.downloadUrl(url, outputPath);
			return true;
		}catch (IOException e) {
			logger.error("资源["+url+"]下载失败");
			CommonUtil.printLogError(e, logger);
			return false;
		}
	}

}
