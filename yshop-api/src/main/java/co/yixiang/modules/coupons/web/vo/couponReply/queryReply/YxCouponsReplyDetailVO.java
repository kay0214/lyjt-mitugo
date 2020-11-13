/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package co.yixiang.modules.coupons.web.vo.couponReply.queryReply;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author PC-LIUSHOUYI
 * @version YxCouponsReplyDetailVO, v0.1 2020/11/13 9:23
 */
@Data
public class YxCouponsReplyDetailVO {

    @ApiModelProperty(value = "评价总件数")
    private Integer replyCount;

    @ApiModelProperty(value = "评价详情")
    private List<YxCouponsReplyVO> replyList;

    @ApiModelProperty(value = "好评率")
    private String goodRate;

    @ApiModelProperty(value = "好评数量")
    private Integer goodReplyCount;

    @ApiModelProperty(value = "中评数量")
    private Integer midReplyCount;

    @ApiModelProperty(value = "差评数量")
    private Integer badReplyCount;
}
