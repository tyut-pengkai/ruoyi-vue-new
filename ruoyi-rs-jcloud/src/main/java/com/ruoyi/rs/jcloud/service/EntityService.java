/**
 * @Copyright: 李源俊  All rights reserved.
 */
package com.ruoyi.rs.jcloud.service;

import java.util.List;

/**
 * 实体通用操作接口
 * @author lyj
 */
public interface EntityService {
	
	Integer countByExample(Class<?> entityClass, Object example) throws Exception;

    Integer deleteByExample(Class<?> entityClass, Object example) throws Exception;

    Integer deleteByPrimaryKey(Class<?> entityClass, Object key) throws Exception;

    <T> Integer insert(Class<?> entityClass, T record) throws Exception;
	
	<T> Integer insertBatch(Class<?> entityClass, List<T> records) throws Exception;

	<T> Integer insertSelective(Class<?> entityClass, T record) throws Exception;

	<T> List<T> selectByExample(Class<T> entityClass, Object example);
	
    <T> T selectByPrimaryKey(Class<T> entityClass, Object key) throws Exception;

    Integer updateByExampleSelective(Class<?> entityClass, Object record, Object example) throws Exception;

    Integer updateByExample(Class<?> entityClass, Object record, Object example) throws Exception;

    Integer updateByPrimaryKeySelective(Class<?> entityClass, Object record) throws Exception;

    Integer updateByPrimaryKey(Class<?> entityClass, Object record) throws Exception;

}
