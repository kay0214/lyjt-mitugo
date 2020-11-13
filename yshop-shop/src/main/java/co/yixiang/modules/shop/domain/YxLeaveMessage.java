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
import java.io.Serializable;

/**
* @author nxl
* @date 2020-11-04
*/
@Data
@TableName("yx_leave_message")
public class YxLeaveMessage implements Serializable {

    /** id */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    /** 订单号 */
    private Integer linkId;


    /** 商户id */
    private Integer merId;


    /** 联系人 */
    @NotBlank
    private String userName;


    /** 电话 */
    @NotBlank
    private String userPhone;


    /** 留言信息 */
    @NotBlank
    private String message;


    /** 状态：0 -> 待处理，1 -> 已处理，2 -> 不予处理 */
    @NotNull
    private Integer status;

    /** 处理时间 */
    private Integer takeTime;

    /** 留言类型：0 -> 商品，1-> 卡券 2 -> 商城订单，3 -> 本地生活订单，4 ->商户，5 -> 平台 */
    @NotNull
    private Integer messageType;


    /** 备注 */
    private String remark;


    /** 是否删除（0：未删除，1：已删除） */
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

    public void copy(YxLeaveMessage source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
