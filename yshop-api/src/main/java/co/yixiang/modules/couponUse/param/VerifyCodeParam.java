package co.yixiang.modules.couponUse.param;

import co.yixiang.common.web.param.QueryParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class VerifyCodeParam extends QueryParam {
    private String verifyCode;
    private Integer shipId;
    private Integer shipUserId;
}
