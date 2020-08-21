package co.yixiang.modules.user.web.param;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @ClassName AddressParam
 * @Author hupeng <610796224@qq.com>
 * @Date 2019/10/28
 **/
@Data
public class AddressParam  implements Serializable {
    private String id;
    @NotBlank(message = "收货人姓名不可为空")
    private String real_name;
    private String post_code;
    private String is_default;
    @NotBlank(message = "详细地址不能为空")
    private String detail;
    @NotBlank(message = "联系电话不能为空")
    private String phone;
    private AddressDetailParam address;
}
