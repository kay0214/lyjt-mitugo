package co.yixiang.modules.couponUse.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 客服留言表 查询结果对象
 * </p>
 *
 * @author lsy
 * @date 2020-11-04
 */
@Data
@ApiModel(value = "YxLeaveMessageVo对象", description = "客服留言表查询参数")
public class YxLeaveMessageVo implements Serializable {

    @ApiModelProperty(value = "id")
    private Integer id;

    @ApiModelProperty(value = "主键关联id")
    private Integer linkId;

    @ApiModelProperty(value = "商户id")
    private Integer merId;

    @ApiModelProperty(value = "联系人")
    private String userName;

    @ApiModelProperty(value = "电话")
    private String userPhone;

    @ApiModelProperty(value = "留言信息")
    private String message;

    @ApiModelProperty(value = "状态：0 -> 待处理，1 -> 已处理，2 -> 不予处理")
    private Integer status;

    @ApiModelProperty(value = "处理时间")
    private Integer takeTime;

    @ApiModelProperty(value = "留言类型：0 -> 商品，1-> 卡券 2 -> 商城订单，3 -> 本地生活订单，4 ->商户，5 -> 平台")
    private Integer messageType;

    @ApiModelProperty(value = "备注")
    private String remark;

    /*@ApiModelProperty(value = "是否删除（0：未删除，1：已删除）")
    private Integer delFlag;

    @ApiModelProperty(value = "创建人")
    private Integer createUserId;*/

    @ApiModelProperty(value = "修改人")
    private Integer updateUserId;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "处理时间（显示）")
    private String takeTimeStr;
    @ApiModelProperty(value = "提交时间（显示）")
    private String createTimeStr;
    /*@ApiModelProperty(value = "更新时间")
    private Date updateTime;*/
}