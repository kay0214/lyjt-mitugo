package co.yixiang.modules.ship.web.param;

import co.yixiang.common.web.param.QueryParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * <p>
 * 船只预约表 查询参数对象
 * </p>
 *
 * @author lsy
 * @date 2020-11-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="YxShipAppointQueryParam对象", description="船只预约表查询参数")
public class YxShipAppointQueryParam extends QueryParam {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "船只id")
    private List<Integer> shipIdList;
    @ApiModelProperty(value = "预约时间集合")
    private List<String> dateList;
}
