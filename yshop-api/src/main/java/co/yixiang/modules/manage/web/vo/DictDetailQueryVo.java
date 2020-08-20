package co.yixiang.modules.manage.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

import java.util.Date;

/**
 * <p>
 * 数据字典详情 查询结果对象
 * </p>
 *
 * @author nxl
 * @date 2020-08-20
 */
@Data
@ApiModel(value = "DictDetailQueryVo对象", description = "数据字典详情查询参数")
public class DictDetailQueryVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    @ApiModelProperty(value = "字典标签")
    private String label;

    @ApiModelProperty(value = "字典值")
    private String value;

    @ApiModelProperty(value = "排序")
    private String sort;

    @ApiModelProperty(value = "字典id")
    private Long dictId;

    @ApiModelProperty(value = "创建日期")
    private Date createTime;

}