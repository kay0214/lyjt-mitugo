package co.yixiang.modules.couponUse.param;

import co.yixiang.common.web.param.QueryParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserUpdateParam extends QueryParam {
    private String oldPass;
    private  String newPass;
    private String okNewPass;
}
