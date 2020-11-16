package co.yixiang.modules.user.web.controller;

import cn.hutool.core.date.DateTime;
import co.yixiang.common.api.ApiResult;
import co.yixiang.common.web.controller.BaseController;
import co.yixiang.dozer.service.IGenerator;
import co.yixiang.modules.user.entity.YxLeaveMessage;
import co.yixiang.modules.user.service.YxLeaveMessageService;
import co.yixiang.modules.user.web.dto.YxLeaveMessageSaveDto;
import co.yixiang.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>
 * 客服留言表 前端控制器
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
@Slf4j
@RestController
@RequestMapping("/yxLeaveMessage")
@Api("客服留言表 API")
public class YxLeaveMessageController extends BaseController {

    @Autowired
    private YxLeaveMessageService yxLeaveMessageService;
    @Autowired
    private IGenerator generator;

//    /**
//    * 修改客服留言表
//    */
//    @PostMapping("/update")
//    @ApiOperation(value = "修改YxLeaveMessage对象",notes = "修改客服留言表",response = ApiResult.class)
//    public ApiResult<Boolean> updateYxLeaveMessage(@Valid @RequestBody YxLeaveMessage yxLeaveMessage) throws Exception{
//        boolean flag = yxLeaveMessageService.updateById(yxLeaveMessage);
//        return ApiResult.result(flag);
//    }
//
//    /**
//    * 删除客服留言表
//    */
//    @PostMapping("/delete")
//    @ApiOperation(value = "删除YxLeaveMessage对象",notes = "删除客服留言表",response = ApiResult.class)
//    public ApiResult<Boolean> deleteYxLeaveMessage(@Valid @RequestBody IdParam idParam) throws Exception{
//        boolean flag = yxLeaveMessageService.removeById(idParam.getId());
//        return ApiResult.result(flag);
//    }
//
//    /**
//    * 获取客服留言表
//    */
//    @PostMapping("/info")
//    @ApiOperation(value = "获取YxLeaveMessage对象详情",notes = "查看客服留言表",response = YxLeaveMessageQueryVo.class)
//    public ApiResult<YxLeaveMessageQueryVo> getYxLeaveMessage(@Valid @RequestBody IdParam idParam) throws Exception{
//        YxLeaveMessageQueryVo yxLeaveMessageQueryVo = yxLeaveMessageService.getYxLeaveMessageById(idParam.getId());
//        return ApiResult.ok(yxLeaveMessageQueryVo);
//    }
//    /**
//     * 客服留言表分页列表
//     */
//    @PostMapping("/getPageList")
//    @ApiOperation(value = "获取YxLeaveMessage分页列表", notes = "客服留言表分页列表", response = YxLeaveMessageQueryVo.class)
//    public ApiResult<Paging<YxLeaveMessageQueryVo>> getYxLeaveMessagePageList(@Valid @RequestBody(required = false) YxLeaveMessageQueryParam yxLeaveMessageQueryParam) throws Exception {
//        Paging<YxLeaveMessageQueryVo> paging = yxLeaveMessageService.getYxLeaveMessagePageList(yxLeaveMessageQueryParam);
//        return ApiResult.ok(paging);
//    }

    /**
     * 添加客服留言表
     */
    @PostMapping("/add")
    @ApiOperation(value = "添加YxLeaveMessage对象", notes = "添加客服留言表", response = ApiResult.class)
    public ApiResult<Boolean> addYxLeaveMessage(@Valid @RequestBody YxLeaveMessageSaveDto request) throws Exception {
        int uid = SecurityUtils.getUserId().intValue();
        request.setCreateUserId(uid);
//        boolean flag = yxLeaveMessageService.saveLeaveMessage(request);
        YxLeaveMessage saveData = generator.convert(request, YxLeaveMessage.class);
        saveData.setStatus(0);
        saveData.setDelFlag(0);
        saveData.setCreateUserId(uid);
        saveData.setCreateTime(DateTime.now().toTimestamp());
        boolean flag = yxLeaveMessageService.save(saveData);
        return ApiResult.result(flag);
    }

}

