/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package co.yixiang.modules.couponUse.controller;

import co.yixiang.annotation.AnonymousAccess;
import co.yixiang.aspectj.annotation.NeedLogin;
import co.yixiang.common.web.controller.BaseController;
import co.yixiang.constant.CacheConstant;
import co.yixiang.dozer.service.IGenerator;
import co.yixiang.modules.couponUse.dto.TodayDataDto;
import co.yixiang.modules.manage.entity.SystemUser;
import co.yixiang.modules.screen.service.PlatformService;
import co.yixiang.utils.RedisUtils;
import com.alicp.jetcache.anno.CacheRefresh;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/work/platform/data")
@Api(value = "今日数据统计")
public class TodayDataController extends BaseController {

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private PlatformService platformService;

    private final IGenerator generator;

    public TodayDataController(IGenerator generator) {
        this.generator = generator;
    }


    @AnonymousAccess
    @NeedLogin
    @Cached(name="cachedGetTodayData-", expire = CacheConstant.DEFAULT_EXPIRE_TIME, cacheType = CacheType.REMOTE)
    @CacheRefresh(refresh = CacheConstant.DEFAULT_REFRESH_TIME, stopRefreshAfterLastAccess = CacheConstant.DEFAULT_STOP_REFRESH_TIME)
    @GetMapping(value = "/getTodayData")
    @ApiOperation("核销端今日数据统计")
    public Map<String, Object> getTodayData(@RequestHeader(value = "token") String token) {
        Map<String, Object> map = new HashMap<>();
        SystemUser user = getRedisUser(token);
        TodayDataDto platform = platformService.getWorkDataBySid(user.getStoreId());
        map.put("status", "1");
        map.put("statusDesc", "成功");
        map.put("data", platform);
        return map;
    }

}
