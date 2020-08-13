package co.yixiang.modules.user.web.dto;

import lombok.Data;

/**
 * @ClassName BillDTO
 * @Author hupeng <610796224@qq.com>
 * @Date 2019/11/12
 **/
@Data
public class BillOrderRecordDTO {
    private String orderId;
    private String time;
    private Double number;
    private String avatar;
    private String nickname;
}
