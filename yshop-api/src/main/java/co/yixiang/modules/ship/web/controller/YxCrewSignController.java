package co.yixiang.modules.ship.web.controller;

import co.yixiang.annotation.AnonymousAccess;
import co.yixiang.common.api.ApiResult;
import co.yixiang.common.web.controller.BaseController;
import co.yixiang.common.web.param.IdParam;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.modules.manage.entity.SystemUser;
import co.yixiang.modules.ship.entity.YxCrewSign;
import co.yixiang.modules.ship.service.YxCrewSignService;
import co.yixiang.modules.ship.web.param.YxCrewSignQueryParam;
import co.yixiang.modules.ship.web.param.YxCrewSignReturnParam;
import co.yixiang.modules.ship.web.vo.YxCrewSignQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 船员签到表 前端控制器
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
@Slf4j
@RestController
@RequestMapping("/yxCrewSign")
@Api(value = "船员签到, 卡券核销", tags = "船员签到, 卡券核销")
public class YxCrewSignController extends BaseController {

    @Autowired
    private YxCrewSignService yxCrewSignService;

    /**
    * 添加船员签到表
    */
    /*@PostMapping("/add")
    @ApiOperation(value = "添加YxCrewSign对象",notes = "添加船员签到表",response = ApiResult.class)*/
    public ApiResult<Boolean> addYxCrewSign(@Valid @RequestBody YxCrewSign yxCrewSign) throws Exception{
        boolean flag = yxCrewSignService.save(yxCrewSign);
        return ApiResult.result(flag);
    }

    /**
    * 修改船员签到表
    */
    /*@PostMapping("/update")
    @ApiOperation(value = "修改YxCrewSign对象",notes = "修改船员签到表",response = ApiResult.class)*/
    public ApiResult<Boolean> updateYxCrewSign(@Valid @RequestBody YxCrewSign yxCrewSign) throws Exception{
        boolean flag = yxCrewSignService.updateById(yxCrewSign);
        return ApiResult.result(flag);
    }

    /**
    * 删除船员签到表
    */
    /*@PostMapping("/delete")
    @ApiOperation(value = "删除YxCrewSign对象",notes = "删除船员签到表",response = ApiResult.class)*/
    public ApiResult<Boolean> deleteYxCrewSign(@Valid @RequestBody IdParam idParam) throws Exception{
        boolean flag = yxCrewSignService.removeById(idParam.getId());
        return ApiResult.result(flag);
    }

    /**
    * 获取船员签到表
    */
    /*@PostMapping("/info")
    @ApiOperation(value = "获取YxCrewSign对象详情",notes = "查看船员签到表",response = YxCrewSignQueryVo.class)*/
    public ApiResult<YxCrewSignQueryVo> getYxCrewSign(@Valid @RequestBody IdParam idParam) throws Exception{
        YxCrewSignQueryVo yxCrewSignQueryVo = yxCrewSignService.getYxCrewSignById(idParam.getId());
        return ApiResult.ok(yxCrewSignQueryVo);
    }

    /**
     * 船员签到表分页列表
     */
    /*@PostMapping("/getPageList")
    @ApiOperation(value = "获取YxCrewSign分页列表",notes = "船员签到表分页列表",response = YxCrewSignQueryVo.class)*/
    public ApiResult<Paging<YxCrewSignQueryVo>> getYxCrewSignPageList(@Valid @RequestBody(required = false) YxCrewSignQueryParam yxCrewSignQueryParam) throws Exception{
        Paging<YxCrewSignQueryVo> paging = yxCrewSignService.getYxCrewSignPageList(yxCrewSignQueryParam);
        return ApiResult.ok(paging);
    }
    /**
     * 添加船员签到表
     */
    @AnonymousAccess
    @PostMapping("/captainSign")
    @ApiOperation(value = "核销端：船员签到",notes = "核销端：船员签到",response = ApiResult.class)
    public ResponseEntity<Object> captainSign(@Valid @RequestBody(required = false) Map<String,Object> mapParam,@RequestHeader(value = "token") String token){

        Map<String, Object> map = new HashMap<>();
        BigDecimal temperature = new BigDecimal(mapParam.get("temperature").toString());
        SystemUser user = getRedisUser(token);
        YxCrewSign yxCrewSign = new YxCrewSign();
        yxCrewSign.setUid(user.getId().intValue());
        yxCrewSign.setTemperature(temperature);
        yxCrewSign.setUsername(user.getUsername());
        yxCrewSign.setUserPhone(user.getPhone());
        yxCrewSign.setNickName(user.getNickName());

        map.put("status", "1");
        map.put("statusDesc", "成功");
        yxCrewSignService.save(yxCrewSign);
        return ResponseEntity.ok(map);
    }

    /**
     * 添加船员签到表
     */
    @AnonymousAccess
    @PostMapping("/signOrNot")
    @ApiOperation(value = "核销端：船员当日是否签到",notes = "核销端：船员当日是否签到",response = ApiResult.class)
    public ResponseEntity<Object> signOrNot(@RequestHeader(value = "token") String token){

        Map<String, Object> map = new HashMap<>();
        YxCrewSignReturnParam signReturnParam = new YxCrewSignReturnParam();
        signReturnParam.setSingFlg(0);
        SystemUser user = getRedisUser(token);
        Date dateNow = new Date();
        SimpleDateFormat smp = new SimpleDateFormat("yyyy-MM-dd");
        String strDateNow = smp.format(dateNow);

        QueryWrapper<YxCrewSign> signQueryWrapper = new QueryWrapper<>();
        signQueryWrapper.lambda().eq(YxCrewSign::getUid,user.getId()).between(YxCrewSign::getCreateTime,strDateNow+ " 00:00:00",strDateNow+ " 23:59:59");
        List<YxCrewSign> crewSignList = yxCrewSignService.list(signQueryWrapper);
        if(!CollectionUtils.isEmpty(crewSignList)){
            signReturnParam.setSingFlg(1);
            signReturnParam.setTemperature(crewSignList.get(0).getTemperature());
        }
        map.put("status", "1");
        map.put("statusDesc", "成功");
        map.put("data", signReturnParam);
        return ResponseEntity.ok(map);
    }
}

