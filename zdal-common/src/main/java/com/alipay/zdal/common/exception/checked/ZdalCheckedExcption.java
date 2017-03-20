/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.common.exception.checked;

/**
 * 
 * @author ²®ÑÀ
 * @version $Id: ZdalCheckedExcption.java, v 0.1 2014-1-6 ÏÂÎç05:18:14 Exp $
 */
public class ZdalCheckedExcption extends Exception {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -1186363001286203116L;

    public ZdalCheckedExcption() {
        super();
    }

    public ZdalCheckedExcption(Throwable throwable) {
        super(throwable);
    }

    public ZdalCheckedExcption(String message, Throwable cause) {
        super(message, cause);
    }

    public ZdalCheckedExcption(String arg) {
        super(arg);
    }
}
