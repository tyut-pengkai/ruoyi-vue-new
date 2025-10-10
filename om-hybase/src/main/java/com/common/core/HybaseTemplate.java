package com.common.core;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.common.domain.dto.ServerDTO;
import com.common.domain.HybaseBean;
import com.common.domain.HybaseConnectParams;
import com.common.extractor.HybaseExtractor;
import com.common.mapping.HybaseClassMapping;
import com.common.mapping.HybaseMapping;
import com.common.mapping.HybasePropertyMapping;
import com.common.plugins.HybasePlugin;
import com.common.query.HybasePage;
import com.common.query.HybaseQueryWrapper;
import com.common.query.OffsetLimit;
import com.common.utils.HybaseOperationParams;
import com.common.utils.HybaseSearchParams;
import com.trs.hybase.client.TRSConstants;
import com.trs.hybase.client.*;
import com.trs.hybase.client.params.OperationParams;
import com.trs.hybase.client.params.SearchParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.*;

/**
 * hybase操作接口
 *
 * @param <T> 返回结果类型
 */
@RequiredArgsConstructor
@Component
@Slf4j
public class HybaseTemplate<T extends HybaseBean> implements HybaseOperations<T> {

    private final HybaseMapping hybaseMapping;

    /**
     * hybase基础查询方法
     *
     * @param connectParams hybase连接参数
     * @param classMapping  hybase实体类映射
     * @param searchParams  hybase查询参数
     * @param query         hybase查询语句
     * @param offsetLimit   分页参数
     * @param clazz         返回结果类型
     * @return 查询结果
     */
    private HybasePage<T> select(HybaseConnectParams connectParams, HybaseClassMapping classMapping, SearchParams searchParams, String query, OffsetLimit offsetLimit, Class<T> clazz) {
        List<T> result = new LinkedList<>();
        long totalRecords = 0;
        // 获取hybase连接
        try (TRSConnection connection = connectParams.createConnection()) {
            // 查询
            TRSResultSet trsResultSet = connection.executeSelect(StrUtil.emptyToDefault(connectParams.getTable(), classMapping.getTable()), query, ((long) (offsetLimit.getPageNum() - 1) * offsetLimit.getPageSize()), offsetLimit.getPageSize(), searchParams);
            totalRecords = trsResultSet.getNumFound();
            for (int i = 0; i < trsResultSet.size(); i++) {
                TRSRecord record = trsResultSet.getRecord(i);
                // 处理hybase字段提取器
                Map<String, HybasePropertyMapping> propertyMappings = classMapping.getPropertyMappings();
                // 使用反射创建泛型对象
                T t = ReflectUtil.newInstance(clazz);
                Collection<String> propertyNames = classMapping.getPropertyNames();
                if (StrUtil.isNotEmpty(classMapping.getUuidPropertyName())) {
                    ReflectUtil.setFieldValue(t, classMapping.getUuidPropertyName(), record.getUid());
                }
                for (String propertyName : propertyNames) {
                    // uuid 取值和其他字段根据属性类型取值
                    Class<?> type = ReflectUtil.getField(clazz, propertyName).getType();
                    String value = record.getString(classMapping.getColumnName(propertyName));
                    if (!StrUtil.equalsAny(value, "NULL", "null", null)) {
                        if (type == String.class || type == String[].class) {
                            if (propertyMappings.containsKey(propertyName)) {
                                HybasePropertyMapping hybasePropertyMapping = propertyMappings.get(propertyName);
                                for (HybaseExtractor hybaseExtractor : hybasePropertyMapping.getHybaseExtractorList()) {
                                    value = hybaseExtractor.extractValue(propertyName, value);
                                }
                            }
                            if (StrUtil.isNotEmpty(value)) {
                                if (type.isArray()) {
                                    ReflectUtil.setFieldValue(t, propertyName, StrUtil.split(value, ";"));
                                } else {
                                    ReflectUtil.setFieldValue(t, propertyName, value);
                                }
                            }
                        } else if (type == Integer.class) {
                            ReflectUtil.setFieldValue(t, propertyName, record.getInt(classMapping.getColumnName(propertyName)));
                        } else if (type == Long.class) {
                            ReflectUtil.setFieldValue(t, propertyName, record.getLong(classMapping.getColumnName(propertyName)));
                        } else if (type == Double.class) {
                            ReflectUtil.setFieldValue(t, propertyName, record.getDouble(classMapping.getColumnName(propertyName)));
                        } else if (type == Float.class) {
                            ReflectUtil.setFieldValue(t, propertyName, record.getFloat(classMapping.getColumnName(propertyName)));
                        } else if (type == Date.class) {
                            ReflectUtil.setFieldValue(t, propertyName, record.getDate(classMapping.getColumnName(propertyName)));
                        }
                    } else {
                        if (type == String.class) {
                            ReflectUtil.setFieldValue(t, propertyName, "");
                        }
                    }
                }
                result.add(t);
            }
        } catch (TRSException e) {
            log.error(com.common.core.TRSConstants.SEARCH, e);
        }
        return new HybasePage<>(result, totalRecords, offsetLimit.getPageNum(), offsetLimit.getPageSize());
    }

