package co.yixiang.modules.offpay.service.impl;

import co.yixiang.enums.BillDetailEnum;
import co.yixiang.modules.manage.entity.SystemUser;
import co.yixiang.modules.manage.service.SystemUserService;
import co.yixiang.modules.offpay.entity.YxOffPayOrder;
import co.yixiang.modules.offpay.mapper.YxOffPayOrderMapper;
import co.yixiang.modules.offpay.service.YxOffPayOrderService;
import co.yixiang.modules.offpay.web.param.YxOffPayOrderQueryParam;
import co.yixiang.modules.offpay.web.vo.YxOffPayOrderQueryVo;
import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.modules.shop.web.vo.YxStoreInfoQueryVo;
import co.yixiang.modules.user.entity.YxUserBill;
import co.yixiang.modules.user.service.YxUserBillService;
import co.yixiang.mp.service.YxMiniPayService;
import co.yixiang.utils.DateUtils;
import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.io.Serializable;
import java.math.BigDecimal;


/**
 * <p>
 * 线下支付订单表 服务实现类
 * </p>
 *
 * @author sss
 * @since 2020-09-05
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class YxOffPayOrderServiceImpl extends BaseServiceImpl<YxOffPayOrderMapper, YxOffPayOrder> implements YxOffPayOrderService {

    @Autowired
    private YxMiniPayService miniPayService;

    @Autowired
    private YxOffPayOrderMapper yxOffPayOrderMapper;
    @Autowired
    private SystemUserService systemUserService;
    @Autowired
    private YxUserBillService yxUserBillService;


    @Override
    public YxOffPayOrderQueryVo getYxOffPayOrderById(Serializable id) throws Exception{
        return yxOffPayOrderMapper.getYxOffPayOrderById(id);
    }

    @Override
    public Paging<YxOffPayOrderQueryVo> getYxOffPayOrderPageList(YxOffPayOrderQueryParam yxOffPayOrderQueryParam) throws Exception{
        Page page = setPageParam(yxOffPayOrderQueryParam,OrderItem.desc("create_time"));
        IPage<YxOffPayOrderQueryVo> iPage = yxOffPayOrderMapper.getYxOffPayOrderPageList(page,yxOffPayOrderQueryParam);
        return new Paging(iPage);
    }

    @Override
    public WxPayMpOrderResult wxAppPay(String uuid,  String openid, BigDecimal price,String ipAddress) throws WxPayException {
        BigDecimal bigDecimal = new BigDecimal(100);

        return miniPayService.offWxPay(uuid, openid, "线下商户支付",
                bigDecimal.multiply(price).intValue(),
                BillDetailEnum.TYPE_10.getValue(), ipAddress);
    }

    @Override
    public void updatePaySuccess(YxOffPayOrder offPayOrder, YxStoreInfoQueryVo storeInfoQueryVo) {
        // 修改状态啥的
        this.updateById(offPayOrder);

        // 更新商户余额
        SystemUser systemUser = this.systemUserService.getById(storeInfoQueryVo.getMerId());
        if (null == systemUser) {
            log.error("订单编号：" + offPayOrder.getOrderId() + "未查询到商户所属的id，无法记录资金去向");
            return;
        }

        // 该笔资金实际到账
        SystemUser updateSystemUser = new SystemUser();
        updateSystemUser.setId(systemUser.getId());
        updateSystemUser.setTotalAmount(systemUser.getTotalAmount().add(offPayOrder.getTotalPrice()));
        updateSystemUser.setWithdrawalAmount(systemUser.getWithdrawalAmount().add(offPayOrder.getTotalPrice()));
        this.systemUserService.updateById(updateSystemUser);

        // 插入商户资金明细
        YxUserBill merBill = new YxUserBill();
        merBill.setUid(storeInfoQueryVo.getMerId());
        merBill.setLinkId(offPayOrder.getOrderId());
        merBill.setPm(1);
        merBill.setTitle("小程序线下支付");
        merBill.setCategory(BillDetailEnum.CATEGORY_1.getValue());
        merBill.setType(BillDetailEnum.TYPE_10.getValue());
        merBill.setNumber(offPayOrder.getTotalPrice());
        // 目前只支持微信付款、没有余额
        merBill.setBalance(updateSystemUser.getWithdrawalAmount());
        merBill.setAddTime(DateUtils.getNowTime());
        merBill.setStatus(1);
        merBill.setMerId(storeInfoQueryVo.getMerId());
        merBill.setUserType(2);
        merBill.setUsername(systemUser.getUsername());

        this.yxUserBillService.save(merBill);
    }

}
