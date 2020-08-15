/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package co.yixiang.common.util;

import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GeodeticCurve;
import org.gavaghan.geodesy.GlobalCoordinates;

/**
 * @author zhangqingqing
 * @version DistanceMeterUtil, v0.1 2020/8/15 14:19
 */
public class DistanceMeterUtil {

    public static double getDistanceMeter(GlobalCoordinates gpsFrom, GlobalCoordinates gpsTo)
    {
        //创建GeodeticCalculator，调用计算方法，传入坐标系、经纬度用于计算距离
        GeodeticCurve geoCurve = new GeodeticCalculator().calculateGeodeticCurve(Ellipsoid.Sphere, gpsFrom, gpsTo);

        return geoCurve.getEllipsoidalDistance();
    }

    public static void main(String[] args) {

        GlobalCoordinates source = new GlobalCoordinates(36.064248,120.380606);
        GlobalCoordinates target = new GlobalCoordinates(36.168228,120.426103);

        double meter1 = getDistanceMeter(source, target);

        System.out.println("Sphere坐标系计算结果："+meter1 + "米");

    }
}
