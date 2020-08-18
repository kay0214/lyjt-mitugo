/**
 * Copyright (C) 2018-2020
 */
package co.yixiang.modules.shop.service.impl;

import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.modules.shop.entity.YxStoreCouponIssueUser;
import co.yixiang.modules.shop.mapper.YxStoreCouponIssueUserMapper;
import co.yixiang.modules.shop.service.YxStoreCouponIssueUserService;
import co.yixiang.modules.shop.web.param.YxStoreCouponIssueUserQueryParam;
import co.yixiang.modules.shop.web.vo.YxStoreCouponIssueUserQueryVo;
import co.yixiang.utils.OrderUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;


/**
 * <p>
 * 优惠券前台用户领取记录表 服务实现类
 * </p>
 *
 * @author hupeng
 * @since 2019-10-27
 */
@Slf4j
@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class YxStoreCouponIssueUserServiceImpl extends BaseServiceImpl<YxStoreCouponIssueUserMapper, YxStoreCouponIssueUser> implements YxStoreCouponIssueUserService {

    private final YxStoreCouponIssueUserMapper yxStoreCouponIssueUserMapper;


    @Override
    public void addUserIssue(int uid, int id) {
        YxStoreCouponIssueUser couponIssueUser = new YxStoreCouponIssueUser();
        couponIssueUser.setAddTime(OrderUtil.getSecondTimestampTwo());
        couponIssueUser.setIssueCouponId(id);
        couponIssueUser.setUid(uid);
        save(couponIssueUser);
    }

    @Override
    public YxStoreCouponIssueUserQueryVo getYxStoreCouponIssueUserById(Serializable id) throws Exception{
        return yxStoreCouponIssueUserMapper.getYxStoreCouponIssueUserById(id);
    }

    @Override
    public Paging<YxStoreCouponIssueUserQueryVo> getYxStoreCouponIssueUserPageList(YxStoreCouponIssueUserQueryParam yxStoreCouponIssueUserQueryParam) throws Exception{
        Page page = setPageParam(yxStoreCouponIssueUserQueryParam,OrderItem.desc("create_time"));
        IPage<YxStoreCouponIssueUserQueryVo> iPage = yxStoreCouponIssueUserMapper.getYxStoreCouponIssueUserPageList(page,yxStoreCouponIssueUserQueryParam);
        return new Paging(iPage);
    }

}
