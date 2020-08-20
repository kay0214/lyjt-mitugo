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
 * 数据字典
 * </p>
 *
 * @author nxl
 * @since 2020-08-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "Dict对象", description = "数据字典")
public class Dict extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "字典名称")
    private String name;

    @ApiModelProperty(value = "描述")
    private String remark;

    @ApiModelProperty(value = "创建日期")
    private Date createTime;

}
