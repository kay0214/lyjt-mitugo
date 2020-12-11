package co.yixiang.common.exception;

import co.yixiang.common.constant.SYSConstant;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 业务异常
 *
 * @author framework
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BusinessException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private Integer status = SYSConstant.DEFAULT_FAILURE_CODE;

    public BusinessException(String msg){
        super(msg);
    }

    public BusinessException(Integer status, String msg){
        super(msg);
        this.status = status;
    }
}
