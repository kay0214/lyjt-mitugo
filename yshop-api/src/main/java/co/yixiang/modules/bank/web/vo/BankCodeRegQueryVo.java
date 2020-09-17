package co.yixiang.modules.bank.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;

import java.util.Date;

/**
 * <p>
 * 联行号地区代码 查询结果对象
 * </p>
 *
 * @author sss
 * @date 2020-09-17
 */
@Data
@ApiModel(value="BankCodeRegQueryVo对象", description="联行号地区代码查询参数")
public class BankCodeRegQueryVo implements Serializable{
    private static final long serialVersionUID = 1L;

private Integer id;

@ApiModelProperty(value = "省份")
private String pname;

@ApiModelProperty(value = "名称")
private String name;

@ApiModelProperty(value = "地区代码")
private String bankCode;

}