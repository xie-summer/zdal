/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.rule.ruleengine.entities.retvalue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 专门用于分类收集当前sql中含有的主键，分库键和分表键并统一保存的对象
 * 
 *
 */
public class PartitionElement {
    /**
     * sql中含有分库键的时候，这个set不为空
     */
    private List<Set<String>> db            = new ArrayList<Set<String>>();
    /**
     * sql中的分表键的key
     */
    private List<Set<String>> tab           = new ArrayList<Set<String>>();
    /**
     * sql中的主键
     */
    private List<Set<String>> uniqueColumns = new ArrayList<Set<String>>();

    public PartitionElement() {
        db.add(new HashSet<String>());
        tab.add(new HashSet<String>());
        uniqueColumns.add(new HashSet<String>());
    }

    @Deprecated
    public void addDBFirstElement(String str) {
        PartitionElementUtils.add(db, str);
    }

    @Deprecated
    public void addAllDBFirstElement(Set<String> set) {
        PartitionElementUtils.addAll(db, set);
    }

    @Deprecated
    public void addPKFirstElement(String str) {
        PartitionElementUtils.add(uniqueColumns, str);
    }

    @Deprecated
    public void addAllPKFirstElement(Set<String> set) {
        PartitionElementUtils.addAll(uniqueColumns, set);
    }

    @Deprecated
    public void addTabFirstElement(String str) {
        PartitionElementUtils.add(tab, str);
    }

    @Deprecated
    public void addAllTabFirstElement(Set<String> set) {
        PartitionElementUtils.addAll(tab, set);
    }

    @Deprecated
    public Set<String> getDbFirstElement() {
        if (db.size() == 1) {
            return db.get(0);
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Deprecated
    public Set<String> getTabFirstElement() {
        if (tab.size() == 1) {
            return tab.get(0);
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Deprecated
    public Set<String> getPkFirstElement() {
        if (uniqueColumns.size() == 1) {
            return uniqueColumns.get(0);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public List<Set<String>> getUniqueColumns() {
        return uniqueColumns;
    }

    public List<Set<String>> getDb() {
        return db;
    }

    public List<Set<String>> getTab() {
        return tab;
    }

    public void setDb(List<Set<String>> db) {
        if (db != null)
            this.db = db;
    }

    public void setTab(List<Set<String>> tab) {
        if (tab != null)
            this.tab = tab;
    }

    public void setUniqueColumns(List<Set<String>> uniqueColumns) {
        if (uniqueColumns != null)
            this.uniqueColumns = uniqueColumns;
    }

}
