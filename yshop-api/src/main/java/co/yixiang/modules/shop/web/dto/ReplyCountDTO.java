package co.yixiang.modules.shop.web.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName ReplyCount
 * @Author hupeng <610796224@qq.com>
 * @Date 2019/11/4
 **/
@Data
public class ReplyCountDTO implements Serializable {
    @ApiModelProperty(value = "评价总数")
    private Integer sumCount;
    @ApiModelProperty(value = "好评数量")
    private Integer goodCount;
    @ApiModelProperty(value = "中评数量")
    private Integer inCount;
    @ApiModelProperty(value = "差评数量")
    private Integer poorCount;
    @ApiModelProperty(value = "好评率")
    private String replyChance;
    @ApiModelProperty(value = "好评率（星号）")
    private String replyStar;

}
