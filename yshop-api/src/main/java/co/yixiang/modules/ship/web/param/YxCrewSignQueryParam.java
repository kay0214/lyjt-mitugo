package co.yixiang.modules.ship.web.param;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import co.yixiang.common.web.param.QueryParam;

/**
 * <p>
 * 船员签到表 查询参数对象
 * </p>
 *
 * @author lsy
 * @date 2020-11-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="YxCrewSignQueryParam对象", description="船员签到表查询参数")
public class YxCrewSignQueryParam extends QueryParam {
    private static final long serialVersionUID = 1L;
}
