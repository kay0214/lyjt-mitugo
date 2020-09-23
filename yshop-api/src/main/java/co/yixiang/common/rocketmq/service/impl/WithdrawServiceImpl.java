/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package co.yixiang.common.rocketmq.service.impl;

import co.yixiang.common.rocketmq.entity.WithdrawInfo;
import co.yixiang.common.rocketmq.service.WithdrawService;
import co.yixiang.common.util.HttpUtils;
import co.yixiang.constant.ShopConstants;
import co.yixiang.enums.BillDetailEnum;
import co.yixiang.modules.manage.entity.SystemUser;
import co.yixiang.modules.manage.entity.YxMerchantsDetail;
import co.yixiang.modules.manage.service.YxMerchantsDetailService;
import co.yixiang.modules.user.entity.YxUser;
import co.yixiang.modules.user.entity.YxUserBill;
import co.yixiang.modules.user.entity.YxUserExtract;
import co.yixiang.modules.user.mapper.YxUserBillMapper;
import co.yixiang.modules.user.service.YxUserBillService;
import co.yixiang.modules.user.service.YxUserExtractService;
import co.yixiang.modules.user.service.YxUserService;
import co.yixiang.utils.*;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;


@Slf4j
@Service
public class WithdrawServiceImpl implements WithdrawService {

    /**
     * 旅游集团服务器ip
     */
    final static String payUrl = "http://223.99.57.253:8099/lyjt/transfer";


    @Autowired
    YxUserBillMapper yxUserBillMapper;

    @Autowired
    YxUserBillService yxUserBillService;

    @Autowired
    YxUserExtractService yxUserExtractService;

    @Autowired
    YxUserService yxUserService;

    @Autowired
    YxMerchantsDetailService yxMerchantsDetailService;
    // 提现手续费
    private final BigDecimal EXTRACT_RATE = new BigDecimal(0.006);


    @Value("${yshop.snowflake.datacenterId}")
    private Integer datacenterId;


    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateWithdraw(String id) {
        String value = RedisUtil.get(ShopConstants.WITHDRAW_USER + id);
        if (StringUtils.isNotBlank(value)) {
            log.info("重复提现，id：{}", id);
            return;
        }

        RedisUtil.set(ShopConstants.WITHDRAW_USER + id, 1, 5);
        // 查询提现申请是不是存在
        YxUserExtract userExtract = yxUserExtractService.getById(id);

        if(null == userExtract){
            log.info("提现申请不存在，id：{}", id);
            return;
        }

        if(userExtract.getStatus().intValue() != 0){
            log.info("提现申请状态错误，id：{}   status:{}", id,userExtract.getStatus());
            return;
        }

        // 查询用户的状态  是不是正常用户

        if(userExtract.getUserType().intValue() == 3){
            // 前台用户
            updateUserWithdraw(userExtract);
        }else if(userExtract.getUserType().intValue() == 1){
            // 后台用户 商户
            updateAdminUserWithdraw(userExtract);

        }

        RedisUtil.del(ShopConstants.WITHDRAW_USER + id);
    }

    /**
     * 商户提现
     * @param userExtract
     */
    private void updateAdminUserWithdraw(YxUserExtract userExtract) {
        // 检查商户状态
        SystemUser user = yxUserService.getSystemUserById(userExtract.getUid());

        if(user ==null){
            log.info("查询用户出错，id：{}", userExtract.getId());
            return;
        }

        // 提现金额不能大于用户余额
        BigDecimal userPrice = user.getWithdrawalAmount();
        // 商户得扣除微信支付的手续费 所以取 TruePrice
       /* if(userPrice.compareTo(userExtract.getTruePrice())==-1){
            log.info("提现金额不能大于余额  id:{}", userExtract.getId());
            return;
        }*/
        YxMerchantsDetail yxMerchantsDetail = this.yxMerchantsDetailService.getOne(new QueryWrapper<YxMerchantsDetail>().eq("uid", userExtract.getUid()));
        // 组装调用数据
        WithdrawInfo info = setCommonParam(userExtract);
        info.setTotalAmount(userExtract.getTruePrice().toString());
        // 0->对私账号,1->对公账号
        if(0== yxMerchantsDetail.getBankType().intValue()){
            // 对私账号
            // 1普通账户；2对公账户
            info.setPayerAccttype(1);
        }else {
            //  对公账号
            // 1普通账户；2对公账户
            info.setPayerAccttype(2);
            // 联行号
            info.setBankCode(yxMerchantsDetail.getOpenAccountBank());
            // 开户行
            info.setBankName(yxMerchantsDetail.getOpenAccountSubbranch());
        }

        String result = sendPayRequest(info);


        if("ok".equals(result)){
            // 成功
            log.info("开始执行商户的 提现成功操作");
            updateMerInfo(userExtract,user);
        }else {
            // 失败
            log.info("开始执行商户的 提现失败操作");
            updateWithdrawFaild(userExtract,result);
        }

    }

    /**
     * 商户提现成功操作
     */
    private void updateMerInfo(YxUserExtract yxUserExtract, SystemUser user) {
        String username = user.getNickName();

        // 商户提现扣减手续费
        BigDecimal truePrice = yxUserExtract.getExtractPrice().subtract(yxUserExtract.getExtractPrice().multiply(EXTRACT_RATE));
        saveBill(yxUserExtract, username);

        // 更新审核记录
        updateUserExtract(yxUserExtract, truePrice);
    }

