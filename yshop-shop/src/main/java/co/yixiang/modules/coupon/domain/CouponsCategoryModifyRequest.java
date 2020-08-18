package co.yixiang.modules.coupon.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Author : huanghui
 */
@Data
public class CouponsCategoryModifyRequest implements Serializable {

    /** 券分类主键 */
    @NotNull(message = "分类主键不可为空")
    private Integer id;

    /** 分类名称 */
    @NotBlank(message = "分类名称不可为空")
    private String cateName;

    @NotBlank(message = "分类图片不可为空")
    private String path;

    /** 排序 */
    @NotNull(message = "分类排序不可为空")
    private Integer sort;

    /** 是否推荐. 0:不推荐, 1:推荐 */
    @NotNull(message = "是否推荐不可为空")
    private Integer isShow;

}
