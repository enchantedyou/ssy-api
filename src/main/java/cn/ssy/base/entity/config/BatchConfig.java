package cn.ssy.base.entity.config;

import java.util.HashMap;
import java.util.Map;

import cn.ssy.base.enums.E_ICOREMODULE;


/**
 * <p>
 * 文件功能说明：
 *       	批量配置		
 * </p>
 * 
 * @Author sunshaoyu
 *         <p>
 *         <li>2020年5月20日-下午3:04:03</li>
 *         <li>修改记录</li>
 *         <li>-----------------------------------------------------------</li>
 *         <li>标记：修订内容</li>
 *         <li>2020年5月20日-sunshaoyu：创建注释模板</li>
 *         <li>-----------------------------------------------------------</li>
 *         </p>
 */
public class BatchConfig {
	
	private BatchSubConfig ln;
	private BatchSubConfig dp;
	private Map<E_ICOREMODULE, BatchSubConfig> subConfigMap = new HashMap<E_ICOREMODULE, BatchSubConfig>();

	public BatchConfig(BatchSubConfig ln, BatchSubConfig dp) {
		super();
		this.ln = ln;
		this.dp = dp;
	}
	
	public BatchConfig(){
		
	}

	public void setLn(BatchSubConfig ln) {
		this.ln = ln;
	}

	public void setDp(BatchSubConfig dp) {
		this.dp = dp;
	}
	
	public void loadBatConfigMap(){
		subConfigMap.put(E_ICOREMODULE.LN, ln);
		subConfigMap.put(E_ICOREMODULE.DP, dp);
	}
	
	public BatchSubConfig getBatchConfig(E_ICOREMODULE module) {
		loadBatConfigMap();
		return subConfigMap.get(module);
	}
	
	public BatchSubConfig getEnableBatchConfig() {
		loadBatConfigMap();
		BatchSubConfig subConfig = null;
		
		for(E_ICOREMODULE module : subConfigMap.keySet()){
			if(subConfigMap.get(module).isEnable()){
				if(subConfig != null){
					throw new RuntimeException("The effective batch configuration must be unique");
				}else{
					subConfig = subConfigMap.get(module);
				}
			}
		}
		return subConfig;
	}

	@Override
	public String toString() {
		return "BatchConfig [ln=" + ln + ", dp=" + dp + "]";
	}
}
