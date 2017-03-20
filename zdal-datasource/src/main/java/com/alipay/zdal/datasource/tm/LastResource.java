/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.datasource.tm;

/**
 * A tagging interface to identify an XAResource that does
 * not support prepare and should be used in the last resource
 * gambit. i.e. It is committed after the resources are
 * prepared. If it fails to commit, roll everybody back.
 * 
 * 
 * @author ²®ÑÀ
 * @version $Id: LastResource.java, v 0.1 2014-1-6 ÏÂÎç05:47:35 Exp $
 */
public interface LastResource {
}
