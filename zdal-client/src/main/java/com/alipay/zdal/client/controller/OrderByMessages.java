/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.client.controller;

import java.util.List;

import com.alipay.zdal.parser.visitor.OrderByEle;

public interface OrderByMessages {

    public abstract List<OrderByEle> getOrderbyList();

}