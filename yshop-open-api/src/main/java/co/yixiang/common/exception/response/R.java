package co.yixiang.common.exception.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 统一API响应结果封装
 *
 * @author Chill
 */
@Getter
@Setter
@ToString
@ApiModel(description = "返回信息")
@NoArgsConstructor
public class R<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "状态码", required = true)
	private int status;

	@ApiModelProperty(value = "返回消息", required = true)
	private String message;

	private R(IResultCode resultCode) {
		this(resultCode, resultCode.getStatusDesc());
	}

	private R(IResultCode resultCode,String msg) {
		this(resultCode.getStatus(), msg);
	}

	private R(int status,  String message) {
		this.status = status;
		this.message = message;
	}

	/**
	 * 返回R
	 *
	 * @param resultCode 业务代码
	 * @param <T>        T 泛型标记
	 * @return R
	 */
	public static <T> R<T> success(IResultCode resultCode) {
		return new R<>(resultCode);
	}

	/**
	 * 返回R
	 *
	 * @param resultCode 业务代码
	 * @param message        消息
	 * @param <T>        T 泛型标记
	 * @return R
	 */
	public static <T> R<T> success(IResultCode resultCode, String message) {
		return new R<>(resultCode, message);
	}

	/**
	 * 返回R
	 *
	 * @param status 状态码
	 * @param message  消息
	 * @param <T>  T 泛型标记
	 * @return R
	 */
	public static <T> R<T> fail(int  status, String message) {
		return new R<>( status, message);
	}

	/**
	 * 返回R
	 *
	 * @param resultCode 业务代码
	 * @param <T>        T 泛型标记
	 * @return R
	 */
	public static <T> R<T> fail(IResultCode resultCode) {
		return new R<>(resultCode);
	}

}
