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
* @date 2020-08-20
*/
@Data
@TableName("yx_commission_rate")
public class YxCommissionRate implements Serializable {

    /** 主键 */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    /** 平台抽成 */
    @NotNull(message = "请输入平台抽成")
    @DecimalMin(value="0", message = "平台抽成不在合法范围内" )
    @DecimalMax(value="100", message = "平台抽成不在合法范围内")
    private BigDecimal fundsRate;


    /** 分享人(分销客) */
    @NotNull(message = "请输入分享人抽成")
    @DecimalMin(value="0", message = "分享人抽成不在合法范围内" )
    @DecimalMax(value="100", message = "分享人抽成不在合法范围内")
    private BigDecimal shareRate;


    /** 分享人上级 */
    @NotNull(message = "请输入分享人上级抽成")
    @DecimalMin(value="0", message = "分享人上级抽成不在合法范围内" )
    @DecimalMax(value="100", message = "分享人上级抽成不在合法范围内")
    private BigDecimal shareParentRate;


    /** 推荐人(购买人上级) */
    @NotNull(message = "请输入推荐人抽成")
    @DecimalMin(value="0", message = "推荐人抽成不在合法范围内" )
    @DecimalMax(value="100", message = "推荐人抽成不在合法范围内")
    private BigDecimal parentRate;


    /** 商户 */
    @NotNull(message = "请输入商户抽成")
    @DecimalMin(value="0", message = "商户抽成不在合法范围内" )
    @DecimalMax(value="100", message = "商户抽成不在合法范围内")
    private BigDecimal merRate;


    /** 合伙人 */
    @NotNull(message = "请输入合伙人抽成")
    @DecimalMin(value="0", message = "合伙人抽成不在合法范围内" )
    @DecimalMax(value="100", message = "合伙人抽成不在合法范围内")
    private BigDecimal partnerRate;


    /** 拉新池 */
    @NotNull(message = "请输入拉新池抽成")
    @DecimalMin(value="0", message = "拉新池抽成不在合法范围内" )
    @DecimalMax(value="100", message = "拉新池抽成不在合法范围内")
    private BigDecimal referenceRate;


    /** 是否删除（0：未删除，1：已删除） */
    private Integer delFlag;


    /** 创建人 */
    private Integer createUserId;


    /** 修改人 */
    private Integer updateUserId;


    /** 创建时间 */
    private Timestamp createTime;


    /** 更新时间 */
    private Timestamp updateTime;


    public void copy(YxCommissionRate source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
