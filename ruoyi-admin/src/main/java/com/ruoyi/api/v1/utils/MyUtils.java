package com.ruoyi.api.v1.utils;

import com.ruoyi.common.utils.DateUtils;

import java.math.BigDecimal;
import java.util.Date;

public class MyUtils {

    public static Date getNewExpiredTime(Date oldExpiredTime, Long quota) {
        Date now = DateUtils.getNowDate();
        if (oldExpiredTime == null || oldExpiredTime.before(now)) {
            oldExpiredTime = now;
        }
        if (quota == null) {
            return oldExpiredTime;
        } else {
            return DateUtils.addSeconds(oldExpiredTime, quota.intValue());
        }
    }

    public static BigDecimal getNewPoint(BigDecimal oldPoint, Long quota) {
        if (oldPoint == null) {
            oldPoint = BigDecimal.ZERO;
        }
        if (quota == null) {
            return oldPoint;
        } else {
            return oldPoint.add(BigDecimal.valueOf(quota));
        }
    }
}
