package co.yixiang.modules.coupons.web.param;

import co.yixiang.common.web.param.QueryParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 本地生活, 卡券表 查询参数对象
 * </p>
 *
 * @author zqq
 * @date 2020-12-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="YxCouponsQueryParam对象", description="本地生活, 卡券表查询参数")
public class YxCouponsQueryParam extends QueryParam {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "默认字符串")
    @ApiModelProperty(value = "默认字符串V1.0")
    private String version;

    @NotNull(message = "时间戳")
    @ApiModelProperty(value = "时间戳")
    private String timestamp;

    @NotNull(message = "签名")
    @ApiModelProperty(value = "签名")
    private String signature;

    @NotNull(message = "合作方用户编码")
    @ApiModelProperty(value = "合作方用户编码")
    private String channelId;
}