    private HybasePage<T> select(ServerDTO serverDTO, HybaseQueryWrapper hybaseQueryWrapper, Boolean quickSearch, Boolean nosort, Class<T> clazz) {
        // 创建连接参数
        HybaseConnectParams connectParams = new HybaseConnectParams(serverDTO.getUrl(), serverDTO.getDbName(), serverDTO.getUsername(), serverDTO.getPassword(), 1000);
        // 获取映射
        HybaseClassMapping classMapping = hybaseMapping.getClassMappings().get(hybaseQueryWrapper.getMappingId());
        // 创建查询参数
        SearchParams searchParams = HybaseSearchParams.builder().setReadColumns(classMapping.getColumnNames()).setColorColumns(classMapping.getHighlighterPropertyNames()).setRangeFilter(hybaseQueryWrapper.getQuery()).setQuickSearch(quickSearch).setSortMethod(hybaseQueryWrapper.getHybaseSorts(), classMapping).setNosort(nosort).build();
        log.info("hybase查询参数：{}", searchParams);
        log.info("hybase查询条件：{}", hybaseQueryWrapper.getQuery());
        // 查询
        return select(connectParams, classMapping, searchParams, hybaseQueryWrapper.getQuery(), hybaseQueryWrapper.getOffsetLimit(), clazz);
    }

    @Override
    public Long selectCount(ServerDTO serverDTO, HybaseQueryWrapper hybaseQueryWrapper) {
        long totalRecords = 0;
        HybaseConnectParams connectParams = new HybaseConnectParams(serverDTO.getUrl(), serverDTO.getDbName(), serverDTO.getUsername(), serverDTO.getPassword(), 1000);
        HybaseClassMapping classMapping = hybaseMapping.getClassMappings().get(hybaseQueryWrapper.getMappingId());
        SearchParams searchParams = HybaseSearchParams.builder().setRangeFilter(hybaseQueryWrapper.getQuery()).build();
        log.debug("hybase查询参数：{}", searchParams);
        log.debug("hybase查询条件：{}", hybaseQueryWrapper.getQuery());
        // 获取映射

        // 获取hybase连接
        try (TRSConnection connection = connectParams.createConnection()) {
            // 查询
            TRSResultSet trsResultSet = connection.executeSelect(StrUtil.emptyToDefault(connectParams.getTable(), classMapping.getTable()), hybaseQueryWrapper.getQuery(), 0, 0, searchParams);
            totalRecords = trsResultSet.getNumFound();
        } catch (TRSException e) {
            log.error(com.common.core.TRSConstants.SEARCH, e);
        }
        return totalRecords;
    }

    @Override
    public T selectOne(ServerDTO serverDTO, HybaseQueryWrapper hybaseQueryWrapper, Class<T> clazz) {
        hybaseQueryWrapper.setOffsetLimit(OffsetLimit.UNIQUE);
        HybasePage<T> select = select(serverDTO, hybaseQueryWrapper, clazz);
        if (select.getRecords().size() > 0) {
            return select.getRecords().get(0);
        }
        return null;
    }

    @Override
    public HybasePage<T> select(ServerDTO serverDTO, HybaseQueryWrapper hybaseQueryWrapper, Class<T> clazz) {
        return select(serverDTO, hybaseQueryWrapper, false, false, clazz);
    }

