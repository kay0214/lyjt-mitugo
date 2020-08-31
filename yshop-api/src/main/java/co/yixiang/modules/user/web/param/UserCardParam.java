package co.yixiang.modules.user.web.param;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 银行卡操作
 */
@Data
public class UserCardParam implements Serializable {

    @NotBlank(message = "请填写银行卡号")
    private String bankNo;
    @NotBlank(message = "请填写真实姓名")
    private String realName;
    @NotBlank(message = "请填写银行预留手机号")
    private String bankMobile;

}
