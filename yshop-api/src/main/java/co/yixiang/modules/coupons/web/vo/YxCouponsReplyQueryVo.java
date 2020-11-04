package co.yixiang.modules.coupons.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;

import java.util.Date;

/**
 * <p>
 * 本地生活评论表 查询结果对象
 * </p>
 *
 * @author lsy
 * @date 2020-11-04
 */
@Data
@ApiModel(value="YxCouponsReplyQueryVo对象", description="本地生活评论表查询参数")
public class YxCouponsReplyQueryVo implements Serializable{
    private static final long serialVersionUID = 1L;

@ApiModelProperty(value = "评论ID")
private Integer id;

@ApiModelProperty(value = "用户ID")
private Integer uid;

@ApiModelProperty(value = "订单ID")
private Integer oid;

@ApiModelProperty(value = "卡券id")
private Integer couponId;

@ApiModelProperty(value = "总体感觉")
private Integer generalScore;

@ApiModelProperty(value = "评论内容")
private String comment;

@ApiModelProperty(value = "评论时间")
private Integer addTime;

@ApiModelProperty(value = "管理员回复时间")
private Integer merchantReplyTime;

@ApiModelProperty(value = "0：未回复，1：已回复")
private Integer isReply;

@ApiModelProperty(value = "商户id")
private Integer merId;

@ApiModelProperty(value = "管理员回复内容")
private String merchantReplyContent;

@ApiModelProperty(value = "是否删除（0：未删除，1：已删除）")
private Integer delFlag;

@ApiModelProperty(value = "创建人")
private Integer createUserId;

@ApiModelProperty(value = "修改人")
private Integer updateUserId;

@ApiModelProperty(value = "创建时间")
private Date createTime;

@ApiModelProperty(value = "更新时间")
private Date updateTime;

}