/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co

 */
package co.yixiang.modules.shop.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
* @author hupeng
* @date 2020-05-12
*/

@Data
@TableName("yx_store_product_attr_value")
public class YxStoreProductAttrValue implements Serializable {

    @TableId
    private Integer id;


    /** 商品ID */
    private Integer productId;


    /** 商品属性索引值 (attr_value|attr_value[|....]) */
    private String suk;


    /** 属性对应的库存 */
    @NotNull(message = "请输入库存")
    @Min(message = "库存不能小于0",value = 1)
    @Max(message ="库存不能超过16777215",value = 16777215)
    private Integer stock;


    /** 销量 */
    private Integer sales;


    /** 属性金额 */
    @NotNull(message = "请输入商品价格")
    @DecimalMin(value="0.00", message = "商品价格不在合法范围内" )
    @DecimalMax(value="99999999.99", message = "商品价格不在合法范围内")
    private BigDecimal price;


    /** 图片 */
    private String image;


    /** 唯一值 */
     @TableField(value = "`unique`")
    private String unique;


    /** 成本价 */
    @NotNull(message = "请输入成本价")
    @DecimalMin(value="0.00", message = "成本价不在合法范围内" )
    @DecimalMax(value="99999999.99", message = "成本价不在合法范围内")
    private BigDecimal cost;

    @NotNull(message = "请输入佣金")
    @DecimalMin(value="0.00", message = "佣金不在合法范围内" )
    @DecimalMax(value="99999999.99", message = "佣金不在合法范围内")
    private BigDecimal commission;




    public void copy(YxStoreProductAttrValue source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
