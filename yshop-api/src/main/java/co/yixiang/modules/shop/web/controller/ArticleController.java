/**
 * Copyright (C) 2018-2020
 */
package co.yixiang.modules.shop.web.controller;

import co.yixiang.annotation.AnonymousAccess;
import co.yixiang.common.api.ApiResult;
import co.yixiang.common.web.controller.BaseController;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.modules.shop.service.ArticleService;
import co.yixiang.modules.shop.web.param.YxArticleQueryParam;
import co.yixiang.modules.shop.web.vo.YxArticleQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 文章 前端控制器
 * </p>
 *
 * @author hupeng
 * @since 2019-10-02
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/article")
@Api(value = "文章模块", tags = "商城:文章模块", description = "文章模块")
public class ArticleController extends BaseController {

    private final ArticleService articleService;


    /**
    * 获取文章文章详情
    */
    @AnonymousAccess
    @GetMapping("/details/{id}")
    @ApiOperation(value = "文章详情",notes = "文章详情",response = YxArticleQueryVo.class)
    public ApiResult<YxArticleQueryVo> getYxArticle(@PathVariable Integer id) throws Exception{
        YxArticleQueryVo yxArticleQueryVo = articleService.getYxArticleById(id);
        articleService.incVisitNum(id);
        return ApiResult.ok(yxArticleQueryVo);
    }

    /**
     * 文章列表
     */
    @AnonymousAccess
    @GetMapping("/list")
    @ApiOperation(value = "文章列表",notes = "文章列表",response = YxArticleQueryVo.class)
    public ApiResult<List<YxArticleQueryVo>> getYxArticlePageList(YxArticleQueryParam queryParam){
        Paging<YxArticleQueryVo> paging = articleService.getYxArticlePageList(queryParam);
        return ApiResult.ok(paging.getRecords());
    }

}

