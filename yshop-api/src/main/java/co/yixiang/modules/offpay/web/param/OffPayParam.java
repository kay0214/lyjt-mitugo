package co.yixiang.modules.offpay.web.param;

import co.yixiang.common.web.param.QueryParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * 线下支付
 * @Author : sss
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="本地生活分类列表", description="本地生活分类列表")
public class OffPayParam extends QueryParam {

    @ApiModelProperty(value = "用户打开小程序获取到的参数")
    @NotBlank(message = "参数错误")
    private String storeNid;


}
