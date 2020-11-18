package co.yixiang.modules.manage.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;

import java.util.Date;

/**
 * <p>
 * 系统用户头像 查询结果对象
 * </p>
 *
 * @author lsy
 * @date 2020-11-18
 */
@Data
@ApiModel(value="UserAvatarQueryVo对象", description="系统用户头像查询参数")
public class UserAvatarQueryVo implements Serializable{
    private static final long serialVersionUID = 1L;

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