package co.yixiang.modules.bank.service.impl;

import co.yixiang.common.service.impl.BaseServiceImpl;
import co.yixiang.common.web.vo.Paging;
import co.yixiang.modules.bank.entity.BankCode;
import co.yixiang.modules.bank.mapper.BankCodeMapper;
import co.yixiang.modules.bank.mapper.BankCodeRegMapper;
import co.yixiang.modules.bank.service.BankCnapsService;
import co.yixiang.modules.bank.service.BankCodeService;
import co.yixiang.modules.bank.web.param.BankCodeQueryParam;
import co.yixiang.modules.bank.web.vo.BankCodeQueryVo;
import co.yixiang.utils.StringUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;


/**
 * <p>
 * 联行号表 服务实现类
 * </p>
 *
 * @author sss
 * @since 2020-09-17
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class BankCodeServiceImpl extends BaseServiceImpl<BankCodeMapper, BankCode> implements BankCodeService

{

    @Autowired
    private BankCodeMapper bankCodeMapper;

    @Autowired
    private BankCnapsService cnapsService;

    @Autowired
    private BankCodeRegMapper codeRegMapper;

    @Override
    public BankCodeQueryVo getBankCodeById(Serializable id) throws Exception{
        return bankCodeMapper.getBankCodeById(id);
    }

    @Override
    public Paging<BankCodeQueryVo> getBankCodePageList(BankCodeQueryParam bankCodeQueryParam) throws Exception{
        Page page = setPageParam(bankCodeQueryParam,OrderItem.desc("create_time"));
        IPage<BankCodeQueryVo> iPage = bankCodeMapper.getBankCodePageList(page,bankCodeQueryParam);
        return new Paging(iPage);
    }

    @Override
    public Paging<BankCodeQueryVo> getBanks(BankCodeQueryParam queryParam) {
        Page page = setPageParam(queryParam,OrderItem.desc("bank_code"));

        if(!StringUtils.isBlank(queryParam.getCitys())){
            List<String> bankCodes = codeRegMapper.getBankCodes(queryParam.getProvince(),queryParam.getCitys());
            if(bankCodes!=null){
                queryParam.setRegIds(String.join(",",bankCodes));
            }
        }


        IPage<BankCodeQueryVo> iPage = bankCodeMapper.getBankCodePageList(page,queryParam);
        return new Paging(iPage);
    }

}
