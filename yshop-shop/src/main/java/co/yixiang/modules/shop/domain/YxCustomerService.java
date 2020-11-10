/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package co.yixiang.modules.shop.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author nxl
 * @date 2020-11-04
 */
@Data
@TableName("yx_customer_service")
public class YxCustomerService implements Serializable {

    /** id */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    /** 问题 */
    @NotBlank
    private String question;


    /** 排序 */
    private Integer sort;


    /** 状态：0：启用，1：禁用 */
    @NotNull
    private Integer status;


    /** 用户角色：0->平台运营,1->合伙人,2->商户 */
    private Integer userRole;

    /** 所属商户id */
    private Integer merId;

    /** 是否删除（0：未删除，1：已删除） */
    @TableLogic
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer delFlag;


    /** 创建人 */
    private Integer createUserId;


    /** 修改人 */
    private Integer updateUserId;


    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private Timestamp createTime;


    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Timestamp updateTime;


    /** 回答 */
    @NotBlank
    private String answer;


    public void copy(YxCustomerService source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
