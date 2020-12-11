package co.yixiang.common.web.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


@Data
@ApiModel("查询参数对象")
public class BaseParam implements Serializable {
    private static final long serialVersionUID = -3263921252635611410L;

    @NotNull(message = "版本号不可为空")
    @ApiModelProperty(value = "版本号")
    private String version;

    @NotNull(message = "时间戳不可为空")
    @ApiModelProperty(value = "时间戳")
    private String timestamp;

    @NotNull(message = "签名不可为空")
    @ApiModelProperty(value = "签名")
    private String signature;

    @NotNull(message = "合作方用户编码不可为空")
    @ApiModelProperty(value = "合作方用户编码")
    private String channelId;

}
