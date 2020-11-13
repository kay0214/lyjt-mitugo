package co.yixiang.modules.ship.web.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import co.yixiang.common.web.param.QueryParam;

/**
 * <p>
 * 船只表 查询参数对象
 * </p>
 *
 * @author lsy
 * @date 2020-11-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="YxShipInfoQueryParam对象", description="船只表查询参数")
public class YxShipInfoQueryParam extends QueryParam {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "船只id")
    private Integer id;

    @ApiModelProperty(value = "船只名称")
    private String shipName;

    @ApiModelProperty(value = "船只系列id")
    private Integer seriesId;

    @ApiModelProperty(value = "商户id")
    private Integer merId;

    @ApiModelProperty(value = "所属商铺")
    private Integer storeId;

    @ApiModelProperty(value = "帆船所属商户名")
    private String merName;

    @ApiModelProperty(value = "帆船负责人")
    private String managerName;

    @ApiModelProperty(value = "负责人电话")
    private String managerPhone;

//    @ApiModelProperty(value = "船只状态：0：启用，1：禁用")
//    private Integer shipStatus;

    @ApiModelProperty(value = "船只当前状态：0：在港，1：离港。2：维修中")
    private Integer currentStatus;

}
