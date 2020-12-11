package co.yixiang.common.web.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
@ApiModel("查询参数对象")
public class BaseParam implements Serializable {
    private static final long serialVersionUID = -3263921252635611410L;

    @ApiModelProperty(value = "默认字符串V1.0")
    private String version;

    @ApiModelProperty(value = "时间戳")
    private String timestamp;

    @ApiModelProperty(value = "签名")
    private String signature;

    @ApiModelProperty(value = "合作方用户编码")
    private String channelId;

}
