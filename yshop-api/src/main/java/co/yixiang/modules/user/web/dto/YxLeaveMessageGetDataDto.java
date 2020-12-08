package co.yixiang.modules.user.web.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 客服留言表 查询结果对象
 * </p>
 *
 * @author lsy
 * @date 2020-11-04
 */
@Data
@ApiModel(value = "YxLeaveMessageQueryVo对象", description = "客服留言表查询参数")
public class YxLeaveMessageGetDataDto implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "头像")
    private String imageUrl;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "订单号")
    private String orderNo;
}