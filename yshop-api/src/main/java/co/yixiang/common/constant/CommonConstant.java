package co.yixiang.common.constant;


public interface CommonConstant {

    /**
     * 默认页码为1
     */
    Integer DEFAULT_PAGE_INDEX = 1;

    /**
     * 默认页大小为10
     */
    Integer DEFAULT_PAGE_SIZE = 10;

    /**
     * 登录用户
     */
    String LOGIN_SYS_USER = "loginSysUser";

    /**
     * 登陆token
     */
    String TOKEN = "token";
    /**
     * 刷新token
     */
    String REFRESH_TOKEN = "refresh_token";

    /**
     * 初始密码
     */
    String INIT_PWD = "123456";

    /**
     * 默认头像
     */
    String DEFAULT_HEAD_URL = "";

    /**
     * 管理员角色名称
     */
    String ADMIN_ROLE_NAME = "管理员";

    String ADMIN_LOGIN = "adminLogin";

    //dict类型
    String DICT_TYPE_INDUSTRY_CATEGORY = "industry_category";
    String DICT_TYPE_STORE_SERVICE = "store_service";

    /*********************图片类型 **********************/
    /**
     * 图片类型-卡券
     */
    int IMG_TYPE_CARD=1;
    /**
     * 图片类型-店铺
     */
    int IMG_TYPE_STORE=2;
    /**
     * 图片类型-产品
     */
    int IMG_TYPE_PRODUCT=3;
    /**
     * 图片类型-商户
     */
    int IMG_TYPE_MERCHANTS=4;

    /*********************图片类别**********************/

    /**
     * 图片类别-缩略图
     */
    int IMG_CATEGORY_PIC=1;
    /**
     * 图片类别-轮播图
     */
    int IMG_CATEGORY_ROTATION1 = 2;
    /**
     * 图片类别-手持证件照
     */
    int IMG_PERSON_IDCARD=1;
    /**
     * 图片类别-证件照人像面
     */
    int IMG_PERSON_IDCARD_FACE=2;
    /**
     * 图片类别-证件照国徽面
     */
    int IMG_PERSON_IDCARD_BACK=3;
    /**
     * 图片类别-营业执照
     */
    int IMG_BUSINESS_LICENSE=4;
    /**
     * 图片类别-银行开户证明
     */
    int IMG_BANK_OPEN_PROVE=5;
    /**
     * 图片类别-法人身份证头像面
     */
    int IMG_LEGAL_IDCARD_FACE=6;
    /**
     * 图片类别-法人身份证国徽面
     */
    int IMG_LEGAL_IDCARD_BACK=7;
    /**
     * 图片类别-门店照及经营场所
     */
    int IMG_STORE=8;
    /**
     * 图片类别-医疗机构许可证
     */
    int IMG_LICENCE=9;




    /**
     * 线下支付redis
     */
    String USER_OFF_PAY="USER_OFF_PAY:";

    /**
     * 线下支付订单信息redis
     */
    String USER_OFF_PAY_ORDER="USER_OFF_PAY_ORDER:";
}
