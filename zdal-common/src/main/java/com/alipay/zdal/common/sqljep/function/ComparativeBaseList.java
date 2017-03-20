/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.common.sqljep.function;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

/**
 * Comparative List,作用是持有多个Comparative
 * 
 * 是对and 节点和or节点的一种公共抽象。
 * 
 *
 */
public abstract class ComparativeBaseList extends Comparative {

    protected List<Comparative> list = new ArrayList<Comparative>(2);

    public ComparativeBaseList(int function, Comparable<?> value) {
        super(function, value);
        list.add(new Comparative(function, value));
    }

    protected ComparativeBaseList() {
        super();
    }

    public ComparativeBaseList(int capacity) {
        super();
        list = new ArrayList<Comparative>(capacity);
    }

    public ComparativeBaseList(Comparative item) {
        super(item.getComparison(), item.getValue());
        list.add(item);
    }

    public List<Comparative> getList() {
        return list;
    }

    public void addComparative(Comparative item) {
        this.list.add(item);
    }

    public Object clone() {
        try {
            Constructor<? extends ComparativeBaseList> con = this.getClass().getConstructor(
                (Class[]) null);
            ComparativeBaseList compList = con.newInstance((Object[]) null);
            for (Comparative com : list) {
                compList.addComparative((Comparative) com.clone());
            }
            compList.setComparison(this.getComparison());
            compList.setValue(this.getValue());
            return compList;
        } catch (Exception e) {
            //			e.printStackTrace();
            return null;
        }

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        boolean firstElement = true;
        for (Comparative comp : list) {
            if (!firstElement) {
                sb.append(getRelation());
            }
            sb.append(comp.toString());
            firstElement = false;
        }
        return sb.toString();
    }

    abstract protected String getRelation();
}
