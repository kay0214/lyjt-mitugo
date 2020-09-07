package co.yixiang.modules.offpay.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 店铺表 查询结果对象
 * </p>
 *
 */
@Data
@ApiModel(value = "YxStoreInfoQueryVo对象", description = "店铺表查询参数")
public class OffPayStoreInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;

    @ApiModelProperty(value = "店铺名称")
    private String storeName;

    @ApiModelProperty(value = "店铺电话")
    private String storeMobile;

    @ApiModelProperty(value = "店铺省市区")
    private String storeProvince;

    @ApiModelProperty(value = "店铺详细地址")
    private String storeAddress;

    @ApiModelProperty(value = "店铺缩略图")
    private String storeImage;

    @ApiModelProperty(value = "调用userPay的随机串")
    private String payRand;

}