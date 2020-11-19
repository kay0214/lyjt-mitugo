package co.yixiang.exception.response;

import java.io.Serializable;

/**
 * 业务代码接口
 *
 * @author framework
 */
public interface IResultCode extends Serializable {

	/**
	 * 消息
	 *
	 * @return String
	 */
	String getStatusDesc();

	/**
	 * 状态码
	 *
	 * @return int
	 */
	int getStatus();

}
