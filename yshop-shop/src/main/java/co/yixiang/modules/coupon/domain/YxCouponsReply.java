/**
* Copyright (C) 2018-2020
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.coupon.domain;
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
@TableName("yx_coupons_reply")
public class YxCouponsReply implements Serializable {

    /** 评论ID */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    /** 用户ID */
    @NotNull
    private Integer uid;


    /** 订单ID */
    @NotNull
    private Integer oid;


    /** 卡券id */
    @NotNull
    private Integer couponId;


    /** 总体感觉 */
    @NotNull
    private Integer generalScore;


    /** 评论内容 */
    @NotBlank
    private String comment;


    /** 评论时间 */
    @NotNull
    private Integer addTime;


    /** 管理员回复时间 */
    private Integer merchantReplyTime;


    /** 0：未回复，1：已回复 */
    @NotNull
    private Integer isReply;


    /** 商户id */
    @NotNull
    private Integer merId;


    /** 管理员回复内容 */
    private String merchantReplyContent;


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


    public void copy(YxCouponsReply source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
