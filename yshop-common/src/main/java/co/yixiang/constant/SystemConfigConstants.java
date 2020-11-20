package co.yixiang.constant;

public class SystemConfigConstants {
    //地址配置
    public final static String API="api";
    public final static String API_URL="api_url";
    public final static String SITE_URL="site_url";
    public final static String TENGXUN_MAP_KEY="tengxun_map_key";

    //业务相关配置
    public final static String IMAGEARR="imageArr";
    public final static String INTERGRAL_FULL="integral_full";
    public final static String INTERGRAL_MAX="integral_max";
    public final static String INTERGRAL_RATIO="integral_ratio";
    public final static String ORDER_CANCEL_JOB_TIME="order_cancel_job_time";
    public final static String STORE_BROKERAGE_OPEN="store_brokerage_open";
    public final static String STORE_BROKERAGE_RATIO="store_brokerage_ratio";
    public final static String STORE_BROKERAGE_STATU="store_brokerage_statu";
    public final static String STORE_BROKERAGE_TWO="store_brokerage_two";
    public final static String STORE_FREE_POSTAGE="store_free_postage";
    public final static String STORE_POSTAGE="store_postage";
    public final static String STORE_SEFL_MENTION="store_self_mention";
    public final static String STORE_USER_MIN_RECHARGE="store_user_min_recharge";
    // 用户最低提现金额
    public final static String USER_EXTRACT_MIN_PRICE="user_extract_min_price";
    // 用户提现费率 默认：0
    public final static String USER_EXTRACT_RATE = "user_extract_rate";
    // 商户提现最低金额
    public final static String STORE_EXTRACT_MIN_PRICE = "store_extract_min_price";
    // 商户提现费率 默认为：0.1
    public final static String STORE_EXTRACT_RATE = "store_extract_rate";
    // 商户是否可以提现 0：可提现 1：不可提现
    public final static String STORE_EXTRACT_SWITCH="store_extract_switch";
    // 核销端默认头像地址
    public final static String ADMIN_DEFAULT_AVATAR = "admin_default_avatar";
    // 核销端查询账单默认缩略图
    public final static String BILL_DEFAULT_IMAGE = "bill_default_image";

    //微信相关配置
    public final static String WECHAT_APPID="wechat_appid";
    public final static String WECHAT_APPSECRET="wechat_appsecret";
    public final static String WECHAT_AVATAR="wechat_avatar";
    public final static String WECHAT_ENCODE="wechat_encode";
    public final static String WECHAT_ENCODINGAESKEY="wechat_encodingaeskey";
    public final static String WECHAT_ID="wechat_id";
    public final static String WECHAT_NAME="wechat_name";
    public final static String WECHAT_QRCODE="wechat_qrcode";
    public final static String WECHAT_SHARE_IMG="wechat_share_img";
    public final static String WECHAT_SHARE_SYNOPSIS="wechat_share_synopsis";
    public final static String WECHAT_SHARE_TITLE="wechat_share_title";
    public final static String WECHAT_SOURCEID="wechat_sourceid";
    public final static String WECHAT_TOKEN="wechat_token";
    public final static String WECHAT_TYPE="wechat_type";
    public final static String WXAPP_APPID="wxapp_appId";
    public final static String WXAPP_SECRET="wxapp_secret";
    public final static String WXPAY_APPID="wxpay_appId";
    public final static String WXPAY_KEYPATH="wxpay_keyPath";
    public final static String WXPAY_MCHID="wxpay_mchId";
    public final static String WXPAY_MCHKEY="wxpay_mchKey";
    public final static String WX_NATIVE_APP_APPID="wx_native_app_appId";

    // ----------admin角色相关-----------------------------------------
    // 超级管理员
    public final static int  ROLE_ADMIN = 1;
    // 合伙人
    public final static int  ROLE_PARTNER = 4;
    // 商户
    public final static int  ROLE_MER = 5;
    // 平台管理员
    public final static int  ROLE_MANAGE = 6;
    // 核销人员
    public final static int  ROLE_VERIFICATION = 7;
    // 船只核销人员
    public final static int  ROLE_SHIPVER = 8;
    // 船长
    public final static int  ROLE_CAPTAIN = 9;
    // 景区推广
    public final static int  ROLE_SPREAD = 10;
    // 海岸支队
    public final static int  ROLE_POLICE = 11;
    // ----------admin角色相关-----------------------------------------

    // ----------卡券类型 -----------------------------------------
    // 代金券
    public final static int  COUPON_TYPE_DJ = 1;
    // 折扣券
    public final static int  COUPON_TYPE_ZK = 2;
    // 满减券
    public final static int  COUPON_TYPE_MJ = 3;
    // 船票券
    public final static int  COUPON_TYPE_CP = 4;
    // ----------admin角色相关-----------------------------------------
}
