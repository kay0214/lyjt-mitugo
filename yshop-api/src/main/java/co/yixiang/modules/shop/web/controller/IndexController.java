/**
 * Copyright (C) 2018-2020
 */
package co.yixiang.modules.shop.web.controller;

import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.util.StrUtil;
import co.yixiang.annotation.AnonymousAccess;
import co.yixiang.common.api.ApiResult;
import co.yixiang.constant.ShopConstants;
import co.yixiang.modules.shop.service.YxStoreProductService;
import co.yixiang.modules.shop.service.YxSystemGroupDataService;
import co.yixiang.modules.shop.service.YxSystemStoreService;
import co.yixiang.modules.shop.web.param.YxSystemStoreQueryParam;
import co.yixiang.modules.shop.web.vo.YxSystemStoreQueryVo;
import co.yixiang.mp.config.ShopKeyUtils;
import co.yixiang.utils.FileUtil;
import co.yixiang.utils.RedisUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName IndexController
 * @Author hupeng <610796224@qq.com>
 * @Date 2019/10/19
 **/

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Api(value = "首页模块", tags = "商城:首页模块", description = "首页模块")
public class IndexController {

    private final YxSystemGroupDataService systemGroupDataService;
    private final YxStoreProductService storeProductService;
    private final YxSystemStoreService systemStoreService;



    @AnonymousAccess
//    @Cacheable(cacheNames = ShopConstants.YSHOP_REDIS_INDEX_KEY)
    @GetMapping("/index")
    @ApiOperation(value = "首页数据",notes = "首页数据")
    public ApiResult<Map<String,Object>> index(){

        Map<String,Object> map = new LinkedHashMap<>();
        //banner
        map.put("banner",systemGroupDataService.getDatas(ShopConstants.SHOP_CAROUSEL));
        //首页按钮
        map.put("menus",systemGroupDataService.getDatas(ShopConstants.YSHOP_HOME_MENUS));
        //首页活动区域图片
//        map.put("activity",new String[]{});


        //精品推荐
        map.put("bastList",storeProductService.getList(1,10,1).getRecords());
        //首发新品
//        map.put("firstList",storeProductService.getList(1,6,3));
        //促销单品
//        map.put("benefit",storeProductService.getList(1,3,4));
        //热门榜单
        map.put("likeInfo",storeProductService.getList(1,10,2).getRecords());

        //滚动
//        map.put("roll",systemGroupDataService.getDatas(ShopConstants.YSHOP_HOME_ROLL_NEWS));

        map.put("mapKey",RedisUtil.get(ShopKeyUtils.getTengXunMapKey()));

        return ApiResult.ok(map);
    }

    @AnonymousAccess
    @GetMapping("/search/keyword")
    @ApiOperation(value = "热门搜索关键字获取",notes = "热门搜索关键字获取")
    public ApiResult<List<String>> search(){
        List<Map<String,Object>> list = systemGroupDataService.getDatas(ShopConstants.YSHOP_HOT_SEARCH);
        List<String>  stringList = new ArrayList<>();
        for (Map<String,Object> map : list) {
            stringList.add(map.get("title").toString());
        }
        return ApiResult.ok(stringList);
    }

    @AnonymousAccess
    @PostMapping("/image_base64")
    @ApiOperation(value = "获取图片base64",notes = "获取图片base64")
    @Deprecated
    public ApiResult<List<String>> imageBase64(){
        return ApiResult.ok(null);
    }

    @AnonymousAccess
    @GetMapping("/citys")
    @ApiOperation(value = "获取城市json",notes = "获取城市json")
    public ApiResult<JSONObject> cityJson(){
        String path = "city.json";
        String name = "city.json";
        try {
            File file = FileUtil.inputStreamToFile(new ClassPathResource(path).getStream(), name);
            FileReader fileReader = new FileReader(file,"UTF-8");
            String string = fileReader.readString();
            System.out.println(string);
            JSONObject jsonObject = JSON.parseObject(string);
            return ApiResult.ok(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();

            return ApiResult.fail("无数据");
        }

    }



    @AnonymousAccess
    @GetMapping("/store_list")
    @ApiOperation(value = "获取门店列表",notes = "获取门店列表")
    public ApiResult<Object> storeList( YxSystemStoreQueryParam param){
        Map<String,Object> map = new LinkedHashMap<>();
        List<YxSystemStoreQueryVo> lists;
        if(StrUtil.isBlank(param.getLatitude()) || StrUtil.isBlank(param.getLongitude())){
            lists = systemStoreService.getYxSystemStorePageList(param).getRecords();
        }else{
            lists = systemStoreService.getStoreList(
                    param.getLatitude(),
                    param.getLongitude(),
                    param.getPage(),param.getLimit());
        }

        map.put("list",lists);
       // map.put("mapKey",RedisUtil.get("tengxun_map_key"));
        return ApiResult.ok(map);

    }




}
