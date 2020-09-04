package co.yixiang.modules.shop.service.impl;

import cn.hutool.core.util.ObjectUtil;
import co.yixiang.common.constant.CommonConstant;
import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.util.DistanceMeterUtil;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.constant.ShopConstants;
import co.yixiang.enums.CommonEnum;
import co.yixiang.modules.coupons.service.YxCouponsService;
import co.yixiang.modules.coupons.web.param.LocalLiveQueryParam;
import co.yixiang.modules.coupons.web.vo.LocalLiveCouponsVo;
import co.yixiang.modules.coupons.web.vo.LocalLiveListVo;
import co.yixiang.modules.image.entity.YxImageInfo;
import co.yixiang.modules.image.mapper.YxImageInfoMapper;
import co.yixiang.modules.image.service.YxImageInfoService;
import co.yixiang.modules.manage.entity.DictDetail;
import co.yixiang.modules.manage.service.DictDetailService;
import co.yixiang.modules.shop.entity.YxStoreAttribute;
import co.yixiang.modules.shop.entity.YxStoreInfo;
import co.yixiang.modules.shop.mapper.YxStoreInfoMapper;
import co.yixiang.modules.shop.mapping.YxStoreInfoMap;
import co.yixiang.modules.shop.service.YxStoreAttributeService;
import co.yixiang.modules.shop.service.YxStoreInfoService;
import co.yixiang.modules.shop.service.YxStoreProductService;
import co.yixiang.modules.shop.web.param.YxStoreInfoQueryParam;
import co.yixiang.modules.shop.web.vo.YxStoreInfoDetailQueryVo;
import co.yixiang.modules.shop.web.vo.YxStoreInfoQueryVo;
import co.yixiang.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.gavaghan.geodesy.GlobalCoordinates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>
 * 店铺表 服务实现类
 * </p>
 *
 * @author hupeng
 * @since 2020-08-14
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class YxStoreInfoServiceImpl extends BaseServiceImpl<YxStoreInfoMapper, YxStoreInfo> implements YxStoreInfoService {

    @Autowired
    private YxStoreInfoMapper yxStoreInfoMapper;
    @Autowired
    private YxStoreInfoMap yxStoreInfoMap;
    @Autowired
    private DictDetailService dictDetailService;
    @Autowired
    private YxImageInfoService yxImageInfoService;
    @Autowired
    private YxStoreAttributeService yxStoreAttributeService;
    @Autowired
    private YxCouponsService yxCouponsService;
    @Autowired
    private YxStoreProductService yxStoreProductService;

    @Autowired
    private YxImageInfoMapper yxImageInfoMapper;

    @Override
    public YxStoreInfoQueryVo getYxStoreInfoById(Serializable id){
        return yxStoreInfoMapper.getYxStoreInfoById(id);
    }

    @Override
    public Paging<YxStoreInfoQueryVo> getYxStoreInfoPageList(YxStoreInfoQueryParam yxStoreInfoQueryParam) throws Exception{
        Page page = setPageParam(yxStoreInfoQueryParam,OrderItem.desc("create_time"));
        IPage<YxStoreInfoQueryVo> iPage = yxStoreInfoMapper.getYxStoreInfoPageList(page,yxStoreInfoQueryParam);
        return new Paging(iPage);
    }

    /**
     * 根据参数获取店铺信息
     * @param yxStoreInfoQueryParam
     * @return
     */
    @Override
    public List<YxStoreInfoQueryVo> getStoreInfoList(YxStoreInfoQueryParam yxStoreInfoQueryParam){
        List<YxStoreInfoQueryVo> list = new ArrayList<>();
        if(StringUtils.isBlank(yxStoreInfoQueryParam.getSalesOrder())&&StringUtils.isBlank(yxStoreInfoQueryParam.getScoreOrder())){
            //默认
            QueryWrapper<YxStoreInfo> wrapper = new QueryWrapper<YxStoreInfo>();
            wrapper.eq("del_flag", CommonEnum.DEL_STATUS_0.getValue()).eq("status",0);
            if(StringUtils.isNotBlank(yxStoreInfoQueryParam.getStoreName())){
                wrapper.like("store_name", yxStoreInfoQueryParam.getStoreName());
            }
            wrapper.orderByDesc("create_time");
            Page<YxStoreInfo> pageModel = new Page<YxStoreInfo>(yxStoreInfoQueryParam.getPage(),
                    yxStoreInfoQueryParam.getLimit());
            IPage<YxStoreInfo> pageList = yxStoreInfoMapper.selectPage(pageModel,wrapper);
            list= yxStoreInfoMap.toDto(pageList.getRecords());
        }
        if(ObjectUtil.isNotNull(yxStoreInfoQueryParam.getSalesOrder())&&StringUtils.isNotBlank(yxStoreInfoQueryParam.getSalesOrder())){
            //根据销量排序
            list= yxStoreInfoMapper.selectInfoListBySales(yxStoreInfoQueryParam.getSalesOrder(),yxStoreInfoQueryParam.getStoreName());
        }
        if(ObjectUtil.isNotNull(yxStoreInfoQueryParam.getScoreOrder())&&StringUtils.isNotBlank(yxStoreInfoQueryParam.getScoreOrder())){
            //根据评分排序
            list = yxStoreInfoMapper.selectInfoListBySocre(yxStoreInfoQueryParam.getScoreOrder(),yxStoreInfoQueryParam.getStoreName());
        }
        if(!CollectionUtils.isEmpty(list)){
            for(YxStoreInfoQueryVo yxStoreInfoQueryVo:list){
                //行业类别
                DictDetail dictDetail = dictDetailService.getDictDetailValueByType(CommonConstant.DICT_TYPE_INDUSTRY_CATEGORY,yxStoreInfoQueryVo.getIndustryCategory());
                if(null!=dictDetail){
                    yxStoreInfoQueryVo.setIndustryCategoryInfo(dictDetail.getLabel());
                }
                //店铺缩略图
                yxStoreInfoQueryVo.setStoreImage(yxImageInfoService.selectImgByParam(yxStoreInfoQueryVo.getId(),CommonConstant.IMG_TYPE_STORE,CommonConstant.IMG_CATEGORY_PIC));
            }
        }
        return list;
    }

    /**
     * 显示店铺详情
     * @param id
     * @return
     */
    @Override
    public YxStoreInfoDetailQueryVo getStoreDetailInfoById(Integer id) {
        YxStoreInfoDetailQueryVo yxStoreInfoDetailQueryVo = new YxStoreInfoDetailQueryVo();
        QueryWrapper<YxStoreInfo> wrapper = new QueryWrapper<YxStoreInfo>();
        wrapper.eq("del_flag", CommonEnum.DEL_STATUS_0.getValue()).eq("status", 0).eq("id", id);
        YxStoreInfo yxStoreInfo = this.getOne(wrapper);
        YxStoreInfoQueryVo yxStoreInfoQueryVo = yxStoreInfoMap.toDto(yxStoreInfo);
        //行业类别
        if (ObjectUtil.isNull(yxStoreInfo)) {
            throw new ErrorRequestException("店铺信息为空！");
        }
        DictDetail dictDetail = dictDetailService.getDictDetailValueByType(CommonConstant.DICT_TYPE_INDUSTRY_CATEGORY, yxStoreInfoQueryVo.getIndustryCategory());
        if (null != dictDetail) {
            yxStoreInfoQueryVo.setIndustryCategoryInfo(dictDetail.getLabel());
        }
        //店铺缩略图
        yxStoreInfoQueryVo.setStoreImage(yxImageInfoService.selectImgByParam(yxStoreInfoQueryVo.getId(), CommonConstant.IMG_TYPE_STORE, CommonConstant.IMG_CATEGORY_PIC));
        //轮播图
        yxStoreInfoQueryVo.setStoreRotationImages(yxImageInfoService.selectImgByParamList(yxStoreInfoQueryVo.getId(), CommonConstant.IMG_TYPE_STORE, CommonConstant.IMG_CATEGORY_ROTATION1));
        //店铺服务
        List<String> listService = getStroeAttribute(yxStoreInfo.getId(), 1);
        yxStoreInfoQueryVo.setStoreServiceList(listService);
        //店铺营业时间
        List<String> listOpenTiems = getStroeAttribute(yxStoreInfo.getId(), 0);
        yxStoreInfoQueryVo.setOpenTimes(listOpenTiems);
        yxStoreInfoDetailQueryVo.setSotreInfo(yxStoreInfoQueryVo);
        //卡券信息
        /*List<YxCouponsQueryVo> lsitCoupons = yxCouponsService.getCouponsInfoByStoreId(yxStoreInfo.getId());
        yxStoreInfoDetailQueryVo.setCouponsListInfo(getCouponsImg(lsitCoupons));*/
      /*  //商品信息
        yxStoreInfoDetailQueryVo.setProductListInfo(yxStoreProductService.getProductListByStoreId(yxStoreInfo.getId()));*/
        //是否有可用优惠券
        yxStoreInfoDetailQueryVo.setCouponUse(1);
        Page<YxStoreCouponIssue> pageModel = new Page<>(1, 10);
        List<YxStoreCouponIssueQueryVo> couponIssueQueryList = couponIssueMapper
                .selectCouponListByStoreId(pageModel, id);
        if (CollectionUtils.isEmpty(couponIssueQueryList)) {
            yxStoreInfoDetailQueryVo.setCouponUse(0);
        }
        return yxStoreInfoDetailQueryVo;
    }

    public List<String> getStroeAttribute(int sotreId, int attributeType) {
        List<String> listStr = new ArrayList<String>();
        QueryWrapper<YxStoreAttribute> wrapper = new QueryWrapper<YxStoreAttribute>();
        wrapper.eq("del_flag", CommonEnum.DEL_STATUS_0.getValue()).eq("store_id", sotreId).eq("attribute_type", attributeType);
        List<YxStoreAttribute> yxStoreAttributeList = yxStoreAttributeService.list(wrapper);
        if (CollectionUtils.isEmpty(yxStoreAttributeList)) {
            return null;
        }
        if (1 == attributeType) {
            //店铺服务
            for (YxStoreAttribute yxStoreAttribute : yxStoreAttributeList) {
                //行业类别
                DictDetail dictDetail = dictDetailService.getDictDetailValueByType(CommonConstant.DICT_TYPE_STORE_SERVICE, Integer.parseInt(yxStoreAttribute.getAttributeValue1()));
                if (null != dictDetail) {
                    listStr.add(dictDetail.getLabel());
                }
            }
        } else {
            for (YxStoreAttribute yxStoreAttribute : yxStoreAttributeList) {
                listStr.add(yxStoreAttribute.getAttributeValue1() + " " + yxStoreAttribute.getAttributeValue2());
            }
        }
        return listStr;
    }

    /**
     * @param localLiveQueryParam
     * @return
     * @throws Exception
     */
    @Override
    public Paging<LocalLiveListVo> getLocalLiveList(LocalLiveQueryParam localLiveQueryParam, String location) throws Exception {
        Page page = setPageParam(localLiveQueryParam, OrderItem.desc("create_time"));
        String[] locationArr = location.split(",");
        localLiveQueryParam.setLat(locationArr[1]);
        localLiveQueryParam.setLnt(locationArr[0]);
        IPage<LocalLiveListVo> iPage = yxStoreInfoMapper.getLocalLiveList(page, localLiveQueryParam);
        List<LocalLiveListVo> localLiveListVoList = iPage.getRecords();
        iPage.setTotal(localLiveListVoList.size());
        for (LocalLiveListVo localLiveListVo : localLiveListVoList){
            if(StringUtils.isNotBlank(location)) {
                /*// 设置距离
                // 维度  京都
                GlobalCoordinates source = new GlobalCoordinates(Double.parseDouble(localLiveListVo.getCoordinateY()), Double.parseDouble(localLiveListVo.getCoordinateX()));
                GlobalCoordinates target = new GlobalCoordinates(Double.parseDouble(locationArr[1]), Double.parseDouble(locationArr[0]));
                double distance = DistanceMeterUtil.getDistanceMeter(source, target);
                localLiveListVo.setDistance(distance + "");*/
            }
            QueryWrapper<YxImageInfo> imageInfoQueryWrapper = new QueryWrapper<>();
            imageInfoQueryWrapper.lambda().eq(YxImageInfo::getTypeId, localLiveListVo.getId())
                    .eq(YxImageInfo::getImgType, ShopConstants.IMG_TYPE_STORE).eq(YxImageInfo::getImgCategory, ShopConstants.IMG_CATEGORY_PIC);
            YxImageInfo yxImageInfo = yxImageInfoMapper.selectOne(imageInfoQueryWrapper);
            if (yxImageInfo != null) {
                localLiveListVo.setImg(yxImageInfo.getImgUrl());
            }
//            List<LocalLiveCouponsVo> localLiveCouponsVoList = yxCouponsService.getCouponsLitByBelog(localLiveListVo.getId());
            List<LocalLiveCouponsVo> localLiveCouponsVoList = yxCouponsService.getCouponsListByPram(localLiveListVo.getId(), localLiveQueryParam.getKeyword());

            localLiveListVo.setLocalLiveCouponsVoList(localLiveCouponsVoList);

        }
        return new Paging(iPage);
    }


    /**
     * 本地生活, 卡券表分页列表
     */
    @Override
    public ApiResult<Paging<YxCouponsQueryVo>> getYxCouponsPageListByStoreId(YxCouponsQueryParam yxCouponsQueryParam) {
        try {
            Paging<YxCouponsQueryVo> paging = yxCouponsService.getYxCouponsPageListByStoreId(yxCouponsQueryParam);
            if (paging.getRecords() != null && paging.getRecords().size() > 0) {
                for (YxCouponsQueryVo item : paging.getRecords()) {
                    // 卡券缩略图
                    YxImageInfo thumbnail = yxImageInfoService.getOne(new QueryWrapper<YxImageInfo>().eq("type_id", item.getId()).eq("img_type", LocalLiveConstants.IMG_TYPE_COUPONS)
                            .eq("img_category", ShopConstants.IMG_CATEGORY_PIC).eq("del_flag", 0));
                    if (thumbnail != null) {
                        item.setImage(thumbnail.getImgUrl());
                    }
                    // 已售销量增加虚拟销量
                    item.setTotalSales(item.getSales() + item.getFicti());
                    // 拼接有效期
                    String expireDate = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, item.getExpireDateStart()) + " ~ " + DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, item.getExpireDateEnd());
                    item.setExpireDate(expireDate);
                    item.setAvailableTime(item.getAvailableTimeStart() + " ~ " + item.getAvailableTimeEnd());
                }
            }
            return ApiResult.ok(paging);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
