package co.yixiang.modules.offpay.service.impl;

import co.yixiang.enums.BillDetailEnum;
import co.yixiang.modules.offpay.entity.YxOffPayOrder;
import co.yixiang.modules.offpay.mapper.YxOffPayOrderMapper;
import co.yixiang.modules.offpay.service.YxOffPayOrderService;
import co.yixiang.modules.offpay.web.param.YxOffPayOrderQueryParam;
import co.yixiang.modules.offpay.web.vo.YxOffPayOrderQueryVo;
import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.mp.service.YxMiniPayService;
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

}
