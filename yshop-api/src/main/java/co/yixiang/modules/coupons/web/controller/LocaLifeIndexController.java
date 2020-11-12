package co.yixiang.modules.coupons.web.controller;

import co.yixiang.annotation.AnonymousAccess;
import co.yixiang.common.api.ApiResult;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.constant.CacheConstant;
import co.yixiang.constant.LocalLiveConstants;
import co.yixiang.modules.coupons.web.param.IndexTabQueryParam;
import co.yixiang.modules.coupons.web.param.LocalLiveQueryParam;
import co.yixiang.modules.coupons.web.vo.*;
import co.yixiang.modules.shop.service.YxStoreInfoService;
import co.yixiang.modules.shop.service.YxSystemGroupDataService;
import co.yixiang.modules.system.service.YxNoticeService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alicp.jetcache.anno.CacheRefresh;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private YxStoreInfoService yxStoreInfoService;

    @Autowired
    private YxNoticeService noticeService;

    /**
     * Banner 上下方导航
     * @return
     * @throws Exception
     */
    @AnonymousAccess
    @GetMapping("/getLocalLifeIndex")
    @Cached(name="getLocalLifeIndex-", expire = CacheConstant.DEFAULT_EXPIRE_TIME, cacheType = CacheType.REMOTE)
    @CacheRefresh(refresh = CacheConstant.DEFAULT_REFRESH_TIME, stopRefreshAfterLastAccess = CacheConstant.DEFAULT_STOP_REFRESH_TIME)
    @ApiOperation(value = "本地生活首页幻灯片",notes = "本地生活首页幻灯片",response = ApiResult.class)
    public ApiResult<LocalLiveIndexVo> getLocalLifeNav() throws Exception {

        LocalLiveIndexVo localLiveIndexVo = new LocalLiveIndexVo();

        // 本地生活Banner下导航
        List<LocalLifeSliderVo> menusList = yxSystemGroupDataService.getDataByGroupName("local_live_menu");

        // 本地生活Banner上导航
        List<LocalLifeSliderVo> linksList = yxSystemGroupDataService.getDataByGroupName("local_live_link");

        // 首页幻灯片
        List<LocalLifeSliderVo> sliderVos = yxSystemGroupDataService.getDataByGroupName("local_live_carousel");
        // 设置首页的 模块1 的内容
        List<LocalLifeSliderVo> module1 = yxSystemGroupDataService.getDataByGroupName("local_live_module1");
        // 设置首页的 模块2 的内容
        List<LocalLifeSliderVo> module2 = yxSystemGroupDataService.getDataByGroupName("local_live_module2");
        // 设置首页的文字
        localLiveIndexVo = yxSystemGroupDataService.setIndexTitle(localLiveIndexVo);

        // 首页通知公告
        NoticeVO noticeVO = noticeService.getIndexNotice();
        localLiveIndexVo.setNotice(noticeVO);

        localLiveIndexVo.setLocalLiveMenu(menusList);
        localLiveIndexVo.setLocalLiveLink(linksList);
        localLiveIndexVo.setSliderList(sliderVos);
        localLiveIndexVo.setModule1(module1);
        localLiveIndexVo.setModule2(module2);
        return ApiResult.ok(localLiveIndexVo);
    }

    @AnonymousAccess
    @PostMapping("/getLocalLifeIndexTab")
    @Cached(name="getLocalLifeIndexTab-", expire = CacheConstant.DEFAULT_EXPIRE_TIME, cacheType = CacheType.REMOTE)
    @CacheRefresh(refresh = CacheConstant.DEFAULT_REFRESH_TIME, stopRefreshAfterLastAccess = CacheConstant.DEFAULT_STOP_REFRESH_TIME)
    @ApiOperation(value = "本地生活首页tab数据",notes = "本地生活首页tab数据",response = ApiResult.class)
    public ApiResult<Paging<LocalLifeSliderVo>> getLocalLifeNav(@Valid @RequestBody(required = false) IndexTabQueryParam
                                                                   indexTabQueryParam) throws Exception {

        // 本地生活首页tab数据
        Paging<LocalLifeSliderVo> menusList = yxSystemGroupDataService.getDataByGroupNamePage(indexTabQueryParam);

        return ApiResult.ok(menusList);
    }

    /**
     * 热门搜索关键词
     * @return
     */
    @AnonymousAccess
    @GetMapping("/search/keyword")
    @ApiOperation(value = "热门搜索关键字获取",notes = "热门搜索关键字获取")
    public ApiResult<List<String>> search(){
        List<Map<String,Object>> list = yxSystemGroupDataService.getDatas(LocalLiveConstants.LOCAL_LIVE_HOT_SEARCH);
        List<String>  stringList = new ArrayList<>();
        for (Map<String,Object> map : list) {
            stringList.add(map.get("title").toString());
        }
        return ApiResult.ok(stringList);
    }


    /**
     * 本地生活分类列表
     * @param localLiveQueryParam
     * @return
     * @throws Exception
     */
    @AnonymousAccess
    @PostMapping("/getLocalLiveList")
    @ApiOperation(value = "获取本地生活分页列表",notes = "卡券订单表分页列表",response = YxCouponOrderQueryVo.class)
    public ApiResult<Paging<LocalLiveListVo>> getYxCouponOrderPageList(@Valid @RequestBody(required = false) LocalLiveQueryParam localLiveQueryParam
    ,@RequestHeader(value = "location", required = false) String location) throws Exception{
        Paging<LocalLiveListVo> paging = yxStoreInfoService.getLocalLiveList(localLiveQueryParam,location);
        return ApiResult.ok(paging);
    }

    @AnonymousAccess
    @GetMapping("/getHotData")
    @ApiOperation(value = "获取hot数据",notes = "获取hot数据")
    public ResponseEntity getHotData() {

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("data", getHotDataJson());
        map.put("status", 200);
        map.put("msg", "成功");
        return ResponseEntity.ok(map);
    }

    private  JSONObject getHotDataJson(){

        JSONArray jsa = new JSONArray();
        JSONObject jso = new JSONObject();
        jso.put("id",1);
        jso.put("img","https://mtcdn.metoogo.cn/config/fxdr-thumb.png");

        jso.put("url","https://mth5.metoogo.cn/jumpMinapp?url=/pages/distributor/index");



        jsa.add(jso);

        JSONObject result = new JSONObject();
        result.put("total",1);
        result.put("records",jsa);
        return result;
    }

}
