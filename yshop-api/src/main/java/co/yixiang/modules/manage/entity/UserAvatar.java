package co.yixiang.modules.manage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import co.yixiang.common.entity.BaseEntity;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * <p>
 * 系统用户头像
 * </p>
 *
 * @author lsy
 * @since 2020-11-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="UserAvatar对象", description="系统用户头像")
public class UserAvatar extends BaseEntity {

    private static final long serialVersionUID = 1L;

@TableId(value = "id", type = IdType.AUTO)
private Long id;

@ApiModelProperty(value = "真实文件名")
private String realName;

@ApiModelProperty(value = "路径")
private String path;

@ApiModelProperty(value = "大小")
private String size;

@ApiModelProperty(value = "创建时间")
private Date createTime;

}
