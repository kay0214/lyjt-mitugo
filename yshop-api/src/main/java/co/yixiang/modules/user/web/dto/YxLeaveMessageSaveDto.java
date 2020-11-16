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
public class YxLeaveMessageSaveDto implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键关联id")
    private Integer linkId;

//    @ApiModelProperty(value = "订单号")
//    private String orderId;

    @ApiModelProperty(value = "商户id")
    private Integer merId;

    @ApiModelProperty(value = "联系人")
    private String userName;

    @ApiModelProperty(value = "电话")
    private String userPhone;

    @ApiModelProperty(value = "留言信息")
    private String message;

//    @ApiModelProperty(value = "状态：0 -> 待处理，1 -> 已处理，2 -> 不予处理")
//    private Integer status;

    @ApiModelProperty(value = "留言类型：0 -> 商品，1-> 卡券 2 -> 商城订单，3 -> 本地生活订单，4 ->商户，5 -> 平台")
    private Integer messageType;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建人")
    private Integer createUserId;
}