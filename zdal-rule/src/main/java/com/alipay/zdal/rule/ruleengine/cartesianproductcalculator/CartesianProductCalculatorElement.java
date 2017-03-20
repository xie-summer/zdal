/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.rule.ruleengine.cartesianproductcalculator;

import java.util.Iterator;
import java.util.Set;

public class CartesianProductCalculatorElement implements Parent {
    Parent            parent;
    final Set<Object> elementOfCartesianProductCalculation;
    Iterator<Object>  iteratorOfCartesianProductCalculation;
    Object            currentObject;

    public CartesianProductCalculatorElement(Parent listener,
                                             Set<Object> elementOfCartesianProductCalculation) {
        this.parent = listener;
        this.elementOfCartesianProductCalculation = elementOfCartesianProductCalculation;
    }

    public Object getCurrentObject() {
        return currentObject;
    }

    public void setParent(Parent parent) {
        this.parent = parent;
    }

    public Parent getParent() {
        return parent;
    }

    public Set<Object> getElementOfCartesianProductCalculation() {
        return elementOfCartesianProductCalculation;
    }

    public boolean hasNext() {
        if (elementOfCartesianProductCalculation == null) {
            return parent.parentHasNext();
        }
        //如果第一次进入element的hasNext方法。则初始化一下
        if (iteratorOfCartesianProductCalculation == null) {
            iteratorOfCartesianProductCalculation = elementOfCartesianProductCalculation.iterator();
        }

        if (iteratorOfCartesianProductCalculation.hasNext()) {
            //当前节点还有next值的话，直接回答有值
            return true;
        } else {
            //没有next值的话，询问parent是否有next值。
            return parent.parentHasNext();
        }
    }

    /**
     * 初始化的逻辑和next逻辑基本相同，但不需要通知parent进位
     */
    public void init() {
        if (elementOfCartesianProductCalculation == null) {
            currentObject = null;
            return;
        }
        //如果当前的list就有next值，则使用当前list的next值
        if (iteratorOfCartesianProductCalculation.hasNext()) {
            currentObject = iteratorOfCartesianProductCalculation.next();
        } else {
            //否则通知parent要进位了，然后自己进行重置。
            iteratorOfCartesianProductCalculation = elementOfCartesianProductCalculation.iterator();
            if (iteratorOfCartesianProductCalculation.hasNext()) {
                currentObject = iteratorOfCartesianProductCalculation.next();
            } else {
                currentObject = null;
            }
        }
    }

    public Object next() {
        if (iteratorOfCartesianProductCalculation == null) {
            parent.add();
            return currentObject;
        }
        //如果当前的list就有next值，则使用当前list的next值
        if (iteratorOfCartesianProductCalculation.hasNext()) {
            currentObject = iteratorOfCartesianProductCalculation.next();
        } else {
            //否则通知parent要进位了，然后自己进行重置。
            iteratorOfCartesianProductCalculation = elementOfCartesianProductCalculation.iterator();
            parent.add();
            if (iteratorOfCartesianProductCalculation.hasNext()) {
                currentObject = iteratorOfCartesianProductCalculation.next();
            } else {
                currentObject = null;
            }
        }
        return currentObject;
    }

    public void add() {
        next();
    }

    public void remove() {
        throw new IllegalStateException("shold not be here");
    }

    public boolean parentHasNext() {
        return hasNext();
    }

    @Override
    public String toString() {
        return "current Obj:" + currentObject + "elementOfCartesianProductCalculation:"
               + elementOfCartesianProductCalculation;
    }

}
