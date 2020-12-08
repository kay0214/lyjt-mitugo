package co.yixiang.modules.system.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * HOT配置表 查询结果对象
 * </p>
 *
 * @author lsy
 * @date 2020-11-04
 */
@Data
@ApiModel(value = "YxHotConfigQueryVo对象", description = "HOT配置表查询参数")
public class YxHotConfigQueryVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    private Integer id;

    @ApiModelProperty(value = "标题名")
    private String title;

    @ApiModelProperty(value = "封面图地址")
    private String coverImg;

    @ApiModelProperty(value = "链接")
    private String linkUrl;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "状态：0：启用，1：禁用")
    private Integer status;

    @ApiModelProperty(value = "内容")
    private String content;

}