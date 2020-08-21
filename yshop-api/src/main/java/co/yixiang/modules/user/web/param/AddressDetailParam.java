package co.yixiang.modules.user.web.param;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @ClassName AddressDetailParam
 * @Author hupeng <610796224@qq.com>
 * @Date 2019/10/28
 **/
@Data
public class AddressDetailParam  implements Serializable {

    @NotBlank(message = "城市不可为空")
    private String city;
    @NotBlank(message = "区县不可为空")
    private String district;
    @NotBlank(message = "所在省不可为空")
    private String province;
}
