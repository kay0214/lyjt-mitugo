package co.yixiang.modules.bank.mapper;

import co.yixiang.modules.bank.entity.BankCodeReg;
import co.yixiang.modules.bank.web.param.BankCodeRegQueryParam;
import co.yixiang.modules.bank.web.vo.BankCodeRegQueryVo;
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

    /**
     * 查询所有省份
     * @return
     */
    @Select(" select pname from bank_code_reg GROUP BY pname ORDER BY bank_code asc ")
    List<String> getAllProvinces();

    /**
     * 查询所有市
     * @param name
     * @return
     */
    @Select(" select name from bank_code_reg where pname=#{name} GROUP BY name ORDER BY bank_code asc ")
    List<String> getAllCitys(@Param("name") String name);

    /**
     * 查询编码
     * @param citys
     * @return
     */
    @Select(" select id from bank_code_reg where name=#{citys} and pname=#{province}")
    List<String> getBankCodes(@Param("province")String province ,@Param("citys") String citys);
}
