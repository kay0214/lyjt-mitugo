package co.yixiang.modules.shop.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 购物车表 查询结果对象
 * </p>
 *
 * @author hupeng
 * @date 2019-10-25
 */
@Data
@ApiModel(value = "YxStoreStoreCartQueryVo对象", description = "购物车参数")
public class YxStoreStoreCartVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "可用商品")
    private List<YxStoreStoreCartQueryVo> valid;

    @ApiModelProperty(value = "失效")
    private List<YxStoreStoreCartQueryVo> invalid;

}