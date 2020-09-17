package co.yixiang.modules.bank.mapper;

import co.yixiang.modules.bank.entity.BankCnaps;
import co.yixiang.modules.bank.web.param.BankCnapsQueryParam;
import co.yixiang.modules.bank.web.vo.BankCnapsQueryVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

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

    /**
     * 查询所有银行
     * @return
     */
    @Select(" select * from bank_cnaps ")
    List<BankCnaps> getAllCnaps();

    /**
     * 根据银行名称查询
     * @param bankName
     * @return
     */
    @Select(" select id from bank_cnaps where NAME = #{bankName} ")
    List<String> getAllCnapsByName(@Param("bankName") String bankName);
}
