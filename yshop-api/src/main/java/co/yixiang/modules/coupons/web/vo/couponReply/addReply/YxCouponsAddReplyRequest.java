/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package co.yixiang.modules.coupons.web.vo.couponReply.addReply;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author PC-LIUSHOUYI
 * @version YxCouponsAddReplyRequest, v0.1 2020/11/12 14:55
 */
@Data
public class YxCouponsAddReplyRequest {

    @ApiModelProperty(value = "用户ID")
    private Integer uid;

    @ApiModelProperty(value = "订单ID")
    private String oid;

    @ApiModelProperty(value = "总体感觉")
    private Integer generalScore;

    @ApiModelProperty(value = "评论内容")
    private String comment;

    @ApiModelProperty(value = "评论图片")
    private List<String> images;
}