    @Override
    public HybasePage<T> select(ServerDTO serverDTO, HybaseQueryWrapper hybaseQueryWrapper, Class<T> clazz, List<Class<? extends HybasePlugin>> pluginClass) {
        HybasePage<T> select = select(serverDTO, hybaseQueryWrapper, false, false, clazz);
        if (CollUtil.isNotEmpty(pluginClass)) {
            for (Class<? extends HybasePlugin> plugin : pluginClass) {
                try {
                    HybasePlugin hybasePlugin = plugin.newInstance();
                    for (T record : select.getRecords()) {
                        hybasePlugin.plugin(record);
                    }
                } catch (InstantiationException | IllegalAccessException e) {
                    log.error("hybase插件加载失败", e);
                }
            }
        }
        return select;
    }

    @Override
    public HybasePage<T> selectQuick(ServerDTO serverDTO, HybaseQueryWrapper hybaseQueryWrapper, Class<T> clazz) {
        return select(serverDTO, hybaseQueryWrapper, true, false, clazz);
    }

    @Override
    public HybasePage<T> selectNosort(ServerDTO serverDTO, HybaseQueryWrapper hybaseQueryWrapper, Class<T> clazz) {
        return select(serverDTO, hybaseQueryWrapper, false, true, clazz);
    }

    /**
     * hybase分类查询
     *
     * @param connectParams  hybase连接参数
     * @param classMapping   hybase实体类映射
     * @param searchParams   hybase查询参数
     * @param query          hybase查询语句
     * @param categoryColumn 分类字段
     * @param categoryNum    分类数量
     * @return 分类结果
     */
    private Map<String, Long> selectCategory(HybaseConnectParams connectParams, HybaseClassMapping classMapping, SearchParams searchParams, String query, String categoryColumn, Long categoryNum) {
        Map<String, Long> result = new LinkedHashMap<>();
        // 获取hybase连接
        try (TRSConnection connection = connectParams.createConnection()) {
            TRSResultSet trsResultSet = connection.categoryQuery(StrUtil.emptyToDefault(connectParams.getTable(), classMapping.getTable()), query, null, categoryColumn, categoryNum, searchParams);
            Map<String, Long> categoryMap = trsResultSet.getCategoryMap();
            if (ObjectUtil.isNotNull(categoryMap)) {
                result.putAll(categoryMap);
            }
        } catch (TRSException e) {
            log.error(com.common.core.TRSConstants.SEARCH, e);
        }
        return result;
    }

    @Override
    public Map<String, Long> selectCategory(ServerDTO serverDTO, HybaseQueryWrapper hybaseQueryWrapper) {
        // 创建连接参数
        HybaseConnectParams connectParams = new HybaseConnectParams(serverDTO.getUrl(), serverDTO.getDbName(), serverDTO.getUsername(), serverDTO.getPassword(), 1000);
        // 获取映射
        HybaseClassMapping classMapping = hybaseMapping.getClassMappings().get(hybaseQueryWrapper.getMappingId());
        SearchParams searchParams = HybaseSearchParams.builder().build();
        log.debug("hybase查询参数：{}", searchParams);
        log.debug("hybase查询条件：{}", hybaseQueryWrapper.getQuery());
        return selectCategory(connectParams, classMapping, searchParams, hybaseQueryWrapper.getQuery(), hybaseQueryWrapper.getCategoryColumn(), hybaseQueryWrapper.getCategoryNum());
    }


    /**
     * hybase 数据入库方法
     *
     * @param connectParams 连接参数
     * @param classMapping  映射
     * @param beans         数据
     * @param params        参数
     */
    private void insert(HybaseConnectParams connectParams, HybaseClassMapping classMapping, List<T> beans, OperationParams params) {
        // 获取hybase连接
        try (TRSConnection connection = connectParams.createConnection()) {
            // 准备数据结合
            List<TRSInputRecord> records = new ArrayList<>();
            for (T bean : beans) {
                // 创建hybase输入记录
                TRSInputRecord trsInputRecord = new TRSInputRecord();
                // 反射获取字段
                reflectionProcessing(classMapping, records, bean, trsInputRecord);
            }
            // 插入
            TRSReport report = new TRSReport();
            connection.executeInsert(StrUtil.emptyToDefault(connectParams.getTable(), classMapping.getTable()), records, params, report);
        } catch (TRSException e) {
            log.error(com.common.core.TRSConstants.INSERT, e);
        }
    }

    @Override
    public void insert(ServerDTO serverDTO, String mappingId, List<T> beans) {
        // 创建连接参数
        HybaseConnectParams connectParams = new HybaseConnectParams(serverDTO.getUrl(), serverDTO.getDbName(), serverDTO.getUsername(), serverDTO.getPassword(), 1000);
        // 获取映射
        HybaseClassMapping classMapping = hybaseMapping.getClassMappings().get(mappingId);
        // 创建操作参数
        OperationParams operationParams = HybaseOperationParams.builder().setSkipError(false).setDuplicateOverride(true).setDuplicateError(true).build();
        insert(connectParams, classMapping, beans, operationParams);
    }

