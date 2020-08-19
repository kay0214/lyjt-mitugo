package co.yixiang.common.mybatis;

import java.math.BigDecimal;

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