/**
* Copyright (C) 2018-2020
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.shipManage.domain;
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
@TableName("yx_contract_sign")
public class YxContractSign implements Serializable {

    /** id */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    /** 关联订单号 */
    @NotBlank
    private String orderId;


    /** 模板id */
    @NotNull
    private Integer tempId;


    /** 模板名称 */
    @NotBlank
    private String tempName;


    /** 签署文件地址 */
    private String filePath;


    /** 签署状态 0:签署中 1：签署完成 */
    @NotNull
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


    public void copy(YxContractSign source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
