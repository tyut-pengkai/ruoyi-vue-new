package com.ruoyi.common.utils.bean;

import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.SimpleEntity;
import com.ruoyi.common.core.domain.XktBaseEntity;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.Set;

/**
 * bean对象属性验证
 * 
 * @author ruoyi
 */
public class BeanValidators
{
    public static void validateWithException(Validator validator, Object object, Class<?>... groups)
            throws ConstraintViolationException
    {
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object, groups);
        if (!constraintViolations.isEmpty())
        {
            throw new ConstraintViolationException(constraintViolations);
        }
    }

    public static <T extends SimpleEntity> boolean exists(T entity) {
        return entity != null && Constants.UNDELETED.equals(entity.getDelFlag());
    }

    public static <T extends XktBaseEntity> boolean exists(T entity) {
        return entity != null && Constants.UNDELETED.equals(entity.getDelFlag());
    }
}
