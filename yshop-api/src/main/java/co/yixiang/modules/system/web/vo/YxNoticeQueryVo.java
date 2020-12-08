package co.yixiang.modules.system.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;

import java.util.Date;

/**
 * <p>
 * 公告表 查询结果对象
 * </p>
 *
 * @author lsy
 * @date 2020-11-04
 */
@Data
@ApiModel(value="YxNoticeQueryVo对象", description="公告表查询参数")
public class YxNoticeQueryVo implements Serializable{
    private static final long serialVersionUID = 1L;

@ApiModelProperty(value = "id")
private Integer id;

@ApiModelProperty(value = "图片")
private String noticeImage;

@ApiModelProperty(value = "内容")
private String noticeContent;

@ApiModelProperty(value = "跳转链接")
private String linkUrl;

@ApiModelProperty(value = "状态：0：启用，1：禁用")
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