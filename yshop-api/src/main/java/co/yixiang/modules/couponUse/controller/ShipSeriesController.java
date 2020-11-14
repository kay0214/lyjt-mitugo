package co.yixiang.modules.couponUse.controller;

import co.yixiang.annotation.AnonymousAccess;
import co.yixiang.common.web.controller.BaseController;
import co.yixiang.modules.couponUse.dto.YxShipSeriesVO;
import co.yixiang.modules.manage.entity.SystemUser;
import co.yixiang.modules.ship.entity.YxShipSeries;
import co.yixiang.modules.ship.service.YxShipSeriesService;
import co.yixiang.modules.ship.web.vo.YxShipSeriesQueryVo;
import co.yixiang.modules.user.service.YxUserService;
import co.yixiang.utils.CommonsUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 船只系列表 前端控制器
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
@Slf4j
@RestController
@RequestMapping("/shipSeries")
@Api(value = "核销端：船只系列", tags = "核销端：船只系列")
public class ShipSeriesController extends BaseController {

    @Autowired
    private YxShipSeriesService yxShipSeriesService;
    @Autowired
    private YxUserService yxUserService;

    @AnonymousAccess
    @PostMapping("/getShipSeriesList")
    @ApiOperation(value = "获取系列列表",notes = "获取系列列表",response = YxShipSeriesQueryVo.class)
    public ResponseEntity<Object> getShipSeriesList(@RequestHeader(value = "token") String token) throws Exception {
        //当前登录用户
        Map<String, Object> map = new HashMap<>();

        map.put("status", "99");
        map.put("statusDesc", "暂无船只系列信息！");
        SystemUser user = getRedisUser(token);
        //测试用
//        SystemUser user = yxUserService.getSystemUserByParam(148);

        QueryWrapper<YxShipSeries> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(YxShipSeries::getStoreId, user.getStoreId()).eq(YxShipSeries::getStatus, 0).eq(YxShipSeries::getDelFlag, 0);
        List<YxShipSeries> shipSeriesList = yxShipSeriesService.list(queryWrapper);
        List<YxShipSeriesVO> voList = new ArrayList<YxShipSeriesVO>();
        if (!CollectionUtils.isEmpty(shipSeriesList)) {
            voList = CommonsUtils.convertBeanList(shipSeriesList, YxShipSeriesVO.class);
            map.put("status", "1");
            map.put("statusDesc", "成功");
            map.put("data", voList);
        }
        return ResponseEntity.ok(map);
    }

}

