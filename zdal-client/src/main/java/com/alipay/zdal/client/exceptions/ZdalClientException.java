/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.client.exceptions;

/**
 * ZdalClient的异常处理类.
 * @author xiaoqing.zhouxq
 * @version $Id: ZdalClientException.java, v 0.1 2012-7-31 下午07:01:16 xiaoqing.zhouxq Exp $
 */
public class ZdalClientException extends RuntimeException {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public ZdalClientException(String cause) {
        super(cause);
    }

    public ZdalClientException(Throwable t) {
        super(t);
    }

    public ZdalClientException(String cause, Throwable t) {
        super(cause, t);
    }
}
