package co.yixiang.modules.mybatis;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class GeoPoint {
    public GeoPoint(BigDecimal lng, BigDecimal lat) {
        this.lng = lng;
        this.lat = lat;
    }
    /* 经度 */
    private BigDecimal lng;
    /* 纬度 */
    private BigDecimal lat;
}