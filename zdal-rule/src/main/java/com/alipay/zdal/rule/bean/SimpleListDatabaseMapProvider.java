/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.rule.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alipay.zdal.rule.ruleengine.entities.convientobjectmaker.DatabaseMapProvider;

public class SimpleListDatabaseMapProvider implements DatabaseMapProvider {
    List<String> datasourceKeys = new ArrayList<String>();

    public Map<String, Database> getDatabaseMap() {
        Map<String, Database> returnedMap = new HashMap<String, Database>();
        int index = 0;
        if (datasourceKeys == null) {
            return Collections.emptyMap();
        }
        for (String str : datasourceKeys) {
            Database db = new Database();
            db.setDataSourceKey(str);
            returnedMap.put(String.valueOf(index), db);
            index++;
        }
        return returnedMap;
    }

    public List<String> getDatasourceKeys() {
        return datasourceKeys;
    }

    public void setDatasourceKeys(List<String> datasourceKeys) {
        this.datasourceKeys = datasourceKeys;
    }

}
