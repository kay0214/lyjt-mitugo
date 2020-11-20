package co.yixiang.modules.user.web.controller;

import cn.hutool.core.date.DateTime;
import co.yixiang.common.api.ApiResult;
import co.yixiang.common.web.controller.BaseController;
import co.yixiang.common.web.param.IdParam;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.exception.BadRequestException;
import co.yixiang.modules.user.entity.YxUsedContacts;
import co.yixiang.modules.user.service.YxUsedContactsService;
import co.yixiang.modules.user.web.dto.YxUsedContactsSaveDto;
import co.yixiang.modules.user.web.dto.YxUsedContactsUpdateDto;
import co.yixiang.modules.user.web.param.YxUsedContactsQueryParam;
import co.yixiang.modules.user.web.vo.YxUsedContactsQueryVo;
import co.yixiang.utils.IdCardUtils;
import co.yixiang.utils.SecurityUtils;
import co.yixiang.utils.StringUtils;
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
 * 常用联系人表 前端控制器
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
@Slf4j
@RestController
@RequestMapping("/yxUsedContacts")
@Api("常用联系人表 API")
public class YxUsedContactsController extends BaseController {

    @Autowired
    private YxUsedContactsService yxUsedContactsService;

    /**
     * 添加常用联系人表
     */
    @PostMapping("/add")
    @ApiOperation(value = "添加YxUsedContacts对象", notes = "添加常用联系人表", response = ApiResult.class)
    public ApiResult<Boolean> addYxUsedContacts(@Valid @RequestBody YxUsedContactsSaveDto request) throws Exception {
        int uid = SecurityUtils.getUserId().intValue();
        request.setUserId(uid);
        if (StringUtils.isNotBlank(request.getCardId()) && !IdCardUtils.isValid(request.getCardId())) {
            throw new BadRequestException("请输入正确的身份证号");
        }
        boolean flag = yxUsedContactsService.saveUsedContacts(request);
        return ApiResult.result(flag);
    }

    /**
     * 修改常用联系人表
     */
    @PostMapping("/update")
    @ApiOperation(value = "修改YxUsedContacts对象", notes = "修改常用联系人表", response = ApiResult.class)
    public ApiResult<Boolean> updateYxUsedContacts(@Valid @RequestBody YxUsedContactsUpdateDto request) throws Exception {
        int uid = SecurityUtils.getUserId().intValue();
        request.setUserId(uid);
        if (StringUtils.isNotBlank(request.getCardId()) && !IdCardUtils.isValid(request.getCardId())) {
            throw new BadRequestException("请输入正确的身份证号");
        }
        boolean flag = yxUsedContactsService.updateUsedContacts(request);
        return ApiResult.result(flag);
    }

    /**
     * 删除常用联系人表
     */
    @PostMapping("/delete")
    @ApiOperation(value = "删除YxUsedContacts对象", notes = "删除常用联系人表", response = ApiResult.class)
    public ApiResult<Boolean> deleteYxUsedContacts(@Valid @RequestBody IdParam idParam) throws Exception {
        YxUsedContacts find = this.yxUsedContactsService.getById(idParam.getId());
        if (null == find || 1 == find.getDelFlag()) {
            throw new BadRequestException("当前数据不存在");
        }
        int uid = SecurityUtils.getUserId().intValue();
        YxUsedContacts delete = new YxUsedContacts();
        delete.setId(Integer.parseInt(idParam.getId()));
        delete.setDelFlag(1);
        delete.setUpdateTime(DateTime.now().toTimestamp());
        delete.setUpdateUserId(uid);
        boolean flag = yxUsedContactsService.updateById(delete);
        return ApiResult.result(flag);
    }

    /**
     * 获取常用联系人表
     */
    @PostMapping("/info")
    @ApiOperation(value = "获取YxUsedContacts对象详情", notes = "查看常用联系人表", response = YxUsedContactsQueryVo.class)
    public ApiResult<YxUsedContactsQueryVo> getYxUsedContacts(@Valid @RequestBody IdParam idParam) throws Exception {
        YxUsedContactsQueryVo yxUsedContactsQueryVo = yxUsedContactsService.getYxUsedContactsById(idParam.getId());
        return ApiResult.ok(yxUsedContactsQueryVo);
    }

    /**
     * 常用联系人表分页列表(废) 用/getUsedContactsList接口查询
     */
    @PostMapping("/getPageList")
    @ApiOperation(value = "获取YxUsedContacts分页列表", notes = "常用联系人表分页列表", response = YxUsedContactsQueryVo.class)
    public ApiResult<Paging<YxUsedContactsQueryVo>> getYxUsedContactsPageList(@Valid @RequestBody(required = false) YxUsedContactsQueryParam yxUsedContactsQueryParam) throws Exception {
        int uid = SecurityUtils.getUserId().intValue();
        yxUsedContactsQueryParam.setUserId(uid);
        Paging<YxUsedContactsQueryVo> paging = yxUsedContactsService.getYxUsedContactsPageList(yxUsedContactsQueryParam);
        return ApiResult.ok(paging);
    }

}

