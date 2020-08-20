package co.yixiang.modules.manage.web.controller;

import co.yixiang.common.api.ApiResult;
import co.yixiang.common.web.controller.BaseController;
import co.yixiang.common.web.param.IdParam;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.modules.manage.entity.DictDetail;
import co.yixiang.modules.manage.service.DictDetailService;
import co.yixiang.modules.manage.web.param.DictDetailQueryParam;
import co.yixiang.modules.manage.web.vo.DictDetailQueryVo;
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
 * 数据字典详情 前端控制器
 * </p>
 *
 * @author nxl
 * @since 2020-08-20
 */
@Slf4j
@RestController
@RequestMapping("/dictDetail")
@Api("数据字典详情 API")
public class DictDetailController extends BaseController {

    @Autowired
    private DictDetailService dictDetailService;

    /**
     * 添加数据字典详情
     */
    @PostMapping("/add")
    @ApiOperation(value = "添加DictDetail对象", notes = "添加数据字典详情", response = ApiResult.class)
    public ApiResult<Boolean> addDictDetail(@Valid @RequestBody DictDetail dictDetail) throws Exception {
        boolean flag = dictDetailService.save(dictDetail);
        return ApiResult.result(flag);
    }

    /**
     * 修改数据字典详情
     */
    @PostMapping("/update")
    @ApiOperation(value = "修改DictDetail对象", notes = "修改数据字典详情", response = ApiResult.class)
    public ApiResult<Boolean> updateDictDetail(@Valid @RequestBody DictDetail dictDetail) throws Exception {
        boolean flag = dictDetailService.updateById(dictDetail);
        return ApiResult.result(flag);
    }

    /**
     * 删除数据字典详情
     */
    @PostMapping("/delete")
    @ApiOperation(value = "删除DictDetail对象", notes = "删除数据字典详情", response = ApiResult.class)
    public ApiResult<Boolean> deleteDictDetail(@Valid @RequestBody IdParam idParam) throws Exception {
        boolean flag = dictDetailService.removeById(idParam.getId());
        return ApiResult.result(flag);
    }

    /**
     * 获取数据字典详情
     */
    @PostMapping("/info")
    @ApiOperation(value = "获取DictDetail对象详情", notes = "查看数据字典详情", response = DictDetailQueryVo.class)
    public ApiResult<DictDetailQueryVo> getDictDetail(@Valid @RequestBody IdParam idParam) throws Exception {
        DictDetailQueryVo dictDetailQueryVo = dictDetailService.getDictDetailById(idParam.getId());
        return ApiResult.ok(dictDetailQueryVo);
    }

    /**
     * 数据字典详情分页列表
     */
    @PostMapping("/getPageList")
    @ApiOperation(value = "获取DictDetail分页列表", notes = "数据字典详情分页列表", response = DictDetailQueryVo.class)
    public ApiResult<Paging<DictDetailQueryVo>> getDictDetailPageList(@Valid @RequestBody(required = false) DictDetailQueryParam dictDetailQueryParam) throws Exception {
        Paging<DictDetailQueryVo> paging = dictDetailService.getDictDetailPageList(dictDetailQueryParam);
        return ApiResult.ok(paging);
    }

}

