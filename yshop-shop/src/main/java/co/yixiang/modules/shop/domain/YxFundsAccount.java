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
* @author liusy
* @date 2020-09-11
*/
@Data
@TableName("yx_funds_account")
public class YxFundsAccount implements Serializable {

    /** 主键 */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    /** 平台分佣总额 */
    private BigDecimal price;


    /** 分红总积分 */
    private BigDecimal bonusPoint;


    /** 拉新总积分 */
    private BigDecimal referencePoint;


    /** 是否删除（0：未删除，1：已删除） */
    @NotNull
    @TableLogic
    @TableField(fill=FieldFill.INSERT_UPDATE)
    private Integer delFlag;


    /** 创建人 */
    private Integer createUserId;


    /** 修改人 */
    private Integer updateUserId;


    /** 创建时间 */
    @NotNull
    @TableField(fill= FieldFill.INSERT)
    private Timestamp createTime;


    /** 更新时间 */
    @NotNull
    @TableField(fill= FieldFill.INSERT_UPDATE)
    private Timestamp updateTime;


    public void copy(YxFundsAccount source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
