package co.yixiang.modules.ship.service.impl;

import co.yixiang.modules.ship.entity.YxCrewSign;
import co.yixiang.modules.ship.mapper.YxCrewSignMapper;
import co.yixiang.modules.ship.service.YxCrewSignService;
import co.yixiang.modules.ship.web.param.YxCrewSignQueryParam;
import co.yixiang.modules.ship.web.vo.YxCrewSignQueryVo;
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
 * 船员签到表 服务实现类
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class YxCrewSignServiceImpl extends BaseServiceImpl<YxCrewSignMapper, YxCrewSign> implements YxCrewSignService {

    @Autowired
    private YxCrewSignMapper yxCrewSignMapper;

    @Override
    public YxCrewSignQueryVo getYxCrewSignById(Serializable id) throws Exception{
        return yxCrewSignMapper.getYxCrewSignById(id);
    }

    @Override
    public Paging<YxCrewSignQueryVo> getYxCrewSignPageList(YxCrewSignQueryParam yxCrewSignQueryParam) throws Exception{
        Page page = setPageParam(yxCrewSignQueryParam,OrderItem.desc("create_time"));
        IPage<YxCrewSignQueryVo> iPage = yxCrewSignMapper.getYxCrewSignPageList(page,yxCrewSignQueryParam);
        return new Paging(iPage);
    }

}
