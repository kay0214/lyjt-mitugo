package co.yixiang.modules.user.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;

import java.util.Date;

/**
 * <p>
 * 常用联系人表 查询结果对象
 * </p>
 *
 * @author lsy
 * @date 2020-11-04
 */
@Data
@ApiModel(value="YxUsedContactsQueryVo对象", description="常用联系人表查询参数")
public class YxUsedContactsQueryVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private Integer id;

    @ApiModelProperty(value = "所属用户id")
    private Integer userId;

    @ApiModelProperty(value = "姓名")
    private String userName;

    @ApiModelProperty(value = "用户电话")
    private String userPhone;

    @ApiModelProperty(value = "身份证号码")
    private String cardId;

    @ApiModelProperty(value = "用户类别：0 -> 12岁以下,1 -> 12及岁以上")
    private Integer userType;

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
    @ApiModelProperty(value = "状态：0:未成年 1:成年人 2：老年人")
    private Integer isAdult;
}