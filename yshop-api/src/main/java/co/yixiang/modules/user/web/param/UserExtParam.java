package co.yixiang.modules.user.web.param;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @ClassName UserExtParam
 * @Author hupeng <610796224@qq.com>
 * @Date 2019/11/13
 **/
@Data
public class UserExtParam implements Serializable {

    //支付宝用户名
    private String alipayCode;

    private String extractType;

    @NotBlank(message = "金额不能为空")
    private String money;

    //微信号
    private String weixin;

    //支付宝账号
    private String name;

    // 真实姓名
    private String realName;

    // 银行预留手机号
    private String phone;

    // 银行卡号
    private String bankNo;

    // 联行号
    // @NotBlank(message = "联行号不能为空")
    private String cnapsCode;
}