    @Override
    public void insertSkipError(ServerDTO serverDTO, String mappingId, List<T> beans) {
        // 创建连接参数
        HybaseConnectParams connectParams = new HybaseConnectParams(serverDTO.getUrl(), serverDTO.getDbName(), serverDTO.getUsername(), serverDTO.getPassword(), 1000);
        // 获取映射
        HybaseClassMapping classMapping = hybaseMapping.getClassMappings().get(mappingId);
        // 创建操作参数
        OperationParams operationParams = HybaseOperationParams.builder().setSkipError(true).setDuplicateOverride(true).setDuplicateError(true).build();
        insert(connectParams, classMapping, beans, operationParams);
    }

    @Override
    public void insert(ServerDTO serverDTO, String mappingId, T bean) {
        insert(serverDTO, mappingId, Collections.singletonList(bean));
    }

    /**
     * hybase 数据删除方法根据UUID
     *
     * @param connectParams 连接参数
     * @param classMapping  映射
     * @param uuids         uuid
     */
    private void delete(HybaseConnectParams connectParams, HybaseClassMapping classMapping, List<String> uuids) {
        // 获取hybase连接
        try (TRSConnection connection = connectParams.createConnection()) {
            TRSReport report = new TRSReport();
            connection.executeDelete(StrUtil.emptyToDefault(connectParams.getTable(), classMapping.getTable()), uuids, report);
        } catch (TRSException e) {
            log.error(com.common.core.TRSConstants.DELETE, e);
        }
    }

    @Override
    public void delete(ServerDTO serverDTO, String mappingId, List<String> uuids) {
        // 创建连接参数
        HybaseConnectParams connectParams = new HybaseConnectParams(serverDTO.getUrl(), serverDTO.getDbName(), serverDTO.getUsername(), serverDTO.getPassword(), 1000);

        HybaseClassMapping classMapping = hybaseMapping.getClassMapping(mappingId);
        delete(connectParams, classMapping, uuids);
    }

    /**
     * hybase 数据删除方法根据UUID
     *
     * @param connectParams 连接参数
     * @param classMapping  映射
     * @param uuid          uuid
     */
    private void delete(HybaseConnectParams connectParams, HybaseClassMapping classMapping, String uuid) {
        delete(connectParams, classMapping, Collections.singletonList(uuid));
    }

    @Override
    public void delete(ServerDTO serverDTO, String mappingId, String uuid) {
        // 创建连接参数
        HybaseConnectParams connectParams = new HybaseConnectParams(serverDTO.getUrl(), serverDTO.getDbName(), serverDTO.getUsername(), serverDTO.getPassword(), 1000);

        HybaseClassMapping classMapping = hybaseMapping.getClassMapping(mappingId);
        delete(connectParams, classMapping, Collections.singletonList(uuid));
    }

    /**
     * hybase 数据删除方法根据查询条件
     *
     * @param connectParams 连接参数
     * @param classMapping  映射
     * @param searchParams  查询参数
     * @param query         查询条件
     */
    private void deleteQuery(HybaseConnectParams connectParams, HybaseClassMapping classMapping, SearchParams searchParams, String query) {
        // 获取hybase连接
        try (TRSConnection connection = connectParams.createConnection()) {
            TRSReport report = new TRSReport();
            connection.executeDeleteQuery(StrUtil.emptyToDefault(connectParams.getTable(), classMapping.getTable()), query, searchParams, report);
        } catch (TRSException e) {
            log.error(com.common.core.TRSConstants.DELETE, e);
        }
    }

    @Override
    public void deleteQuery(ServerDTO serverDTO, String mappingId, String query) {
        // 创建连接参数
        HybaseConnectParams connectParams = new HybaseConnectParams(serverDTO.getUrl(), serverDTO.getDbName(), serverDTO.getUsername(), serverDTO.getPassword(), 1000);

        HybaseClassMapping classMapping = hybaseMapping.getClassMapping(mappingId);
        // 创建查询参数
        SearchParams searchParams = HybaseSearchParams.builder().setReadColumns(classMapping.getColumnNames()).setRangeFilter(query).build();
        log.debug("hybase查询参数：{}", searchParams);
        deleteQuery(connectParams, classMapping, searchParams, query);
    }

