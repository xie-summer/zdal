/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.common.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * 用户获取javabean对象中各种数据的工具类.
 * @author 伯牙
 * @version $Id: BeanUtils.java, v 0.1 2012-11-17 下午4:52:07 Exp $
 */
public class BeanUtils {
    /**
     * Convenience method to instantiate a class using its no-arg constructor.
     * As this method doesn't try to load classes by name, it should avoid
     * class-loading issues.
     * <p>Note that this method tries to set the constructor accessible
     * if given a non-accessible (that is, non-public) constructor.
     * @param clazz class to instantiate
     * @return the new instance
     * @throws BeanInstantiationException if the bean cannot be instantiated
     */
    public static <T> T instantiateClass(Class<T> clazz) throws Throwable {
        if (clazz == null) {
            throw new IllegalArgumentException("ERROR ## the clazz is null");
        }
        if (clazz.isInterface()) {
            throw new IllegalArgumentException(clazz + " Specified class is an interface");
        }
        return instantiateClass(clazz.getDeclaredConstructor());
    }

    /**
     * Convenience method to instantiate a class using the given constructor.
     * As this method doesn't try to load classes by name, it should avoid
     * class-loading issues.
     * <p>Note that this method tries to set the constructor accessible
     * if given a non-accessible (that is, non-public) constructor.
     * @param ctor the constructor to instantiate
     * @param args the constructor arguments to apply
     * @return the new instance
     * @throws BeanInstantiationException if the bean cannot be instantiated
     */
    public static <T> T instantiateClass(Constructor<T> ctor, Object... args) throws Throwable {
        if (ctor == null) {
            throw new IllegalArgumentException("ERROR ## the ctor is null");
        }
        try {
            ReflectionUtils.makeAccessible(ctor);
            return ctor.newInstance(args);
        } catch (InstantiationException ex) {
            throw new Throwable(ctor.getDeclaringClass() + " Is it an abstract class?", ex);
        } catch (IllegalAccessException ex) {
            throw new Throwable(ctor.getDeclaringClass() + " Is the constructor accessible?", ex);
        } catch (IllegalArgumentException ex) {
            throw new Throwable(ctor.getDeclaringClass() + " Illegal arguments for constructor", ex);
        } catch (InvocationTargetException ex) {
            throw new Throwable(ctor.getDeclaringClass() + "Constructor threw exception",
                ex.getTargetException());
        }
    }

}
