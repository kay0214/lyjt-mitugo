package co.yixiang.modules.contract.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;

import java.util.Date;

/**
 * <p>
 * 合同签署表 查询结果对象
 * </p>
 *
 * @author lsy
 * @date 2020-11-04
 */
@Data
@ApiModel(value="YxContractSignQueryVo对象", description="合同签署表查询参数")
public class YxContractSignQueryVo implements Serializable{
    private static final long serialVersionUID = 1L;

@ApiModelProperty(value = "id")
private Integer id;

@ApiModelProperty(value = "关联订单号")
private String orderId;

@ApiModelProperty(value = "模板id")
private Integer tempId;

@ApiModelProperty(value = "模板名称")
private String tempName;

@ApiModelProperty(value = "签署文件地址")
private String filePath;

@ApiModelProperty(value = "签署状态 0:签署中 1：签署完成")
private Integer status;

@ApiModelProperty(value = "是否删除（0：未删除，1：已删除）")
private Integer delFlag;

@ApiModelProperty(value = "创建人")
private Integer createUserId;

@ApiModelProperty(value = "修改人")
private Integer updateUserId;

@ApiModelProperty(value = "创建时间")
private Date createTime;

@ApiModelProperty(value = "更新时间")
private Date updateTime;

}