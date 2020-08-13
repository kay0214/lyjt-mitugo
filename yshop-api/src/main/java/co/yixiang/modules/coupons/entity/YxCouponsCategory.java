package co.yixiang.modules.coupons.entity;

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
 * 本地生活, 卡券分类表
 * </p>
 *
 * @author hupeng
 * @since 2020-08-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="YxCouponsCategory对象", description="本地生活, 卡券分类表")
public class YxCouponsCategory extends BaseEntity {

    private static final long serialVersionUID = 1L;

@ApiModelProperty(value = "券分类主键")
@TableId(value = "id", type = IdType.AUTO)
private Integer id;

@ApiModelProperty(value = "父id")
private Integer pid;

@ApiModelProperty(value = "分类名称")
private String cateName;

@ApiModelProperty(value = "排序")
private Integer sort;

@ApiModelProperty(value = "是否推荐. 0:不推荐, 1:推荐")
private Integer isShow;

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
