package co.yixiang.modules.bank.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import co.yixiang.modules.bank.entity.BankCode;
import co.yixiang.modules.bank.web.param.BankCodeQueryParam;
import co.yixiang.modules.bank.web.vo.BankCodeQueryVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * <p>
 * 联行号表 Mapper 接口
 * </p>
 *
 * @author sss
 * @since 2020-09-17
 */
@Repository
public interface BankCodeMapper extends BaseMapper<BankCode> {

    /**
     * 根据ID获取查询对象
     * @param id
     * @return
     */
    BankCodeQueryVo getBankCodeById(Serializable id);

    /**
     * 获取分页对象
     * @param page
     * @param bankCodeQueryParam
     * @return
     */
    IPage<BankCodeQueryVo> getBankCodePageList(@Param("page") Page page, @Param("param") BankCodeQueryParam bankCodeQueryParam);

}
