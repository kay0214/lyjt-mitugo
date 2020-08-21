package co.yixiang.modules.monitor.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * pv 与 ip 统计
 *
 * @author Zheng Jie
 * @date 2018-12-13
 */
@Data
@TableName( "visits")
public class Visits implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String date;

    private Long pvCounts;

    private Long ipCounts;

    @TableField(fill = FieldFill.INSERT)
    private Timestamp createTime;

    private String weekDay;
}
