package co.yixiang.modules.bank.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import co.yixiang.modules.bank.entity.BankCodeReg;
import co.yixiang.modules.bank.web.param.BankCodeRegQueryParam;
import co.yixiang.modules.bank.web.vo.BankCodeRegQueryVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * <p>
 * 联行号地区代码 Mapper 接口
 * </p>
 *
 * @author sss
 * @since 2020-09-17
 */
@Repository
public interface BankCodeRegMapper extends BaseMapper<BankCodeReg> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    BankCodeRegQueryVo getBankCodeRegById(Serializable id);

    /**
     * 获取分页对象
     * @param page
     * @param bankCodeRegQueryParam
     * @return
     */
    IPage<BankCodeRegQueryVo> getBankCodeRegPageList(@Param("page") Page page, @Param("param") BankCodeRegQueryParam bankCodeRegQueryParam);

}
