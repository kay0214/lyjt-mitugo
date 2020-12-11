package co.yixiang.modules.coupons.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 本地生活, 卡券表 查询结果对象
 * </p>
 *
 * @author zqq
 * @date 2020-12-11
 */
@Data
@ApiModel(value="YxCouponsQueryVo对象", description="本地生活, 卡券表查询参数")
public class YxCouponsQueryVo implements Serializable{
    private static final long serialVersionUID = 1L;

@ApiModelProperty(value = "卡券主键")
private Integer id;

@ApiModelProperty(value = "卡券所属商铺")
private Integer storeId;

@ApiModelProperty(value = "卡券编号")
private String couponNum;

@ApiModelProperty(value = "卡券名称")
private String couponName;

@ApiModelProperty(value = "卡券类型;1:代金券, 2:折扣券, 3:满减券, 4:船票券")
private Integer couponType;

@ApiModelProperty(value = "卡券所属分类")
private Integer couponCategory;

@ApiModelProperty(value = "代金券面额, coupon_type为1时使用")
private BigDecimal denomination;

@ApiModelProperty(value = "折扣券折扣率, coupon_type为2时使用")
private BigDecimal discount;

@ApiModelProperty(value = "使用门槛, coupon_type为3时使用")
private BigDecimal threshold;

@ApiModelProperty(value = "优惠金额, coupon_type为3时使用")
private BigDecimal discountAmount;

@ApiModelProperty(value = "销售价格")
private BigDecimal sellingPrice;

@ApiModelProperty(value = "原价")
private BigDecimal originalPrice;

@ApiModelProperty(value = "平台结算价")
private BigDecimal settlementPrice;

@ApiModelProperty(value = "佣金")
private BigDecimal commission;

@ApiModelProperty(value = "每人限购数量")
private Integer quantityLimit;

@ApiModelProperty(value = "库存")
private Integer inventory;

@ApiModelProperty(value = "销量")
private Integer sales;

@ApiModelProperty(value = "虚拟销量")
private Integer ficti;

@ApiModelProperty(value = "核销次数")
private Integer writeOff;

@ApiModelProperty(value = "有效期始")
private Date expireDateStart;

@ApiModelProperty(value = "有效期止")
private Date expireDateEnd;

@ApiModelProperty(value = "热门优惠; 1:是, 0否")
private Integer isHot;

@ApiModelProperty(value = "状态（0：未上架，1：上架）")
private Integer isShow;

@ApiModelProperty(value = "排序")
private Integer sort;

@ApiModelProperty(value = "过期退 0:不支持 1支持")
private Integer outtimeRefund;

@ApiModelProperty(value = "免预约 0:不支持 1支持")
private Integer needOrder;

@ApiModelProperty(value = "随时退 0:不支持 1支持")
private Integer awaysRefund;

@ApiModelProperty(value = "使用条件 描述")
private String useCondition;

@ApiModelProperty(value = "可用时间始")
private String availableTimeStart;

@ApiModelProperty(value = "可用时间止")
private String availableTimeEnd;

@ApiModelProperty(value = "卡券简介")
private String couponInfo;

@ApiModelProperty(value = "是否删除（0：未删除，1：已删除）")
private Integer delFlag;

@ApiModelProperty(value = "创建人 根据创建人关联店铺")
private Integer createUserId;

@ApiModelProperty(value = "修改人")
private Integer updateUserId;

@ApiModelProperty(value = "创建时间")
private Date createTime;

@ApiModelProperty(value = "更新时间")
private Date updateTime;

@ApiModelProperty(value = "卡券详情")
private String content;

@ApiModelProperty(value = "船只系列id")
private Integer seriesId;

@ApiModelProperty(value = "船只id")
private Integer shipId;

@ApiModelProperty(value = "合同模板id")
private Integer tempId;

@ApiModelProperty(value = "乘客人数")
private Integer passengersNum;

@ApiModelProperty(value = "合同规则（0：无需保险，1：必须购买，2：非必须）")
private Integer insuranceRole;

@ApiModelProperty(value = "有效期（0：不限，1：其他）")
private Integer validity;

@ApiModelProperty(value = "有效天数（有效期=1时）")
private Integer validityDays;

@ApiModelProperty(value = "在线发票（0：不支持，1：支持）")
private Integer onlineInvoice;

@ApiModelProperty(value = "景区推广价格")
private BigDecimal scenicPrice;

@ApiModelProperty(value = "旅行社价格")
private BigDecimal travelPrice;

@ApiModelProperty(value = "健康确认(逗号分隔)")
private String confirmation;

@ApiModelProperty(value = "分佣模式（0：按平台，1：不分佣，2：自定义分佣）")
private Integer customizeType;

}