package co.yixiang.modules.coupons.service.impl;

import cn.hutool.core.date.DateTime;
import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.constant.ShopConstants;
import co.yixiang.dozer.service.IGenerator;
import co.yixiang.exception.BadRequestException;
import co.yixiang.modules.coupons.entity.YxCouponOrder;
import co.yixiang.modules.coupons.entity.YxCouponsReply;
import co.yixiang.modules.coupons.mapper.YxCouponsReplyMapper;
import co.yixiang.modules.coupons.service.YxCouponOrderService;
import co.yixiang.modules.coupons.service.YxCouponsReplyService;
import co.yixiang.modules.coupons.web.param.YxCouponsReplyQueryParam;
import co.yixiang.modules.coupons.web.vo.YxCouponsReplyQueryVo;
import co.yixiang.modules.coupons.web.vo.couponReply.addReply.YxCouponsAddReplyRequest;
import co.yixiang.modules.coupons.web.vo.couponReply.queryReply.YxCouponsReplyDetailVO;
import co.yixiang.modules.coupons.web.vo.couponReply.queryReply.YxCouponsReplyVO;
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>
 * 本地生活评论表 服务实现类
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class YxCouponsReplyServiceImpl extends BaseServiceImpl<YxCouponsReplyMapper, YxCouponsReply> implements YxCouponsReplyService {

    @Autowired
    private YxCouponsReplyMapper yxCouponsReplyMapper;
    @Autowired
    private YxCouponOrderService yxCouponOrderService;
    @Autowired
    private YxImageInfoService yxImageInfoService;
    @Autowired
    private YxUserService yxUserService;
    @Autowired
    private IGenerator generator;


    @Override
    public YxCouponsReplyQueryVo getYxCouponsReplyById(Serializable id) throws Exception {
        return yxCouponsReplyMapper.getYxCouponsReplyById(id);
    }

    @Override
    public Paging<YxCouponsReplyQueryVo> getYxCouponsReplyPageList(YxCouponsReplyQueryParam yxCouponsReplyQueryParam) throws Exception {
        Page page = setPageParam(yxCouponsReplyQueryParam, OrderItem.desc("create_time"));
        IPage<YxCouponsReplyQueryVo> iPage = yxCouponsReplyMapper.getYxCouponsReplyPageList(page, yxCouponsReplyQueryParam);
        return new Paging(iPage);
    }

    /**
     * 提交卡券订单评价
     *
     * @param request
     * @return
     */
    @Override
    public boolean createReply(YxCouponsAddReplyRequest request) {
        // 获取订单信息
        YxCouponOrder yxCouponOrder = this.yxCouponOrderService.getOne(new QueryWrapper<YxCouponOrder>().lambda()
                .eq(YxCouponOrder::getOrderId, request.getOid())
                .eq(YxCouponOrder::getEvaluate, 0)
                .eq(YxCouponOrder::getStatus, 6));
        if (null == yxCouponOrder) {
            throw new BadRequestException("评价失败");
        }
        // 处理评价入库
        YxCouponsReply insertReply = generator.convert(request, YxCouponsReply.class);
        insertReply.setCouponId(yxCouponOrder.getCouponId());
        insertReply.setAddTime(DateUtils.getNowTime());
        insertReply.setIsReply(0);
        insertReply.setMerId(yxCouponOrder.getMerId());
        insertReply.setDelFlag(0);
        insertReply.setCreateUserId(request.getUid());
        insertReply.setCreateTime(DateTime.now().toTimestamp());
        this.save(insertReply);
        // 处理图片入库 1步骤正常来讲不会查到数据
        if (null != request.getImages() && request.getImages().size() > 0) {
            // 1.图片已存在更新所有图片状态为删除
            List<YxImageInfo> imageList = this.yxImageInfoService.list(new QueryWrapper<YxImageInfo>().lambda()
                    .eq(YxImageInfo::getDelFlag, 0)
                    .eq(YxImageInfo::getImgType, ShopConstants.IMG_TYPE_CARD)
                    .eq(YxImageInfo::getImgCategory, ShopConstants.IMG_CATEGORY_REPLY)
                    .eq(YxImageInfo::getTypeId, insertReply.getId()));
            if (null != imageList && imageList.size() > 0) {
                for (YxImageInfo item : imageList) {
                    YxImageInfo updateImage = new YxImageInfo();
                    updateImage.setId(item.getId());
                    updateImage.setDelFlag(1);
                    updateImage.setUpdateTime(DateTime.now().toTimestamp());
                    this.yxImageInfoService.updateById(updateImage);
                }
            }
            // 2.处理插入图片
            List<YxImageInfo> insertImages = new ArrayList<>();
            for (String imageUrl : request.getImages()) {
                YxImageInfo image = new YxImageInfo();
                image.setTypeId(insertReply.getId());
                image.setImgType(ShopConstants.IMG_TYPE_CARD);
                image.setImgCategory(ShopConstants.IMG_CATEGORY_REPLY);
                image.setImgUrl(imageUrl);
                image.setDelFlag(0);
                image.setCreateUserId(request.getUid());
                image.setCreateTime(DateTime.now().toTimestamp());
                insertImages.add(image);
            }
            this.yxImageInfoService.saveBatch(insertImages);
        }
        // 处理订单状态
        YxCouponOrder updateOrder = new YxCouponOrder();
        updateOrder.setId(yxCouponOrder.getId());
        updateOrder.setEvaluate(1);
        updateOrder.setUpdateTime(DateTime.now().toTimestamp());
        this.yxCouponOrderService.updateById(updateOrder);
        return true;
    }

    /**
     * 处理评价数据
     *
     * @param yxCouponsReply
     * @return
     */
    @Override
    public YxCouponsReplyVO convertCouponsReply(YxCouponsReply yxCouponsReply) {
        YxCouponsReplyVO replyVO = generator.convert(yxCouponsReply, YxCouponsReplyVO.class);
        // 处理评论用户头像
        YxUser yxUser = this.yxUserService.getById(yxCouponsReply.getUid());
        if (null != yxUser) {
            replyVO.setNickname(yxUser.getNickname());
            replyVO.setAvatar(yxUser.getAvatar());
        }
        replyVO.setAddTimeStr(DateUtils.timestampToStr10(yxCouponsReply.getAddTime()));
        replyVO.setMerchantReplyTimeStr(DateUtils.timestampToStr10(yxCouponsReply.getMerchantReplyTime()));
        return replyVO;
    }

    /**
     * 获取卡券评价详情
     *
     * @param param
     * @return
     */
    @Override
    public YxCouponsReplyDetailVO getReplyDetailList(YxCouponsReplyQueryParam param) {
        // 查询评价总数
        YxCouponsReplyDetailVO result = new YxCouponsReplyDetailVO();
        Integer totalReply = this.count(new QueryWrapper<YxCouponsReply>().lambda()
                .eq(YxCouponsReply::getCouponId, param.getCouponId())
                .eq(YxCouponsReply::getDelFlag, 0));
        // 无评价返回报错
        if (totalReply <= 0) {
            throw new BadRequestException("当前卡券暂无评价");
        }
        // 处理好评率
        Integer goodCount = this.count(new QueryWrapper<YxCouponsReply>().lambda()
                .eq(YxCouponsReply::getCouponId, param.getCouponId())
                .eq(YxCouponsReply::getDelFlag, 0)
                .gt(YxCouponsReply::getGeneralScore, 3));
        result.setGoodReplyCount(goodCount);
        result.setGoodRate(new BigDecimal(goodCount).divide(new BigDecimal(totalReply), 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)) + "%");

        // 查询中差评数
        Integer midCount = this.count(new QueryWrapper<YxCouponsReply>().lambda()
                .eq(YxCouponsReply::getCouponId, param.getCouponId())
                .eq(YxCouponsReply::getDelFlag, 0)
                .eq(YxCouponsReply::getGeneralScore, 3));
        result.setMidReplyCount(midCount);
        Integer badCount = this.count(new QueryWrapper<YxCouponsReply>().lambda()
                .eq(YxCouponsReply::getCouponId, param.getCouponId())
                .eq(YxCouponsReply::getDelFlag, 0)
                .lt(YxCouponsReply::getGeneralScore, 3));
        result.setBadReplyCount(badCount);

        // 处理评价分页数据
        IPage<YxCouponsReply> replyList = this.page(new Page<>(param.getPage(), param.getLimit()), new QueryWrapper<YxCouponsReply>().lambda()
                .eq(YxCouponsReply::getCouponId, param.getCouponId())
                .eq(YxCouponsReply::getDelFlag, 0)
                .orderByDesc(YxCouponsReply::getAddTime));
        if (null != replyList && replyList.getTotal() > 0) {
            List<YxCouponsReplyVO> replyVOs = new ArrayList<>();
            for (YxCouponsReply item : replyList.getRecords()) {
                YxCouponsReplyVO replyVO = convertCouponsReply(item);
                replyVOs.add(replyVO);
            }
            result.setReplyList(replyVOs);
        }
        return result;
    }

}
