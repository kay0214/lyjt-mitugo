/**
* Copyright (C) 2018-2020
* All rights reserved, Designed By www.yixiang.co
* 注意：
* 本软件为www.yixiang.co开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package co.yixiang.modules.shopConfig.service.dto;

import lombok.Data;
import java.sql.Timestamp;
import java.io.Serializable;

/**
* @author nxl
* @date 2020-11-04
*/
@Data
public class YxHotConfigDto implements Serializable {

    /** id */

    private Integer id;


    /** 公司名 */

    private String title;


    /** 封面图地址 */

    private String coverImg;


    /** 链接 */

    private String linkUrl;


    /** 排序 */

    private Integer sort;


    /** 状态：0：启用，1：禁用 */

    private Integer status;


    /** 是否删除（0：未删除，1：已删除） */

    private Integer delFlag;


    /** 创建人 */

    private Integer createUserId;


    /** 修改人 */

    private Integer updateUserId;

    /** 修改人名称 */

    private String updateUsername;


    /** 创建时间 */

    private Timestamp createTime;


    /** 更新时间 */

    private Timestamp updateTime;


    /** 内容 */

    private String content;

}
