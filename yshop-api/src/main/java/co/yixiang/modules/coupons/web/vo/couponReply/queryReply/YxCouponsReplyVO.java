package co.yixiang.modules.coupons.web.vo.couponReply.queryReply;

import co.yixiang.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 本地生活评论表
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "YxCouponsReply对象", description = "本地生活评论表")
public class YxCouponsReplyVO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "评论ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "用户ID")
    private Integer uid;

    @ApiModelProperty(value = "用户昵称")
    private String nickname;

    @ApiModelProperty(value = "用户头像")
    private String avatar;

    @ApiModelProperty(value = "订单ID")
    private String oid;

    @ApiModelProperty(value = "卡券id")
    private Integer couponId;

    @ApiModelProperty(value = "总体感觉")
    private Integer generalScore;

    @ApiModelProperty(value = "评论内容")
    private String comment;

    @ApiModelProperty(value = "评论时间")
    private String addTimeStr;

    @ApiModelProperty(value = "管理员回复时间")
    private String merchantReplyTimeStr;

    @ApiModelProperty(value = "0：未回复，1：已回复")
    private Integer isReply;

    @ApiModelProperty(value = "管理员回复内容")
    private String merchantReplyContent;
}
