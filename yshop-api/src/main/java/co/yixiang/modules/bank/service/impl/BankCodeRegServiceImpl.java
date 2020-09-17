package co.yixiang.modules.bank.service.impl;

import co.yixiang.modules.bank.entity.BankCodeReg;
import co.yixiang.modules.bank.mapper.BankCodeRegMapper;
import co.yixiang.modules.bank.service.BankCodeRegService;
import co.yixiang.modules.bank.web.param.BankCodeRegQueryParam;
import co.yixiang.modules.bank.web.vo.BankCodeRegQueryVo;
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
import java.util.List;


/**
 * <p>
 * 联行号地区代码 服务实现类
 * </p>
 *
 * @author sss
 * @since 2020-09-17
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class BankCodeRegServiceImpl extends BaseServiceImpl<BankCodeRegMapper, BankCodeReg> implements BankCodeRegService
{

    @Autowired
    private BankCodeRegMapper bankCodeRegMapper;

    @Override
    public BankCodeRegQueryVo getBankCodeRegById(Serializable id) throws Exception{
        return bankCodeRegMapper.getBankCodeRegById(id);
    }

    @Override
    public Paging<BankCodeRegQueryVo> getBankCodeRegPageList(BankCodeRegQueryParam bankCodeRegQueryParam) throws Exception{
        Page page = setPageParam(bankCodeRegQueryParam,OrderItem.desc("create_time"));
        IPage<BankCodeRegQueryVo> iPage = bankCodeRegMapper.getBankCodeRegPageList(page,bankCodeRegQueryParam);
        return new Paging(iPage);
    }

    /**
     * 查询所有的省份
     * @return
     */
    @Override
    public List<String> getAllProvinces() {
        return bankCodeRegMapper.getAllProvinces();
    }

    /**
     * 查询所有市
     * @param name
     * @return
     */
    @Override
    public List<String> getAllCitys(String name) {
        return bankCodeRegMapper.getAllCitys(name);
    }

}
