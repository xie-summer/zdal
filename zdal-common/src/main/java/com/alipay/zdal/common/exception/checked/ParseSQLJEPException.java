/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.common.exception.checked;

/**
 * 
 * @author 伯牙
 * @version $Id: ParseSQLJEPException.java, v 0.1 2014-1-6 下午05:18:09 Exp $
 */
public class ParseSQLJEPException extends ZdalCheckedExcption {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 7724677712426352259L;

    public ParseSQLJEPException(Throwable th) {
        super("调用sqlJep的parseExpression的时候发生错误" + th.getMessage());
    }

}
