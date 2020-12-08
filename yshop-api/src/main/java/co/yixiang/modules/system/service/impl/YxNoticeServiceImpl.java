package co.yixiang.modules.system.service.impl;

import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.modules.coupons.web.vo.NoticeVO;
import co.yixiang.modules.system.entity.YxNotice;
import co.yixiang.modules.system.mapper.YxNoticeMapper;
import co.yixiang.modules.system.service.YxNoticeService;
import co.yixiang.modules.system.web.param.YxNoticeQueryParam;
import co.yixiang.modules.system.web.vo.YxNoticeQueryVo;
import co.yixiang.utils.CommonsUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;


/**
 * <p>
 * 公告表 服务实现类
 * </p>
 *
 * @author lsy
 * @since 2020-11-04
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class YxNoticeServiceImpl extends BaseServiceImpl<YxNoticeMapper, YxNotice> implements YxNoticeService
{

    @Autowired
    private YxNoticeMapper yxNoticeMapper;

    @Override
    public YxNoticeQueryVo getYxNoticeById(Serializable id) throws Exception{
        return yxNoticeMapper.getYxNoticeById(id);
    }

    @Override
    public Paging<YxNoticeQueryVo> getYxNoticePageList(YxNoticeQueryParam yxNoticeQueryParam) throws Exception{
        Page page = setPageParam(yxNoticeQueryParam,OrderItem.desc("create_time"));
        IPage<YxNoticeQueryVo> iPage = yxNoticeMapper.getYxNoticePageList(page,yxNoticeQueryParam);
        return new Paging(iPage);
    }

    /**
     * 获得首页的通知公告
     * @return
     */
    @Override
    public NoticeVO getIndexNotice() {
        try{
            Paging<YxNoticeQueryVo> list = getYxNoticePageList(new YxNoticeQueryParam() );
            if(list==null ||list.getTotal()<=0){
                return new NoticeVO();
            }
            YxNoticeQueryVo item = list.getRecords().get(0);
            if(item.getStatus().intValue()==1){
                // 如果是禁用的话
                return new NoticeVO();
            }
            return CommonsUtils.convertBean(item,NoticeVO.class);
        }catch (Exception e){
            return new NoticeVO();
        }
    }

}
