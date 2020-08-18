/**
 * Copyright (C) 2018-2020
 */
package co.yixiang.modules.user.service;

import co.yixiang.common.service.BaseService;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.modules.user.entity.YxUserAddress;
import co.yixiang.modules.user.web.param.YxUserAddressQueryParam;
import co.yixiang.modules.user.web.vo.YxUserAddressQueryVo;

import java.io.Serializable;

/**
 * <p>
 * 用户地址表 服务类
 * </p>
 *
 * @author hupeng
 * @since 2019-10-28
 */
public interface YxUserAddressService extends BaseService<YxUserAddress> {

    YxUserAddress getUserDefaultAddress(int uid);

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    YxUserAddressQueryVo getYxUserAddressById(Serializable id);

    /**
     * 获取分页对象
     * @param yxUserAddressQueryParam
     * @return
     */
    Paging<YxUserAddressQueryVo> getYxUserAddressPageList(YxUserAddressQueryParam yxUserAddressQueryParam);

}
