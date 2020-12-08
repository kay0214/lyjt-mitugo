package co.yixiang.modules.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import co.yixiang.common.entity.BaseEntity;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * <p>
 * 商家入驻表
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="YxMerchantsSettlement对象", description="商家入驻表")
public class YxMerchantsSettlement extends BaseEntity {

    private static final long serialVersionUID = 1L;

@ApiModelProperty(value = "id")
@TableId(value = "id", type = IdType.AUTO)
private Integer id;

@ApiModelProperty(value = "公司名")
private String companyName;

@ApiModelProperty(value = "联系人")
private String contactsName;

@ApiModelProperty(value = "联系电话")
private String phone;

@ApiModelProperty(value = "联系地址")
private String address;

@ApiModelProperty(value = "说明")
@TableField(value = "`explain`")
private String explain;

@ApiModelProperty(value = "备注")
private String remark;

@ApiModelProperty(value = "状态：0：待联系，1：有意向，2：已拒绝")
private Integer status;

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
