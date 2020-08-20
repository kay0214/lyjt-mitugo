package co.yixiang.modules.manage.entity;

import co.yixiang.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * <p>
 * 数据字典详情
 * </p>
 *
 * @author nxl
 * @since 2020-08-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "DictDetail对象", description = "数据字典详情")
public class DictDetail extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
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
