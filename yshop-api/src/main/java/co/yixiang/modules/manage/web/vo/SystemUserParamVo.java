package co.yixiang.modules.manage.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 系统用户 查询结果对象
 * </p>
 *
 * @author hupeng
 * @date 2020-08-13
 */
@Data
@ApiModel(value = "SystemUserParamVo对象", description = "系统用户查询参数")
public class SystemUserParamVo implements Serializable {

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "用户角色：超级管理员 -> 1，合伙人 -> 4 ，商户 -> 5，平台管理员 -> 6， 核销人员 -> 7，船只核销人员 -> 8，船长 -> 9，景区推广 -> 10 ，海岸支队 -> 11")
    private Integer userRole;

    @ApiModelProperty(value = "头像")
    private String avatar;

}