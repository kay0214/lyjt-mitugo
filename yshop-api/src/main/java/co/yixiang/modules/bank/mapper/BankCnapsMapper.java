package co.yixiang.modules.bank.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import co.yixiang.modules.bank.entity.BankCnaps;
import co.yixiang.modules.bank.web.param.BankCnapsQueryParam;
import co.yixiang.modules.bank.web.vo.BankCnapsQueryVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * <p>
 * 银行机构编码 Mapper 接口
 * </p>
 *
 * @author sss
 * @since 2020-09-17
 */
@Repository
public interface BankCnapsMapper extends BaseMapper<BankCnaps> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    BankCnapsQueryVo getBankCnapsById(Serializable id);

    /**
     * 获取分页对象
     * @param page
     * @param bankCnapsQueryParam
     * @return
     */
    IPage<BankCnapsQueryVo> getBankCnapsPageList(@Param("page") Page page, @Param("param") BankCnapsQueryParam bankCnapsQueryParam);

}
