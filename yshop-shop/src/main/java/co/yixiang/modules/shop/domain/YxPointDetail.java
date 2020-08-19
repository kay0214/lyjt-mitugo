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
* @author huiy
* @date 2020-08-19
*/
@Data
@TableName("yx_point_detail")
public class YxPointDetail implements Serializable {

    /** 主键id */
    @TableId
    private Integer id;


    /** 用户ID */
    @NotNull
    private Integer uid;


    /** 用户名 */
    private String username;


    /** 积分类别 0:拉新 1:分红 */
    @NotNull
    private Integer type;


    /** 订单编号 */
    private String orderId;


    /** 订单类型 0:商品购买 1:本地生活 */
    private Integer orderType;


    /** 订单金额 */
    private BigDecimal orderPrice;


    /** 订单佣金 */
    private BigDecimal commission;


    /** 商户id */
    @NotNull
    private Integer merchantsId;


    /** 商户获取积分数 */
    private BigDecimal merchantsPoint;


    /** 合伙人id */
    @NotNull
    private Integer partnerId;


    /** 合伙人获取积分数 */
    private BigDecimal partnerPoint;


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


    public void copy(YxPointDetail source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
