package co.yixiang.modules.user.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.util.WxUtils;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.constant.ShopConstants;
import co.yixiang.constant.SystemConfigConstants;
import co.yixiang.enums.AppFromEnum;
import co.yixiang.exception.BadRequestException;
import co.yixiang.exception.ErrorRequestException;
import co.yixiang.modules.manage.entity.SystemUser;
import co.yixiang.modules.manage.mapper.SystemUserMapper;
import co.yixiang.modules.order.service.YxStoreOrderService;
import co.yixiang.modules.order.web.vo.YxStoreOrderQueryVo;
import co.yixiang.modules.security.rest.param.LoginParam;
import co.yixiang.modules.security.security.TokenProvider;
import co.yixiang.modules.security.security.vo.JwtUser;
import co.yixiang.modules.security.service.OnlineUserService;
import co.yixiang.modules.shop.service.YxStoreCouponUserService;
import co.yixiang.modules.shop.service.YxSystemConfigService;
import co.yixiang.modules.shop.service.YxSystemStoreStaffService;
import co.yixiang.modules.user.entity.*;
import co.yixiang.modules.user.mapper.YxUserMapper;
import co.yixiang.modules.user.service.*;
import co.yixiang.modules.user.web.dto.PromUserDTO;
import co.yixiang.modules.user.web.param.PromParam;
import co.yixiang.modules.user.web.param.YxUserQueryParam;
import co.yixiang.modules.user.web.vo.YxUserQueryVo;
import co.yixiang.mp.config.ShopKeyUtils;
import co.yixiang.mp.config.WxMpConfiguration;
import co.yixiang.utils.OrderUtil;
import co.yixiang.utils.RedisUtil;
import co.yixiang.utils.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.vdurmont.emoji.EmojiParser;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author hupeng
 * @since 2020-08-13
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class YxUserServiceImpl extends BaseServiceImpl<YxUserMapper, YxUser> implements YxUserService {

    @Autowired
    private YxUserMapper yxUserMapper;
    @Autowired
    private YxStoreOrderService orderService;
    @Autowired
    private YxSystemConfigService systemConfigService;
    @Autowired
    private YxUserBillService billService;
    @Autowired
    private YxUserLevelService userLevelService;
    @Autowired
    private YxSystemUserLevelService systemUserLevelService;
    @Autowired
    private YxStoreCouponUserService storeCouponUserService;
    @Autowired
    private YxSystemStoreStaffService systemStoreStaffService;
    @Autowired
    private SystemUserMapper systemUserMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private OnlineUserService onlineUserService;
    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private AuthenticationManagerBuilder authenticationManagerBuilder;
    @Autowired
    private WxMaService wxMaService;
    @Autowired
    private YxWechatUserService wechatUserService;
    @Value("${single.login}")
    private Boolean singleLogin;
    @Value("${yshop.notify.sms.enable}")
    private Boolean enableSms;


    /**
     * 返回会员价
     *
     * @param price
     * @param uid
     * @return
     */
    @Override
    public double setLevelPrice(double price, int uid) {
        QueryWrapper<YxUserLevel> wrapper = new QueryWrapper<>();
        wrapper.eq("is_del", 0).eq("status", 1)
                .eq("uid", uid).orderByDesc("grade").last("limit 1");
        YxUserLevel userLevel = userLevelService.getOne(wrapper);
        YxSystemUserLevel systemUserLevel = new YxSystemUserLevel();
        if (ObjectUtil.isNotNull(userLevel)) systemUserLevel = systemUserLevelService.getById(userLevel.getLevelId());
        int discount = 100;
        if (ObjectUtil.isNotNull(userLevel)) discount = systemUserLevel.getDiscount().intValue();
        return NumberUtil.mul(NumberUtil.div(discount, 100), price);
    }


    /**
     * 更新用户余额
     *
     * @param uid
     * @param price
     */
    @Override
    public void incMoney(int uid, double price) {
        yxUserMapper.incMoney(uid, price);
    }

    @Override
    public void incIntegral(int uid, double integral) {
        yxUserMapper.incIntegral(integral, uid);
    }

    /**
     * 一级返佣
     *
     * @param order
     * @return
     */
    @Override
    public boolean backOrderBrokerage(YxStoreOrderQueryVo order) {
        //如果分销没开启直接返回
        String open = systemConfigService.getData("store_brokerage_open");
        if (StrUtil.isEmpty(open) || open.equals("2")) return false;
        //支付金额减掉邮费
        double payPrice = 0d;
        payPrice = NumberUtil.sub(order.getPayPrice(), order.getPayPostage()).doubleValue();

        //获取购买商品的用户
        YxUserQueryVo userInfo = getYxUserById(order.getUid());
        //当前用户不存在 没有上级  直接返回
        if (ObjectUtil.isNull(userInfo) || userInfo.getSpreadUid() == 0) return true;

        //获取后台分销类型  1 指定分销 2 人人分销
        int storeBrokerageStatus = 1;
        if (StrUtil.isNotEmpty(systemConfigService.getData(SystemConfigConstants.STORE_BROKERAGE_STATU))) {
            storeBrokerageStatus = Integer.valueOf(systemConfigService
                    .getData(SystemConfigConstants.STORE_BROKERAGE_STATU));
        }

        //指定分销 判断 上级是否时推广员  如果不是推广员直接跳转二级返佣
        YxUserQueryVo preUser = getYxUserById(userInfo.getSpreadUid());
        if (storeBrokerageStatus == 1) {

            if (preUser.getIsPromoter() == 0) {
                return backOrderBrokerageTwo(order);
            }
        }

        //获取后台一级返佣比例
        String storeBrokerageRatioStr = systemConfigService.getData(SystemConfigConstants.STORE_BROKERAGE_RATIO);
        int storeBrokerageRatio = 0;
        if (StrUtil.isNotEmpty(storeBrokerageRatioStr)) {
            storeBrokerageRatio = Integer.valueOf(storeBrokerageRatioStr);
        }
        //一级返佣比例 等于零时直接返回 不返佣
        if (storeBrokerageRatio == 0) return true;

        //计算获取一级返佣比例
        double brokerageRatio = NumberUtil.div(storeBrokerageRatio, 100);
        //成本价
        double cost = order.getCost().doubleValue();

        //成本价大于等于支付价格时直接返回
        if (cost >= payPrice) return true;

        //获取订单毛利
        payPrice = NumberUtil.sub(payPrice, cost);

        //返佣金额 = 毛利 / 一级返佣比例
        double brokeragePrice = NumberUtil.mul(payPrice, brokerageRatio);

        //返佣金额小于等于0 直接返回不返佣金
        if (brokeragePrice <= 0) return true;

        //计算上级推广员返佣之后的金额
        double balance = NumberUtil.add(preUser.getBrokeragePrice(), brokeragePrice)
                .doubleValue();
        String mark = userInfo.getNickname() + "成功消费" + order.getPayPrice() + "元,奖励推广佣金" +
                brokeragePrice;
        //插入流水
        YxUserBill userBill = new YxUserBill();
        userBill.setUid(userInfo.getSpreadUid());
        userBill.setTitle("获得推广佣金");
        userBill.setLinkId(order.getId().toString());
        userBill.setCategory("now_money");
        userBill.setType("brokerage");
        userBill.setNumber(BigDecimal.valueOf(brokeragePrice));
        userBill.setBalance(BigDecimal.valueOf(balance));
        userBill.setMark(mark);
        userBill.setStatus(1);
        userBill.setPm(1);
        userBill.setAddTime(OrderUtil.getSecondTimestampTwo());
        billService.save(userBill);

        //添加用户余额
        yxUserMapper.incBrokeragePrice(brokeragePrice,
                userInfo.getSpreadUid());

        //一级返佣成功 跳转二级返佣
        backOrderBrokerageTwo(order);

        return false;
    }

    /**
     * 二级返佣
     *
     * @param order
     * @return
     */
    @Override
    public boolean backOrderBrokerageTwo(YxStoreOrderQueryVo order) {

        double payPrice = 0d;
        payPrice = NumberUtil.sub(order.getPayPrice(), order.getPayPostage()).doubleValue();

        YxUserQueryVo userInfo = getYxUserById(order.getUid());

        //获取上推广人
        YxUserQueryVo userInfoTwo = getYxUserById(userInfo.getSpreadUid());

        //上推广人不存在 或者 上推广人没有上级    直接返回
        if (ObjectUtil.isNull(userInfoTwo) || userInfoTwo.getSpreadUid() == 0) return true;

        //获取后台分销类型  1 指定分销 2 人人分销
        int storeBrokerageStatus = 1;
        if (StrUtil.isNotEmpty(systemConfigService.getData(SystemConfigConstants.STORE_BROKERAGE_STATU))) {
            storeBrokerageStatus = Integer.valueOf(systemConfigService
                    .getData(SystemConfigConstants.STORE_BROKERAGE_STATU));
        }
        //指定分销 判断 上上级是否时推广员  如果不是推广员直接返回
        YxUserQueryVo preUser = getYxUserById(userInfoTwo.getSpreadUid());
        if (storeBrokerageStatus == 1) {

            if (preUser.getIsPromoter() == 0) {
                return true;
            }
        }

        //获取二级返佣比例
        String storeBrokerageTwoStr = systemConfigService.getData(SystemConfigConstants.STORE_BROKERAGE_TWO);
        int storeBrokerageTwo = 0;
        if (StrUtil.isNotEmpty(storeBrokerageTwoStr)) {
            storeBrokerageTwo = Integer.valueOf(storeBrokerageTwoStr);
        }
        //一级返佣比例 等于零时直接返回 不返佣
        if (storeBrokerageTwo == 0) return true;

        //计算获取二级返佣比例
        double brokerageRatio = NumberUtil.div(storeBrokerageTwo, 100);
        //成本价
        double cost = order.getCost().doubleValue();

        //成本价大于等于支付价格时直接返回
        if (cost >= payPrice) return true;

        //获取订单毛利
        payPrice = NumberUtil.sub(payPrice, cost);

        //返佣金额 = 毛利 / 二级返佣比例
        double brokeragePrice = NumberUtil.mul(payPrice, brokerageRatio);

        //返佣金额小于等于0 直接返回不返佣金
        if (brokeragePrice <= 0) return true;

        //获取上上级推广员信息
        double balance = NumberUtil.add(preUser.getBrokeragePrice(), brokeragePrice)
                .doubleValue();
        String mark = "二级推广人" + userInfo.getNickname() + "成功消费" + order.getPayPrice() + "元,奖励推广佣金" +
                brokeragePrice;
        //插入流水
        YxUserBill userBill = new YxUserBill();
        userBill.setUid(userInfoTwo.getSpreadUid());
        userBill.setTitle("获得推广佣金");
        userBill.setLinkId(order.getId().toString());
        userBill.setCategory("now_money");
        userBill.setType("brokerage");
        userBill.setNumber(BigDecimal.valueOf(brokeragePrice));
        userBill.setBalance(BigDecimal.valueOf(balance));
        userBill.setMark(mark);
        userBill.setStatus(1);
        userBill.setPm(1);
        userBill.setAddTime(OrderUtil.getSecondTimestampTwo());
        billService.save(userBill);

        //添加用户余额
        yxUserMapper.incBrokeragePrice(brokeragePrice,
                userInfoTwo.getSpreadUid());


        return false;
    }

    @Override
    public void setUserSpreadCount(int uid) {
        QueryWrapper<YxUser> wrapper = new QueryWrapper<>();
        wrapper.eq("spread_uid", uid);
        int count = yxUserMapper.selectCount(wrapper);

        YxUser user = new YxUser();
        user.setUid(uid);
        user.setSpreadCount(count);

        yxUserMapper.updateById(user);
    }

    @Override
    public int getSpreadCount(int uid, int type) {
        QueryWrapper<YxUser> wrapper = new QueryWrapper<>();
        wrapper.eq("spread_uid", uid);
        int count = 0;
        if (type == 1) {
            count = yxUserMapper.selectCount(wrapper);
        } else {
            List<YxUser> userList = yxUserMapper.selectList(wrapper);
            List<Integer> userIds = userList.stream().map(YxUser::getUid)
                    .collect(Collectors.toList());
            if (userIds.isEmpty()) {
                count = 0;
            } else {
                QueryWrapper<YxUser> wrapperT = new QueryWrapper<>();
                wrapperT.in("spread_uid", userIds);

                count = yxUserMapper.selectCount(wrapperT);
            }

        }
        return count;
    }

    @Override
    public List<PromUserDTO> getUserSpreadGrade(PromParam promParam, int uid) {
        QueryWrapper<YxUser> wrapper = new QueryWrapper<>();
        wrapper.eq("spread_uid", uid);
        List<YxUser> userList = yxUserMapper.selectList(wrapper);
        List<Integer> userIds = userList.stream().map(YxUser::getUid)
                .collect(Collectors.toList());
        List<PromUserDTO> list = new ArrayList<>();
        if (userIds.isEmpty()) {
            return list;
        }
        String sort;
        if (StringUtils.isBlank(promParam.getSort())) {
            sort = "u.add_time desc";
        } else {
            sort = promParam.getSort();
        }
        String keyword = null;
        if (StringUtils.isNotBlank(promParam.getKeyword())) {
            keyword = promParam.getKeyword();
        }
        Page<YxUser> pageModel = new Page<>(promParam.getPage(), promParam.getLimit());
        //-级
        list = yxUserMapper.getUserSpreadCountList(pageModel, userIds, keyword, sort);
        return list;
    }

    /**
     * 设置推广关系
     *
     * @param spread
     * @param uid
     */
    @Override
    public boolean setSpread(int spread, int uid) {
        //如果分销没开启直接返回
        String open = systemConfigService.getData("store_brokerage_open");
        if (StrUtil.isEmpty(open) || open.equals("2")) return false;
        //当前用户信息
        YxUserQueryVo userInfo = getYxUserById(uid);
        if (ObjectUtil.isNull(userInfo)) return true;

        //当前用户有上级直接返回
        if (userInfo.getSpreadUid() > 0) return true;
        //没有推广编号直接返回
        if (spread == 0) return true;
        if (spread == uid) return true;

        //不能互相成为上下级
        YxUserQueryVo userInfoT = getYxUserById(spread);
        if (ObjectUtil.isNull(userInfoT)) return true;

        if (userInfoT.getSpreadUid() == uid) return true;

        //1-指定分销 2-人人分销
        int storeBrokerageStatus = Integer.valueOf(systemConfigService
                .getData(SystemConfigConstants.STORE_BROKERAGE_STATU));
        //如果是指定分销，如果 推广人不是分销员不能形成关系
        if (storeBrokerageStatus == 1 && userInfoT.getIsPromoter() == 0) {
            return true;
        }
        YxUser yxUser = new YxUser();

        yxUser.setParentId(spread);
        // 推荐人类型:1商户;2合伙人;3用户 目前只有前端用户可以分享二维码
        yxUser.setParentType(3);
        yxUser.setSpreadUid(spread);
        yxUser.setSpreadTime(OrderUtil.getSecondTimestampTwo());
        yxUser.setUid(uid);
        yxUserMapper.updateById(yxUser);

        return true;

    }

    @Override
    public void incPayCount(int uid) {
        yxUserMapper.incPayCount(uid);
    }

    @Override
    public void decPrice(int uid, double payPrice) {
        yxUserMapper.decPrice(payPrice, uid);
    }

    @Override
    public void decIntegral(int uid, double integral) {
        yxUserMapper.decIntegral(integral, uid);
    }

    @Override
    public YxUserQueryVo getYxUserById(Serializable id) {
        YxUserQueryVo userQueryVo = yxUserMapper.getYxUserById(id);
        return userQueryVo;
    }

    @Override
    public YxUserQueryVo getNewYxUserById(Serializable id) {
        YxUserQueryVo userQueryVo = yxUserMapper.getYxUserById(id);
        if (userQueryVo == null) {
            throw new ErrorRequestException("用户不存在");
        }
        userQueryVo.setOrderStatusNum(orderService.orderData((int) id));
        userQueryVo.setCouponCount(storeCouponUserService.getUserValidCouponCount((int) id));
        //判断分销类型
        String statu = systemConfigService.getData(SystemConfigConstants.STORE_BROKERAGE_STATU);
        if (StrUtil.isNotEmpty(statu)) {
            userQueryVo.setStatu(Integer.valueOf(statu));
        } else {
            userQueryVo.setStatu(0);
        }

        //获取核销权限
        userQueryVo.setCheckStatus(systemStoreStaffService.checkStatus((int) id, 0));

        return userQueryVo;
    }

    @Override
    public Paging<YxUserQueryVo> getYxUserPageList(YxUserQueryParam yxUserQueryParam) throws Exception {
        Page page = setPageParam(yxUserQueryParam, OrderItem.desc("create_time"));
        IPage<YxUserQueryVo> iPage = yxUserMapper.getYxUserPageList(page, yxUserQueryParam);
        return new Paging(iPage);
    }

    @Override
    public YxUser findByName(String name) {
        QueryWrapper<YxUser> wrapper = new QueryWrapper<>();
        wrapper.eq("username", name);
        return getOne(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Object authLogin(String code, String spread, HttpServletRequest request) {
        try {
            WxMpService wxService = WxMpConfiguration.getWxMpService();
            WxMpOAuth2AccessToken wxMpOAuth2AccessToken = wxService.oauth2getAccessToken(code);
            WxMpUser wxMpUser = wxService.oauth2getUserInfo(wxMpOAuth2AccessToken, null);
            String openid = wxMpUser.getOpenId();

            //如果开启了UnionId
            if (StrUtil.isNotBlank(wxMpUser.getUnionId())) {
                openid = wxMpUser.getUnionId();
            }
            YxUser yxUser = this.findByName(openid);

            String username = "";
            if (ObjectUtil.isNull(yxUser)) {
                //过滤掉表情
                String nickname = EmojiParser.removeAllEmojis(wxMpUser.getNickname());
                log.info("昵称：{}", nickname);
                //用户保存
                YxUser user = new YxUser();
                user.setAccount(nickname);
                //如果开启了UnionId
                if (StrUtil.isNotBlank(wxMpUser.getUnionId())) {
                    username = wxMpUser.getUnionId();
                    user.setUsername(wxMpUser.getUnionId());
                } else {
                    username = wxMpUser.getOpenId();
                    user.setUsername(wxMpUser.getOpenId());
                }
                user.setPassword(passwordEncoder.encode(ShopConstants.YSHOP_DEFAULT_PWD));
                user.setPwd(passwordEncoder.encode(ShopConstants.YSHOP_DEFAULT_PWD));
                user.setPhone("");
                user.setUserType(AppFromEnum.WECHAT.getValue());
                user.setLoginType(AppFromEnum.WECHAT.getValue());
                user.setAddTime(OrderUtil.getSecondTimestampTwo());
                user.setLastTime(OrderUtil.getSecondTimestampTwo());
                user.setNickname(nickname);
                user.setAvatar(wxMpUser.getHeadImgUrl());
                user.setNowMoney(BigDecimal.ZERO);
                user.setBrokeragePrice(BigDecimal.ZERO);
                user.setIntegral(BigDecimal.ZERO);

                this.save(user);


                //保存微信用户
                YxWechatUser yxWechatUser = new YxWechatUser();
                yxWechatUser.setAddTime(OrderUtil.getSecondTimestampTwo());
                yxWechatUser.setNickname(nickname);
                yxWechatUser.setOpenid(wxMpUser.getOpenId());
                int sub = 0;
                if (ObjectUtil.isNotNull(wxMpUser.getSubscribe()) && wxMpUser.getSubscribe()) sub = 1;
                yxWechatUser.setSubscribe(sub);
                yxWechatUser.setSex(wxMpUser.getSex());
                yxWechatUser.setLanguage(wxMpUser.getLanguage());
                yxWechatUser.setCity(wxMpUser.getCity());
                yxWechatUser.setProvince(wxMpUser.getProvince());
                yxWechatUser.setCountry(wxMpUser.getCountry());
                yxWechatUser.setHeadimgurl(wxMpUser.getHeadImgUrl());
                if (ObjectUtil.isNotNull(wxMpUser.getSubscribeTime())) {
                    yxWechatUser.setSubscribeTime(wxMpUser.getSubscribeTime().intValue());
                }
                if (StrUtil.isNotBlank(wxMpUser.getUnionId())) {
                    yxWechatUser.setUnionid(wxMpUser.getUnionId());
                }
                if (StrUtil.isNotEmpty(wxMpUser.getRemark())) {
                    yxWechatUser.setUnionid(wxMpUser.getRemark());
                }
                if (ObjectUtil.isNotEmpty(wxMpUser.getGroupId())) {
                    yxWechatUser.setGroupid(wxMpUser.getGroupId());
                }
                yxWechatUser.setUid(user.getUid());

                wechatUserService.save(yxWechatUser);

            } else {
                username = yxUser.getUsername();
                if (StrUtil.isNotBlank(wxMpUser.getOpenId()) || StrUtil.isNotBlank(wxMpUser.getUnionId())) {
                    YxWechatUser wechatUser = new YxWechatUser();
                    wechatUser.setUid(yxUser.getUid());
                    wechatUser.setUnionid(wxMpUser.getUnionId());
                    wechatUser.setOpenid(wxMpUser.getOpenId());

                    wechatUserService.updateById(wechatUser);
                }
            }


            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(username,
                            ShopConstants.YSHOP_DEFAULT_PWD);

            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            // 生成令牌
            String token = tokenProvider.createToken(authentication);
            final JwtUser jwtUserT = (JwtUser) authentication.getPrincipal();
            // 保存在线信息
            onlineUserService.save(jwtUserT, token, request);

            Date expiresTime = tokenProvider.getExpirationDateFromToken(token);
            String expiresTimeStr = DateUtil.formatDateTime(expiresTime);

            Map<String, String> map = new LinkedHashMap<>();
            map.put("token", token);
            map.put("expires_time", expiresTimeStr);

            if (singleLogin) {
                //踢掉之前已经登录的token
                onlineUserService.checkLoginOnUser(jwtUserT.getUsername(), token);
            }

            //设置推广关系
            if (StrUtil.isNotEmpty(spread) && !spread.equals("NaN")) {
                this.setSpread(Integer.valueOf(spread),
                        jwtUserT.getId().intValue());
            }

            // 返回 token
            return map;
        } catch (WxErrorException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new BadRequestException(e.toString());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Object wxappAuth(LoginParam loginParam, HttpServletRequest request) {
        String code = loginParam.getCode();
        String encryptedData = loginParam.getEncryptedData();
        String iv = loginParam.getIv();
        String spread = loginParam.getSpread();
        try {
            //读取redis配置
            String appId = RedisUtil.get(ShopKeyUtils.getWxAppAppId());
            String secret = RedisUtil.get(ShopKeyUtils.getWxAppSecret());
            if (StrUtil.isBlank(appId) || StrUtil.isBlank(secret)) {
                throw new ErrorRequestException("请先配置小程序");
            }
           /* WxMaDefaultConfigImpl wxMaConfig = new WxMaDefaultConfigImpl();
            wxMaConfig.setAppid(appId);
            wxMaConfig.setSecret(secret);

            wxMaService.setWxMaConfig(wxMaConfig);
            WxMaJscode2SessionResult session = wxMaService.getUserService().getSessionInfo(code);
            //解密数据,防止 session 无 unionId
            WxMaUserInfo wxMpUser = wxMaService.getUserService()
                    .getUserInfo(session.getSessionKey(), encryptedData, iv);*/
            String openId = "";
            WxMaUserInfo wxMpUser = new WxMaUserInfo();
            JSONObject userJSONObject = WxUtils.getUserInfo(code, appId, secret);
            if (userJSONObject != null) {
                JSONObject unionIdJSONObject = WxUtils.decryptPhoneData(userJSONObject.getString("session_key"), encryptedData, iv);
                if (unionIdJSONObject != null) {
                    openId = unionIdJSONObject.getString("openId");
                    wxMpUser.setOpenId(openId);
                    wxMpUser.setGender(unionIdJSONObject.getString("gender"));
                    wxMpUser.setNickName(unionIdJSONObject.getString("nickName"));
                    wxMpUser.setUnionId(unionIdJSONObject.getString("unionId"));
                    wxMpUser.setAvatarUrl(unionIdJSONObject.getString("avatarUrl"));
                    wxMpUser.setLanguage(unionIdJSONObject.getString("language"));
                    wxMpUser.setCity(unionIdJSONObject.getString("city"));
                    wxMpUser.setProvince(unionIdJSONObject.getString("province"));
                    wxMpUser.setCountry(unionIdJSONObject.getString("country"));
                }
            }
            //如果开启了UnionId
            if (StrUtil.isNotBlank(wxMpUser.getUnionId())) {
                openId = wxMpUser.getUnionId();
            }

            YxUser yxUser = this.findByName(openId);
            String username = "";
            if (ObjectUtil.isNull(yxUser)) {
                //过滤掉表情
                String nickname = EmojiParser.removeAllEmojis(wxMpUser.getNickName());
                //用户保存
                YxUser user = new YxUser();
                user.setAccount(nickname);

                //如果开启了UnionId
                if (StrUtil.isNotBlank(wxMpUser.getUnionId())) {
                    username = wxMpUser.getUnionId();
                    user.setUsername(wxMpUser.getUnionId());
                } else {
                    username = wxMpUser.getOpenId();
                    user.setUsername(wxMpUser.getOpenId());
                }
                user.setPassword(passwordEncoder.encode(ShopConstants.YSHOP_DEFAULT_PWD));
                user.setPwd(passwordEncoder.encode(ShopConstants.YSHOP_DEFAULT_PWD));
                user.setPhone("");
                user.setUserType(AppFromEnum.ROUNTINE.getValue());
                user.setAddTime(OrderUtil.getSecondTimestampTwo());
                user.setLastTime(OrderUtil.getSecondTimestampTwo());
                user.setNickname(nickname);
                user.setAvatar(wxMpUser.getAvatarUrl());
                user.setNowMoney(BigDecimal.ZERO);
                user.setBrokeragePrice(BigDecimal.ZERO);
                user.setIntegral(BigDecimal.ZERO);

                this.save(user);


                //保存微信用户
                YxWechatUser yxWechatUser = new YxWechatUser();
                // System.out.println("wxMpUser:"+wxMpUser);
                yxWechatUser.setAddTime(OrderUtil.getSecondTimestampTwo());
                yxWechatUser.setNickname(nickname);
                yxWechatUser.setRoutineOpenid(wxMpUser.getOpenId());
                int sub = 0;
                yxWechatUser.setSubscribe(sub);
                yxWechatUser.setSex(Integer.valueOf(wxMpUser.getGender()));
                yxWechatUser.setLanguage(wxMpUser.getLanguage());
                yxWechatUser.setCity(wxMpUser.getCity());
                yxWechatUser.setProvince(wxMpUser.getProvince());
                yxWechatUser.setCountry(wxMpUser.getCountry());
                yxWechatUser.setHeadimgurl(wxMpUser.getAvatarUrl());
                if (StrUtil.isNotBlank(wxMpUser.getUnionId())) {
                    yxWechatUser.setUnionid(wxMpUser.getUnionId());
                }
                yxWechatUser.setUid(user.getUid());

                wechatUserService.save(yxWechatUser);

                //设置推广关系
                if (StringUtils.isNotBlank(spread)) {
                    this.setSpread(Integer.valueOf(spread),
                            user.getUid());
                    // 设置推广人的推广人数+1
                    yxUserMapper.updateUserPusCount(Integer.valueOf(spread));
                }

            } else {
                username = yxUser.getUsername();
                if (StrUtil.isNotBlank(wxMpUser.getOpenId()) || StrUtil.isNotBlank(wxMpUser.getUnionId())) {
                    YxWechatUser wechatUser = new YxWechatUser();
                    wechatUser.setUid(yxUser.getUid());
                    wechatUser.setUnionid(wxMpUser.getUnionId());
                    wechatUser.setRoutineOpenid(wxMpUser.getOpenId());

                    wechatUserService.updateById(wechatUser);
                }
            }


            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(username,
                            ShopConstants.YSHOP_DEFAULT_PWD);

            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            // 生成令牌
            String token = tokenProvider.createToken(authentication);
            final JwtUser jwtUserT = (JwtUser) authentication.getPrincipal();
            // 保存在线信息
            onlineUserService.save(jwtUserT, token, request);

            Date expiresTime = tokenProvider.getExpirationDateFromToken(token);
            String expiresTimeStr = DateUtil.formatDateTime(expiresTime);


            Map<String, String> map = new LinkedHashMap<>();
            map.put("token", token);
            map.put("expires_time", expiresTimeStr);

            if (singleLogin) {
                //踢掉之前已经登录的token
                onlineUserService.checkLoginOnUser(jwtUserT.getUsername(), token);
            }

            // 返回 token
            return map;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BadRequestException(e.toString());
        }
    }

    /**
     * 根据登录用户名查询系统用户
     *
     * @param username
     * @return
     */
    @Override
    public SystemUser getSystemUserByUserName(String username) {
        QueryWrapper<SystemUser> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username).eq("merchants_status", 0)
                .eq("user_role", 2)
                .eq("enabled", 1);
        return systemUserMapper.selectOne(wrapper);
    }
}
