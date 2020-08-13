package co.yixiang.modules.image.service.impl;

import co.yixiang.modules.image.entity.YxImageInfo;
import co.yixiang.modules.image.mapper.YxImageInfoMapper;
import co.yixiang.modules.image.service.YxImageInfoService;
import co.yixiang.modules.image.web.param.YxImageInfoQueryParam;
import co.yixiang.modules.image.web.vo.YxImageInfoQueryVo;
import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.web.vo.Paging;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.io.Serializable;


/**
 * <p>
 * 图片表 服务实现类
 * </p>
 *
 * @author hupeng
 * @since 2020-08-13
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class YxImageInfoServiceImpl extends BaseServiceImpl<YxImageInfoMapper, YxImageInfo> implements YxImageInfoService {

    @Autowired
    private YxImageInfoMapper yxImageInfoMapper;

    @Override
    public YxImageInfoQueryVo getYxImageInfoById(Serializable id) throws Exception{
        return yxImageInfoMapper.getYxImageInfoById(id);
    }

    @Override
    public Paging<YxImageInfoQueryVo> getYxImageInfoPageList(YxImageInfoQueryParam yxImageInfoQueryParam) throws Exception{
        Page page = setPageParam(yxImageInfoQueryParam,OrderItem.desc("create_time"));
        IPage<YxImageInfoQueryVo> iPage = yxImageInfoMapper.getYxImageInfoPageList(page,yxImageInfoQueryParam);
        return new Paging(iPage);
    }

}
