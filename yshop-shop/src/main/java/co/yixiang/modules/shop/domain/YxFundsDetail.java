/**
* Copyright (C) 2018-2020
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.shop.domain;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.*;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.validation.constraints.*;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.io.Serializable;

/**
* @author huiy
* @date 2020-08-19
*/
@Data
@TableName("yx_funds_detail")
public class YxFundsDetail implements Serializable {

    /** 主键 */
    @TableId
    private Integer id;


    /** 1:微商城下单,2:本地生活下单,3:微商城退款,4:本地生活退款 */
    @NotNull
    private Integer type;


    /** 用户uid */
    private Integer uid;


    /** 用户名 */
    private String username;


    /** 订单号 */
    @NotBlank
    private String orderId;


    /** 明细种类; 0:支出;1:收入 */
    @NotNull
    private Integer pm;


    /** 订单金额 */
    @NotNull
    private BigDecimal orderAmount;


    /** 订单日期 */
    @NotNull
    private Timestamp addTime;


    public void copy(YxFundsDetail source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
