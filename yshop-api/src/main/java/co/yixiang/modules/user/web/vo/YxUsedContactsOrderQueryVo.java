package co.yixiang.modules.user.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 常用联系人表 查询结果对象
 * </p>
 *
 * @author lsy
 * @date 2020-11-04
 */
@Data
@ApiModel(value="YxUsedContactsOrderQueryVo对象", description="添加联系人")
public class YxUsedContactsOrderQueryVo implements Serializable {
    @ApiModelProperty(value = "联系人列表")
    List<YxUsedContactsQueryVo> contactsQueryVoList;
    @ApiModelProperty(value = "乘坐人数量")
    int maxPassengersNum;
}