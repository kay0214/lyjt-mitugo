//package co.yixiang.modules.contract.web.controller;
//
//import co.yixiang.modules.contract.entity.YxSignInfo;
//import co.yixiang.modules.contract.service.YxSignInfoService;
//import co.yixiang.modules.contract.web.param.YxSignInfoQueryParam;
//import co.yixiang.modules.contract.web.vo.YxSignInfoQueryVo;
//import co.yixiang.common.web.controller.BaseController;
//import co.yixiang.common.api.ApiResult;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.validation.Valid;
//
//import co.yixiang.common.web.vo.Paging;
//import co.yixiang.common.web.param.IdParam;
//
///**
// * <p>
// * 签章信息表 前端控制器
// * </p>
// *
// * @author lsy
// * @since 2020-11-11
// */
//@Slf4j
//@RestController
//@RequestMapping("/yxSignInfo")
//@Api("签章信息表 API")
//public class YxSignInfoController extends BaseController {
//
//    @Autowired
//    private YxSignInfoService yxSignInfoService;
//
//    /**
//    * 添加签章信息表
//    */
//    @PostMapping("/add")
//    @ApiOperation(value = "添加YxSignInfo对象",notes = "添加签章信息表",response = ApiResult.class)
//    public ApiResult<Boolean> addYxSignInfo(@Valid @RequestBody YxSignInfo yxSignInfo) throws Exception{
//        boolean flag = yxSignInfoService.save(yxSignInfo);
//        return ApiResult.result(flag);
//    }
//
//    /**
//    * 修改签章信息表
//    */
//    @PostMapping("/update")
//    @ApiOperation(value = "修改YxSignInfo对象",notes = "修改签章信息表",response = ApiResult.class)
//    public ApiResult<Boolean> updateYxSignInfo(@Valid @RequestBody YxSignInfo yxSignInfo) throws Exception{
//        boolean flag = yxSignInfoService.updateById(yxSignInfo);
//        return ApiResult.result(flag);
//    }
//
//    /**
//    * 删除签章信息表
//    */
//    @PostMapping("/delete")
//    @ApiOperation(value = "删除YxSignInfo对象",notes = "删除签章信息表",response = ApiResult.class)
//    public ApiResult<Boolean> deleteYxSignInfo(@Valid @RequestBody IdParam idParam) throws Exception{
//        boolean flag = yxSignInfoService.removeById(idParam.getId());
//        return ApiResult.result(flag);
//    }
//
//    /**
//    * 获取签章信息表
//    */
//    @PostMapping("/info")
//    @ApiOperation(value = "获取YxSignInfo对象详情",notes = "查看签章信息表",response = YxSignInfoQueryVo.class)
//    public ApiResult<YxSignInfoQueryVo> getYxSignInfo(@Valid @RequestBody IdParam idParam) throws Exception{
//        YxSignInfoQueryVo yxSignInfoQueryVo = yxSignInfoService.getYxSignInfoById(idParam.getId());
//        return ApiResult.ok(yxSignInfoQueryVo);
//    }
//
//    /**
//     * 签章信息表分页列表
//     */
//    @PostMapping("/getPageList")
//    @ApiOperation(value = "获取YxSignInfo分页列表",notes = "签章信息表分页列表",response = YxSignInfoQueryVo.class)
//    public ApiResult<Paging<YxSignInfoQueryVo>> getYxSignInfoPageList(@Valid @RequestBody(required = false) YxSignInfoQueryParam yxSignInfoQueryParam) throws Exception{
//        Paging<YxSignInfoQueryVo> paging = yxSignInfoService.getYxSignInfoPageList(yxSignInfoQueryParam);
//        return ApiResult.ok(paging);
//    }
//
//}
//
