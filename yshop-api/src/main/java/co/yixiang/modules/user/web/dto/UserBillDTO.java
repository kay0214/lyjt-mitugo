package co.yixiang.modules.user.web.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @ClassName UserBillDTO
 * @Author hupeng <610796224@qq.com>
 * @Date 2019/11/12
 **/
@Data
public class UserBillDTO {
    @ApiModelProperty(value = "分佣时间")
    private String addTime;
    @ApiModelProperty(value = "账单标题")
    private String title;
    @ApiModelProperty(value = "明细数字")
    private BigDecimal number;
    @ApiModelProperty(value = "0 = 支出 1 = 获得")
    private Integer pm;
}
