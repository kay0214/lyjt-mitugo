package co.yixiang.modules.user.web.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import co.yixiang.common.web.param.QueryParam;

/**
 * <p>
 * 常用联系人表 查询参数对象
 * </p>
 *
 * @author lsy
 * @date 2020-11-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="YxLeaveMessageQueryParam对象", description="常用联系人表查询参数")
public class YxLeaveMessageQueryParam extends QueryParam {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "开始时间")
    private String startTime;
    @ApiModelProperty(value = "结束时间")
    private String endTime;
    @ApiModelProperty(value = "状态：0 -> 待处理，1 -> 已处理，2 -> 不予处理")
    private Integer status;
    @ApiModelProperty(value = "商户id")
    private Integer merId;
}
