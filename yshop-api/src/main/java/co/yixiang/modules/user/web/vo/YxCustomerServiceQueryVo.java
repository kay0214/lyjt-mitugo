package co.yixiang.modules.user.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;

import java.util.Date;

/**
 * <p>
 * 机器人客服表 查询结果对象
 * </p>
 *
 * @author lsy
 * @date 2020-11-04
 */
@Data
@ApiModel(value="YxCustomerServiceQueryVo对象", description="机器人客服表查询参数")
public class YxCustomerServiceQueryVo implements Serializable{
    private static final long serialVersionUID = 1L;

@ApiModelProperty(value = "id")
private Integer id;

@ApiModelProperty(value = "问题")
private String question;

@ApiModelProperty(value = "排序")
private Integer sort;

@ApiModelProperty(value = "状态：0：启用，1：禁用")
private Integer status;

@ApiModelProperty(value = "用户角色：0->平台运营,1->合伙人,2->商户")
private Integer userRole;

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

@ApiModelProperty(value = "回答")
private String answer;

}