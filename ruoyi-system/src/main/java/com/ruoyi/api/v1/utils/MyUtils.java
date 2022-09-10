package com.ruoyi.api.v1.utils;

import com.ruoyi.common.core.domain.entity.SysApp;
import com.ruoyi.common.core.domain.entity.SysAppUser;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyUtils {

    public static Date getNewExpiredTimeAdd(Date oldExpiredTime, Long quota) {
        Date now = DateUtils.getNowDate();
        if (oldExpiredTime == null || oldExpiredTime.before(now)) {
            oldExpiredTime = now;
        }
        if (quota == null) {
            return oldExpiredTime;
        } else {
//            return DateUtils.addSeconds(oldExpiredTime, quota.intValue());
            LocalDateTime ldt = DateUtils.toLocalDateTime(oldExpiredTime);
            ldt = ldt.plus(quota, ChronoUnit.SECONDS);
            return DateUtils.toDate(ldt);
        }
    }

    public static Date getNewExpiredTimeSub(Date oldExpiredTime, Long quota) {
        Date now = DateUtils.getNowDate();
        if (oldExpiredTime == null || oldExpiredTime.before(now)) {
            oldExpiredTime = now;
        }
        if (quota == null) {
            return oldExpiredTime;
        } else {
//            return DateUtils.addSeconds(oldExpiredTime, -quota.intValue());
            LocalDateTime ldt = DateUtils.toLocalDateTime(oldExpiredTime);
            ldt = ldt.minus(quota, ChronoUnit.SECONDS);
            return DateUtils.toDate(ldt);
        }
    }

    public static BigDecimal getNewPointAdd(BigDecimal oldPoint, Long quota) {
        if (oldPoint == null) {
            oldPoint = BigDecimal.ZERO;
        }
        if (quota == null) {
            return oldPoint;
        } else {
            return oldPoint.add(BigDecimal.valueOf(quota));
        }
    }

    public static BigDecimal getNewPointAdd(BigDecimal oldPoint, Double quota) {
        if (oldPoint == null) {
            oldPoint = BigDecimal.ZERO;
        }
        if (quota == null) {
            return oldPoint;
        } else {
            return oldPoint.add(BigDecimal.valueOf(quota));
        }
    }

    public static BigDecimal getNewPointSub(BigDecimal oldPoint, Long quota) {
        if (oldPoint == null) {
            oldPoint = BigDecimal.ZERO;
        }
        if (quota == null) {
            return oldPoint;
        } else {
            return oldPoint.subtract(BigDecimal.valueOf(quota));
        }
    }

    public static BigDecimal getNewPointSub(BigDecimal oldPoint, Double quota) {
        if (oldPoint == null) {
            oldPoint = BigDecimal.ZERO;
        }
        if (quota == null) {
            return oldPoint;
        } else {
            return oldPoint.subtract(BigDecimal.valueOf(quota));
        }
    }

    public static List<Class<?>> getClassesFromPackage(String packagePath) {
        String RESOURCE_PATTERN = "/**/*.class";
        List<Class<?>> classList = new ArrayList<>();
        //spring工具类，可以获取指定路径下的全部类
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        try {
            String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + ClassUtils.convertClassNameToResourcePath(packagePath) + RESOURCE_PATTERN;
            Resource[] resources = resourcePatternResolver.getResources(pattern);
            //MetadataReader 的工厂类
            MetadataReaderFactory readerfactory = new CachingMetadataReaderFactory(resourcePatternResolver);
            for (Resource resource : resources) {
                //用于读取类信息
                MetadataReader reader = readerfactory.getMetadataReader(resource);
                //扫描到的class
                String classname = reader.getClassMetadata().getClassName();
                Class<?> clazz = Class.forName(classname);
                classList.add(clazz);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return classList;
    }

    public static Integer getEffectiveLoginLimitU(SysApp app, SysAppUser appUser) {
        Integer cardLoginLimit = appUser.getCardLoginLimitU();
        Integer appUserLoginLimit = appUser.getLoginLimitU();
        Integer appLoginLimit = app.getLoginLimitU();
        return getEffectiveLoginLimit(cardLoginLimit, appUserLoginLimit, appLoginLimit);
    }

    public static Integer getEffectiveLoginLimitM(SysApp app, SysAppUser appUser) {
        Integer cardLoginLimit = appUser.getCardLoginLimitM();
        Integer appUserLoginLimit = appUser.getLoginLimitM();
        Integer appLoginLimit = app.getLoginLimitM();
        return getEffectiveLoginLimit(cardLoginLimit, appUserLoginLimit, appLoginLimit);
    }

    public static Integer getEffectiveLoginLimit(Integer cardLoginLimit, Integer appUserLoginLimit, Integer appLoginLimit) {
        if (cardLoginLimit != -2) {
            return cardLoginLimit;
        }
        if (appUserLoginLimit != -2) {
            return appUserLoginLimit;
        }
        if (appLoginLimit >= -1) {
            return appLoginLimit;
        } else {
            return 1;
        }
    }
}
