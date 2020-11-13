package co.yixiang.modules.user.web.param;

import co.yixiang.common.web.param.QueryParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 机器人客服表 查询参数对象
 * </p>
 *
 * @author lsy
 * @date 2020-11-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "YxCustomerServiceQueryParam对象", description = "机器人客服表查询参数")
public class YxCustomerServiceQueryParam extends QueryParam {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "状态：0：启用，1：禁用")
    private Integer status = 0;
    @ApiModelProperty(value = "是否删除（0：未删除，1：已删除）")
    private Integer delFlag = 0;
    @ApiModelProperty(value = "所属商户id")
    private Integer merId;
    @ApiModelProperty(value = "用户角色：0->平台运营,1->合伙人,2->商户")
    private Integer userRole;
}
