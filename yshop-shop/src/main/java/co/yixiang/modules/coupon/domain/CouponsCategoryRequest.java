package co.yixiang.modules.coupon.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 分类列表查询 实体
 * @Author : huanghui
 */
@Data
public class CouponsCategoryRequest implements Serializable {

    private String cateName;

    @ApiModelProperty(value = "当前页码")
    private int currPage = 1;

    @ApiModelProperty(value = "每页记录数")
    private int pageSize = 10;

    @ApiModelProperty(value = "分页查询参数start")
    private int limitStart;

    @ApiModelProperty(value = "分页查询参数end")
    private int limitEnd;

}
