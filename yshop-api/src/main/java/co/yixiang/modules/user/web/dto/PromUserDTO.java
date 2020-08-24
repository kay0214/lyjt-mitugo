package co.yixiang.modules.user.web.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName PromUserDTO
 * @Author hupeng <610796224@qq.com>
 * @Date 2019/11/12
 **/
@Data
public class PromUserDTO {
    @ApiModelProperty(value = "头像地址")
    private String avatar;
    @ApiModelProperty(value = "昵称")
    private String nickname;
    @ApiModelProperty(value = "下级人数")
    private Integer childCount;
    @ApiModelProperty(value = "订单总金额")
    private Integer numberCount;
    @ApiModelProperty(value = "订单数量")
    private Integer orderCount;
    @ApiModelProperty(value = "uid")
    private Integer uid;
    @ApiModelProperty(value = "注册时间")
    private String time;
}
