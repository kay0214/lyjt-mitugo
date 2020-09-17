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
 * 联行号表
 * </p>
 *
 * @author sss
 * @since 2020-09-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="BankCode对象", description="联行号表")
public class BankCode extends BaseEntity {

    private static final long serialVersionUID = 1L;

@TableId(value = "id", type = IdType.AUTO)
private Integer id;

@ApiModelProperty(value = "所属银行")
private Integer bankId;

@ApiModelProperty(value = "联行号")
private String bankCode;

@ApiModelProperty(value = "所属银行")
private String bankName;

@ApiModelProperty(value = "支行")
private String bankAdd;

@ApiModelProperty(value = "地区代码id")
private Integer regId;

}
