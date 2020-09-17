package co.yixiang.modules.bank.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;

import java.util.Date;

/**
 * <p>
 * 联行号表 查询结果对象
 * </p>
 *
 * @author sss
 * @date 2020-09-17
 */
@Data
@ApiModel(value="BankCodeQueryVo对象", description="联行号表查询参数")
public class BankCodeQueryVo implements Serializable{
    private static final long serialVersionUID = 1L;

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