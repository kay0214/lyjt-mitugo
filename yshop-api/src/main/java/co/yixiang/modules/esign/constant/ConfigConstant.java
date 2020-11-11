package co.yixiang.modules.esign.constant;

import com.timevale.tech.sdk.constants.AlgorithmType;
import com.timevale.tech.sdk.constants.HttpType;

/**
 * description 常用配置常量类
 * @author lsy
 * datetime 2019年7月1日上午10:13:57
 */
public class ConfigConstant {
	
	//协议类型（可选HTTP或HTTPS)                                                                        
	public static final HttpType HTTP_TYPE = HttpType.HTTP;
	
	//算法类型（可选HMACSHA256/RSA，推荐使用HMACSHA256)
	public static final AlgorithmType ALGORITHM_TYPE = AlgorithmType.HMACSHA256;
	
	//e签宝公钥，可从开放平台获取，若算法类型为RSA，此项必填
	public static final String ESIGN_PUB_KEY = null;
	
	//e签宝私钥，可从开放平台下载密钥生成工具生成，若算法为RSA，此项必填
	public static final String ESIGN_PRI_KEY = null;
	
	//当前悟空SDK版本号
	public static final String TECH_SDK_VERSION = "2.1.5";

}
