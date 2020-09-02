package co.yixiang.modules.coupons.web.controller;

import co.yixiang.annotation.AnonymousAccess;
import co.yixiang.common.api.ApiResult;
import co.yixiang.common.web.controller.BaseController;
import co.yixiang.common.web.param.IdParam;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.constant.CacheConstant;
import co.yixiang.constant.LocalLiveConstants;
import co.yixiang.constant.ShopConstants;
import co.yixiang.modules.coupons.entity.YxCoupons;
import co.yixiang.modules.coupons.service.YxCouponsService;
import co.yixiang.modules.coupons.web.param.YxCouponsQueryParam;
import co.yixiang.modules.coupons.web.vo.YxCouponsQueryVo;
import co.yixiang.modules.image.entity.YxImageInfo;
import co.yixiang.modules.image.service.YxImageInfoService;
import com.alicp.jetcache.anno.CacheRefresh;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 本地生活, 卡券表 前端控制器
 * </p>
 *
 * @author hupeng
 * @since 2020-08-13
 */
@Slf4j
@RestController
@RequestMapping("/yxCoupons")
@Api(value = "本地生活, 卡券表 API")
public class YxCouponsController extends BaseController {

    @Autowired
    private YxCouponsService yxCouponsService;

    @Autowired
    private YxImageInfoService yxImageInfoService;

    /**
    * 添加本地生活, 卡券表
    */
    @PostMapping("/add")
    @ApiOperation(value = "添加YxCoupons对象",notes = "添加本地生活, 卡券表",response = ApiResult.class)
    public ApiResult<Boolean> addYxCoupons(@Valid @RequestBody YxCoupons yxCoupons) throws Exception{
        boolean flag = yxCouponsService.save(yxCoupons);
        return ApiResult.result(flag);
    }

    /**
    * 修改本地生活, 卡券表
    */
    @PostMapping("/update")
    @ApiOperation(value = "修改YxCoupons对象",notes = "修改本地生活, 卡券表",response = ApiResult.class)
    public ApiResult<Boolean> updateYxCoupons(@Valid @RequestBody YxCoupons yxCoupons) throws Exception{
        boolean flag = yxCouponsService.updateById(yxCoupons);
        return ApiResult.result(flag);
    }

    /**
    * 删除本地生活, 卡券表
    */
    @PostMapping("/delete")
    @ApiOperation(value = "删除YxCoupons对象",notes = "删除本地生活, 卡券表",response = ApiResult.class)
    public ApiResult<Boolean> deleteYxCoupons(@Valid @RequestBody IdParam idParam) throws Exception{
        boolean flag = yxCouponsService.removeById(idParam.getId());
        return ApiResult.result(flag);
    }

    /**
    * 获取本地生活, 卡券详情
    */
    @AnonymousAccess
    @PostMapping("/info")
    @ApiOperation(value = "获取YxCoupons对象详情",notes = "查看本地生活, 卡券表",response = YxCouponsQueryVo.class)
    public ApiResult<YxCouponsQueryVo> getYxCoupons(@Valid @RequestBody IdParam idParam) throws Exception {
        YxCouponsQueryVo yxCouponsQueryVo = yxCouponsService.getYxCouponsById(idParam.getId());

        if (yxCouponsQueryVo != null){
            // 总销量
            yxCouponsQueryVo.setTotalSales(yxCouponsQueryVo.getSales() + yxCouponsQueryVo.getFicti());
        }
        // 卡券缩略图
        YxImageInfo thumbnail = yxImageInfoService.getOne(new QueryWrapper<YxImageInfo>().eq("type_id", idParam.getId()).eq("img_type", LocalLiveConstants.IMG_TYPE_COUPONS)
                .eq("img_category", ShopConstants.IMG_CATEGORY_PIC).eq("del_flag", 0));
        if (thumbnail != null){
            yxCouponsQueryVo.setImage(thumbnail.getImgUrl());
        }
        // 轮播图
        List<YxImageInfo> sliderImg = yxImageInfoService.list(new QueryWrapper<YxImageInfo>().eq("type_id", idParam.getId()).eq("img_type", LocalLiveConstants.IMG_TYPE_COUPONS)
                .eq("img_category", ShopConstants.IMG_CATEGORY_ROTATION1).eq("del_flag", 0));
        List<String> sliderImages = new ArrayList<>();
        if (sliderImg.size() > 0) {
            for (YxImageInfo slider : sliderImg) {
                String imgPath = slider.getImgUrl();
                sliderImages.add(imgPath);
            }
            yxCouponsQueryVo.setSliderImage(sliderImages);
        }
        return ApiResult.ok(yxCouponsQueryVo);
    }

    /**
     * 本地生活, 卡券表分页列表
     */
    @AnonymousAccess
    @PostMapping("/getPageList")
    @ApiOperation(value = "获取YxCoupons分页列表",notes = "本地生活, 卡券表分页列表",response = YxCouponsQueryVo.class)
    public ApiResult<Paging<YxCouponsQueryVo>> getYxCouponsPageList(@Valid @RequestBody(required = false) YxCouponsQueryParam yxCouponsQueryParam) throws Exception{
        Paging<YxCouponsQueryVo> paging = yxCouponsService.getYxCouponsPageList(yxCouponsQueryParam);
        if(paging.getRecords()!=null && paging.getRecords().size()>0){
            for (YxCouponsQueryVo item:paging.getRecords()) {
                // 卡券缩略图
                YxImageInfo thumbnail = yxImageInfoService.getOne(new QueryWrapper<YxImageInfo>().eq("type_id", item.getId()).eq("img_type", LocalLiveConstants.IMG_TYPE_COUPONS)
                        .eq("img_category", ShopConstants.IMG_CATEGORY_PIC).eq("del_flag", 0));
                if (thumbnail != null){
                    item.setImage(thumbnail.getImgUrl());
                }
            }
        }
        return ApiResult.ok(paging);
    }

    /**
     * 本地生活卡券热销榜单
     * @param yxCouponsQueryParam
     * @return
     * @throws Exception
     */
    @AnonymousAccess
    @PostMapping("/getCouponsHotList")
    @Cached(name="cachedCouponsHotList-", expire = CacheConstant.DEFAULT_EXPIRE_TIME, cacheType = CacheType.BOTH)
    @CacheRefresh(refresh = CacheConstant.DEFAULT_REFRESH_TIME, stopRefreshAfterLastAccess = CacheConstant.DEFAULT_STOP_REFRESH_TIME)
    @ApiOperation(value = "本地生活卡券,热销榜单", notes = "本地生活卡券,热销榜单")
    public ApiResult<List<YxCouponsQueryVo>> getCouponsHotList(@Valid @RequestBody(required = false) YxCouponsQueryParam yxCouponsQueryParam) throws Exception{
        List<YxCouponsQueryVo> paging = yxCouponsService.getCouponsHotList(yxCouponsQueryParam);
        for (YxCouponsQueryVo item:paging) {
            // 卡券缩略图
            YxImageInfo thumbnail = yxImageInfoService.getOne(new QueryWrapper<YxImageInfo>().eq("type_id", item.getId()).eq("img_type", LocalLiveConstants.IMG_TYPE_COUPONS)
                    .eq("img_category", ShopConstants.IMG_CATEGORY_PIC).eq("del_flag", 0));
            if (thumbnail != null){
                item.setImage(thumbnail.getImgUrl());
            }
        }
        return ApiResult.ok(paging);
    }

}

