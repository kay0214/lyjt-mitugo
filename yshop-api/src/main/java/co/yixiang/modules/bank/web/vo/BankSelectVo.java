package co.yixiang.modules.bank.web.vo;

import co.yixiang.modules.bank.entity.BankCnaps;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
public class BankSelectVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<String> provinces;

    private List<BankCnaps> cnaps;


}