package co.yixiang.modules.coupons.web.vo;

import co.yixiang.common.mybatis.GeoPoint;
import co.yixiang.modules.coupons.entity.YxCoupons;
import co.yixiang.modules.ship.web.vo.YxShipPassengerQueryVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 卡券订单表 查询结果对象
 * </p>
 *
 * @author hupeng
 * @date 2020-08-13
 */
@Data
@ApiModel(value = "YxCouponOrderQueryVo对象", description = "卡券订单表查询参数")
public class YxCouponOrderQueryVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "订单ID")
    private Integer id;

    @ApiModelProperty(value = "订单号")
    private String orderId;

    @ApiModelProperty(value = "用户id")
    private Integer uid;

    @ApiModelProperty(value = "用户姓名")
    private String realName;

    @ApiModelProperty(value = "用户电话")
    private String userPhone;

    @ApiModelProperty(value = "订单商品总数")
    private Integer totalNum;

    @ApiModelProperty(value = "订单总价")
    private BigDecimal totalPrice;

    @ApiModelProperty(value = "卡券id")
    private Integer couponId;

    @ApiModelProperty(value = "卡券金额")
    private BigDecimal couponPrice;

    @ApiModelProperty(value = "支付状态 0未支付 1已支付")
    private Integer payStaus;

    @ApiModelProperty(value = "支付时间")
    private Integer payTime;

    @ApiModelProperty(value = "可被核销次数")
    private Integer useCount;

    @ApiModelProperty(value = "已核销次数")
    private Integer usedCount;

    @ApiModelProperty(value = "订单状态（0:待支付 1:已过期 2:待发放3:支付失败4:待使用5:已使用6:已核销7:退款中8:已退款9:退款驳回")
    private Integer status;

    @ApiModelProperty(value = "0 未退款 1 申请中 2 已退款")
    private Integer refundStatus;

    @ApiModelProperty(value = "退款用户说明")
    private String refundReasonWapExplain;

    @ApiModelProperty(value = "退款时间")
    private Integer refundReasonTime;

    @ApiModelProperty(value = "不退款的理由")
    private String refundReason;

    @ApiModelProperty(value = "退款金额")
    private BigDecimal refundPrice;

    @ApiModelProperty(value = "备注")
    private String mark;

    @ApiModelProperty(value = "商户ID")
    private Integer merId;

    @ApiModelProperty(value = "推荐人用户ID")
    private Integer parentId;

    @ApiModelProperty(value = "推荐人类型:1商户;2合伙人;3用户")
    private Integer parentType;

    @ApiModelProperty(value = "分享人Id")
    private Integer shareId;

    @ApiModelProperty(value = "分享人的推荐人id")
    private Integer shareParentId;

    @ApiModelProperty(value = "分享人的推荐人类型")
    private Integer shareParentType;

    @ApiModelProperty(value = "核销码")
    private String verifyCode;

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

    @ApiModelProperty(value = "店铺id")
    private Integer storeId;

    @ApiModelProperty(value = "店铺名称")
    private String storeName;

    @ApiModelProperty(value = "店铺详细地址")
    private String storeAddress;

    @ApiModelProperty(value = "距离")
    private String distance;

    private GeoPoint coordinate;

    @ApiModelProperty(value = "地图坐标经度")
    private String coordinateX;

    @ApiModelProperty(value = "地图坐标纬度")
    private String coordinateY;

    @ApiModelProperty(value = "卡券有效期")
    private String expireDate;

    @ApiModelProperty(value = "使用时间段")
    private String availableTime;

    @ApiModelProperty(value = "购买时间")
    private String buyTime;

    @ApiModelProperty(value = "缩略图")
    private String image;

    @ApiModelProperty(value = "卡券类型;1:代金券, 2:折扣券, 3:满减券，4:船票券")
    private Integer couponType;

    @ApiModelProperty(value = "卡券名称")
    private String couponName;

    @ApiModelProperty(value = "创建时间")
    private String createTimeStr;

    @ApiModelProperty(value = "卡券信息")
    private YxCoupons yxCoupons;

    @ApiModelProperty(value = "卡券List")
    private List<YxCouponOrderDetailQueryVo> detailList;

    @ApiModelProperty(value = "店铺缩略图")
    private String storeImage;

    // 以下为船只信息
    @ApiModelProperty(value = "船只名称")
    private String shipName;
    @ApiModelProperty(value = "系列名称")
    private String seriesName;

    @ApiModelProperty(value = "乘船地图坐标")
    private GeoPoint shipCoordinate;

    @ApiModelProperty(value = "乘船地图坐标经度")
    private String shipCoordinateX;

    @ApiModelProperty(value = "乘船地图坐标纬度")
    private String shipCoordinateY;

    @ApiModelProperty(value = "乘船地址")
    private String shipAddress;

    @ApiModelProperty(value = "健康确认(数组) ")
    private List<String> confirmationList;

    @ApiModelProperty(value = "模板名称")
    private String tempName;

    @ApiModelProperty(value = "模板文件地址")
    private String tempFilePath;

    @ApiModelProperty(value = "状态(0：展示立即使用，1：不展示)")
    private Integer shipOrderStatus;


    @ApiModelProperty(value = "评价状态：0未评价 1已评价")
    private Integer evaluate;

    @ApiModelProperty(value = "乘客列表")
    private List<YxShipPassengerQueryVo> passengeList;

    @ApiModelProperty(value = "限乘人数")
    private Integer rideLimit;

    @ApiModelProperty(value = "可选张数")
    private Integer optionalNum;
}