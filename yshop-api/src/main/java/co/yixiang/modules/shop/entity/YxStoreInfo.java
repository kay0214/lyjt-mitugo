package co.yixiang.modules.shop.entity;

import java.math.BigDecimal;
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
 * 店铺表
 * </p>
 *
 * @author hupeng
 * @since 2020-08-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="YxStoreInfo对象", description="店铺表")
public class YxStoreInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

@TableId(value = "id", type = IdType.AUTO)
private Integer id;

@ApiModelProperty(value = "店铺编号")
private String storeNid;

@ApiModelProperty(value = "店铺名称")
private String storeName;

@ApiModelProperty(value = "管理人用户名")
private String manageUserName;

@ApiModelProperty(value = "商户id")
private Integer merId;

@ApiModelProperty(value = "合伙人id")
private Integer partnerId;

@ApiModelProperty(value = "管理人电话")
private String manageMobile;

@ApiModelProperty(value = "店铺电话")
private String storeMobile;

@ApiModelProperty(value = "状态：0：上架，1：下架")
private Integer status;

@ApiModelProperty(value = "人均消费")
private BigDecimal perCapita;

@ApiModelProperty(value = "行业类别")
private Integer industryCategory;

@ApiModelProperty(value = "店铺省市区")
private String storeProvince;

@ApiModelProperty(value = "店铺详细地址")
private String storeAddress;

@ApiModelProperty(value = "地图坐标经度")
private String coordinateX;

@ApiModelProperty(value = "地图坐标纬度")
private String coordinateY;

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

@ApiModelProperty(value = "店铺介绍")
private String introduction;

}
