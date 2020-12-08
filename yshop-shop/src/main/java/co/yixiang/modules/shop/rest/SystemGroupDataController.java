/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co

 */
package co.yixiang.modules.shop.rest;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import co.yixiang.constant.ShopConstants;
import co.yixiang.exception.BadRequestException;
import co.yixiang.logging.aop.log.Log;
import co.yixiang.modules.shop.domain.YxSystemGroupData;
import co.yixiang.modules.shop.service.YxSystemGroupDataService;
import co.yixiang.modules.shop.service.dto.IndexTitleParam;
import co.yixiang.modules.shop.service.dto.IndexTitleReqParam;
import co.yixiang.modules.shop.service.dto.YxSystemGroupDataIndexQueryCriteria;
import co.yixiang.modules.shop.service.dto.YxSystemGroupDataQueryCriteria;
import co.yixiang.utils.OrderUtil;
import co.yixiang.utils.RedisUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @author hupeng
* @date 2019-10-18
*/
@Api(tags = "商城:数据配置管理")
@RestController
@RequestMapping("api")
public class SystemGroupDataController {

    private final YxSystemGroupDataService yxSystemGroupDataService;
    @Autowired
    private RedisUtils redisUtils;

    public SystemGroupDataController(YxSystemGroupDataService yxSystemGroupDataService) {
        this.yxSystemGroupDataService = yxSystemGroupDataService;
    }

    @ApiOperation(value = "查询数据配置")
    @GetMapping(value = "/yxSystemGroupData")
    @PreAuthorize("hasAnyRole('admin','YXSYSTEMGROUPDATA_ALL','YXSYSTEMGROUPDATA_SELECT')")
    public ResponseEntity getYxSystemGroupDatas(YxSystemGroupDataQueryCriteria criteria,
                                                Pageable pageable){

        if(criteria.getStatus()!=null){
            if(criteria.getStatus().intValue()==0){
                criteria.setStatus(null);
            }
        }
        if(criteria.getGroupName()!=null && criteria.getGroupName().equals("1")){
            // 查询首页配置
            YxSystemGroupDataIndexQueryCriteria indexQueryCriteria = new YxSystemGroupDataIndexQueryCriteria();
            indexQueryCriteria.setStatus(criteria.getStatus());
            List<String> groupNames = new ArrayList<>();
            groupNames.add("local_live_module1");
            groupNames.add("local_live_module2");
            groupNames.add("local_live_module3");
            groupNames.add("local_live_module4");
            groupNames.add("local_live_module5");

            indexQueryCriteria.setGroupName(groupNames);
            Sort sort = new Sort(Sort.Direction.ASC, "sort").and(new Sort(Sort.Direction.DESC,"add_time"));
            Pageable pageableT = PageRequest.of(pageable.getPageNumber(),
                    pageable.getPageSize(),
                    sort);
            return new ResponseEntity(yxSystemGroupDataService.queryAllByIndex(indexQueryCriteria,pageableT),HttpStatus.OK);
        }else{
            Sort sort = new Sort(Sort.Direction.ASC, "sort").and(new Sort(Sort.Direction.DESC,"add_time"));
            Pageable pageableT = PageRequest.of(pageable.getPageNumber(),
                    pageable.getPageSize(),
                    sort);
            return new ResponseEntity(yxSystemGroupDataService.queryAll(criteria,pageableT),HttpStatus.OK);
        }
    }

    @Log("新增数据配置")
    @ApiOperation(value = "新增数据配置")
    @PostMapping(value = "/yxSystemGroupData")
    @CacheEvict(cacheNames = ShopConstants.YSHOP_REDIS_INDEX_KEY,allEntries = true)
    @PreAuthorize("hasAnyRole('admin','YXSYSTEMGROUPDATA_ALL','YXSYSTEMGROUPDATA_CREATE')")
    public ResponseEntity create(@RequestBody String jsonStr){
        //if(StrUtil.isNotEmpty("22")) throw new BadRequestException("演示环境禁止操作");
        JSONObject jsonObject = JSON.parseObject(jsonStr);

        if(ObjectUtil.isNotNull(jsonObject.get("name"))){
            if(StrUtil.isEmpty(jsonObject.get("name").toString())){
                throw new BadRequestException("名称必须填写");
            }
        }

        if(ObjectUtil.isNotNull(jsonObject.get("title"))){
            if(StrUtil.isEmpty(jsonObject.get("title").toString())){
                throw new BadRequestException("标题必须填写");
            }
        }

        if(ObjectUtil.isNotNull(jsonObject.get("info"))){
            if(StrUtil.isEmpty(jsonObject.get("info").toString())){
                throw new BadRequestException("简介必须填写");
            }
        }

        if(ObjectUtil.isNotNull(jsonObject.get("pic"))){
            if(StrUtil.isEmpty(jsonObject.get("pic").toString())){
                throw new BadRequestException("图片必须上传");
            }
        }


        YxSystemGroupData yxSystemGroupData = new YxSystemGroupData();
        yxSystemGroupData.setGroupName(jsonObject.get("groupName").toString());
        jsonObject.remove("groupName");
        yxSystemGroupData.setValue(jsonObject.toJSONString());
        yxSystemGroupData.setStatus(jsonObject.getInteger("status"));
        yxSystemGroupData.setSort(jsonObject.getInteger("sort"));
        yxSystemGroupData.setAddTime(OrderUtil.getSecondTimestampTwo());

        return new ResponseEntity(yxSystemGroupDataService.save(yxSystemGroupData),HttpStatus.CREATED);
    }

