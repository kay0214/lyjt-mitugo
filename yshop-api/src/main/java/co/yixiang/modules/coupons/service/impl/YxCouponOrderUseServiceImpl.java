package co.yixiang.modules.coupons.service.impl;

import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.constant.ShopConstants;
import co.yixiang.dozer.service.IGenerator;
import co.yixiang.modules.couponUse.criteria.YxCouponOrderUseQueryCriteria;
import co.yixiang.modules.couponUse.dto.YxCouponOrderUseDto;
import co.yixiang.modules.coupons.entity.YxCouponOrder;
import co.yixiang.modules.coupons.entity.YxCouponOrderUse;
import co.yixiang.modules.coupons.entity.YxCoupons;
import co.yixiang.modules.coupons.mapper.YxCouponOrderMapper;
import co.yixiang.modules.coupons.mapper.YxCouponOrderUseMapper;
import co.yixiang.modules.coupons.mapper.YxCouponsMapper;
import co.yixiang.modules.coupons.service.YxCouponOrderUseService;
import co.yixiang.modules.coupons.web.param.YxCouponOrderUseQueryParam;
import co.yixiang.modules.coupons.web.vo.YxCouponOrderUseQueryVo;
import co.yixiang.modules.image.entity.YxImageInfo;
import co.yixiang.modules.image.service.YxImageInfoService;
import co.yixiang.modules.user.entity.YxUser;
import co.yixiang.modules.user.service.YxUserService;
import co.yixiang.utils.DateUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * 用户地址表 服务实现类
 * </p>
 *
 * @author hupeng
 * @since 2020-08-13
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class YxCouponOrderUseServiceImpl extends BaseServiceImpl<YxCouponOrderUseMapper, YxCouponOrderUse> implements YxCouponOrderUseService {

    @Autowired
    private YxCouponOrderUseMapper yxCouponOrderUseMapper;
    @Autowired
    private YxCouponsMapper yxCouponsMapper;
    @Autowired
    private YxCouponOrderMapper yxCouponOrderMapper;
    @Autowired
    private YxUserService yxUserService;
    @Autowired
    private YxImageInfoService yxImageInfoService;

    private final IGenerator generator;

    public YxCouponOrderUseServiceImpl(IGenerator generator) {
        this.generator = generator;
    }

    @Override
    public YxCouponOrderUseQueryVo getYxCouponOrderUseById(Serializable id) throws Exception {
        return yxCouponOrderUseMapper.getYxCouponOrderUseById(id);
    }

    @Override
    public Paging<YxCouponOrderUseQueryVo> getYxCouponOrderUsePageList(YxCouponOrderUseQueryParam yxCouponOrderUseQueryParam) throws Exception {
        Page page = setPageParam(yxCouponOrderUseQueryParam, OrderItem.desc("create_time"));
        IPage<YxCouponOrderUseQueryVo> iPage = yxCouponOrderUseMapper.getYxCouponOrderUsePageList(page, yxCouponOrderUseQueryParam);
        return new Paging(iPage);
    }

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(YxCouponOrderUseQueryCriteria criteria) {
//        getPage(pageable);
//        PageInfo<YxCouponOrderUse> page = new PageInfo<>(baseMapper.selectList(QueryHelpPlus.getPredicate(YxCouponOrderUse.class, criteria)));
        Page page = setPageParam(criteria, OrderItem.desc("id"));

        IPage<YxCouponOrderUse> ipage = this.page(page, new QueryWrapper<YxCouponOrderUse>().lambda().eq(YxCouponOrderUse::getStoreId, criteria.getStoreId()));
        if (ipage.getTotal() == 0) {
            Map<String, Object> map = new LinkedHashMap<>(2);
            map.put("content", new ArrayList<>());
            map.put("totalElements", 0);
            map.put("status", "1");
            map.put("statusDesc", "成功");
            return map;
        }
        List<YxCouponOrderUseDto> list = new ArrayList<>();
        for (YxCouponOrderUse item : ipage.getRecords()) {
            YxCouponOrderUseDto dto = generator.convert(item, YxCouponOrderUseDto.class);
            YxCouponOrder yxCouponOrder = this.yxCouponOrderMapper.selectOne(new QueryWrapper<YxCouponOrder>().lambda().eq(YxCouponOrder::getOrderId, item.getOrderId()));
            if (null != yxCouponOrder) {
                dto.setCouponId(yxCouponOrder.getCouponId());
                YxUser yxUser = this.yxUserService.getOne(new QueryWrapper<YxUser>().lambda().eq(YxUser::getUid, yxCouponOrder.getUid()));
                if (null != yxUser) {
                    dto.setNickName(yxUser.getNickname());
                }
            }
            YxCoupons yxCoupons = this.yxCouponsMapper.selectById(yxCouponOrder.getCouponId());
            if (null != yxCoupons) {
                dto.setCouponType(yxCoupons.getCouponType());
                dto.setDenomination(yxCoupons.getDenomination());
                dto.setDiscount(yxCoupons.getDiscount());
                dto.setThreshold(yxCoupons.getThreshold());
                dto.setDiscountAmount(yxCoupons.getDiscountAmount());
                dto.setCouponName(yxCoupons.getCouponName());
                // 获取卡券缩略图
                YxImageInfo imageInfo = this.yxImageInfoService.selectOneImg(yxCoupons.getId(), ShopConstants.IMG_TYPE_CARD, ShopConstants.IMG_CATEGORY_PIC);
                if (null != imageInfo) {
                    dto.setCouponImage(imageInfo.getImgUrl());
                }
            }
            dto.setUseTime(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM, item.getCreateTime()));
            list.add(dto);
        }
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", list);
        map.put("totalElements", ipage.getTotal());
        map.put("status", "1");
        map.put("statusDesc", "成功");
        return map;
    }
}
