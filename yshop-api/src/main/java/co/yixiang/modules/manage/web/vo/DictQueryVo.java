package co.yixiang.modules.manage.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

import java.util.Date;

/**
 * <p>
 * 数据字典 查询结果对象
 * </p>
 *
 * @author nxl
 * @date 2020-08-20
 */
@Data
@ApiModel(value = "DictQueryVo对象", description = "数据字典查询参数")
public class DictQueryVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    @ApiModelProperty(value = "字典名称")
    private String name;

    @ApiModelProperty(value = "描述")
    private String remark;

    @ApiModelProperty(value = "创建日期")
    private Date createTime;

}