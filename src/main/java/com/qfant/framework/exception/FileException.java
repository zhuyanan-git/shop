package com.qfant.framework.exception;

import com.qfant.framework.exception.base.BaseException;

/**
 * 文件信息异常类
 * 
 * @author Liar
 */
public class FileException extends BaseException
{
    private static final long serialVersionUID = 1L;

    public FileException(String code, Object[] args)
    {
        super("file", code, args, null);
    }

}
