package com.ruoyi.system.mapper;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public interface PublicSqlMapper {

    @Select("${nativeSql}")
    Object nativeSql(@Param("nativeSql") String nativeSql);

    /**
     * 通用查询
     *
     * @return
     */
    @Select("${sql}")
    List<LinkedHashMap<String, Object>> select(Map<String, Object> map);

    /**
     * 新增
     *
     * @param map
     * @return
     */
    @Insert("${sql}")
    int insert(Map<String, Object> map);

    /**
     * 修改
     *
     * @param map
     * @return
     */
    @Update("${sql}")
    int update(Map<String, Object> map);

    /**
     * 删除
     *
     * @param map
     * @return
     */
    @Delete("${sql}")
    int delete(Map<String, Object> map);
}

