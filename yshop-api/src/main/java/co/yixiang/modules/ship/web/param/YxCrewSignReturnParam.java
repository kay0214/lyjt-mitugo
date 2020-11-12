package co.yixiang.modules.ship.web.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * <p>
 * 船员签到表 查询参数对象
 * </p>
 *
 * @author lsy
 * @date 2020-11-04
 */
@Data
@ApiModel(value="YxCrewSignParam对象", description="船员签到参数")
public class YxCrewSignReturnParam{

    @ApiModelProperty(value = "是否签到：0 -> 未签到，1 -> 已签到")
    private Integer singFlg;
    @ApiModelProperty(value = "体温")
    private BigDecimal temperature;
}
