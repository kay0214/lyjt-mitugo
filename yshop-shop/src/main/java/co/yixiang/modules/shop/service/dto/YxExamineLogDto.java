/**
* Copyright (C) 2018-2020
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.shop.service.dto;

import lombok.Data;
import java.sql.Timestamp;
import java.io.Serializable;

/**
* @author liusy
* @date 2020-08-17
*/
@Data
public class YxExamineLogDto implements Serializable {

    /** 主键 */

    private Integer id;


    /** 审批类型 1:提现 2:商户信息 */

    private Integer type;


    /** 审核数据关联id */

    private Integer typeId;


    /** 审批状态：0->待审核,1->通过,2->驳回 */

    private Integer status;


    /** 审核说明 */

    private String remark;


    /** 是否删除（0：未删除，1：已删除） */

    private Integer delFlag;


    /** 创建人(审核人) */

    private Integer createUserId;


    /** 修改人 */

    private Integer updateUserId;


    /** 创建时间 */

    private Timestamp createTime;


    /** 更新时间 */

    private Timestamp updateTime;

}
