package co.yixiang.modules.bank.entity;

import co.yixiang.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 银行机构编码
 * </p>
 *
 * @author sss
 * @since 2020-09-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="BankCnaps对象", description="银行机构编码")
public class BankCnaps extends BaseEntity {

    private static final long serialVersionUID = 1L;

@TableId(value = "id", type = IdType.AUTO)
private Integer id;

@ApiModelProperty(value = "联行号")
    @TableField("CODE")
private String code;

@ApiModelProperty(value = "银行")
    @TableField("NAME")
private String name;

}
