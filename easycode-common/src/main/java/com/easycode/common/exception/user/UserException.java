package com.easycode.common.exception.user;

import com.easycode.common.exception.base.BaseException;

/**
 * 用户信息异常类
 * 
 * @author easycode
 */
public class UserException extends BaseException
{
    private static final long serialVersionUID = 1L;

    public UserException(String code, Object[] args)
    {
        super("user", code, args, null);
    }
}