    /**
     * hybase 数据更新方法
     *
     * @param connectParams 连接参数
     * @param classMapping  映射
     * @param beans         对象
     * @param params        操作参数
     */
    private void update(HybaseConnectParams connectParams, HybaseClassMapping classMapping, List<T> beans, OperationParams params) {
        // 获取hybase连接
        try (TRSConnection connection = connectParams.createConnection()) {
            // 准备数据结合
            List<TRSInputRecord> records = new ArrayList<>();
            for (T bean : beans) {
                // 创建hybase输入记录
                TRSInputRecord trsInputRecord = new TRSInputRecord();
                if (StrUtil.isNotEmpty(classMapping.getUuidPropertyName())) {
                    Object fieldValue = ReflectUtil.getFieldValue(bean, classMapping.getUuidPropertyName());
                    trsInputRecord.setUid(String.valueOf(fieldValue));
                }
                // 反射处理对象映射到hybase输入记录
                reflectionProcessing(classMapping, records, bean, trsInputRecord);
            }
            // 插入
            TRSReport report = new TRSReport();
            connection.executeUpdate(StrUtil.emptyToDefault(connectParams.getTable(), classMapping.getTable()), records, params, report);
        } catch (TRSException e) {
            log.error(com.common.core.TRSConstants.INSERT, e);
        }
    }

    /**
     * hybase insert和update方法反射处理对象映射到hybase输入记录
     *
     * @param classMapping   映射
     * @param records        hybase输入记录集合
     * @param bean           对象
     * @param trsInputRecord hybase输入记录
     * @throws TRSException 异常
     */
    private void reflectionProcessing(HybaseClassMapping classMapping, List<TRSInputRecord> records, T bean, TRSInputRecord trsInputRecord) throws TRSException {
        Field[] fields = ReflectUtil.getFields(bean.getClass());
        for (Field value : fields) {
            // 获取字段映射和字段赋值
            String field = classMapping.getColumnPropertyMap().get(value.getName());
            if (StrUtil.isNotEmpty(field)) {
                Object fieldValue = ReflectUtil.getFieldValue(bean, value.getName());
                trsInputRecord.addColumn(field, fieldValue);
            }
        }
        records.add(trsInputRecord);
    }

    /**
     * hybase 数据更新方法
     *
     * @param serverDTO 服务器管理
     * @param mappingId 映射id
     * @param beans     对象
     * @param params    操作参数
     */
    private void update(ServerDTO serverDTO, String mappingId, List<T> beans, OperationParams params) {
        HybaseConnectParams connectParams = new HybaseConnectParams(serverDTO.getUrl(), serverDTO.getDbName(), serverDTO.getUsername(), serverDTO.getPassword(), 1000);
        HybaseClassMapping classMapping = hybaseMapping.getClassMapping(mappingId);
        update(connectParams, classMapping, beans, params);
    }

    @Override
    public void update(ServerDTO serverDTO, String mappingId, List<T> beans) {
        OperationParams operationParams = HybaseOperationParams.builder().setSkipError(false).build();
        update(serverDTO, mappingId, beans, operationParams);
    }

    @Override
    public void update(ServerDTO serverDTO, String mappingId, T bean) {
        update(serverDTO, mappingId, Collections.singletonList(bean));
    }

    private double selectCategory(HybaseConnectParams connectParams, HybaseClassMapping classMapping,
                                  SearchParams searchParams, String query,
                                  String expression, String field) {

        double totalRecords = 0;
        // 获取hybase连接
        try (TRSConnection connection = connectParams.createConnection()) {
            // 查询
            TRSResultSet trsResultSet = connection.expressionQuery(StrUtil.emptyToDefault(connectParams.getTable(), classMapping.getTable()), query, expression , searchParams);
            totalRecords = trsResultSet.getExpressionResult(field);
        } catch (TRSException e) {
            log.error(com.common.core.TRSConstants.SEARCH, e);
        }
        return totalRecords;
    }
    @Override
    public double expression(ServerDTO serverDTO, HybaseQueryWrapper hybaseQueryWrapper, String expression, String field) {
        // 创建连接参数
        HybaseConnectParams connectParams = new HybaseConnectParams(serverDTO.getUrl(), serverDTO.getDbName(), serverDTO.getUsername(), serverDTO.getPassword(), 1000);
        // 获取映射
        HybaseClassMapping classMapping = hybaseMapping.getClassMappings().get(hybaseQueryWrapper.getMappingId());
        // 创建查询参数
        SearchParams searchParams = HybaseSearchParams.builder().setReadColumns(classMapping.getColumnNames()).setColorColumns(classMapping.getHighlighterPropertyNames()).setRangeFilter(hybaseQueryWrapper.getQuery()).setSortMethod(hybaseQueryWrapper.getHybaseSorts(), classMapping).build();
        log.debug("hybase查询参数：{}", searchParams);
        log.debug("hybase查询条件：{}", hybaseQueryWrapper.getQuery());
        // 查询
        return selectCategory(connectParams, classMapping, searchParams, hybaseQueryWrapper.getQuery(), expression, field);
    }

