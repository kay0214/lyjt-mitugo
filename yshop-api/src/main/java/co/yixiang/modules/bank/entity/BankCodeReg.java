package co.yixiang.modules.bank.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import co.yixiang.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * <p>
 * 联行号地区代码
 * </p>
 *
 * @author sss
 * @since 2020-09-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="BankCodeReg对象", description="联行号地区代码")
public class BankCodeReg extends BaseEntity {

    private static final long serialVersionUID = 1L;

@TableId(value = "id", type = IdType.AUTO)
private Integer id;

@ApiModelProperty(value = "省份")
private String pname;

@ApiModelProperty(value = "名称")
private String name;

@ApiModelProperty(value = "地区代码")
private String bankCode;

}
