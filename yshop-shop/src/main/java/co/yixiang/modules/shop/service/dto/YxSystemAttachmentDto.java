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
import java.io.Serializable;

/**
* @author nxl
* @date 2020-09-01
*/
@Data
public class YxSystemAttachmentDto implements Serializable {


    private Integer attId;


    /** 附件名称 */

    private String name;


    /** 附件路径 */

    private String attDir;


    /** 压缩图片路径 */

    private String sattDir;


    /** 附件大小 */

    private String attSize;


    /** 附件类型 */

    private String attType;


    /** 分类ID0编辑器,1产品图片,2拼团图片,3砍价图片,4秒杀图片,5文章图片,6组合数据图 */

    private Integer pid;


    /** 上传时间 */

    private Integer time;


    /** 图片上传类型 1本地 2七牛云 3OSS 4COS  */

    private Integer imageType;


    /** 图片上传模块类型 1 后台上传 2 用户生成 */

    private Integer moduleType;


    /** 用户id */

    private Integer uid;


    /** 邀请码 */

    private String inviteCode;

}
