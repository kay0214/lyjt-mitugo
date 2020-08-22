package co.yixiang.modules.shop.entity;

import lombok.Data;

import java.util.List;

/**
 * system_group_data 表 value 字段实体
 * @Author : huanghui
 */
@Data
public class SystemGroupDataValue {

    private List imageArr;
    private String name;
    private String id;
    private String pic;
    private Integer sort;
    private String url;
    private String wxapp_url;
    private Integer status;
}