    @Log("修改数据配置")
    @ApiOperation(value = "修改数据配置")
    @PutMapping(value = "/yxSystemGroupData")
    @CacheEvict(cacheNames = ShopConstants.YSHOP_REDIS_INDEX_KEY,allEntries = true)
    @PreAuthorize("hasAnyRole('admin','YXSYSTEMGROUPDATA_ALL','YXSYSTEMGROUPDATA_EDIT')")
    public ResponseEntity update(@RequestBody String jsonStr){
        //if(StrUtil.isNotEmpty("22")) throw new BadRequestException("演示环境禁止操作");
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        if(ObjectUtil.isNotNull(jsonObject.get("name"))){
            if(StrUtil.isEmpty(jsonObject.get("name").toString())){
                throw new BadRequestException("名称必须填写");
            }
        }

        if(ObjectUtil.isNotNull(jsonObject.get("title"))){
            if(StrUtil.isEmpty(jsonObject.get("title").toString())){
                throw new BadRequestException("标题必须填写");
            }
        }

        if(ObjectUtil.isNotNull(jsonObject.get("pic"))){
            if(StrUtil.isEmpty(jsonObject.get("pic").toString())){
                throw new BadRequestException("图片必须上传");
            }
        }

        YxSystemGroupData yxSystemGroupData = new YxSystemGroupData();

        yxSystemGroupData.setGroupName(jsonObject.get("groupName").toString());
        jsonObject.remove("groupName");
        yxSystemGroupData.setValue(jsonObject.toJSONString());
        if(jsonObject.getInteger("status") == null){
            yxSystemGroupData.setStatus(1);
        }else{
            yxSystemGroupData.setStatus(jsonObject.getInteger("status"));
        }

        if(jsonObject.getInteger("sort") == null){
            yxSystemGroupData.setSort(0);
        }else{
            yxSystemGroupData.setSort(jsonObject.getInteger("sort"));
        }


        yxSystemGroupData.setId(Integer.valueOf(jsonObject.get("id").toString()));
        yxSystemGroupDataService.saveOrUpdate(yxSystemGroupData);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @Log("删除数据配置")
    @ApiOperation(value = "删除数据配置")
    @DeleteMapping(value = "/yxSystemGroupData/{id}")
    @PreAuthorize("hasAnyRole('admin','YXSYSTEMGROUPDATA_ALL','YXSYSTEMGROUPDATA_DELETE')")
    public ResponseEntity delete(@PathVariable Integer id){
        //if(StrUtil.isNotEmpty("22")) throw new BadRequestException("演示环境禁止操作");
        yxSystemGroupDataService.removeById(id);
        return new ResponseEntity(HttpStatus.OK);
    }



    @Log("保存首页数据配置")
    @ApiOperation(value = "保存首页数据配置")
    @PostMapping(value = "/saveIndexSystemGroupData")
    public ResponseEntity saveIndexSystemGroupData(@RequestBody IndexTitleReqParam param){

        redisUtils.set(ShopConstants.PAGE_INDEX_TITLE_1,JSONObject.toJSONString(param.getTitle_1()));
        redisUtils.set(ShopConstants.PAGE_INDEX_TITLE_2,JSONObject.toJSONString(param.getTitle_2()));
        redisUtils.set(ShopConstants.PAGE_INDEX_TITLE_3_1,JSONObject.toJSONString(param.getTitle_3_1()));
        redisUtils.set(ShopConstants.PAGE_INDEX_TITLE_3_2,JSONObject.toJSONString(param.getTitle_3_2()));
        redisUtils.set(ShopConstants.PAGE_INDEX_TITLE_3_3,JSONObject.toJSONString(param.getTitle_3_3()));

        return new ResponseEntity(HttpStatus.OK);
    }

    @Log("查询首页数据配置")
    @ApiOperation(value = "查询首页数据配置")
    @PostMapping(value = "/getIndexSystemGroupData")
    public ResponseEntity getIndexSystemGroupData(){

        IndexTitleParam title_1 = JSONObject.parseObject(redisUtils.get(ShopConstants.PAGE_INDEX_TITLE_1)+"",IndexTitleParam.class);
        IndexTitleParam title_2 = JSONObject.parseObject(redisUtils.get(ShopConstants.PAGE_INDEX_TITLE_2)+"",IndexTitleParam.class);
        IndexTitleParam title_3_1 = JSONObject.parseObject(redisUtils.get(ShopConstants.PAGE_INDEX_TITLE_3_1)+"",IndexTitleParam.class);
        IndexTitleParam title_3_2 = JSONObject.parseObject(redisUtils.get(ShopConstants.PAGE_INDEX_TITLE_3_2)+"",IndexTitleParam.class);
        IndexTitleParam title_3_3 = JSONObject.parseObject(redisUtils.get(ShopConstants.PAGE_INDEX_TITLE_3_3)+"",IndexTitleParam.class);
        Map<String,IndexTitleParam> result = new HashMap<>();
        result.put("title_1",title_1);
        result.put("title_2",title_2);
        result.put("title_3_1",title_3_1);
        result.put("title_3_2",title_3_2);
        result.put("title_3_3",title_3_3);
        return new ResponseEntity(result,HttpStatus.OK);
    }
}
