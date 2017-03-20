/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.alipay.zdal.client.config;

import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.util.CollectionUtils;

import com.alipay.zdal.client.config.bean.AppDataSourceBean;
import com.alipay.zdal.client.config.bean.PhysicalDataSourceBean;
import com.alipay.zdal.client.config.bean.ZdalAppBean;
import com.alipay.zdal.client.config.exceptions.ZdalConfigException;
import com.alipay.zdal.common.Constants;

/**
 * <p>
 * A single loader to load Zdal configurations via Web Service or local
 * directory in order to initialize Zdal data source context. Afterward, it
 * holds all zdal configurations across applications distinguish by unique id
 * consist of appName + dbMode + zone
 * </p>
 * 
 * @author <a href="mailto:xiang.yangx@alipay.com">Yang Xiang</a>
 * 
 */
public class ZdalConfigurationLoader {

    private static final Logger                  log          = Logger
                                                                  .getLogger(Constants.CONFIG_LOG_NAME_LOGNAME);

    private static final ZdalConfigurationLoader instance     = new ZdalConfigurationLoader();

    /** 存放app的所有数据源的配置信息，key=appName  value=(key = appDsName,value = zdalconfig )*/
    private Map<String, Map<String, ZdalConfig>> appDsConfigs = new HashMap<String, Map<String, ZdalConfig>>();

    public static ZdalConfigurationLoader getInstance() {
        return instance;
    }

    /**
     * Load Zdal configuration context via Spring XmlApplicationContext when the
     * zdal has not been loaded up. If application's Zdal configuration has been
     * loaded up, just fetch the zdal config from the single configuration map.
     * 
     * @param appName
     * @param dbMode
     * @param idcName
     * @param appDsName
     * @param zdataconsoleUrl
     * @param configPath
     * @return
     */
    public synchronized ZdalConfig getZdalConfiguration(String appName, String dbMode,
                                                        String appDsName, String configPath) {
        Map<String, ZdalConfig> configs = appDsConfigs.get(appName);
        if (configs == null || configs.isEmpty()) {
            Map<String, ZdalConfig> maps = getZdalConfigurationFromLocal(appName, dbMode,
                appDsName, configPath);
            appDsConfigs.put(appName, maps);
            return maps.get(appDsName);
        } else {
            return configs.get(appDsName);
        }
    }

    /**
     * It is pretty much as same as getZdalConfiguration, but it only load
     * configuration from local which given parameter configPath.
     * 
     * @param appName
     * @param dbMode
     * @param idcName
     * @param appDsName
     * @param configPath
     *            Given path to load applications's data source and rule
     *            configuraiton files.
     * @return
     */
    private synchronized Map<String, ZdalConfig> getZdalConfigurationFromLocal(String appName,
                                                                               String dbMode,
                                                                               String appDsName,
                                                                               String configPath) {
        List<String> zdalConfigurationFilePathList = new ArrayList<String>();
        File configurationFile = new File(configPath, MessageFormat.format(
            Constants.LOCAL_CONFIG_FILENAME_SUFFIX, appName, dbMode));
        if (configurationFile.exists() && configurationFile.isFile()) {
            zdalConfigurationFilePathList.add(configurationFile.getAbsolutePath());
        }
        configurationFile = new File(configPath, MessageFormat.format(
            Constants.LOCAL_RULE_CONFIG_FILENAME_SUFFIX, appName, dbMode));
        if (configurationFile.exists() && configurationFile.isFile()) {
            zdalConfigurationFilePathList.add(configurationFile.getAbsolutePath());
        }
        if (zdalConfigurationFilePathList.isEmpty()) {
            throw new ZdalConfigException(
                "ERROR ## There is no local Zdal configuration files for " + appName
                        + " to initialize ZdalDataSource.");
        }
        return loadZdalConfigurationContext(zdalConfigurationFilePathList
            .toArray(new String[zdalConfigurationFilePathList.size()]), appName, dbMode);
    }

    /**
     * 
     * @param fileNames
     * @param appName
     * @param dbMode
     * @param idcName
     */
    private synchronized Map<String, ZdalConfig> loadZdalConfigurationContext(String[] fileNames,
                                                                              String appName,
                                                                              String dbMode) {
        Map<String, ZdalConfig> zdalConfigMap = new HashMap<String, ZdalConfig>();
        try {
            FileSystemXmlApplicationContext ctx = new FileSystemXmlApplicationContext(fileNames);
            if (null == ctx.getBean(appName)) {
                throw new ZdalConfigException("ERROR ## It must has at least one app bean in "
                                              + appName + " Zdal configuration file ");
            }
            ZdalAppBean appBean = (ZdalAppBean) ctx.getBean(appName);
            if (CollectionUtils.isEmpty(appBean.getAppDataSourceList())) {
                throw new ZdalConfigException(
                    "ERROR ## It must has at least one appDataSource bean in " + appName
                            + " ZdalAppBean");
            }
            if (!appName.equals(appBean.getAppName())) {
                throw new ZdalConfigException("ERROR ## the configured appName is "
                                              + appBean.getAppName()
                                              + " are not match with requested appName:" + appName);
            }
            if (!dbMode.equals(appBean.getDbmode())) {
                throw new ZdalConfigException("ERROR ## The configured dbMode is "
                                              + appBean.getDbmode()
                                              + " are not match with requested dbMode: " + dbMode);
            }

            for (AppDataSourceBean appDataSourceBean : appBean.getAppDataSourceList()) {
                zdalConfigMap.put(appDataSourceBean.getAppDataSourceName(), populateZdalConfig(
                    appBean, appDataSourceBean));
            }
            return zdalConfigMap;
        } catch (Exception e) {
            StringBuilder stb = new StringBuilder();
            stb.append("Error### Zdal failed to load Zdal datasource and rule context with files ");
            for (String fileName : fileNames) {
                stb.append(fileName).append(", ");
            }
            stb.append(" when Zdal was loading them.");
            log.error(stb.toString(), e);
            throw new ZdalConfigException(e);
        }
    }

    /**
     * 
     * @param appBean
     * @param appDataSourceBean
     * @return
     */
    protected ZdalConfig populateZdalConfig(ZdalAppBean appBean, AppDataSourceBean appDataSourceBean) {
        ZdalConfig config = new ZdalConfig();
        config.setAppName(appBean.getAppName());
        config.setDbmode(appBean.getDbmode());
        config.setAppDsName(appDataSourceBean.getAppDataSourceName());
        config.setDbType(appDataSourceBean.getDbType());
        config.setDataSourceConfigType(appDataSourceBean.getDataSourceConfigType());
        // Set Rule
        config.setAppRootRule(appDataSourceBean.getAppRule());
        for (PhysicalDataSourceBean physicalDataSource : appDataSourceBean
            .getPhysicalDataSourceSet()) {
            config.getDataSourceParameters().put(physicalDataSource.getName(),
                DataSourceParameter.valueOf(physicalDataSource));
            if (null != physicalDataSource.getLogicDbNameSet()
                && !physicalDataSource.getLogicDbNameSet().isEmpty()) {
                for (String logicDBName : physicalDataSource.getLogicDbNameSet()) {
                    config.getLogicPhysicsDsNames().put(logicDBName, physicalDataSource.getName());
                }
            }
        }
        config.setGroupRules(appDataSourceBean.getGroupDataSourceRuleMap());
        config.setFailoverRules(appDataSourceBean.getFailOverGroupRuleMap());

        return config;
    }
}
