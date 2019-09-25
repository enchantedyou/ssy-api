package cn.ssy.base.enums;


/**
 * <p>
 * 文件功能说明：
 *       	语言种类枚举		
 * </p>
 * 
 * @Author sunshaoyu
 *         <p>
 *         <li>2019年9月9日-上午10:51:37</li>
 *         <li>修改记录</li>
 *         <li>-----------------------------------------------------------</li>
 *         <li>标记：修订内容</li>
 *         <li>2019年9月9日-sunshaoyu：创建注释模板</li>
 *         <li>-----------------------------------------------------------</li>
 *         </p>
 */
public enum E_LANGUAGE {
	
	ZHCN("zh-CN","中文简体"),
	
	ZHTW("zh-TW","中文繁体"),
	
	JAPEN("ja","日语"),
	
	EN("en","英语");
	
	private String id;
	
	private String desc;
	
	private E_LANGUAGE(String id, String desc) {
		this.id = id;
		this.desc = desc;
	}

	public String getId() {
		return id;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public void setId(String id) {
		this.id = id;
	}
}
