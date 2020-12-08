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
* @author nxl
* @date 2020-11-04
*/
@Data
public class YxCustomerServiceDto implements Serializable {

    /** id */

    private Integer id;


    /** 问题 */

    private String question;


    /** 排序 */

    private Integer sort;


    /** 状态：0：启用，1：禁用 */

    private Integer status;


    /** 用户角色：0->平台运营,1->合伙人,2->商户 */

    private Integer userRole;

    /** 所属商户id */
    private Integer merId;

    /** 是否删除（0：未删除，1：已删除） */

    private Integer delFlag;


    /** 创建人 */

    private Integer createUserId;


    /** 修改人 */

    private Integer updateUserId;


    /** 创建时间 */

    private Timestamp createTime;


    /** 更新时间 */

    private Timestamp updateTime;


    /** 回答 */

    private String answer;

}
