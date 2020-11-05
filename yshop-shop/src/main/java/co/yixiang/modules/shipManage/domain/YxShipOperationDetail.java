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
* @date 2020-11-05
*/
@Data
@TableName("yx_ship_operation_detail")
public class YxShipOperationDetail implements Serializable {

    /** id */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    /** 卡券订单id */
    @NotBlank
    private String couponOrderId;


    /** 船只id */
    @NotNull
    private Integer shipId;


    /** 船只出港批次号 */
    @NotBlank
    private String batchNo;


    /** 船只名称 */
    private String shipName;


    /** 船长id */
    @NotNull
    private Integer captainId;


    /** 船长姓名 */
    private String captainName;


    /** 核销人id */
    @NotNull
    private Integer useId;


    /** 核销人姓名 */
    private String useName;


    /** 乘客身体状况 */
    private String healthStatus;


    /** 承载人数 */
    private Integer totalPassenger;


    /** 老年人人数 */
    private Integer oldPassenger;


    /** 未成年人数 */
    private Integer underagePassenger;


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


    public void copy(YxShipOperationDetail source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
