package co.yixiang.modules.bank.service.impl;

import co.yixiang.modules.bank.entity.BankCnaps;
import co.yixiang.modules.bank.mapper.BankCnapsMapper;
import co.yixiang.modules.bank.service.BankCnapsService;
import co.yixiang.modules.bank.web.param.BankCnapsQueryParam;
import co.yixiang.modules.bank.web.vo.BankCnapsQueryVo;
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
 * 银行机构编码 服务实现类
 * </p>
 *
 * @author sss
 * @since 2020-09-17
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class BankCnapsServiceImpl extends BaseServiceImpl<BankCnapsMapper, BankCnaps> implements BankCnapsService
{

    @Autowired
    private BankCnapsMapper bankCnapsMapper;

    @Override
    public BankCnapsQueryVo getBankCnapsById(Serializable id) throws Exception{
        return bankCnapsMapper.getBankCnapsById(id);
    }

    @Override
    public Paging<BankCnapsQueryVo> getBankCnapsPageList(BankCnapsQueryParam bankCnapsQueryParam) throws Exception{
        Page page = setPageParam(bankCnapsQueryParam,OrderItem.desc("create_time"));
        IPage<BankCnapsQueryVo> iPage = bankCnapsMapper.getBankCnapsPageList(page,bankCnapsQueryParam);
        return new Paging(iPage);
    }

    @Override
    public List<BankCnaps> getAllCnaps() {
        return bankCnapsMapper.getAllCnaps();
    }

    @Override
    public List<String> getAllCnapsByName(String bankName) {
        return bankCnapsMapper.getAllCnapsByName(bankName);
    }

}
