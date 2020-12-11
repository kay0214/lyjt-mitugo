package co.yixiang.modules.coupons.service.impl;

import co.yixiang.common.api.ApiResult;
import co.yixiang.common.exception.BusinessException;
import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.modules.coupons.entity.YxCoupons;
import co.yixiang.modules.coupons.mapper.YxCouponsMapper;
import co.yixiang.modules.coupons.service.YxCouponsService;
import co.yixiang.modules.coupons.web.param.YxCouponsQueryParam;
import co.yixiang.modules.coupons.web.vo.YxCouponsQueryVo;
import co.yixiang.util.SignatureUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Map;


/**
 * <p>
 * 本地生活, 卡券表 服务实现类
 * </p>
 *
 * @author zqq
 * @since 2020-12-11
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class YxCouponsServiceImpl extends BaseServiceImpl<YxCouponsMapper, YxCoupons> implements YxCouponsService {

    @Autowired
    private YxCouponsMapper yxCouponsMapper;


    @Value("${online.appKey}")
    private String appKey;

    @Value("${online.version}")
    private String version;


    @Override
    public YxCouponsQueryVo getYxCouponsById(Serializable id) throws Exception{
        return yxCouponsMapper.getYxCouponsById(id);
    }

    @Override
    public Paging<YxCouponsQueryVo> getYxCouponsPageList(YxCouponsQueryParam yxCouponsQueryParam) throws Exception{
        Page page = setPageParam(yxCouponsQueryParam,OrderItem.desc("create_time"));
        IPage<YxCouponsQueryVo> iPage = yxCouponsMapper.getYxCouponsPageList(page,yxCouponsQueryParam);
        return new Paging(iPage);
    }

    @Override
    public ApiResult selectYxCouponsPageList(YxCouponsQueryParam yxCouponsQueryParam) throws Exception {
        Map<String, String> map= BeanUtils.describe(yxCouponsQueryParam);
        if (!yxCouponsQueryParam.getSignature().equals(SignatureUtil.getSignature(map,appKey))){
            throw new BusinessException("验签失败");
        }
        Paging<YxCouponsQueryVo> paging = getYxCouponsPageList(yxCouponsQueryParam);
        return ApiResult.ok(paging);
    }

}
