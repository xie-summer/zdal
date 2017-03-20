/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.alipay.zdal.client.config.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;

import com.alipay.zdal.common.lang.StringUtil;

/**
 * @author <a href="mailto:xiang.yangx@alipay.com">Yang Xiang</a>
 *
 */
public class ZdalAppBean implements InitializingBean {

    /** 应用名称. */
    private String                  appName;

    /**数据库环境.  */
    private String                  dbmode;

    /** 数据源列表. */
    private List<AppDataSourceBean> appDataSourceList = new ArrayList<AppDataSourceBean>();

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getDbmode() {
        return dbmode;
    }

    public void setDbmode(String dbmode) {
        this.dbmode = dbmode;
    }

    public List<AppDataSourceBean> getAppDataSourceList() {
        return appDataSourceList;
    }

    public void setAppDataSourceList(List<AppDataSourceBean> appDataSourceList) {
        this.appDataSourceList = appDataSourceList;
    }

    /* (non-Javadoc)
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        if (StringUtil.isBlank(appName)) {
            throw new IllegalArgumentException("ERROR ## the appName is null");
        }
        if (StringUtil.isBlank(dbmode)) {
            throw new IllegalArgumentException("ERROR ## the dbmode is null of " + appName);
        }
        if (appDataSourceList == null || appDataSourceList.isEmpty()) {
            throw new IllegalArgumentException("ERROR ## the appDataSource is empty of " + appName);
        } else {
            //校验是否有同名的appDataSourceName.
            Map<String, AppDataSourceBean> tmps = new HashMap<String, AppDataSourceBean>();
            for (AppDataSourceBean bean : appDataSourceList) {
                tmps.put(bean.getAppDataSourceName(), bean);
            }
            if (tmps.size() != appDataSourceList.size()) {
                throw new IllegalArgumentException(
                    "ERROR ## the appDataSourceList has same appDataSourceName of " + appName);
            }
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((appDataSourceList == null) ? 0 : appDataSourceList.hashCode());
        result = prime * result + ((appName == null) ? 0 : appName.hashCode());
        result = prime * result + ((dbmode == null) ? 0 : dbmode.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ZdalAppBean other = (ZdalAppBean) obj;
        if (appDataSourceList == null) {
            if (other.appDataSourceList != null)
                return false;
        } else if (!appDataSourceList.equals(other.appDataSourceList))
            return false;
        if (appName == null) {
            if (other.appName != null)
                return false;
        } else if (!appName.equals(other.appName))
            return false;
        if (dbmode == null) {
            if (other.dbmode != null)
                return false;
        } else if (!dbmode.equals(other.dbmode))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "ZdalAppBean [appDataSourceList=" + appDataSourceList + ", appName=" + appName
               + ", dbmode=" + dbmode + "]";
    }

}
