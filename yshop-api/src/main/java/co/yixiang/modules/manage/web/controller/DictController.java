package co.yixiang.modules.manage.web.controller;

import co.yixiang.common.api.ApiResult;
import co.yixiang.common.web.controller.BaseController;
import co.yixiang.common.web.param.IdParam;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.modules.manage.entity.Dict;
import co.yixiang.modules.manage.service.DictService;
import co.yixiang.modules.manage.web.param.DictQueryParam;
import co.yixiang.modules.manage.web.vo.DictQueryVo;
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
 * 数据字典 前端控制器
 * </p>
 *
 * @author nxl
 * @since 2020-08-20
 */
@Slf4j
@RestController
@RequestMapping("/dict")
@Api("数据字典 API")
public class DictController extends BaseController {

    @Autowired
    private DictService dictService;

    /**
     * 添加数据字典
     */
    @PostMapping("/add")
    @ApiOperation(value = "添加Dict对象", notes = "添加数据字典", response = ApiResult.class)
    public ApiResult<Boolean> addDict(@Valid @RequestBody Dict dict) throws Exception {
        boolean flag = dictService.save(dict);
        return ApiResult.result(flag);
    }

    /**
     * 修改数据字典
     */
    @PostMapping("/update")
    @ApiOperation(value = "修改Dict对象", notes = "修改数据字典", response = ApiResult.class)
    public ApiResult<Boolean> updateDict(@Valid @RequestBody Dict dict) throws Exception {
        boolean flag = dictService.updateById(dict);
        return ApiResult.result(flag);
    }

    /**
     * 删除数据字典
     */
    @PostMapping("/delete")
    @ApiOperation(value = "删除Dict对象", notes = "删除数据字典", response = ApiResult.class)
    public ApiResult<Boolean> deleteDict(@Valid @RequestBody IdParam idParam) throws Exception {
        boolean flag = dictService.removeById(idParam.getId());
        return ApiResult.result(flag);
    }

    /**
     * 获取数据字典
     */
    @PostMapping("/info")
    @ApiOperation(value = "获取Dict对象详情", notes = "查看数据字典", response = DictQueryVo.class)
    public ApiResult<DictQueryVo> getDict(@Valid @RequestBody IdParam idParam) throws Exception {
        DictQueryVo dictQueryVo = dictService.getDictById(idParam.getId());
        return ApiResult.ok(dictQueryVo);
    }

    /**
     * 数据字典分页列表
     */
    @PostMapping("/getPageList")
    @ApiOperation(value = "获取Dict分页列表", notes = "数据字典分页列表", response = DictQueryVo.class)
    public ApiResult<Paging<DictQueryVo>> getDictPageList(@Valid @RequestBody(required = false) DictQueryParam dictQueryParam) throws Exception {
        Paging<DictQueryVo> paging = dictService.getDictPageList(dictQueryParam);
        return ApiResult.ok(paging);
    }

}

