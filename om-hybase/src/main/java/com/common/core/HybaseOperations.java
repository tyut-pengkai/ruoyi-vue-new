package com.common.core;

import com.common.domain.dto.ServerDTO;
import com.common.domain.HybaseBean;
import com.common.plugins.HybasePlugin;
import com.common.query.HybasePage;
import com.common.query.HybaseQueryWrapper;

import java.util.List;
import java.util.Map;

/**
 * hybase操作接口
 *
 * @param <T> 返回结果类型
 */
public interface HybaseOperations<T extends HybaseBean> {

    /**
     * hybase基础计数查询方法
     *
     * @param serverDTO    hybase连接实体
     * @param hybaseQueryWrapper hybase查询参数
     * @return 查询结果
     */
    Long selectCount(ServerDTO serverDTO, HybaseQueryWrapper hybaseQueryWrapper);

    /**
     * hybase基础查询方法
     *
     * @param serverDTO    hybase连接实体
     * @param hybaseQueryWrapper hybase查询参数
     * @param clazz              返回结果类型
     * @return 查询结果
     */
    T selectOne(ServerDTO serverDTO, HybaseQueryWrapper hybaseQueryWrapper, Class<T> clazz);

    /**
     * hybase基础查询方法
     *
     * @param serverDTO    hybase连接实体
     * @param hybaseQueryWrapper hybase查询参数
     * @param clazz              返回结果类型
     * @return 查询结果
     */
    HybasePage<T> select(ServerDTO serverDTO, HybaseQueryWrapper hybaseQueryWrapper, Class<T> clazz);

    /**
     * hybase基础查询方法
     *
     * @param serverDTO    hybase连接实体
     * @param hybaseQueryWrapper hybase查询参数
     * @param clazz              返回结果类型
     * @param pluginClass        插件类
     * @return 查询结果
     */
    HybasePage<T> select(ServerDTO serverDTO, HybaseQueryWrapper hybaseQueryWrapper, Class<T> clazz, List<Class<? extends HybasePlugin>> pluginClass);

    /**
     * hybase快速查询方法（只查询3个子库）
     *
     * @param serverDTO    hybase连接实体
     * @param hybaseQueryWrapper hybase查询参数
     * @param clazz              返回结果类型
     * @return 查询结果
     */
    HybasePage<T> selectQuick(ServerDTO serverDTO, HybaseQueryWrapper hybaseQueryWrapper, Class<T> clazz);

    /**
     * hybase无序查询方法，可以查询1W条以上数据
     *
     * @param serverDTO    hybase连接实体
     * @param hybaseQueryWrapper hybase查询参数
     * @param clazz              返回结果类型
     * @return 查询结果
     */
    HybasePage<T> selectNosort(ServerDTO serverDTO, HybaseQueryWrapper hybaseQueryWrapper, Class<T> clazz);

    /**
     * hybase分类统计查询方法
     *
     * @param serverDTO    hybase连接实体
     * @param hybaseQueryWrapper hybase查询参数
     * @return 查询结果
     */
    Map<String, Long> selectCategory(ServerDTO serverDTO, HybaseQueryWrapper hybaseQueryWrapper);

    /**
     * hybase 数据入库方法
     *
     * @param serverDTO hybase连接实体
     * @param mappingId       映射id
     * @param beans           数据
     */
    void insert(ServerDTO serverDTO, String mappingId, List<T> beans);


    void insertSkipError(ServerDTO serverDTO, String mappingId, List<T> beans);

    /**
     * hybase 数据入库方法
     *
     * @param serverDTO hybase连接实体
     * @param mappingId       映射id
     * @param bean            数据
     */
    void insert(ServerDTO serverDTO, String mappingId, T bean);

    /**
     * hybase 数据删除方法
     *
     * @param serverDTO hybase连接实体
     * @param mappingId       映射id
     * @param uuids           uuid集合
     */
    void delete(ServerDTO serverDTO, String mappingId, List<String> uuids);

    /**
     * hybase 数据删除方法
     *
     * @param serverDTO hybase连接实体
     * @param mappingId       映射id
     * @param uuid            uuid
     */
    void delete(ServerDTO serverDTO, String mappingId, String uuid);

    /**
     * hybase 数据删除方法
     *
     * @param serverDTO hybase连接实体
     * @param mappingId       映射id
     * @param query           查询语句
     */
    void deleteQuery(ServerDTO serverDTO, String mappingId, String query);

    /**
     * hybase 数据批量更新方法
     *
     * @param serverDTO hybase连接实体
     * @param mappingId       映射id
     * @param beans           数据
     */
    void update(ServerDTO serverDTO, String mappingId, List<T> beans);

    /**
     * hybase 数据更新方法
     *
     * @param serverDTO hybase连接实体
     * @param mappingId       映射id
     * @param bean            数据
     */
    void update(ServerDTO serverDTO, String mappingId, T bean);

    /**
     *  hybase 统计检索 用于对检索结果进行数值统计，目前支持： SUM  MAX  MIN
     * @param serverDTO
     * @param hybaseQueryWrapper
     * @param expression
     * @param field
     * @return
     */
    double expression(ServerDTO serverDTO, HybaseQueryWrapper hybaseQueryWrapper, String expression, String field);

    double sum(ServerDTO serverDTO, HybaseQueryWrapper hybaseQueryWrapper, String field);

    double max(ServerDTO serverDTO, HybaseQueryWrapper hybaseQueryWrapper, String field);

    double min(ServerDTO serverDTO, HybaseQueryWrapper hybaseQueryWrapper, String field);
}
