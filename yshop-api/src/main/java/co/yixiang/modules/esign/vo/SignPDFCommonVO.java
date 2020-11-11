/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package co.yixiang.modules.esign.vo;

import lombok.Data;

/**
 * @author PC-LIUSHOUYI
 * @version SignPDFCommonVO, v0.1 2019/12/25 17:38
 */
@Data
public class SignPDFCommonVO {

    /**
     * 签署者账号标识
     */
    private String accountId;

    /**
     * 印章图片Base64
     */
    private String sealData;

    /**
     * 待签署PDF文档本地路径，含文档名 必填
     */
    private String srcPdfFile;

    /**
     * 签署后PDF文档本地路径
     */
    private String dstPdfFile;

    /**
     * 关键字定位，相对于关键字的X坐标偏移量，默认0
     */
    private Integer posX;

    /**
     * 关键字定位，相对于关键字的Y坐标偏移量，默认0
     */
    private Integer posY;

    /**
     * 关键字，仅限关键字签章时有效
     */
    private String key;

    /**
     * 是否个人签章 0:否 1：是
     */
    private int isPerson;

    /**
     * 印章展现宽度，将以此宽度对印章图片做同比缩放
     */
    private String width;
}
