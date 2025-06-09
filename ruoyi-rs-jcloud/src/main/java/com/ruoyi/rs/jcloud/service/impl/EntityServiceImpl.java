/**
 * @Copyright: 李源俊  All rights reserved.
 */
package com.ruoyi.rs.jcloud.service.impl;

import java.lang.reflect.Method;
import java.util.List;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.rs.core.exception.OhBizException;
import com.ruoyi.rs.jcloud.service.EntityService;
import org.springframework.stereotype.Service;


/**
 * 实体通用操作服务
 * @author lyj
 */
@Service("entityService")
@SuppressWarnings("unchecked")
public class EntityServiceImpl implements EntityService {
	
    public Integer countByExample(Class<?> entityClass, Object example) throws Exception{
		return (Integer)callMethod(entityClass, "countByExample", new Class[]{example.getClass()}, new Object[]{example});
	}

    public Integer deleteByExample(Class<?> entityClass, Object example) throws Exception{
		return (Integer)callMethod(entityClass, "deleteByExample", new Class[]{example.getClass()}, new Object[]{example});
	}

    public Integer deleteByPrimaryKey(Class<?> entityClass, Object key){
		try {
			return (Integer)callMethod(entityClass, "deleteByPrimaryKey", new Class[]{key.getClass()}, new Object[]{key});
		} catch (Exception e) {
			throw new OhBizException("deleteByPrimaryKey出现异常，类型：" + entityClass.getName() + "，值：" + key, e);
		}
	}

    public <T> Integer insert(Class<?> entityClass, T record) throws Exception{
		return (Integer)callMethod(entityClass, "insert", new Class[]{record.getClass()}, new Object[]{record});
	}
	
    public <T> Integer insertBatch(Class<?> entityClass, List<T> records) throws Exception{
		return (Integer)callMethod(entityClass, "insertBatch", new Class[]{List.class}, new Object[]{records});
	}

    public <T> Integer insertSelective(Class<?> entityClass, T record) {
		try {
			return (Integer) callMethod(entityClass, "insertSelective", new Class[]{record.getClass()}, new Object[]{record});
		} catch (Exception e) {
			throw new OhBizException("insertSelective出现异常，类型：" + entityClass.getName(), e);
		}
	}

    public <T> List<T> selectByExample(Class<T> entityClass, Object example){
		try {
			return (List<T>)callMethod(entityClass, "selectByExample", new Class[]{example.getClass()}, new Object[]{example});
		} catch (Exception e) {
			throw new OhBizException("selectByExample出现异常，类型：" + entityClass.getName(), e);
		}
	}
	
//    public <T> List<T> selectByExample(Class<T> entityClass, Object example, RowBounds rowBounds) throws Exception{
//		return (List<T>)callMethod(entityClass, "selectByExample", new Class[]{example.getClass(), RowBounds.class}, new Object[]{example, rowBounds});
//	}
	
//	@SuppressWarnings("rawtypes")
//    public <T> PageList<T> selectByExample(Class<T> entityClass, Object example, PageBounds pageBounds) throws Exception{
//
//		PageHelper.startPage(pageBounds.getPage(), pageBounds.getLimit());
//
//		Page<T> page = (Page<T>)callMethod(entityClass, "selectByExample", new Class[]{example.getClass()}, new Object[]{example});
//
//		Paginator paginator = new Paginator(page.getPageNum(), pageBounds.getLimit(), (int) page.getTotal());
//
//		return new PageList(page, paginator);
//	}

    public <T> T selectByPrimaryKey(Class<T> entityClass, Object key) {
		try {
			return (T) callMethod(entityClass, "selectByPrimaryKey", new Class[]{key.getClass()}, new Object[]{key});
		} catch (Exception e) {
			throw new OhBizException("selectByPrimaryKey出现异常，类型：" + entityClass.getName() + "，值：" + key, e);
		}
	}

    public Integer updateByExampleSelective(Class<?> entityClass, Object record, Object example) {
		try {
			return (Integer)callMethod(entityClass, "updateByExampleSelective", new Class[]{record.getClass(), example.getClass()}, new Object[]{record, example});
		} catch (Exception e) {
			throw new OhBizException("updateByExampleSelective出现异常，类型：" + entityClass.getName() + "，值：" + record, e);
		}
	}

    public Integer updateByExample(Class<?> entityClass, Object record, Object example) {
		try {
			return (Integer)callMethod(entityClass, "updateByExample", new Class[]{record.getClass(), example.getClass()}, new Object[]{record, example});
		} catch (Exception e) {
			throw new OhBizException("updateByExample出现异常，类型：" + entityClass.getName() + "，值：" + record, e);
		}
	}

    public Integer updateByPrimaryKeySelective(Class<?> entityClass, Object record) {
		try {
			return (Integer)callMethod(entityClass, "updateByPrimaryKeySelective", new Class[]{record.getClass()}, new Object[]{record});
		} catch (Exception e) {
			throw new OhBizException("updateByPrimaryKeySelective出现异常，类型：" + entityClass.getName() + "，值：" + record, e);
		}
	}

    public Integer updateByPrimaryKey(Class<?> entityClass, Object record){
		try {
			return (Integer)callMethod(entityClass, "updateByPrimaryKey", new Class[]{record.getClass()}, new Object[]{record});
		} catch (Exception e) {
			throw new OhBizException("updateByPrimaryKey出现异常，类型：" + entityClass.getName() + "，值：" + record, e);
		}
	}

	
	/**
	 * 执行函数
	 * @param entityClass
	 * @param methodName
	 * @param paramClass
	 * @param Object
	 * @return
	 * @throws Exception
	 */
	protected Object callMethod(Class<?> entityClass, String methodName, Class<?>[] paramClass, Object[] Object) throws Exception{

		String name = entityClass.getName();

		name = name.replaceFirst(".domain.", ".dao.") + "Mapper";

		Class<?> mapperClass = Class.forName(name);

		if (mapperClass == null) {
			throw new ClassNotFoundException("未找到类：" + name);
		}

		Object mapper;
		try{
			mapper = SpringUtils.getBean(mapperClass);
        }
        catch(Exception e){
        	mapper = SpringUtils.getBean(mapperClass.getSimpleName());
        }

        Method m = mapper.getClass().getMethod(methodName, paramClass);
        
        try{
        	Object result = m.invoke(mapper, Object);
        	return result;
        }
        catch(Exception e){
        	
        	if(e.getMessage() != null) throw e;
        	if(e.getCause() != null) throw new Exception(e.getCause().getMessage());
        }
        
        return null;
	}
	
}