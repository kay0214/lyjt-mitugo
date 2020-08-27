/**
* Copyright (C) 2018-2020
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.coupon.service.impl;

import co.yixiang.modules.coupon.domain.YxCouponOrderDetail;
import co.yixiang.common.service.impl.BaseServiceImpl;
import lombok.AllArgsConstructor;
import co.yixiang.dozer.service.IGenerator;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import co.yixiang.common.utils.QueryHelpPlus;
import co.yixiang.utils.ValidationUtil;
import co.yixiang.utils.FileUtil;
import co.yixiang.modules.coupon.service.YxCouponOrderDetailService;
import co.yixiang.modules.coupon.service.dto.YxCouponOrderDetailDto;
import co.yixiang.modules.coupon.service.dto.YxCouponOrderDetailQueryCriteria;
import co.yixiang.modules.coupon.service.mapper.YxCouponOrderDetailMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
// 默认不使用缓存
//import org.springframework.cache.annotation.CacheConfig;
//import org.springframework.cache.annotation.CacheEvict;
//import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
* @author liusy
* @date 2020-08-26
*/
@Service
@AllArgsConstructor
//@CacheConfig(cacheNames = "yxCouponOrderDetail")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YxCouponOrderDetailServiceImpl extends BaseServiceImpl<YxCouponOrderDetailMapper, YxCouponOrderDetail> implements YxCouponOrderDetailService {

    private final IGenerator generator;

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(YxCouponOrderDetailQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<YxCouponOrderDetail> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), YxCouponOrderDetailDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<YxCouponOrderDetail> queryAll(YxCouponOrderDetailQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(YxCouponOrderDetail.class, criteria));
    }


    @Override
    public void download(List<YxCouponOrderDetailDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (YxCouponOrderDetailDto yxCouponOrderDetail : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("订单号", yxCouponOrderDetail.getOrderId());
            map.put("用户id", yxCouponOrderDetail.getUid());
            map.put("卡券id", yxCouponOrderDetail.getCouponId());
            map.put("可被核销次数", yxCouponOrderDetail.getUseCount());
            map.put("已核销次数", yxCouponOrderDetail.getUsedCount());
            map.put("卡券状态（0:待支付 1:已过期 2:待发放3:支付失败4:待使用5:已使用6:已核销7:退款中8:已退款9:退款驳回", yxCouponOrderDetail.getStatus());
            map.put("备注", yxCouponOrderDetail.getRemark());
            map.put("核销码", yxCouponOrderDetail.getVerifyCode());
            map.put("是否删除（0：未删除，1：已删除）", yxCouponOrderDetail.getDelFlag());
            map.put("创建人 根据创建人关联店铺", yxCouponOrderDetail.getCreateUserId());
            map.put("修改人", yxCouponOrderDetail.getUpdateUserId());
            map.put("创建时间", yxCouponOrderDetail.getCreateTime());
            map.put("更新时间", yxCouponOrderDetail.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
