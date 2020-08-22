package co.yixiang.modules.coupons.web.controller;

import co.yixiang.annotation.AnonymousAccess;
import co.yixiang.common.api.ApiResult;
import co.yixiang.modules.coupons.web.vo.LocalLifeSliderVo;
import co.yixiang.modules.coupons.web.vo.LocalLiveIndexVo;
import co.yixiang.modules.shop.entity.SystemGroupDataValue;
import co.yixiang.modules.shop.entity.YxSystemGroupData;
import co.yixiang.modules.shop.service.YxSystemGroupDataService;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 本地生活首页 控制器
 * @Author : huanghui
 */
@Slf4j
@RestController
@RequestMapping("/localLife")
@Api(value = "本地生活, 首页 API")
public class LocaLifeIndexController {

    @Autowired
    private YxSystemGroupDataService yxSystemGroupDataService;

    /**
     * Banner 上下方导航
     * @return
     * @throws Exception
     */
    @AnonymousAccess
    @GetMapping("/getLocalLifeIndex")
    @ApiOperation(value = "本地生活首页幻灯片",notes = "本地生活首页幻灯片",response = ApiResult.class)
    public ApiResult<LocalLiveIndexVo> getLocalLifeNav() throws Exception {
        // 本地生活Banner下导航
        List<YxSystemGroupData> localLiveMenu = yxSystemGroupDataService.list(new QueryWrapper<YxSystemGroupData>()
                .eq("group_name", "local_live_menu").eq("status", 1).orderByAsc("sort"));

        LocalLiveIndexVo localLiveIndexVo = new LocalLiveIndexVo();

        List<LocalLifeSliderVo> menusList = new ArrayList<>();
        if (localLiveMenu != null){
            for (YxSystemGroupData menu : localLiveMenu){
                LocalLifeSliderVo liveMenuVo = new LocalLifeSliderVo();
                String jsonString = menu.getValue();

                SystemGroupDataValue sliderSystemData = JSON.parseObject(jsonString, SystemGroupDataValue.class);
                BeanUtils.copyProperties(sliderSystemData, liveMenuVo);
                menusList.add(liveMenuVo);
            }
        }
        // 本地生活Banner上导航
        List<YxSystemGroupData> localLiveLink = yxSystemGroupDataService.list(new QueryWrapper<YxSystemGroupData>()
                .eq("group_name", "local_live_link").eq("status", 1).orderByAsc("sort"));

        List<LocalLifeSliderVo> linksList = new ArrayList<>();
        if (localLiveLink != null){
            for (YxSystemGroupData links : localLiveLink){
                LocalLifeSliderVo liveLinkVo = new LocalLifeSliderVo();
                String jsonString = links.getValue();

                SystemGroupDataValue sliderSystemData = JSON.parseObject(jsonString, SystemGroupDataValue.class);
                BeanUtils.copyProperties(sliderSystemData, liveLinkVo);
                linksList.add(liveLinkVo);
            }
        }

        // 首页幻灯片
        List<YxSystemGroupData> groupDataList = yxSystemGroupDataService.list(new QueryWrapper<YxSystemGroupData>()
                .eq("group_name", "local_live_carousel").eq("status", 1).orderByAsc("sort"));

        List<LocalLifeSliderVo> sliderVos = new ArrayList<>();
        if (groupDataList != null){
            for (YxSystemGroupData yxSystemGroupData : groupDataList){
                LocalLifeSliderVo localLifeSliderVo = new LocalLifeSliderVo();
                String value = yxSystemGroupData.getValue();
                SystemGroupDataValue sliderSystemData = JSON.parseObject(value, SystemGroupDataValue.class);
                BeanUtils.copyProperties(sliderSystemData, localLifeSliderVo);
                sliderVos.add(localLifeSliderVo);
            }
        }

        localLiveIndexVo.setLocalLiveMenu(menusList);
        localLiveIndexVo.setLocalLiveLink(linksList);
        localLiveIndexVo.setSliderList(sliderVos);
        return ApiResult.ok(localLiveIndexVo);
    }

    // 获取本地生活幻灯片
//    @GetMapping("/getSliderImages")
//    @ApiOperation(value = "本地生活首页幻灯片",notes = "本地生活首页幻灯片",response = ApiResult.class)
//    public ApiResult<List<LocalLifeSliderVo>> getSliderImages() throws Exception{
//
//        return ApiResult.ok(sliderVos);
//    }



}
