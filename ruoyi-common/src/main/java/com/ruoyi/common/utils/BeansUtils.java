package com.ruoyi.common.utils;

import org.dozer.DozerBeanMapperBuilder;
import org.dozer.Mapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.FeatureDescriptor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author Wenbo Huang
 * @version 1.0
 * @date 2021/3/31 10:35 AM
 */
public class BeansUtils {
    /**
     * default MAPPER without config
     */
    private static final Mapper MAPPER = DozerBeanMapperBuilder.buildDefault();

    /***
     * @param listSrc Source object collection
     * @param clazz   Target object class type
     */
    @SuppressWarnings("unchecked")
    public static <TContent> List<TContent> convertList(List listSrc,
                                                        Class<TContent> clazz) {
        List<TContent> listDes = new ArrayList<>();
        if (listSrc.isEmpty()) {
            return listDes;
        }
        listSrc.forEach(objSrc -> {
            listDes.add(convertObject(objSrc, clazz));
        });
        return listDes;
    }

    /**
     * Single object conversion
     *
     * @param content Source object
     * @param clazz   Target object class type
     */
    public static <TContent> TContent convertObject(Object content,
                                                    Class<TContent> clazz) {
        if (content == null) {
            return null;
        }
        return MAPPER.map(content, clazz);
    }

    /**
     * Exclude NUll value of source target
     *
     * @param source
     * @param target
     */
    public static void copyProperties(Object source, Object target) {
        BeanWrapper wrappedSource = new BeanWrapperImpl(source);
        String[] nullProperties = Stream.of(wrappedSource.getPropertyDescriptors())
                .map(FeatureDescriptor::getName)
                .filter(propertyName -> wrappedSource.getPropertyValue(propertyName) == null).toArray(String[]::new);
        List<String> properties = new ArrayList<>();
        properties.addAll(Arrays.asList(nullProperties));
        BeanUtils.copyProperties(source, target, properties.toArray(new String[0]));
    }
}