    @Override
    public double sum(ServerDTO serverDTO, HybaseQueryWrapper hybaseQueryWrapper, String field) {
        // 创建连接参数
        HybaseConnectParams connectParams = new HybaseConnectParams(serverDTO.getUrl(), serverDTO.getDbName(), serverDTO.getUsername(), serverDTO.getPassword(), 1000);
        // 获取映射
        HybaseClassMapping classMapping = hybaseMapping.getClassMappings().get(hybaseQueryWrapper.getMappingId());
        // 创建查询参数
        SearchParams searchParams = HybaseSearchParams.builder().setReadColumns(classMapping.getColumnNames()).setColorColumns(classMapping.getHighlighterPropertyNames()).setRangeFilter(hybaseQueryWrapper.getQuery()).setSortMethod(hybaseQueryWrapper.getHybaseSorts(), classMapping).build();
        log.debug("hybase查询参数：{}", searchParams);
        log.debug("hybase查询条件：{}", hybaseQueryWrapper.getQuery());
        // 查询
        String expression = "SUM(" + field + ")";
        return selectCategory(connectParams, classMapping, searchParams, hybaseQueryWrapper.getQuery(), expression, field);
    }

    @Override
    public double max(ServerDTO serverDTO, HybaseQueryWrapper hybaseQueryWrapper, String field) {
        // 创建连接参数
        HybaseConnectParams connectParams = new HybaseConnectParams(serverDTO.getUrl(), serverDTO.getDbName(), serverDTO.getUsername(), serverDTO.getPassword(), 1000);
        // 获取映射
        HybaseClassMapping classMapping = hybaseMapping.getClassMappings().get(hybaseQueryWrapper.getMappingId());
        // 创建查询参数
        SearchParams searchParams = HybaseSearchParams.builder().setReadColumns(classMapping.getColumnNames()).setColorColumns(classMapping.getHighlighterPropertyNames()).setRangeFilter(hybaseQueryWrapper.getQuery()).setSortMethod(hybaseQueryWrapper.getHybaseSorts(), classMapping).build();
        log.debug("hybase查询参数：{}", searchParams);
        log.debug("hybase查询条件：{}", hybaseQueryWrapper.getQuery());
        // 查询
        String expression = "MAX(" + field + ")";
        return selectCategory(connectParams, classMapping, searchParams, hybaseQueryWrapper.getQuery(), expression, field);
    }

    @Override
    public double min(ServerDTO serverDTO, HybaseQueryWrapper hybaseQueryWrapper, String field) {
        // 创建连接参数
        HybaseConnectParams connectParams = new HybaseConnectParams(serverDTO.getUrl(), serverDTO.getDbName(), serverDTO.getUsername(), serverDTO.getPassword(), 1000);
        // 获取映射
        HybaseClassMapping classMapping = hybaseMapping.getClassMappings().get(hybaseQueryWrapper.getMappingId());
        // 创建查询参数
        SearchParams searchParams = HybaseSearchParams.builder().setReadColumns(classMapping.getColumnNames()).setColorColumns(classMapping.getHighlighterPropertyNames()).setRangeFilter(hybaseQueryWrapper.getQuery()).setSortMethod(hybaseQueryWrapper.getHybaseSorts(), classMapping).build();
        log.debug("hybase查询参数：{}", searchParams);
        log.debug("hybase查询条件：{}", hybaseQueryWrapper.getQuery());
        // 查询
        String expression = "MIN(" + field + ")";
        return selectCategory(connectParams, classMapping, searchParams, hybaseQueryWrapper.getQuery(), expression, field);
    }

}
