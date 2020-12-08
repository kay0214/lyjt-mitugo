package co.yixiang.modules.ship.entity;

import co.yixiang.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 船员签到表
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "YxCrewSign对象", description = "船员签到表")
public class YxCrewSign extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "用户ID")
    private Integer uid;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "联系电话")
    private String userPhone;

    @ApiModelProperty(value = "体温")
    private BigDecimal temperature;

    @ApiModelProperty(value = "是否删除（0：未删除，1：已删除）")
    private Integer delFlag;

    @ApiModelProperty(value = "创建人")
    private Integer createUserId;

    @ApiModelProperty(value = "修改人")
    private Integer updateUserId;

    @ApiModelProperty(value = "创建时间（签到时间）")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "用户昵称")
    private String nickName;


}
