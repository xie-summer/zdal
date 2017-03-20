package com.alipay.zdal.common.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * ThreadLocal Context
 * 
 * @author 伯牙
 * @version $Id: NestThreadLocalMap.java, v 0.1 2014-1-6 下午05:22:59 Exp $
 */
public class NestThreadLocalMap {
    private static final Logger                             log           = Logger
                                                                              .getLogger(NestThreadLocalMap.class);
    protected final static ThreadLocal<Map<Object, Object>> threadContext = new MapThreadLocal();

    private NestThreadLocalMap() {
    };

    public static void put(Object key, Object value) {
        getContextMap().put(key, value);
    }

    public static Object remove(Object key) {
        return getContextMap().remove(key);
    }

    public static Object get(Object key) {
        return getContextMap().get(key);
    }

    public static boolean containsKey(Object key) {
        return getContextMap().containsKey(key);
    }

    private static class MapThreadLocal extends ThreadLocal<Map<Object, Object>> {
        protected Map<Object, Object> initialValue() {
            return new HashMap<Object, Object>() {

                private static final long serialVersionUID = 3637958959138295593L;

                public Object put(Object key, Object value) {
                    if (log.isDebugEnabled()) {
                        if (containsKey(key)) {
                            log.debug("Overwritten attribute to thread context: " + key + " = "
                                      + value);
                        } else {
                            log.debug("Added attribute to thread context: " + key + " = " + value);
                        }
                    }

                    return super.put(key, value);
                }
            };
        }
    }

    /**
     * 取得thread context Map的实例。
     *
     * @return thread context Map的实例
     */
    protected static Map<Object, Object> getContextMap() {
        return (Map<Object, Object>) threadContext.get();
    }

    /**
     * 清理线程所有被hold住的对象。以便重用！
     */

    public static void reset() {
        getContextMap().clear();
    }
}
