package co.yixiang.modules.shop.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 商品表 查询结果对象
 * </p>
 *
 * @author nxl
 * @date 2020-08-19
 */
@Data
@ApiModel(value = "YxStoreProductAndStoreQueryVo对象", description = "商品和商铺表查询参数")
public class YxStoreProductAndStoreQueryVo implements Serializable {
    @ApiModelProperty(value = "商品列表")
    private List<YxStoreProductQueryVo> productList;
    @ApiModelProperty(value = "店铺id")
    private Integer storeId;
    @ApiModelProperty(value = "店铺名称")
    private String storeName;
    @ApiModelProperty(value = "行业类别")
    private String industryCategoryInfo;
    @ApiModelProperty(value = "共几家")
    private Integer sumStoe = 0;
    @ApiModelProperty(value = "店铺缩略图")
    private String storeImage;
    @ApiModelProperty(value = "店铺详细地址")
    private String storeAddress;
}