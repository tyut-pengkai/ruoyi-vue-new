package com.ruoyi.common.utils.desensitization;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReflectUtil;
import com.ruoyi.common.utils.desensitization.handler.*;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 脱敏工具类
 *
 * @author liangyuqi
 * @date 2022/4/29 11:22
 */
@Slf4j
public class DesensitizationUtil {

    private static final Map<SensitiveTypeEnum, SensitiveHandler> SENSITIVE_HANDLERS = new ConcurrentHashMap<>();

    static {
        SENSITIVE_HANDLERS.put(SensitiveTypeEnum.DEFAULT, new DefaultSensitiveHandler());
        SENSITIVE_HANDLERS.put(SensitiveTypeEnum.CHINESE_NAME, new ChineseNameSensitiveHandler());
        SENSITIVE_HANDLERS.put(SensitiveTypeEnum.ID_CARD_NO, new IdCardNoSensitiveHandler());
        SENSITIVE_HANDLERS.put(SensitiveTypeEnum.PHONE_NO, new PhoneNoSensitiveHandler());
        SENSITIVE_HANDLERS.put(SensitiveTypeEnum.BANK_CARD_NO, new BankCardNoSensitiveHandler());
        SENSITIVE_HANDLERS.put(SensitiveTypeEnum.DETAILED_ADDRESS, new DetailedAddressSensitiveHandler());
        SENSITIVE_HANDLERS.put(SensitiveTypeEnum.OBJECT, new ObjectSensitiveHandler());
    }

    /**
     * 数据脱敏
     *
     * @param source
     * @param <T>
     * @return
     */
    public static <T> T desensitize(T source) {
        if (source != null) {
            Class clazz = source.getClass();
            try {
                if (clazz.isArray()) {
                    //array
                    Arrays.stream((Object[]) source).forEach(DesensitizationUtil::desensitize);
                } else if (Iterable.class.isAssignableFrom(clazz)) {
                    //list
                    ((Iterable) source).forEach(DesensitizationUtil::desensitize);
                } else if (Map.class.isAssignableFrom(clazz)) {
                    //map
                    ((Map) source).values().forEach(DesensitizationUtil::desensitize);
                } else {
                    Field[] fields = ReflectUtil.getFields(clazz);
                    for (Field field : fields) {
                        Desensitization annotation = field.getDeclaredAnnotation(Desensitization.class);
                        if (annotation != null) {
                            Object value = ReflectUtil.getFieldValue(source, field);
                            SensitiveHandler sensitiveHandler = SENSITIVE_HANDLERS.get(annotation.value());
                            field.set(source, sensitiveHandler.handle(value));
                        }
                    }
                }
            } catch (Exception e) {
                log.error("脱敏失败:{}", source, e);
            }
        }
        return source;
    }

    /**
     * 数据脱敏
     *
     * @param type
     * @param sources
     * @return
     */
    public static List<String> desensitize(SensitiveTypeEnum type, List<String> sources) {
        if (CollUtil.isEmpty(sources)) {
            return sources;
        }
        List<String> targets = new ArrayList<>(sources.size());
        for (String source : sources) {
            targets.add(desensitize(type, source));
        }
        return targets;
    }

    /**
     * 数据脱敏
     *
     * @param type
     * @param source
     * @return
     */
    public static String desensitize(SensitiveTypeEnum type, String source) {
        SensitiveHandler sensitiveHandler = SENSITIVE_HANDLERS.get(type);
        return sensitiveHandler.handle(source);
    }
}
