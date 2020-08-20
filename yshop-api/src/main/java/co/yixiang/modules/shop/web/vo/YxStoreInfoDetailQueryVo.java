package co.yixiang.modules.shop.web.vo;

import co.yixiang.modules.coupons.web.vo.YxCouponsQueryVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 店铺表 查询结果对象
 * </p>
 *
 * @author hupeng
 * @date 2020-08-14
 */
@Data
@ApiModel(value = "YxStoreInfoDetailQueryVo对象", description = "店铺详情信息")
public class YxStoreInfoDetailQueryVo implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "店铺信息")
    private YxStoreInfoQueryVo sotreInfo;
    @ApiModelProperty(value = "卡券信息")
    private List<YxCouponsQueryVo> couponsListInfo;
    @ApiModelProperty(value = "商品信息")
    private List<YxStoreProductNoAttrQueryVo> productListInfo;
}