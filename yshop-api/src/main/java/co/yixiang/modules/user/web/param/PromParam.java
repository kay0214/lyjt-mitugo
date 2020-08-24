package co.yixiang.modules.user.web.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName PromParam
 * @Author hupeng <610796224@qq.com>
 * @Date 2019/11/12
 **/
@Data
public class PromParam  implements Serializable {
    // 查询推广级数 本期仅限一级推广
    private Integer grade;
    @ApiModelProperty(value = "查询昵称关键字")
    private String  keyword;
    @ApiModelProperty(value = "每页件数")
    private Integer limit;
    @ApiModelProperty(value = "页码")
    private Integer page;
    @ApiModelProperty(value = "排序字段")
    private String  sort;
}
