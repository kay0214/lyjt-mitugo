/**
* Copyright (C) 2018-2020
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.bank.domain;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.*;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.validation.constraints.*;
import java.io.Serializable;

/**
* @author lsy
* @date 2020-11-20
*/
@Data
@TableName("bank_code")
public class BankCode implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    /** 所属银行 */
    private Integer bankId;


    /** 联行号 */
    private String bankCode;


    /** 所属银行 */
    private String bankName;


    /** 支行 */
    private String bankAdd;


    /** 地区代码id */
    private Integer regId;


    public void copy(BankCode source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
