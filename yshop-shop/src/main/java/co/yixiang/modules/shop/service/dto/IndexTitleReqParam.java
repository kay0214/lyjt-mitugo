package co.yixiang.modules.shop.service.dto;

import co.yixiang.common.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 首页文字
 * </p>
 *
 * @author
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="首页文字", description="首页文字")
public class IndexTitleReqParam extends BaseEntity {

    IndexTitleParam title_1;
    IndexTitleParam title_2;
    IndexTitleParam title_3_1;
    IndexTitleParam title_3_2;
    IndexTitleParam title_3_3;

}
