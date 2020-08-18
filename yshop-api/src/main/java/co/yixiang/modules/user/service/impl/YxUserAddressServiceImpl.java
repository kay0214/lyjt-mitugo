/**
 * Copyright (C) 2018-2020
 */
package co.yixiang.modules.user.service.impl;

import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.modules.user.entity.YxUserAddress;
import co.yixiang.modules.user.mapper.YxUserAddressMapper;
import co.yixiang.modules.user.service.YxUserAddressService;
import co.yixiang.modules.user.web.param.YxUserAddressQueryParam;
import co.yixiang.modules.user.web.vo.YxUserAddressQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;


/**
 * <p>
 * 用户地址表 服务实现类
 * </p>
 *
 * @author hupeng
 * @since 2019-10-28
 */
@Slf4j
@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class YxUserAddressServiceImpl extends BaseServiceImpl<YxUserAddressMapper, YxUserAddress> implements YxUserAddressService {

    private final YxUserAddressMapper yxUserAddressMapper;

    @Override
    public YxUserAddressQueryVo getYxUserAddressById(Serializable id){
        return yxUserAddressMapper.getYxUserAddressById(id);
    }

    @Override
    public YxUserAddress getUserDefaultAddress(int uid) {
        QueryWrapper<YxUserAddress> wrapper = new QueryWrapper<>();
        wrapper.eq("is_default",1).eq("uid",uid).eq("is_del",0).last("limit 1");
        return getOne(wrapper);
    }

    @Override
    public Paging<YxUserAddressQueryVo> getYxUserAddressPageList(YxUserAddressQueryParam yxUserAddressQueryParam){
        Page page = setPageParam(yxUserAddressQueryParam,OrderItem.desc("add_time"));
        IPage<YxUserAddressQueryVo> iPage = yxUserAddressMapper.getYxUserAddressPageList(page,yxUserAddressQueryParam);
        return new Paging(iPage);
    }

}
