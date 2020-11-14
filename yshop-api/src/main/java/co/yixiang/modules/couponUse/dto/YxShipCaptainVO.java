package co.yixiang.modules.couponUse.dto;

import co.yixiang.common.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 本地生活帆船订单乘客表
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "YxShipCaptainVO对象", description = "船长")
public class YxShipCaptainVO extends BaseEntity {


    @ApiModelProperty(value = "船长id")
    private Integer id;

    @ApiModelProperty(value = "船长名")
    private String batchNo;

}
