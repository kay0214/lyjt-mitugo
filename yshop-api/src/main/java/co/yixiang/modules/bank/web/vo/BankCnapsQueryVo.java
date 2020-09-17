package co.yixiang.modules.bank.web.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 银行机构编码 查询结果对象
 * </p>
 *
 * @author sss
 * @date 2020-09-17
 */
@Data
@ApiModel(value="BankCnapsQueryVo对象", description="银行机构编码查询参数")
public class BankCnapsQueryVo implements Serializable{
    private static final long serialVersionUID = 1L;

private Integer id;

@ApiModelProperty(value = "联行号")
    @TableField("CODE")
private String code;

@ApiModelProperty(value = "银行")
    @TableField("NAME")
private String name;

}