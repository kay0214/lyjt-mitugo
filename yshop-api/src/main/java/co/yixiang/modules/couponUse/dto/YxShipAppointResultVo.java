package co.yixiang.modules.couponUse.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 船只预约表 查询结果对象
 * </p>
 *
 * @author lsy
 * @date 2020-11-04
 */
@Data
@ApiModel(value="YxShipAppointResultVo对象", description="船只预约返回参数")
public class YxShipAppointResultVo implements Serializable{

@ApiModelProperty(value = "日期集合")
private List<String> dateList;

@ApiModelProperty(value = "船只名")
private String shipName;

/*@ApiModelProperty(value = "预约数据")
private YxShipAppointVo yxShipAppointVo;*/
}