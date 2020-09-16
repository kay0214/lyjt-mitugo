/**
 * Copyright (C) 2018-2020
 */
package co.yixiang.modules.shop.service.impl;

import cn.hutool.core.util.ObjectUtil;
import co.yixiang.common.api.ApiResult;
import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.exception.ErrorRequestException;
import co.yixiang.modules.shop.entity.YxStoreCouponIssue;
import co.yixiang.modules.shop.entity.YxStoreCouponIssueUser;
import co.yixiang.modules.shop.entity.YxStoreCouponUser;
import co.yixiang.modules.shop.entity.YxStoreInfo;
import co.yixiang.modules.shop.mapper.YxStoreCouponIssueMapper;
import co.yixiang.modules.shop.mapper.YxStoreCouponIssueUserMapper;
import co.yixiang.modules.shop.service.*;
import co.yixiang.modules.shop.web.param.YxStoreCouponIssueQueryParam;
import co.yixiang.modules.shop.web.param.YxStoreCouponQueryParam;
import co.yixiang.modules.shop.web.vo.YxStoreCouponIssueQueryVo;
import co.yixiang.modules.shop.web.vo.YxStoreCouponQueryVo;
import co.yixiang.utils.DateUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.List;


/**
 * <p>
 * 优惠券前台领取表 服务实现类
 * </p>
 *
 * @author liusy
 * @since 2020-08-31
 */
