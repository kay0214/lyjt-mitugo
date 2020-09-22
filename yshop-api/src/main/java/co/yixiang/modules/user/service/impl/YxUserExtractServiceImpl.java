/**
 * Copyright (C) 2018-2020
 */
package co.yixiang.modules.user.service.impl;

import cn.hutool.core.util.StrUtil;
import co.yixiang.common.rocketmq.MqProducer;
import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.constant.MQConstant;
import co.yixiang.exception.ErrorRequestException;
import co.yixiang.modules.pay.param.PaySeachParam;
import co.yixiang.modules.user.entity.YxUser;
import co.yixiang.modules.user.entity.YxUserExtract;
import co.yixiang.modules.user.mapper.YxUserExtractMapper;
import co.yixiang.modules.user.service.YxUserExtractService;
import co.yixiang.modules.user.service.YxUserService;
import co.yixiang.modules.user.web.param.UserExtParam;
import co.yixiang.modules.user.web.param.YxUserExtractQueryParam;
import co.yixiang.modules.user.web.vo.YxUserExtractQueryVo;
import co.yixiang.modules.user.web.vo.YxUserQueryVo;
import co.yixiang.utils.OrderUtil;
import co.yixiang.utils.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hyjf.framework.starter.recketmq.MessageContent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;


/**
 * <p>
 * 用户提现表 服务实现类
 * </p>
 *
 * @author hupeng
 * @since 2019-11-11
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class YxUserExtractServiceImpl extends BaseServiceImpl<YxUserExtractMapper, YxUserExtract> implements YxUserExtractService {

    @Autowired
    YxUserExtractMapper yxUserExtractMapper;
    @Autowired
    YxUserService userService;
    @Autowired
    private MqProducer mqProducer;

    /**
     * 开始提现
     *
     * @param uid
     * @param param
     */
    @Override
    public void updateUserExtract(int uid, UserExtParam param) {
        YxUserQueryVo userInfo = userService.getYxUserById(uid);
        BigDecimal extractPrice = userInfo.getNowMoney();
        if (extractPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ErrorRequestException("佣金不足无法提现");
        }
        BigDecimal money = new BigDecimal(param.getMoney());
        if (money.compareTo(extractPrice) > 0) {
            throw new ErrorRequestException("提现佣金不足");
        }
        if (money.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ErrorRequestException("提现佣金大于0");
        }

        BigDecimal balance = extractPrice.subtract(money);

        YxUserExtract userExtract = new YxUserExtract();
        userExtract.setUid(uid);
        userExtract.setExtractType(StringUtils.isNotBlank(param.getExtractType()) ? param.getExtractType() : "bank");
        userExtract.setExtractPrice(new BigDecimal(param.getMoney()));
        userExtract.setAddTime(OrderUtil.getSecondTimestampTwo());
        userExtract.setBalance(balance);
        userExtract.setStatus(0);
        userExtract.setBankCode(param.getBankNo());
        userExtract.setBankAddress(userInfo.getBankName());
        userExtract.setBankMobile(param.getPhone());
        // 联行号
        userExtract.setCnapsCode(param.getCnapsCode());
        // 前台用户提现
        userExtract.setUserType(3);

        if (StringUtils.isNotBlank(param.getName())) {
            userExtract.setRealName(param.getName());
        } else {
            userExtract.setRealName(userInfo.getNickname());
        }

        if (StringUtils.isNotBlank(param.getWeixin())) {
            userExtract.setWechat(param.getWeixin());
        } else {
            userExtract.setWechat(userInfo.getNickname());
        }

        String mark = "";
        if (param.getExtractType().equals("alipay")) {
            if (StrUtil.isEmpty(param.getAlipayCode())) {
                throw new ErrorRequestException("请输入支付宝账号");
            }
            userExtract.setAlipayCode(param.getAlipayCode());
            mark = "使用支付宝提现" + param.getMoney() + "元";
        } else if (param.getExtractType().equals("weixin")) {
//            if (StrUtil.isEmpty(param.getWeixin())) {
//                throw new ErrorRequestException("请输入微信账号");
//            }
            mark = "使用微信提现" + param.getMoney() + "元";
        }

        yxUserExtractMapper.insert(userExtract);

        //更新佣金 money
        userService.updateExtractMoney(uid, money);

        // 用户提现信息有未保存的保存到库里
        YxUser yxUser = new YxUser();
        yxUser.setUid(uid);
        yxUser.setBankNo(param.getBankNo());
        yxUser.setBankMobile(param.getPhone());
        yxUser.setRealName(param.getRealName());
        yxUser.setCnapsCode(param.getCnapsCode());
        this.userService.updateById(yxUser);

        // 插入一条提现是申请记录后发送mq
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", userExtract.getId() + "");
        mqProducer.messageSend2(new MessageContent(MQConstant.MITU_TOPIC, MQConstant.MITU_WITHDRAW_TAG, UUID.randomUUID().toString(), jsonObject));
    }

    @Override
    public BigDecimal extractSum(int uid) {
        return yxUserExtractMapper.sumPrice(uid);
    }

    @Override
    public YxUserExtractQueryVo getYxUserExtractById(Serializable id) throws Exception {
        return yxUserExtractMapper.getYxUserExtractById(id);
    }

    @Override
    public Paging<YxUserExtractQueryVo> getYxUserExtractPageList(YxUserExtractQueryParam yxUserExtractQueryParam) throws Exception {
//        Page page = setPageParam(yxUserExtractQueryParam, OrderItem.desc("add_time"));
//        IPage<YxUserExtractQueryVo> iPage = yxUserExtractMapper.getYxUserExtractPageList(page, yxUserExtractQueryParam);

        QueryWrapper<YxUserExtract> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(YxUserExtract::getUid, yxUserExtractQueryParam.getUid()).eq(YxUserExtract::getStatus, 1).eq(YxUserExtract::getUserType, 3);
        queryWrapper.lambda().orderByDesc(YxUserExtract::getAddTime);
        IPage<YxUserExtract> list = page(new Page<>(yxUserExtractQueryParam.getPage(), yxUserExtractQueryParam.getLimit()), queryWrapper);
        return new Paging<>(list);
    }

    /**
     * 确认提现信息
     *
     * @param param
     */
    @Override
    public YxUserExtract getConfirmOrder(PaySeachParam param) {
        QueryWrapper<YxUserExtract> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", param.getId())
                .eq("seq_no", param.getSeqNo())
                .eq("true_price", param.getTotalAmount())
                .eq("bank_code", param.getPayeeNo())
                .eq("real_name", param.getPayeeName())
                .eq("status", 0)
                .eq("add_time", param.getAddTime());

        return getOne(queryWrapper);
    }

}
