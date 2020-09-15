/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package co.yixiang.modules.screen.controller;

import co.yixiang.annotation.AnonymousAccess;
import co.yixiang.common.web.controller.BaseController;
import co.yixiang.constant.ShopConstants;
import co.yixiang.dozer.service.IGenerator;
import co.yixiang.modules.screen.dto.PlatformDto;
import co.yixiang.modules.screen.service.PlatformService;
import co.yixiang.utils.RedisUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/platform/data")
@Api(value = "平台数据统计")
public class PlatformController extends BaseController {

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private PlatformService platformService;

    private final IGenerator generator;

    public PlatformController(IGenerator generator) {
        this.generator = generator;
    }


    @AnonymousAccess
    @GetMapping(value = "/getTodayData")
    @ApiOperation("今日数据统计")
    public ResponseEntity<Object> getTodayData() {
        Map<String, Object> map = new HashMap<>();

        PlatformDto platform = platformService.getTodayData();
        if (redisUtils.hasKey(ShopConstants.DATA_STATISTICS)) {
            map.put("status", "1");
            map.put("statusDesc", "成功");
            map.put("data", redisUtils.get(ShopConstants.DATA_STATISTICS));
            return ResponseEntity.ok(map);
        }
        map.put("status", "999");
        map.put("statusDesc", "未查询到数据，请稍后查看");
        return ResponseEntity.ok(map);
    }

}
