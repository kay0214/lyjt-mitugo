package co.yixiang.modules.order.web.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @ClassName OrderExtendDTO
 * @Author hupeng <610796224@qq.com>
 * @Date 2019/10/28
 **/
@Data
public class OrderExtendDTO implements Serializable {
    private String key;
    @ApiModelProperty(value = "订单主键id")
    private Integer id;
    @ApiModelProperty(value = "订单id")
    private String orderId;
    private Map<String,String> jsConfig;
}
