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
* @date 2020-08-14
*/
@Data
public class YxImageInfoDto implements Serializable {

    /** id */
    private Integer id;

    /** 类型id（关联id） */
    private Integer typeId;

    /** 图片类型（1：卡券，2：店铺，3：商品，4：商户相关） */
    private Integer imgType;

    /** 图片类别 1：缩略图/图片，2：轮播图，3:视频，4:评论图片。以下选项针对商户 1：个人手持身份证,2：个人证件照人像面，3：个人证件照国徽面，4：营业执照，5：银行开户证明，6：法人身份证头像面，7：法人身份证头像面国徽面，8：门店照及经营场所，9： 医疗机构许可证 */
    private Integer imgCategory;

    /** 图片地址 */
    private String imgUrl;

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
