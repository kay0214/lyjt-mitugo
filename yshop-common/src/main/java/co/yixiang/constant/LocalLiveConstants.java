package co.yixiang.constant;

/**
 * 本地生活常量类
 * @Author : huanghui
 */
public interface LocalLiveConstants {

    /**
     * 订单自动取消时间（分钟）
     */
    long ORDER_OUTTIME_UNPAY = 30;

    /** 本地生活热搜 */
    String LOCAL_LIVE_HOT_SEARCH = "local_live_hot";

    /** 卡券分类相关 */
    int IMG_TYPE_COUPONS_CATEGORY = 5;
    /** 卡券相关 */
    int IMG_TYPE_COUPONS = 1;

    /**
     * redis订单未付款key
     */
    String REDIS_COUPON_ORDER_OUTTIME_UNPAY = "coupon_order:unpay:";
}
