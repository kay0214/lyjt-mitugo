/**
* Copyright (C) 2018-2020
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.shopConfig.domain;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.*;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.validation.constraints.*;
import java.sql.Timestamp;
import java.io.Serializable;

/**
* @author nxl
* @date 2020-11-04
*/
@Data
@TableName("yx_merchants_settlement")
public class YxMerchantsSettlement implements Serializable {

    /** id */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    /** 公司名 */
    @NotBlank
    private String companyName;


    /** 联系人 */
    @NotBlank
    private String contactsName;


    /** 联系电话 */
    @NotBlank
    private String phone;


    /** 联系地址 */
    @NotBlank
    private String address;


    /** 说明 */
    @NotBlank
    @TableField(value = "`explain`")
    private String explain;


    /** 备注 */
    @NotBlank
    private String remark;


    /** 状态：0：待联系，1：有意向，2：已拒绝 */
    @NotNull
    @TableField(value = "`status`")
    private Integer status;


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


    public void copy(YxMerchantsSettlement source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
