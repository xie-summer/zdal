/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.rule.ruleengine.entities.retvalue;

import java.util.Set;

public interface DatabasesAndTables {
    public String getDbIndex();

    public Set<String> getTableNames();
}
