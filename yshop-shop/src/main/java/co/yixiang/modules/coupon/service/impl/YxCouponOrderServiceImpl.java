package co.yixiang.modules.coupon.service.impl;

import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.utils.QueryHelpPlus;
import co.yixiang.dozer.service.IGenerator;
import co.yixiang.modules.coupon.domain.YxCouponOrder;
import co.yixiang.modules.coupon.service.YxCouponOrderService;
import co.yixiang.modules.coupon.service.dto.YxCouponOrderDto;
import co.yixiang.modules.coupon.service.dto.YxCouponOrderQueryCriteria;
import co.yixiang.modules.coupon.service.mapper.YxCouponOrderMapper;
import co.yixiang.utils.FileUtil;
import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
* @author huiy
* @date 2020-08-14
*/
@Service
@AllArgsConstructor
//@CacheConfig(cacheNames = "yxCouponOrder")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YxCouponOrderServiceImpl extends BaseServiceImpl<YxCouponOrderMapper, YxCouponOrder> implements YxCouponOrderService {

    private final IGenerator generator;

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(YxCouponOrderQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<YxCouponOrder> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), YxCouponOrderDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<YxCouponOrder> queryAll(YxCouponOrderQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(YxCouponOrder.class, criteria));
    }

    @Override
    public void download(List<YxCouponOrderDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (YxCouponOrderDto yxCouponOrder : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("订单号", yxCouponOrder.getOrderId());
            map.put("用户id", yxCouponOrder.getUid());
            map.put("用户姓名", yxCouponOrder.getRealName());
            map.put("用户电话", yxCouponOrder.getUserPhone());
            map.put("订单商品总数", yxCouponOrder.getTotalNum());
            map.put("订单总价", yxCouponOrder.getTotalPrice());
            map.put("卡券id", yxCouponOrder.getCouponId());
            map.put("卡券金额", yxCouponOrder.getCouponPrice());
            map.put("支付状态 0未支付 1已支付", yxCouponOrder.getPayStaus());
            map.put("支付时间", yxCouponOrder.getPayTime());
            map.put("可被核销次数", yxCouponOrder.getUseCount());
            map.put("已核销次数", yxCouponOrder.getUsedCount());
            map.put("订单状态（0:待支付 1:已过期 2:待发放3:支付失败4:待使用5:已使用6:已核销7:退款中8:已退款9:退款驳回", yxCouponOrder.getStatus());
            map.put("0 未退款 1 申请中 2 已退款", yxCouponOrder.getRefundStatus());
            map.put("退款用户说明", yxCouponOrder.getRefundReasonWapExplain());
            map.put("退款时间", yxCouponOrder.getRefundReasonTime());
            map.put("不退款的理由", yxCouponOrder.getRefundReason());
            map.put("退款金额", yxCouponOrder.getRefundPrice());
            map.put("备注", yxCouponOrder.getMark());
            map.put("商户ID", yxCouponOrder.getMerId());
            map.put("推荐人用户ID", yxCouponOrder.getParentId());
            map.put("推荐人类型:1商户;2合伙人;3用户", yxCouponOrder.getParentType());
            map.put("分享人Id", yxCouponOrder.getShareId());
            map.put("分享人的推荐人id", yxCouponOrder.getShareParentId());
            map.put("分享人的推荐人类型", yxCouponOrder.getShareParentType());
            map.put("核销码", yxCouponOrder.getVerifyCode());
            map.put("是否删除（0：未删除，1：已删除）", yxCouponOrder.getDelFlag());
            map.put("创建人 根据创建人关联店铺", yxCouponOrder.getCreateUserId());
            map.put("修改人", yxCouponOrder.getUpdateUserId());
            map.put("创建时间", yxCouponOrder.getCreateTime());
            map.put("更新时间", yxCouponOrder.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
