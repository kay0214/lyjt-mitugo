package co.yixiang.modules.user.web.param;

import co.yixiang.common.web.param.QueryParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
@ApiModel(value = "YxUsedContactsQueryParam对象", description = "常用联系人表查询参数")
public class YxUsedContactsQueryParam extends QueryParam {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "所属用户id")
    private Integer userId;

    @ApiModelProperty(value = "是否删除（0：未删除，1：已删除）")
    private Integer delFlag;
}
