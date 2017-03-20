/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.transaction;

/**
 *  This exception is thrown to report that a heuristic decision was made and
 *  that some some parts of the transaction have been committed while other
 *  parts have been rolled back. 
 *
 *  @version $Revision: 37390 $
 */
public class HeuristicMixedException extends Exception {

    /**
     *  Creates a new <code>HeuristicMixedException</code> without a
     *  detail message.
     */
    public HeuristicMixedException() {
    }

    /**
     *  Constructs an <code>HeuristicMixedException</code> with the
     *  specified detail message.
     *
     *  @param msg the detail message.
     */
    public HeuristicMixedException(String msg) {
        super(msg);
    }
}