    /**
     * 保存交易流水
     * @param yxUserExtract
     * @param username
     */
    private void saveBill(YxUserExtract yxUserExtract, String username) {
        // 保存流水
        YxUserBill yxUserBill = new YxUserBill();
        yxUserBill.setUid(yxUserExtract.getUid());
        yxUserBill.setUsername(username);
        yxUserBill.setLinkId(yxUserExtract.getId() + "");
        //0 = 支出 1 = 获得
        yxUserBill.setPm(0);
        yxUserBill.setTitle("用户提现");
        yxUserBill.setCategory(BillDetailEnum.CATEGORY_1.getValue());
        yxUserBill.setType(BillDetailEnum.TYPE_4.getValue());
        yxUserBill.setNumber(yxUserExtract.getExtractPrice());
        yxUserBill.setBalance(BigDecimal.ZERO);
        yxUserBill.setMark("自动提现");
        yxUserBill.setAddTime(OrderUtil.getSecondTimestampTwo());
        yxUserBill.setStatus(1);
        yxUserBill.setUserType(yxUserExtract.getUserType());
        this.yxUserBillService.save(yxUserBill);
    }


    /**
     * 提现失败操作
     * @param userExtract
     * @param result
     */
    private void updateWithdrawFaild(YxUserExtract userExtract, String result) {
        String resultStr = result;
        if(result.length()>20){
            resultStr = result.substring(0,result.length()-1);
        }
        userExtract.setFailMsg(resultStr);
        userExtract.setRetMess(resultStr);
        // -1 未通过 0 审核中 1 已提现  2处理中  3提现失败
        userExtract.setStatus(3);
        userExtract.setFailTime(DateUtils.getNowTime());
        yxUserExtractService.updateById(userExtract);

        YxUser updateYxUser = new YxUser();
        updateYxUser.setUid(userExtract.getUid());
        updateYxUser.setNowMoney(userExtract.getExtractPrice());
        // 恢复用户余额
        if (3 == userExtract.getUserType()) {
            // 前台用户
            this.yxUserService.updateUserMoney(updateYxUser);
        } else {
            this.yxUserService.updateMerMoney(updateYxUser);
        }

    }

    /**
     * 前台用户提现
     * @param userExtract
     */
    private void updateUserWithdraw(YxUserExtract userExtract) {
        // 检查用户状态
        YxUser user = yxUserService.getById(userExtract.getUid());
        if(user == null){
            log.info("未查询到用户，id：{}", userExtract.getId());
            return;
        }
        if(user.getStatus().intValue() == 0){
            log.info("用户状态错误，id：{}   用户:{}", userExtract.getId(),user.getUid());
            return;
        }

        // 提现金额不能大于用户余额
        BigDecimal userPrice = user.getBrokeragePrice();
       /* if(userPrice.compareTo(userExtract.getExtractPrice())==-1){
            log.info("提现金额不能大于余额  id:{}", userExtract.getId());
            return;
        }*/

        // 组装调用数据
        WithdrawInfo info = setCommonParam(userExtract);
        info.setTotalAmount(userExtract.getExtractPrice().toString());
        // 1普通账户；2对公账户
        info.setPayerAccttype(1);

        String result = sendPayRequest(info);
        if("ok".equals(result)){
            // 成功
            log.info("开始执行用户的 提现成功操作");
            updateUserInfo(userExtract,user);
        }else {
            // 失败
            updateWithdrawFaild(userExtract,result);
        }
    }

    /**
     * 用户提现成功操作
     * @param user
     */
    private void updateUserInfo(YxUserExtract yxUserExtract, YxUser user) {

        String username = user.getNickname();

        // 商户提现扣减手续费
        saveBill(yxUserExtract, username);
        updateUserExtract(yxUserExtract, yxUserExtract.getExtractPrice());

    }

    private void updateUserExtract(YxUserExtract yxUserExtract, BigDecimal extractPrice) {
        // 更新审核记录
        yxUserExtract.setId(yxUserExtract.getId());
        yxUserExtract.setStatus(1);
        yxUserExtract.setMark("自动提现");
        // 记录实际到账金额
        yxUserExtract.setTruePrice(extractPrice);

        this.yxUserExtractService.updateById(yxUserExtract);
    }

    /**
     * 发送请求
     * @param info
     * @return
     */
    private String sendPayRequest(WithdrawInfo info) {
        //return "ok";
        return HttpUtils.postJson(payUrl,JSONObject.toJSONString(info));
    }

    /**
     * 设置调用的公共参数
     * @param userExtract
     * @return
     */
    private WithdrawInfo setCommonParam(YxUserExtract userExtract ) {
        WithdrawInfo info = new WithdrawInfo();
        // 生成订单号
        String uuid = SnowflakeUtil.getOrderId(datacenterId);
        userExtract.setSeqNo(uuid);

        info.setId(userExtract.getId());
        info.setAddTime(userExtract.getAddTime()+"");
        info.setSeqNo(uuid);
        info.setPayeeMobile(userExtract.getBankMobile());
        // 银行卡
        info.setPayeeNo(userExtract.getBankCode());
        info.setPayeeName(userExtract.getRealName());
        // 联行号
        info.setBankCode(userExtract.getCnapsCode());
        return info;
    }

}