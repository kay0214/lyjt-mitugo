package co.yixiang.modules.contract.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import co.yixiang.common.entity.BaseEntity;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * <p>
 * 签章信息表
 * </p>
 *
 * @author lsy
 * @since 2020-11-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="YxSignInfo对象", description="签章信息表")
public class YxSignInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

@ApiModelProperty(value = "id")
@TableId(value = "id", type = IdType.AUTO)
private Integer id;

@ApiModelProperty(value = "姓名/企业名称")
private String truename;

@ApiModelProperty(value = "身份证号/社会信用代码")
private String cardNo;

@ApiModelProperty(value = "类型 0：个人 1：企业")
private Integer signType;

@ApiModelProperty(value = "e签宝个人账号id")
private String accountId;

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

@ApiModelProperty(value = "悟空个人签章id")
private String sealData;

}
