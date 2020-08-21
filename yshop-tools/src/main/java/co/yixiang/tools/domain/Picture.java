/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co

 */
package co.yixiang.tools.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
* @author hupeng
* @date 2020-05-13
*/

@Data
@TableName("picture")
public class Picture implements Serializable {

    /** ID */
    @TableId(value = "id", type = IdType.AUTO)
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
   // @Column(name = "id")
    private Long id;


    /** 上传日期 */
   // @Column(name = "create_time")
    @TableField(fill= FieldFill.INSERT)
    private Timestamp createTime;


    /** 删除的URL */
   // @Column(name = "delete_url")
    private String deleteUrl;


    /** 图片名称 */
   // @Column(name = "filename")
    private String filename;


    /** 图片高度 */
   // @Column(name = "height")
    private String height;


    /** 图片大小 */
   // @Column(name = "size")
    private String size;


    /** 图片地址 */
   // @Column(name = "url")
    private String url;


    /** 用户名称 */
   // @Column(name = "username")
    private String username;


    /** 图片宽度 */
   // @Column(name = "width")
    private String width;


    /** 文件的MD5值 */
   // @Column(name = "md5code")
    private String md5code;

    public void copy(Picture source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