@Slf4j
@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class YxStoreCouponIssueServiceImpl extends BaseServiceImpl<YxStoreCouponIssueMapper, YxStoreCouponIssue> implements YxStoreCouponIssueService {

    @Autowired
    private YxStoreCouponIssueMapper yxStoreCouponIssueMapper;
    @Autowired
    private YxStoreCouponIssueUserMapper storeCouponIssueUserMapper;
    @Autowired
    private YxStoreCouponUserService storeCouponUserService;
    @Autowired
    private YxStoreCouponIssueUserService storeCouponIssueUserService;
    @Autowired
    private YxStoreInfoService yxStoreInfoService;
    @Autowired
    private YxStoreCouponService couponService;


    /**
     * 领取优惠券
     * @param id id
     * @param uid uid
     */
    @Override
    public void issueUserCoupon(int id, int uid) {
        YxStoreCouponIssueQueryVo couponIssueQueryVo = yxStoreCouponIssueMapper
                .selectOne(id);
        if(ObjectUtil.isNull(couponIssueQueryVo)) throw new ErrorRequestException("领取的优惠劵已领完或已过期");
        int count = couponCount(id,uid);
        if(count > 0) throw new ErrorRequestException("已领取过该优惠劵");

        if(couponIssueQueryVo.getRemainCount() <= 0 && couponIssueQueryVo.getIsPermanent() == 0){
            throw new ErrorRequestException("抱歉优惠卷已经领取完了");
        }

        storeCouponUserService.addUserCoupon(uid,couponIssueQueryVo.getCid());

        storeCouponIssueUserService.addUserIssue(uid,id);

        if(couponIssueQueryVo.getTotalCount() > 0){
            yxStoreCouponIssueMapper.decCount(id);
        }

    }

    @Override
    public int couponCount(int id, int uid) {
        QueryWrapper<YxStoreCouponIssueUser> wrapper= new QueryWrapper<>();
        wrapper.eq("uid",uid).eq("issue_coupon_id",id);
        int count = storeCouponIssueUserMapper.selectCount(wrapper);
        return count;
    }

    public Integer couponUserCount(int id, int uid) {
        QueryWrapper<YxStoreCouponUser> wrapper= new QueryWrapper<YxStoreCouponUser>();
        wrapper.eq("uid",uid).eq("issue_coupon_id",id);
        List<YxStoreCouponUser> couponUserList =  storeCouponUserService.list(wrapper);
        if(!CollectionUtils.isEmpty(couponUserList)){
            return couponUserList.get(0).getStatus();
        }
        return -1;
    }

    /**
     * 优惠券列表
     * @param page
     * @param limit
     * @param uid
     * @return
     */
    @Override
    public List<YxStoreCouponIssueQueryVo> getCouponList(int page, int limit, int uid) {
        Page<YxStoreCouponIssue> pageModel = new Page<>(page, limit);
        List<YxStoreCouponIssueQueryVo> list = yxStoreCouponIssueMapper
                .selectList(pageModel);
        for (YxStoreCouponIssueQueryVo couponIssue : list) {
            //店铺名称
            YxStoreInfo storeInfo = yxStoreInfoService.getById(couponIssue.getStoreId());
            couponIssue.setStoreName(storeInfo.getStoreName());

            int count = couponCount(couponIssue.getId(),uid);

            if(count > 0){
                couponIssue.setIsUse(true);
            }else{
                couponIssue.setIsUse(false);
            }

        }
        return list;
    }

    @Override
    public YxStoreCouponIssueQueryVo getYxStoreCouponIssueById(Serializable id) throws Exception{
        return yxStoreCouponIssueMapper.getYxStoreCouponIssueById(id);
    }

    @Override
    public Paging<YxStoreCouponIssueQueryVo> getYxStoreCouponIssuePageList(YxStoreCouponIssueQueryParam yxStoreCouponIssueQueryParam) throws Exception{
        Page page = setPageParam(yxStoreCouponIssueQueryParam,OrderItem.desc("create_time"));
        IPage<YxStoreCouponIssueQueryVo> iPage = yxStoreCouponIssueMapper.getYxStoreCouponIssuePageList(page,yxStoreCouponIssueQueryParam);
        return new Paging(iPage);
    }
    /**
     * 领取优惠券
     * @param id id
     * @param uid uid
     */
    @Override
    public void issueUserCouponNew(int id, int uid) {
        YxStoreCouponIssueQueryVo couponIssueQueryVo = yxStoreCouponIssueMapper
                .selectOne(id);
        if(ObjectUtil.isNull(couponIssueQueryVo)) throw new ErrorRequestException("领取的优惠劵已领完或已过期");

        YxStoreCouponQueryVo couponQueryVo = couponService.getYxStoreCouponById(couponIssueQueryVo.getCid());

        int count = couponCount(id,uid);
        if(count > 0) throw new ErrorRequestException("已领取过该优惠劵");

        if(couponIssueQueryVo.getRemainCount() <= 0 && couponIssueQueryVo.getIsPermanent() == 0){
            throw new ErrorRequestException("抱歉优惠卷已经领取完了");
        }

        storeCouponUserService.addUserCouponNew(uid,couponIssueQueryVo.getCid(),couponQueryVo.getStoreId(),id);

        storeCouponIssueUserService.addUserIssue(uid,id);

        if(couponIssueQueryVo.getTotalCount() > 0){
            yxStoreCouponIssueMapper.decCount(id);
        }

    }

    @Override
    public List<YxStoreCouponIssueQueryVo> getCouponListByStoreId(int page, int limit, int uid,Integer storeId) {
        Page<YxStoreCouponIssue> pageModel = new Page<>(page, limit);
        List<YxStoreCouponIssueQueryVo> list = yxStoreCouponIssueMapper
                .selectCouponListByStoreId(pageModel,storeId);
        if(null==storeId){
            list =  yxStoreCouponIssueMapper
                    .selectList(pageModel);
        }
        for (YxStoreCouponIssueQueryVo couponIssue : list) {
            //店铺名称
            YxStoreInfo storeInfo = yxStoreInfoService.getById(couponIssue.getStoreId());
            couponIssue.setStoreName(storeInfo.getStoreName());

            int count = couponCount(couponIssue.getId(),uid);

            if(count > 0){
                couponIssue.setIsUse(true);
            }else{
                couponIssue.setIsUse(false);
            }

        }
        return list;
    }

    @Override
    public ApiResult<Paging<YxStoreCouponIssueQueryVo>> getYxCouponsPageListByStoreId(YxStoreCouponQueryParam yxCouponsQueryParam, Integer uid) {
        Paging<YxStoreCouponIssueQueryVo> paging =getYxCouponsPageListByStoreId(yxCouponsQueryParam);
        if (paging.getRecords() != null && paging.getRecords().size() > 0) {
            for (YxStoreCouponIssueQueryVo couponIssue : paging.getRecords()) {
                //店铺名称
                YxStoreInfo storeInfo = yxStoreInfoService.getById(couponIssue.getStoreId());
                couponIssue.setStoreName(storeInfo.getStoreName());
                int count = couponCount(couponIssue.getId(), uid);

                if (count > 0) {
                    couponIssue.setIsUse(true);
                } else {
                    couponIssue.setIsUse(false);
                }
                Integer userdFlg = couponUserCount(couponIssue.getId(),uid);
                if(ObjectUtil.isNotNull(userdFlg)){
                    couponIssue.setUsedFlg(userdFlg);
                }
                //格式化日期
                String strFormatStart = DateUtils.timestampToStr10(couponIssue.getStartTime(),DateUtils.YYYY_MM_DD);
                String strFormatEnd = DateUtils.timestampToStr10(couponIssue.getEndTime(),DateUtils.YYYY_MM_DD);
                couponIssue.setFormatEndTime(strFormatEnd);
                couponIssue.setFormatStartTime(strFormatStart);
            }
        }

        return ApiResult.ok(paging);
    }

    public Paging<YxStoreCouponIssueQueryVo> getYxCouponsPageListByStoreId(YxStoreCouponQueryParam yxCouponsQueryParam) {
        Page page = setPageParam(yxCouponsQueryParam, OrderItem.desc(" B.sort "));
        IPage<YxStoreCouponIssueQueryVo> iPage = yxStoreCouponIssueMapper.selectCouponListByStoreIdPage(page, yxCouponsQueryParam);
        iPage.setTotal(yxStoreCouponIssueMapper.getCountByStoreId(yxCouponsQueryParam));
        return new Paging(iPage);
    }
}
