package co.yixiang.modules.shop.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;

import java.util.Date;

/**
 * <p>
 * 店铺属性表 查询结果对象
 * </p>
 *
 * @author hupeng
 * @date 2020-08-13
 */
@Data
@ApiModel(value="YxStoreAttributeQueryVo对象", description="店铺属性表查询参数")
public class YxStoreAttributeQueryVo implements Serializable{
    private static final long serialVersionUID = 1L;

private Integer id;

@ApiModelProperty(value = "店铺id")
private Integer storeId;

@ApiModelProperty(value = "属性值1")
private String attributeValue1;

@ApiModelProperty(value = "属性值2")
private String attributeValue2;

@ApiModelProperty(value = "属性类型：0：营业时间，1：店铺服务")
private Integer attributeType;

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