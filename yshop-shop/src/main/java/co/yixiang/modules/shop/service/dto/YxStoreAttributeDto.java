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
* @author huiy
* @date 2020-08-14
*/
@Data
public class YxStoreAttributeDto implements Serializable {

    private Integer id;

    /** 店铺id */
    private Integer storeId;

    /** 属性值1 */
    private String attributeValue1;

    /** 属性值2 */
    private String attributeValue2;

    /** 属性类型：0：营业时间，1：店铺服务 */
    private Integer attributeType;

    /** 是否删除（0：未删除，1：已删除） */

    /** 创建人 */
    private Integer createUserId;

    /** 修改人 */
    private Integer updateUserId;

    /** 创建时间 */
    private Timestamp createTime;

    /** 更新时间 */
    private Timestamp updateTime;
    private Boolean delFlag;

}
