package co.yixiang.modules.user.web.dto;

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
 * 常用联系人表
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "YxUsedContacts对象", description = "常用联系人表")
public class YxUsedContactsSaveDto extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "所属用户id")
    private Integer userId;

    @ApiModelProperty(value = "姓名")
    private String userName;

    @ApiModelProperty(value = "用户电话")
    private String userPhone;

    @ApiModelProperty(value = "身份证号码")
    private String cardId;

    @ApiModelProperty(value = "用户类别：0 -> 12岁以下,1 -> 12及岁以上")
    private Integer userType;
}
