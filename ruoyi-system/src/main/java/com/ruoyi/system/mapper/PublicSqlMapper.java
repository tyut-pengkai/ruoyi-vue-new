package com.ruoyi.system.mapper;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;
import java.util.List;

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
    <T> List<LinkedHashMap<String, T>> select(@Param("sql") String sql, Class<T> clazz);

    /**
     * 通用查询
     *
     * @return
     */
    @Select("${sql}")
    <T> List<T> select2(@Param("sql") String sql, Class<T> clazz);

    /**
     * 新增
     *
     * @param map
     * @return
     */
    @Insert("${sql}")
    int insert(@Param("sql") String sql);

    /**
     * 修改
     *
     * @param map
     * @return
     */
    @Update("${sql}")
    int update(@Param("sql") String sql);

    /**
     * 删除
     *
     * @param map
     * @return
     */
    @Delete("${sql}")
    int delete(@Param("sql") String sql);
}

