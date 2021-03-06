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
@TableName("yx_ship_info")
public class YxShipInfo implements Serializable {

    /** 船只id */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    /** 船只名称 */
    @NotBlank
    private String shipName;


    /** 船只系列id */
    @NotNull
    private Integer seriesId;


    /** 商户id */
    @NotNull
    private Integer merId;


    /** 所属商铺 */
    @NotNull
    private Integer storeId;


    /** 帆船所属商户名 */
    private String merName;


    /** 帆船负责人 */
    private String managerName;


    /** 负责人电话 */
    private String managerPhone;


    /** 船只状态：0：启用，1：禁用 */
    @NotNull
    private Integer shipStatus;


    /** 船只当前状态：0：在港，1：离港。2：维修中 */
    @NotNull
    private Integer currentStatus;


    /** 最近一次出港时间 */
    private Integer lastLeaveTime;


    /** 最近一次返港时间 */
    private Integer lastReturnTime;


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


    public void copy(YxShipInfo source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
