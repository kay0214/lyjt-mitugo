/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co

 */
package co.yixiang.modules.shop.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
* @author hupeng
* @date 2020-05-12
*/

@Data
@TableName("yx_user_bill")
public class YxUserBill implements Serializable {

    /** 用户账单id */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    /** 用户uid */
    private Integer uid;

    /** 用户名 */
    private String username;

    /** 关联id */
    private String linkId;


    /** 0 = 支出 1 = 获得 */
    private Integer pm;


    /** 账单标题 */
    private String title;


    /** 明细种类 */
    private String category;


    /** 明细类型 */
    private String type;


    /** 明细数字 */
    private BigDecimal number;


    /** 剩余 */
    private BigDecimal balance;


    /** 备注 */
    private String mark;


    /** 添加时间 */
    @TableField(fill= FieldFill.INSERT)
    private Integer addTime;


    /** 0 = 带确定 1 = 有效 -1 = 无效 */
    @TableField(value = "`status`")
    private Integer status;

    /** 用户类型 0:预留 1商户;2合伙人;3用户 */
    private Integer userType;

    /** 佣金类型0:商品返佣 1：卡券返佣 */
    private Integer brokerageType;

    public void copy(YxUserBill source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
